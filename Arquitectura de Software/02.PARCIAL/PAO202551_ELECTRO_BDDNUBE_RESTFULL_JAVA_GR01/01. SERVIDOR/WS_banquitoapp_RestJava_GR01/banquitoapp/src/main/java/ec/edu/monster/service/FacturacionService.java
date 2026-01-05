package ec.edu.monster.service;

import ec.edu.monster.dto.*;
import ec.edu.monster.model.*;
import ec.edu.monster.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service
public class FacturacionService {

    @Autowired private ElectrodomesticoRepository electroRepo;
    @Autowired private FacturaRepository facturaRepo;
    // 1. INYECCIÓN NUEVA:
    @Autowired private FacturaDetalleRepository facturaDetalleRepo;

    @Autowired private ClienteRepository clienteRepo;
    @Autowired private CreditoService creditoService;

    @Transactional
    public RespuestaFacturacion procesarFactura(PeticionFactura peticion) {
        BigDecimal subtotal = BigDecimal.ZERO;

        // Validar Stock y calcular subtotal
        for (ItemFactura item : peticion.getItems()) {
            Electrodomestico prod = electroRepo.findById(item.getIdElectrodomestico())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            if (prod.getStock() < item.getCantidad()) {
                return new RespuestaFacturacion("Stock insuficiente para: " + prod.getNombre());
            }
            subtotal = subtotal.add(prod.getPrecioVenta().multiply(BigDecimal.valueOf(item.getCantidad())));
        }

        BigDecimal total = subtotal;
        BigDecimal descuento = BigDecimal.ZERO;
        int idCredito = 0;

        // Descuentos
        if ("Efectivo".equalsIgnoreCase(peticion.getFormaPago())) {
            descuento = subtotal.multiply(BigDecimal.valueOf(0.33)); // 33%
            total = subtotal.subtract(descuento);
        } else if ("Credito".equalsIgnoreCase(peticion.getFormaPago())) {
            PeticionCredito petCredito = new PeticionCredito();
            petCredito.setCedula(peticion.getCedulaCliente());
            petCredito.setPrecioElectrodomestico(subtotal.doubleValue());
            petCredito.setNumeroCuotas(peticion.getNumeroCuotas());

            RespuestaCredito resCredito = creditoService.otorgarCredito(petCredito);

            if (!resCredito.isCreditoAprobado()) {
                return new RespuestaFacturacion("Crédito denegado: " + resCredito.getMensaje());
            }
            idCredito = resCredito.getIdCreditoGenerado();
        }

        // Guardar Factura (Cabecera)
        Factura factura = new Factura();
        Cliente cliente = clienteRepo.findById(peticion.getCedulaCliente()).orElseThrow();
        factura.setCliente(cliente);
        factura.setFecha(new Date());
        factura.setFormaPago(peticion.getFormaPago());
        factura.setSubtotal(subtotal);
        factura.setDescuento(descuento);
        factura.setTotal(total);
        factura.setIdCreditoBanco(idCredito);

        factura = facturaRepo.save(factura); // Obtenemos el ID de la factura

        // Guardar Detalles (Items)
        for (ItemFactura item : peticion.getItems()) {
            Electrodomestico prod = electroRepo.findById(item.getIdElectrodomestico()).get();

            // Bajar Stock
            prod.setStock(prod.getStock() - item.getCantidad());
            electroRepo.save(prod);

            FacturaDetalle detalle = new FacturaDetalle();
            detalle.setFactura(factura); // Vinculamos con la factura guardada
            detalle.setElectrodomestico(prod);
            detalle.setCantidad(item.getCantidad());
            detalle.setPrecioUnitarioVenta(prod.getPrecioVenta());
            detalle.setSubtotalLinea(prod.getPrecioVenta().multiply(BigDecimal.valueOf(item.getCantidad())));

            // 2. GUARDADO EXPLÍCITO: ¡Esto es lo que faltaba!
            facturaDetalleRepo.save(detalle);
        }

        // Refrescamos la factura para que incluya la lista de detalles recién guardados al devolverla
        // (Opcional, pero ayuda a que el cliente reciba la lista llena de inmediato)
        factura = facturaRepo.findById(factura.getIdFactura()).get();

        return new RespuestaFacturacion(factura);
    }

    public List<Factura> listarFacturasPorCliente(String cedula) {
        return facturaRepo.findByClienteCedula(cedula);
    }

    public Factura buscarFacturaPorId(Integer id) {
        return facturaRepo.findById(id).orElse(null);
    }
}