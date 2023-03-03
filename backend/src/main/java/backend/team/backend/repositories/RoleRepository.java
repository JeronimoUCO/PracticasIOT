package backend.team.backend.repositories;

import backend.team.backend.models.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleModel, Integer> {
    //RoleModel findbyNameRole(String name);
}
