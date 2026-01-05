package ec.edu.monster.servicio_web_eureka.servicio;

import ec.edu.monster.servicio_web_eureka.datos.AtmRepositorio;
import ec.edu.monster.servicio_web_eureka.datos.EstadoAtmRepositorio;
import ec.edu.monster.servicio_web_eureka.datos.SucursalRepositorio;
import ec.edu.monster.servicio_web_eureka.dto.AtmDTO;
import ec.edu.monster.servicio_web_eureka.dto.EstadoAtmDTO;
import ec.edu.monster.servicio_web_eureka.dto.MapaDTO;
import ec.edu.monster.servicio_web_eureka.dto.SucursalDTO;
import ec.edu.monster.servicio_web_eureka.modelo.Atm;
import ec.edu.monster.servicio_web_eureka.modelo.EstadoAtm;
import ec.edu.monster.servicio_web_eureka.modelo.Sucursal;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EurekaService {

    private final SucursalRepositorio sucursalRepo;
    private final AtmRepositorio atmRepo;
    private final EstadoAtmRepositorio estadoRepo;

    public EurekaService(SucursalRepositorio sucursalRepo,
                         AtmRepositorio atmRepo,
                         EstadoAtmRepositorio estadoRepo) {
        this.sucursalRepo = sucursalRepo;
        this.atmRepo = atmRepo;
        this.estadoRepo = estadoRepo;
    }

    // =========================
    // MAPA COMPLETO
    // =========================
    public MapaDTO obtenerMapa() {
        List<SucursalDTO> sucursales = obtenerSucursales();
        List<AtmDTO> atms = obtenerAtms();
        List<EstadoAtmDTO> estados = obtenerEstadosAtm();

        MapaDTO mapa = new MapaDTO();
        mapa.setSucursales(sucursales);
        mapa.setAtms(atms);
        mapa.setEstadosAtm(estados);
        return mapa;
    }

    // =========================
    // LECTURAS SUCURSAL
    // =========================
    public List<SucursalDTO> obtenerSucursales() {
        return sucursalRepo.findAll()
                .stream()
                .map(this::toSucursalDTO)
                .collect(Collectors.toList());
    }

    public SucursalDTO obtenerSucursalPorId(Integer id) {
        Sucursal sucursal = sucursalRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Sucursal no encontrada con id: " + id));
        return toSucursalDTO(sucursal);
    }

    // =========================
    // CREAR / ACTUALIZAR / ELIMINAR SUCURSAL
    // =========================
    public SucursalDTO crearSucursal(SucursalDTO dto) {
        Sucursal sucursal = new Sucursal();
        sucursal.setNombre(dto.getNombre());
        sucursal.setCiudad(dto.getCiudad());
        sucursal.setDireccion(dto.getDireccion());
        sucursal.setLatitud(dto.getLatitud());
        sucursal.setLongitud(dto.getLongitud());
        sucursal.setContadorCuenta(0); // inicial

        sucursal = sucursalRepo.save(sucursal);
        return toSucursalDTO(sucursal);
    }

    public SucursalDTO actualizarSucursal(Integer id, SucursalDTO dto) {
        Sucursal sucursal = sucursalRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Sucursal no encontrada con id: " + id));

        sucursal.setNombre(dto.getNombre());
        sucursal.setCiudad(dto.getCiudad());
        sucursal.setDireccion(dto.getDireccion());
        sucursal.setLatitud(dto.getLatitud());
        sucursal.setLongitud(dto.getLongitud());

        sucursal = sucursalRepo.save(sucursal);
        return toSucursalDTO(sucursal);
    }

    public void eliminarSucursal(Integer id) {
        if (!sucursalRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Sucursal no encontrada con id: " + id);
        }
        sucursalRepo.deleteById(id);
    }

    // =========================
    // LECTURAS ATM
    // =========================
    public List<AtmDTO> obtenerAtms() {
        return atmRepo.findAll()
                .stream()
                .map(this::toAtmDTO)
                .collect(Collectors.toList());
    }

    public AtmDTO obtenerAtmPorId(Long id) {
        Atm atm = atmRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "ATM no encontrado con id: " + id));
        return toAtmDTO(atm);
    }

    // =========================
    // CREAR / ACTUALIZAR / ELIMINAR ATM
    // =========================
    public AtmDTO crearAtm(AtmDTO dto) {
        Atm atm = new Atm();
        atm.setNombre(dto.getNombre());
        atm.setDireccion(dto.getDireccion());
        atm.setLatitud(dto.getLatitud());
        atm.setLongitud(dto.getLongitud());
        atm.setEstado(dto.getEstado());

        atm = atmRepo.save(atm);
        return toAtmDTO(atm);
    }

    public AtmDTO actualizarAtm(Long id, AtmDTO dto) {
        Atm atm = atmRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "ATM no encontrado con id: " + id));

        atm.setNombre(dto.getNombre());
        atm.setDireccion(dto.getDireccion());
        atm.setLatitud(dto.getLatitud());
        atm.setLongitud(dto.getLongitud());
        atm.setEstado(dto.getEstado());

        atm = atmRepo.save(atm);
        return toAtmDTO(atm);
    }

    public void eliminarAtm(Long id) {
        if (!atmRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "ATM no encontrado con id: " + id);
        }
        atmRepo.deleteById(id);
    }

    // =========================
    // LECTURAS ESTADO ATM
    // =========================
    public List<EstadoAtmDTO> obtenerEstadosAtm() {
        return estadoRepo.findAll()
                .stream()
                .map(this::toEstadoAtmDTO)
                .collect(Collectors.toList());
    }

    public EstadoAtmDTO obtenerEstadoAtmPorId(Long id) {
        EstadoAtm estado = estadoRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Estado de ATM no encontrado con id: " + id));
        return toEstadoAtmDTO(estado);
    }

    // =========================
    // CREAR / ACTUALIZAR / ELIMINAR ESTADO ATM
    // =========================
    public EstadoAtmDTO crearEstadoAtm(EstadoAtmDTO dto) {
        Atm atm = atmRepo.findById(dto.getIdAtm())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "ATM no encontrado con id: " + dto.getIdAtm()));

        EstadoAtm estado = new EstadoAtm();
        estado.setAtm(atm);
        estado.setEstado(dto.getEstado());

        LocalDateTime fecha = dto.getFechaRegistro() != null
                ? dto.getFechaRegistro()
                : LocalDateTime.now();
        estado.setFechaRegistro(fecha);

        estado = estadoRepo.save(estado);
        return toEstadoAtmDTO(estado);
    }

    public EstadoAtmDTO actualizarEstadoAtm(Long id, EstadoAtmDTO dto) {
        EstadoAtm estado = estadoRepo.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Estado de ATM no encontrado con id: " + id));

        if (dto.getEstado() != null) {
            estado.setEstado(dto.getEstado());
        }
        if (dto.getFechaRegistro() != null) {
            estado.setFechaRegistro(dto.getFechaRegistro());
        }

        estado = estadoRepo.save(estado);
        return toEstadoAtmDTO(estado);
    }

    public void eliminarEstadoAtm(Long id) {
        if (!estadoRepo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Estado de ATM no encontrado con id: " + id);
        }
        estadoRepo.deleteById(id);
    }

    // =========================
    // MAPEOS ENTITY -> DTO
    // =========================
    private SucursalDTO toSucursalDTO(Sucursal s) {
        SucursalDTO dto = new SucursalDTO();
        dto.setId(s.getIdSucursal());
        dto.setNombre(s.getNombre());
        dto.setCiudad(s.getCiudad());
        dto.setDireccion(s.getDireccion());
        dto.setLatitud(s.getLatitud());
        dto.setLongitud(s.getLongitud());
        return dto;
    }

    private AtmDTO toAtmDTO(Atm a) {
        AtmDTO dto = new AtmDTO();
        dto.setId(a.getIdAtm());
        dto.setNombre(a.getNombre());
        dto.setDireccion(a.getDireccion());
        dto.setLatitud(a.getLatitud());
        dto.setLongitud(a.getLongitud());
        dto.setEstado(a.getEstado());
        return dto;
    }

    private EstadoAtmDTO toEstadoAtmDTO(EstadoAtm e) {
        EstadoAtmDTO dto = new EstadoAtmDTO();
        dto.setIdEstado(e.getIdEstado());
        dto.setIdAtm(e.getAtm().getIdAtm());
        dto.setFechaRegistro(e.getFechaRegistro());
        dto.setEstado(e.getEstado());
        return dto;
    }
}
