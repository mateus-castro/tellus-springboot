package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.dominio.Pagamento;
import br.com.bandtec.tellusspringboot.repositorio.PagamentoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;


import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PagamentoControllerTest {
    @MockBean
    PagamentoRepository repositoryPagamento;

    @Autowired
    PagamentoController controller;

    @Test
    void postPagamento() {
        Pagamento pagamento = new Pagamento();
        pagamento.setId(123);
        pagamento.setTipo("Boleto");

        assertEquals(controller.postPagamento(pagamento).getStatusCodeValue(), 201);
    }

    @Test
    void getCsv() {
    }

    @Test
    void getTxt() {
    }

    @Test
    void gravaListaCsv() {

    }

    @Test
    void gravaListaTxt() {
    }
}