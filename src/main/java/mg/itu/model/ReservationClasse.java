package mg.itu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Reservation_classe")
public class ReservationClasse {

    @EmbeddedId
    private ReservationClasseId id;

    @Column(name = "nombre")
    private Integer nombre;

    @ManyToOne
    @MapsId("idTypeSiege")
    @JoinColumn(name = "id_Type_siege")
    private TypeSiege typeSiege;

    @ManyToOne
    @MapsId("idReservation")
    @JoinColumn(name = "id_reservation")
    private Reservation reservation;

    @ManyToOne
    @MapsId("idClasse")
    @JoinColumn(name = "id_classe")
    private Classe classe;

    
    public ReservationClasse() {}

    public ReservationClasseId getId() {
        return id;
    }

    public void setId(ReservationClasseId id) {
        this.id = id;
    }

    public Integer getNombre() {
        return nombre;
    }

    public void setNombre(Integer nombre) {
        this.nombre = nombre;
    }

    public TypeSiege getTypeSiege() {
        return typeSiege;
    }

    public void setTypeSiege(TypeSiege typeSiege) {
        this.typeSiege = typeSiege;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public Classe getClasse() {
        return classe;
    }

    public void setClasse(Classe classe) {
        this.classe = classe;
    }
}