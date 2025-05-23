package mg.itu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Type_siege")
public class TypeSiege {

    @Id
    @Column(name = "id_Type_siege")
    private String idTypeSiege;

    @Column(name = "libelle", nullable = false)
    private String libelle;

    
    public TypeSiege() {}

    public String getIdTypeSiege() {
        return idTypeSiege;
    }

    public void setIdTypeSiege(String idTypeSiege) {
        this.idTypeSiege = idTypeSiege;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}