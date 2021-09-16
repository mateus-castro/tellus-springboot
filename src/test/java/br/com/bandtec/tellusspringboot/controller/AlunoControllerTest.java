package br.com.bandtec.tellusspringboot.controller;

import br.com.bandtec.tellusspringboot.dominio.Aluno;
import br.com.bandtec.tellusspringboot.repositorio.AlunoRepository;
import br.com.bandtec.tellusspringboot.utils.PilhaObjeto;
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
class AlunoControllerTest {
    @MockBean
    AlunoRepository repositoryAluno;

    @Autowired
    AlunoController controller;
    @Test
    void getAluno() {
        List<Aluno> alunoTeste = Arrays.asList(new Aluno(), new Aluno());

        Mockito.when(repositoryAluno.findAll()).thenReturn(alunoTeste);

        ResponseEntity<List<Aluno>> resposta = controller.getAluno();

        assertEquals(200, resposta.getStatusCodeValue());
        assertEquals(2, resposta.getBody().size());
    }

    @Test
    void postAluno() {
        Aluno aluno = new Aluno();
        aluno.setId(123);
        aluno.setNome("Aluninho");

        assertEquals(controller.postAluno(aluno).getStatusCodeValue(), 201);
    }

    @Test
    void deleteAluno() {
        PilhaObjeto<Aluno> pilha = new PilhaObjeto<>(10);
        Aluno aluno = new Aluno();
        aluno.setId(10);
        aluno.setNome("Nome");

        pilha.push(aluno);
        Mockito.when(repositoryAluno.existsById(10)).thenReturn(true);
        Optional<Aluno> retorno = Optional.of(aluno);
        Mockito.when(repositoryAluno.findById(10)).thenReturn(retorno);

        assertEquals(controller.deleteAluno(10).getStatusCodeValue(), 200);
        assertEquals(controller.deleteAluno(11).getStatusCodeValue(), 204);
    }

    @Test
    void desfazerPost() {

    }

    @Test
    void desfazerDelete() {
    }
}