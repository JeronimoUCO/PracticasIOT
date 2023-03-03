package backend.team.backend.services;



import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import backend.team.backend.dto.ScheduleDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import backend.team.backend.client.web.WebServiceClient;
import backend.team.backend.models.ScheduleModel;
import backend.team.backend.models.UserModel;
import backend.team.backend.models.WareHouseModel;
import backend.team.backend.repositories.ScheduleRepository;
import backend.team.backend.repositories.UserRepository;
import backend.team.backend.repositories.WareHouseRepository;
import backend.team.backend.transversal.messages.Messages;
import backend.team.backend.transversal.response.rest.Response;
import backend.team.backend.transversal.utilitaries.UtilObject;
import backend.team.backend.transversal.utilitaries.UtilText;







@Service
@Transactional
public class ScheduleService{
    @Autowired
    private ScheduleRepository scheduleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private WareHouseRepository warehouseRepository;
    @Autowired
    private ModelMapper modelMapper;

    public List<ScheduleModel> getSchedules(){
        return scheduleRepository.findAll();
    }

    public ResponseEntity<?> saveSchedule(ScheduleModel schedule){
        ResponseEntity<?> respuestaSolicitud = null;
        Response<ScheduleModel> response = new Response<>();
        boolean datosValidos = true;

        //validar que el objeto no sea nulo
        if(UtilObject.isNull(schedule)){
            response.agregarMensaje(Messages.ERROR_OBJECT_AGENDA_VACIA);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        //Validar información del tipo de agendamiento.
        if(UtilObject.isNull(schedule.getType())){
            response.agregarMensaje(Messages.ERROR_SCHEDULE_TYPE_NULL);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;

        }else if(UtilText.isEmpty(schedule.getType())){
            response.agregarMensaje(Messages.ERROR_SCHEDULE_TYPE_EMPTY);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        //Validar informacion de la bodega donde se hace el agendamiento
        if(UtilObject.isNull(schedule.getWarehouse())){
            response.agregarMensaje(Messages.ERROR_WAREHOUSE_NULL);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(!validateWareHouseExist(schedule.getWarehouse().getId())){
            response.agregarMensaje(Messages.ERROR_WAREHOUSE_DOES_NOT_EXIST);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        //validar informacion de la descripcion
        if(UtilObject.isNull(schedule.getDescription())){
            response.agregarMensaje(Messages.ERROR_SCHEDULE_DESCRIPTION_NULL);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(UtilText.isEmpty(schedule.getDescription())){
            response.agregarMensaje(Messages.ERROR_SCHEDULE_DESCRIPTION_EMPTY);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(UtilText.applyTrim(schedule.getDescription()).length()<3){
            response.agregarMensaje(Messages.ERROR_SCHEDULE_DESCRIPTION_LENGHT);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        //Validar informacion del usuario
        if(UtilObject.isNull(schedule.getUser())){
            response.agregarMensaje(Messages.ERROR_SCHEDULE_USER_NULL);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(validateUserExist(schedule.getUser().getId()) == false){
            response.agregarMensaje(Messages.ERROR_SCHEDULE_USER_DOES_NOT_EXIST);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        //Validar hora y fecha de agendamiento
        if(UtilObject.isNull(schedule.getDate())){
            response.agregarMensaje(Messages.ERROR_SCHEDULE_DATE_NULL);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(!respectsBounds(schedule.getDate())){
            response.agregarMensaje(Messages.ERROR_SCHEDULE_DATE_NOT_AVAILABLE);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        if(datosValidos){
            ScheduleDTO scheduleDTO = converToDTO(scheduleRepository.save(schedule));
            respuestaSolicitud= new ResponseEntity<>(scheduleDTO, HttpStatus.OK);
        }
        return respuestaSolicitud;

    }

    public Optional<ScheduleModel> getScheduleById(long id){
        return scheduleRepository.findById(id);
    }

    public ResponseEntity<?> deleteSchedule(long id){
        ResponseEntity<?> deleteResponse = null;
        Response<ScheduleModel> response=new Response<>();
        try {
            scheduleRepository.deleteById(id);
            response.agregarMensaje(Messages.SCHEDULE_SUCCESSFULLY_UPDATED);
            deleteResponse=new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            response.agregarMensaje(Messages.ERROR_WAREHOUSE_DOES_NOT_EXIST);
            deleteResponse=new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return deleteResponse;
    }

    public ResponseEntity<?> validateSchedule(Integer warehouseId){
        ScheduleModel activeSchedule = null;
        ResponseEntity<?> respuestaSolicitud = null;
        Response<ScheduleModel> response = new Response<>();
        boolean datosValidos = true;
        //validar id de la bodega
        if(UtilObject.isNull(warehouseId)){
            response.agregarMensaje(Messages.ERROR_WAREHOUSE_ID_NULL);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(!validateWareHouseExist(warehouseId)){
            response.agregarMensaje(Messages.ERROR_WAREHOUSE_DOES_NOT_EXIST);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        //validar si existe almenos un agendamiento en la bodega
        if(scheduleRepository.findAllByWarehouseId(warehouseId).isEmpty()){
            response.agregarMensaje(Messages.ERROR_DOES_NOT_EXIST_SCHEDULE);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else {
            activeSchedule = searchActiveSchedule(scheduleRepository.findAllByWarehouseId(warehouseId));
            if (activeSchedule==null){
                response.agregarMensaje(Messages.ERROR_DOES_NOT_EXIST_AN_ACTIVE_SCHEDULE);
                respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
                datosValidos= false;
            }else{
                datosValidos=true;
            }

        }

        if(datosValidos){

            respuestaSolicitud= new ResponseEntity<>(activeSchedule.getUser().getUserName(), HttpStatus.OK);
            //consumir servicio de chatbot
            WebServiceClient.consume(activeSchedule.getUser().getUserName());

        }
        return respuestaSolicitud;
    }

    private Boolean validateWareHouseExist(Integer warehouseId){
        Optional<WareHouseModel> listWarehouse = warehouseRepository.findById(warehouseId);

        if (listWarehouse.isPresent()){
            return true; //Exist
        }else{
            return false; //Not exist
        }
    }

    private Boolean validateUserExist(Integer userId){
        Optional<UserModel> listUser = userRepository.findById(userId);

        if(listUser.isPresent()){
            return true; //Exist
        }else {
            return false; //Not exist
        }
    }

    private ScheduleModel searchActiveSchedule(List<ScheduleModel> schedules){
        ScheduleModel activeSchedule = null;
        for (ScheduleModel schedule:schedules){
            if (scheduleIsActive(schedule)){
                activeSchedule = schedule;
            }
        }
        return activeSchedule;
    }

    private Boolean scheduleIsActive(ScheduleModel schedule){

        LocalDateTime startTime=schedule.getDate();
        LocalDateTime endTime=startTime.plusMinutes(5);


        if (LocalDateTime.now().isAfter(endTime) || LocalDateTime.now().isBefore(startTime)){
            System.out.println(LocalDateTime.now());
            return false;
        }
        else {
            return true;
        }
    }

    private boolean respectsBounds(LocalDateTime date){
        return scheduleRepository.findScheduleWithinDates(date.minusMinutes(5), date.plusMinutes(5)).size()==0;
    }

    private ScheduleDTO converToDTO(ScheduleModel schedule){
        modelMapper.typeMap(ScheduleModel.class, ScheduleDTO.class).addMapping(src->src.getUser().getId(), ScheduleDTO::setUser);
        modelMapper.typeMap(ScheduleModel.class, ScheduleDTO.class).addMapping(src->src.getWarehouse().getId(), ScheduleDTO::setWarehouse);
        modelMapper.typeMap(ScheduleModel.class, ScheduleDTO.class).addMapping(src->src.getCode().getCode(), ScheduleDTO::setCode);
        ScheduleDTO converted=modelMapper.map(schedule, ScheduleDTO.class);


        return converted;
    }
}
