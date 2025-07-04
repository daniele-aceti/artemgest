package artemgest.artemgest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import artemgest.artemgest.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findByRagioneSocialeContainingIgnoreCase(String ragioneSociale);

}
