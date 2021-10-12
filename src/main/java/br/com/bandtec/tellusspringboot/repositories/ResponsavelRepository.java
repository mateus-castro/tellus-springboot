package br.com.bandtec.tellusspringboot.repositories;

import br.com.bandtec.tellusspringboot.domains.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ResponsavelRepository extends JpaRepository<Responsavel, Integer> {
    Responsavel findResponsavelByEmailAndSenha(String email, String senha);
    Responsavel findResponsavelByCpf(String cpf);

    boolean existsByEmailAndSenha(String email, String senha);
    boolean existsByCpf(String cpf);
    void deleteByCpf(String cpf);
}
