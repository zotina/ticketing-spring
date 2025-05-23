package mg.itu.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Table(name = "utilisateur")
public class Utilisateur implements UserDetails {

    @Id
    @Column(name = "id_utilisateur", nullable = false, length = 50)
    private String idUtilisateur;

    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String telephone;


    @Column(name = "mdp", length = 255)
    private String mpd;

    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role idRole;

    public Utilisateur() {}

    public Utilisateur(String idUtilisateur, String telephone, String mpd, Role idRole) {
        this.idUtilisateur = idUtilisateur;
        this.telephone = telephone;
        this.mpd = mpd;
        this.idRole = idRole;
    }

    public String getIdUtilisateur() { return idUtilisateur; }
    public void setIdUtilisateur(String idUtilisateur) { this.idUtilisateur = idUtilisateur; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public String getMpd() { return mpd; }
    public void setMpd(String mpd) { this.mpd = mpd; }

    public Role getIdRole() { return idRole; }
    public void setIdRole(Role idRole) { this.idRole = idRole; }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(() -> "ROLE_" + idRole.getLibelle()); 
    }

    @Override
    public String getPassword() {
        return mpd;
    }

    @Override
    public String getUsername() {
        return telephone;
    }   

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Utilisateur(String idUtilisateur, String telephone) {
        this.idUtilisateur = idUtilisateur;
        this.telephone = telephone;
    }

    
}