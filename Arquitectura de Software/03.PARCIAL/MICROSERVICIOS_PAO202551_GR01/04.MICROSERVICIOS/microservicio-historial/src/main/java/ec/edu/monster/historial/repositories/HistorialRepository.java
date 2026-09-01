package ec.edu.monster.historial.repositories;

import ec.edu.monster.historial.entities.Movimiento;
import ec.edu.monster.historial.entities.MovimientoId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HistorialRepository extends JpaRepository<Movimiento, MovimientoId> {
    // Consulta personalizada para obtener el historial de una cuenta
    List<Movimiento> findByCuentaOrderByNroMovDesc(String cuenta);
}