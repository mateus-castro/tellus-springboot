package br.com.bandtec.tellusspringboot.repositories;

import br.com.bandtec.tellusspringboot.domains.Escola;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EscolaRepository extends JpaRepository<Escola, Integer> {
    List<Escola> findAll();
    Boolean existsByCnpj(String cnpj);
    Escola findByCnpj(String cnpj);
}
