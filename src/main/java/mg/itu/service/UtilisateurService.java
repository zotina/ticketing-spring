package mg.itu.service;

import mg.itu.model.Utilisateur;
import mg.itu.repository.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {
 
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    public Utilisateur saveUtilisateur(Utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public Utilisateur findByTelephone(String telephone) {
        return utilisateurRepository.findByTelephone(telephone); 
    }

    public Utilisateur findByIdUtilisateur(String id) {
        return utilisateurRepository.findByIdUtilisateur(id); 
    }
    
}
