# Amazin — Mercado Express | CP5 Spring Security MVC

Aplicação Web desenvolvida em **Java + Spring Boot + Maven**, com **Thymeleaf**, **Spring Data JPA/Hibernate**, **PostgreSQL**, **Lombok** e **Spring Security**.

O CP5 aproveita o domínio de mercado express do CP anterior e acrescenta **autenticação com login e cadastro (Sign Up) próprios**, **dois perfis de usuário (ADMIN e USER)**, **banco PostgreSQL** e **deploy em produção**.

## Integrantes

| Nome | RM |
|---|---|
| Bruno A Zanateli | RM563736 |
| Christian S Freitas | RM566098 |
| Rodrigo Tiezzi | RM562975 |
| Pedro P Biasolli | RM562521 |
| Matheus Souza | RM562532 |

## Links

- **GitHub:** https://github.com/Bruno-A-Z/cp05
- **Deploy:** [https://amazin.onrender.com](https://cp05.onrender.com)
- **Sistema utilizado no deploy:** Render (Web Service com Docker + PostgreSQL do Render)
- **IDE utilizada:** IntelliJ IDEA

## Requisitos do enunciado atendidos

- Spring Framework MVC configurado com Maven e Java 21.
- Dependências: Spring Web, Thymeleaf, Spring Security, Spring Data JPA, Validation, HATEOAS, Lombok, PostgreSQL Driver e DevTools.
- Banco **PostgreSQL**, configurado em `application.yml`.
- Tabela de produtos `TDS_Sec_MVC_TB_Mercado` e tabela de usuários `TDS_Users_Mercado`.
- CRUD completo (Create, Read, Update, Delete) com links e botões na interface Web.
- Tela de **login personalizada**, diferente da fornecida pelo Spring Security.
- Tela de **Sign Up (cadastro)** que salva o perfil na tabela `TDS_Users_Mercado`.
- Após o login, redirecionamento para uma tela de acesso restrito conforme o perfil.
- **Dois tipos de usuário** (`ADMIN` e `USER`) com acessos diferentes.
- Página base (landing page) em `index.html`.
- Deploy realizado e link de produção disponibilizado neste README.

## Tecnologias

- Java 21
- Spring Boot 3.3.3
- Maven
- Spring Web MVC
- Spring Security (BCrypt)
- Spring Data JPA / Hibernate
- PostgreSQL
- Lombok
- Thymeleaf
- Bean Validation
- Spring HATEOAS (mantido do CP anterior)
- Springdoc OpenAPI / Swagger
- Bootstrap 5
- Docker + Render

## Spring Initializr

![Spring Initializr](docs/screenshots/00-initializr.png)

## Modelo de dados

### Produtos — tabela `TDS_Sec_MVC_TB_Mercado`

Entidade `Item`.

| Coluna | Campo Java | Tipo |
|---|---|---|
| `ID` | `id` | Long |
| `NAME_ITEM` | `name` | String |
| `KIND_ITEM` | `kind` | String |
| `ITEM_SECTOR` | `sector` | Enum `Sector` |
| `ITEM_SIZE` | `size` | Enum `Size` |
| `ITEM_PRICE` | `price` | Double |

Setores: `KITCHEN`, `CLEANING`, `SHIRT`, `SOCKS`, `PANTS`.
Tamanhos: `BIG`, `MEDIUM`, `LARGE`.

### Usuários — tabela `TDS_Users_Mercado`

Entidade `Usuario`.

| Coluna | Campo Java | Tipo |
|---|---|---|
| `ID` | `id` | Long |
| `NOME` | `nome` | String |
| `USERNAME` | `username` | String (único) |
| `EMAIL` | `email` | String (único) |
| `SENHA` | `senha` | String (criptografada com BCrypt) |
| `PERFIL` | `role` | Enum `Role` (`ADMIN` ou `USER`) |

> O PostgreSQL grava os nomes das tabelas em minúsculas (`tds_sec_mvc_tb_mercado` e `tds_users_mercado`). As tabelas são criadas automaticamente pelo Hibernate.

## Estrutura principal

```text
src/main/java/com/java/cp04/Amazin
├── assembler
│   └── ItemModelAssembler.java
├── config
│   ├── DataInitializer.java
│   ├── SecurityConfig.java
│   └── SwaggerConfig.java
├── controller
│   └── ItemController.java
├── dto
│   └── CadastroForm.java
├── enumerator
│   ├── Role.java
│   ├── Sector.java
│   └── Size.java
├── model
│   ├── Item.java
│   └── Usuario.java
├── repository
│   ├── ItemRepository.java
│   └── UsuarioRepository.java
├── service
│   ├── ItemService.java
│   ├── UsuarioDetailsService.java
│   └── UsuarioService.java
└── web
    ├── AuthController.java
    ├── GlobalModelAttributes.java
    ├── HomeController.java
    └── ItemViewController.java
```

Templates Thymeleaf:

```text
src/main/resources/templates
├── fragments/layout.html
├── itens/detalhes.html
├── itens/form.html
├── itens/lista.html
├── acesso-negado.html
├── admin.html
├── cadastro.html
├── cliente.html
├── index.html
└── login.html
```

## Configuração do banco (PostgreSQL)

Por segurança, as credenciais **não ficam gravadas no GitHub**. O arquivo `application.yml` lê variáveis de ambiente, com valores padrão para uso local:

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/mercadoexpress}
    username: ${DB_USER:postgres}
    password: ${DB_PASSWORD:postgres}
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: ${DDL_AUTO:update}
```

### Criando o banco local

```sql
CREATE DATABASE mercadoexpress;
```

### Variáveis de ambiente

#### Windows PowerShell

```powershell
$env:DB_URL="jdbc:postgresql://localhost:5432/mercadoexpress"
$env:DB_USER="postgres"
$env:DB_PASSWORD="SUA_SENHA"
$env:ADMIN_USERNAME="admin"
$env:ADMIN_PASSWORD="admin123"
```

#### Linux/macOS

```bash
export DB_URL="jdbc:postgresql://localhost:5432/mercadoexpress"
export DB_USER="postgres"
export DB_PASSWORD="SUA_SENHA"
export ADMIN_USERNAME="admin"
export ADMIN_PASSWORD="admin123"
```

### Por que `update`?

O modo `update` cria as tabelas na primeira execução e preserva os dados nas seguintes. Não é recomendado usar `create-drop` em produção, pois apagaria os dados a cada reinício.

### Dados iniciais

Na primeira execução, a classe `DataInitializer` cria automaticamente:

- o usuário **ADMIN** (definido por `ADMIN_USERNAME` e `ADMIN_PASSWORD`);
- três produtos de exemplo, caso a tabela de produtos esteja vazia.

## Executando localmente

```bash
mvn spring-boot:run
```

Ou, no Windows:

```powershell
mvnw.cmd spring-boot:run
```

A aplicação ficará disponível em:

- Landing page: `http://localhost:8082/`
- Login: `http://localhost:8082/req/login`
- Cadastro: `http://localhost:8082/req/cadastro`
- Produtos: `http://localhost:8082/web/itens`
- Swagger: `http://localhost:8082/swagger-ui/index.html` (somente ADMIN)

