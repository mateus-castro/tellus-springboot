package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.*;
import br.com.bandtec.tellusspringboot.handlers.ResponsavelHandler;
import br.com.bandtec.tellusspringboot.repositories.*;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    @CrossOrigin
    @GetMapping
    public ResponseEntity getAllRespOfEscola(@RequestParam("cnpj") String cnpj){
        if(escolaRepo.existsByCnpj(cnpj)){
            List<Responsavel> listaResp = new ResponsavelHandler().pegaRespsDaEscola(cnpj, contRepo, escolaRepo);
            return ResponseEntity.status(200).body(listaResp);
        } else{
            System.out.println("[getAllRespOfEscola] Escola especificada não encontrada");
            return ResponseEntity.status(204).build();
        }
    }

    @CrossOrigin
    @GetMapping("/dependentes")
    public ResponseEntity getDependentesPorResp(@RequestParam("cpf") String cpf){
        if(respRepo.existsByCpf(cpf)){
            List<Contrato> contratoList = contRepo.findAllByFkResponsavel(respRepo.findResponsavelByCpf(cpf));
            List<Aluno> alunoList = new ArrayList<Aluno>();
            for ( Contrato contrato : contratoList ) {
                alunoList.add(contrato.getFkAluno());
            }
            return ResponseEntity.status(200).body(alunoList);
        }
        return ResponseEntity.status(404).body("[getDEpendentesPorResp]");
    }

    // gráfico de valor pago do contrato de responsável
    @CrossOrigin
    @GetMapping("/contratos")
    public ResponseEntity getValorTotalDeContratoPago(@RequestParam("cpf") String cpf,
                                                      @RequestParam("nomeAluno") String nomeAluno){
        if(respRepo.existsByCpf(cpf) && alunoRepo.existsAlunoByNome(nomeAluno)){
            Contrato contrato = contRepo.findContratoByFkAluno(alunoRepo.findAlunoByNome(nomeAluno));
            List<Pagamento> listaPagtos = pagRepo.findAllByFkContrato(contrato);
            Double res = 0.0;
            for ( Pagamento pagto : listaPagtos ) {
                res += pagto.getValor();
            }
            res = ( 100 * res ) / contrato.getValor();
            return ResponseEntity.status(200).body(res);
        }
        System.out.println("[getValorTotalDeContratoPago] Parâmetros inválidos");
        return ResponseEntity.status(404).build();
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity postResponsavel(@RequestBody Responsavel responsavel) {
        respRepo.save(responsavel);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin
    @DeleteMapping
    public ResponseEntity deleteResponsavel(@RequestParam("cpf") String cpf) {
        if (respRepo.existsByCpf(cpf)) {
            respRepo.deleteByCpf(cpf);
            return ResponseEntity.status(200).body("Responsavel deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Responsavel com o CPF: " + cpf + "Não foi encontrado");
    }

    @PutMapping
    public ResponseEntity updateResponsavel(@RequestBody Responsavel newResp) {
        if (respRepo.existsByCpf(newResp.getCpf())) {
            Responsavel oldResp = respRepo.findResponsavelByCpf(newResp.getCpf());
            respRepo.deleteByCpf(oldResp.getCpf());
            respRepo.save(newResp);
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(204).body("Responsável não encontrado.");
    }
}

