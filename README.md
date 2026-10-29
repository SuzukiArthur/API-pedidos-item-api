# pedido-item-api

API REST para gerenciar clientes, itens e pedidos usando Java 25, Spring Boot, Spring Data JPA e SQLite.

## Requisitos

- JDK 25
- Maven 3.9+

## Execução

```powershell
mvn spring-boot:run
```

A aplicação fica disponível em `http://localhost:8080` e a documentação Swagger em `http://localhost:8080/swagger-ui.html`.

## Banco

A propriedade `spring.datasource.url=jdbc:sqlite:pedido-item.db` cria o arquivo SQLite na raiz do projeto.

## Endpoints principais

- `POST /api/v1/clientes`
- `GET /api/v1/clientes`
- `GET /api/v1/clientes/{id}`
- `PUT /api/v1/clientes/{id}`
- `DELETE /api/v1/clientes/{id}`
- `POST /api/v1/itens`
- `GET /api/v1/itens`
- `GET /api/v1/itens/{id}`
- `PUT /api/v1/itens/{id}`
- `DELETE /api/v1/itens/{id}`
- `POST /api/v1/pedidos`
- `GET /api/v1/pedidos`
- `GET /api/v1/pedidos/{id}`
- `PUT /api/v1/pedidos/{id}`
- `DELETE /api/v1/pedidos/{id}`
- `POST /api/v1/pedidos/{id}/itens`
- `GET /api/v1/pedidos/{id}/itens`
- `PUT /api/v1/pedidos/{id}/itens/{pedidoItemId}`
- `DELETE /api/v1/pedidos/{id}/itens/{pedidoItemId}`
- `GET /api/v1/clientes/{id}/pedidos`
- `GET /api/v1/pedidos/{id}/total`

## Regras de negócio

- clientes inexistentes e itens inexistentes são rejeitados;
- quantidade menor ou igual a zero é inválida;
- preços menores ou iguais a zero são rejeitados;
- não é possível excluir cliente com pedidos;
- não é possível excluir item associado a pedidos;
- ao excluir pedido, os registros de `pedido_item` são removidos automaticamente;
- status permitido: `ABERTO`, `FINALIZADO`, `CANCELADO`.

## Testes

```powershell
mvn test
```