## Perfis de acesso

| Perfil | O que pode acessar |
|---|---|
| **Público** (sem login) | Landing page, login e cadastro |
| **USER** | Consulta de produtos (lista e detalhes) e tela `/cliente` |
| **ADMIN** | Tudo do USER + CRUD de produtos, painel `/admin` e Swagger |

Após o login, o redirecionamento é automático:

- **ADMIN** → `/admin`
- **USER** → `/cliente`

O cadastro público (`/req/cadastro`) sempre cria usuários com perfil **USER**. O perfil **ADMIN** só existe pelo usuário criado na inicialização da aplicação.

## Segurança

A aplicação usa uma cadeia de segurança com **Spring Security**:

- Login por formulário **personalizado** (`/req/login`), com usuários carregados da tabela `TDS_Users_Mercado` pelo `UsuarioDetailsService`.
- Senhas armazenadas com **BCrypt**.
- Proteção **CSRF** ativa nos formulários Web.
- Página de **acesso negado** para quem tenta acessar uma rota sem permissão.
- **HTTP Basic** habilitado para testar a API REST em `/item/**` no Postman/Insomnia.

### Rotas públicas

```text
/
/req/login
/req/cadastro
/acesso-negado
/css/**
```

### Rotas para USER e ADMIN

```text
GET /web/itens
GET /web/itens/{id}
/cliente
GET /item/**
```

### Rotas somente ADMIN

```text
/admin
GET  /web/itens/novo
POST /web/itens
GET  /web/itens/{id}/editar
POST /web/itens/{id}/editar
POST /web/itens/{id}/deletar
POST, PUT, PATCH, DELETE /item/**
/swagger-ui/**
```

## Interface Web e CRUD

### READ — consulta de produtos (USER e ADMIN)

A página `/web/itens` lista os produtos carregados da tabela `TDS_Sec_MVC_TB_Mercado` através do `ItemRepository`.

Cada produto possui o botão **Ver**, que abre:

```text
GET /web/itens/{id}
```

Na tela de detalhes são exibidos nome, tipo, setor, tamanho e preço.

