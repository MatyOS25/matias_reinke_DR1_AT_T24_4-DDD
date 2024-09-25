package br.edu.at_ddd.almoxarifado.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Quantidade {
    private int valor;
    
    public Quantidade(int valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa");
        }
        this.valor = valor;
    }
    
    public Quantidade adicionar(int quantidade) {
        return new Quantidade(this.valor + quantidade);
    }
    
    public Quantidade subtrair(int quantidade) {
        return new Quantidade(Math.max(0, this.valor - quantidade));
    }
}
