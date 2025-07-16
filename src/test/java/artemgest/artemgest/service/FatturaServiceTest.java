package artemgest.artemgest.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import artemgest.artemgest.exception.ClienteNotFoundException;
import artemgest.artemgest.exception.FatturaNotFoundException;
import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.StatoFattura;
import artemgest.artemgest.model.StatoIva;
import artemgest.artemgest.repository.ClienteRepository;
import artemgest.artemgest.repository.FatturaRepository;


@ExtendWith(MockitoExtension.class)
class FatturaServiceTest {

    @Mock
    private FatturaRepository fatturaRepository;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private FatturaService fatturaService;


    private Cliente cliente;
    private Fattura fattura;

    @BeforeEach
    void setUp() {
        this.cliente = new Cliente();
        this.cliente.setId(1L);
        this.cliente.setRagioneSociale("Test Cliente");

        this.fattura = new Fattura();
        this.fattura.setId(1L);
        this.fattura.setStatoIva(StatoIva.RIDOTTA);
    }


    @Test
    void creaNuovaFatturaSuccess() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(fatturaRepository.save(any(Fattura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Fattura nuovaFattura = fatturaService.creaNuovaFattura(fattura, 1L);

        assertThat(nuovaFattura.getIva()).isEqualTo(new BigDecimal("0.04"));
        assertThat(nuovaFattura.getCliente()).isEqualTo(cliente);
        assertThat(nuovaFattura.getDataInizioFattura()).isEqualTo(LocalDate.now());
        assertThat(nuovaFattura.getDataScadenzaFattura()).isEqualTo(LocalDate.now().plusDays(30));
    }


    @Test
    void creaNuovaFatturaClienteNotFound() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fatturaService.creaNuovaFattura(fattura, 99L))
                .isInstanceOf(ClienteNotFoundException.class);
    }


    @Test
    void tutteFattureConParametro() {
        when(fatturaRepository.findByCliente_RagioneSocialeContainingIgnoreCase("Test"))
                .thenReturn(List.of(fattura));

        List<Fattura> risultato = fatturaService.tutteFatture("Test");

        assertThat(risultato).containsExactly(fattura);
    }

    @Test
    void tutteFattureSenzaParametro() {
        when(fatturaRepository.findAll()).thenReturn(List.of(fattura));

        List<Fattura> risultato = fatturaService.tutteFatture(null);

        assertThat(risultato).containsExactly(fattura);
    }


    @Test
    void fatturaEsistente() {
        when(fatturaRepository.findById(1L)).thenReturn(Optional.of(fattura));

        Fattura risultato = fatturaService.fattura(1L);

        assertThat(risultato).isEqualTo(fattura);
    }

    @Test
    void fatturaNotFound() {
        when(fatturaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> fatturaService.fattura(1L))
                .isInstanceOf(FatturaNotFoundException.class);
    }


    @Test
    void cambiaStatoFatturaSuccess() {
        Fattura formFattura = new Fattura();
        formFattura.setStatoFattura(StatoFattura.PAGATO);

        when(fatturaRepository.findById(1L)).thenReturn(Optional.of(fattura));
        when(fatturaRepository.save(any(Fattura.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Fattura aggiornata = fatturaService.cambiaStatoFattura(1L, formFattura);

        assertThat(aggiornata.getStatoFattura()).isEqualTo(StatoFattura.PAGATO);
    }

    @Test
    void cambiaStatoFatturaNotFound() {
        when(fatturaRepository.findById(99L)).thenReturn(Optional.empty());

        Fattura formFattura = new Fattura();
        formFattura.setStatoFattura(StatoFattura.PAGATO);

        assertThatThrownBy(() -> fatturaService.cambiaStatoFattura(99L, formFattura))
                .isInstanceOf(FatturaNotFoundException.class);
    }


    @Test
    void cercaFatturaOrdineSuccess() {
        when(fatturaRepository.findFirstByOrdine_Id(1L)).thenReturn(Optional.of(fattura));

        Optional<Fattura> result = fatturaService.cercaFatturaOrdine(1L);

        assertThat(result).isPresent().contains(fattura);
    }


    @Test
    void listaFatture() {
        when(fatturaRepository.findAll()).thenReturn(Arrays.asList(fattura));

        List<Fattura> lista = fatturaService.listaFatture();

        assertThat(lista).hasSize(1).containsExactly(fattura);
    }

}
