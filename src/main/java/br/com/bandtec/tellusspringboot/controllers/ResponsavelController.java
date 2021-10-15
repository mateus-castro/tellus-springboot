package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Aluno;
import br.com.bandtec.tellusspringboot.domains.Contrato;
import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.repositories.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositories.EscolaRepository;
import br.com.bandtec.tellusspringboot.repositories.ResponsavelRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/responsavel")
@Api(value = "Question")

public class ResponsavelController {

    @Autowired
    private EscolaRepository escolaRepo;

    @Autowired
    private ResponsavelRepository repositoryResponsavel;

    @Autowired
    private ContratoRepository contRepo;

    @CrossOrigin
    @GetMapping
    public ResponseEntity getAllRespOfEscola(@RequestParam("cnpj") String cnpj){
        if(escolaRepo.existsByCnpj(cnpj)){
            Escola escola = escolaRepo.findByCnpj(cnpj);
            List<Responsavel> listaResp = new ArrayList<>();
            List<Contrato> listaContrato = contRepo.findAllByFkEscola(escola);

            for ( Contrato contrato : listaContrato ) {
                Responsavel newResp = contrato.getFkResponsavel();
                if(!listaResp.contains(newResp)) {
                    listaResp.add(newResp);
                }
            }

            return ResponseEntity.status(200).body(listaResp);
        } else{
            System.out.println("[getAllRespOfEscola] Escola especificada não encontrada");
            return ResponseEntity.status(204).build();
        }
    }

    @CrossOrigin
    @GetMapping("/alunos")
    public ResponseEntity getDependentesPorResp(@RequestParam("cpf") String cpf){
        if(repositoryResponsavel.existsByCpf(cpf)){
            List<Contrato> contratoList = contRepo.findAllByFkResponsavel(repositoryResponsavel.findResponsavelByCpf(cpf));
            List<Aluno> alunoList = new ArrayList<Aluno>();
            for ( Contrato contrato : contratoList ) {
                alunoList.add(contrato.getFkAluno());
            }
            return ResponseEntity.status(200).body(alunoList);
        }
        return ResponseEntity.status(200).build();
    }

    @CrossOrigin
    @GetMapping("/contratos")
    public ResponseEntity getValorTotalDeContratoPago(@RequestParam("cpf") String cpf){
        if(repositoryResponsavel.existsByCpf(cpf)){
            List<Contrato> contratoList = contRepo.findAllByFkResponsavel(repositoryResponsavel.findResponsavelByCpf(cpf));
//            return ResponseEntity.status(200).body(alunoList);
        }
        return ResponseEntity.status(200).build();
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity postResponsavel(@RequestBody Responsavel responsavel) {
        repositoryResponsavel.save(responsavel);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin
    @DeleteMapping
    public ResponseEntity deleteResponsavel(@RequestParam("cpf") String cpf) {
        if (repositoryResponsavel.existsByCpf(cpf)) {
            repositoryResponsavel.deleteByCpf(cpf);
            return ResponseEntity.status(200).body("Responsavel deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Responsavel com o CPF: " + cpf + "Não foi encontrado");
    }

    @PutMapping
    public ResponseEntity updateResponsavel(@RequestBody Responsavel newResp) {
        if (repositoryResponsavel.existsByCpf(newResp.getCpf())) {
            Responsavel oldResp = repositoryResponsavel.findResponsavelByCpf(newResp.getCpf());
            repositoryResponsavel.deleteByCpf(oldResp.getCpf());
            repositoryResponsavel.save(newResp);
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(204).body("Responsável não encontrado.");
    }
}

