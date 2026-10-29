package br.edu.si.pedidoitemapi.repository;

import br.edu.si.pedidoitemapi.entity.PedidoItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {

    boolean existsByItemId(Long itemId);

    List<PedidoItem> findByPedidoIdOrderById(Long pedidoId);

    Optional<PedidoItem> findByPedidoIdAndId(Long pedidoId, Long id);
}
