package artemgest.artemgest.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.StatoFattura;
import artemgest.artemgest.repository.FatturaRepository;

@Service
public class DashboardService {

    private final FatturaRepository fatturaRepository;

    @Autowired
    public DashboardService(FatturaRepository fatturaRepository) {
        this.fatturaRepository = fatturaRepository;
    }

    public Map<String, BigDecimal> filtraFatture() {

        Map<String, BigDecimal> mappaRisultatiDashboard = new HashMap<>();
        List<Fattura> tutte = fatturaRepository.findAll();

        //data dal primo fino all'ultimo giorno del mese
        LocalDate oggi = LocalDate.now();
        LocalDate primoDelMese = oggi.withDayOfMonth(1);
        LocalDate ultimoDelMese = oggi.withDayOfMonth(oggi.lengthOfMonth());

        //valori nella dashboard
        BigDecimal numeroFattureEmesse = BigDecimal.ZERO;
        BigDecimal totalePagato = BigDecimal.ZERO;
        BigDecimal numeroFattureScadute = BigDecimal.ZERO;
        BigDecimal numeroFattureInAttesa = BigDecimal.ZERO;
        BigDecimal fatturatoInAttesa = BigDecimal.ZERO;

        for (Fattura f : tutte) {
            numeroFattureEmesse = numeroFattureEmesse.add(BigDecimal.ONE); //numeroFattureEmesse++

            if (f.getDataInizioFattura() == null) {
                continue;
            }

            LocalDate data = f.getDataInizioFattura();
            StatoFattura stato = f.getStatoFattura();

            if (!data.isBefore(primoDelMese) && !data.isAfter(ultimoDelMese) && stato == StatoFattura.PAGATO) {
                totalePagato = totalePagato.add(f.getImportoTotale());
            }

            if (stato == StatoFattura.SCADUTO) {
                numeroFattureScadute = numeroFattureScadute.add(BigDecimal.ONE);
            }

            if (stato == StatoFattura.IN_ATTESA) {
                numeroFattureInAttesa = numeroFattureInAttesa.add(BigDecimal.ONE);
                fatturatoInAttesa = fatturatoInAttesa.add(f.getImportoTotale());
            }
        }

        mappaRisultatiDashboard.put("numeroFattureEmesse", numeroFattureEmesse);
        mappaRisultatiDashboard.put("totale", totalePagato);
        mappaRisultatiDashboard.put("numeroFattureScadute", numeroFattureScadute);
        mappaRisultatiDashboard.put("numeroFattureInAttesa", numeroFattureInAttesa);
        mappaRisultatiDashboard.put("fatturatoInAttesa", fatturatoInAttesa);

        return mappaRisultatiDashboard;
    }

}
