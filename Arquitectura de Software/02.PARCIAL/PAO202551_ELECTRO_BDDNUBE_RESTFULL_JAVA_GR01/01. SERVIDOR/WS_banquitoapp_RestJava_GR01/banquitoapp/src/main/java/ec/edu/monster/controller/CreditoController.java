package ec.edu.monster.controller;

import ec.edu.monster.dto.PeticionCredito;
import ec.edu.monster.dto.RespuestaCredito;
import ec.edu.monster.dto.RespuestaMonto;
import ec.edu.monster.dto.RespuestaValidacion;
import ec.edu.monster.model.AmortizacionDetalle;
import ec.edu.monster.model.Credito;
import ec.edu.monster.repository.AmortizacionDetalleRepository;
import ec.edu.monster.service.CreditoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/credito")
@CrossOrigin(origins = "*")
public class CreditoController {

    @Autowired
    private CreditoService creditoService;

    @Autowired
    private AmortizacionDetalleRepository amortizacionRepo;

    @GetMapping("/validar/{cedula}")
    public RespuestaValidacion validarSujeto(@PathVariable String cedula) {
        return creditoService.validarSujetoDeCredito(cedula);
    }

    @GetMapping("/monto-maximo/{cedula}")
    public RespuestaMonto calcularMontoMaximo(@PathVariable String cedula) {
        return creditoService.calcularMontoMaximo(cedula);
    }

    @PostMapping("/otorgar")
    public RespuestaCredito otorgarCredito(@RequestBody PeticionCredito peticion) {
        return creditoService.otorgarCredito(peticion);
    }

    @GetMapping("/amortizacion/{idCredito}")
    public List<AmortizacionDetalle> consultarTablaAmortizacion(@PathVariable Integer idCredito) {
        return amortizacionRepo.findAll().stream()
                .filter(a -> a.getCredito().getIdCredito().equals(idCredito))
                .toList();
    }

    // --- NUEVO: Historial de Créditos ---
    @GetMapping("/cliente/{cedula}")
    public List<Credito> verHistorialCreditos(@PathVariable String cedula) {
        return creditoService.listarCreditosPorCliente(cedula);
    }
}