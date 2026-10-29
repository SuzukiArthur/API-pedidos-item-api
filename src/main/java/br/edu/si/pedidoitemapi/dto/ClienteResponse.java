package br.edu.si.pedidoitemapi.dto;

import br.edu.si.pedidoitemapi.entity.Cliente;

public record ClienteResponse(Long id, String nome, String email, String telefone) {

    public static ClienteResponse from(Cliente cliente) {
        return new ClienteResponse(cliente.getId(), cliente.getNome(), cliente.getEmail(), cliente.getTelefone());
    }
}
