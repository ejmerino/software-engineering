package ec.edu.monster.model;

public class RespuestaFacturacion {
    private boolean exito;
    private String mensaje;
    private Factura facturaGenerada;

    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Factura getFacturaGenerada() { return facturaGenerada; }
    public void setFacturaGenerada(Factura facturaGenerada) { this.facturaGenerada = facturaGenerada; }
    
    // Método de compatibilidad
    public Factura getFactura() { return facturaGenerada; }
    public boolean isFueExitoso() { return exito; }
    public String getError() { return mensaje; }
}