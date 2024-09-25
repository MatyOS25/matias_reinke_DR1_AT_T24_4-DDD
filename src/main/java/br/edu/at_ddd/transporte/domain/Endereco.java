package br.edu.at_ddd.transporte.domain;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Embeddable
@Data
public class Endereco {
    private String rua;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    
    @Override
    public String toString() {
        return String.format("%s, %s - %s, %s - %s, %s - CEP: %s",
                rua, numero, complemento, bairro, cidade, estado, cep);
    }
}
