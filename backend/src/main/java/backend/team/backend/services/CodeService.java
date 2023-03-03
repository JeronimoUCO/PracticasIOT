package backend.team.backend.services;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import backend.team.backend.dto.CodeDTO;
import backend.team.backend.models.CodeModel;
import backend.team.backend.client.mqtt.MQTT;
import backend.team.backend.repositories.CodeRepository;



@Service
public class CodeService {

    @Autowired
    private CodeRepository codeRepository;

    //https://ourcodeworld.co/articulos/leer/964/como-generar-cadenas-alfanumericas-aleatorias-con-una-longitud-personalizada-en-java
    private String generateRandomCode(){
        String CHAR_LOWER = "abcdefghijklmnopqrstuvwxyz";
        //String CHAR_UPPER = CHAR_LOWER.toUpperCase();
        String NUMBER = "0123456789";

        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(6);

        for (int i = 0; i < 3; i++) { //3 letters
            int rndCharAt = random.nextInt(CHAR_LOWER.length());
            char rndChar = CHAR_LOWER.charAt(rndCharAt);
            sb.append(rndChar);
        }
        for (int i = 0; i < 3; i++) { //3 numbers
            int rndCharAt = random.nextInt(NUMBER.length());
            char rndChar = NUMBER.charAt(rndCharAt);
            sb.append(rndChar);
        }

        return  sb.toString();
    }
    private Boolean validateCodeNotExist(String code){
        Optional<CodeModel> listCode = codeRepository.findById(code);
        System.out.println(listCode);

        if (!listCode.isPresent()){
            return true; //Does not exist
        }else{
            return false; //It already exists
        }
    }


    public CodeDTO createCode(LocalDateTime startTimeValidity, Long validminutes){
        String randomCode = generateRandomCode();
        while(validateCodeNotExist(randomCode)==false){ //if the generated code already exists, then a new one is generated.
            randomCode = generateRandomCode();
        }
        

        LocalDateTime endTimeValidity = startTimeValidity.plusMinutes(validminutes); //Expires in validminutes



        codeRepository.save(new CodeModel(randomCode,startTimeValidity,endTimeValidity));
        return CodeDTO.create(randomCode);
    }

    public Boolean validateExpirationCode(String code){
        if (validateCodeNotExist(code)){ //if code does not exist, then just returns false
            return false;
        }else{

            CodeModel codeToValidate = codeRepository.findById(code).get();


            if (codeIsValid(codeToValidate)){
                MQTT mqtt = new MQTT();
                mqtt.publish();
                return true; //Code is valid
            }else{
                return false; //Code is not valid
            }



        }
    }

    private Boolean codeIsValid(CodeModel codeModel){

        if (LocalDateTime.now().isAfter(codeModel.getEndTimeValidity()) || LocalDateTime.now().isBefore(codeModel.getStartTimeValidity())){
            return false;
        }
        else {
            return true;


        }
    }

    
    public Optional<CodeModel> getByCode(String code){
        return codeRepository.findById(code);
    }

}
