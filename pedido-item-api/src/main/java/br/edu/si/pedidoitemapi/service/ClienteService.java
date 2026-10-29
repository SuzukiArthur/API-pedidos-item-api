package br.edu.si.pedidoitemapi.service;

import br.edu.si.pedidoitemapi.entity.Cliente;
import br.edu.si.pedidoitemapi.exception.ConflitoException;
import br.edu.si.pedidoitemapi.exception.RecursoNaoEncontradoException;
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
    public Cliente criar(Cliente cliente) {
        if (clienteRepository.existsByEmail(cliente.getEmail())) {
            throw new ConflitoException("Já existe cliente com o e-mail " + cliente.getEmail());
        }
        cliente.setId(null);
        return clienteRepository.save(cliente);
    }

    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Cliente buscar(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + id));
    }

    @Transactional
    public Cliente atualizar(Long id, Cliente dados) {
        Cliente cliente = buscar(id);
        if (clienteRepository.existsByEmailAndIdNot(dados.getEmail(), id)) {
            throw new ConflitoException("Já existe cliente com o e-mail " + dados.getEmail());
        }
        cliente.setNome(dados.getNome());
        cliente.setEmail(dados.getEmail());
        cliente.setTelefone(dados.getTelefone());
        return clienteRepository.save(cliente);
    }

    @Transactional
    public void excluir(Long id) {
        Cliente cliente = buscar(id);
        if (pedidoRepository.existsByClienteId(id)) {
            throw new ConflitoException("Cliente possui pedidos e não pode ser excluído.");
        }
        clienteRepository.delete(cliente);
    }
}
