package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.controllers.EscolaController;
import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.repositories.EscolaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EscolaControllerTest {
    @MockBean
    EscolaRepository repositoryEscola;

//    @Autowired
//    EscolaController controller;

//    @Test
//    void getEscola() {
//        List<Escola> escolaTeste = Arrays.asList(new Escola(), new Escola());
//
//        Mockito.when(repositoryEscola.findAll()).thenReturn(escolaTeste);
//
//        ResponseEntity<List<Escola>> resposta = controller.getEscola();
//
//        assertEquals(200, resposta.getStatusCodeValue());
//        assertEquals(2, resposta.getBody().size());
//    }

//    @Test
//    void getContrato() {
//        Escola escola = new Escola();
//        escola.setCnpj("123");
//
//        Mockito.when(repositoryEscola.existsByCnpj("123")).thenReturn(true);
//        Mockito.when(repositoryEscola.findByCnpj("123")).thenReturn(escola);
//
//        assertEquals(controller.getContrato("123").getStatusCodeValue(), 200);
//        assertEquals(controller.getContrato("321").getStatusCodeValue(), 204);
//    }
//
//    @Test
//    void postEscola() {
//        Escola escola = new Escola();
//        escola.setCnpj("123");
//        escola.setRazaoSocial("Escolinha");
//
//        assertEquals(controller.postEscola(escola).getStatusCodeValue(), 201);
//    }
}