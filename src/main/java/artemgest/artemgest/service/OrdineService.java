package artemgest.artemgest.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.DettaglioOrdine;
import artemgest.artemgest.model.Ordine;
import artemgest.artemgest.model.Prodotto;
import artemgest.artemgest.repository.ClienteRepository;
import artemgest.artemgest.repository.DettaglioOrdineRepository;
import artemgest.artemgest.repository.OrdineRepository;
import artemgest.artemgest.repository.ProdottoRepository;

@Service
public class OrdineService {

    private final DettaglioOrdineRepository dettaglioOrdineRepository;

    private final ClienteRepository clienteRepository;
    private final OrdineRepository ordineRepository;
    private final ProdottoRepository prodottoRepository;

    public OrdineService(OrdineRepository ordineRepository, ProdottoRepository prodottoRepository,
            ClienteRepository clienteRepository, DettaglioOrdineRepository dettaglioOrdineRepository) {
        this.ordineRepository = ordineRepository;
        this.prodottoRepository = prodottoRepository;
        this.clienteRepository = clienteRepository;
        this.dettaglioOrdineRepository = dettaglioOrdineRepository;
    }

    public Ordine nuovoOrdine() {
        Ordine ordine = new Ordine();
        ordine.setDettagli(new ArrayList<>());  // Inizializza la lista per evitare null
        return ordine;
    }

    public List<Prodotto> listaProdotti() {
        return prodottoRepository.findAll();
    }

    public Ordine salvaOrdine(Ordine ordine, Long idCliente) {
        ordine.setCliente(clienteRepository.findById(idCliente).orElseThrow());


        Ordine ordineSalvato = ordineRepository.save(ordine);

        for (DettaglioOrdine dettaglio : ordine.getDettagli()) {
            Prodotto prodotto = dettaglio.getProdotto();
            int disponibile = prodotto.getQuantitaDisponibile();
            int ordinata = dettaglio.getQuantita();

            if (ordinata > disponibile) {
                throw new IllegalArgumentException("Prodotto " + prodotto.getNome() + " ha solo " + disponibile + " unità disponibili.");
            }

            prodotto.setQuantitaDisponibile(disponibile - ordinata);
            prodottoRepository.save(prodotto);
        }

        return ordineSalvato;
    }

    public List<Ordine> listaOrdini(Cliente cliente) {
        return ordineRepository.findByCliente(cliente);
    }

    public Prodotto nuovoProdotto() {
        return new Prodotto();
    }

    public Ordine salvaOrdine(Ordine ordine){
        return ordineRepository.save(ordine);
    }

    public Prodotto salvaProdotto(Prodotto param) {
        return prodottoRepository.save(param);
    }

    public List<DettaglioOrdine> listaProdottoFattura(Long idOrdine) {
        return dettaglioOrdineRepository.findDettagliByOrdineId(idOrdine);
    }

    public Optional<Prodotto> cercaProdotto(Long idProdotto){
        return prodottoRepository.findById(idProdotto);
    }
}
