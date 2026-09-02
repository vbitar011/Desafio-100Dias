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
*   **Transferências Bancárias:** Operação de transferência de valores entre contas (PIX/TED), garantindo a integridade dos saldos com validações de segurança (Exceptions) e sincronização simultânea no Banco de Dados SQLite.
*   **Sistema de Autenticação:** Implementação de senhas numéricas (4 a 6 dígitos) exigidas para operações financeiras (Saque, Transferência e Extrato), com validação via Expressões Regulares (Regex) e armazenamento em Banco de Dados SQLite.
*   **Arquitetura de Software (Service Layer):** Criação da classe `CaixaEletronicoService` para atuar como a camada de serviço, separando completamente as regras de negócio da interface com o usuário.
*   **Refatoração e Clean Code:** Aplicação da técnica *Extract Method* no menu principal para eliminar "Code Smells" (como o switch-case gigante e difícil de manter).
*   **Boas Práticas (SOLID):** Adequação do sistema ao Princípio da Responsabilidade Única (SRP), garantindo que a classe UI apenas gerencie a exibição e a leitura de dados, enquanto o Serviço orquestra toda a lógica financeira.
*   **Desenvolvimento End-to-End:** Conclusão do ciclo de vida relacional do PIX (1:N) com a implementação do fluxo de cadastro na camada de Serviço.
*   **Blindagem de Fluxo (Fail-Fast):** Aplicação rigorosa de *Guard Clauses* para validar autenticação de usuário e integridade de opções de menu antes de permitir transações de gravação no banco de dados.
*   **Clean Architecture:** Otimização do tratamento de exceções mediante a remoção de blocos `try/catch` redundantes no Service Layer, delegando a responsabilidade de falhas de infraestrutura exclusivamente ao Data Access Object (DAO).
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
*   **Persistência de Dados (CRUD Completo com SQLite):**
    * **C**reate: Salvamento seguro de novas contas (`INSERT`).
    * **R**ead: Carregamento automático de clientes (`SELECT`).
    * **U**pdate: Atualização financeira de saldos em tempo real (`UPDATE`).
    * **D**elete: Encerramento de contas com validação de segurança de saldo zerado (`DELETE`).
*   **Prevenção de Falhas e UX:** Implementação de classes utilitárias (`TecladoUtil`) com laços de repetição e `try-catch` para blindar o sistema contra entradas inválidas (InputMismatchException), garantindo o funcionamento contínuo do terminal.
*   **Arquitetura Clean Code:**
    * Aplicação do Princípio da Responsabilidade Única (SRP - S.O.L.I.D), eliminando *God Classes*.
    * Separação do sistema em camadas lógicas (Dados, Negócios e Apresentação via `CaixaEletronicoUI`).
    * Uso de Injeção de Dependência via construtores para compartilhamento seguro de instâncias. 
*   **UX e Formatação:** Limpeza de interface para simular um terminal real e uso da classe `NumberFormat` para a formatação e exibição de valores monetários no padrão regional (R$).
*   **Segurança e Tipagem:** Uso de `Enums` para controle rigoroso de categorias (ex: `TipoConta`), prevenindo erros de "Strings Mágicas".
*   **Banco de Dados Relacional (SQLite):** Modelagem de dados com relacionamentos 1:N utilizando Chaves Estrangeiras (Foreign Keys) para vincular o histórico de transações às contas.
*   **Persistência Avançada:** Leitura e gravação de histórico de movimentações (extrato) diretamente no disco.
*   **Segurança de Memória:** Resolução de vulnerabilidade de recursão infinita (*StackOverflowError*) na rotina de formatação de datas transacionais.
*   **Integridade de Banco de Dados:** Correção do mapeamento de atributos no `ContaDAO`, garantindo a reconstrução exata de objetos a partir do SQLite e evitando a corrupção cruzada de dados.
*   **Tipagem Forte:** Restauração da validação de `Enum` (`TipoConta.valueOf`) na leitura de registros persistidos, blindando o sistema contra inconsistências de texto (*String*).
*   **Design de Código (*Clean Code*):** Implementação de Cláusulas de Guarda (*Guard Clauses*) e adoção de lançamento de exceções ativas (`IllegalArgumentException`) na camada de Domínio, rejeitando operações financeiras inválidas sem uso de estruturas condicionais redundantes (`else`).
*   **Refatoração de Fluxo (Guard Clauses):** Reescrita completa dos métodos da Camada de Serviço (`exibirExtrato`, `consultarSaldo`, `encerrarConta`) aplicando *Guard Clauses*. Eliminação do *Arrow Anti-Pattern* (condicionais aninhadas profundas) para garantir uma leitura linear e aborto precoce de falhas.
*   **Prevenção de Falhas Críticas:** Isolamento técnico contra `NullPointerException` ao validar a existência de objetos na RAM antes de invocar métodos de autenticação.
*   **Segurança de Acesso:** Fechamento de brechas de domínio. Acesso a dados sensíveis (extrato, saldo) e operações destrutivas (exclusão de conta) agora exigem obrigatoriamente validação criptográfica (senha numérica com Regex).
*   **Otimização de I/O:** Padronização do consumo de buffer de teclado (`Scanner.next()`) e realocação de inputs de senha para otimizar o processamento e a experiência do usuário, bloqueando interações desnecessárias em entidades nulas.
*   **Modelagem de Banco de Dados (1:N):** Criação da tabela `chaves_pix` no SQLite, implementando cardinalidade de Um-para-Muitos com `FOREIGN KEY` referenciando a tabela de contas e `PRIMARY KEY` na chave para garantir exclusividade e prevenção de colisão de dados.
*   **Data Access Object (DAO):** Implementação de queries SQL limpas (`INSERT` e `SELECT` com junção lógica) para salvar e rastrear chaves PIX, tratando erros específicos de infraestrutura (`SQLException`).
*   **Padrão de Design DRY:** Refatoração crítica do método de transferência (`CaixaEletronicoService`), separando a lógica de roteamento (identificação por Conta vs. PIX) da lógica de execução transacional, eliminando duplicação de código e reduzindo a complexidade ciclomática.
*   **Gerenciamento de Escopo:** Correção de alocação de variáveis na memória (escopo de bloco) para garantir o fluxo seguro de dados entre as validações de segurança e as operações financeiras.
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
