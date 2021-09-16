package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.dominio.Gerente;
import br.com.bandtec.tellusspringboot.dominio.Login;
import br.com.bandtec.tellusspringboot.dominio.Responsavel;

import static br.com.bandtec.tellusspringboot.utils.Requisicao.filaReq;

import br.com.bandtec.tellusspringboot.repositorio.GerenteRepository;
import br.com.bandtec.tellusspringboot.repositorio.ResponsavelRepository;
import br.com.bandtec.tellusspringboot.utils.ListaObjeto;
import br.com.bandtec.tellusspringboot.utils.Requisicao;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    public static ListaObjeto<Requisicao> listaReqTratadas = new ListaObjeto<>(20);

    @Autowired
    private ResponsavelRepository repositoryResponsavel;
    @Autowired
    private GerenteRepository repositoryGerente;

    @CrossOrigin
    @GetMapping("/gerente")
    public ResponseEntity getGerente() {
        List<Gerente> lista = repositoryGerente.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(lista);
        }
    }

    @CrossOrigin
    @PostMapping("/gerente")
    public ResponseEntity postGerente(@RequestBody Gerente gerente) {
        repositoryGerente.save(gerente);
        return ResponseEntity.status(201).build();
    }

    @CrossOrigin
    @DeleteMapping("/gerente/{cpf}")
    public ResponseEntity deleteGerente(@PathVariable String cpf) {
        if (repositoryGerente.existsByCpf(cpf)) {
            repositoryGerente.deleteByCpf(cpf);
            return ResponseEntity.status(200).body("Gerente deletado com sucesso");
        }
        return ResponseEntity.status(204).body("Gerente com o cpf: " + cpf + "Não foi encontrado");
    }

    @CrossOrigin
    @GetMapping("/responsavel")
    public ResponseEntity getResponsavel() {
        List<Responsavel> lista = repositoryResponsavel.findAll();
        if (lista.isEmpty()) {
            return ResponseEntity.status(204).build();
        } else {
            return ResponseEntity.status(200).body(lista);
        }
    }

    public Requisicao lerListaRecursiva(int i, String protocolo) {
        if (listaReqTratadas.getTamanho() > 0) {
            if (listaReqTratadas.getElemento(i).getProtocolo().equals(protocolo)) {
                Requisicao req = listaReqTratadas.getElemento(i);
                listaReqTratadas.removePeloIndice(i);
                return req;
            }
            lerListaRecursiva(i + 1, protocolo);
        }
        return null;
    }

    @GetMapping("/responsavel/{protocolo}")
    public ResponseEntity getResponsavelPorProtocolo(@PathVariable String protocolo) {
        if (listaReqTratadas.getTamanho() > 0) {
            Requisicao requisicao = lerListaRecursiva(0, protocolo);
            if (requisicao != null) {
                return ResponseEntity.status(200).body(requisicao);
            }
        }
        return ResponseEntity.status(204).body("Nenhuma requisição foi concluída até o momento - " + LocalDateTime.now());
    }

    @PostMapping("/responsavel")
    public ResponseEntity postResponsavel(@RequestBody Responsavel responsavel) {
        String protocolo = UUID.randomUUID().toString();
        LocalDateTime previsao = LocalDateTime.now().plusSeconds(40);
        Thread enfileirarRequisicoes = new Thread(() -> {
            try {
                Thread.sleep(10000);
                filaReq.insert(new Requisicao(protocolo, responsavel, previsao));
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
        enfileirarRequisicoes.start();
        return ResponseEntity.status(200).body("Protocolo correspondente a requisição: " + protocolo +
                "\nPrevisão " + previsao.toString());
    }

    @CrossOrigin
    @DeleteMapping("/responsavel/{cpf}")
    public ResponseEntity deleteResponsavel(@PathVariable String cpf) {
        if (repositoryResponsavel.existsByCpf(cpf)) {
            repositoryResponsavel.deleteByCpf(cpf);
            return ResponseEntity.status(200).body("Responsavel deletado com sucesso");
        }
        return ResponseEntity.status(404).body("Responsavel com o CPF: " + cpf + "Não foi encontrado");
    }

    @PutMapping("/{cpf}/{telefone}")
    public ResponseEntity updateResponsavel(@PathVariable String cpf, @PathVariable String telefone) {
        if (repositoryResponsavel.existsByCpf(cpf)) {
            Responsavel resp = repositoryResponsavel.findResponsavelByCpf(cpf);
            resp.setTelefone(telefone);
            repositoryResponsavel.save(resp);
            return ResponseEntity.status(200).build();
        }
        return ResponseEntity.status(204).body("Responsável não encontrado.");
    }

    @CrossOrigin
    @GetMapping("/login/{cpf}/{senha}/{isResp}")
    public ResponseEntity getLogin(@PathVariable String cpf, @PathVariable String senha, @PathVariable Boolean isResp) {
        Login login = new Login();
        login.setCpf(cpf);
        login.setSenha(senha);
        login.setResponsavel(isResp);
        if (!login.isResponsavel()) {
            if (repositoryGerente.existsByCpfAndSenha(login.getCpf(), login.getSenha())) {
                return ResponseEntity.status(200).body(repositoryGerente.findByCpfAndSenha(login.getCpf(), login.getSenha()));
            } else {
                return ResponseEntity.status(204).body("Gerente não encontrado!");
            }

        } else {
            if (repositoryResponsavel.existsByCpfAndSenha(login.getCpf(), login.getSenha())) {
                return ResponseEntity.status(200).body(repositoryResponsavel.findResponsavelByCpfAndSenha(login.getCpf(), login.getSenha()));
            } else {
                return ResponseEntity.status(204).body("Responsável não encontrado!");
            }
        }
    }
}
