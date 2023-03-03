package backend.team.backend.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.team.backend.dto.ScheduleDTO;
import backend.team.backend.dto.WarehouseIDDTO;
import backend.team.backend.excepcion.ExcepcionBodegaNoExiste;
import backend.team.backend.excepcion.ExcepcionUsuarioNoExiste;
import backend.team.backend.models.ScheduleModel;
import backend.team.backend.repositories.UserRepository;
import backend.team.backend.repositories.WareHouseRepository;
import backend.team.backend.services.CodeService;
import backend.team.backend.services.ScheduleService;
import backend.team.backend.services.UserService;
import backend.team.backend.services.WareHouseService;

@RestController
@RequestMapping("/agendamiento")
public class ScheduleController {
    @Autowired
    private ScheduleService scheduleService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private WareHouseService wareHouseService;
    @Autowired
    private CodeService codeService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WareHouseRepository warehouseRepository;


    private ScheduleDTO converToDTO(ScheduleModel schedule){
        modelMapper.typeMap(ScheduleModel.class, ScheduleDTO.class).addMapping(src->src.getUser().getId(), ScheduleDTO::setUser);
        modelMapper.typeMap(ScheduleModel.class, ScheduleDTO.class).addMapping(src->src.getWarehouse().getId(), ScheduleDTO::setWarehouse);
        modelMapper.typeMap(ScheduleModel.class, ScheduleDTO.class).addMapping(src->src.getCode().getCode(), ScheduleDTO::setCode);
        ScheduleDTO converted=modelMapper.map(schedule, ScheduleDTO.class);


        return converted;
    }

    private ScheduleModel converToEntity(ScheduleDTO schedule){
        if(!userRepository.findById(schedule.getUser()).isPresent()){
            throw new ExcepcionUsuarioNoExiste("El usuario no existe");
        }
        if(!warehouseRepository.findById(schedule.getWarehouse()).isPresent()){
            throw new ExcepcionBodegaNoExiste("La bodega no existe");
        }
        ScheduleModel converted=modelMapper.map(schedule,ScheduleModel.class);
        converted.setUser(userService.getUserById(schedule.getUser()).get());
        converted.setWarehouse(wareHouseService.getWarehouseById(schedule.getWarehouse()).get());
        String code=codeService.createCode(schedule.getDate(),Long.valueOf(5)).getCode();
        converted.setCode(codeService.getByCode(code).get());
        return converted;
    }

    @GetMapping()
    public List<ScheduleDTO> list(){
        List<ScheduleModel> schedules=scheduleService.getSchedules();
        return schedules.stream().map(this::converToDTO).collect(Collectors.toList());
    }
    @PostMapping()
    public ResponseEntity<?> saveSchedule(@RequestBody ScheduleDTO schedule){
        ScheduleModel scheduleEntity=converToEntity(schedule);
        return scheduleService.saveSchedule(scheduleEntity);
    }
    @GetMapping(path="/{id}")
    public ScheduleDTO getScheduleById(@PathVariable("id") long id){
        return converToDTO(this.scheduleService.getScheduleById(id).get());
    }
    @PutMapping(path="/{id}")
    public String update(@PathVariable("id") long id, @RequestBody ScheduleDTO schedule){
        try {
            scheduleService.getScheduleById(id).get();
            schedule.setId(id);
            scheduleService.saveSchedule(converToEntity(schedule));
            return "Se ha actualizado el agendamiento exitosamente";
        } catch (Exception e) {
            return "No se ha podido actualizar el agendamiento";
        }
    }
    @DeleteMapping(path="/{id}")
    public ResponseEntity<?> deleteScheduleById(@PathVariable("id") long id){
        return this.scheduleService.deleteSchedule(id);
    }

    @PostMapping("/validar")
    public ResponseEntity<?> validateSchedule(@RequestBody WarehouseIDDTO warehouseDTO){
        return scheduleService.validateSchedule(warehouseDTO.getWarehouseId());
    }
}

