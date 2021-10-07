package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Contrato;
import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.repositories.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositories.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contrato")
public class ContratoController {
    @Autowired
    private ContratoRepository repositoryContrato;

    @Autowired
    private ResponsavelRepository repositoryResponsavel;

    @PostMapping
    public ResponseEntity postContrato(@RequestBody Contrato contrato) {
        repositoryContrato.save(contrato);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin
    @GetMapping
    public ResponseEntity getContratosOfResp(@RequestParam("cpf") String cpf) {
        if (repositoryResponsavel.existsByCpf(cpf)) {
            Responsavel responsavel = repositoryResponsavel.findResponsavelByCpf(cpf);
            return ResponseEntity.status(200).body(repositoryContrato.findByFkResponsavel(responsavel));
        } else {
            return ResponseEntity.status(204).build();
        }
    }
}
