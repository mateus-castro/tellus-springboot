package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.*;
import br.com.bandtec.tellusspringboot.handlers.ResponsavelHandler;
import br.com.bandtec.tellusspringboot.repositories.*;
import br.com.bandtec.tellusspringboot.services.HashService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/responsavel")
@Api(value = "Question")

public class ResponsavelController {

    @Autowired
    private EscolaRepository escolaRepo;

    @Autowired
    private AlunoRepository alunoRepo;

    @Autowired
    private ResponsavelRepository respRepo;

    @Autowired
    private ContratoRepository contRepo;

    @Autowired
    private PagamentoRepository pagRepo;

    @Autowired
    private HashService hashService;

    @CrossOrigin
    @GetMapping
    public ResponseEntity getAllRespOfEscola(@RequestParam("cnpj") String cnpj) {
        if (escolaRepo.existsByCnpj(cnpj)) {
            List<Responsavel> listaResp = new ResponsavelHandler().pegaRespsDaEscola(cnpj, contRepo, escolaRepo);
            return ResponseEntity.status(200).body(listaResp);
        } else {
            System.out.println("[getAllRespOfEscola] Escola especificada não encontrada");
            return ResponseEntity.status(204).build();
        }
    }

    @CrossOrigin
    @GetMapping("/cache")
    public ResponseEntity getAllRespOfEscolaCache(@RequestParam("cnpj") String cnpj) {
        if (escolaRepo.existsByCnpj(cnpj)) {
            return ResponseEntity.status(200).body(HashService.hashTable.retornaTotalLista(cnpj));
        } else {
            System.out.println("[getAllRespOfEscola] Escola especificada não encontrada");
            return ResponseEntity.status(204).build();
        }
    }

    @CrossOrigin
    @GetMapping("/dependentes")
    public ResponseEntity getDependentesPorResp(@RequestParam("cpf") String cpf) {
        if (respRepo.existsByCpf(cpf)) {
            List<Contrato> contratoList = contRepo.findAllByFkResponsavel(respRepo.findResponsavelByCpf(cpf));
            List<Aluno> alunoList = new ArrayList<Aluno>();
            for (Contrato contrato : contratoList) {
                alunoList.add(contrato.getFkAluno());
            }
            return ResponseEntity.status(200).body(alunoList);
        }
        return ResponseEntity.status(404).body("[getDEpendentesPorResp]");
    }

    // gráfico de valor pago do contrato de responsável
    @CrossOrigin
    @GetMapping("/loadbar")
    public ResponseEntity getValorTotalDeContratoPago(@RequestParam("cpf") String cpf,
                                                      @RequestParam("nomeAluno") String nomeAluno) {
        if (respRepo.existsByCpf(cpf) && alunoRepo.existsAlunoByNome(nomeAluno)) {
            Contrato contrato = contRepo.findContratoByFkAluno(alunoRepo.findAlunoByNome(nomeAluno));

            List<Pagamento> listaPagtos = pagRepo.findAllByFkContrato(contrato);
            Double res = 0.0;

            for (Pagamento pagto : listaPagtos) {
                res += pagto.getValor();
            }
            res = (100 * res) / contrato.getValor();
            return ResponseEntity.status(200).body(res);
        }

        return ResponseEntity.status(404).build();
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity postResponsavel(@RequestBody Responsavel responsavel, @RequestParam String cnpj) {
        if(respRepo.existsByCpf(responsavel.getCpf())) {
            respRepo.save(responsavel);
            hashService.insereEmCache(responsavel, escolaRepo.findByCnpj(cnpj));
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.status(409).build();
    }

    @ApiOperation(value = "Deleta um responsável do banco e em cache.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleta o responsável."),
            @ApiResponse(code = 404, message = "Não insere gerente no banco e retorna mensagem de erro.")
    })
    @CrossOrigin
    @DeleteMapping
    public ResponseEntity deleteResponsavel(
            @ApiParam(name =  "cpf", type = "String", value = "CPF do Responsável", example = "515.973.188-17", required = true)
            @RequestParam("cpf") String cpf,
            @ApiParam(name =  "cnpj", type = "String", value = "CNPJ da Escola", example = "78.461.539/0001-35", required = true)
            @RequestParam("cnpj") String cnpj) {
        if (respRepo.existsByCpf(cpf)) {
            respRepo.deleteByCpf(cpf);
            hashService.removeEmCache(respRepo.findResponsavelByCpf(cpf), escolaRepo.findByCnpj(cnpj));
            return ResponseEntity.status(200).body("Responsavel deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Responsavel com o CPF: " + cpf + "Não foi encontrado");
    }

    @CrossOrigin
    @GetMapping("/calendario")
    public ResponseEntity getCalendario(@RequestParam("cpf") String cpf,
                                        @RequestParam("nomeAluno") String nomeAluno) {
        if (respRepo.existsByCpf(cpf) && alunoRepo.existsAlunoByNome(nomeAluno)) {
            Contrato contrato = contRepo.findContratoByFkAluno(alunoRepo.findAlunoByNome(nomeAluno));
            List<Pagamento> listaPagtos = pagRepo.findAllByFkContrato(contrato);


            Double valorJuros = escolaRepo.findById(contrato.getFkEscola().getId()).get().getJuros();
            List<Metrica> lista = new ArrayList<>();

            //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ROOT);
            //LocalDate date = LocalDate.parse("2099/12/2099");
            Date dataContrato = new Date();
            dataContrato = Date.from(dataContrato.toInstant());

            for (Pagamento pagto : listaPagtos) {
                Metrica metrica = new Metrica();

                if (pagto.getSituacao() == 1 && pagto.getDataVenc().before(dataContrato)) {

                    metrica.setJuros(valorJuros);
                    metrica.setDataJuros(pagto.getDataVenc());

                    lista.add(metrica);
                }
            }
            System.out.println();
            return ResponseEntity.status(200).body(lista);
        }

        return ResponseEntity.status(404).build();
    }

    @CrossOrigin
    @GetMapping("/juros")
    public ResponseEntity getJuros(@RequestParam("cpf") String cpf,
                                   @RequestParam("nomeAluno") String nomeAluno) {
        if (respRepo.existsByCpf(cpf) && alunoRepo.existsAlunoByNome(nomeAluno)) {
            Contrato contrato = contRepo.findContratoByFkAluno(alunoRepo.findAlunoByNome(nomeAluno));
            List<Pagamento> listaPagtos = pagRepo.findAllByFkContrato(contrato);

            Double valorJuros = escolaRepo.findById(contrato.getFkEscola().getId()).get().getJuros();
            List<Metrica> lista = new ArrayList<>();
            Metrica metrica = new Metrica();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ROOT);
            LocalDate date = LocalDate.parse("31/12/2099",formatter);
            Date dataContrato = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());

            for (Pagamento pagto : listaPagtos) {
                if (pagto.getSituacao() == 1 && pagto.getDataVenc().before(dataContrato)) {
                    metrica.setJuros(valorJuros);
                    metrica.setProxPagamento(pagto.getDataVenc());

                    lista.add(metrica);
                }
            }
            System.out.println();
            return ResponseEntity.status(200).body(lista);
        }

        return ResponseEntity.status(404).build();
    }
}
