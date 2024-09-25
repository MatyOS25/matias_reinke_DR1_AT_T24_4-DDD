package br.edu.at_ddd.transporte.domain;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Entrega {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    private UUID pedidoId;
    
    @Embedded
    private Endereco enderecoEntrega;
    
    private LocalDateTime dataPrevisaoEntrega;
    
    @Enumerated(EnumType.STRING)
    private StatusEntrega status;
    
    public void atualizarStatus(StatusEntrega novoStatus) {
        this.status = novoStatus;
    }
}
