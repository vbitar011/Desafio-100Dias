# 🏦 Sistema Bancário (Caixa Eletrônico) - Java

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)
![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-green?style=for-the-badge)

## 💻 Sobre o Projeto

Este projeto é um simulador de Caixa Eletrônico (Console Application) desenvolvido inteiramente em **Java**. Ele foi construído de forma progressiva durante a minha jornada no desafio **#100DaysOfCode**, evoluindo de simples scripts no terminal para uma arquitetura robusta com Persistência de Dados em Banco de Dados SQL.

O objetivo principal do projeto é consolidar e aplicar conceitos fundamentais e avançados da Engenharia de Software e da linguagem Java.

---

## ⚙️ Funcionalidades

O sistema conta com um menu interativo completo que permite:

*   **Gestão de Contas:** Criação dinâmica de Contas Correntes e Contas Poupança.
*   **Operações Financeiras:** Depósitos e Saques com validações de segurança em tempo real.
*   **Auditoria de Transações:** Geração de extratos detalhados registrando data e hora exatas de cada operação.
*   **Regras de Negócio (Impostos):** Cobrança de tributos específica para Contas Correntes.
*   **Relatórios Avançados:** 
    *   Visualização do Patrimônio Total do banco.
    *   Filtro de contas com maior saldo.
*   **Persistência de Dados (DB):** Salvamento e carregamento automático de todas as contas e clientes utilizando um banco de dados relacional.

---

## 🛠️ Tecnologias e Conceitos Aplicados

Durante o desenvolvimento deste sistema, foram aplicados os seguintes conceitos técnicos:

*   **Programação Orientada a Objetos (POO):** Classes, Herança, Encapsulamento e Polimorfismo.
*   **Contratos e Abstração:** Uso de `Interfaces` (ex: `Tributavel`) para padronizar comportamentos.
*   **Tratamento de Erros:** Criação de Exceções Customizadas (ex: `SaldoInsuficienteException`) e tratamento com blocos `try-catch`.
*   **Java Moderno:** 
    *   Uso massivo de `Collections` (`List`, `ArrayList`).
    *   Manipulação de dados com `Stream API`, `Lambdas` e `Method References`.
    *   Manipulação de datas com a biblioteca `java.time`.
*   *   **Persistência de Dados (DB):** Salvamento (INSERT), carregamento automático (SELECT) e atualização de saldos em tempo real (UPDATE) utilizando o banco de dados SQLite.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
*   **Java Development Kit (JDK):** Versão 17 ou superior.
*   **Driver JDBC do SQLite:** Arquivo `.jar` (ex: `sqlite-jdbc.jar`) adicionado às dependências do projeto.
*   Uma IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code).

### Passos
1. Faça o clone deste repositório:
   ```bash
   git clone https://github.com/vbitar011/Desafio-100Dias/tree/master/Conta%20Bancaria
