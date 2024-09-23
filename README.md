# Sistema de Processamento de Transações

Este repositório contém um sistema de processamento de transações em tempo real, utilizando Java, Kafka, MongoDB, Docker e várias ferramentas de observabilidade.

## Tecnologias Utilizadas

- **Java**: Linguagem de programação principal do sistema.
- **Kafka**: Sistema de mensageria para processamento assíncrono de eventos.
- **MongoDB**: Banco de dados não-relacional escolhido por sua flexibilidade e escalabilidade.
- **Docker**: Para criação e gerenciamento de containers, facilitando a execução em diferentes ambientes.
- **Prometheus**: Monitoramento e coleta de métricas.
- **Grafana**: Visualização de métricas.
- **Fluent Bit e Splunk**: Para gerenciamento de logs.

## Decisões Técnicas

### Escolha do Banco de Dados Não-Relacional

Optei pelo MongoDB por várias razões:

- **Escalabilidade**: MongoDB é projetado para escalar horizontalmente, facilitando a distribuição de dados em várias máquinas, o que é essencial para um sistema de processamento de pedidos em larga escala.
- **Flexibilidade de Modelagem**: A modelagem de dados em MongoDB permite armazenar documentos JSON, que se adaptam bem à natureza variável dos dados de pedidos. Isso facilita a adição de novos campos sem a necessidade de migrações complexas.
- **Desempenho**: O MongoDB oferece um desempenho excelente em operações de leitura e escrita, o que é crítico para um sistema que processa pedidos em tempo real.

### Modelagem dos Dados

Os dados das ordens são armazenados em uma coleção chamada `orders`, que contém os seguintes campos:
- `orderId`: Identificador único da ordem.
- `customerId`: Identificador do cliente.
- `amount`: Valor da ordem.
- `status`: Status da ordem (ex: "PAID", "PENDING", "FAILED").
- `timestamp`: Data e hora da criação da ordem.
- `createdAt`: Data e hora de criação do registro, usada para TTL.

Os pedidos são modelados como documentos na coleção orders, contendo campos como orderId, customerId, amount, status, timestamp, e um campo createdAt para gerenciamento de TTL (Time to Live). Essa modelagem permite que os dados sejam consultados rapidamente e gerenciados de forma eficiente, com a vantagem adicional de permitir a expiração automática de pedidos antigos.

### Desenho da solução
![Screenshot from 2024-09-23 03-56-52](https://github.com/user-attachments/assets/0119017b-5e93-447c-aa42-fbe8e48fad84)

## Como Rodar o Sistema Localmente

### Passos para Execução

1. **Clone o repositório**:

   ```bash
   git clone https://github.com/AlanCS7/ecommerce-transactions
   cd ecommerce-transactions

2. **Construa e inicie os containers**:

   ```bash
   docker-compose up --build

3. **Acesse os serviços**

   - Consumer Service: http://localhost:8080
   - Producer Service: http://localhost:8088
   - Grafana: http://localhost:3000 (usuário: admin, senha: admin)
   - Prometheus: http://localhost:9090
   - Splunk: http://localhost:8000 (usuário: admin, senha: password)
