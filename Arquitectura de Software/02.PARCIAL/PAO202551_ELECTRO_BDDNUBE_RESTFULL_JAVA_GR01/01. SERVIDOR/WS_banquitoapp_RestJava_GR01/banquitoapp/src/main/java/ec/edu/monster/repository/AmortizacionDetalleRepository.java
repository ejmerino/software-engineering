package ec.edu.monster.repository;

import ec.edu.monster.model.AmortizacionDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AmortizacionDetalleRepository extends JpaRepository<AmortizacionDetalle, Integer> {
}