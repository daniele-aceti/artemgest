package artemgest.artemgest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import artemgest.artemgest.model.Fattura;

public interface FatturaRepository extends JpaRepository<Fattura, Long> {

        List<Fattura> findByCliente_RagioneSocialeContainingIgnoreCase(String ragioneSociale);

}
