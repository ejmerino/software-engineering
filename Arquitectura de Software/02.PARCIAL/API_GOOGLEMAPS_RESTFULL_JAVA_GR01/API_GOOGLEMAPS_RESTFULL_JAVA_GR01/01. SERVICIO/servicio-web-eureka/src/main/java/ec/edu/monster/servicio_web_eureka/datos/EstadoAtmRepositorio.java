package ec.edu.monster.servicio_web_eureka.datos;

import ec.edu.monster.servicio_web_eureka.modelo.EstadoAtm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoAtmRepositorio extends JpaRepository<EstadoAtm, Long> {
}
