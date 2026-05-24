# Cash-Flow — Controle Financeiro Pessoal

API REST para acompanhar receitas, gastos e investimentos e projetar **quanto tempo falta para atingir uma meta financeira**.

> 🚧 **Projeto em desenvolvimento.** O objetivo da primeira fase é registrar receita, gastos fixos, gastos variáveis e o investimento do mês, calcular o rendimento (por exemplo, com base no CDI) e mostrar a relação **tempo × meta** — ou seja, quanto ainda falta para alcançar o objetivo.

## Tecnologias

- **Java 21**
- **Spring Boot** — Spring Web MVC, Spring Data JPA e Bean Validation
- **Banco H2** (em memória) — veja a nota em [Status](#status-e-próximos-passos)
- **Frontend** em Angular (repositório separado)
- Backend hospedado na **Render**

## Arquitetura

O projeto é organizado em camadas, separando claramente as responsabilidades:

| Camada | Papel |
|--------|-------|
| `controllers` | Expõem os endpoints REST |
| `services` | Concentram as regras de negócio |
| `repositories` | Acesso a dados via Spring Data JPA |
| `models` | Entidades: `Usuario`, `Categoria`, `Despesa` |
| `exceptions` | Tratamento centralizado de erros (`ResourceExceptionHandler`, `StandardError`) |
| `config` | Configuração de CORS |

## Endpoints (exemplos)

| Método | Rota | Descrição |
|--------|------|-----------|
| `GET` | `/despesas` | Lista todas as despesas |
| `POST` | `/despesas` | Cria uma nova despesa |
| `GET` | `/despesas/total` | Retorna o total gasto |

Há também endpoints para usuários e categorias.

## Como rodar localmente

```bash
# com o Maven Wrapper incluído no projeto
./mvnw spring-boot:run
```

A aplicação sobe em `http://localhost:8080`.
O console do banco H2 fica disponível em `http://localhost:8080/h2-console`.

## Status e próximos passos

- [x] Estrutura base da API (despesas, categorias e usuários)
- [x] Tratamento de exceções e configuração de CORS
- [ ] Cálculo de rendimento de investimento (CDI) e projeção **tempo × meta**
- [ ] Migrar de H2 (em memória) para um banco persistente, como **PostgreSQL** — atualmente os dados são reiniciados a cada restart da aplicação
- [ ] Integração completa com o frontend Angular

## Autor

**Thalisson Pereira**
[LinkedIn](https://www.linkedin.com/in/thalissonpereira2003) · [GitHub](https://github.com/ThalissonS)
