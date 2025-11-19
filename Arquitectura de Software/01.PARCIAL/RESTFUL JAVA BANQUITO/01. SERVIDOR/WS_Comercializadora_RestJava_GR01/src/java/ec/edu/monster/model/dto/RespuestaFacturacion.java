package ec.edu.monster.model.dto;

import ec.edu.monster.model.Factura;

/**
 * Un objeto de respuesta unificado.
 * Contiene la factura si fue exitosa, o un mensaje de error si no lo fue.
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
    
    // Un helper para que el JSON sea más limpio
    public boolean isFueExitoso() {
        return error == null;
    }
}