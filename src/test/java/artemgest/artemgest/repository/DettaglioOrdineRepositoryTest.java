package artemgest.artemgest.repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.DettaglioOrdine;
import artemgest.artemgest.model.Ordine;
import artemgest.artemgest.model.Prodotto;

@DataJpaTest
@ActiveProfiles("test")
class DettaglioOrdineRepositoryTest {

    @Autowired
    private DettaglioOrdineRepository dettaglioOrdineRepository;

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Test
    void nativeFindDettaglioOrdineId() {
        // Creo e salvo il cliente
        Cliente cliente = new Cliente();
        cliente.setRagioneSociale("Prova Cliente");
        cliente.setpIvaCFiscale("12345678901");
        cliente.setEmail("prova@email.it");
        cliente.setTelefono("02030006606");
        cliente.setIndirizzo("via delle prove 1");
        cliente.setCitta("Dimare");
        cliente.setCap("000122");
        cliente.setProvincia("DM");
        clienteRepository.save(cliente);

        // Creo e salvo l'ordine associato al cliente
        Ordine ordine = new Ordine();
        ordine.setCliente(cliente);
        ordineRepository.save(ordine);

        // Creo e salvo un prodotto
        Prodotto prodotto = new Prodotto();
        prodotto.setNome("Prodotto Test");
        prodotto.setUpc("UPCTEST123");
        prodotto.setPrezzo(10.0);
        prodottoRepository.save(prodotto);

        // Creo e salvo il dettaglio ordine associato all'ordine e al prodotto
        DettaglioOrdine dettaglio = new DettaglioOrdine();
        dettaglio.setOrdine(ordine);
        dettaglio.setProdotto(prodotto);
        dettaglio.setQuantita(5);
        dettaglioOrdineRepository.save(dettaglio);

        // Test
        List<DettaglioOrdine> listaDettaglioOrdine = dettaglioOrdineRepository.findDettagliByOrdineId(ordine.getId());

        assertThat(listaDettaglioOrdine).isNotEmpty();
        assertThat(listaDettaglioOrdine.get(0).getOrdine().getId()).isEqualTo(ordine.getId());
        assertThat(listaDettaglioOrdine.get(0).getProdotto().getId()).isEqualTo(prodotto.getId());
    }

}
