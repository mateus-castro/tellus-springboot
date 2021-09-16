package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.dominio.Aluno;
import br.com.bandtec.tellusspringboot.utils.PilhaObjeto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import br.com.bandtec.tellusspringboot.repositorio.AlunoRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    private AlunoRepository repositoryAluno;

    private PilhaObjeto<Aluno> pilha = new PilhaObjeto<>(10);

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
        pilha.push(aluno);
        return ResponseEntity.status(201).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteAluno(@PathVariable Integer id) {
        if (repositoryAluno.existsById(id)) {
            Aluno aluno = repositoryAluno.findById(id).get();
            pilha.push(aluno);
            repositoryAluno.deleteById(aluno.getId());
            return ResponseEntity.status(200).build();
        } else {
            return ResponseEntity.status(204).build();
        }
    }

    @DeleteMapping("/desfazer-post")
    public ResponseEntity desfazerPost() {
        repositoryAluno.deleteById(pilha.pop().getId());
        return ResponseEntity.status(200).build();
    }

    @PostMapping("/desfazer-delete")
    public ResponseEntity desfazerDelete() {
        repositoryAluno.save(pilha.pop());
        return ResponseEntity.status(200).build();
    }
}