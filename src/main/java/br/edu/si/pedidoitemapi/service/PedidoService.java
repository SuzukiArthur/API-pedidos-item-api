package br.edu.si.pedidoitemapi.service;

import br.edu.si.pedidoitemapi.dto.PedidoRequest;
import br.edu.si.pedidoitemapi.dto.PedidoResponse;
import br.edu.si.pedidoitemapi.entity.Pedido;
import br.edu.si.pedidoitemapi.exception.ResourceNotFoundException;
import br.edu.si.pedidoitemapi.repository.PedidoRepository;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;

    public PedidoService(PedidoRepository pedidoRepository, ClienteService clienteService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
    }

    @Transactional
    public PedidoResponse criar(PedidoRequest request) {
        Pedido pedido = new Pedido(clienteService.obterCliente(request.clienteId()), request.status());
        return PedidoResponse.from(pedidoRepository.save(pedido));
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listar() {
        return pedidoRepository.findAll().stream().map(PedidoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public PedidoResponse buscar(Long id) {
        return PedidoResponse.from(obterPedido(id));
    }

    @Transactional
    public PedidoResponse atualizar(Long id, PedidoRequest request) {
        Pedido pedido = obterPedido(id);
        pedido.setCliente(clienteService.obterCliente(request.clienteId()));
        pedido.setStatus(request.status());
        return PedidoResponse.from(pedidoRepository.save(pedido));
    }

    @Transactional
    public void excluir(Long id) {
        Pedido pedido = obterPedido(id);
        pedidoRepository.delete(pedido);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPorCliente(Long clienteId) {
        clienteService.obterCliente(clienteId);
        return pedidoRepository.findByClienteId(clienteId).stream().map(PedidoResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotal(Long id) {
        return PedidoResponse.from(obterPedido(id)).valorTotal();
    }

    public Pedido obterPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido " + id + " não encontrado."));
    }

    public Pedido salvar(Pedido pedido) {
        return pedidoRepository.save(pedido);
    }
}
