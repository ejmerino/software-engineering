package ec.edu.monster.historial.entities;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovimientoId implements Serializable {
    private String cuenta;
    private Integer nroMov;
}