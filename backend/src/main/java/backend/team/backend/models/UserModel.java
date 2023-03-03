package backend.team.backend.models;

import javax.persistence.*;

@Entity
@Table(name="usuario")
public class UserModel {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name="user_name")
    private String userName;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name="password")

    private String password;
    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name= "idrole")//referencedColumnName = "id"
    private RoleModel role;

    public UserModel() {
    }

    public UserModel(Integer id, String userName, String firstName, String lastName, String password) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }


    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setfirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public RoleModel getRole() {
        if(role ==null){
            role = new RoleModel();
        }
        return role;
    }

    public void setRole(RoleModel role) {
        this.role = role;
    }

}
