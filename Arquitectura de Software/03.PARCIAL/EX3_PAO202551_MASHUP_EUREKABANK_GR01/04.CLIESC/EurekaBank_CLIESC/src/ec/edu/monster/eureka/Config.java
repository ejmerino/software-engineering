package ec.edu.monster.eureka;

public final class Config {
    // ⚠️ Ajusta si tu WAR/host/puerto son distintos
    public static final String HOST = "localhost";
    public static final int    PORT = 8080;
    public static final String APP_CONTEXT = "WSEurekaBank_GRO01";

    public static String baseUrl() {
        return String.format("http://%s:%d/%s/webresources/coreBancario",
                HOST, PORT, APP_CONTEXT);
    }

    private Config() {}
}
