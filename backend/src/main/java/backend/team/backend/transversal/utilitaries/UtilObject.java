package backend.team.backend.transversal.utilitaries;

public class UtilObject {

    private UtilObject() {
    }
    public static <T> boolean isNull(T objeto) {
        return objeto==null;
    }

    public static <T> T defaultValue(T objeto, T defecto){
        return objeto != null ? objeto : defecto;
    }
}
