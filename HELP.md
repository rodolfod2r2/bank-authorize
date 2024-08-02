# Guia de Ajuda

## Visão Geral

Este documento fornece orientações sobre como utilizar e desenvolver a aplicação bancária.

## Configuração do Ambiente

### Requisitos

- **Java 11 ou superior**: Certifique-se de que o JDK está instalado e configurado no seu sistema.
- **Maven**: Ferramenta de gerenciamento de projetos Java.

### Banco de Dados

O projeto está configurado para usar um banco de dados H2 em memória por padrão. Para usar outro banco de dados, altere as configurações no arquivo `application.properties` ou `application.yaml`.

### Execução da Aplicação

Para iniciar a aplicação, utilize o comando Maven:

```bash
mvn spring-boot:run
```

A aplicação será iniciada no `http://localhost:8080`.

## Uso da API

imagem Swagger

## Desenvolvimento

Para adicionar novas funcionalidades ou corrigir bugs, siga estas etapas:

1. Faça um fork do repositório.
2. Crie uma nova branch para suas alterações.
3. Implemente as alterações e escreva testes.
4. Envie um pull request com uma descrição clara das alterações.
