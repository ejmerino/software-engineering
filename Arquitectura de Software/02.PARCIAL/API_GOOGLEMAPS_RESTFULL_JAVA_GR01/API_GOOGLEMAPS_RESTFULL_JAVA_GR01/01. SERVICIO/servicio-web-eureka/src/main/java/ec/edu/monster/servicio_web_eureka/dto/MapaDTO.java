package ec.edu.monster.servicio_web_eureka.dto;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MapaDTO {
    private List<SucursalDTO> sucursales;
    private List<AtmDTO> atms;
    private List<EstadoAtmDTO> estadosAtm;
}
