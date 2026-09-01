package ec.edu.monster.transacciones.entities;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MovimientoId implements Serializable {
    private String cuenta; // Debe coincidir con el nombre en Movimiento
    private Integer nroMov; // Debe coincidir con el nombre en Movimiento
}