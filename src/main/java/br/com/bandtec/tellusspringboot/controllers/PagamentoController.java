package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Aluno;
import br.com.bandtec.tellusspringboot.domains.Contrato;
import br.com.bandtec.tellusspringboot.domains.Pagamento;
import br.com.bandtec.tellusspringboot.repositories.AlunoRepository;
import br.com.bandtec.tellusspringboot.repositories.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositories.PagamentoRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamento")
@Api(value = "Question")

public class PagamentoController {
    @Autowired
    private PagamentoRepository pagRepo;

    @Autowired
    private ContratoRepository contRepo;

    @Autowired
    private AlunoRepository alunoRepo;

    @ApiOperation(value = "Insere um pagamento.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Pagamento inserido no banco com sucesso."),
            @ApiResponse(code = 400, message = "Requisição falhou.")
    })
    @PostMapping
    public ResponseEntity postPagamento(@RequestBody Pagamento pagamento) {
        try{
            pagRepo.save(pagamento);
            return ResponseEntity.status(201).build();
        } catch(Error e){
            System.out.println("[postPagamento] Erro de requisição " + e);
            return ResponseEntity.status(400).build();
        }
    }

    @ApiOperation(value = "Retorna todos os pagamentos de um contrato.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista de pagamentos."),
            @ApiResponse(code = 204, message = "Não existe nenhum gerente.")
    })
    @GetMapping
    public ResponseEntity getPagamentoByContrato(@RequestParam("ra") String raAluno) {
        try {
            Aluno aluno = alunoRepo.findAlunoByRa(raAluno);
            Contrato contrato = contRepo.findContratoByFkAluno(aluno);
            List<Pagamento> pagamentos = pagRepo.findAllByFkContrato(contrato);
            return ResponseEntity.status(200).body(pagamentos);
        } catch(Error e){
            System.out.println(e);
            return ResponseEntity.status(400).build();
        }

    }

}