package backend.team.backend.transversal.utilitaries;

public class UtilText {

    private UtilText() {

    }
    public static String defaultValue(String value, String defaultValue) {
        return value != null ? value : defaultValue ;
    }

    public static String applyTrim(String valor) {
        return valor == null ? "" : valor.trim();
    }

    public static boolean isEmpty(String valor) {
        return "".equals(applyTrim(valor));
    }
}
