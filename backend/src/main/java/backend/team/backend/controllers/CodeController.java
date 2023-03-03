package backend.team.backend.controllers;

import backend.team.backend.dto.CodeDTO;
import backend.team.backend.dto.CodeGenerateDTO;
import backend.team.backend.services.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/codigo")
public class CodeController {
    @Autowired
    private CodeService codeService;

    @PostMapping("/generar")
    public CodeDTO generateCode(@RequestBody CodeGenerateDTO codeGenerateDTO){
        return codeService.createCode(codeGenerateDTO.getStartTimeValidity(),codeGenerateDTO.getValidminutes());
    }

    @PostMapping
    public Boolean validateCode(@RequestBody CodeDTO codeDTO){
        return codeService.validateExpirationCode(codeDTO.getCode());
    }
}
