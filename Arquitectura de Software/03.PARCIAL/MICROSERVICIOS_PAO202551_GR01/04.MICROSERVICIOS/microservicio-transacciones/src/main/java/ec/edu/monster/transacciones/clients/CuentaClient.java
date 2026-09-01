package ec.edu.monster.transacciones.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

@FeignClient(name = "microservicio-cuentas", url = "http://localhost:8081")
public interface CuentaClient {
    @PutMapping("/api/cuentas/{id}/actualizar-saldo")
    Integer actualizarSaldo(@PathVariable String id,
                            @RequestParam BigDecimal importe,
                            @RequestParam String tipoAccion);
}