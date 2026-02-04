package ec.edu.monster.controlador;

public class Session {
    private static String usuario;

    public static void login(String user) { usuario = user; }
    public static void logout() { usuario = null; }
    public static String getUsuario() { return usuario; }
    public static boolean isLogged() { return usuario != null && !usuario.isEmpty(); }
}
