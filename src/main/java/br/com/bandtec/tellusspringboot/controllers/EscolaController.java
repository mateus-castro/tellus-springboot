package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.repositories.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositories.EscolaRepository;
import br.com.bandtec.tellusspringboot.repositories.GerenteRepository;
import br.com.bandtec.tellusspringboot.repositories.ResponsavelRepository;
import io.swagger.annotations.Api;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/escola")
@Api(value = "Question")

public class EscolaController {

    @Autowired
    private EscolaRepository escolaRepo;

    @Autowired
    private ResponsavelRepository respRepo;

    @Autowired
    private GerenteRepository gerRepo;

    @Autowired
    private ContratoRepository contratoRepository;

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
    public ResponseEntity getLogin(@RequestParam String email, @RequestParam String senha, @RequestParam("type") String type) {
        if(type.equals("resp")){
            if (respRepo.existsByEmailAndSenha(email, senha)) {
                Responsavel responsavel = respRepo.findResponsavelByEmailAndSenha(email, senha);
                Escola escolaQuery = contratoRepository.findFirstByFkResponsavel(responsavel).getFkEscola();
                JSONObject body = new JSONObject();
                body.put("responsavel", responsavel);
                body.put("escola", escolaQuery);
                return ResponseEntity.status(200).body(body.toMap());
            } else {
                return ResponseEntity.status(204).body(null);
            }
        } else if(type.equals("ger")){
            if (gerRepo.existsByEmailAndSenha(email, senha)) {
                return ResponseEntity.status(200).body(gerRepo.findGerenteByEmailAndSenha(email, senha));
            } else {
                return ResponseEntity.status(204).body(null);
            }
        } else {
            return ResponseEntity.status(404).body("[getLogin] Parâmetro de requisição inválido");
        }
    }
}
