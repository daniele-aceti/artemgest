package artemgest.artemgest.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import artemgest.artemgest.model.Fattura;

public interface FatturaRepository extends JpaRepository<Fattura, Long> {

    List<Fattura> findByCliente_RagioneSocialeContainingIgnoreCase(String ragioneSociale);

    Optional<Fattura> findFirstByOrdine_Id(Long ordineId);

}
