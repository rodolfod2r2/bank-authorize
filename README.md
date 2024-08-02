# Bank Authorize Application

Este projeto é uma aplicação bancária simples construída com Spring Boot e JPA. Ele fornece funcionalidades básicas para gerenciar contas bancárias, cartões, saldos e transações.

## Funcionalidades

- Gerenciar tipos de contas (Alimentação, Refeição, Dinheiro).
- Gerenciar tipos de cartões (Débito, Crédito).
- Registrar e visualizar saldos de contas.
- Adicionar e visualizar transações bancárias.
- Associar comerciantes a transações com códigos MCC.

## Estrutura do Projeto

O projeto é dividido em várias entidades e serviços:

- **Entidades**:
  - `Customer`: Representa um cliente com uma conta associada.
  - `Account`: Representa uma conta bancária com saldos e cartões associados.
  - `Card`: Representa um cartão bancário.
  - `Balance`: Representa o saldo de um tipo específico de conta.
  - `Merchant`: Representa um comerciante associado a um código MCC.
  - `Transaction`: Representa uma transação bancária.

- **Serviços**:
  - `TransactionService`: Gerencia as operações relacionadas a transações.

- **Repositórios**:
  - `TransactionRepository`: Interface para acessar as transações no banco de dados.

- **Controladores**:
  - `TransactionController`: Exponha endpoints REST para gerenciar transações.

## Diagrama
![info](assets\authorize.png)

## Endpoints REST - SWAGGER

![info](assets\swagger.png)

## Requisitos

- Java 11 ou superior
- Maven
- Banco de dados relacional (configuração padrão usa H2 em memória)

## Configuração

1. Clone o repositório:

   ```bash
   git clone https://github.com/rodolfod2r2/bank-application.git
   ```

2. Navegue até o diretório do projeto:

   ```bash
   cd bank-application
   ```

3. Compile e execute a aplicação:

   ```bash
   mvn spring-boot:run
   ```

4. A aplicação estará disponível em `http://localhost:8080`.

### JAVA DOCS
![info](assets\javadocs.png)


## Contribuindo

Sinta-se à vontade para enviar pull requests. Para reportar problemas ou sugestões, abra uma issue no repositório.

## Licença

Este projeto está licenciado sob a [MIT License](LICENSE).


## Estratégias para Garantir Processamento Único de Transações por Conta

Para garantir que apenas uma transação por conta seja processada em um determinado momento é crucial para evitar problemas como a duplicação de transações e a inconsistência dos dados. Aqui estão algumas estratégias:


### 1. Bloqueio Otimista (Optimistic Locking)

Assume que as transações não irão conflitar. Verifica conflitos apenas no momento do commit.

### 2. Bloqueio Pessimista (Pessimistic Locking)

Bloqueia os recursos necessários para a transação desde o início até o final, prevenindo outros acessos concorrentes.

### 3. Coordenação de Transações com Middleware

Utiliza um middleware para gerenciar e coordenar as transações, garantindo que apenas uma transação por conta seja processada de cada vez.

### 4. Cache Distribuído com Controle de Concorrência

Implementa um cache distribuído que gerencia o acesso concorrente aos dados, garantindo a consistência.

### 5. Base de Dados Transacional com Mecanismos de Controle

Utiliza um sistema de banco de dados que oferece mecanismos robustos de controle de concorrência e bloqueio.