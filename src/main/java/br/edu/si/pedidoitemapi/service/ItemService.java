package br.edu.si.pedidoitemapi.service;

import br.edu.si.pedidoitemapi.dto.ItemRequest;
import br.edu.si.pedidoitemapi.dto.ItemResponse;
import br.edu.si.pedidoitemapi.entity.Item;
import br.edu.si.pedidoitemapi.exception.ConflictException;
import br.edu.si.pedidoitemapi.exception.ResourceNotFoundException;
import br.edu.si.pedidoitemapi.repository.ItemRepository;
import br.edu.si.pedidoitemapi.repository.PedidoItemRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final PedidoItemRepository pedidoItemRepository;

    public ItemService(ItemRepository itemRepository, PedidoItemRepository pedidoItemRepository) {
        this.itemRepository = itemRepository;
        this.pedidoItemRepository = pedidoItemRepository;
    }

    @Transactional
    public ItemResponse criar(ItemRequest request) {
        Item item = new Item(request.descricao(), request.preco(), request.estoque());
        return ItemResponse.from(itemRepository.save(item));
    }

    @Transactional(readOnly = true)
    public List<ItemResponse> listar() {
        return itemRepository.findAll().stream().map(ItemResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ItemResponse buscar(Long id) {
        return ItemResponse.from(obterItem(id));
    }

    @Transactional
    public ItemResponse atualizar(Long id, ItemRequest request) {
        Item item = obterItem(id);
        item.setDescricao(request.descricao());
        item.setPreco(request.preco());
        item.setEstoque(request.estoque());
        return ItemResponse.from(itemRepository.save(item));
    }

    @Transactional
    public void excluir(Long id) {
        Item item = obterItem(id);
        if (pedidoItemRepository.existsByItemId(id)) {
            throw new ConflictException("Não é possível excluir um item associado a pedidos.");
        }
        itemRepository.delete(item);
    }

    public Item obterItem(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item " + id + " não encontrado."));
    }
}
