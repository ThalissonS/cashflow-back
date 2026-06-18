# TAREFAS — Backend Cash-Flow (`cashflow-back`)

> Arquivo para o agente que trabalha **somente no repositório do backend**.
> Repo: Spring Boot 4 / Java 21 / JPA. H2 (dev) / PostgreSQL (prod). Deploy na Render.
> Leia antes de mexer: `GUIA-PARA-IA.md`, `README.md`, `BACKEND-FASES-0-3.md`, `SIMULADOR-INVESTIMENTOS.md`.
> **Este repo é independente do frontend.** Você não tem acesso ao front; o contrato de
> comunicação entre os dois está na seção "Contrato de API" abaixo — é o que o front espera.

---

## 0. Versão base
Use a versão **com simulador** (a que tem `LancamentoController`, `MetaController`,
`ResumoController`, `InvestimentoController`). Se houver uma versão fase-1 só com
`Despesa`/`Categoria`/`Usuario`, **descarte-a**.

---

## 1. Diagnóstico — o que já existe e o que falta

**Já existe e funciona (mas nunca foi compilado na origem):**
- `/categorias` (GET, POST) — categorias globais.
- `/lancamentos` (POST, GET, GET `/mes/{ano}/{mes}`, DELETE `/{id}`).
- `/metas` (POST, GET, GET `/{id}/projecao`, DELETE `/{id}`).
- `/resumo/{ano}/{mes}` — foto financeira do mês com CDI real do Banco Central.
- `/investimentos/tipos`, `/ofertas`, `/simular` — CDB/LCI/LCA/Poupança com IR regressivo.
- `/despesas` — entidade **legada** (fase 1), mantida só para não quebrar o front antigo.

**FALTA (o `GUIA-PARA-IA.md` promete, mas NÃO existe no código):**
- ❌ Spring Security — nenhuma config; todos os endpoints estão abertos.
- ❌ JWT — sem geração/validação de token, sem `Authorization: Bearer`.
- ❌ BCrypt — `Usuario.senha` é salva em **texto plano** (`UserController.criar` faz `save` direto).
- ❌ Login/registro — só existe `GET/POST /usuarios` cru, sem autenticação.
- ❌ Status de aprovação (`PENDENTE`/`APROVADO`) e `AdminInicializador` — não existem.
- ❌ `UserDetails` / `isEnabled()` no `Usuario` — não implementado.
- ❌ Isolamento de dados por usuário — `LancamentoRepository` e `MetaRepository` **não
  filtram por `usuarioId`**. O campo `usuario` em `Lancamento` existe, está marcado como
  opcional e nunca é preenchido a partir do token.

---

## 2. Tarefas

### B1. Segurança e autenticação (JWT + BCrypt) — PRIORIDADE ALTA
- [ ] Adicionar no `pom.xml`: `spring-boot-starter-security` e lib JWT (ex.: `io.jsonwebtoken:jjwt`).
- [ ] Fazer `Usuario` implementar `UserDetails`; `isEnabled()` = (`status == APROVADO`).
- [ ] Criar enum `StatusUsuario` (`PENDENTE`, `APROVADO`) e papel (`ROLE_USER`, `ROLE_ADMIN`).
- [ ] Hashear senha com `BCryptPasswordEncoder` no registro. **Nunca** salvar/retornar senha em texto plano.
- [ ] Criar `SecurityConfig` (stateless, filtro JWT, regras de rota). Mover a config de **CORS
  para dentro do `SecurityConfig`** e ajustar/remover o `CorsConfig` atual (hoje libera `*`;
  em prod restringir à origem do front).
- [ ] Filtro JWT (`OncePerRequestFilter`): lê `Authorization: Bearer`, valida e popula o `SecurityContext`.
- [ ] Endpoints de auth:
  - `POST /auth/registrar` — cria usuário `PENDENTE`, senha com BCrypt.
  - `POST /auth/login` — valida credenciais e retorna `{ token, nome, papel }`. Deve falhar para `PENDENTE` (via `isEnabled`).
