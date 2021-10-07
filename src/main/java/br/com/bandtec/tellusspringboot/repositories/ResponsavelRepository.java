package br.com.bandtec.tellusspringboot.repositories;

import br.com.bandtec.tellusspringboot.domains.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponsavelRepository extends JpaRepository<Responsavel, Integer> {

    List<Responsavel> findAll();

    Responsavel findResponsavelByEmailAndSenha(String email, String senha);
    Responsavel findResponsavelByCpf(String cpf);

    boolean existsByEmailAndSenha(String email, String senha);
    boolean existsByCpf(String cpf);
    void deleteByCpf(String cpf);
}
