package br.com.bandtec.tellusspringboot.repositories;

import br.com.bandtec.tellusspringboot.domains.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {

    Aluno findAlunoByNome(String nome);
    Aluno findAlunoByRa(String ra);
    boolean existsAlunoByRa(String ra);
    boolean existsAlunoByNome(String nome);
}