- [ ] `AdminInicializador` (`CommandLineRunner`): cria admin inicial a partir de env vars
  (`ADMIN_EMAIL`, `ADMIN_SENHA`) se ainda não existir.
- [ ] Admin: `GET /admin/usuarios` (lista **sem expor senha**), `POST /admin/usuarios/{id}/aprovar`.
  Proteger com papel ADMIN; **nunca** rejeitar/expor o admin.

### B2. Isolamento de dados por usuário — PRIORIDADE ALTA
- [ ] Tornar dono de `Lancamento` e `Meta` **obrigatório**, preenchido a partir do **usuário
  do token**, nunca do corpo da requisição.
- [ ] `LancamentoRepository` e `MetaRepository`: adicionar `usuarioId` em **todas** as
  queries/somatórios (`somarPorTipoNoPeriodo`, `totalAcumuladoPorTipo`, `totalPorTipo`, etc.).
- [ ] `ResumoMensalService` e `MetaService` recebem o `usuarioId` do contexto de segurança e o repassam ao repositório.
- [ ] Manter **Categorias globais** (decisão do GUIA). Documentar.

### B3. Limpeza e robustez
- [ ] Trocar `@Autowired` em campo por **injeção via construtor** (`UserController` ainda usa campo).
- [ ] Rodar `./mvnw clean compile` e `./mvnw test` — **nunca foi compilado na origem**. Corrigir o que falhar.
- [ ] Conferir `application-prod.properties` (Postgres por env vars) e a subida do perfil `prod` na Render.
- [ ] (Opcional, recomendado) Migrar `ddl-auto=update` → **Flyway** para versionar schema.
- [ ] (Opcional) Remover `Despesa` legada **só depois** que o front migrar para `/lancamentos`.

---

## 3. Contrato de API (o front depende EXATAMENTE destes nomes)

> Se você mudar qualquer nome de campo aqui, avise — o front espelha isso 1:1.

**Auth (A CRIAR):** `POST /auth/registrar`; `POST /auth/login` → `{ token, nome, papel }`.

**ResumoMensalDTO** (`GET /resumo/{ano}/{mes}`):
`ano, mes, totalReceitas, totalGastosFixos, totalGastosVariaveis, totalInvestidoNoMes,
totalGastos, saldo, patrimonioInvestido, cdiAnual, rendimentoEstimadoMes`.

**LancamentoRequest** (`POST /lancamentos`):
`descricao (obrig), valor (>0), tipo (RECEITA|GASTO_FIXO|GASTO_VARIAVEL|INVESTIMENTO),
ano (>=2000), mes (1-12), categoriaId?`.
Demais: `GET /lancamentos`, `GET /lancamentos/mes/{ano}/{mes}`, `DELETE /lancamentos/{id}`.

**MetaRequest** (`POST /metas`):
`nome (obrig), valorAlvo (>0), aporteMensal (>=0), valorInicial?`.
Demais: `GET /metas`, `GET /metas/{id}/projecao`, `DELETE /metas/{id}`.

**SimulacaoRequest** (`POST /investimentos/simular`):
`tipo (CDB|LCI|LCA|POUPANCA), valorInicial (>=0), aporteMensal?, prazoMeses (>=1),
percentualCdi?, bancoId?`.
Demais: `GET /investimentos/tipos`, `GET /investimentos/ofertas?tipo={tipo}`.

**Categoria** (`/categorias`): `{ id, nome, descricao }` — global, GET e POST.

---

## 4. Pontos de atenção (não esquecer)
- Conversão de taxa anual→mensal por **juros compostos** `(1+i)^(1/12)-1`, nunca dividir por 12.
- `usuarioId` sempre vem do **token**, nunca do corpo da requisição.
- Área de admin **nunca** expõe senha e **nunca** rejeita o admin.
- IR do simulador usa aproximação `meses × 30` dias (consciente; difere em centavos em fronteiras como 24 meses).
- Mudanças sensíveis de segurança exigem confirmação do dono.

## 5. Ordem sugerida
B1 → B2 → B3.
