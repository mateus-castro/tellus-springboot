package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.dominio.Gerente;
import br.com.bandtec.tellusspringboot.dominio.Login;
import br.com.bandtec.tellusspringboot.dominio.Responsavel;
import br.com.bandtec.tellusspringboot.repositorio.GerenteRepository;
import br.com.bandtec.tellusspringboot.repositorio.ResponsavelRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UsuarioControllerTest {

    @MockBean
    private ResponsavelRepository repositoryResponsavel;

    @MockBean
    private GerenteRepository repositoryGerente;

    @Autowired
    UsuarioController controller;
    @Test
    void getGerente() {
        List<Gerente> gerenteTeste = Arrays.asList(new Gerente(), new Gerente());

        Mockito.when(repositoryGerente.findAll()).thenReturn(gerenteTeste);

        ResponseEntity<List<Gerente>> resposta = controller.getGerente();

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(2, resposta.getBody().size());
    }

    @Test
    void postGerente() {
        Gerente gerente = new Gerente();
        gerente.setCpf("123");
        gerente.setNome("Gerente");

        assertEquals(controller.postGerente(gerente).getStatusCodeValue(), 201);
    }

    @Test
    void deleteGerente() {
        Gerente gerente = new Gerente();
        gerente.setCpf("123");
        gerente.setNome("Gerente");

        Mockito.when(repositoryGerente.existsByCpf(gerente.getCpf())).thenReturn(true);
        Optional<Gerente> retorno = Optional.empty();
        Mockito.when(repositoryGerente.findById(11)).thenReturn(retorno);

        assertEquals(controller.deleteGerente(gerente.getCpf()).getStatusCodeValue(), 200);
        assertEquals(controller.deleteGerente("1337").getStatusCodeValue(), 204);
    }

    @Test
    void getResponsavel() {
        List<Responsavel> responsavelTeste = Arrays.asList(new Responsavel(), new Responsavel());

        Mockito.when(repositoryResponsavel.findAll()).thenReturn(responsavelTeste);

        ResponseEntity<List<Responsavel>> resposta = controller.getResponsavel();

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(2, resposta.getBody().size());
    }

    @Test
    void getResponsavelPorProtocolo() {
    }

    @Test
    void postResponsavel() {
        Responsavel responsavel = new Responsavel();
        responsavel.setCpf("123");
        responsavel.setNome("Gerente");

        assertEquals(controller.postResponsavel(responsavel).getStatusCodeValue(), 200);
    }

    @Test
    void deleteResponsavel() {
        Responsavel responsavel = new Responsavel();
        responsavel.setCpf("123");
        responsavel.setNome("Responsavel");

        Mockito.when(repositoryResponsavel.existsByCpf(responsavel.getCpf())).thenReturn(true);
        Optional<Responsavel> retorno = Optional.empty();
        Mockito.when(repositoryResponsavel.findById(11)).thenReturn(retorno);

        assertEquals(controller.deleteResponsavel(responsavel.getCpf()).getStatusCodeValue(), 200);
        assertEquals(controller.deleteGerente("1337").getStatusCodeValue(), 204);
    }

    @Test
    void updateResponsavel() {
        Responsavel responsavel = new Responsavel();
        responsavel.setCpf("123");
        responsavel.setTelefone("(11)912341234");

        Mockito.when(repositoryResponsavel.existsByCpf(responsavel.getCpf())).thenReturn(true);
        Mockito.when(repositoryResponsavel.findResponsavelByCpf(responsavel.getCpf())).thenReturn(responsavel);

        ResponseEntity resposta = controller.updateResponsavel(responsavel.getCpf(), responsavel.getTelefone());

        assertEquals(200, resposta.getStatusCodeValue());
    }

    @Test
    void getLoginResponsavel() {
        Login login = new Login();
        login.setCpf("123");
        login.setSenha("senha");
        login.setResponsavel(true);

        Mockito.when(repositoryResponsavel.existsByCpfAndSenha(login.getCpf(), login.getSenha())).thenReturn(true);
        ResponseEntity resposta = controller.getLogin(login.getCpf(), login.getSenha(), login.isResponsavel());

        assertEquals(200, resposta.getStatusCodeValue());
    }

    @Test
    void getLoginGerente() {
        Login login = new Login();
        login.setCpf("123");
        login.setSenha("senha");
        login.setResponsavel(false);

        Mockito.when(repositoryGerente.existsByCpfAndSenha(login.getCpf(), login.getSenha())).thenReturn(true);
        ResponseEntity resposta = controller.getLogin(login.getCpf(), login.getSenha(), login.isResponsavel());

        assertEquals(200, resposta.getStatusCodeValue());
    }
}