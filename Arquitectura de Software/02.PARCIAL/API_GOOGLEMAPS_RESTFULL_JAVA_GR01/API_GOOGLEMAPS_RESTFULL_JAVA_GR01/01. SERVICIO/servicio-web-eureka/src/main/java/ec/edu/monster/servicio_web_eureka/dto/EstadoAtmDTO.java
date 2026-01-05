package ec.edu.monster.servicio_web_eureka.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstadoAtmDTO {
    private Long idEstado;
    private Long idAtm;
    private LocalDateTime fechaRegistro;
    private String estado;
}
