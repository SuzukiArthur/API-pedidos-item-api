package br.edu.si.pedidoitemapi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ItemRequest(
        @NotBlank(message = "descricao é obrigatória") @Size(max = 150, message = "descricao deve ter até 150 caracteres") String descricao,
        @NotNull(message = "preco é obrigatório") @DecimalMin(value = "0.01", message = "preco deve ser maior que zero") @Digits(integer = 8, fraction = 2, message = "preco deve ter até 2 casas decimais") BigDecimal preco,
        @NotNull(message = "estoque é obrigatório") @Min(value = 0, message = "estoque não pode ser negativo") Integer estoque) {
}
