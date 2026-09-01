package ec.edu.monster.transacciones.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class TransferenciaRequest {
    private String cuentaOrigen;
    private String cuentaDestino;
    private BigDecimal importe;
    private String empleado;
}