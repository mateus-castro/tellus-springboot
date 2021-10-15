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

    @ApiOperation(value = "Retorna todos o gerentes referentes a uma escola.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista de gerentes."),
            @ApiResponse(code = 204, message = "Não existe nenhum gerente.")
    })
    @PostMapping
    public ResponseEntity postPagamento(@RequestBody Pagamento pagamento) {
        repositoryPagamento.save(pagamento);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity getPagamentoByContrato(@RequestBody Contrato contrato) {
        List<Pagamento> pagamentos = repositoryPagamento.findPagamentosByFkContrato(contrato);
        return ResponseEntity.status(200).body(pagamentos);
    }

}