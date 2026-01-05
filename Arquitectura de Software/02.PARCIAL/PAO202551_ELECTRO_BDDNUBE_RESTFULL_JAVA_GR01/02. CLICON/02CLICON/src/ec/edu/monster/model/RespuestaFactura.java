package ec.edu.monster.model;

// POJO para deserializar la respuesta de la factura
public class RespuestaFactura {
    private String mensaje;
    private String error;
    
    public String getMensaje() { return mensaje; }
    public String getError() { return error; }

    public boolean fueExitoso() {
        return error == null || error.isEmpty();
    }

    @Override
    public String toString() {
        if (fueExitoso()) {
            return "ÉXITO: " + mensaje;
        }
        return "ERROR: " + error;
    }
}