package mg.itu.repository;

import mg.itu.model.ReservationClasse;
import mg.itu.model.ReservationClasseId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationClasseRepository extends JpaRepository<ReservationClasse, ReservationClasseId> {

    @Query("SELECT COALESCE(SUM(rc.nombre), 0) FROM ReservationClasse rc WHERE rc.reservation.idReservation = :idReservation")
    Integer sumNombreByReservationId(String idReservation);
}