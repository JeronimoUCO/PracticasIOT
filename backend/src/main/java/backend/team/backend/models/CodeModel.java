package backend.team.backend.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="codigo")
public class CodeModel {
    @Id
    @Column(name="code")
    private String code;
    @Column(name="starttimevalidity")
    private LocalDateTime startTimeValidity;
    @Column(name="endtimevalidity")
    private LocalDateTime endTimeValidity;

    public CodeModel(String code, LocalDateTime startTimeValidity, LocalDateTime endTimeValidity) {
        this.code = code;
        this.startTimeValidity = startTimeValidity;
        this.endTimeValidity = endTimeValidity;
    }

    public CodeModel(){

    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getStartTimeValidity() {
        return startTimeValidity;
    }

    public LocalDateTime getEndTimeValidity() {
        return endTimeValidity;
    }



}
