package ec.edu.monster.transacciones.services;

import ec.edu.monster.transacciones.clients.CuentaClient;
import ec.edu.monster.transacciones.dto.TransferenciaRequest;
import ec.edu.monster.transacciones.entities.Movimiento;
import ec.edu.monster.transacciones.repositories.MovimientoRepository;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TransaccionService {
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CuentaClient cuentaClient;

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Transactional
    public void registrarRetiro(String idCuenta, BigDecimal importe, String idEmpleado) {
        // 1. Llamada al microservicio de cuentas para obtener el nroMov
        Integer nuevoNroMov = cuentaClient.actualizarSaldo(idCuenta, importe, "SALIDA");

        // 2. Crear y guardar el movimiento con su PK compuesta
        Movimiento mov = new Movimiento();
        mov.setCuenta(idCuenta);    // Parte 1 de la PK
        mov.setNroMov(nuevoNroMov); // Parte 2 de la PK
        mov.setImporte(importe);
        mov.setEmpleado(idEmpleado);
        mov.setTipo("004"); // Retiro
        mov.setFecha(LocalDateTime.now());

        movimientoRepository.save(mov);
    }

    @Transactional
    public void registrarTransferencia(TransferenciaRequest req) {
        if (req.getCuentaOrigen().equals(req.getCuentaDestino())) {
            throw new RuntimeException("Error: La cuenta de origen y destino son iguales.");
        }

        // 1. PROCESAR CARGO (Origen - Tipo 009: Salida)
        Integer nroMovOrigen = cuentaClient.actualizarSaldo(req.getCuentaOrigen(), req.getImporte(), "SALIDA");

        movimientoRepository.registrarMovimientoNativo(
                req.getCuentaOrigen(),
                nroMovOrigen,
                LocalDateTime.now(),
                req.getEmpleado(),
                "009",
                req.getImporte()
        );

        // 2. PROCESAR ABONO (Destino - Tipo 008: Ingreso)
        Integer nroMovDestino = cuentaClient.actualizarSaldo(req.getCuentaDestino(), req.getImporte(), "INGRESO");

        movimientoRepository.registrarMovimientoNativo(
                req.getCuentaDestino(),
                nroMovDestino,
                LocalDateTime.now(),
                req.getEmpleado(),
                "008",
                req.getImporte()
        );
    }
}