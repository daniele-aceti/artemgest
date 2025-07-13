package artemgest.artemgest.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import artemgest.artemgest.exception.ClienteNotFoundException;
import artemgest.artemgest.exception.FatturaNotFoundException;
import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.StatoIva;
import artemgest.artemgest.repository.ClienteRepository;
import artemgest.artemgest.repository.FatturaRepository;

@Service
public class FatturaService {

    private final ClienteRepository clienteRepository;

    private final FatturaRepository fatturaRepository;

    @Autowired
    public FatturaService(FatturaRepository fatturaRepository, ClienteRepository clienteRepository) {
        this.fatturaRepository = fatturaRepository;
        this.clienteRepository = clienteRepository;
    }

    public Fattura creaNuovaFattura(Fattura fattura, Long idCliente) {
        if (fattura.getStatoIva() == StatoIva.ESENTE) {
            fattura.setIva(BigDecimal.ZERO);
        } else if (fattura.getStatoIva() == StatoIva.RIDOTTA) {
            fattura.setIva(new BigDecimal("0.04"));
        } else {
            fattura.setIva(new BigDecimal("0.22"));
        }

        LocalDate dataDiOggi = LocalDate.now();
        fattura.setDataInizioFattura(dataDiOggi);
        fattura.setDataScadenzaFattura(fattura.getDataInizioFattura().plusDays(30));
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        if (cliente.isEmpty()) {
            throw new ClienteNotFoundException(idCliente);
        }
        fattura.setCliente(cliente.get());
        return fatturaRepository.save(fattura);
    }

    public List<Fattura> tutteFatture(String param) {
        if (param == null || param.isBlank()) {
            return fatturaRepository.findAll(); //TODO: INSERIRE UN LIMITE DEI CLIENTI CARICATI "TUTTI" RALLENTA
        }
        return fatturaRepository.findByCliente_RagioneSocialeContainingIgnoreCase(param);
    }

    public Fattura fattura(Long id) {
        Optional<Fattura> optFattura = fatturaRepository.findById(id);
        if (optFattura.isEmpty()) {
            throw new FatturaNotFoundException(id);
        }
        return optFattura.get();
    }

    public Fattura cambiaStatoFattura(Long id, Fattura formFattura) {
        Optional<Fattura> fattura = fatturaRepository.findById(id);
        if (fattura.isEmpty()) {
            throw new FatturaNotFoundException(id);
        }
        fattura.get().setStatoFattura(formFattura.getStatoFattura());
        return fatturaRepository.save(fattura.get());
    }

    public Optional<Fattura> cercaFatturaOrdine(Long idOrdine){
        return fatturaRepository.findFirstByOrdine_Id(idOrdine);
    }

}
