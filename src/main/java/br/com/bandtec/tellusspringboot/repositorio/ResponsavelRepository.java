package br.com.bandtec.tellusspringboot.repositorio;

import br.com.bandtec.tellusspringboot.dominio.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResponsavelRepository extends JpaRepository<Responsavel, Integer> {

    List<Responsavel> findAll();

    Responsavel findResponsavelByCpfAndSenha(String cpf, String senha);
    Responsavel findResponsavelByCpf(String cpf);

    boolean existsByCpfAndSenha(String cpf, String senha);
    boolean existsByCpf(String cpf);
    void deleteByCpf(String cpf);
}