package br.com.bandtec.tellusspringboot.repositorio;

import br.com.bandtec.tellusspringboot.dominio.Contrato;
import br.com.bandtec.tellusspringboot.dominio.Responsavel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
    List<Contrato> findAll();

    Contrato findByFkResponsavel(Responsavel responsavel);

    boolean existsByFkResponsavel(int idResponsavel);
}
