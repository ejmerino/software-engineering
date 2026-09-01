package ec.edu.monster.cuentas.repositories;

import ec.edu.monster.cuentas.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, String> {

    // CORRECCIÓN: Usamos 'codigo' porque así se llama la variable en Cuenta.java
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Cuenta c WHERE c.codigo = :id")
    Optional<Cuenta> findByIdWithLock(@Param("id") String id);
}