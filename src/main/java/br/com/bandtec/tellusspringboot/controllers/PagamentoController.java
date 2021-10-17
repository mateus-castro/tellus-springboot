package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Contrato;
import br.com.bandtec.tellusspringboot.domains.Pagamento;
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
    private PagamentoRepository repositoryPagamento;

    @ApiOperation(value = "Insere um pagamento.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Pagamento inserido no banco com sucesso."),
            @ApiResponse(code = 400, message = "Requisição falhou.")
    })
    @PostMapping
    public ResponseEntity postPagamento(@RequestBody Pagamento pagamento) {
        try{
            repositoryPagamento.save(pagamento);
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
    public ResponseEntity getPagamentoByContrato(@RequestBody Contrato contrato) {
        List<Pagamento> pagamentos = repositoryPagamento.findAllByFkContrato(contrato);
        return ResponseEntity.status(200).body(pagamentos);
    }

}