package ec.edu.monster.historial.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoDTO {
    private String cuenta;
    private Integer nroMov;
    private LocalDateTime fecha;
    private String empleado;
    private String tipo;
    private BigDecimal importe;
}