package mg.itu.model;

import jakarta.persistence.*;
@Entity
@Table(name = "role")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_role", nullable = false, length = 50)
    private String idRole;

    @Column(name = "libelle", length = 50)
    private String libelle;

    public String getIdRole() { return idRole; }
    public void setIdRole(String idRole) { this.idRole = idRole; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public Role() {}

    public Role(String idRole, String libelle) {
        this.idRole = idRole;
        this.libelle = libelle;
    }

    public Role(String libelle) {
        this.libelle = libelle;
    }

}
