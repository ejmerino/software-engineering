package ec.edu.monster.transacciones.controllers;

import ec.edu.monster.transacciones.dto.RetiroRequest;
import ec.edu.monster.transacciones.dto.TransferenciaRequest;
import ec.edu.monster.transacciones.services.TransaccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/transacciones")
public class TransaccionController {

    @Autowired
    private TransaccionService service;

    @PostMapping("/retiro")
    public ResponseEntity<?> retiro(@RequestBody RetiroRequest request) {
        service.registrarRetiro(request.getCuenta(), request.getImporte(), request.getEmpleado());
        return ResponseEntity.ok(Map.of("mensaje", "Retiro procesado con éxito"));
    }

    @PostMapping("/transferencia")
    public ResponseEntity<?> transferencia(@RequestBody TransferenciaRequest request) {
        service.registrarTransferencia(request);
        return ResponseEntity.ok(Map.of("mensaje", "Transferencia realizada con éxito"));
    }
}