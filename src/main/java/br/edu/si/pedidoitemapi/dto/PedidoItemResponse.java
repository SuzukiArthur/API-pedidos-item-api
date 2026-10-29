package br.edu.si.pedidoitemapi.dto;

import br.edu.si.pedidoitemapi.entity.PedidoItem;
import java.math.BigDecimal;

public record PedidoItemResponse(
        Long id,
        Long itemId,
        String descricao,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal) {

    public static PedidoItemResponse from(PedidoItem pedidoItem) {
        BigDecimal subtotal = pedidoItem.getPrecoUnitario().multiply(BigDecimal.valueOf(pedidoItem.getQuantidade()));
        return new PedidoItemResponse(
                pedidoItem.getId(),
                pedidoItem.getItem().getId(),
                pedidoItem.getItem().getDescricao(),
                pedidoItem.getQuantidade(),
                pedidoItem.getPrecoUnitario(),
                subtotal);
    }
}
