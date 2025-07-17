package artemgest.artemgest.repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Ordine;

@DataJpaTest
@ActiveProfiles("test")
class OrdineRepositoryTest {

    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void findByCliente() {

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

        List<Ordine> cercaCliente = ordineRepository.findByCliente(cliente);
        assertThat(cercaCliente).isNotEmpty();
        assertThat(cercaCliente.get(0).getCliente()).isEqualTo(ordine.getCliente());
    }

}
