package mg.itu.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import mg.itu.model.Role;

public interface RoleRepository extends JpaRepository<Role, String> {
    Optional<Role> findByLibelle(String libelle);
}

