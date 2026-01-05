package ec.edu.monster.servicio_web_eureka.modelo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sucursal")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sucursal")
    private Integer idSucursal;

    @Column(name = "vch_sucunombre", nullable = false, length = 255)
    private String nombre;

    @Column(name = "vch_sucuciudad", nullable = false, length = 255)
    private String ciudad;

    @Column(name = "vch_sucudireccion", length = 255)
    private String direccion;

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;

    @Column(name = "int_sucucontcuenta", nullable = false)
    private Integer contadorCuenta;
}
