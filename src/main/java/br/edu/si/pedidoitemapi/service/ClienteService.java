package br.edu.si.pedidoitemapi.service;

import br.edu.si.pedidoitemapi.dto.ClienteRequest;
import br.edu.si.pedidoitemapi.dto.ClienteResponse;
import br.edu.si.pedidoitemapi.entity.Cliente;
import br.edu.si.pedidoitemapi.exception.ConflictException;
import br.edu.si.pedidoitemapi.exception.ResourceNotFoundException;
import br.edu.si.pedidoitemapi.repository.ClienteRepository;
import br.edu.si.pedidoitemapi.repository.PedidoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final PedidoRepository pedidoRepository;

    public ClienteService(ClienteRepository clienteRepository, PedidoRepository pedidoRepository) {
        this.clienteRepository = clienteRepository;
        this.pedidoRepository = pedidoRepository;
    }

    @Transactional
    public ClienteResponse criar(ClienteRequest request) {
        if (clienteRepository.existsByEmail(request.email())) {
            throw new ConflictException("Já existe um cliente cadastrado com este e-mail.");
        }
        Cliente cliente = new Cliente(request.nome(), request.email(), request.telefone());
        return ClienteResponse.from(clienteRepository.save(cliente));
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream().map(ClienteResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscar(Long id) {
        return ClienteResponse.from(obterCliente(id));
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = obterCliente(id);
        if (clienteRepository.existsByEmailAndIdNot(request.email(), id)) {
            throw new ConflictException("Já existe um cliente cadastrado com este e-mail.");
        }
        cliente.setNome(request.nome());
        cliente.setEmail(request.email());
        cliente.setTelefone(request.telefone());
        return ClienteResponse.from(clienteRepository.save(cliente));
    }

    @Transactional
    public void excluir(Long id) {
        Cliente cliente = obterCliente(id);
        if (pedidoRepository.existsByClienteId(id)) {
            throw new ConflictException("Não é possível excluir um cliente que possui pedidos.");
        }
        clienteRepository.delete(cliente);
    }

    public Cliente obterCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente " + id + " não encontrado."));
    }
}
