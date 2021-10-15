package br.com.bandtec.tellusspringboot.repositories;

import br.com.bandtec.tellusspringboot.domains.Contrato;
import br.com.bandtec.tellusspringboot.domains.Escola;
import br.com.bandtec.tellusspringboot.domains.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
    List<Contrato> findAll();

    Contrato findFirstByFkResponsavel(Responsavel responsavel);
    List<Contrato> findAllByFkEscola(Escola escola);

    boolean existsByFkResponsavel(int idResponsavel);
}
