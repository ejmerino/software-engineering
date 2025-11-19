package ec.edu.monster.model.dto;

public class RespuestaMonto {
    
    private boolean esSujetoDeCredito;
    private double montoMaximo;
    private String mensaje;

    public RespuestaMonto(boolean esSujetoDeCredito, double montoMaximo, String mensaje) {
        this.esSujetoDeCredito = esSujetoDeCredito;
        this.montoMaximo = montoMaximo;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public boolean isEsSujetoDeCredito() {
        return esSujetoDeCredito;
    }
    public void setEsSujetoDeCredito(boolean esSujetoDeCredito) {
        this.esSujetoDeCredito = esSujetoDeCredito;
    }
    public double getMontoMaximo() {
        return montoMaximo;
    }
    public void setMontoMaximo(double montoMaximo) {
        this.montoMaximo = montoMaximo;
    }
    public String getMensaje() {
        return mensaje;
    }
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}