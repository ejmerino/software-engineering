package ec.edu.monster.transacciones.repositories;

import ec.edu.monster.transacciones.entities.Movimiento;
import ec.edu.monster.transacciones.entities.MovimientoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface MovimientoRepository extends JpaRepository<Movimiento, MovimientoId> {

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO movimiento (chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte) " +
            "VALUES (:cuenta, :nro, :fecha, :empl, :tipo, :importe)", nativeQuery = true)
    void registrarMovimientoNativo(@Param("cuenta") String cuenta,
                                   @Param("nro") Integer nro,
                                   @Param("fecha") LocalDateTime fecha,
                                   @Param("empl") String empl,
                                   @Param("tipo") String tipo,
                                   @Param("importe") BigDecimal importe);
}