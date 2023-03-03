package backend.team.backend.repositories;

import backend.team.backend.models.WareHouseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WareHouseRepository extends JpaRepository<WareHouseModel,Integer>{

}
