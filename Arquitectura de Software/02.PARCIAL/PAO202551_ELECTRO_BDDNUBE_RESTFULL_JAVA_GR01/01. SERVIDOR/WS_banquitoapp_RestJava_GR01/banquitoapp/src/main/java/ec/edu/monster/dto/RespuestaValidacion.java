package ec.edu.monster.dto;
public class RespuestaValidacion {
    private boolean esAprobado;
    private String mensaje;
    public RespuestaValidacion(boolean esAprobado, String mensaje) {
        this.esAprobado = esAprobado;
        this.mensaje = mensaje;
    }
    // Getters y Setters
    public boolean isEsAprobado() { return esAprobado; }
    public String getMensaje() { return mensaje; }
}