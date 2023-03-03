package backend.team.backend.assemblers;

import backend.team.backend.dto.UserDTO;
import backend.team.backend.models.UserModel;

public class UserAssembler {
    public static UserModel assembleToModel(UserDTO userDTO){
        UserModel user = new UserModel();
        user.setfirstName(userDTO.getUserName());
        user.setPassword(userDTO.getPassword());
        user.setUserName(userDTO.getUserName());
        user.setLastName(userDTO.getLastName());
        user.getRole().setId(userDTO.getIdRole());
        return user;
    }
}
