package br.edu.si.pedidoitemapi.dto;

import br.edu.si.pedidoitemapi.entity.PedidoItem;
import java.math.BigDecimal;
import java.math.RoundingMode;

public record PedidoItemResponse(
        Long id,
        Long itemId,
        String descricao,
        Integer quantidade,
        BigDecimal precoUnitario,
        BigDecimal subtotal) {

    /** Deve ser chamado dentro de transacao (associacao LAZY com Item). */
    public static PedidoItemResponse de(PedidoItem pi) {
        BigDecimal preco = pi.getPrecoUnitario().setScale(2, RoundingMode.HALF_UP);
        BigDecimal subtotal = preco.multiply(BigDecimal.valueOf(pi.getQuantidade()));
        return new PedidoItemResponse(
                pi.getId(),
                pi.getItem().getId(),
                pi.getItem().getDescricao(),
                pi.getQuantidade(),
                preco,
                subtotal);
    }
}
