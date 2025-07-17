package artemgest.artemgest.service;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import artemgest.artemgest.exception.ClienteNotFoundException;
import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.repository.ClienteRepository;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

    @BeforeEach
    void clienteSetUp() {
        this.cliente = new Cliente();
        this.cliente.setId(1L);
        this.cliente.setRagioneSociale("Test Cliente");
    }

    @Test
    void creaNuovoCliente_shouldReturnSavedCliente() {
        when(clienteRepository.save(cliente)).thenReturn(cliente);

        Cliente result = clienteService.creaNuovoCliente(cliente);

        assertThat(result)
                .isNotNull()
                .extracting("id", "ragioneSociale")
                .containsExactly(1L, "Test Cliente");
    }

    @Test
    void clienteTest() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.cliente(cliente.getId()).get();

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getRagioneSociale()).isEqualTo("Test Cliente");
    }

    @Test
    void listaCliente() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<Cliente> listaClienti = clienteService.listaCliente();
        assertThat(listaClienti)
                .isNotEmpty()
                .containsExactly(cliente);
    }

    @Test
    void cercaCliente() {
        when(clienteRepository.findByRagioneSocialeContainingIgnoreCase("Test Cliente")).thenReturn(List.of(cliente));

        List<Cliente> clientiTrovati = clienteService.cercaCliente("Test Cliente");

        assertThat(clientiTrovati)
                .isNotEmpty()
                .containsExactly(cliente);
    }

    @Test
    void dettaglioCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.dettaglioCliente(cliente.getId());

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(cliente.getId());
        assertThat(result.getRagioneSociale()).isEqualTo(cliente.getRagioneSociale());

        //eccezione
        assertThatThrownBy(() -> clienteService.dettaglioCliente(99L))
                .isInstanceOf(ClienteNotFoundException.class);
    }

}
