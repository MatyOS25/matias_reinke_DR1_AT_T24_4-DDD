package br.edu.at_ddd.transporte.repository;

import br.edu.at_ddd.transporte.domain.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface EntregaRepository extends JpaRepository<Entrega, UUID> {
}
