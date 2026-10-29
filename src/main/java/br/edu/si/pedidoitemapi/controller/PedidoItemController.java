package br.edu.si.pedidoitemapi.controller;

import br.edu.si.pedidoitemapi.dto.PedidoItemRequest;
import br.edu.si.pedidoitemapi.dto.PedidoItemResponse;
import br.edu.si.pedidoitemapi.dto.QuantidadeRequest;
import br.edu.si.pedidoitemapi.service.PedidoItemService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/pedidos/{pedidoId}/itens")
public class PedidoItemController {

    private final PedidoItemService pedidoItemService;

    public PedidoItemController(PedidoItemService pedidoItemService) {
        this.pedidoItemService = pedidoItemService;
    }

    @PostMapping
    public ResponseEntity<PedidoItemResponse> adicionar(
            @PathVariable Long pedidoId, @Valid @RequestBody PedidoItemRequest request) {
        PedidoItemResponse response = pedidoItemService.adicionar(pedidoId, request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public List<PedidoItemResponse> listar(@PathVariable Long pedidoId) {
        return pedidoItemService.listar(pedidoId);
    }

    @PutMapping("/{pedidoItemId}")
    public PedidoItemResponse atualizar(
            @PathVariable Long pedidoId,
            @PathVariable Long pedidoItemId,
            @Valid @RequestBody QuantidadeRequest request) {
        return pedidoItemService.atualizar(pedidoId, pedidoItemId, request);
    }

    @DeleteMapping("/{pedidoItemId}")
    public ResponseEntity<Void> excluir(@PathVariable Long pedidoId, @PathVariable Long pedidoItemId) {
        pedidoItemService.excluir(pedidoId, pedidoItemId);
        return ResponseEntity.noContent().build();
    }
}
