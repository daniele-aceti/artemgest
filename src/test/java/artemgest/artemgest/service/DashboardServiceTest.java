package artemgest.artemgest.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.StatoFattura;
import artemgest.artemgest.repository.FatturaRepository;

public class DashboardServiceTest {

    private FatturaRepository fatturaRepository;
    private DashboardService dashboardService;

    @BeforeEach
    void setUp() {
        fatturaRepository = mock(FatturaRepository.class);
        dashboardService = new DashboardService(fatturaRepository);
    }

    @Test
    void testFiltraFatture() {
        LocalDate now = LocalDate.now();
        LocalDate startOfMonth = now.withDayOfMonth(1);

        // Creo alcune fatture simulate
        Fattura pagatoFattura = new Fattura();
        pagatoFattura.setDataInizioFattura(startOfMonth.plusDays(1));
        pagatoFattura.setStatoFattura(StatoFattura.PAGATO);
        pagatoFattura.setImportoTotale(BigDecimal.valueOf(100));

        Fattura scadutoFattura = new Fattura();
        scadutoFattura.setDataInizioFattura(now.minusDays(10));
        scadutoFattura.setStatoFattura(StatoFattura.SCADUTO);
        scadutoFattura.setImportoTotale(BigDecimal.valueOf(200));

        Fattura inAttesaFattura = new Fattura();
        inAttesaFattura.setDataInizioFattura(now.minusDays(5));
        inAttesaFattura.setStatoFattura(StatoFattura.IN_ATTESA);
        inAttesaFattura.setImportoTotale(BigDecimal.valueOf(300));

        List<Fattura> fatture = Arrays.asList(pagatoFattura, scadutoFattura, inAttesaFattura);
        when(fatturaRepository.findAll()).thenReturn(fatture);

        Map<String, BigDecimal> result = dashboardService.filtraFatture("QUALSIASI"); // tipoFiltro non usato

        assertThat(result.get("numeroFattureEmesse")).isEqualByComparingTo(BigDecimal.valueOf(3));
        assertThat(result.get("totale")).isEqualByComparingTo(BigDecimal.valueOf(100));
        assertThat(result.get("numeroFattureScadute")).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(result.get("numeroFattureInAttesa")).isEqualByComparingTo(BigDecimal.ONE);
        assertThat(result.get("fatturatoInAttesa")).isEqualByComparingTo(BigDecimal.valueOf(300));

    }
}
