package br.com.bandtec.tellusspringboot.repositorio;

import br.com.bandtec.tellusspringboot.dominio.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GerenteRepository extends JpaRepository<Gerente, Integer> {
    List<Gerente> findAll();

    Gerente findByCpfAndSenha(String cpf, String senha);
    boolean existsByCpfAndSenha(String cpf, String senha);
    boolean existsByCpf(String cpf);
    void deleteByCpf(String cpf);
}
