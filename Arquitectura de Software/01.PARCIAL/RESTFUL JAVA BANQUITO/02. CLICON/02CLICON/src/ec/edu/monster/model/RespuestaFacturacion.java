package ec.edu.monster.model;

/**
 * POJO (Molde) para recibir la respuesta unificada de facturación.
 * Contiene la factura (en éxito) o un error (en rechazo).
 */
public class RespuestaFacturacion {
    
    private Factura factura; // Será null si hay error
    private String error;    // Será null si hay éxito

    // Constructor para Éxito
    public RespuestaFacturacion(Factura factura) {
        this.factura = factura;
        this.error = null;
    }

    // Constructor para Error
    public RespuestaFacturacion(String error) {
        this.factura = null;
        this.error = error;
    }
    
    // Constructor vacío para Gson
    public RespuestaFacturacion() {
    }

    // Getters y Setters
    public Factura getFactura() {
        return factura;
    }
    public void setFactura(Factura factura) {
        this.factura = factura;
    }
    public String getError() {
        return error;
    }
    public void setError(String error) {
        this.error = error;
    }
    
    // Helper para revisar si la operación fue exitosa
    public boolean isFueExitoso() {
        return error == null || error.isEmpty();
    }
}