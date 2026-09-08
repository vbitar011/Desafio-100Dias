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

*   **Respostas Possíveis:**

*   **200 OK:** Plano criado e persistido no banco com sucesso (Retorna o JSON com o id gerado).
*   **400 Bad Request:** Falha de validação (GIGO interceptado pelo Bean Validation).

#### 2. Listar Todos os Planos

*   **Rota:** GET /planos
*   **Descrição:** Retorna a lista de todos os planos cadastrados no banco de dados.
*   **Retorno:** Array de objetos JSON contendo id, nome e valor.

####🚀 Como Executar
*   **Clone o repositório.**
*   **Abra a pasta saas-pagamentos na sua IDE (IntelliJ, Eclipse, etc).**
*   **Aguarde o Maven baixar as dependências (pom.xml).**
*   **Execute a classe SaasPagamentosApplication.java.**
*   **A API estará disponível na porta http://localhost:8080.**
*   **O console do banco de dados H2 pode ser acessado em http://localhost:8080/h2-console (JDBC URL: jdbc:h2:mem:saasdb).**