package artemgest.artemgest.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import artemgest.artemgest.model.Prodotto;

public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {

}
