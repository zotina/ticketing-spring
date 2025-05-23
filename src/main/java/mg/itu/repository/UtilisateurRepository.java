package mg.itu.repository;

import mg.itu.model.Utilisateur;


import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, String> {
    Utilisateur findByTelephone(String telephone);   
    Utilisateur findByIdUtilisateur(String idUtilisateur);
}