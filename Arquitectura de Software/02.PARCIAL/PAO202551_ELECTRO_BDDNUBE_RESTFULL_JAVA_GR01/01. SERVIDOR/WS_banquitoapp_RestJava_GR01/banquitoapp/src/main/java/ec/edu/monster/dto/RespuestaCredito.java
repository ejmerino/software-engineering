package ec.edu.monster.dto;
public class RespuestaCredito {
    private boolean creditoAprobado;
    private String mensaje;
    private int idCreditoGenerado;
    public RespuestaCredito(boolean aprobado, String mensaje, int id) {
        this.creditoAprobado = aprobado;
        this.mensaje = mensaje;
        this.idCreditoGenerado = id;
    }
    // Constructor para error (sin ID)
    public RespuestaCredito(boolean aprobado, String mensaje) {
        this.creditoAprobado = aprobado;
        this.mensaje = mensaje;
    }
    // Getters y Setters...
    public boolean isCreditoAprobado() { return creditoAprobado; }
    public String getMensaje() { return mensaje; }
    public int getIdCreditoGenerado() { return idCreditoGenerado; }
}