package mg.itu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Ville")
public class Ville {

    @Id
    @Column(name = "id_ville")
    private String idVille;

    @Column(name = "nom", nullable = false)
    private String nom;

    
    public Ville() {}

    public String getIdVille() {
        return idVille;
    }

    public void setIdVille(String idVille) {
        this.idVille = idVille;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}