### CREATE — somente ADMIN

O botão **Novo produto** abre:

```text
GET /web/itens/novo
```

O formulário é enviado para:

```text
POST /web/itens
```

Após o cadastro, o usuário retorna para a lista e visualiza a mensagem de sucesso.

### UPDATE — somente ADMIN

O botão **Editar** abre:

```text
GET /web/itens/{id}/editar
```

O formulário é enviado para:

```text
POST /web/itens/{id}/editar
```

Os dados são atualizados pela camada `ItemService` e persistidos pelo `ItemRepository`.

### DELETE — somente ADMIN

O botão **Excluir** realiza:

```text
POST /web/itens/{id}/deletar
```

Antes do envio, a interface solicita confirmação ao usuário.

## Endpoints REST

Base URL:

```text
http://localhost:8082/item
```

| Método | Endpoint | Acesso |
|---|---|---|
| GET | `/item` | USER ou ADMIN |
| GET | `/item/{id}` | USER ou ADMIN |
| POST | `/item` | ADMIN |
| PUT | `/item/{id}` | ADMIN |
| PATCH | `/item/{id}` | ADMIN |
| DELETE | `/item/{id}` | ADMIN |

As chamadas exigem **HTTP Basic** com usuário e senha cadastrados.

Exemplo de `POST /item`:

```json
{
  "name": "Sabão líquido",
  "kind": "Limpeza",
  "sector": "CLEANING",
  "size": "LARGE",
  "price": 18.90
}
```

## Telas e demonstração

### 00 — Landing page (`index.html`)

<img width="2555" height="1265" alt="image" src="https://github.com/user-attachments/assets/842b9c80-f724-41ac-890a-dbe80c59bb88" />


Página base da aplicação, com acesso a **Entrar** e **Criar conta**.

### 01 — Cadastro (Sign Up)

<img width="2555" height="1267" alt="image" src="https://github.com/user-attachments/assets/73d76f59-02ab-42f5-a622-163e6cdb90a5" />

Formulário com validação (nome, usuário, e-mail e senha). O usuário é salvo na tabela `TDS_Users_Mercado` com perfil `USER` e senha criptografada.

### 02 — Login personalizado

<img width="2559" height="1269" alt="image" src="https://github.com/user-attachments/assets/34b58854-a47c-4f29-b17f-e6e00a5408ae" />


Tela de login própria, diferente da fornecida pelo Spring Security. Quem não possui conta é direcionado ao cadastro.

### 03 — Área restrita do USER

<img width="2557" height="1267" alt="image" src="https://github.com/user-attachments/assets/16553e07-abfb-42cc-a471-920c299ec643" />


Após o login com perfil `USER`, o redirecionamento é para `/cliente`.

### 04 — Painel do ADMIN

<img width="2557" height="1268" alt="image" src="https://github.com/user-attachments/assets/0cd9801c-a628-422d-b033-3c372d5c0d00" />


Após o login com perfil `ADMIN`, o redirecionamento é para `/admin`.

### 05 — READ: lista de produtos

<img width="2559" height="1268" alt="image" src="https://github.com/user-attachments/assets/8632069a-15cb-4bd6-9b21-65c22dd04543" />


Produtos carregados da tabela `TDS_Sec_MVC_TB_Mercado`. O `USER` vê apenas o botão **Ver**; o `ADMIN` também vê **Editar** e **Excluir**.

### 06 — CREATE: novo produto

<img width="2557" height="1267" alt="image" src="https://github.com/user-attachments/assets/26c041dc-7605-444f-a49d-2bf076ae345a" />


### 07 — UPDATE: editar produto

<img width="2559" height="1267" alt="image" src="https://github.com/user-attachments/assets/b039c2af-33cb-4d6d-971e-a69135f82672" />


### 08 — DELETE: excluir produto

<img width="2557" height="1268" alt="image" src="https://github.com/user-attachments/assets/eb0d59eb-4617-4e1d-9711-2e7ca4c6bc1c" />


### 09 — Acesso negado

<img width="2557" height="1267" alt="image" src="https://github.com/user-attachments/assets/28f8d9b7-7cf7-47e3-ade0-794d2ab6ec4a" />


Usuário com perfil `USER` tentando acessar uma rota exclusiva do `ADMIN` (`/web/itens/novo`).

### 10 — Banco de dados

<img width="880" height="152" alt="image" src="https://github.com/user-attachments/assets/31c3871c-5603-416a-8c28-2345524e8777" />

Tabelas `tds_sec_mvc_tb_mercado` e `tds_users_mercado` no pgAdmin.

## Deploy

**Link de produção:** [https://amazin.onrender.com](https://cp05.onrender.com)
