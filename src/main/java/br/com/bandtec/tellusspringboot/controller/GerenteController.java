package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.dominio.Escola;
import br.com.bandtec.tellusspringboot.dominio.Gerente;
import br.com.bandtec.tellusspringboot.dominio.Login;
import br.com.bandtec.tellusspringboot.dominio.Responsavel;
import br.com.bandtec.tellusspringboot.repositorio.EscolaRepository;
import br.com.bandtec.tellusspringboot.repositorio.GerenteRepository;
import br.com.bandtec.tellusspringboot.repositorio.ResponsavelRepository;
import br.com.bandtec.tellusspringboot.utils.ListaObjeto;
import br.com.bandtec.tellusspringboot.utils.RegistroArquivo;
import br.com.bandtec.tellusspringboot.utils.Requisicao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/gerente")

public class GerenteController {

    @Autowired
    private GerenteRepository repositoryGerente;

    @CrossOrigin
    @GetMapping
    public ResponseEntity getGerente() {
        List<Gerente> lista = repositoryGerente.findAll();
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
        return ResponseEntity.status(204).body("Gerente com o cpf: " + cpf + "Não foi encontrado");
    }


    // TODO tirar o parâmetro boolean por que não faz sentido os gerentes terem informações de outros gerentes
    @CrossOrigin
    @GetMapping("/login")
    public ResponseEntity getLogin(@RequestBody Login login) {
            if (repositoryGerente.existsByEmailAndSenha(login.getEmail(), login.getSenha())) {
                return ResponseEntity.status(200).body(repositoryGerente.findByEmailAndSenha(login.getEmail(), login.getSenha()));
            } else {
                return ResponseEntity.status(204).body("Gerente não encontrado!");
            }
    }

    @CrossOrigin
    @PostMapping("/csv-download")
    public ResponseEntity<String> postCsv(@RequestParam MultipartFile file) throws IOException {
        if(!Objects.equals(file.getContentType(), "text/csv")){
            return ResponseEntity.status(400).body("Arquivo enviado não está no formato correto. Envie um arquivo .csv :).");
        } else {
            byte[] fileBytes = file.getBytes();
            String arquivo = new String(fileBytes);
            ArrayList<String> response = new RegistroArquivo().insereRegistrosDeArquivo(arquivo, new Escola());
            if(response.size() > 0){
                StringBuilder summary = new StringBuilder();
                for (String index: response) {
                    summary.append(index);
                }
                return ResponseEntity.status(204).body(summary.toString());
            } else {
                return ResponseEntity.status(200).body("OK");
            }

        }
    }

}
