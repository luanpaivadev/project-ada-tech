# ADR 0002: Utilização do RabbitMQ para Comunicação Assíncrona

## Status

Aceito

## Contexto

Nossa arquitetura de microserviços requer um mecanismo de comunicação entre serviços que:

- Permita comunicação assíncrona para operações que não exigem resposta imediata
- Garanta a entrega de mensagens mesmo em caso de falha temporária de um serviço
- Suporte diferentes padrões de mensageria (pub/sub, filas, etc.)
- Permita desacoplamento entre produtores e consumidores de mensagens
- Seja escalável para lidar com picos de carga

Especificamente, precisamos de um sistema de mensageria para:

1. Notificar o Product Service quando um pagamento é aprovado para atualização do inventário
2. Notificar o Invoice Service para emissão de nota fiscal após confirmação de pagamento
3. Enviar atualizações de status de pedido para o Notification Service
4. Implementar padrões de saga para transações distribuídas

## Decisão

Adotaremos o RabbitMQ como nosso message broker para comunicação assíncrona entre microserviços.

Implementaremos as seguintes filas principais:

1. `product.payment_approved`: Notifica o Product Service sobre pagamentos aprovados
2. `notification.status_order`: Envia atualizações de status para o Notification Service
3. `order.inventory_successfully_updated`: Notifica o Order Service que o inventário foi atualizado
4. `order.tax_invoice_issued`: Notifica o Order Service que a nota fiscal foi emitida

Cada serviço será responsável por:
- Definir as filas que produz e consome
- Implementar tratamento de erros e retentativas
- Garantir idempotência no processamento de mensagens

## Consequências

### Positivas

- Desacoplamento temporal entre serviços (não precisam estar disponíveis simultaneamente)
- Melhor resiliência a falhas (mensagens persistidas até serem processadas)
- Capacidade de absorver picos de carga (filas atuam como buffer)
- Suporte a diferentes padrões de troca de mensagens (direct, fanout, topic, etc.)
- Facilidade para implementar padrões como CQRS e Event Sourcing
- Interface de administração para monitoramento e depuração

### Negativas

- Complexidade adicional na infraestrutura (mais um componente para manter)
- Necessidade de lidar com mensagens duplicadas (idempotência)
- Desafios de depuração em fluxos assíncronos
- Latência adicional para operações que poderiam ser síncronas
- Curva de aprendizado para a equipe

## Alternativas Consideradas

### Apache Kafka

**Prós**:
- Maior throughput para volumes muito grandes de mensagens
- Melhor para análise de streams de eventos
- Retenção de mensagens por tempo configurável
- Melhor para event sourcing

**Contras**:
- Maior complexidade operacional
- Requer ZooKeeper (até versões recentes)
- Curva de aprendizado mais íngreme
- Overhead para nosso volume atual de mensagens

### Amazon SQS/SNS

**Prós**:
- Serviço gerenciado (sem preocupação com infraestrutura)
- Integração nativa com outros serviços AWS
- Escalabilidade automática

**Contras**:
- Lock-in com provedor específico
- Limitações em alguns padrões de mensageria
- Custos baseados em volume de mensagens
- Menos flexibilidade para configurações avançadas

### Comunicação Síncrona via REST

**Prós**:
- Simplicidade de implementação
- Resposta imediata
- Sem componentes adicionais

**Contras**:
- Acoplamento temporal (ambos os serviços precisam estar disponíveis)
- Menor resiliência a falhas
- Propagação de falhas entre serviços
- Dificuldade para implementar transações distribuídas

## Implementação

A implementação do RabbitMQ será feita usando:

- Container Docker para o RabbitMQ
- Spring AMQP para integração com os serviços Java
- Configuração de filas durável para garantir persistência de mensagens
- Dead Letter Queues para mensagens que não podem ser processadas
- Retry policies para tentativas automáticas em caso de falha

## Referências

- [RabbitMQ Documentation](https://www.rabbitmq.com/documentation.html)
- [Enterprise Integration Patterns](https://www.enterpriseintegrationpatterns.com/)
- [Spring AMQP Documentation](https://docs.spring.io/spring-amqp/docs/current/reference/html/)
- [Microservices Patterns: With Examples in Java](https://microservices.io/book)