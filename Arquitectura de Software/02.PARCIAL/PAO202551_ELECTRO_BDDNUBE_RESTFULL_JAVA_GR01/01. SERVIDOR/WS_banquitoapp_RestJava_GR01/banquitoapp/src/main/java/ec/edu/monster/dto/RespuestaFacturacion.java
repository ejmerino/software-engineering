package ec.edu.monster.dto;

import ec.edu.monster.model.Factura;

public class RespuestaFacturacion {
    private boolean exito;
    private String mensaje;
    private Factura facturaGenerada;

    // Constructor para CASOS DE ÉXITO
    public RespuestaFacturacion(Factura factura) {
        this.exito = true;
        this.mensaje = "Factura generada exitosamente";
        this.facturaGenerada = factura;
    }

    // Constructor para CASOS DE ERROR
    public RespuestaFacturacion(String mensajeError) {
        this.exito = false;
        this.mensaje = mensajeError;
        this.facturaGenerada = null;
    }

    // Getters y Setters
    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Factura getFacturaGenerada() { return facturaGenerada; }
    public void setFacturaGenerada(Factura facturaGenerada) { this.facturaGenerada = facturaGenerada; }
}