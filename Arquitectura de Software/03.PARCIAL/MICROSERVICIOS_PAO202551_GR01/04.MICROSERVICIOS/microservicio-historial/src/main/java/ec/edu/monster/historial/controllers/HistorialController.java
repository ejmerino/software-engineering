package ec.edu.monster.historial.controllers;

import ec.edu.monster.historial.dtos.MovimientoDTO;
import ec.edu.monster.historial.services.HistorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/historial")
public class HistorialController {

    @Autowired
    private HistorialService service;

    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<MovimientoDTO>> consultarHistorial(@PathVariable String idCuenta) {
        // Al devolver DTOs, evitamos problemas de serialización de entidades pesadas
        return ResponseEntity.ok(service.obtenerHistorial(idCuenta));
    }
}