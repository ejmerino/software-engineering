package ec.edu.monster.historial.services;

import ec.edu.monster.historial.dtos.MovimientoDTO;
import ec.edu.monster.historial.entities.Movimiento;
import ec.edu.monster.historial.repositories.HistorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HistorialService {

    @Autowired
    private HistorialRepository repository;

    public List<MovimientoDTO> obtenerHistorial(String idCuenta) {
        List<Movimiento> movimientos = repository.findByCuentaOrderByNroMovDesc(idCuenta);

        if (movimientos.isEmpty()) {
            throw new RuntimeException("No hay movimientos para la cuenta: " + idCuenta);
        }

        // Convertimos la lista de Entidades a DTOs usando Stream API
        return movimientos.stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    private MovimientoDTO convertirADto(Movimiento mov) {
        return new MovimientoDTO(
                mov.getCuenta(),
                mov.getNroMov(),
                mov.getFecha(),
                mov.getEmpleado(),
                mov.getTipo(),
                mov.getImporte()
        );
    }
}