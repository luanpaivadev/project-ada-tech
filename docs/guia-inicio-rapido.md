# Guia de Início Rápido - Projeto ADA Tech

## Testando o Sistema

Você pode testar o sistema usando a collection do Postman [Projeto ADA.postman_collection.json](./Projeto%20ADA.postman_collection.json) ou usando os comandos curl abaixo.

### 1. Autenticação

O sistema já possui dois usuários pré-cadastrados:

| ID | Email | Nome | Senha | Papel |
| --- | --- | --- | --- | --- |
| 389d8a40-4d9b-4b90-8ace-029f04222915 | johndoe@email.com | John Doe | senha123!@ | ROLE_EMPLOYEE |
| 3f3afd64-8a39-4c1d-8ff8-ab13d7b2309e | janedoe@email.com | Jane Doe | senha123!@ | ROLE_USER |

**Importante**: 
- Para simular a criação de pedidos, utilize o usuário Jane Doe (`janedoe@email.com`) com a função `ROLE_USER`.
- Apenas usuários com a função `ROLE_EMPLOYEE` podem emitir notas fiscais, despachar pedidos e concluir entrega do pedido. Para testar essas funcionalidades, utilize o usuário John Doe (`johndoe@email.com`).

Para obter um token JWT:

```bash
curl -X POST http://localhost:8091/auth-service/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "janedoe@email.com",
    "password": "senha123!@"
  }'
```

Guarde o token JWT retornado (`accessToken`) para usar nas próximas requisições.

### 2. Listando Produtos

**Nota**: Os produtos já são criados automaticamente na inicialização do projeto product-service através do Flyway.

```bash
curl -X GET http://localhost:8093/product-service/v1/products \
  -H "Authorization: Bearer SEU_TOKEN_JWT"
```

Anote o ID de um dos produtos listados para usar na criação do pedido.

### 3. Criando um Pedido

Com o token obtido e o ID do produto:

```bash
curl -X POST http://localhost:8092/order-service/v1/orders \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -d '{
    "items": [
      {
        "productId": "SEU_PRODUTO_ID",
        "quantity": 1
      }
    ],
    "address": {
      "recipientName": "Jane Doe",
      "streetAddress": "Rua 1",
      "district": "Centro",
      "city": "São Paulo",
      "state": "São Paulo",
      "postalCode": "00.0000-00",
      "country": "Brazil",
      "phoneNumber": "(21)98888-7777"
    },
    "paymentMethod": "CREDIT_CARD"
  }'
```

Guarde o ID do pedido retornado para usar nas próximas requisições.

### 4. Simulando Aprovação de Pagamento

```bash
curl -X POST http://localhost:8092/order-service/v1/orders/webhook/payment \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer SEU_TOKEN_JWT" \
  -H "x-idempotency-key: $(uuidgen)" \
  -H "client-secret: 29a55997-5017-4947-8714-09382965ec5f" \
  -d '{
    "orderId": "SEU_PEDIDO_ID",
    "paymentId": "'$(uuidgen)'",
    "statusPayment": "APPROVED",
    "message": "Payment Approved"
  }'
```

### 5. Consultando um Pedido

```bash
curl -X GET http://localhost:8092/order-service/v1/orders/SEU_PEDIDO_ID \
  -H "Authorization: Bearer SEU_TOKEN_JWT"
```

O status do pedido deve mudar para "IN_SEPARATION" após alguns segundos (devido ao processamento assíncrono).

## Acessando as Interfaces de Gerenciamento

- **RabbitMQ**: http://localhost:15672 (usuário: user, senha: 0d76739a-a5ec-4ee2-a847-56081ce6a6c9)
  - Útil para verificar o estado das filas e mensagens
- **Grafana**: http://localhost:3000 (usuário: admin, senha: admin)
  - Monitoramento de métricas dos serviços

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
   - **Exemplo**: Use o usuário John Doe (`johndoe@email.com`) para testar emissão de notas fiscais e despacho de pedidos

3. **ROLE_SERVICE**: Permissões para comunicação entre microserviços
   - Comunicação interna entre serviços
   - Acesso a endpoints protegidos
   
### Serviços Cadastrados no Auth-Service

O auth-service possui dois microserviços cadastrados que podem gerar tokens para comunicação interna:

| ID | Client ID | Client Secret | Roles |
|----|-----------|---------------|-------|
| 4c1da05b-449d-45e6-8b17-46d9e2ecf6ee | order-service | 66793dcf-fb4d-402e-8cef-5d5e66c9505e | ROLE_SERVICE |
| e164e48d-795e-4242-9093-6a1774eb6503 | invoice-service | 7ec2dc07-96a0-4881-a19d-9db308a4f4bb | ROLE_SERVICE |

### Gerando Token para Serviços

Para gerar um token para comunicação entre serviços, utilize o seguinte comando:

```bash
curl -X GET "http://localhost:8091/auth-service/v1/auth/internal-token?clientId=order-service&clientSecret=66793dcf-fb4d-402e-8cef-5d5e66c9505e"
```

Substitua os valores de `clientId` e `clientSecret` conforme necessário.

### Fluxo de Teste Recomendado

1. Faça login como Jane Doe (ROLE_USER) para criar um pedido
2. Faça login como John Doe (ROLE_EMPLOYEE) para aprovar o pagamento, emitir a nota fiscal e despachar o pedido
3. Volte para Jane Doe para verificar o status do pedido

## Próximos Passos

- Veja os diagramas de fluxo em [diagrama-fluxo-pedidos.md](./diagrama-fluxo-pedidos.md)