package ec.edu.monster.repository;

import ec.edu.monster.model.Credito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CreditoRepository extends JpaRepository<Credito, Integer> {

    // Regla 4: Verificar créditos activos
    @Query("SELECT COUNT(c) FROM Credito c WHERE c.cliente.cedula = :cedula AND c.estado = 'Activo'")
    int countCreditosActivos(@Param("cedula") String cedula);

    // Buscar historial de créditos
    List<Credito> findByClienteCedula(String cedula);
}