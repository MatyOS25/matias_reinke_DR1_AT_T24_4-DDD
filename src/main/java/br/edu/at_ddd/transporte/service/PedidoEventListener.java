package br.edu.at_ddd.transporte.service;

import br.edu.at_ddd.transporte.domain.Entrega;
import br.edu.at_ddd.transporte.domain.Endereco;
import br.edu.at_ddd.transporte.domain.StatusEntrega;
import br.edu.at_ddd.transporte.repository.EntregaRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Service
public class PedidoEventListener {

    private final EntregaRepository entregaRepository;

    public PedidoEventListener(EntregaRepository entregaRepository) {
        this.entregaRepository = entregaRepository;
    }

    @RabbitListener(queues = "#{rabbitMQConfig.QUEUE_TRANSPORTE}")
    public void receberEventoPedido(Map<String, Object> evento) {
        String eventType = (String) evento.get("eventType");
        if ("PedidoProntoParaEnvio".equals(eventType)) {
            processarPedidoProntoParaEnvio(evento);
        }
    }

    private void processarPedidoProntoParaEnvio(Map<String, Object> evento) {
        UUID pedidoId = UUID.fromString((String) evento.get("pedidoId"));
        UUID clienteId = UUID.fromString((String) evento.get("clienteId"));
        Map<String, String> enderecoMap = (Map<String, String>) evento.get("enderecoEntrega");

        Endereco endereco = new Endereco();
        endereco.setRua(enderecoMap.get("rua"));
        endereco.setNumero(enderecoMap.get("numero"));
        endereco.setComplemento(enderecoMap.get("complemento"));
        endereco.setBairro(enderecoMap.get("bairro"));
        endereco.setCidade(enderecoMap.get("cidade"));
        endereco.setEstado(enderecoMap.get("estado"));
        endereco.setCep(enderecoMap.get("cep"));

        Entrega entrega = new Entrega();
        entrega.setPedidoId(pedidoId);
        entrega.setEnderecoEntrega(endereco);
        entrega.setDataPrevisaoEntrega(LocalDateTime.now().plusDays(5)); // Exemplo: previsão de 5 dias
        entrega.setStatus(StatusEntrega.PENDENTE);

        entregaRepository.save(entrega);

        System.out.println("Entrega criada para o pedido: " + pedidoId);
    }
}
