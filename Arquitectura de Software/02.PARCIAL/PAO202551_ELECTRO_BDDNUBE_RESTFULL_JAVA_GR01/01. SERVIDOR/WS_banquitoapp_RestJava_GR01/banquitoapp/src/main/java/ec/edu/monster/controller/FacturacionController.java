package ec.edu.monster.controller;

import ec.edu.monster.dto.PeticionFactura;
import ec.edu.monster.dto.RespuestaFacturacion;
import ec.edu.monster.model.Factura;
import ec.edu.monster.service.FacturacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/facturacion")
@CrossOrigin(origins = "*")
public class FacturacionController {

    @Autowired
    private FacturacionService facturacionService;

    @PostMapping("/vender")
    public RespuestaFacturacion procesarVenta(@RequestBody PeticionFactura peticion) {
        return facturacionService.procesarFactura(peticion);
    }

    // --- NUEVO: Historial de Compras ---
    @GetMapping("/cliente/{cedula}")
    public List<Factura> verHistorialCompras(@PathVariable String cedula) {
        return facturacionService.listarFacturasPorCliente(cedula);
    }

    @GetMapping("/{id}")
    public Factura buscarPorId(@PathVariable Integer id) {
        return facturacionService.buscarFacturaPorId(id);
    }
}