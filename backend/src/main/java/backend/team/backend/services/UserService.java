package backend.team.backend.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import backend.team.backend.transversal.messages.Messages;
import backend.team.backend.transversal.response.rest.Response;
import backend.team.backend.transversal.utilitaries.UtilObject;
import backend.team.backend.transversal.utilitaries.UtilText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import backend.team.backend.models.UserModel;
import backend.team.backend.repositories.UserRepository;

@Service
@Transactional
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findByUserName(username);

        if (user==null){
            throw new UsernameNotFoundException("Usuario no encontrado en la base de datos");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority(user.getRole().getNameRole()));
        return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getPassword(),authorities);


    }

    public List<UserModel> getUsers() {
         return userRepository.findAll();
    }

    public ResponseEntity<?> saveUser(UserModel user) {
        ResponseEntity<?> respuestaSolicitud = null;
        Response<UserModel> response = new Response<>();
        boolean datosValidos = true;

        if(UtilObject.isNull(user)){
            response.agregarMensaje(Messages.ERROR_OBJECT_USER_VACIO);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }
        //Validar información del nombre de usuario.
        if(UtilObject.isNull(user.getUserName())){
            response.agregarMensaje(Messages.ERROR_USER_USERNAME_NULL);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;

        }else if(UtilText.isEmpty(user.getUserName())){
            response.agregarMensaje(Messages.ERROR_USER_USERNAME_EMPTY);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(UtilText.applyTrim(user.getUserName()).length()>20){
            response.agregarMensaje(Messages.ERROR_USER_USERNAME_LENGHT);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        //Validar informacion del primer nombre
        if(UtilObject.isNull(user.getFirstName())){
            response.agregarMensaje(Messages.ERROR_USER_FIRST_NAME_NULL);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(UtilText.isEmpty(user.getFirstName())){
            response.agregarMensaje(Messages.ERROR_USER_FIRST_NAME_VACIO);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(UtilText.applyTrim(user.getFirstName()).length()>20){
            response.agregarMensaje(Messages.ERROR_USER_FIRST_NAME_VACIO_LENGHT);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        //validar información del apellido
        if(UtilObject.isNull(user.getLastName())){
            response.agregarMensaje(Messages.ERROR_USER_LASTNAME_NULL);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(UtilText.isEmpty(user.getLastName())){
            response.agregarMensaje(Messages.ERROR_USER_LASTNAME_EMPTY);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(UtilText.applyTrim(user.getLastName()).length()>20){
            response.agregarMensaje(Messages.ERROR_USER_LASTNAME_LENGHT);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        //Validar información de la contraseña
        if(UtilObject.isNull(user.getPassword())){
            response.agregarMensaje(Messages.ERROR_USER_PASSWORD_NULL);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(UtilText.isEmpty(user.getPassword())){
            response.agregarMensaje(Messages.ERROR_USER_PASSWORD_EMPTY);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(UtilText.applyTrim(user.getPassword()).length()<8){
            response.agregarMensaje(Messages.ERROR_USER_PASSWORD_LENGHT);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        if(UtilObject.isNull(user.getRole())){
            response.agregarMensaje(Messages.ERROR_USER_ID_ROLE_NULL);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }else if(user.getRole().getId()>3){
            response.agregarMensaje(Messages.ERROR_USER_ID_ROLE_DOES_NOT_EXIST);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }


        if(userRepository.findByUserName(user.getUserName())!=null){
            response.agregarMensaje(Messages.ERROR_USER_USERNAME_ALREADY_EXISTS);
            respuestaSolicitud= new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            datosValidos= false;
        }

        if(datosValidos){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            respuestaSolicitud= new ResponseEntity<>(userRepository.save(user), HttpStatus.OK);
        }
        return respuestaSolicitud;
    }

    public Optional<UserModel> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public boolean deleteUser(Integer id) {
        try {
            userRepository.deleteById(id);
            return true;
        } catch (Exception err) {
            return false;
        }
    }

    public UserModel findByUsername(String username){
        return userRepository.findByUserName(username);
    }


}
