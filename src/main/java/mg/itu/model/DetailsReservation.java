package mg.itu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "DetailsReservation")
public class DetailsReservation {

    @Id
    @Column(name = "id_detailsReservation")
    private String idDetailsReservation;

    @Column(name = "nom")
    private String nom;

    @Column(name = "age")
    private Integer age;

    @Column(name = "passport")
    private String passport; 

    @ManyToOne
    @JoinColumn(name = "id_reservation", nullable = false)
    private Reservation reservation;

    
    public String getIdDetailsReservation() {
        return idDetailsReservation;
    }

    public void setIdDetailsReservation(String idDetailsReservation) {
        this.idDetailsReservation = idDetailsReservation;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Reservation getReservation() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }

    public String getId_reservation() {
        return reservation != null ? reservation.getIdReservation() : null;
    }

    public void setId_reservation(String idReservation) {
        if (this.reservation == null) {
            this.reservation = new Reservation();
        }
        this.reservation.setIdReservation(idReservation);
    }
}