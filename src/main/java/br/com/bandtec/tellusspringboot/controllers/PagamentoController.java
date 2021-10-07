package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Contrato;
import br.com.bandtec.tellusspringboot.domains.Pagamento;
import br.com.bandtec.tellusspringboot.repositories.PagamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pagamento")

public class PagamentoController {
    @Autowired

    private PagamentoRepository repositoryPagamento;

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