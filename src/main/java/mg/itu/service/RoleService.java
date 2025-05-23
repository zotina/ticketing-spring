package mg.itu.service;

import mg.itu.model.Role;
import mg.itu.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    public Role findByLibelle(String libelle) {
        return  roleRepository.findByLibelle("USER")
                .orElseThrow(() -> new RuntimeException("Rôle USER introuvable"));
    }
}
