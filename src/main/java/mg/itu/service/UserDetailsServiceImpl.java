package mg.itu.service;

import mg.itu.model.Utilisateur;
import mg.itu.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.authority.AuthorityUtils;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;


     @Override
    public UserDetails loadUserByUsername(String telephone) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByTelephone(telephone);
        
        if (utilisateur == null) {
            throw new UsernameNotFoundException("Utilisateur non trouvé");
        }
        return new org.springframework.security.core.userdetails.User(
            utilisateur.getUsername(), 
            utilisateur.getPassword(),
            AuthorityUtils.createAuthorityList("ROLE_" + utilisateur.getIdRole().getLibelle().toUpperCase())
        );
    }
}
