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
public class PagamentoController {
    @Autowired

    private PagamentoRepository repositoryPagamento;

    //TODO adicionar endpoint de geramento de CSV

    @PostMapping
    public ResponseEntity postPagamento(@RequestBody Pagamento pagamento) {
        repositoryPagamento.save(pagamento);
        return ResponseEntity.status(201).build();
    }

    @GetMapping(value = "/csv-download/{nomeArquivo}", produces = "application/csv")
    @ResponseBody
    public ResponseEntity getCsv(@PathVariable String nomeArquivo) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", "attachment; filename=" + nomeArquivo + ".csv");

        return ResponseEntity.status(200).headers(headers).body(RegistroArquivo.leBaixaArquivo(nomeArquivo));
    }

    @GetMapping(value = "/txt-download/{nomeArquivo}", produces = "application/txt")
    @ResponseBody
    public ResponseEntity getTxt(@PathVariable String nomeArquivo) {
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", "attachment; filename=" + nomeArquivo + ".txt");

        return ResponseEntity.status(200).headers(headers).body(RegistroArquivo.leArquivo(nomeArquivo));
    }

    @GetMapping("/grava-csv/{nomeArquivo}")
    public ResponseEntity gravaListaCsv(@PathVariable String nomeArquivo) {
        if (repositoryPagamento.findAll().isEmpty()) {
            return ResponseEntity.status(204).body("Lista vazia.");
        }

        RegistroArquivo.gravaListaPagamento(repositoryPagamento.findAll(), nomeArquivo);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/grava-txt/{nomeArquivo}")
    public ResponseEntity gravaListaTxt(@PathVariable String nomeArquivo) {
        String nomeArq = nomeArquivo + ".txt";
        String header = "";
        String trailer = "";
        int contRegDados = 0;

        Date dataDeHoje = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        header += "00PAGAMENTOS01";
        header += formatter.format(dataDeHoje);
        header += "01";

        RegistroArquivo.gravaArquivo(nomeArq, header);

        for (int i = 0; i < repositoryPagamento.findAll().size(); i++) {
            Pagamento p = repositoryPagamento.findAll().get(i);

            String corpo = "";
            corpo += "02";
            corpo += String.format("%-2s", p.getId());
            corpo += String.format("%-45s", p.getFkContrato().getFkAluno().getId());
            corpo += String.format("%-13s", p.getTipo());
            corpo += String.format("%-8s", p.getDataVenc());
            corpo += String.format("%5.2f", p.getValor());
            corpo += String.format("%-8s", p.getSituacao());
            corpo += String.format("%-45s", p.getFkContrato().getFkEscola().getId());

            contRegDados++;
            RegistroArquivo.gravaArquivo(nomeArq, corpo);
        }

        trailer += "01";
        trailer += String.format("%010d", contRegDados);
        RegistroArquivo.gravaArquivo(nomeArq, trailer);

        return ResponseEntity.status(200).build();
    }
}