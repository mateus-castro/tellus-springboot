package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Aluno;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.bandtec.tellusspringboot.repositories.AlunoRepository;

import java.util.List;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    private AlunoRepository repositoryAluno;

    @GetMapping
    public ResponseEntity getAluno() {
        List<Aluno> lista = repositoryAluno.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(200).body(lista);
        }
    }

    @PostMapping
    public ResponseEntity postAluno(@RequestBody Aluno aluno) {
        repositoryAluno.save(aluno);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping
    public ResponseEntity deleteAluno(@RequestParam("ra") String ra) {
        if (repositoryAluno.existsAlunoByRa(ra)) {
            Aluno aluno = repositoryAluno.findAlunoByRa(ra);
            repositoryAluno.deleteById(aluno.getId());
            return ResponseEntity.status(200).build();
        } else {
            return ResponseEntity.status(204).build();
        }
    }
}