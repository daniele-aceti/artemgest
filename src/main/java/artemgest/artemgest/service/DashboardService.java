package artemgest.artemgest.service;

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
        Double importoTotale = 0.00;
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
                // Verifica se la data è nel mese corrente
                if (!data.isBefore(primoGiornoDelMese) && !data.isAfter(ultimoGiornoDelMese)) {
                    numeroFattureMese++;
                    if (fattura.getStatoFattura() == StatoFattura.PAGATO) {
                        importoTotale += ((fattura.getImporto() * fattura.getIva()) + fattura.getImporto());
                    }
                    if(fattura.getStatoFattura() == StatoFattura.SCADUTO){
                        fattureScadute++;
                    }
                    if(fattura.getStatoFattura() == StatoFattura.IN_ATTESA){
                        fattureInAttesa++;
                    }
                }
            }

        }
        model.addAttribute("fatturatoMese", importoTotale);
        model.addAttribute("numeroFattureMese", numeroFattureMese);
        model.addAttribute("numeroFattureScaduteMese", fattureScadute);
        model.addAttribute("numeroFattureInAttesaMese", fattureInAttesa);
        return fatture;
    }
}
