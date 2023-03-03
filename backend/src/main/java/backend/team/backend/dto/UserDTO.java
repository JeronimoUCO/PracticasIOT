package backend.team.backend.dto;

public class UserDTO {
    private String userName;
    private String firstName;
    private String lastName;
    private String password;
    private Integer idRole;
  
    public UserDTO() {
    }


    public static UserDTO create(){
        return new UserDTO();
    }

    public UserDTO(String userName, String firstName, String lastName, String password, Integer idRole ) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.idRole=idRole;
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

    public void setFirstName(String firstName) {
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


    public Integer getIdRole() {
        return idRole;
    }

    public void setIdRole(Integer idRole) {
        this.idRole = idRole;
    }
}
