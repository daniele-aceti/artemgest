package artemgest.artemgest.controller.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import artemgest.artemgest.model.Prodotto;
import artemgest.artemgest.service.OrdineService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/magazzino")
public class MagazzinoRestController {

    private final OrdineService ordineService;

    @Autowired
    public MagazzinoRestController(OrdineService ordineService) {
        this.ordineService = ordineService;
    }

    @Operation(summary = "Prodotti", description = "Ottieni solo i prodotti in giacenza")
    @ApiResponse(responseCode = "200", description = "Lista dei prodotti")
    @GetMapping("/prodotti")
    public ResponseEntity<List<Prodotto>> prodottiInGiacenza() {
        List<Prodotto> tuttiProdotti = ordineService.listaProdotti();
        List<Prodotto> prodottiDisponibili = new ArrayList<>();

        for (Prodotto prodotto : tuttiProdotti) {
            if (prodotto.getQuantitaDisponibile() > 0) {
                prodottiDisponibili.add(prodotto);
            }
        }

        return ResponseEntity.ok(prodottiDisponibili);
    }

    @Operation(summary = "Prodotto", description = "Ottieni solo il prodotto che cerchi")
    @ApiResponse(responseCode = "200", description = "Prodotto specifico")
    @GetMapping("/prodotti/{id}")
    public ResponseEntity<Prodotto> prdottoSpecifico(@PathVariable long id) {
        Optional<Prodotto> prodotto = ordineService.cercaProdotto(id);
        if (prodotto.isPresent()) {
            return ResponseEntity.ok(prodotto.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

}
