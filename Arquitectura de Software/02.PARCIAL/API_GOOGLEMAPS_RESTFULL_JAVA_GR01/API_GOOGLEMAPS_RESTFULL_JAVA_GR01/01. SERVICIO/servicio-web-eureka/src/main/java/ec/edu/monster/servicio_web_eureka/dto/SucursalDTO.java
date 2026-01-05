package ec.edu.monster.servicio_web_eureka.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SucursalDTO {
    private Integer id;
    private String nombre;
    private String ciudad;
    private String direccion;
    private Double latitud;
    private Double longitud;
}
