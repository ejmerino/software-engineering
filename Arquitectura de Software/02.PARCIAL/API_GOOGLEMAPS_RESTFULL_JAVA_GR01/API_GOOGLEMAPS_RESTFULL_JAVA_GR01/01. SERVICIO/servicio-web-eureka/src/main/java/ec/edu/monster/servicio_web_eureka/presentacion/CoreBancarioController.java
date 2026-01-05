package ec.edu.monster.servicio_web_eureka.presentacion;

import ec.edu.monster.servicio_web_eureka.dto.AtmDTO;
import ec.edu.monster.servicio_web_eureka.dto.EstadoAtmDTO;
import ec.edu.monster.servicio_web_eureka.dto.MapaDTO;
import ec.edu.monster.servicio_web_eureka.dto.SucursalDTO;
import ec.edu.monster.servicio_web_eureka.servicio.EurekaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/core")
@CrossOrigin(origins = "*")   // para React / Android
public class CoreBancarioController {

    private final EurekaService eurekaService;

    public CoreBancarioController(EurekaService eurekaService) {
        this.eurekaService = eurekaService;
    }

    // =========================
    // MAPA COMPLETO
    // =========================
    @GetMapping("/mapa")
    public MapaDTO obtenerMapa() {
        return eurekaService.obtenerMapa();
    }

    // =========================
    // CRUD SUCURSALES
    // =========================

    // Listar todas
    @GetMapping("/sucursales")
    public List<SucursalDTO> listarSucursales() {
        return eurekaService.obtenerSucursales();
    }

    // Obtener por ID
    @GetMapping("/sucursales/{id}")
    public SucursalDTO obtenerSucursal(@PathVariable Integer id) {
        return eurekaService.obtenerSucursalPorId(id);
    }

    // Crear
    @PostMapping("/sucursales")
    public SucursalDTO crearSucursal(@RequestBody SucursalDTO sucursalDTO) {
        return eurekaService.crearSucursal(sucursalDTO);
    }

    // Actualizar
    @PutMapping("/sucursales/{id}")
    public SucursalDTO actualizarSucursal(@PathVariable Integer id,
                                          @RequestBody SucursalDTO sucursalDTO) {
        return eurekaService.actualizarSucursal(id, sucursalDTO);
    }

    // Eliminar
    @DeleteMapping("/sucursales/{id}")
    public void eliminarSucursal(@PathVariable Integer id) {
        eurekaService.eliminarSucursal(id);
    }

    // =========================
    // CRUD ATMS
    // =========================

    // Listar todos
    @GetMapping("/atms")
    public List<AtmDTO> listarAtms() {
        return eurekaService.obtenerAtms();
    }

    // Obtener por ID
    @GetMapping("/atms/{id}")
    public AtmDTO obtenerAtm(@PathVariable Long id) {
        return eurekaService.obtenerAtmPorId(id);
    }

    // Crear
    @PostMapping("/atms")
    public AtmDTO crearAtm(@RequestBody AtmDTO atmDTO) {
        return eurekaService.crearAtm(atmDTO);
    }

    // Actualizar
    @PutMapping("/atms/{id}")
    public AtmDTO actualizarAtm(@PathVariable Long id,
                                @RequestBody AtmDTO atmDTO) {
        return eurekaService.actualizarAtm(id, atmDTO);
    }

    // Eliminar
    @DeleteMapping("/atms/{id}")
    public void eliminarAtm(@PathVariable Long id) {
        eurekaService.eliminarAtm(id);
    }

    // =========================
    // CRUD ESTADOS ATM
    // =========================

    // Listar todos
    @GetMapping("/estados-atm")
    public List<EstadoAtmDTO> listarEstadosAtm() {
        return eurekaService.obtenerEstadosAtm();
    }

    // Obtener por ID
    @GetMapping("/estados-atm/{id}")
    public EstadoAtmDTO obtenerEstadoAtm(@PathVariable Long id) {
        return eurekaService.obtenerEstadoAtmPorId(id);
    }

    // Crear
    @PostMapping("/estados-atm")
    public EstadoAtmDTO crearEstadoAtm(@RequestBody EstadoAtmDTO estadoAtmDTO) {
        return eurekaService.crearEstadoAtm(estadoAtmDTO);
    }

    // Actualizar
    @PutMapping("/estados-atm/{id}")
    public EstadoAtmDTO actualizarEstadoAtm(@PathVariable Long id,
                                            @RequestBody EstadoAtmDTO estadoAtmDTO) {
        return eurekaService.actualizarEstadoAtm(id, estadoAtmDTO);
    }

    // Eliminar
    @DeleteMapping("/estados-atm/{id}")
    public void eliminarEstadoAtm(@PathVariable Long id) {
        eurekaService.eliminarEstadoAtm(id);
    }
}
