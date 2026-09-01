package ec.edu.monster.cuentas.services;

import ec.edu.monster.cuentas.entities.Cuenta;
import ec.edu.monster.cuentas.repositories.CuentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository repository;

    /**
     * Obtiene una cuenta por su código (ID).
     * Soluciona el error 404 en las consultas GET.
     */
    public Optional<Cuenta> obtenerCuenta(String id) {
        return repository.findById(id);
    }

    /**
     * Actualiza el saldo y el contador de movimientos.
     * @return El nuevo número de movimiento (contadorMov + 1).
     * @throws RuntimeException si la cuenta no existe, no está activa o tiene saldo insuficiente.
     */
    @Transactional
    public Integer actualizarSaldo(String idCuenta, BigDecimal importe, String tipoAccion) {
        // 1. Bloqueo Pesimista: 'FOR UPDATE' en SQL.
        // Evita que dos ventanillas modifiquen la misma cuenta simultáneamente.
        Cuenta cuenta = repository.findByIdWithLock(idCuenta)
                .orElseThrow(() -> new RuntimeException("Error: La cuenta " + idCuenta + " no existe."));

        // 2. Validación de estado según el campo 'estado' de tu entidad Cuenta.java
        if (!"ACTIVO".equalsIgnoreCase(cuenta.getEstado())) {
            throw new RuntimeException("Error: La cuenta no se encuentra activa.");
        }

        // 3. Cálculo de saldo usando BigDecimal para evitar errores de coma flotante
        BigDecimal nuevoSaldo;
        if ("INGRESO".equalsIgnoreCase(tipoAccion)) {
            nuevoSaldo = cuenta.getSaldo().add(importe);
        } else {
            nuevoSaldo = cuenta.getSaldo().subtract(importe);
        }

        // 4. Validación de saldo negativo
        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Error: Saldo insuficiente para realizar el retiro.");
        }

        // 5. Incrementar el contador de movimientos
        // Este valor se envía de vuelta para ser usado como PK en la tabla 'movimiento'
        Integer nuevoContador = cuenta.getContadorMov() + 1;

        // 6. Persistencia de datos
        cuenta.setSaldo(nuevoSaldo);
        cuenta.setContadorMov(nuevoContador);

        repository.save(cuenta);

        // Retornamos el número para que el Microservicio de Transacciones lo use
        return nuevoContador;
    }
}