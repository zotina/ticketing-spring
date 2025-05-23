package mg.itu.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Vol")
public class Vol {

    @Id
    @Column(name = "id_vol")
    private String idVol;

    @Column(name = "date_vol", nullable = false)
    private LocalDateTime dateVol;

    @Column(name = "enPromotion")
    private Boolean enPromotion;

    @ManyToOne
    @JoinColumn(name = "id_ville", nullable = false)
    private Ville villeDepart;

    @ManyToOne
    @JoinColumn(name = "id_ville_1", nullable = false)
    private Ville villeArrivee;

    public Vol() {}

    public String getIdVol() {
        return idVol;
    }

    public void setIdVol(String idVol) {
        this.idVol = idVol;
    }

    public LocalDateTime getDateVol() {
        return dateVol;
    }

    public void setDateVol(LocalDateTime dateVol) {
        this.dateVol = dateVol;
    }

    public Boolean getEnPromotion() {
        return enPromotion;
    }

    public void setEnPromotion(Boolean enPromotion) {
        this.enPromotion = enPromotion;
    }

    public Ville getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(Ville villeDepart) {
        this.villeDepart = villeDepart;
    }

    public Ville getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(Ville villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

}