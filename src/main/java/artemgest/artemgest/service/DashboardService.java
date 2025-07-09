package artemgest.artemgest.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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

    public double importoONumero(boolean importoONumero, String tipoFiltro) {
        List<Fattura> fatture = filtraFatture(tipoFiltro);
        BigDecimal totale = BigDecimal.ZERO;
        if (importoONumero) {
            for (Fattura fattura : fatture) {
                BigDecimal importo = BigDecimal.valueOf(fattura.getImporto());
                BigDecimal iva = fattura.getIva().add(BigDecimal.ONE);
                totale = totale.add(importo.multiply(iva).setScale(2, RoundingMode.HALF_UP));
            }
            return totale.doubleValue();
        }
        return fatture.size();
    }

}

/* 
 * 
 * package artemgest.artemgest.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

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

    public List<Fattura> listaImporti(Model model) {
        BigDecimal importoTotale = BigDecimal.ZERO;
        BigDecimal importoInAttesa = BigDecimal.ZERO;
        int numeroFattureMese = 0;
        int fattureScadute = 0;
        int fattureInAttesa = 0;

        List<Fattura> fatture = fatturaRepository.findAll();
        LocalDate oggi = LocalDate.now();
        LocalDate primoGiornoDelMese = oggi.withDayOfMonth(1);
        LocalDate ultimoGiornoDelMese = oggi.withDayOfMonth(oggi.lengthOfMonth());

        for (Fattura fattura : fatture) {
            LocalDate data = fattura.getDataInizioFattura();
            if (data != null) {
                BigDecimal importo = BigDecimal.valueOf(fattura.getImporto());
                BigDecimal totale = importo.multiply(fattura.getIva()).add(importo)
                        .setScale(2, RoundingMode.HALF_UP);

                // Verifica se la data è nel mese corrente
                if (!data.isBefore(primoGiornoDelMese) && !data.isAfter(ultimoGiornoDelMese)) {
                    numeroFattureMese++;
                    if (fattura.getStatoFattura() == StatoFattura.PAGATO) {
                        importoTotale = importoTotale.add(totale);
                    }
                }

                if (fattura.getStatoFattura() == StatoFattura.SCADUTO) {
                    fattureScadute++;
                }

                if (fattura.getStatoFattura() == StatoFattura.IN_ATTESA) {
                    importoInAttesa = importoInAttesa.add(totale);
                    fattureInAttesa++;
                }
            }
        }

        model.addAttribute("fatturatoMese", importoTotale.setScale(2, RoundingMode.HALF_UP));
        model.addAttribute("fatturatoMeseInAttesa", importoInAttesa.setScale(2, RoundingMode.HALF_UP));
        model.addAttribute("numeroFattureMese", numeroFattureMese);
        model.addAttribute("numeroFattureScaduteMese", fattureScadute);
        model.addAttribute("numeroFattureInAttesaMese", fattureInAttesa);

        return fatture;
    }


}
 */
