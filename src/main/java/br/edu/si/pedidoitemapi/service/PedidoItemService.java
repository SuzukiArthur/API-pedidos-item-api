package br.edu.si.pedidoitemapi.service;

import br.edu.si.pedidoitemapi.dto.PedidoItemRequest;
import br.edu.si.pedidoitemapi.dto.PedidoItemResponse;
import br.edu.si.pedidoitemapi.dto.QuantidadeRequest;
import br.edu.si.pedidoitemapi.entity.Pedido;
import br.edu.si.pedidoitemapi.entity.PedidoItem;
import br.edu.si.pedidoitemapi.exception.ResourceNotFoundException;
import br.edu.si.pedidoitemapi.repository.PedidoItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PedidoItemService {

    private final PedidoItemRepository pedidoItemRepository;
    private final PedidoService pedidoService;
    private final ItemService itemService;

    public PedidoItemService(PedidoItemRepository pedidoItemRepository, PedidoService pedidoService, ItemService itemService) {
        this.pedidoItemRepository = pedidoItemRepository;
        this.pedidoService = pedidoService;
        this.itemService = itemService;
    }

    @Transactional
    public PedidoItemResponse adicionar(Long pedidoId, PedidoItemRequest request) {
        Pedido pedido = pedidoService.obterPedido(pedidoId);
        var item = itemService.obterItem(request.itemId());

        PedidoItem pedidoItem = new PedidoItem(item, request.quantidade(), item.getPreco());
        pedido.adicionarItem(pedidoItem);

        PedidoItem persistedItem = pedidoItemRepository.saveAndFlush(pedidoItem);
        return PedidoItemResponse.from(persistedItem);
    }

    @Transactional(readOnly = true)
    public List<PedidoItemResponse> listar(Long pedidoId) {
        pedidoService.obterPedido(pedidoId);
        return pedidoItemRepository.findByPedidoIdOrderById(pedidoId).stream()
                .map(PedidoItemResponse::from)
                .toList();
    }

    @Transactional
    public PedidoItemResponse atualizar(Long pedidoId, Long pedidoItemId, QuantidadeRequest request) {
        PedidoItem pedidoItem = obterPedidoItem(pedidoId, pedidoItemId);
        pedidoItem.setQuantidade(request.quantidade());
        return PedidoItemResponse.from(pedidoItemRepository.save(pedidoItem));
    }

    @Transactional
    public void excluir(Long pedidoId, Long pedidoItemId) {
        PedidoItem pedidoItem = obterPedidoItem(pedidoId, pedidoItemId);
        pedidoItem.getPedido().removerItem(pedidoItem);
        pedidoItemRepository.delete(pedidoItem);
    }

    private PedidoItem obterPedidoItem(Long pedidoId, Long pedidoItemId) {
        pedidoService.obterPedido(pedidoId);
        return pedidoItemRepository.findByPedidoIdAndId(pedidoId, pedidoItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Item de pedido " + pedidoItemId + " não encontrado no pedido " + pedidoId + "."));
    }
}
