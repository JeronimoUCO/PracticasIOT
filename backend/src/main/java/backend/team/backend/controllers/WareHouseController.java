package backend.team.backend.controllers;


import backend.team.backend.dto.WarehouseDTO;
import backend.team.backend.models.WareHouseModel;
import backend.team.backend.services.WareHouseService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bodega")
public class WareHouseController {
    @Autowired
    private WareHouseService warehouseService;
    @Autowired
    private ModelMapper modelMapper;

    private WarehouseDTO converToDTO(WareHouseModel wareHouse){
        WarehouseDTO converted=modelMapper.map(wareHouse, WarehouseDTO.class);
        return converted;
    }

    private WareHouseModel converToEntity(WarehouseDTO wareHouse){
        WareHouseModel converted=modelMapper.map(wareHouse,WareHouseModel.class);
        return converted;
    }

    @PostMapping()
    public WareHouseModel createWarehouse(@RequestBody WarehouseDTO warehouseDTO){
        return warehouseService.createWarehouse(converToEntity(warehouseDTO));
    }

}
