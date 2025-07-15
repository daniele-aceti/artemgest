package artemgest.artemgest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import artemgest.artemgest.model.DettaglioOrdine;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.Ordine;
import artemgest.artemgest.model.Prodotto;
import artemgest.artemgest.service.ClienteService;
import artemgest.artemgest.service.FatturaService;
import artemgest.artemgest.service.OrdineService;

@WebMvcTest(OrdineController.class)
public class OrdineControllerTest {

    @Autowired
    private MockMvc mockmvc;

    @MockitoBean
    ClienteService clienteService;

    @MockitoBean
    OrdineService ordineService;

    @MockitoBean
    FatturaService fatturaService;

    @Test
    void creaOrdine() throws Exception {
        Long idCliente = 1L;
        List<Prodotto> listaProdottiMock = new ArrayList<>();

        // Mock cliente
        Cliente clienteMock = new Cliente();
        clienteMock.setId(idCliente);
        Mockito.when(clienteService.cliente(idCliente)).thenReturn(Optional.of(clienteMock));

        // Mock nuovo ordine
        Ordine ordineMock = new Ordine();
        Mockito.when(ordineService.nuovoOrdine()).thenReturn(ordineMock);

        Mockito.when(ordineService.listaProdotti()).thenReturn(listaProdottiMock);

        mockmvc.perform(get("/ordine/{idCliente}", idCliente))
                .andExpect(status().isOk())
                .andExpect(view().name("formOrdine"))
                .andExpect(model().attributeExists("prodotti"))
                .andExpect(model().attributeExists("ordine"));

    }

    @Test
    void creaOrdinePostTest() throws Exception {
        Long idCliente = 1L;
        Cliente cliente = new Cliente();
        cliente.setId(idCliente);
        Ordine ordineMock = new Ordine();
        ordineMock.setCliente(cliente);

        Mockito.doNothing().when(ordineService).salvaOrdine(ordineMock, idCliente);

        mockmvc.perform(post("/ordine/{idCliente}", idCliente))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/nuovaFattura/" + idCliente + "/" + ordineMock.getId()));
    }

    @Test
    void nuovoProdotto() throws Exception {
        Prodotto prodottoMock = new Prodotto();

        Mockito.when(ordineService.nuovoProdotto()).thenReturn(prodottoMock);

        mockmvc.perform(get("/prodotto"))
                .andExpect(status().isOk())
                .andExpect(view().name("formProdotto"))
                .andExpect(model().attributeExists("prodotto"));
    }

    @Test
    void prodotto() throws Exception {
        Prodotto prodottoMock = new Prodotto();

        Mockito.when(ordineService.salvaProdotto(prodottoMock)).thenReturn(prodottoMock);

        mockmvc.perform(post("/prodotto"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/magazzino"));
    }

    @Test
    void magazzino() throws Exception {
        List<Prodotto> listaProdottiMock = new ArrayList<>();

        Mockito.when(ordineService.listaProdotti()).thenReturn(listaProdottiMock);

        mockmvc.perform(get("/magazzino"))
                .andExpect(status().isOk())
                .andExpect(view().name("magazzino"))
                .andExpect(model().attributeExists("listaProdotti"));
    }

    @Test
    void magazzinoTest() throws Exception {
        Long idProdottoMock = 1L;
        Prodotto prodottoMock = new Prodotto();
        prodottoMock.setId(idProdottoMock);

        Mockito.when(ordineService.cercaProdotto(idProdottoMock)).thenReturn(Optional.of(prodottoMock));

        mockmvc.perform(post("/magazzino/{idProdotto}", idProdottoMock))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/magazzino"));
    }

    @Test
    void listaOrdini() throws Exception {
        List<Ordine> listaOrdiniMock = new ArrayList<>();
        Long idCliente = 1L;
        Cliente clienteMock = new Cliente();
        clienteMock.setId(idCliente);

        Mockito.when(ordineService.listaOrdini(clienteMock)).thenReturn(listaOrdiniMock);
        Mockito.when(clienteService.cliente(idCliente)).thenReturn(Optional.of(clienteMock));

        mockmvc.perform(get("/listaOrdini/{idCliente}", idCliente))
                .andExpect(status().isOk())
                .andExpect(view().name("listaOrdini"))
                .andExpect(model().attributeExists("listaOrdini"));
    }

    @Test
    void dettaglioOrdine() throws Exception {
        Long idOrdine = 1L;
        List<DettaglioOrdine> listaOrdiniMock = new ArrayList<>();
        Cliente cliente = new Cliente();
        cliente.setRagioneSociale("Nome Cliente");

        Fattura fatturaMock = new Fattura();
        fatturaMock.setCliente(cliente);

        Mockito.when(ordineService.listaProdottoFattura(idOrdine)).thenReturn(listaOrdiniMock);
        Mockito.when(fatturaService.cercaFatturaOrdine(idOrdine)).thenReturn(Optional.of(fatturaMock));

        mockmvc.perform(get("/dettaglioOrdine/{idOrdine}", idOrdine))
                .andExpect(status().isOk())
                .andExpect(view().name("dettaglioOrdine"))
                .andExpect(model().attributeExists("fattura"))
                .andExpect(model().attributeExists("listaDettagliOrdini"));
    }

}
