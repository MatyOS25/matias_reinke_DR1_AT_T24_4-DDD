# Perguntas e Respostas sobre Domain Events e Microsserviços

## 1. Projetar softwares usando "domain events":
O módulo PetFriends_Web foi desenvolvido em ReactJS, acessando os microsserviços de Clientes, Produtos e Pedidos de forma síncrona, via REST API. Que funcionalidade síncrona executada pelo cliente é diretamente afetada pelos eventos de domínio?

**Resposta:** A funcionalidade síncrona que é diretamente afetada pelos eventos de domínio é a atualização do status do pedido. Quando um cliente faz uma requisição para verificar o status de um pedido, essa informação precisa estar atualizada com base nos eventos de domínio que ocorreram. Por exemplo, se um evento de domínio "PedidoEnviado" foi disparado pelo microsserviço de Pedidos, a próxima vez que o cliente consultar o status do pedido via API REST, essa informação deve refletir o novo estado. Assim, a consulta síncrona do status do pedido é diretamente impactada pelos eventos de domínio que ocorrem nos bastidores do sistema.

## 2. Explique de forma sucinta qual é a diferença de enviar eventos somente com o ID do agregado e enviar um payload completo?

**Resposta:** A principal diferença entre enviar eventos apenas com o ID do agregado e enviar um payload completo está na quantidade de informações transmitidas e na flexibilidade do sistema.

Ao enviar apenas o ID do agregado:
- Reduz-se o volume de dados trafegados na rede.
- O serviço receptor precisa fazer uma consulta adicional para obter os detalhes completos do agregado.
- Mantém-se um acoplamento mais baixo entre os serviços.
- Garante-se que o serviço receptor sempre terá os dados mais atualizados ao consultar o agregado.

Ao enviar um payload completo:
- Aumenta-se o volume de dados trafegados na rede.
- O serviço receptor tem acesso imediato a todas as informações necessárias.
- Pode haver um acoplamento maior entre os serviços.
- Existe o risco de trabalhar com dados desatualizados se o agregado for modificado após o envio do evento.

A escolha entre essas abordagens depende das necessidades específicas do sistema, considerando fatores como latência, consistência dos dados e acoplamento entre os serviços.

## 3. A partir da resposta dada na questão anterior, como você projetaria o evento a ser enviado pelo PetFriends_Pedido para o PetFriends_Almoxarifado?

**Resposta:** Para o evento enviado do PetFriends_Pedido para o PetFriends_Almoxarifado, eu optaria por enviar um payload que contenha o ID do pedido e informações essenciais sobre os produtos. Isso porque o Almoxarifado precisa saber quais produtos e em que quantidade devem ser reservados ou liberados, mas não necessita de todos os detalhes do pedido. O evento poderia ser estruturado assim:


{
"eventType": "PedidoConfirmado",
"pedidoId": "123e4567-e89b-12d3-a456-426614174000",
"itens": [
{
"produtoId": "789e4567-e89b-12d3-a456-426614174001",
"quantidade": 2
},
{
"produtoId": "456e4567-e89b-12d3-a456-426614174002",
"quantidade": 1
}
]
}


Essa abordagem fornece informações suficientes para que o Almoxarifado possa atualizar seu estoque sem precisar consultar o serviço de Pedidos novamente, mas mantém o evento relativamente leve e focado nas informações relevantes para o contexto do Almoxarifado.

## 4. A partir da resposta dada na questão anterior, como você projetaria o evento a ser enviado pelo PetFriends_Pedido para o PetFriends_Transporte?

**Resposta:** Para o evento enviado do PetFriends_Pedido para o PetFriends_Transporte, eu projetaria um evento que contenha o ID do pedido e as informações necessárias para iniciar o processo de entrega. O serviço de Transporte precisa de detalhes sobre o destinatário e o endereço de entrega, mas não necessariamente dos produtos específicos. O evento poderia ser estruturado assim:

{
"eventType": "PedidoProntoParaEnvio",
"pedidoId": "123e4567-e89b-12d3-a456-426614174000",
"clienteId": "321e4567-e89b-12d3-a456-426614174003",
"enderecoEntrega": {
"rua": "Rua das Flores",
"numero": "123",
"complemento": "Apto 45",
"bairro": "Centro",
"cidade": "São Paulo",
"estado": "SP",
"cep": "01234-567"
},
"volumeTotal": 3,
"pesoTotal": 5.5
}

## 5. Explique de forma sucinta o que é um Gateway de Serviço, suas vantagens e desvantagens.

**Resposta:** Um Gateway de Serviço é um componente que atua como ponto de entrada único para uma arquitetura de microsserviços, gerenciando e roteando as requisições dos clientes para os serviços apropriados.

Vantagens:
- Centraliza a autenticação e autorização
- Simplifica o gerenciamento de APIs
- Permite balanceamento de carga e cache
- Facilita a implementação de políticas de segurança

Desvantagens:
- Pode se tornar um ponto único de falha
- Adiciona uma camada extra de latência
- Pode aumentar a complexidade da arquitetura
- Requer configuração e manutenção adicionais

## 6. O que é ID de Correlação e quais são os seus pré-requisitos?

**Resposta:** ID de Correlação é um identificador único gerado para cada requisição que passa por múltiplos serviços em uma arquitetura de microsserviços. Ele permite rastrear e correlacionar todas as operações relacionadas a uma única transação.

Pré-requisitos:
- Implementação consistente em todos os serviços
- Propagação do ID através de chamadas entre serviços
- Inclusão do ID em logs e mensagens de rastreamento
- Suporte da infraestrutura para capturar e armazenar os IDs

## 7. Qual é a função do Spring Cloud Sleuth e sua relação com o serviço Zipkin?

**Resposta:** Spring Cloud Sleuth é uma biblioteca que adiciona rastreamento distribuído a aplicações Spring Boot. Sua função principal é gerar e propagar IDs de rastreamento e span através dos serviços.

Relação com Zipkin:
- Sleuth gera os dados de rastreamento
- Zipkin coleta, armazena e visualiza esses dados
- Sleuth pode ser configurado para enviar dados diretamente para o Zipkin
- Juntos, fornecem uma solução completa de rastreamento distribuído

## 8. Explique de forma sucinta o que é Agregador de Logs, suas vantagens e desvantagens.

**Resposta:** Um Agregador de Logs é uma ferramenta que coleta, centraliza e analisa logs de múltiplos serviços e sistemas distribuídos.

Vantagens:
- Centraliza logs de diferentes fontes
- Facilita a análise e correlação de eventos
- Melhora a capacidade de detecção e resolução de problemas
- Suporta retenção e arquivamento de logs a longo prazo

Desvantagens:
- Pode requerer recursos significativos de infraestrutura
- Pode adicionar complexidade à arquitetura
- Requer configuração e manutenção adicionais
- Pode haver desafios de segurança e privacidade ao centralizar logs
