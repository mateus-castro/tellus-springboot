package br.com.bandtec.tellusspringboot.repositories;

import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.domains.Gerente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GerenteRepository extends JpaRepository<Gerente, Integer> {
    List<Gerente> findGerentesByFkEscola(Escola escola);

    Gerente findGerenteByEmailAndSenha(String email, String senha);
    Optional<Gerente> findGerenteByEmail(String email);
    Gerente findByCpf(String cpf);
    boolean existsByEmailAndSenha(String email, String senha);
    boolean existsByCpf(String cpf);
    void deleteByCpf(String cpf);
}
