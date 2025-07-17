package artemgest.artemgest.controller;

import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.service.ClienteService;
import artemgest.artemgest.service.FatturaService;
import artemgest.artemgest.service.OrdineService;

@WebMvcTest(ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock dei service utilizzati nel controller
    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private FatturaService fatturaService;

    @MockitoBean
    private OrdineService ordineService;

    @Test
    void testPresentazione() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("presentazione"));
    }

    @Test
    void testDashboard() throws Exception {
        Mockito.when(clienteService.cercaCliente(Mockito.anyString()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/clienti"))
                .andExpect(status().isOk())
                .andExpect(view().name("clienti"))
                .andExpect(model().attributeExists("listaClienti"));
    }

    @Test
    void testNuovoCliente() throws Exception {
        mockMvc.perform(get("/nuovoCliente"))
                .andExpect(status().isOk())
                .andExpect(view().name("formCliente"))
                .andExpect(model().attributeExists("nuovoCliente"));
    }

    @Test
    void dettaglioCliente() throws Exception {
        Long idCliente = 1L;

        Cliente clienteMock = new Cliente();
        clienteMock.setId(idCliente);

        Mockito.when(clienteService.dettaglioCliente(idCliente)).thenReturn(clienteMock);
        mockMvc.perform(get("/dettaglioCliente/{id}", idCliente))
                .andExpect(status().isOk())
                .andExpect(view().name("dettaglioCliente"))
                .andExpect(model().attributeExists("cliente"));
    }
}
