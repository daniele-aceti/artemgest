package artemgest.artemgest.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.DettaglioOrdine;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.Ordine;
import artemgest.artemgest.model.Prodotto;
import artemgest.artemgest.model.StatoIva;
import artemgest.artemgest.repository.ClienteRepository;
import artemgest.artemgest.repository.DettaglioOrdineRepository;
import artemgest.artemgest.repository.OrdineRepository;
import artemgest.artemgest.repository.ProdottoRepository;

@ExtendWith(MockitoExtension.class)
public class OridneServiceTest {

    @Mock
    private OrdineRepository ordineRepository;

    @Mock
    private ProdottoRepository prodottoRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private DettaglioOrdineRepository dettaglioOrdineRepository;

    @InjectMocks
    private OrdineService ordineService;

    private Ordine ordine;
    private Fattura fattura;
    private Cliente cliente;
    private Prodotto prodotto;

    @BeforeEach
    void setUp() {
        this.ordine = new Ordine();
        this.ordine.setId(1L);

        this.cliente = new Cliente();
        this.cliente.setId(1L);
        this.cliente.setRagioneSociale("Test Cliente");

        this.fattura = new Fattura();
        this.fattura.setId(1L);
        this.fattura.setStatoIva(StatoIva.RIDOTTA);

        this.prodotto = new Prodotto();
        this.prodotto.setId(1L);
        this.prodotto.setQuantitaDisponibile(1);

    }

    @Test
    void listaProdotti() {
        when(prodottoRepository.findAll()).thenReturn(List.of(prodotto));

        List<Prodotto> listaProdotti = ordineService.listaProdotti();

        assertThat(listaProdotti).isNotEmpty();
        assertThat(listaProdotti).containsExactly(prodotto);
    }

    @Test
    void salvaOrdine() {
        DettaglioOrdine dettaglio = new DettaglioOrdine();
        ordine.setDettagli(List.of(dettaglio));

        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        ordineService.salvaOrdine(ordine, 1L);

        assertThat(ordine.getCliente()).isEqualTo(cliente);
        assertThat(ordine.getDettagli().get(0).getOrdine()).isEqualTo(ordine);
        verify(ordineRepository).save(ordine);
    }

    @Test
    void listaOrdini() {
        Cliente clienteTest = new Cliente();
        clienteTest.setId(1L);
        clienteTest.setRagioneSociale("Test Cliente");

        when(ordineRepository.findByCliente(clienteTest)).thenReturn(List.of(ordine));

        List<Ordine> ordini = ordineService.listaOrdini(clienteTest);

        assertThat(ordini).isNotEmpty();
        assertThat(ordini).containsExactly(ordine);
    }

    @Test
    void nuovoProdotto() {
        Prodotto nuovo = ordineService.nuovoProdotto();
        assertThat(nuovo).isNotNull();
    }

    @Test
    void salvaOrdineTest() {
        when(ordineRepository.save(ordine)).thenReturn(ordine);

        Ordine salvato = ordineService.salvaOrdine(ordine);

        assertThat(salvato).isNotNull();
        assertThat(salvato).isEqualTo(ordine);
    }

    @Test
    void salvaProdotto() {
        Prodotto prodotto = new Prodotto();
        prodotto.setId(1L);

        when(prodottoRepository.save(prodotto)).thenReturn(prodotto);

        Prodotto salvato = ordineService.salvaProdotto(prodotto);

        assertThat(salvato).isNotNull();
        assertThat(salvato.getId()).isEqualTo(1L);
    }

    @Test
    void listaProdottoFattura() {
        DettaglioOrdine dettaglio = new DettaglioOrdine();
        dettaglio.setId(1L);

        when(dettaglioOrdineRepository.findDettagliByOrdineId(1L)).thenReturn(List.of(dettaglio));

        List<DettaglioOrdine> dettagli = ordineService.listaProdottoFattura(1L);

        assertThat(dettagli).isNotEmpty();
        assertThat(dettagli).containsExactly(dettaglio);
    }

    @Test
    void cercaProdotto() {
        Prodotto prodotto = new Prodotto();
        prodotto.setId(1L);

        when(prodottoRepository.findById(1L)).thenReturn(Optional.of(prodotto));

        Optional<Prodotto> result = ordineService.cercaProdotto(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(prodotto);
    }

    @Test
    void cercaOrdine() {
        when(ordineRepository.findById(1L)).thenReturn(Optional.of(ordine));

        Optional<Ordine> result = ordineService.cercaOrdine(1L);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(ordine);
    }

}
