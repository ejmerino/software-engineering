package ec.edu.monster.repository;
import ec.edu.monster.model.Electrodomestico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElectrodomesticoRepository extends JpaRepository<Electrodomestico, Integer> {
}