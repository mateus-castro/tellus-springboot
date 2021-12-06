package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.*;
import br.com.bandtec.tellusspringboot.handlers.GerenteHandler;
import br.com.bandtec.tellusspringboot.repositories.*;
import br.com.bandtec.tellusspringboot.services.HashService;
import br.com.bandtec.tellusspringboot.utils.Util;
import br.com.bandtec.tellusspringboot.utils.hash.HashTable;
import com.amazonaws.services.s3.AmazonS3Client;
import io.swagger.annotations.*;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.spring.web.json.Json;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/gerente")
@Api(value = "Question")

public class GerenteController {

    @Autowired
    private GerenteRepository repositoryGerente;

    @Autowired
    private ResponsavelRepository respRepo;

    @Autowired
    private AlunoRepository alunoRepo;

    @Autowired
    private ContratoRepository contratoRepo;

    @Autowired
    private EscolaRepository escolaRepo;

    @Autowired
    private PagamentoRepository pgtoRepo;

    @ApiOperation(value = "Retorna todos o gerentes referentes a uma escola.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista de gerentes."),
            @ApiResponse(code = 204, message = "Não existe nenhum gerente.")
    })
    @CrossOrigin
    @GetMapping
    public ResponseEntity getGerentesByEscola(
            @ApiParam(
                    name =  "cnpj",
                    type = "String",
                    value = "CNPJ da escola",
                    example = "60.829.806/0001-19",
                    required = true
            )
            @RequestParam("cnpj") String cnpj
    ) {
        Escola escola = escolaRepo.findByCnpj(cnpj);
        List<Gerente> lista = repositoryGerente.findGerentesByFkEscola(escola);
        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(lista);
        }
    }

    @ApiOperation(value = "Insere um gerente no banco.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Insere o gerente no banco e não retorna nada."),
            @ApiResponse(code = 204, message = "Não insere gerente no banco e retorna mensagem de erro."),
            @ApiResponse(code = 400, message = "Requisição inválida")
    })
    @CrossOrigin
    @PostMapping
    public ResponseEntity postGerente(@RequestBody Gerente gerente) {
        String valCpf = Util.formataCpf(gerente.getCpf());
        if(!valCpf.equals("")) {
            if (repositoryGerente.existsByCpf(valCpf)) {
                System.out.println("[postGerente] Gerente já é cadastrado");
                return ResponseEntity.status(409).body("Gerente já foi cadastrado");
            } else {
                repositoryGerente.save(gerente);
                return ResponseEntity.status(201).build();
            }
        } else {
            System.out.println("[postGerente] Parâmetro inválido");
            return ResponseEntity.status(400).build();
        }
    }

    @ApiOperation(value = "Deleta um gerente do banco.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleta o gerente."),
            @ApiResponse(code = 404, message = "Não insere gerente no banco e retorna mensagem de erro.")
    })
    @CrossOrigin
    @DeleteMapping
    public ResponseEntity deleteGerente(
            @ApiParam(name =  "cpf", type = "String", value = "CPF do Gerente", example = "515.973.188-17", required = true)
            @RequestParam("cpf") String cpf
    ) {
        if (repositoryGerente.existsByCpf(cpf)) {
            repositoryGerente.deleteByCpf(cpf);
            return ResponseEntity.status(200).body("Gerente deletado com sucesso");
        }
        System.out.println("[deleteGerente] Gerente com o cpf " + cpf + " não foi encontrado");
        return ResponseEntity.status(404).build();
    }

    @ApiOperation(value = "Faz um cadastro massivo via .csv.")
    @CrossOrigin
    @PostMapping("/csv-download")
    public ResponseEntity<String> CadastroMassivo(
            @ApiParam(name =  "file", type = "MultipartFile", value = "Arquivo .csv que contém os dados a serem inseridos", example = "teste.csv", required = true)
            @RequestParam("file") MultipartFile file,
            @ApiParam(name =  "cpf", type = "String", value = "CPF do Gerente", example = "515.973.188-17", required = true)
            @RequestParam("cpf") String cpfGerente) throws IOException {
        if(!Objects.equals(file.getContentType(), "text/csv") && !Objects.equals(file.getContentType(), "application/vnd.ms-excel")){
            return ResponseEntity.status(400).body("Arquivo enviado não está no formato correto. Envie um arquivo .cs.");
        }

        if(repositoryGerente.existsByCpf(cpfGerente)) {
            byte[] fileBytes = file.getBytes();
            String arquivo = new String(fileBytes);
            ArrayList<String> response = new GerenteHandler().insereRegistrosDeArquivo(arquivo, repositoryGerente.findByCpf(cpfGerente).getFkEscola(), respRepo, alunoRepo, contratoRepo,pgtoRepo);
            if(response.size() > 0){
                StringBuilder summary = new StringBuilder();
                for (String index: response) {
                    summary.append(index);
                }
                return ResponseEntity.status(200).body(summary.toString());
            } else {
                return ResponseEntity.status(200).body("OK");
            }
        } else {
            return ResponseEntity.status(404).body("Gerente informado não foi encontrado.");
        }

    }

    @CrossOrigin
    @GetMapping("/pesquisa")
    public ResponseEntity pesquisaHashTable(@RequestParam("value") String value, @RequestParam("cnpj") String cnpj) throws IOException {
        if(escolaRepo.existsByCnpj(cnpj)){
            List<ResponsavelCacheModel> list = new GerenteHandler().pesquisaHash(value, cnpj);
            return ResponseEntity.status(200).body(list);
        }
        return ResponseEntity.status(404).build();
    }

    @CrossOrigin
    @GetMapping("/juros")
    public ResponseEntity getJurosTotalResponsavelEscola(@RequestParam("cnpj") String cnpj)
    {
        Escola escola = escolaRepo.findByCnpj(cnpj);

        if(escolaRepo.existsByCnpj(cnpj))
        {
            List<Contrato> lista = contratoRepo.findAllByFkEscola(escola);
            List<Pagamento> listaPgto = new ArrayList<>();

            Integer qntJuros = 0;

            for(int i = 0; i < lista.size(); i++)
            {
               listaPgto = pgtoRepo.findAllByFkContrato(lista.get(i));

               for(Pagamento pagamento: listaPgto) {
                   if (pagamento.getSituacao() == 1) {
                       qntJuros++;
                   }
               }
            }

            Double jurosTotal = escolaRepo.findByCnpj(cnpj).getJuros() * qntJuros;

            return ResponseEntity.status(200).body(jurosTotal);
        }
        else
        {
            return ResponseEntity.badRequest().build();
        }
    }

    @CrossOrigin
    @GetMapping("/adesao")
    public ResponseEntity getAdesaoEscola(@RequestParam("cnpj") String cnpj)
    {
       Escola escola = escolaRepo.findByCnpj(cnpj);

       List<Contrato> lista = contratoRepo.findAllByFkEscola(escola);

       Double qntAdesao = 0.0;
       Double qntEvasao = 0.0;
       Double total = 0.0;



       for (Contrato contrato: lista)
       {
           if(contrato.getSituacao().equals("novo"))
           {
              qntAdesao++;
           }
           if(contrato.getSituacao().equals("encerrado"))
           {
              qntEvasao++;
           }
           else
           {
              total++;
           }
       }

       Metrica metrica = new Metrica();

       metrica.setAdesao((qntAdesao / total) * 100);
       metrica.setEvasao((qntEvasao / total) * 100);


       return ResponseEntity.status(200).body(metrica);
    }

    @CrossOrigin
    @GetMapping("/situacao")
    public ResponseEntity getSituacaoPagamento(@RequestParam("cnpj") String cnpj)
    {
        Escola escola = escolaRepo.findByCnpj(cnpj);

        List<Contrato> contratos = contratoRepo.findAllByFkEscola(escola);
        List<Pagamento> pagamentos = new ArrayList<>();

        Integer qntPago = 0;
        Integer qntAtrasado = 0;


        for (Contrato contrato : contratos)
        {
            List<Pagamento> pgto = pgtoRepo.findAllByFkContrato(contrato);
            pagamentos.addAll(pgto);

            for(Pagamento pagamento: pgto)
            {
                if(pagamento.getSituacao().equals(1) || pagamento.getSituacao().equals(1))
                {
                    qntPago++;
                }
                else
                {
                    qntAtrasado++;
                }
            }
        }

        Metrica metrica = new Metrica();

        metrica.setQntPago(qntPago);
        metrica.setQntAtrasado(qntAtrasado);

        return ResponseEntity.status(200).body(metrica);
    }
}
