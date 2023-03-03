package backend.team.backend.dto;

import java.time.LocalDateTime;

public class CodeGenerateDTO {
    private LocalDateTime startTimeValidity;
    private Long validminutes;

    public  CodeGenerateDTO(){

    }

    public LocalDateTime getStartTimeValidity() {
        return startTimeValidity;
    }

    public Long getValidminutes() {
        return validminutes;
    }
}
