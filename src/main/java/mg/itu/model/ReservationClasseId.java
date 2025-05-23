package mg.itu.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ReservationClasseId implements Serializable {

    private String idTypeSiege;
    private String idReservation;
    private String idClasse;

    
    public ReservationClasseId() {}

    public ReservationClasseId(String idTypeSiege, String idReservation, String idClasse) {
        this.idTypeSiege = idTypeSiege;
        this.idReservation = idReservation;
        this.idClasse = idClasse;
    }

    
    public String getIdTypeSiege() {
        return idTypeSiege;
    }

    public void setIdTypeSiege(String idTypeSiege) {
        this.idTypeSiege = idTypeSiege;
    }

    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }

    public String getIdClasse() {
        return idClasse;
    }

    public void setIdClasse(String idClasse) {
        this.idClasse = idClasse;
    }

    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReservationClasseId that = (ReservationClasseId) o;
        return Objects.equals(idTypeSiege, that.idTypeSiege) &&
               Objects.equals(idReservation, that.idReservation) &&
               Objects.equals(idClasse, that.idClasse);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTypeSiege, idReservation, idClasse);
    }
}