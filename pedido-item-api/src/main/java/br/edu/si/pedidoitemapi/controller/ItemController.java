package br.edu.si.pedidoitemapi.controller;

import br.edu.si.pedidoitemapi.entity.Item;
import br.edu.si.pedidoitemapi.service.ItemService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/itens")
@Tag(name = "Itens")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Item criar(@Valid @RequestBody Item item) {
        return itemService.criar(item);
    }

    @GetMapping
    public List<Item> listar() {
        return itemService.listar();
    }

    @GetMapping("/{id}")
    public Item buscar(@PathVariable Long id) {
        return itemService.buscar(id);
    }

    @PutMapping("/{id}")
    public Item atualizar(@PathVariable Long id, @Valid @RequestBody Item item) {
        return itemService.atualizar(id, item);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        itemService.excluir(id);
    }
}
