package backend.team.backend.models;

import backend.team.backend.transversal.excepciones.ExceptionRole;
import backend.team.backend.transversal.messages.Messages;

import javax.persistence.*;

import static javax.persistence.GenerationType.AUTO;

@Entity
@Table(name="role")
public class RoleModel {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Integer id;
    @Column(name="name",unique = true, length = 20)
    private String nameRole;

    public RoleModel(){
    }

    public RoleModel(Integer id, String name) {
        this.id = id;
        this.nameRole = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        switch (id){
            case 1:
                setNameRole("ROLE_ADMIN");
                break;
            case 2:
                setNameRole("ROLE_USER");
                break;
            case 3:
                setNameRole("ROLE_PICKER");
                break;
            default:
                throw new ExceptionRole(Messages.ERROR_ID_ROLE_INVALID);
        }
        this.id = id;
    }

    public String getNameRole() {
        return nameRole;
    }

    public void setNameRole(String name) {
        this.nameRole = name;
    }


}
