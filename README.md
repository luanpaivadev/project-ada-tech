# Documentação do Projeto ADA Tech

## Visão Geral

Este projeto implementa uma arquitetura de microserviços para um sistema de e-commerce, com foco em processamento de pedidos, produtos, pagamentos, emissão de nota fiscal e notificações. A comunicação entre os serviços é realizada principalmente através de mensageria assíncrona utilizando RabbitMQ.

## Pré-requisitos

- Docker e Docker Compose
- Java 21
- Maven
- Git

## Iniciando os Serviços

Para iniciar todos os serviços de uma única vez, execute o seguinte comando na raiz do projeto:

```bash
docker-compose up -d --build
```

#### Usuários Pré-cadastrados

O sistema já possui dois usuários pré-cadastrados para testes:

| ID | Email | Nome | Senha | Papel |
| --- | --- | --- | --- | --- |
| 389d8a40-4d9b-4b90-8ace-029f04222915 | johndoe@email.com | John Doe | senha123!@ | ROLE_EMPLOYEE |
| 3f3afd64-8a39-4c1d-8ff8-ab13d7b2309e | janedoe@email.com | Jane Doe | senha123!@ | ROLE_USER |

**Importante**: 
- Para simular a criação de pedidos, utilize o usuário Jane Doe (`janedoe@email.com`) com a função `ROLE_USER`.
- Apenas usuários com a função `ROLE_EMPLOYEE` podem emitir notas fiscais, despachar pedidos e concluir a entrega do pedido. Para testar essas funcionalidades, utilize o usuário John Doe (`johndoe@email.com`).

## Permissões de Usuário

O sistema possui dois níveis de permissão para usuários humanos:

1. **ROLE_USER**: Permissões básicas para clientes
   - Criar e consultar pedidos
   - **Exemplo**: Use o usuário Jane Doe (`janedoe@email.com`) para simular a criação de pedidos

2. **ROLE_EMPLOYEE**: Permissões avançadas para funcionários
   - Listar produtos (apenas funcionários e serviços podem consultar produtos)
   - Emitir notas fiscais
   - Despachar pedidos (atualizar status para IN_DELIVERY)
   - Gerenciar produtos
   - **Exemplo**: Use o usuário John Doe (`johndoe@email.com`) para testar emissão de notas fiscais, despacho de pedidos e confirmação de entrega do pedido.

3. **ROLE_SERVICE**: Permissões para comunicação entre microserviços
   - Comunicação interna entre serviços
   - Acesso a endpoints protegidos

### Serviços Cadastrados no Auth-Service

O auth-service possui dois microserviços cadastrados que podem gerar tokens para comunicação interna:

| ID | Client ID | Client Secret | Roles |
|----|-----------|---------------|-------|
| 4c1da05b-449d-45e6-8b17-46d9e2ecf6ee | order-service | 66793dcf-fb4d-402e-8cef-5d5e66c9505e | ROLE_SERVICE |
| e164e48d-795e-4242-9093-6a1774eb6503 | invoice-service | (valor encriptado) | ROLE_SERVICE |

#### Gerando Token para Serviços

Para gerar um token para comunicação entre serviços, utilize o seguinte endpoint:

```
GET http://localhost:8091/auth-service/v1/auth/internal-token?clientId=order-service&clientSecret=66793dcf-fb4d-402e-8cef-5d5e66c9505e
```

Substitua os valores de `clientId` e `clientSecret` conforme necessário.

### Fluxo de Teste Recomendado

1. Faça login como Jane Doe (ROLE_USER) para criar um pedido.
2. Faça login como John Doe (ROLE_EMPLOYEE) para emitir a nota fiscal, despachar o pedido e confirmar a entrega.
3. Volte para Jane Doe para verificar o status do pedido.

**Nota**: Os produtos já são criados automaticamente na inicialização do projeto product-service através do Flyway, então não é necessário criar produtos manualmente.

## Arquitetura do Sistema

O sistema é composto pelos seguintes microserviços:

1. **Auth Service**: Responsável pela autenticação e autorização dos usuários.
2. **Order Service**: Gerencia o ciclo de vida dos pedidos.
3. **Product Service**: Gerencia o catálogo de produtos e o inventário.
4. **Invoice Service**: Responsável pela emissão de notas fiscais.
5. **Notification Service**: Envia notificações aos clientes sobre o status dos pedidos.
6. **RabbitMQ Service**: Serviço de mensageria para comunicação assíncrona entre os microserviços.
7. **Grafana Service**: Monitoramento e visualização de métricas do sistema.

