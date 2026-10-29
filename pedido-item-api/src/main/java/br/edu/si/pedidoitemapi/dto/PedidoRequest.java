package br.edu.si.pedidoitemapi.dto;

import br.edu.si.pedidoitemapi.entity.StatusPedido;
import jakarta.validation.constraints.NotNull;

/** Corpo de POST/PUT /pedidos. Se status vier nulo na criacao, assume ABERTO. */
public record PedidoRequest(
        @NotNull Long clienteId,
        StatusPedido status) {
}
