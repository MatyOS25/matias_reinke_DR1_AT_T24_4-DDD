package br.edu.at_ddd.almoxarifado.repository;

import br.edu.at_ddd.almoxarifado.domain.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> {
}
