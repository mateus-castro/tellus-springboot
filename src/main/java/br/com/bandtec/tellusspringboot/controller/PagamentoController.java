package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.dominio.Pagamento;
import br.com.bandtec.tellusspringboot.dominio.Responsavel;
import br.com.bandtec.tellusspringboot.repositorio.PagamentoRepository;
import br.com.bandtec.tellusspringboot.repositorio.ResponsavelRepository;
import br.com.bandtec.tellusspringboot.utils.ListaObjeto;
import br.com.bandtec.tellusspringboot.utils.RegistroArquivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/pagamento")

// TODO definir se realmente será preciso de uma controller de pagamento
public class PagamentoController {
    @Autowired

    private PagamentoRepository repositoryPagamento;

    @PostMapping
    public ResponseEntity postPagamento(@RequestBody Pagamento pagamento) {
        repositoryPagamento.save(pagamento);
        return ResponseEntity.status(201).build();
    }
//
//    @GetMapping(value = "/csv-download/{nomeArquivo}", produces = "application/csv")
//    @ResponseBody
//    public ResponseEntity getCsv(@PathVariable String nomeArquivo) {
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.add("Content-Disposition", "attachment; filename=" + nomeArquivo + ".csv");
//
//        return ResponseEntity.status(200).headers(headers).body(RegistroArquivo.leBaixaArquivo(nomeArquivo));
//    }

    @GetMapping(value = "/txt-download/{nomeArquivo}", produces = "application/txt")
    @ResponseBody
    public ResponseEntity getTxt(@PathVariable String nomeArquivo) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", "attachment; filename=" + nomeArquivo + ".txt");

        return ResponseEntity.status(200).headers(headers).body("lala");
    }
//
//    @GetMapping("/grava-csv/{nomeArquivo}")
//    public ResponseEntity gravaListaCsv(@PathVariable String nomeArquivo) {
//        if (repositoryPagamento.findAll().isEmpty()) {
//            return ResponseEntity.status(204).body("Lista vazia.");
//        }
//
//        RegistroArquivo.gravaListaPagamento(repositoryPagamento.findAll(), nomeArquivo);
//        return ResponseEntity.status(200).build();
//    }

}