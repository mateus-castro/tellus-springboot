package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.dominio.Contrato;
import br.com.bandtec.tellusspringboot.dominio.Responsavel;
import br.com.bandtec.tellusspringboot.repositorio.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositorio.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/contrato")
public class ContratoController {
    @Autowired
    private ContratoRepository repositoryContrato;

    @Autowired
    private ResponsavelRepository repositoryResponsavel;

    @GetMapping
    public ResponseEntity getContrato() {
        List<Contrato> lista = repositoryContrato.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(lista);
        }
    }

    @PostMapping
    public ResponseEntity postContrato(@RequestBody Contrato contrato) {
        repositoryContrato.save(contrato);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin
    @GetMapping("/responsavel/{cpf}")
    public ResponseEntity getContrato(@PathVariable String cpf) {
        if (repositoryResponsavel.existsByCpf(cpf)) {
            Responsavel responsavel = repositoryResponsavel.findResponsavelByCpf(cpf);
            return ResponseEntity.status(200).body(repositoryContrato.findByFkResponsavel(responsavel));
        } else {
            return ResponseEntity.status(204).build();
        }
    }
}
