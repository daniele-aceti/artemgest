package artemgest.artemgest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.service.ClienteService;
import artemgest.artemgest.service.FatturaService;
import jakarta.validation.Valid;

@Controller
public class ClienteController {

    private final FatturaService fatturaService;

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService, FatturaService fatturaService) {
        this.clienteService = clienteService;
        this.fatturaService = fatturaService;
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping
    public String presentazione() {
        return "presentazione";
    }

    @GetMapping("/clienti")
    public String dashboard(@RequestParam(name = "keyword", required = false) String param, Model model) {
        model.addAttribute("listaClienti", clienteService.cercaCliente(param));
        return "clienti";
    }

    @GetMapping("/nuovoCliente")
    public String nuovoCliente(Model model) {
        model.addAttribute("nuovoCliente", new Cliente());
        return "formCliente";
    }

    @PostMapping("/nuovoCliente")
    public String nuovoClientePost(@Valid @ModelAttribute("nuovoCliente") Cliente clienteForm, Model model) {
        clienteService.creaNuovoCliente(clienteForm);
        return "redirect:/dettaglioCliente/" + clienteForm.getId();
    }

    @GetMapping("/nuovaFattura/{idCliente}")
    public String nuovaFattura(@PathVariable Long idCliente, Model model) {
        model.addAttribute("cliente", clienteService.dettaglioCLiente(idCliente));
        model.addAttribute("nuovaFattura", new Fattura());
        return "formFattura";
    }

    /* 
    @PostMapping("/nuovaFattura/{idCliente}")
    public String nuovaFatturaPost(@PathVariable("idCliente") Long idCliente, @Valid @ModelAttribute("nuovaFattura") Fattura formFattura,
            Model model) {
        fatturaService.creaNuovaFattura(formFattura, idCliente);
        return "redirect:/"; //TODO STAMPA FATTURA FILE DI STAMPA
    }
     */

    @GetMapping("/dettaglioCliente/{id}")
    public String dettaglioCliente(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.dettaglioCLiente(id));
        return "dettaglioCliente";
    }

}
