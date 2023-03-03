package backend.team.backend.dto;

public class CodeDTO {

    private String code;

    private CodeDTO(String code) {
        setCode(code);
    }

    public CodeDTO() {
    }

    public static CodeDTO create(String code){
        return new CodeDTO(code);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
