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

    public record ClienteResumo(Long id, String nome) {
    }

    /** Deve ser chamado dentro de transacao (colecoes LAZY). */
    public static PedidoResponse de(Pedido pedido) {
        List<PedidoItemResponse> itens = pedido.getItens().stream()
                .map(PedidoItemResponse::de)
                .toList();
        BigDecimal total = itens.stream()
                .map(PedidoItemResponse::subtotal)
                .reduce(BigDecimal.ZERO.setScale(2), BigDecimal::add);
        return new PedidoResponse(
                pedido.getId(),
                new ClienteResumo(pedido.getCliente().getId(), pedido.getCliente().getNome()),
                pedido.getDataPedido(),
                pedido.getStatus(),
                itens,
                total);
    }
}
