package ec.edu.monster.model.dto;

public class RespuestaCredito {
    private boolean creditoAprobado;
    private String mensaje;
    private int idCreditoGenerado; // Opcional, para referencia

    public RespuestaCredito(boolean creditoAprobado, String mensaje, int idCreditoGenerado) {
        this.creditoAprobado = creditoAprobado;
        this.mensaje = mensaje;
        this.idCreditoGenerado = idCreditoGenerado;
    }

    // Constructor simple para rechazos
    public RespuestaCredito(boolean creditoAprobado, String mensaje) {
        this(creditoAprobado, mensaje, 0);
    }

    // Getters y Setters
    public boolean isCreditoAprobado() { return creditoAprobado; }
    public void setCreditoAprobado(boolean creditoAprobado) { this.creditoAprobado = creditoAprobado; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }
    public int getIdCreditoGenerado() { return idCreditoGenerado; }
    public void setIdCreditoGenerado(int idCreditoGenerado) { this.idCreditoGenerado = idCreditoGenerado; }
}