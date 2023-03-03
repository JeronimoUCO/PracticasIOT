package backend.team.backend.excepcion;

public class ExcepcionBodegaNoExiste extends RuntimeException{
    public ExcepcionBodegaNoExiste(String mensaje){
        super(mensaje);
    }
}
