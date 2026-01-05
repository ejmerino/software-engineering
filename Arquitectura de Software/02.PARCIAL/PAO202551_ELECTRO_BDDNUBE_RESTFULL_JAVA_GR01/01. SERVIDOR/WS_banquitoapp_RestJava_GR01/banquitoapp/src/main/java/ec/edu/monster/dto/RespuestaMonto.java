package ec.edu.monster.dto;
public class RespuestaMonto {
    private boolean esSujetoDeCredito;
    private double montoMaximo;
    private String mensaje;
    public RespuestaMonto(boolean esSujeto, double monto, String msg) {
        this.esSujetoDeCredito = esSujeto;
        this.montoMaximo = monto;
        this.mensaje = msg;
    }
    // Getters y Setters...
    public boolean isEsSujetoDeCredito() { return esSujetoDeCredito; }
    public double getMontoMaximo() { return montoMaximo; }
    public String getMensaje() { return mensaje; }
}