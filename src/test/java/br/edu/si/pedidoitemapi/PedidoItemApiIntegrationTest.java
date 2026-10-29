package br.edu.si.pedidoitemapi;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
class PedidoItemApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void resetDatabase() {
        jdbcTemplate.execute("DELETE FROM pedido_item");
        jdbcTemplate.execute("DELETE FROM pedido");
        jdbcTemplate.execute("DELETE FROM item");
        jdbcTemplate.execute("DELETE FROM cliente");
    }

    @Test
    void shouldCreateClientItemAndOrderAndRespectOrderRelationships() throws Exception {
        String email = "maria-" + UUID.randomUUID() + "@email.com";
        long clienteId = extractId(mockMvc.perform(post("/api/v1/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"nome\":\"Maria Silva\",\"email\":\"" + email + "\",\"telefone\":\"27999999999\"}"))
                .andExpect(status().isCreated())
                .andReturn());

        long itemId = extractId(mockMvc.perform(post("/api/v1/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"descricao\":\"Teclado USB\",\"preco\":85.90,\"estoque\":30}"))
                .andExpect(status().isCreated())
                .andReturn());

        long pedidoId = extractId(mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\":" + clienteId + ",\"status\":\"ABERTO\"}"))
                .andExpect(status().isCreated())
                .andReturn());

        long pedidoItemId = extractId(mockMvc.perform(post("/api/v1/pedidos/{id}/itens", pedidoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":" + itemId + ",\"quantidade\":2}"))
                .andExpect(status().isCreated())
                .andReturn());

        mockMvc.perform(get("/api/v1/pedidos/{id}", pedidoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valorTotal", is(171.8)))
                .andExpect(jsonPath("$.itens[0].precoUnitario", is(85.9)))
                .andExpect(jsonPath("$.itens[0].quantidade", is(2)));

        mockMvc.perform(put("/api/v1/pedidos/{pedidoId}/itens/{pedidoItemId}", pedidoId, pedidoItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"quantidade\":3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidade", is(3)))
                .andExpect(jsonPath("$.subtotal", is(257.7)));

        mockMvc.perform(delete("/api/v1/pedidos/{id}", pedidoId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/pedidos/{id}/itens", pedidoId))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/v1/clientes/{id}", clienteId))
                .andExpect(status().isNoContent());

        mockMvc.perform(delete("/api/v1/itens/{id}", itemId))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldRejectInvalidRequests() throws Exception {
        mockMvc.perform(post("/api/v1/pedidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clienteId\":999,\"status\":\"ABERTO\"}"))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/v1/pedidos/999/itens")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"itemId\":1,\"quantidade\":0}"))
                .andExpect(status().isBadRequest());
    }

    private long extractId(MvcResult mvcResult) throws Exception {
        return objectMapper.readTree(mvcResult.getResponse().getContentAsString()).get("id").asLong();
    }
}
