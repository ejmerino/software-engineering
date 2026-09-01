package ec.edu.monster.transacciones.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "movimiento")
@IdClass(MovimientoId.class) // Indica que la PK es compuesta
@Data
public class Movimiento {

    @Id
    @Column(name = "chr_cuencodigo")
    private String cuenta;

    @Id
    @Column(name = "int_movinumero")
    private Integer nroMov;

    @Column(name = "dtt_movifecha")
    private LocalDateTime fecha;

    @Column(name = "chr_emplcodigo")
    private String empleado;

    @Column(name = "chr_tipocodigo")
    private String tipo;

    @Column(name = "dec_moviimporte")
    private BigDecimal importe;

    // Lombok @Data generará los getters y setters automáticamente
}