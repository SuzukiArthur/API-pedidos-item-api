package br.edu.si.pedidoitemapi.dto;

import br.edu.si.pedidoitemapi.entity.Cliente;

public record ClienteResumo(Long id, String nome) {

    public static ClienteResumo from(Cliente cliente) {
        return new ClienteResumo(cliente.getId(), cliente.getNome());
    }
}
