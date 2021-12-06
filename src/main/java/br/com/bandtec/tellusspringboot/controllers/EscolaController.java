package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.domains.Gerente;
import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.handlers.EscolaHandler;
import br.com.bandtec.tellusspringboot.repositories.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositories.EscolaRepository;
import br.com.bandtec.tellusspringboot.repositories.GerenteRepository;
import br.com.bandtec.tellusspringboot.repositories.ResponsavelRepository;
import br.com.bandtec.tellusspringboot.utils.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    @ApiOperation(value = "Retorna uma escola.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna a escola do cnpj informado."),
            @ApiResponse(code = 204, message = "Não existe nenhuma escola com esse cnpj.")
    })
    @CrossOrigin
    @GetMapping
    public ResponseEntity getEscola(@RequestParam("cnpj") String cnpj) {
        if(escolaRepo.existsByCnpj(cnpj)){
            Escola escola = escolaRepo.findByCnpj(cnpj);
            return ResponseEntity.status(200).body(escola);
        }
        return ResponseEntity.status(204).build();
    }

    @ApiOperation(value = "Cadastra uma escola.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Cadastra uma escola."),
            @ApiResponse(code = 409, message = "Aconteceu algum conflito.")
    })
    @CrossOrigin
    @PostMapping
    public ResponseEntity postEscola(@RequestBody Escola escola) {
        if(escolaRepo.existsByCnpj(escola.getCnpj())){
            escola.setCnpj(Util.formataCnpj(escola.getCnpj()));
            escolaRepo.save(escola);
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.status(409).build();
    }

    @ApiOperation(value = "Retorna um objeto com informações do usuário logado.")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Usuário encontrado."),
            @ApiResponse(code = 204, message = "Usuário inexistente."),
            @ApiResponse(code = 404, message = "Parâmetro da requisição inválido.")
    })
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
                Gerente gerente = gerRepo.findGerenteByEmailAndSenha(email, senha);
                JSONObject body = new JSONObject();
                JSONObject newGerente = new JSONObject();

                return ResponseEntity.status(200).body(new EscolaHandler().formatGerBody(gerente, body, newGerente).toMap());
            } else {
                return ResponseEntity.status(204).body(null);
            }
        } else {
            System.out.println("[getLogin] Parâmetro de requisição inválido");
            return ResponseEntity.status(404).build();
        }
    }
}
