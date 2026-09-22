# 🚀 SaaS Pagamentos e Assinaturas (API RESTful)

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2_Database-4479A1?style=for-the-badge&logo=database&logoColor=white)
![Status](https://img.shields.io/badge/Status-Em_Desenvolvimento-yellow?style=for-the-badge)

## 💻 Sobre o Projeto

Este projeto é o Back-end de uma plataforma SaaS (Software as a Service) focada na gestão de pagamentos e assinaturas. Construído com **Spring Boot**, o sistema expõe uma API RESTful para gerenciar planos de assinatura, clientes e processamento de pagamentos.

O desenvolvimento foca em boas práticas de mercado, incluindo validação rigorosa de dados (Fail-Fast), persistência com Spring Data JPA e estruturação padronizada de rotas HTTP.

---

## ⚙️ Tecnologias Utilizadas

*   **Linguagem:** Java 17
*   **Framework:** Spring Boot 3
*   **Persistência:** Spring Data JPA / Hibernate
*   **Banco de Dados:** H2 Database (In-Memory para desenvolvimento)
*   **Validação:** Jakarta Bean Validation

---

## 📚 Documentação da API (Endpoints)

Abaixo estão os endpoints disponíveis e os contratos de comunicação.

### 📦 Módulo: Planos de Assinatura

#### 1. Criar um Novo Plano
*   **Rota:** `POST /planos`
*   **Descrição:** Cadastra um novo plano no catálogo do SaaS.
*   **Regras de Negócio:** O nome não pode estar vazio e o valor deve ser estritamente maior que zero.

**Payload Esperado (Corpo da Requisição):**
```json
{
  "nome": "Plano Pro",
  "valor": 49.90
}
```

*   **Respostas Possíveis:**

*   **200 OK:** Plano criado e persistido no banco com sucesso (Retorna o JSON com o id gerado).
*   **400 Bad Request:** Falha de validação (GIGO interceptado pelo Bean Validation).

#### 2. Listar Todos os Planos

*   **Rota:** GET /planos
*   **Descrição:** Retorna a lista de todos os planos cadastrados no banco de dados.
*   **Retorno:** Array de objetos JSON contendo id, nome e valor.

---

## 🛡️ Tratamento de Erros (Global)

A API possui um interceptador global de exceções (`@RestControllerAdvice`) para garantir que erros internos não sejam expostos e que o cliente receba respostas HTTP padronizadas.

*   **Entidade não encontrada (404 Not Found):** Se o cliente enviar um ID inexistente em rotas de busca, atualização ou deleção lógica, a API intercepta a falha e retorna o status `404` com uma mensagem amigável de erro.
*   **Dados Inválidos (400 Bad Request):** Disparado automaticamente pelo Jakarta Bean Validation se o payload violar regras (ex: campos vazios, valores negativos ou e-mails mal formatados).

---

#### 🚀 Como Executar
*   **Clone o repositório.**
*   **Abra a pasta saas-pagamentos na sua IDE (IntelliJ, Eclipse, etc).**
*   **Aguarde o Maven baixar as dependências (pom.xml).**
*   **Execute a classe SaasPagamentosApplication.java.**
*   **A API estará disponível na porta http://localhost:8080.**
*   **O console do banco de dados H2 pode ser acessado em http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:saasdb).**

### 👤 Módulo: Clientes

#### 1. Cadastrar um Novo Cliente
*   **Rota:** `POST /clientes`
*   **Descrição:** Registra um novo cliente na plataforma.
*   **Regras de Negócio:** Nome obrigatório. O e-mail deve ter um formato válido (validação via Regex/Bean Validation).

**Payload Esperado (Corpo da Requisição):**
```json
{
  "nome": "Victor",
  "email": "victor@email.com"
}
```

*   **Respostas Possíveis:**

*   **200 OK:** Cliente cadastrado e persistido no banco com sucesso.
*   **400 Bad Request:** Falha de validação (E-mail em formato inválido ou campos ausentes).

#### 2. Listar Todos os Clientes

*   **Rota:** GET /clientes
*   **Descrição:** Retorna a lista de todos os clientes cadastrados.
*   **Retorno:** Array de objetos JSON contendo id, nome e email.

### 🤝 Módulo: Assinaturas

#### 1. Criar uma Assinatura
*   **Rota:** `POST /assinaturas`
*   **Descrição:** Vincula um Cliente existente a um Plano existente.
*   **Regras de Negócio:** Recebe apenas os IDs de referência. A data de início é gerada automaticamente pelo servidor e o status inicial é "ATIVA".

**Payload Esperado (Corpo da Requisição):**
```json
{
  "cliente": {
    "id": 1
  },
  "plano": {
    "id": 1
  }
}
```

*   **Respostas Possíveis:**

*   **200 OK:** Relacionamento salvo no banco de dados com sucesso.
*   **400 Bad Request:** JSON mal formatado ou erro de validação.

#### 2. Listar Assinaturas

*   **Rota:** GET /assinaturas
*   **Descrição:** Retorna a lista de todas as assinaturas vinculadas.

#### 3. Cancelar uma Assinatura (Soft Delete)
*   **Rota:** `PATCH /assinaturas/{id}/cancelar`
*   **Descrição:** Realiza a exclusão lógica de uma assinatura, alterando seu status para "CANCELADA" sem remover o histórico do banco de dados.
*   **Regras de Negócio:** O ID passado na URL deve existir no banco de dados.

**Exemplo de Resposta (200 OK):**
```json
{
  "id": 1,
  "nomeCliente": "Victor",
  "nomePlano": "Plano Pro",
  "dataInicio": "2026-09-12",
  "status": "CANCELADA"
}
```

### 💰 Módulo: Pagamentos

#### 1. Listar Pagamentos
*   **Rota:** `GET /pagamentos`
*   **Descrição:** Retorna o histórico de todas as cobranças (boletos/cartão) geradas pelo sistema.
*   **Retorno:** Array de objetos achatados (DTO) com o ID da assinatura vinculada.

#### 2. Pagar uma Cobrança
*   **Rota:** `PATCH /pagamentos/{id}/pagar`
*   **Descrição:** Simula a aprovação financeira de uma transação.
*   **Regras de Negócio:** Altera o status do pagamento para "PAGO". O ID da cobrança deve existir (retorna 404 caso contrário).

**Exemplo de Resposta (200 OK):**
```json
{
  "id": 1,
  "assinaturaId": 1,
  "valor": 49.9,
  "dataVencimento": "2026-09-20",
  "status": "PAGO"
}
```

*   **Regras de Negócio:** Altera o estado do pagamento para "PAGO". O ID da cobrança deve existir (retorna 404 caso contrário). Se o pagamento já se encontrar no estado "PAGO", a API rejeita a operação com um erro de validação.

**Exemplo de Resposta de Erro (400 Bad Request - Regra de Negócio):**
```text
Erro de Validação: Este pagamento já foi processado e não pode ser pago novamente.
```

#### 3. Cancelar uma Assinatura (Soft Delete)
*   **Rota:** `PATCH /assinaturas/{id}/cancelar`
*   **Descrição:** Realiza a exclusão lógica de uma assinatura, alterando o seu estado para "CANCELADA".
*   **Efeito Cascata (Side-effect):** Qualquer pagamento associado a esta assinatura que ainda se encontre no estado "PENDENTE" será automaticamente alterado para "CANCELADO" para evitar cobranças indevidas.