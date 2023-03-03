package backend.team.backend.controllers;

import static backend.team.backend.assemblers.UserAssembler.assembleToModel;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import backend.team.backend.dto.UserDTO;
import backend.team.backend.models.UserModel;
import backend.team.backend.services.UserService;

@RestController
@RequestMapping("/usuario")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    private UserDTO converToDto(UserModel user){
        modelMapper.typeMap(UserModel.class, UserDTO.class).addMapping(src->src.getRole().getId(), UserDTO::setIdRole);
        UserDTO converted=modelMapper.map(user,UserDTO.class);
        return converted;
    }


    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                UserModel user = userService.findByUsername(username);

                String access_token = JWT.create()
                        .withSubject(user.getUserName())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000)) //10 minutos
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", (Map<String, ?>) user.getRole())
                        .sign(algorithm);

                Map<String,String> tokens = new HashMap<>();
                tokens.put("access_token",access_token);
                tokens.put("refresh_token",refresh_token);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),tokens);
            }catch (Exception exception){
                System.out.println("exception: "+exception.getMessage());
                response.setHeader("error",exception.getMessage());
                response.setStatus(FORBIDDEN.value());


                Map<String,String> error = new HashMap<>();
                error.put("error_message",exception.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(),error);
            }

        }else {
            throw new RuntimeException("Refresh token is missing");
        }
    } 

    @GetMapping()
    public List<UserDTO> list(){
        List<UserModel> users=userService.getUsers();
        return users.stream().map(this::converToDto).collect(Collectors.toList());
    }
    @PostMapping()
    public ResponseEntity<?> saveUser(@RequestBody UserDTO user){
        UserModel userEntity= assembleToModel(user);
        return userService.saveUser(userEntity);
    }

    @GetMapping(path="/{id}")
    public UserDTO getUserById(@PathVariable("id") int id){
    return converToDto(this.userService.getUserById(id).get());
    }

    @PutMapping("/{id}")
    public String update(@PathVariable("id") int id, @RequestBody UserDTO user){
        try{
            userService.getUserById(id).get();
            UserModel converted= assembleToModel(user);
            converted.setId(id);
            userService.saveUser(converted);
            return "Se ha actualizado exitosamente";
        }catch(Exception e){
            return "Se ha producido un error tratando de actualizar";
        }
    }

    @DeleteMapping(path="/{id}")
    public String deleteUserById(@PathVariable("id") int id){
        boolean ok=this.userService.deleteUser(id);
        if(ok){
            return "Usuario con id "+id+" ha sido eliminado exitosamente";
        }
        else{
            return "No se pudo eliminar el usuario con el id"+id;
        }
    }
}
