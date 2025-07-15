package artemgest.artemgest.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.StatoFattura;
import artemgest.artemgest.service.ClienteService;
import artemgest.artemgest.service.FatturaService;
import artemgest.artemgest.service.OrdineService;
import artemgest.artemgest.service.PdfFatturaService;

@WebMvcTest(FatturaController.class)
public class FatturaControllerTest {

    @Autowired
    private MockMvc mockmvc;

    @MockitoBean
    private OrdineService ordineService;

    @MockitoBean
    private FatturaService fatturaService;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private PdfFatturaService pdfFatturaService;

    @Test
    void tutteFattureTest() throws Exception {
        List<Fattura> fattureMock = new ArrayList<>();
        Fattura fattura = new Fattura();
        fattura.setDataInizioFattura(LocalDate.now().minusDays(40));
        fattureMock.add(fattura);

        Mockito.when(fatturaService.tutteFatture(Mockito.anyString())).thenReturn(fattureMock);

        mockmvc.perform(get("/fatture"))
                .andExpect(status().isOk())
                .andExpect(view().name("fatture"))
                .andExpect(model().attributeExists("listaFatture"));
    }

    @Test
    void dettaglioFatturaTest() throws Exception {
        Long id = 1L;
        Cliente clienteMock = new Cliente();
        clienteMock.setRagioneSociale("Prova");
        Fattura fatturaMock = new Fattura();
        fatturaMock.setId(id);
        fatturaMock.setCliente(clienteMock);
        fatturaMock.setImportoTotale(BigDecimal.valueOf(150.0));

        Mockito.when(fatturaService.fattura(id)).thenReturn(fatturaMock);

        mockmvc.perform(get("/dettaglioFattura/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("dettaglioFattura"))
                .andExpect(model().attributeExists("fattura"));
    }

    @Test
    void cambioStatoPostTest() throws Exception {
        Long id = 1L;

        Fattura fatturaMock = new Fattura();
        fatturaMock.setId(id);
        fatturaMock.setStatoFattura(StatoFattura.PAGATO);

        Mockito.when(fatturaService.cambiaStatoFattura(id, fatturaMock)).thenReturn(fatturaMock);
        mockmvc.perform(post("/cambioStato/{id}", id)
                .param("statoFattura", "PAGATO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/fatture"));
    }

    @Test
    void nuovaFatturaTest() throws Exception {
        Long idCliente = 1L;
        Long idOrdine = 2L;

        Cliente clienteMock = new Cliente();
        clienteMock.setId(idCliente);


        Mockito.when(clienteService.dettaglioCliente(idCliente)).thenReturn(clienteMock);
        mockmvc.perform(get("/nuovaFattura/{idCliente}/{idOrdine}", idCliente, idOrdine))
        .andExpect(status().isOk())
        .andExpect(view().name("formFattura"))
        .andExpect(model().attributeExists("idOrdine"))
         .andExpect(model().attributeExists("cliente"))
          .andExpect(model().attributeExists("nuovaFattura"));
    }

}
