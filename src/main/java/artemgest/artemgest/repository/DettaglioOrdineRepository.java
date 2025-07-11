package artemgest.artemgest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import artemgest.artemgest.model.DettaglioOrdine;

public interface DettaglioOrdineRepository extends JpaRepository<DettaglioOrdine, Long> {

    @Query("SELECT d FROM DettaglioOrdine d WHERE d.ordine.id = :idOrdine")
    List<DettaglioOrdine> findDettagliByOrdineId(@Param("idOrdine") Long idOrdine);

}
