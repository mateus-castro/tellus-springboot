package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.dominio.Escola;
import br.com.bandtec.tellusspringboot.dominio.Responsavel;
import br.com.bandtec.tellusspringboot.repositorio.EscolaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/escola")

public class EscolaController {

    @Autowired
    private EscolaRepository repositoryEscola;

    @GetMapping
    public ResponseEntity getEscola() {
        List<Escola> lista = repositoryEscola.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(lista);
        }
    }

    @GetMapping("contrato/{cnpj}")
    public ResponseEntity getContrato(@PathVariable String cnpj) {
        if (repositoryEscola.existsByCnpj(cnpj)) {
            Escola escola = repositoryEscola.findByCnpj(cnpj);
            return ResponseEntity.status(200).body(escola);
        } else {
            return ResponseEntity.status(204).build();
        }
    }

    @CrossOrigin
    @GetMapping("/{cnpj}") // TODO trocar esse path variable pra request body
    public ResponseEntity getEscola(@PathVariable String cnpj) {
        if(repositoryEscola.existsByCnpj(cnpj)){
            Escola escola = repositoryEscola.findByCnpj(cnpj);
            return ResponseEntity.status(200).body(escola);
        }
        return ResponseEntity.status(204).build();
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity postEscola(@RequestBody Escola escola) {
        repositoryEscola.save(escola);
        return ResponseEntity.status(201).build();

    }
}
