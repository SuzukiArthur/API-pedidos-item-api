package br.edu.si.pedidoitemapi.dto;

import br.edu.si.pedidoitemapi.entity.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record PedidoRequest(
        @NotNull(message = "clienteId é obrigatório") Long clienteId,
        @NotNull(message = "status é obrigatório") StatusPedido status) {
}
