package br.edu.at_ddd.almoxarifado.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

@Entity
@Data
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private String nome;
    private String descricao;
    
    @Embedded
    private Quantidade quantidade;
    
    public void adicionarEstoque(int quantidade) {
        this.quantidade = this.quantidade.adicionar(quantidade);
    }
    
    public void removerEstoque(int quantidade) {
        this.quantidade = this.quantidade.subtrair(quantidade);
    }
}
