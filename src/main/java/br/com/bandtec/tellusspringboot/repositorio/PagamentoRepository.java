package br.com.bandtec.tellusspringboot.repositorio;

import br.com.bandtec.tellusspringboot.dominio.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PagamentoRepository extends JpaRepository<Pagamento, Integer> {
    List<Pagamento> findAll();

}
