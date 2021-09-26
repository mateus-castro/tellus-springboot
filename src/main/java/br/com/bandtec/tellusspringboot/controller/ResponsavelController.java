package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.dominio.Escola;
import br.com.bandtec.tellusspringboot.dominio.Login;
import br.com.bandtec.tellusspringboot.dominio.Responsavel;
import br.com.bandtec.tellusspringboot.repositorio.EscolaRepository;
import br.com.bandtec.tellusspringboot.repositorio.ResponsavelRepository;
import br.com.bandtec.tellusspringboot.utils.ListaObjeto;
import br.com.bandtec.tellusspringboot.utils.Requisicao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static br.com.bandtec.tellusspringboot.TellusApplication.listaReqTratadas;

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

//    @GetMapping
//    public ResponseEntity getResponsavelPorProtocolo(@RequestParam("protocolo") String protocolo) {
//        if (listaReqTratadas.getTamanho() > 0) {
//            Requisicao requisicao = lerListaRecursiva(0, protocolo);
//            if (requisicao != null) {
//                return ResponseEntity.status(200).body(requisicao);
//            }
//        }
//        return ResponseEntity.status(204).body("Nenhuma requisição foi concluída até o momento - " + LocalDateTime.now());
//    }

    @PostMapping
    public ResponseEntity postResponsavel(@RequestBody Responsavel responsavel) {
        String protocolo = UUID.randomUUID().toString();
        LocalDateTime previsao = LocalDateTime.now().plusSeconds(40);
        return ResponseEntity.status(200).body("Protocolo correspondente a requisição: " + protocolo +
                "\nPrevisão " + previsao.toString());
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
    public ResponseEntity<Responsavel> getLogin(@RequestBody Login login) {
            if (repositoryResponsavel.existsByEmailAndSenha(login.getEmail(), login.getSenha())) {
                return ResponseEntity.status(200).body(repositoryResponsavel.findResponsavelByEmailAndSenha(login.getEmail(), login.getSenha()));
            } else {
                return ResponseEntity.status(204).body(null);
            }
    }
}

