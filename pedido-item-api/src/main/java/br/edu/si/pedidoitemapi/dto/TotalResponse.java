package br.edu.si.pedidoitemapi.dto;

import java.math.BigDecimal;

public record TotalResponse(Long pedidoId, BigDecimal valorTotal) {
}
