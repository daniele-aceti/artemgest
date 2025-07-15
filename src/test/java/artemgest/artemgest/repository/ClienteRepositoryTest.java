package artemgest.artemgest.repository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import artemgest.artemgest.model.Cliente;

@DataJpaTest
@ActiveProfiles("test")
public class ClienteRepositoryTest {

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    void findByRagioneSociale() {
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

        List<Cliente> risultati = clienteRepository.findByRagioneSocialeContainingIgnoreCase("prova");

        assertThat(risultati).isNotEmpty();
        assertThat(risultati.get(0).getRagioneSociale()).isEqualTo("Prova");
    }

}
