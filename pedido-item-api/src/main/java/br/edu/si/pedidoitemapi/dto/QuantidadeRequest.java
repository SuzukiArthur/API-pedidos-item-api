package br.edu.si.pedidoitemapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

/** Corpo de PUT /pedidos/{id}/itens/{pedidoItemId}. */
public record QuantidadeRequest(
        @NotNull @Positive Integer quantidade) {
}
