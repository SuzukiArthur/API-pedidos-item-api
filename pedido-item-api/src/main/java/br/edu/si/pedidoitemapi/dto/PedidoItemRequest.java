package br.edu.si.pedidoitemapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** Corpo de POST /pedidos/{id}/itens. O preco unitario NAO e aceito do cliente. */
public record PedidoItemRequest(
        @NotNull Long itemId,
        @NotNull @Positive Integer quantidade) {
}
