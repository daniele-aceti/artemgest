package artemgest.artemgest.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.Ordine;

@DataJpaTest
@ActiveProfiles("test")
public class FatturaRepositoryTest {

    @Autowired
    private FatturaRepository fatturaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private OrdineRepository ordineRepository;

    @Test
    void findByOrdineECliente() {
        // Crea e salva il cliente
        Cliente cliente = new Cliente();
        cliente.setRagioneSociale("Prova");
        cliente.setpIvaCFiscale("12345678901");
        cliente.setEmail("prova@email.it");
        cliente.setTelefono("02030006606");
        cliente.setIndirizzo("via delle prove 1");
        cliente.setCitta("Dimare");
        cliente.setCap("000122");
        cliente.setProvincia("DM");
        clienteRepository.save(cliente);

        // Crea e salva un ordine
        Ordine ordine = new Ordine();
        ordine.setCliente(cliente);
        ordineRepository.save(ordine);

        // Crea e salva una fattura
        Fattura fattura = new Fattura();
        fattura.setCliente(cliente);
        fattura.setOrdine(ordine);
        fattura.setImportoTotale(BigDecimal.valueOf(100.0));
        fattura.setIva(BigDecimal.valueOf(22.0));
        fattura.setDataInizioFattura(LocalDate.now());
        fatturaRepository.save(fattura);

        // Test ricerca per ragione sociale
        List<Fattura> fattureByCliente = fatturaRepository.findByCliente_RagioneSocialeContainingIgnoreCase("prova");
        assertThat(fattureByCliente).isNotEmpty();
        assertThat(fattureByCliente.get(0).getCliente().getRagioneSociale()).isEqualTo("Prova");

        // Test ricerca per id ordine
        Optional<Fattura> optFattura = fatturaRepository.findFirstByOrdine_Id(ordine.getId());
        assertThat(optFattura).isPresent();
        assertThat(optFattura.get().getOrdine().getId()).isEqualTo(ordine.getId());
    }

}
