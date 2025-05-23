package mg.itu.service;

import mg.itu.model.Utilisateur;
import mg.itu.repository.RoleRepository;
import mg.itu.repository.UtilisateurRepository;
import mg.itu.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class AuthService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private HttpSession session; 

    public Authentication authenticateAdmin(String telephone, String password) {
        Utilisateur utilisateur = utilisateurRepository.findByTelephone(telephone);
        
        if (utilisateur == null || !utilisateur.getIdRole().getLibelle().equalsIgnoreCase("ADMIN")) {
            throw new RuntimeException("L'utilisateur n'a pas les droits administrateur");
        }

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(password, utilisateur.getPassword()) &&
            !password.equals(utilisateur.getPassword())) {
            throw new RuntimeException("Mot de passe incorrect"); 
        }

        
        String role = utilisateur.getIdRole().getLibelle();
        String jwtToken = jwtTokenProvider.generateToken(telephone, role);

        
        session.setAttribute("jwtToken", jwtToken);

        
        Authentication authentication = new UsernamePasswordAuthenticationToken(telephone, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }
}