package backend.team.backend.excepcion;

public class ExcepcionUsuarioNoExiste extends RuntimeException{
    
    public ExcepcionUsuarioNoExiste(String mensaje){
        super(mensaje);
    }
}
