package artemgest.artemgest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.repository.ClienteRepository;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    public Cliente creaNuovoCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> cliente(Long id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> cercaCliente(String param) {
        if (param == null || param.isBlank()) {
            return clienteRepository.findAll(); //TODO: INSERIRE UN LIMITE DEI CLIENTI CARICATI "TUTTI" RALLENTA
        }
        return clienteRepository.findByRagioneSocialeContainingIgnoreCase(param);
    }

    public Cliente dettaglioCLiente(Long idCLiente){
       return clienteRepository.findById(idCLiente).get();
    }
}
