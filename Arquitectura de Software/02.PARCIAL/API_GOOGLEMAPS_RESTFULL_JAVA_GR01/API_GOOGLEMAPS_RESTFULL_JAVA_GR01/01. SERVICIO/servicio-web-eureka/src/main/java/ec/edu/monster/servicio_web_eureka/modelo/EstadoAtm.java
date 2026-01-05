package ec.edu.monster.servicio_web_eureka.modelo;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "estadoatm")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoAtm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado")
    private Long idEstado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_atm", nullable = false)
    private Atm atm;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado;  // ACTIVO, MANTENIMIENTO, FUERA DE SERVICIO
}
