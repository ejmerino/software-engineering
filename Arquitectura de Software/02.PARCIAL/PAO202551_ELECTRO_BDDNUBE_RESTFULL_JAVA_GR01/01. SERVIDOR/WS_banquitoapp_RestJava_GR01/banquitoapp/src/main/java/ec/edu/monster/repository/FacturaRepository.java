package ec.edu.monster.repository;

import ec.edu.monster.model.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FacturaRepository extends JpaRepository<Factura, Integer> {
    // Buscar historial de compras
    List<Factura> findByClienteCedula(String cedula);
}