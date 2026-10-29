package br.edu.si.pedidoitemapi.controller;

import br.edu.si.pedidoitemapi.dto.PedidoItemRequest;
import br.edu.si.pedidoitemapi.dto.PedidoItemResponse;
import br.edu.si.pedidoitemapi.dto.QuantidadeRequest;
import br.edu.si.pedidoitemapi.service.PedidoItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/pedidos/{pedidoId}/itens")
@Tag(name = "Itens do pedido")
public class PedidoItemController {

    private final PedidoItemService service;

    public PedidoItemController(PedidoItemService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoItemResponse adicionar(@PathVariable Long pedidoId,
                                        @Valid @RequestBody PedidoItemRequest request) {
        return service.adicionar(pedidoId, request);
    }

    @GetMapping
    public List<PedidoItemResponse> listar(@PathVariable Long pedidoId) {
        return service.listar(pedidoId);
    }

    @PutMapping("/{pedidoItemId}")
    public PedidoItemResponse alterarQuantidade(@PathVariable Long pedidoId,
                                                @PathVariable Long pedidoItemId,
                                                @Valid @RequestBody QuantidadeRequest request) {
        return service.alterarQuantidade(pedidoId, pedidoItemId, request);
    }

    @DeleteMapping("/{pedidoItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long pedidoId, @PathVariable Long pedidoItemId) {
        service.remover(pedidoId, pedidoItemId);
    }
}
