package artemgest.artemgest.controller;


import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.service.ClienteService;
import artemgest.artemgest.service.FatturaService;
import artemgest.artemgest.service.OrdineService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock dei service utilizzati nel controller
    @MockBean
    private ClienteService clienteService;

    @MockBean
    private FatturaService fatturaService;

    @MockBean
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
}

