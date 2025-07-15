package artemgest.artemgest.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.service.ClienteService;
import artemgest.artemgest.service.FatturaService;
import artemgest.artemgest.service.OrdineService;
import jakarta.validation.Valid;

@Controller
public class ClienteController {

    private final OrdineService ordineService;

    private final FatturaService fatturaService;

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService, FatturaService fatturaService, OrdineService ordineService) {
        this.clienteService = clienteService;
        this.fatturaService = fatturaService;
        this.ordineService = ordineService;
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
    public String nuovoClientePost(@Valid @ModelAttribute("nuovoCliente") Cliente clienteForm, BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return "formCliente";
        }
        clienteService.creaNuovoCliente(clienteForm);
        return "redirect:/dettaglioCliente/" + clienteForm.getId();
    }


    @GetMapping("/dettaglioCliente/{id}")
    public String dettaglioCliente(@PathVariable Long id, Model model) {
        model.addAttribute("cliente", clienteService.dettaglioCliente(id));
        return "dettaglioCliente";
    }

}
