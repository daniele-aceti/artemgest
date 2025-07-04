package artemgest.artemgest.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Fattura;
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
        fattura.setDataScadenzaFattura(fattura.getDataInizioFattura().plusDays(30));
        Optional<Cliente> cliente = clienteRepository.findById(idCliente);
        fattura.setCliente(cliente.get());
        return fatturaRepository.save(fattura);
    }

}
