package br.edu.si.pedidoitemapi.controller;

import br.edu.si.pedidoitemapi.dto.PedidoResponse;
import br.edu.si.pedidoitemapi.entity.Cliente;
import br.edu.si.pedidoitemapi.service.ClienteService;
import br.edu.si.pedidoitemapi.service.PedidoService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clientes")
@Tag(name = "Clientes")
public class ClienteController {

    private final ClienteService clienteService;
    private final PedidoService pedidoService;

    public ClienteController(ClienteService clienteService, PedidoService pedidoService) {
        this.clienteService = clienteService;
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cliente criar(@Valid @RequestBody Cliente cliente) {
        return clienteService.criar(cliente);
    }

    @GetMapping
    public List<Cliente> listar() {
        return clienteService.listar();
    }

    @GetMapping("/{id}")
    public Cliente buscar(@PathVariable Long id) {
        return clienteService.buscar(id);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Long id, @Valid @RequestBody Cliente cliente) {
        return clienteService.atualizar(id, cliente);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        clienteService.excluir(id);
    }

    // Desafio extra
    @GetMapping("/{id}/pedidos")
    public List<PedidoResponse> listarPedidos(@PathVariable Long id) {
        return pedidoService.listarPorCliente(id);
    }
}
