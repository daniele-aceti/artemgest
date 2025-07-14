package artemgest.artemgest.controller.api;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.StatoFattura;
import artemgest.artemgest.service.FatturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardRestController {

    private final FatturaService fatturaService;

    public DashboardRestController(FatturaService fatturaService) {
        this.fatturaService = fatturaService;
    }

    @Operation(summary = "Ottieni il fatturato mensile", description = "Calcola il totale delle fatture pagate nel mese corrente")
    @ApiResponse(responseCode = "200", description = "Totale fatturato del mese")
    @GetMapping("/fatturato")
    public ResponseEntity<BigDecimal> fatturatoMensile() {
        BigDecimal totale = BigDecimal.ZERO;
        List<Fattura> fatture = fatturaService.listaFatture();
        LocalDate oggi = LocalDate.now();
        LocalDate primoDelMese = oggi.withDayOfMonth(1);
        LocalDate ultimoDelMese = oggi.withDayOfMonth(oggi.lengthOfMonth());
        for (Fattura fattura : fatture) {
            if (fattura.getDataInizioFattura() != null) {
                LocalDate data = fattura.getDataInizioFattura();
                if (!data.isBefore(primoDelMese) && !data.isAfter(ultimoDelMese) && fattura.getStatoFattura() == StatoFattura.PAGATO) {
                    totale = totale.add(fattura.getImportoTotale());
                }
            }
        }
        return ResponseEntity.ok(totale);
    }

}
