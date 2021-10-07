package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.domains.Login;
import br.com.bandtec.tellusspringboot.repositories.EscolaRepository;
import br.com.bandtec.tellusspringboot.repositories.GerenteRepository;
import br.com.bandtec.tellusspringboot.repositories.ResponsavelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/escola")

public class EscolaController {

    @Autowired
    private EscolaRepository escolaRepo;

    @Autowired
    private ResponsavelRepository respRepo;

    @Autowired
    private GerenteRepository gerRepo;

    @CrossOrigin
    @GetMapping
    public ResponseEntity getEscola(@RequestParam("cnpj") String cnpj) {
        if(escolaRepo.existsByCnpj(cnpj)){
            Escola escola = escolaRepo.findByCnpj(cnpj);
            return ResponseEntity.status(200).body(escola);
        }
        return ResponseEntity.status(204).build();
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity postEscola(@RequestBody Escola escola) {
        escolaRepo.save(escola);
        return ResponseEntity.status(201).build();

    }

    @CrossOrigin
    @GetMapping("/login")
    public ResponseEntity getLogin(@RequestBody Login login, @RequestParam("type") String type) {
        if(type.equals("resp")){
            if (respRepo.existsByEmailAndSenha(login.getEmail(), login.getSenha())) {
                return ResponseEntity.status(200).body(respRepo.findResponsavelByEmailAndSenha(login.getEmail(), login.getSenha()));
            } else {
                return ResponseEntity.status(204).body(null);
            }
        } else if(type.equals("ger")){
            if (gerRepo.existsByEmailAndSenha(login.getEmail(), login.getSenha())) {
                return ResponseEntity.status(200).body(gerRepo.findGerenteByEmailAndSenha(login.getEmail(), login.getSenha()));
            } else {
                return ResponseEntity.status(204).body(null);
            }
        } else {
            return ResponseEntity.status(404).body("Parâmetro da requisição errado");
        }
    }
}
