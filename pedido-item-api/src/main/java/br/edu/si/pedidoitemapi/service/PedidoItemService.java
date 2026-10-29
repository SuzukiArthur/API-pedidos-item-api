package br.edu.si.pedidoitemapi.service;

import br.edu.si.pedidoitemapi.dto.PedidoItemRequest;
import br.edu.si.pedidoitemapi.dto.PedidoItemResponse;
import br.edu.si.pedidoitemapi.dto.QuantidadeRequest;
import br.edu.si.pedidoitemapi.entity.Item;
import br.edu.si.pedidoitemapi.entity.Pedido;
import br.edu.si.pedidoitemapi.entity.PedidoItem;
import br.edu.si.pedidoitemapi.exception.RecursoNaoEncontradoException;
import br.edu.si.pedidoitemapi.repository.ItemRepository;
import br.edu.si.pedidoitemapi.repository.PedidoItemRepository;
import br.edu.si.pedidoitemapi.repository.PedidoRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoItemService {

    private final PedidoRepository pedidoRepository;
    private final ItemRepository itemRepository;
    private final PedidoItemRepository pedidoItemRepository;

    public PedidoItemService(PedidoRepository pedidoRepository,
                             ItemRepository itemRepository,
                             PedidoItemRepository pedidoItemRepository) {
        this.pedidoRepository = pedidoRepository;
        this.itemRepository = itemRepository;
        this.pedidoItemRepository = pedidoItemRepository;
    }

    /** Quantidade > 0 ja foi validada (400) no DTO. Preco unitario e copiado do cadastro do item. */
    @Transactional
    public PedidoItemResponse adicionar(Long pedidoId, PedidoItemRequest request) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado: " + pedidoId));
        Item item = itemRepository.findById(request.itemId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado: " + request.itemId()));

        PedidoItem pedidoItem = new PedidoItem();
        pedidoItem.setPedido(pedido);
        pedidoItem.setItem(item);
        pedidoItem.setQuantidade(request.quantidade());
        pedidoItem.setPrecoUnitario(item.getPreco());
        return PedidoItemResponse.de(pedidoItemRepository.save(pedidoItem));
    }

    @Transactional(readOnly = true)
    public List<PedidoItemResponse> listar(Long pedidoId) {
        verificarPedido(pedidoId);
        return pedidoItemRepository.findByPedidoId(pedidoId).stream()
                .map(PedidoItemResponse::de)
                .toList();
    }

    @Transactional
    public PedidoItemResponse alterarQuantidade(Long pedidoId, Long pedidoItemId, QuantidadeRequest request) {
        PedidoItem pedidoItem = buscarDoPedido(pedidoId, pedidoItemId);
        pedidoItem.setQuantidade(request.quantidade());
        return PedidoItemResponse.de(pedidoItemRepository.save(pedidoItem));
    }

    @Transactional
    public void remover(Long pedidoId, Long pedidoItemId) {
        pedidoItemRepository.delete(buscarDoPedido(pedidoId, pedidoItemId));
    }

    /** Garante que o pedidoItemId pertence ao pedido informado na URL. */
    private PedidoItem buscarDoPedido(Long pedidoId, Long pedidoItemId) {
        verificarPedido(pedidoId);
        return pedidoItemRepository.findByIdAndPedidoId(pedidoItemId, pedidoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Item " + pedidoItemId + " não pertence ao pedido " + pedidoId));
    }

    private void verificarPedido(Long pedidoId) {
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new RecursoNaoEncontradoException("Pedido não encontrado: " + pedidoId);
        }
    }
}