Os serviços se comunicam através do RabbitMQ para operações assíncronas.

## Monitoramento

- **RabbitMQ**: http://localhost:15672 (usuário: user, senha: 0d76739a-a5ec-4ee2-a847-56081ce6a6c9)
- **Grafana**: http://localhost:3000 (usuário: admin, senha: admin)

## Fluxo de Processamento de Pedidos

### 1. Criação do Pedido

**Ponto de Entrada**: `OrderController.createNewOrder()`

1. O cliente autenticado envia uma requisição POST para `/v1/orders` com os dados do pedido.
2. O sistema verifica a disponibilidade dos produtos no inventário através do `ProductServicePort`.
3. O pedido é criado com status `PAYMENT_PENDING`.
4. Uma mensagem é enviada para a fila `notification.status_order` para notificar o cliente sobre a criação do pedido.

### 2. Processamento do Pagamento

**Ponto de Entrada**: `OrderController.paymentWebhook()`

1. O gateway de pagamento envia uma notificação para o webhook `/v1/orders/webhook/payment`.
2. O sistema valida o cabeçalho `x-idempotency-key` para garantir que o pagamento não seja processado mais de uma vez.
3. O sistema atualiza o status do pedido de acordo com o resultado do pagamento:
   - Se aprovado: Status `PAYMENT_APPROVED` e envia mensagem para `product.payment_approved`
   - Se falhou: Status `PAYMENT_FAILURE` e envia notificação
   - Se rejeitado: Status `PAYMENT_REJECTED` e envia notificação

### 3. Atualização do Inventário

**Ponto de Entrada**: `UpdateInventoryListener.orderUpdateInventory()`

1. Após o pagamento ser aprovado, o Product Service atualiza o inventário.
2. Quando a atualização é concluída com sucesso, uma mensagem é enviada para a fila `order.inventory_successfully_updated`.
3. O Order Service recebe a mensagem e atualiza o status do pedido para `IN_SEPARATION`.
4. Uma notificação é enviada ao cliente sobre a mudança de status.

### 4. Emissão da Nota Fiscal

**Ponto de Entrada**: `TaxInvoiceIssuedListener.orderTaxInvoiceIssued()`

1. O Invoice Service processa o pedido e emite a nota fiscal.
2. Após a emissão, uma mensagem é enviada para a fila `order.tax_invoice_issued`.
3. O Order Service recebe a mensagem e atualiza o status do pedido para `TAX_INVOICE_ISSUED`.
4. Uma notificação é enviada ao cliente sobre a emissão da nota fiscal.

### 5. Separação e Entrega

**Ponto de Entrada**: `OrderController.updateStatusOrder()`

1. O sistema interno atualiza o status do pedido para `IN_DELIVERY` quando o pedido é enviado para entrega.
2. Uma notificação é enviada ao cliente sobre o envio do pedido.
3. Quando o pedido é entregue, o status é atualizado para `DELIVERED`.
4. Uma notificação final é enviada ao cliente confirmando a entrega.

## Filas de Mensagens

### Filas Utilizadas pelo Order Service

1. **product.payment_approved**: Notifica o Product Service sobre pagamentos aprovados para atualização do inventário.
2. **notification.status_order**: Envia atualizações de status para o Notification Service.
3. **order.inventory_successfully_updated**: Recebe confirmação de que o inventário foi atualizado com sucesso.
4. **order.tax_invoice_issued**: Recebe notificação de que a nota fiscal foi emitida.

## Estados do Pedido

O pedido pode passar pelos seguintes estados:

1. **PAYMENT_PENDING**: Aguardando confirmação do pagamento.
2. **PAYMENT_APPROVED**: Pagamento aprovado, aguardando processamento.
3. **PAYMENT_FAILURE**: Falha no processamento do pagamento.
4. **PAYMENT_REJECTED**: Pagamento rejeitado pelo gateway.
5. **IN_SEPARATION**: Pedido em processo de separação no estoque.
6. **TAX_INVOICE_ISSUED**: Nota fiscal emitida.
7. **IN_DELIVERY**: Pedido em rota de entrega.
8. **DELIVERED**: Pedido entregue ao cliente.


## Documentação Adicional

- Para um guia de início rápido, veja [guia-inicio-rapido.md](./docs/guia-inicio-rapido.md)