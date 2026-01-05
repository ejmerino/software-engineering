package ec.edu.monster.service;

import ec.edu.monster.dto.*;
import ec.edu.monster.model.AmortizacionDetalle;
import ec.edu.monster.model.Cliente;
import ec.edu.monster.model.Credito;
import ec.edu.monster.repository.AmortizacionDetalleRepository;
import ec.edu.monster.repository.ClienteRepository;
import ec.edu.monster.repository.CreditoRepository;
import ec.edu.monster.repository.MovimientoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

@Service
public class CreditoService {

    @Autowired private ClienteRepository clienteRepo;
    @Autowired private MovimientoRepository movimientoRepo;
    @Autowired private CreditoRepository creditoRepo;
    @Autowired private AmortizacionDetalleRepository amortizacionRepo;

    public RespuestaValidacion validarSujetoDeCredito(String cedula) {
        Cliente cliente = clienteRepo.findById(cedula).orElse(null);
        if (cliente == null) return new RespuestaValidacion(false, "Rechazado: No es cliente.");

        if (movimientoRepo.contarDepositosUltimoMes(cedula) == 0) {
            return new RespuestaValidacion(false, "Rechazado: Sin depósitos recientes.");
        }

        // --- FIX DE FECHA SEGURO ---
        LocalDate fechaNac = new java.sql.Date(cliente.getFechaNacimiento().getTime()).toLocalDate();

        int edad = Period.between(fechaNac, LocalDate.now()).getYears();
        if ("C".equalsIgnoreCase(cliente.getEstadoCivil()) && edad < 25) {
            return new RespuestaValidacion(false, "Rechazado: Casado menor de 25 años.");
        }

        if (creditoRepo.countCreditosActivos(cedula) > 0) {
            return new RespuestaValidacion(false, "Rechazado: Ya tiene un crédito activo.");
        }

        return new RespuestaValidacion(true, "Aprobado.");
    }

    public RespuestaMonto calcularMontoMaximo(String cedula) {
        RespuestaValidacion val = validarSujetoDeCredito(cedula);
        if (!val.isEsAprobado()) return new RespuestaMonto(false, 0, val.getMensaje());

        double promDep = movimientoRepo.promedioDepositosTrimestre(cedula);
        double promRet = movimientoRepo.promedioRetirosTrimestre(cedula);

        double diferencia = promDep - promRet;
        if (diferencia <= 0) return new RespuestaMonto(true, 0, "Capacidad de pago cero o negativa.");

        double montoMax = (diferencia * 0.60) * 9;
        return new RespuestaMonto(true, montoMax, "Cálculo exitoso.");
    }

    @Transactional
    public RespuestaCredito otorgarCredito(PeticionCredito peticion) {
        RespuestaMonto montoRes = calcularMontoMaximo(peticion.getCedula());

        if (!montoRes.isEsSujetoDeCredito()) return new RespuestaCredito(false, montoRes.getMensaje());
        if (peticion.getPrecioElectrodomestico() > montoRes.getMontoMaximo())
            return new RespuestaCredito(false, "Monto excede el máximo aprobado.");

        double tasaMensual = 0.16 / 12.0;
        double cuota = (peticion.getPrecioElectrodomestico() * tasaMensual) /
                (1 - Math.pow(1 + tasaMensual, -peticion.getNumeroCuotas()));
        BigDecimal cuotaBD = BigDecimal.valueOf(cuota).setScale(2, RoundingMode.HALF_UP);

        Cliente cliente = clienteRepo.findById(peticion.getCedula()).get();
        Credito credito = new Credito();
        credito.setCliente(cliente);
        credito.setMontoPrestamo(BigDecimal.valueOf(peticion.getPrecioElectrodomestico()));
        credito.setTasaInteresAnual(BigDecimal.valueOf(0.16));
        credito.setNumeroCuotas(peticion.getNumeroCuotas());
        credito.setValorCuotaFija(cuotaBD);
        credito.setFechaAprobacion(new Date());
        credito.setEstado("Activo");

        credito = creditoRepo.save(credito);

        double saldoCapital = peticion.getPrecioElectrodomestico();
        LocalDate fechaPago = LocalDate.now().plusMonths(1);

        for (int i = 1; i <= peticion.getNumeroCuotas(); i++) {
            double interes = saldoCapital * tasaMensual;
            double capital = cuotaBD.doubleValue() - interes;
            saldoCapital -= capital;

            if (i == peticion.getNumeroCuotas() && Math.abs(saldoCapital) < 1.0) saldoCapital = 0;

            AmortizacionDetalle detalle = new AmortizacionDetalle();
            detalle.setCredito(credito);
            detalle.setNumeroCuota(i);
            detalle.setFechaPagoProgramada(java.sql.Date.valueOf(fechaPago));
            detalle.setValorCuota(cuotaBD);
            detalle.setInteresPagado(BigDecimal.valueOf(interes));
            detalle.setCapitalPagado(BigDecimal.valueOf(capital));
            detalle.setSaldoCapital(BigDecimal.valueOf(saldoCapital < 0 ? 0 : saldoCapital));

            amortizacionRepo.save(detalle);
            fechaPago = fechaPago.plusMonths(1);
        }

        return new RespuestaCredito(true, "Crédito generado.", credito.getIdCredito());
    }

    // --- NUEVO MÉTODO DE HISTORIAL ---
    public List<Credito> listarCreditosPorCliente(String cedula) {
        return creditoRepo.findByClienteCedula(cedula);
    }
}