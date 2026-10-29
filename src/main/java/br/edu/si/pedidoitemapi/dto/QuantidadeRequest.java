package br.edu.si.pedidoitemapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record QuantidadeRequest(@NotNull(message = "quantidade é obrigatória") @Positive(message = "quantidade deve ser maior que zero") Integer quantidade) {
}
