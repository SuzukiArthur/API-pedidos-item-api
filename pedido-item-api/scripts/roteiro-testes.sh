#!/usr/bin/env bash
# Roteiro de testes (secao 8.1) via curl. Execute com a API rodando em localhost:8080.
# Uso: bash scripts/roteiro-testes.sh
# Dependencias: curl. Todos os dados sao ficticios.
BASE=${BASE:-http://localhost:8080/api/v1}
H='Content-Type: application/json'
SUF=$(date +%s)   # evita e-mail duplicado ao reexecutar

req() { # req <titulo> <metodo> <url> [json]
  echo; echo "### $1"
  if [ -n "$4" ]; then
    curl -s -w '\n-> HTTP %{http_code}\n' -X "$2" -H "$H" -d "$4" "$BASE$3"
  else
    curl -s -w '\n-> HTTP %{http_code}\n' -X "$2" "$BASE$3"
  fi
}

req "1. Cadastrar cliente 1 (201)" POST /clientes "{\"nome\":\"Maria Silva\",\"email\":\"maria$SUF@email.com\",\"telefone\":\"27999999999\"}"
req "1. Cadastrar cliente 2 (201)" POST /clientes "{\"nome\":\"Joao Souza\",\"email\":\"joao$SUF@email.com\"}"
req "2. Listar clientes (200)" GET /clientes
echo; echo ">> Informe os IDs retornados acima (cliente 1, cliente 2) e ajuste os comandos abaixo se nao forem 1 e 2."
C1=${C1:-1}; C2=${C2:-2}
req "3. Alterar telefone do cliente $C1 (200)" PUT /clientes/$C1 "{\"nome\":\"Maria Silva\",\"email\":\"maria$SUF@email.com\",\"telefone\":\"27988888888\"}"
req "4. Cadastrar item A (201)" POST /itens '{"descricao":"Teclado USB","preco":85.90,"estoque":30}'
req "4. Cadastrar item B (201)" POST /itens '{"descricao":"Mouse USB","preco":45.00,"estoque":50}'
req "4. Cadastrar item C (201)" POST /itens '{"descricao":"Monitor 24","preco":799.99,"estoque":10}'
req "4b. Preco invalido (400)" POST /itens '{"descricao":"Invalido","preco":0,"estoque":1}'
I1=${I1:-1}; I2=${I2:-2}
req "5. Criar pedido para cliente existente (201)" POST /pedidos "{\"clienteId\":$C1,\"status\":\"ABERTO\"}"
P=${P:-1}
req "6. Adicionar item 1 ao pedido (201)" POST /pedidos/$P/itens "{\"itemId\":$I1,\"quantidade\":2}"
req "6. Adicionar item 2 ao pedido (201)" POST /pedidos/$P/itens "{\"itemId\":$I2,\"quantidade\":1}"
req "6b. Quantidade zero (400)" POST /pedidos/$P/itens "{\"itemId\":$I1,\"quantidade\":0}"
req "6c. Item inexistente (404)" POST /pedidos/$P/itens '{"itemId":9999,"quantidade":1}'
req "7. Consultar pedido com itens (200)" GET /pedidos/$P
req "8. Alterar quantidade do pedido_item 1 (200)" PUT /pedidos/$P/itens/1 '{"quantidade":3}'
req "Preco do item muda; pedido nao (200)" PUT /itens/$I1 '{"descricao":"Teclado USB","preco":99.90,"estoque":30}'
req "   -> pedido continua com preco antigo (200)" GET /pedidos/$P
req "Excluir item associado (409)" DELETE /itens/$I1
req "Excluir cliente com pedido (409)" DELETE /clientes/$C1
req "9. Remover item do pedido (204)" DELETE /pedidos/$P/itens/2
req "10. Pedido com cliente inexistente (404)" POST /pedidos '{"clienteId":9999,"status":"ABERTO"}'
req "Extra: pedidos do cliente (200)" GET /clientes/$C1/pedidos
req "Extra: total do pedido (200)" GET /pedidos/$P/total
req "11. Excluir pedido (204)" DELETE /pedidos/$P
req "11. Pedido excluido (404)" GET /pedidos/$P
req "12. Excluir cliente sem pedidos (204)" DELETE /clientes/$C2

echo; echo "Confira tambem o arquivo SQLite: sqlite3 pedido-item.db 'select * from pedido_item;'"
