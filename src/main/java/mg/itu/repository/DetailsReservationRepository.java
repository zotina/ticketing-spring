package mg.itu.repository;

import mg.itu.model.DetailsReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DetailsReservationRepository extends JpaRepository<DetailsReservation, String> {
    List<DetailsReservation> findByReservationIdReservation(String idReservation);
}