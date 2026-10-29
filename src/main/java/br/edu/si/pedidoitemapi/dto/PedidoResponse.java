package br.edu.si.pedidoitemapi.dto;

import br.edu.si.pedidoitemapi.entity.Pedido;
import br.edu.si.pedidoitemapi.entity.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        ClienteResumo cliente,
        LocalDateTime dataPedido,
        StatusPedido status,
        List<PedidoItemResponse> itens,
        BigDecimal valorTotal) {

    public static PedidoResponse from(Pedido pedido) {
        List<PedidoItemResponse> itens = pedido.getItens().stream()
                .map(PedidoItemResponse::from)
                .toList();
        BigDecimal valorTotal = itens.stream()
                .map(PedidoItemResponse::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new PedidoResponse(
                pedido.getId(),
                ClienteResumo.from(pedido.getCliente()),
                pedido.getDataPedido(),
                pedido.getStatus(),
                itens,
                valorTotal);
    }
}
