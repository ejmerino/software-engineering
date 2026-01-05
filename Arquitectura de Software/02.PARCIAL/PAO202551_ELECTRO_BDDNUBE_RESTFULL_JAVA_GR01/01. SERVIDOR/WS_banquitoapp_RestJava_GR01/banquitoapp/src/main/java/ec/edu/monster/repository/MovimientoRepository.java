package ec.edu.monster.repository;

import ec.edu.monster.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    // CORRECCIÓN: Todo cambiado a minúsculas (movimiento, cuenta, num_cuenta, cedula, tipo, fecha)

    // Regla 2: Verificar depósitos en el último mes
    @Query(value = "SELECT COUNT(*) FROM movimiento m " +
            "JOIN cuenta c ON m.num_cuenta = c.num_cuenta " +
            "WHERE c.cedula = :cedula AND m.tipo = 'DEP' " +
            "AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)", nativeQuery = true)
    int contarDepositosUltimoMes(@Param("cedula") String cedula);

    // Punto 6: Promedio de Depósitos (últimos 3 meses)
    @Query(value = "SELECT COALESCE(AVG(m.valor), 0) FROM movimiento m " +
            "JOIN cuenta c ON m.num_cuenta = c.num_cuenta " +
            "WHERE c.cedula = :cedula AND m.tipo = 'DEP' " +
            "AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)", nativeQuery = true)
    double promedioDepositosTrimestre(@Param("cedula") String cedula);

    // Punto 6: Promedio de Retiros (últimos 3 meses)
    @Query(value = "SELECT COALESCE(AVG(m.valor), 0) FROM movimiento m " +
            "JOIN cuenta c ON m.num_cuenta = c.num_cuenta " +
            "WHERE c.cedula = :cedula AND m.tipo = 'RET' " +
            "AND m.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)", nativeQuery = true)
    double promedioRetirosTrimestre(@Param("cedula") String cedula);
}