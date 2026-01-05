package ec.edu.monster.repository;

import ec.edu.monster.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    // NO PONER findByClienteCedula AQUÍ.
    // Como la cédula es el @Id, usa findById(cedula) que ya viene gratis.
}