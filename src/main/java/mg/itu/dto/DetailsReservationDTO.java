package mg.itu.dto;

public class DetailsReservationDTO {

    private String idDetailsReservation;
    private String nom;
    private Integer age;
    private String passport; 
    private String idReservation;

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

    public String getIdReservation() {
        return idReservation;
    }

    public void setIdReservation(String idReservation) {
        this.idReservation = idReservation;
    }
}