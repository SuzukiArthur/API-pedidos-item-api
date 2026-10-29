# pedido-item-api

API REST para gerenciar **clientes**, **itens** e **pedidos** (atividade prática de laboratório).
Tecnologias: Java 25, Spring Boot 3.5, Spring Web, Spring Data JPA, Bean Validation, SQLite, Maven e Swagger/OpenAPI (springdoc).

## Pré-requisitos

- JDK 25 e Maven 3.9+ (`java -version`, `mvn -version`)
- Acesso ao Maven Central para baixar as dependências

> Se o seu laboratório ainda não tem o JDK 25, altere `<java.version>` no `pom.xml` para a versão instalada (Spring Boot 3.5 roda no Java 17+).

## Como executar

```bash
mvn spring-boot:run
```

ou

```bash
mvn clean package
java -jar target/pedido-item-api-0.0.1-SNAPSHOT.jar
```

- API: http://localhost:8080/api/v1
- Swagger UI: http://localhost:8080/swagger-ui.html
- Banco: arquivo `pedido-item.db` criado automaticamente na pasta onde a aplicação é executada (`ddl-auto=update`).

## Estrutura

```
src/main/java/br/edu/si/pedidoitemapi/
├── PedidoItemApiApplication.java
├── controller/   Cliente, Item, Pedido, PedidoItem (endpoints REST)
├── service/      regras de negócio e transações
├── repository/   Spring Data JPA
├── entity/       Cliente, Pedido, Item, PedidoItem, StatusPedido
├── dto/          requests/responses (evitam ciclos de serialização)
└── exception/    exceções e tratamento global (400/404/409)
```

## Endpoints

| Recurso | Endpoints |
|---|---|
| Clientes | `POST/GET /api/v1/clientes`, `GET/PUT/DELETE /api/v1/clientes/{id}` |
| Itens | `POST/GET /api/v1/itens`, `GET/PUT/DELETE /api/v1/itens/{id}` |
| Pedidos | `POST/GET /api/v1/pedidos`, `GET/PUT/DELETE /api/v1/pedidos/{id}` |
| Itens do pedido | `POST/GET /api/v1/pedidos/{id}/itens`, `PUT/DELETE /api/v1/pedidos/{id}/itens/{pedidoItemId}` |
| Desafio extra | `GET /api/v1/clientes/{id}/pedidos`, `GET /api/v1/pedidos/{id}/total` |

### Exemplos

```json
POST /api/v1/clientes   { "nome": "Maria Silva", "email": "maria@email.com", "telefone": "27999999999" }
POST /api/v1/itens      { "descricao": "Teclado USB", "preco": 85.90, "estoque": 30 }
POST /api/v1/pedidos    { "clienteId": 1, "status": "ABERTO" }
POST /api/v1/pedidos/1/itens        { "itemId": 1, "quantidade": 2 }
PUT  /api/v1/pedidos/1/itens/1      { "quantidade": 3 }
```

## Regras de negócio implementadas

- Pedido de cliente inexistente e item inexistente → **404**.
- Quantidade ≤ 0 e preço ≤ 0 → **400** (Bean Validation).
- Cliente com pedidos e item associado a pedidos não podem ser excluídos → **409**.
- E-mail de cliente duplicado → **409**.
- Excluir um pedido remove seus `pedido_item` na mesma transação (cascade), sem apagar o cadastro dos itens → **204**.
- `data_pedido` é preenchida pelo servidor; `preco_unitario` é copiado do cadastro do item no momento da inclusão e **não** é aceito do cliente. Mudar o preço do item depois não altera pedidos existentes.
- `pedidoItemId` deve pertencer ao pedido da URL (senão **404**).
- Status permitidos: `ABERTO`, `FINALIZADO`, `CANCELADO` (status inválido → **400**). Sem baixa automática de estoque.
- `subtotal` e `valorTotal` são calculados no backend (DTOs), sem colunas extras.

## SQLite

- `PRAGMA foreign_keys=ON` é aplicado em **toda conexão** via parâmetro da URL JDBC (`?foreign_keys=on`).
- O pool Hikari usa 1 conexão (`maximum-pool-size=1`) porque o SQLite aceita um escritor por vez.
- `ddl-auto=update` é só para esta atividade; em produção use migrações versionadas (Flyway/Liquibase).

## Testes

Com a API rodando:

```bash
bash scripts/roteiro-testes.sh
```

O script executa o roteiro da seção 8.1 via `curl` (ajuste `C1`, `C2`, `I1`, `I2`, `P` se os IDs gerados forem diferentes de 1/2). Para as capturas de tela, repita os passos no Swagger UI. Confira os dados no arquivo SQLite:

```bash
sqlite3 pedido-item.db "select * from pedido; select * from pedido_item;"
```
