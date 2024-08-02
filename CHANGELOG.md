# Changelog

## [Unreleased]

### Adicionado
- Adição de entidades `Customer`, `Account`, `Card`, `Balance`, `Merchant`, `Transaction`.
- Implementação de serviços e controladores para gerenciar transações.
- Configuração básica de endpoints REST para consultar e adicionar transações.

### Alterado
- Estrutura das entidades e relacionamentos para suportar saldos e cartões associados a contas.

### Corrigido
- Correção de possíveis problemas de inicialização do banco de dados H2 em memória.

## [0.1.0] - 2024-07-31

### Adicionado
- Implementação inicial do projeto com Spring Boot e JPA.
- Configuração do ambiente e primeiros testes de funcionalidade.

### Nota
Este é o primeiro release oficial do projeto. Futuras versões incluirão melhorias e novas funcionalidades com base no feedback dos usuários e nas necessidades identificadas.