package backend.team.backend.models;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name="agenda")
public class ScheduleModel {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name="type")
    private String type;
    @ManyToOne
    @JoinColumn(name="warehouse")
    private WareHouseModel warehouse;
    @Column(name="description")
    private String description;
    @Column(name="date")
    private LocalDateTime date;
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserModel user;

    @OneToOne
    @JoinColumn(name="code")
    private CodeModel code;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public WareHouseModel getWarehouse() {
        return warehouse;
    }
    public void setWarehouse(WareHouseModel warehouse) {
        this.warehouse = warehouse;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public UserModel getUser() {
        return user;
    }
    public void setUser(UserModel user) {
        this.user = user;
  }
    public CodeModel getCode() {
        return code;
    }
    public void setCode(CodeModel code) {
        this.code = code;
    }
  
}