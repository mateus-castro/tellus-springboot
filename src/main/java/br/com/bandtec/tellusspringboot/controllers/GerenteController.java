package br.com.bandtec.tellusspringboot.controllers;

import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.domains.Gerente;
import br.com.bandtec.tellusspringboot.domains.Login;
import br.com.bandtec.tellusspringboot.handlers.GerenteHandler;
import br.com.bandtec.tellusspringboot.repositories.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/gerente")
@Api(value = "Question")

public class GerenteController {

    @Autowired
    private GerenteRepository repositoryGerente;

    @Autowired
    private ResponsavelRepository respRepo;

    @Autowired
    private AlunoRepository alunoRepo;

    @Autowired
    private ContratoRepository contratoRepo;

    @Autowired
    private EscolaRepository escolaRepo;


    @ApiOperation(value = "Retorna todos o gerentes referentes a uma escola.")
    @CrossOrigin
    @GetMapping
    public ResponseEntity getGerentesByEscola(@RequestParam("cnpj") String cnpj) {
        Escola escola = escolaRepo.findByCnpj(cnpj);
        List<Gerente> lista = repositoryGerente.findGerentesByFkEscola(escola);
        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(lista);
        }
    }

    @CrossOrigin
    @PostMapping
    public ResponseEntity postGerente(@RequestBody Gerente gerente) {
        repositoryGerente.save(gerente);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin
    @DeleteMapping
    public ResponseEntity deleteGerente(@RequestParam("cpf") String cpf) {
        if (repositoryGerente.existsByCpf(cpf)) {
            repositoryGerente.deleteByCpf(cpf);
            return ResponseEntity.status(200).body("Gerente deletado com sucesso");
        }
        return ResponseEntity.status(204).body("Gerente com o cpf " + cpf + " não foi encontrado");
    }

    @CrossOrigin
    @GetMapping("/login")
    public ResponseEntity getLogin(@RequestBody Login login) {
            if (repositoryGerente.existsByEmailAndSenha(login.getEmail(), login.getSenha())) {
                return ResponseEntity.status(200).body(repositoryGerente.findGerenteByEmailAndSenha(login.getEmail(), login.getSenha()));
            } else {
                return ResponseEntity.status(204).body("Gerente não encontrado!");
            }
    }

    @CrossOrigin
    @PostMapping("/csv-download")
    public ResponseEntity<String> CadastroMassivo(@RequestParam("file") MultipartFile file, @RequestParam("cpf") String cpfGerente) throws IOException {
        if(!Objects.equals(file.getContentType(), "text/csv")){
            return ResponseEntity.status(400).body("Arquivo enviado não está no formato correto. Envie um arquivo .csv :).");
        }

        if(repositoryGerente.existsByCpf(cpfGerente)) {
            byte[] fileBytes = file.getBytes();
            String arquivo = new String(fileBytes);
            ArrayList<String> response = new GerenteHandler().insereRegistrosDeArquivo(arquivo, repositoryGerente.findByCpf(cpfGerente).getFkEscola(), respRepo, alunoRepo, contratoRepo);
            if(response.size() > 0){
                StringBuilder summary = new StringBuilder();
                for (String index: response) {
                    summary.append(index);
                }
                return ResponseEntity.status(200).body(summary.toString());
            } else {
                return ResponseEntity.status(200).body("OK");
            }
        } else {
            return ResponseEntity.status(404).body("Gerente informado não foi encontrado.");
        }


    }

}
