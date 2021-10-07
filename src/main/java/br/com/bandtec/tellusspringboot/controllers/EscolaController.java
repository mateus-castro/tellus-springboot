package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.repositories.EscolaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/escola")

public class EscolaController {

    @Autowired
    private EscolaRepository repositoryEscola;

    @CrossOrigin
    @GetMapping
    public ResponseEntity getEscola(@RequestParam("cnpj") String cnpj) {
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
