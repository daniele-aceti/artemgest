package artemgest.artemgest.controller;

import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import artemgest.artemgest.model.Ordine;
import artemgest.artemgest.model.Prodotto;
import artemgest.artemgest.service.ClienteService;
import artemgest.artemgest.service.OrdineService;
import jakarta.validation.Valid;




@Controller
public class OrdineController {

    private final ClienteService clienteService;
    private final OrdineService ordineService;

    public OrdineController(OrdineService ordineService, ClienteService clienteService) {
        this.ordineService = ordineService;
        this.clienteService = clienteService;
    }

    @GetMapping("/ordine/{idCliente}")
    public String creaOrdine(@PathVariable Long idCliente, Model model) {
        Ordine ordine = ordineService.nuovoOrdine();
        ordine.setCliente(clienteService.cliente(idCliente).orElseThrow(() -> new IllegalArgumentException("Cliente non trovato")));
        model.addAttribute("prodotti", ordineService.listaProdotti());
        model.addAttribute("ordine", ordine);
        return "formOrdine";
    }

    @PostMapping("/ordine/{idCliente}")
    public String creaOrdinePost(@PathVariable Long idCliente,
            @Valid @ModelAttribute("ordine") Ordine formOrdine,
            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formOrdine";
        }
        ordineService.salvaOrdine(formOrdine, idCliente);
        return "redirect:/nuovaFattura/" + idCliente + "/" + formOrdine.getId();
    }

    @GetMapping("/prodotto")
    public String nuovoProdotto(Model model) {
        model.addAttribute("prodotto", ordineService.nuovoProdotto());
        return "formProdotto";
    }

    @PostMapping("/prodotto")
    public String postMethodName(@Valid @ModelAttribute("prodotto") Prodotto formProdotto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "formProdotto";
        }
        ordineService.salvaProdotto(formProdotto);
        return "redirect:/magazzino";
    }

    @GetMapping("/magazzino")
    public String magazzino(Model model) {
        model.addAttribute("listaProdotti", ordineService.listaProdotti());
        return "magazzino";
    }

    @PostMapping("/magazzino/{idProdotto}")
    public String getMethodName(@PathVariable Long idProdotto, @ModelAttribute Prodotto formProdotto, Model model) {
        Optional<Prodotto> prodotto = ordineService.cercaProdotto(idProdotto);
        prodotto.get().setQuantitaDisponibile(formProdotto.getQuantitaDisponibile());
        ordineService.salvaProdotto(prodotto.get());
        return "redirect:/magazzino";
    }
    
    

    @GetMapping("/listaOrdini/{idCliente}")
    public String listaOrdini(@PathVariable Long idCliente,Model model) {
        model.addAttribute("listaOrdini", ordineService.listaOrdini(clienteService.cliente(idCliente).get()));
        return "listaOrdini";
    }
    
}
