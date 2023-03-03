package backend.team.backend.services;

import backend.team.backend.models.WareHouseModel;
import backend.team.backend.repositories.WareHouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class WareHouseService {

    @Autowired
    private WareHouseRepository wareHouseRepository;

    public WareHouseModel createWarehouse(WareHouseModel warehouseModel){
        return wareHouseRepository.save(warehouseModel);
    }

    public Optional<WareHouseModel> getWarehouseById(int id){
        return wareHouseRepository.findById(id);
    }
}
