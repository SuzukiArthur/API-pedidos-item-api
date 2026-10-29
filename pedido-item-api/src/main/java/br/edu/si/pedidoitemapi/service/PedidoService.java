package br.edu.si.pedidoitemapi.service;

import br.edu.si.pedidoitemapi.dto.PedidoRequest;
import br.edu.si.pedidoitemapi.dto.PedidoResponse;
import br.edu.si.pedidoitemapi.dto.TotalResponse;
import br.edu.si.pedidoitemapi.entity.Cliente;
import br.edu.si.pedidoitemapi.entity.Pedido;
import br.edu.si.pedidoitemapi.entity.StatusPedido;
import br.edu.si.pedidoitemapi.exception.RecursoNaoEncontradoException;
import br.edu.si.pedidoitemapi.repository.ClienteRepository;
import br.edu.si.pedidoitemapi.repository.PedidoRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;

    public PedidoService(PedidoRepository pedidoRepository, ClienteRepository clienteRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public PedidoResponse criar(PedidoRequest request) {
        Cliente cliente = buscarCliente(request.clienteId());
        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataPedido(LocalDateTime.now().withNano(0)); // preenchida pelo servidor
        pedido.setStatus(request.status() != null ? request.status() : StatusPedido.ABERTO);
        return PedidoResponse.de(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidoRepository.findAll().stream().map(PedidoResponse::de).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscar(Long id) {
        return PedidoResponse.de(buscarEntidade(id));
    }

    @Transactional
    public PedidoResponse atualizar(Long id, PedidoRequest request) {
        Pedido pedido = buscarEntidade(id);
        pedido.setCliente(buscarCliente(request.clienteId()));
        if (request.status() != null) {
            pedido.setStatus(request.status());
        }
        return PedidoResponse.de(pedidoRepository.save(pedido));
    }

    /** Remove o pedido e seus pedido_item na mesma transacao (cascade); itens do cadastro permanecem. */
    @Transactional
    public void excluir(Long id) {
        pedidoRepository.delete(buscarEntidade(id));
    }

    @Transactional(readOnly = true)
    public TotalResponse total(Long id) {
        PedidoResponse pedido = buscar(id);
        return new TotalResponse(pedido.id(), pedido.valorTotal());
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorCliente(Long clienteId) {
        buscarCliente(clienteId);
        return pedidoRepository.findByClienteId(clienteId).stream().map(PedidoResponse::de).toList();
    }

    Pedido buscarEntidade(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado: " + id));
    }

    private Cliente buscarCliente(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente não encontrado: " + id));
    }
}
