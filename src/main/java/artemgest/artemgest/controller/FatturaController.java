package artemgest.artemgest.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.StatoFattura;
import artemgest.artemgest.service.ClienteService;
import artemgest.artemgest.service.FatturaService;
import artemgest.artemgest.service.OrdineService;
import artemgest.artemgest.service.PdfFatturaService;

@Controller
public class FatturaController {

    private final OrdineService ordineService;

    private final FatturaService fatturaService;

    private final ClienteService clienteService;

    private final PdfFatturaService pdfFatturaService;

    @Autowired
    public FatturaController(ClienteService clienteService, FatturaService fatturaService, PdfFatturaService pdfFatturaService, OrdineService ordineService) {
        this.clienteService = clienteService;
        this.fatturaService = fatturaService;
        this.pdfFatturaService = pdfFatturaService;
        this.ordineService = ordineService;
    }

    @PostMapping("/genera-fattura/{idCliente}/{idOrdine}")
    public ResponseEntity<byte[]> generaPdfFattura(
            @PathVariable Long idOrdine,
            @PathVariable Long idCliente,
            @ModelAttribute("nuovaFattura") Fattura formFattura
    ) throws IOException {
        formFattura.setOrdine(ordineService.cercaOrdine(idOrdine).get());
        formFattura.setImportoTotale(BigDecimal.ZERO);
        return pdfFatturaService.generaPdfFattura(idOrdine, idCliente, formFattura, true);
    }

    @PostMapping("/ristampa-fattura/{idCliente}/{idFattura}")
    public ResponseEntity<byte[]> ristampaPdfFattura(
            @PathVariable Long idCliente,
            @PathVariable Long idFattura) throws IOException {

        Fattura fattura = fatturaService.fattura(idFattura); // prendi la fattura dal DB

        if (fattura == null) {
            // gestione fattura non trovata
            return ResponseEntity.notFound().build();
        }

        Long idOrdine = fattura.getOrdine().getId(); // prendi l'id ordine dalla fattura

        // Chiamo il servizio pdf passando idOrdine, idCliente, fattura e false (non nuova)
        return pdfFatturaService.generaPdfFattura(idOrdine, idCliente, fattura, false);
    }

    @GetMapping("/fatture")
    public String tutteFatture(@RequestParam(name = "keyword", required = false) String param, Model model) {
        List<Fattura> listaFatture = fatturaService.tutteFatture(param);
        LocalDate oggi = LocalDate.now();

        for (Fattura fattura : listaFatture) {
            LocalDate dataScadenza = fattura.getDataInizioFattura().plusDays(30);
            // Se oggi è dopo la data di scadenza, la fattura è SCADUTA
            if (oggi.isAfter(dataScadenza)) {
                fattura.setStatoFattura(StatoFattura.SCADUTO);
            }
        }

        model.addAttribute("listaFatture", listaFatture);
        return "fatture";
    }

    @GetMapping("/dettaglioFattura/{id}")
    public String dettaglioFattur(@PathVariable Long id, Model model) {
        model.addAttribute("fattura", fatturaService.fattura(id));
        return "dettaglioFattura";
    }

    @PostMapping("/cambioStato/{id}")
    public String postMethodName(@PathVariable Long id, @ModelAttribute Fattura formFattura) {
        fatturaService.cambiaStatoFattura(id, formFattura);
        return "redirect:/fatture";
    }

}
