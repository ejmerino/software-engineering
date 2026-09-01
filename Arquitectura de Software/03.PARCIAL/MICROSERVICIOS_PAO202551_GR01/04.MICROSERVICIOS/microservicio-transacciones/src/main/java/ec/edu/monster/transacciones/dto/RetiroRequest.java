package ec.edu.monster.transacciones.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class RetiroRequest {
    private String cuenta;
    private BigDecimal importe;
    private String empleado;
}