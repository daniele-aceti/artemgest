package artemgest.artemgest.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.StatoIva;
import artemgest.artemgest.repository.FatturaRepository;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceTest {

    @Mock
    private FatturaRepository fatturaRepository;

    @InjectMocks
    private FatturaService fatturaService;

    private Fattura fattura;

    @BeforeEach
    void fatturaSetUp() {
        this.fattura = new Fattura();
        this.fattura.setId(1L);
        this.fattura.setStatoIva(StatoIva.RIDOTTA);
    }


}
