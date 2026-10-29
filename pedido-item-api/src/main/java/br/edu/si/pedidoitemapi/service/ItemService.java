package br.edu.si.pedidoitemapi.service;

import br.edu.si.pedidoitemapi.entity.Item;
import br.edu.si.pedidoitemapi.exception.ConflitoException;
import br.edu.si.pedidoitemapi.exception.RecursoNaoEncontradoException;
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
    public Item criar(Item item) {
        item.setId(null);
        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public List<Item> listar() {
        return itemRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Item buscar(Long id) {
        return itemRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Item não encontrado: " + id));
    }

    /** Alterar o preco aqui nao afeta pedidos existentes (pedido_item guarda o preco_unitario). */
    @Transactional
    public Item atualizar(Long id, Item dados) {
        Item item = buscar(id);
        item.setDescricao(dados.getDescricao());
        item.setPreco(dados.getPreco());
        item.setEstoque(dados.getEstoque());
        return itemRepository.save(item);
    }

    @Transactional
    public void excluir(Long id) {
        Item item = buscar(id);
        if (pedidoItemRepository.existsByItemId(id)) {
            throw new ConflitoException("Item associado a pedidos e não pode ser excluído.");
        }
        itemRepository.delete(item);
    }
}
