package ec.edu.monster.cuentas.controllers;

import ec.edu.monster.cuentas.entities.Cuenta;
import ec.edu.monster.cuentas.services.CuentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService service;

    // FIX PARA EL 404: Recupera el método GET
    @GetMapping("/{id}")
    public ResponseEntity<Cuenta> obtenerCuenta(@PathVariable String id) {
        return service.obtenerCuenta(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/actualizar-saldo")
    public Integer actualizarSaldo(@PathVariable String id,
                                   @RequestParam BigDecimal importe,
                                   @RequestParam String tipoAccion) {
        return service.actualizarSaldo(id, importe, tipoAccion);
    }
}