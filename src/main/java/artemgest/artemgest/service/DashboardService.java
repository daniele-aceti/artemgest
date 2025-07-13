package artemgest.artemgest.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    private List<Fattura> filtraFatture(String tipoFiltro) {
        List<Fattura> risultato = new ArrayList<>();
        List<Fattura> tutte = fatturaRepository.findAll();
        LocalDate oggi = LocalDate.now();
        LocalDate primoDelMese = oggi.withDayOfMonth(1);
        LocalDate ultimoDelMese = oggi.withDayOfMonth(oggi.lengthOfMonth());

        for (Fattura f : tutte) {
            boolean aggiungi = false;

            if (f.getDataInizioFattura() != null) {
                LocalDate data = f.getDataInizioFattura();

                if (tipoFiltro.equals("PAGATO")) {
                    if (!data.isBefore(primoDelMese) && !data.isAfter(ultimoDelMese) && f.getStatoFattura() == StatoFattura.PAGATO) {
                        aggiungi = true;
                    }
                }

                if (tipoFiltro.equals("SCADUTE") && f.getStatoFattura() == StatoFattura.SCADUTO) {
                    aggiungi = true;
                }

                if (tipoFiltro.equals("IN_ATTESA") && f.getStatoFattura() == StatoFattura.IN_ATTESA) {
                    aggiungi = true;
                }
            }

            if (aggiungi) {
                risultato.add(f);
            }
        }

        return risultato;
    }

    public double fattureDelMese(boolean importoONumero) {
        String pagato = "PAGATO";
        return importoONumero(importoONumero, pagato);
    }

    public int fattureScadute() {
        List<Fattura> scadute = filtraFatture("SCADUTE");
        return scadute.size();
    }

    public double fattureInAttesa(boolean importoONumero) {
        String inAttesa = "IN_ATTESA";
        return importoONumero(importoONumero, inAttesa);

    }

    public double importoONumero(boolean calcolaImporto, String tipoFiltro) {
        List<Fattura> fatture = filtraFatture(tipoFiltro);
        if (calcolaImporto) {
            BigDecimal totale = BigDecimal.ZERO;
            for (Fattura fattura : fatture) {
                if (fattura.getImportoTotale() != null) {
                    totale = totale.add(fattura.getImportoTotale());
                }
            }
            return totale.doubleValue();
        } else {
            return (double) fatture.size();
        }
    }

}
