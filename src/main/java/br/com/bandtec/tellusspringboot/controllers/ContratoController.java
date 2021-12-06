package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Contrato;
import br.com.bandtec.tellusspringboot.domains.Responsavel;
import br.com.bandtec.tellusspringboot.repositories.ContratoRepository;
import br.com.bandtec.tellusspringboot.repositories.ResponsavelRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/contrato")
@Api(value = "Question")

public class ContratoController {

    @Autowired
    private ContratoRepository repositoryContrato;

    @Autowired
    private ResponsavelRepository respRepo;

    @ApiOperation(value = "Cadastra um contrato")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Contrato cadastrado com sucesso.")
    })
    @CrossOrigin
    @PostMapping
    public ResponseEntity postContrato(@RequestBody Contrato contrato) {
        repositoryContrato.save(contrato);
        return ResponseEntity.status(201).build();
    }

    @ApiOperation(value = "Retorna todos os pagamentos de um contrato.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Retorna uma lista de pagamentos."),
            @ApiResponse(code = 204, message = "Não existe nenhum gerente.")
    })
    @CrossOrigin
    @GetMapping
    public ResponseEntity getContratosOfResp(@RequestParam("cpf") String cpf) {
        if (respRepo.existsByCpf(cpf)) {
            Responsavel responsavel = respRepo.findResponsavelByCpf(cpf);
            return ResponseEntity.status(200).body(repositoryContrato.findFirstByFkResponsavel(responsavel));
        } else {
            return ResponseEntity.status(204).build();
        }
    }
}
