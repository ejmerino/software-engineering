package ec.edu.monster.model;

public class RespuestaFacturacion {
    private boolean exito;
    private String mensaje;
    private Factura facturaGenerada;

    // --- GETTERS Y SETTERS ESTÁNDAR (Para Gson) ---
    public boolean isExito() { return exito; }
    public void setExito(boolean exito) { this.exito = exito; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public Factura getFacturaGenerada() { return facturaGenerada; }
    public void setFacturaGenerada(Factura facturaGenerada) { this.facturaGenerada = facturaGenerada; }
    
    // --- MÉTODOS DE COMPATIBILIDAD (Para que tu MainAppController no falle) ---
    
    // El controlador llama a isFueExitoso(), nosotros le devolvemos isExito()
    public boolean isFueExitoso() {
        return exito;
    }

    // El controlador llama a getError(), nosotros le devolvemos el mensaje
    public String getError() {
        return mensaje;
    }

    // El controlador llama a getFactura(), nosotros le devolvemos facturaGenerada
    public Factura getFactura() {
        return facturaGenerada;
    }
}