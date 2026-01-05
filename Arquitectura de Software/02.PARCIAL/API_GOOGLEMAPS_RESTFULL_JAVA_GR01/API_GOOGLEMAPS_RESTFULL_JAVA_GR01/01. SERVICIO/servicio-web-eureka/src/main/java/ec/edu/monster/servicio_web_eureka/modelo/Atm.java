package ec.edu.monster.servicio_web_eureka.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "atm")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Atm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_atm")
    private Long idAtm;

    @Column(name = "nombre", nullable = false, length = 50)
    private String nombre;

    @Column(name = "direccion", nullable = false, length = 100)
    private String direccion;

    // ❌ quitar precision y scale
    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;

    @Column(name = "estado", nullable = false, length = 20)
    private String estado; // ACTIVO, MANTENIMIENTO, FUERA DE SERVICIO
}
