# 🏦 Sistema Bancário (Terminal ATM) - Java & SQLite

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![SQLite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)
![Design Patterns](https://img.shields.io/badge/Design_Patterns-SOLID_|_DRY-4CAF50?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Finalizado-success?style=for-the-badge)

## 💻 Resumo Executivo

Este projeto é um simulador de Caixa Eletrônico (ATM) de alto nível construído para o terminal (Console Application). Desenvolvido como projeto prático ao longo de 30 dias de imersão, o sistema transcende scripts básicos para implementar uma **arquitetura de software robusta, segura e totalmente integrada a um banco de dados relacional**.

O foco do desenvolvimento foi a aplicação estrita de boas práticas de Engenharia de Software (*Clean Code*, *Fail-Fast*, Padrões de Projeto e Governança de Dados), entregando um sistema resiliente, à prova de falhas de usuário e com integridade transacional.

---

## 🏗️ Arquitetura e Engenharia de Software

O diferencial técnico deste projeto não reside apenas no que ele faz, mas em *como* o código foi construído. As seguintes abordagens foram aplicadas no core da aplicação:

### 1. Separação de Conceitos (MVC Simplificado & SOLID)
*   **Apresentação (`CaixaEletronicoUI`):** Totalmente isolada, responsável apenas por renderizar menus e processar *inputs/outputs* de interface, respeitando o Princípio da Responsabilidade Única (SRP).
*   **Regras de Negócios (`CaixaEletronicoService`):** O "cérebro" do banco. Centraliza validações de segurança, cálculos de impostos (para instâncias de `Tributavel`) e orquestração de transações.
*   **Persistência (`ContaDAO`):** Implementação do Padrão *Data Access Object*. O sistema de negócios nunca lida com queries SQL, toda comunicação com o banco de dados é terceirizada e encapsulada no DAO.

### 2. Segurança e Tratamento de Exceções (Defensive Programming)
*   **Cláusulas de Guarda (*Fail-Fast*):** Eliminação do *Arrow Anti-Pattern* (pirâmides de if/else). Validações de nulidade e acesso são processadas no topo dos métodos para abortamento precoce, mantendo o "caminho feliz" (Happy Path) isolado.
*   **Tratamento de Exceções Customizadas:** Uso ativo de exceções de domínio (ex: `SaldoInsuficienteException`) acopladas ao Domínio (`Conta.java`), garantindo que regras financeiras jamais sejam violadas em memória.
*   **Blindagem de Scanner e UX de Falhas:** O utilitário `TecladoUtil` intercepta vazamentos de buffer e tipos de dados inválidos (`InputMismatchException`), garantindo que o programa nunca quebre. Erros técnicos de banco de dados (`SQLException`) são mascarados para o usuário final com mensagens amigáveis baseadas no contexto de negócio.

### 3. Governança de Dados (Data Governance)
*   **Expressões Regulares (Regex):** O sistema não confia no input do usuário. Operações críticas exigem padronização por Expressões Regulares (ex: Numeração de Conta `^\d{5}-\d$`, senhas numéricas estritas, e validação de nomes completos).
*   **Integridade Relacional Física:** Uso do SQLite como validador de última fronteira. Cardinalidade 1:N com *Foreign Keys* e blindagem antifraude por *Primary Keys* para evitar colisões (ex: bloqueio estrutural de duplicidade de chaves PIX).

---

## ⚙️ Funcionalidades Core

*   **Autenticação Obrigatória:** Nenhuma operação financeira ou visualização de dados sensíveis (Saldo/Extrato) ocorre sem a inserção da senha (blindada por Regex).
*   **Operações Transacionais (PIX e TED):** Transferências complexas que atualizam múltiplas entidades no banco de dados. Implementadas sob o princípio DRY (*Don't Repeat Yourself*), onde lógicas de roteamento de destinatário (Busca por Número vs. Busca por Chave) são separadas do cálculo financeiro atômico.
*   **Persistência (CRUD End-to-End):**
    *   **C**reate: Cadastros seguros de Contas e vínculos Multi-Chave PIX.
    *   **R**ead: Restauração autônoma da base de clientes (Reidratação de Memória).
    *   **U**pdate: Atualização assíncrona de movimentações e saldos (Sincronização em tempo real).
    *   **D**elete: Bloqueio de exclusão de contas com saldo remanescente.
*   **Auditoria de Caixa:** Geração de Extrato Histórico registrado por data/hora transacional.

---

## 🛠️ Tech Stack & Bibliotecas

*   **Linguagem:** Java 17+ (Core Java, Orientação a Objetos, Stream API, Expressões Lambda, e `java.time`).
*   **Banco de Dados:** SQLite (Relacional, Local).
*   **Design & Segurança:** Regex (`String.matches()`), Injeção de Dependências em Construtores, Polimorfismo e Interfaces contratuais.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
*   **Java Development Kit (JDK):** Versão 17 ou superior.
*   **Driver JDBC do SQLite:** Arquivo `.jar` (ex: `sqlite-jdbc.jar`) adicionado às dependências/classpath do seu projeto na IDE.
*   Uma IDE (IntelliJ IDEA, Eclipse) configurada com o terminal ativo.

### Inicialização
1. Clone este repositório:
   ```bash
   git clone [https://github.com/vbitar011/Desafio-100Dias.git](https://github.com/vbitar011/Desafio-100Dias.git)