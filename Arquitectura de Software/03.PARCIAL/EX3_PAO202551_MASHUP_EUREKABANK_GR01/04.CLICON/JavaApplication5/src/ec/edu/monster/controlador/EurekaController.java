package ec.edu.monster.controlador;

import ec.edu.monster.modelo.Movimiento;
import ec.edu.monster.servicio.EurekaService;
import java.util.List;

public class EurekaController {

    private final EurekaService service = new EurekaService();

    public List<Movimiento> traerMovimientos(String cuenta) {
        try { return service.traerMovimientos(cuenta); }
        catch (Exception e) { throw new RuntimeException("Error al obtener movimientos: " + e.getMessage()); }
    }

    public void regDeposito(String cuenta, double importe, String codEmp) {
        try { service.regDeposito(cuenta, importe, codEmp); }
        catch (Exception e) { throw new RuntimeException("Error al registrar depósito: " + e.getMessage()); }
    }

    public void regRetiro(String cuenta, double importe, String codEmp) {
        try { service.regRetiro(cuenta, importe, codEmp); }
        catch (Exception e) { throw new RuntimeException("Error al registrar retiro: " + e.getMessage()); }
    }

    public void regTransferencia(String origen, String destino, double importe, String codEmp) {
        try { service.regTransferencia(origen, destino, importe, codEmp); }
        catch (Exception e) { throw new RuntimeException("Error al realizar transferencia: " + e.getMessage()); }
    }

    public double traerSaldo(String cuenta) {
        try { return service.obtenerSaldo(cuenta); }
        catch (Exception e) { throw new RuntimeException("Error al obtener saldo: " + e.getMessage()); }
    }
}
