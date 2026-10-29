package br.edu.si.pedidoitemapi.dto;

import br.edu.si.pedidoitemapi.entity.Item;
import java.math.BigDecimal;

public record ItemResponse(Long id, String descricao, BigDecimal preco, Integer estoque) {

    public static ItemResponse from(Item item) {
        return new ItemResponse(item.getId(), item.getDescricao(), item.getPreco(), item.getEstoque());
    }
}
