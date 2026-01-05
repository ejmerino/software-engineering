package ec.edu.monster.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "MOVIMIENTO")
public class Movimiento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_MOVIMIENTO")
    private Integer codMovimiento;

    @Column(name = "TIPO", length = 3) // DEP o RET
    private String tipo;

    @Column(name = "VALOR")
    private BigDecimal valor;

    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @ManyToOne
    @JoinColumn(name = "NUM_CUENTA", nullable = false)
    private Cuenta cuenta;

    public Movimiento() {}
    // Generar Getters y Setters...
}