package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.dominio.Contrato;
import br.com.bandtec.tellusspringboot.dominio.Responsavel;
import br.com.bandtec.tellusspringboot.repositorio.ContratoRepository;
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
class ContratoControllerTest {
    @MockBean
    ContratoRepository repositoryContrato;

    @MockBean
    private ResponsavelRepository repositoryResponsavel;

    @Autowired
    ContratoController controller;

    @Test
    void getContrato() {
        List<Contrato> contratoTeste = Arrays.asList(new Contrato(), new Contrato());

        Mockito.when(repositoryContrato.findAll()).thenReturn(contratoTeste);

        ResponseEntity<List<Contrato>> resposta = controller.getContrato();

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(2, resposta.getBody().size());
    }

    @Test
    void postContrato() {
        Contrato contrato = new Contrato();
        contrato.setId(123);

        assertEquals(controller.postContrato(contrato).getStatusCodeValue(), 201);
    }

    @Test
    void getContratoPorResponsavel() {
        Responsavel responsavel = new Responsavel();
        responsavel.setId(1);

        Contrato contrato = new Contrato();
        contrato.setFkResponsavel(responsavel);

        Optional<Responsavel> retorno = Optional.of(responsavel);
        Mockito.when(repositoryResponsavel.findById(1)).thenReturn(retorno);
        Mockito.when(repositoryContrato.findByFkResponsavel(retorno.get())).thenReturn(contrato);

        assertEquals(controller.getContrato("11").getStatusCodeValue(), 204);
    }
}