package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Login;
import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.repositories.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/responsavel")
public class ResponsavelController {

    @Autowired
    private ResponsavelRepository repositoryResponsavel;

    @CrossOrigin
    @GetMapping
    public ResponseEntity getResponsavel() {
        List<Responsavel> lista = repositoryResponsavel.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(lista);
        }
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity postResponsavel(@RequestBody Responsavel responsavel) {
        repositoryResponsavel.save(responsavel);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin
    @DeleteMapping
    public ResponseEntity deleteResponsavel(@RequestParam("cpf") String cpf) {
        if (repositoryResponsavel.existsByCpf(cpf)) {
            repositoryResponsavel.deleteByCpf(cpf);
            return ResponseEntity.status(200).body("Responsavel deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Responsavel com o CPF: " + cpf + "Não foi encontrado");
    }

    // TODO permitir a alteração de qualquer atributo do responsável, e não só o telefone
    @PutMapping
    public ResponseEntity updateResponsavel(@RequestParam("cpf") String cpf, @RequestParam("telefone") String telefone) {
        if (repositoryResponsavel.existsByCpf(cpf)) {
            Responsavel resp = repositoryResponsavel.findResponsavelByCpf(cpf);
            resp.setTelefone(telefone);
            repositoryResponsavel.save(resp);
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(204).body("Responsável não encontrado.");
    }

    // TODO tirar o parâmetro boolean por que não faz sentido os gerentes terem informações de outros gerentes
    @CrossOrigin
    @GetMapping("/login")
    public ResponseEntity getLogin(@RequestBody Login login) {
            if (repositoryResponsavel.existsByEmailAndSenha(login.getEmail(), login.getSenha())) {
                return ResponseEntity.status(200).body(repositoryResponsavel.findResponsavelByEmailAndSenha(login.getEmail(), login.getSenha()));
            } else {
                return ResponseEntity.status(204).body(null);
            }
    }
}

