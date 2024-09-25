package br.edu.at_ddd.almoxarifado.service;

import br.edu.at_ddd.almoxarifado.domain.Produto;
import br.edu.at_ddd.almoxarifado.repository.ProdutoRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PedidoEventListener {

    private final ProdutoRepository produtoRepository;

    public PedidoEventListener(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @RabbitListener(queues = "#{rabbitMQConfig.QUEUE_ALMOXARIFADO}")
    public void receberEventoPedido(Map<String, Object> evento) {
        String eventType = (String) evento.get("eventType");
        if ("PedidoConfirmado".equals(eventType)) {
            processarPedidoConfirmado(evento);
        }
    }

    private void processarPedidoConfirmado(Map<String, Object> evento) {
        UUID pedidoId = UUID.fromString((String) evento.get("pedidoId"));
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> itens = (List<Map<String, Object>>) evento.get("itens");

        for (Map<String, Object> item : itens) {
            UUID produtoId = UUID.fromString((String) item.get("produtoId"));
            int quantidade = (Integer) item.get("quantidade");

            Produto produto = produtoRepository.findById(produtoId)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + produtoId));

            produto.removerEstoque(quantidade);
            produtoRepository.save(produto);
        }

        System.out.println("Estoque atualizado para o pedido: " + pedidoId);
    }
}
