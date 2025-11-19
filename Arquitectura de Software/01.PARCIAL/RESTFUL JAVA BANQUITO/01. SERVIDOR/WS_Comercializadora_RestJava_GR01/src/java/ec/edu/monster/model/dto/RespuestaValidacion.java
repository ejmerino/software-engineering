package ec.edu.monster.model.dto;

public class RespuestaValidacion {
    
    private boolean esAprobado;
    private String mensaje;

    public RespuestaValidacion(boolean esAprobado, String mensaje) {
        this.esAprobado = esAprobado;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public boolean isEsAprobado() {
        return esAprobado;
    }
    public void setEsAprobado(boolean esAprobado) {
        this.esAprobado = esAprobado;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}