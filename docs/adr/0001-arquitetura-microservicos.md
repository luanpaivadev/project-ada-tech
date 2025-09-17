# ADR 0001: Adoção de Arquitetura de Microserviços

## Status

Aceito

## Contexto

O Projeto ADA Tech precisa de uma arquitetura que permita:

- Escalabilidade independente de diferentes partes do sistema
- Desenvolvimento paralelo por múltiplas equipes
- Isolamento de falhas
- Flexibilidade para adotar diferentes tecnologias para diferentes componentes
- Facilidade de manutenção e evolução contínua

O sistema precisa lidar com processamento de pedidos, gerenciamento de produtos, autenticação, faturamento e notificações, cada um com requisitos específicos de escalabilidade e tecnologia.

## Decisão

Adotaremos uma arquitetura de microserviços, dividindo o sistema nos seguintes serviços:

1. **Auth Service**: Responsável pela autenticação e autorização dos usuários
2. **Order Service**: Gerenciamento do ciclo de vida dos pedidos
3. **Product Service**: Gerenciamento de produtos e inventário
4. **Invoice Service**: Emissão de notas fiscais
5. **Notification Service**: Envio de notificações aos clientes

Cada serviço terá:
- Seu próprio repositório de dados
- Sua própria API REST
- Implantação independente via Docker

A comunicação entre serviços será realizada através de:
- Comunicação síncrona via REST para operações que exigem resposta imediata
- Comunicação assíncrona via RabbitMQ para operações que podem ser processadas em segundo plano

## Consequências

### Positivas

- Cada serviço pode ser desenvolvido, testado e implantado independentemente
- Diferentes equipes podem trabalhar em paralelo em diferentes serviços
- Falhas em um serviço não afetam diretamente outros serviços
- Cada serviço pode escalar independentemente de acordo com sua demanda específica
- Possibilidade de usar tecnologias específicas para cada domínio de problema

### Negativas

- Maior complexidade operacional para gerenciar múltiplos serviços
- Necessidade de lidar com comunicação entre serviços e possíveis falhas
- Desafios de consistência de dados entre serviços
- Overhead de rede para comunicação entre serviços
- Necessidade de monitoramento mais sofisticado

## Alternativas Consideradas

### Arquitetura Monolítica

**Prós**:
- Simplicidade de desenvolvimento inicial
- Facilidade de teste end-to-end
- Menor latência para operações internas

**Contras**:
- Dificuldade de escalar partes específicas do sistema
- Acoplamento entre diferentes funcionalidades
- Dificuldade para múltiplas equipes trabalharem em paralelo
- Risco de falha total do sistema

### Arquitetura Baseada em Serverless

**Prós**:
- Menor preocupação com infraestrutura
- Escalabilidade automática
- Modelo de pagamento por uso

**Contras**:
- Limitações de tempo de execução
- Maior latência para inicialização (cold start)
- Dependência de provedor específico
- Dificuldade para operações de longa duração

## Referências

- [Microservices Pattern](https://microservices.io/patterns/microservices.html)
- [Martin Fowler on Microservices](https://martinfowler.com/articles/microservices.html)
- [Sam Newman - Building Microservices](https://samnewman.io/books/building_microservices/)