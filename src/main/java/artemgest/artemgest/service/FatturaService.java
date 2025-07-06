package artemgest.artemgest.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Fattura;
import artemgest.artemgest.model.StatoFattura;
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
        if (fattura.getStatoIva().equals("ESENTE")) {
            fattura.setIva(0.0);
        } else if (fattura.getStatoIva().equals("RIDOTTA")) {
            fattura.setIva(0.4);
        } else {
            fattura.setIva(0.22);
        }
        LocalDate dataDiOggi = LocalDate.now();
        fattura.setDataInizioFattura(dataDiOggi);
        fattura.setDataScadenzaFattura(fattura.getDataInizioFattura().plusDays(30));
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        fattura.setCliente(cliente.get());
        fattura.setStatoFattura(StatoFattura.IN_ATTESA);
        return fatturaRepository.save(fattura);
    }

    public List<Fattura> tutteFatture(String param) {
        if (param == null || param.isBlank()) {
            return fatturaRepository.findAll(); //TODO: INSERIRE UN LIMITE DEI CLIENTI CARICATI "TUTTI" RALLENTA
        }
        return fatturaRepository.findByCliente_RagioneSocialeContainingIgnoreCase(param);
    }

    public Fattura fattura(Long id) {
        return fatturaRepository.findById(id).get();
    }

    public Fattura cambiaStatoFattura(Long id, Fattura formFattura){
        Fattura fattura = fatturaRepository.findById(id).get();
        fattura.setStatoFattura(formFattura.getStatoFattura());
        return fatturaRepository.save(fattura);
    }

}
