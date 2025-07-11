package artemgest.artemgest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import artemgest.artemgest.model.Cliente;
import artemgest.artemgest.model.Ordine;

public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    
    List<Ordine> findByCliente(Cliente cliente);
}
