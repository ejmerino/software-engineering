package ec.edu.monster.service;

import ec.edu.monster.model.Electrodomestico; 
import ec.edu.monster.model.Factura;
import ec.edu.monster.model.dto.ItemFactura;
import ec.edu.monster.model.dto.PeticionFactura;
import ec.edu.monster.model.dto.RespuestaCredito;
import ec.edu.monster.model.dto.RespuestaMonto;
import ec.edu.monster.model.dto.RespuestaFacturacion;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationScoped
public class FacturacionService {

    private final String URL = "jdbc:mariadb://localhost:3307/db_comercializadora?useGSSAPI=false";
    private final String USER = "root";
    private final String PASSWORD = "1234"; 

    @Inject
    private BanQuitoClienteService banquitoCliente;

    public void setBanquitoCliente(BanQuitoClienteService banquitoCliente) {
        this.banquitoCliente = banquitoCliente;
    }
    
    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Lógica para el Punto 9
     */
    public RespuestaFacturacion procesarFactura(PeticionFactura peticion) {
        
        Connection con = null;
        BigDecimal subtotal = new BigDecimal(0);
        BigDecimal descuento = new BigDecimal(0);
        BigDecimal totalPagar = BigDecimal.ZERO; 
        int idCreditoBanco = 0;
        
        Factura facturaGenerada = new Factura();
        
        try {
            con = getConnection();
            con.setAutoCommit(false); 
            
            // 1. Calcular el total y verificar STOCK
            for (ItemFactura item : peticion.getItems()) {
                Electrodomestico producto = getProductoParaVenta(con, item.getIdElectrodomestico());
                
                if (producto == null) {
                    return new RespuestaFacturacion("Error: Producto con ID " + item.getIdElectrodomestico() + " no existe.");
                }
                if (item.getCantidad() > producto.getStock()) {
                    return new RespuestaFacturacion("Error: Stock insuficiente. Solo quedan " + producto.getStock() + " unidades de " + producto.getNombre());
                }
                
                // Suma el total y registra los detalles
                BigDecimal precioItem = producto.getPrecioVenta();
                BigDecimal subtotalItem = precioItem.multiply(new BigDecimal(item.getCantidad()));
                subtotal = subtotal.add(subtotalItem).setScale(2, RoundingMode.HALF_UP);
                
                item.setPrecioUnitario(precioItem); 
            }

            // 2. Lógica de Forma de Pago y Validación de Crédito
            if ("Efectivo".equalsIgnoreCase(peticion.getFormaPago())) {
                descuento = subtotal.multiply(new BigDecimal(0.33)).setScale(2, RoundingMode.HALF_UP);
                totalPagar = subtotal.subtract(descuento);

            } else if ("Credito".equalsIgnoreCase(peticion.getFormaPago())) {
                totalPagar = subtotal; 

                // Validación 1: Sujeto de Crédito y Monto
                RespuestaMonto montoMax = banquitoCliente.consultarMontoMaximo(peticion.getCedulaCliente());
                
                if (!montoMax.isEsSujetoDeCredito()) {
                    return new RespuestaFacturacion("Crédito rechazado: " + montoMax.getMensaje());
                }
                if (subtotal.doubleValue() > montoMax.getMontoMaximo()) {
                    return new RespuestaFacturacion("Crédito rechazado: El precio (USD " + subtotal + ") supera el monto máximo aprobado (USD " + montoMax.getMontoMaximo() + ").");
                }
                
                // Validación 2: Otorgar Crédito
                RespuestaCredito cred = banquitoCliente.solicitarCredito(peticion.getCedulaCliente(), subtotal.doubleValue(), peticion.getNumeroCuotas());
                if (!cred.isCreditoAprobado()) {
                    return new RespuestaFacturacion("Error: BanQuito no pudo generar el crédito. Razón: " + cred.getMensaje());
                }
                idCreditoBanco = cred.getIdCreditoGenerado();
                
            } else {
                return new RespuestaFacturacion("Forma de pago no válida.");
            }

            // 3. GUARDAR LA FACTURA (Cabecera)
            String sqlFactura = "INSERT INTO Factura (cedula_cliente, fecha, forma_pago, subtotal, descuento, total, id_credito_banco) VALUES (?, CURDATE(), ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstFactura = con.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS)) {
                pstFactura.setString(1, peticion.getCedulaCliente());
                pstFactura.setString(2, peticion.getFormaPago());
                pstFactura.setBigDecimal(3, subtotal);
                pstFactura.setBigDecimal(4, descuento);
                pstFactura.setBigDecimal(5, totalPagar);
                pstFactura.setInt(6, idCreditoBanco);
                pstFactura.executeUpdate();
                
                // Obtener el ID generado
                try (ResultSet rsKeys = pstFactura.getGeneratedKeys()) {
                    if (rsKeys.next()) {
                        facturaGenerada.setIdFactura(rsKeys.getInt(1));
                    } else {
                         throw new SQLException("Fallo al obtener ID de factura.");
                    }
                }
            }

            // 4. GUARDAR DETALLES Y ACTUALIZAR STOCK (Transaccional)
            String sqlDetalle = "INSERT INTO Factura_Detalle (id_factura, id_electrodomestico, cantidad, precio_unitario_venta, subtotal_linea) VALUES (?, ?, ?, ?, ?)";
            String sqlUpdateStock = "UPDATE Electrodomestico SET stock = stock - ? WHERE id_electrodomestico = ?";
            
            try (PreparedStatement pstDetalle = con.prepareStatement(sqlDetalle);
                 PreparedStatement pstStock = con.prepareStatement(sqlUpdateStock)) {
                
                for (ItemFactura item : peticion.getItems()) {
                    
                    // Detalle
                    BigDecimal precio = item.getPrecioUnitario();
                    BigDecimal subtotalLinea = precio.multiply(new BigDecimal(item.getCantidad()));
                    
                    pstDetalle.setInt(1, facturaGenerada.getIdFactura());
                    pstDetalle.setInt(2, item.getIdElectrodomestico());
                    pstDetalle.setInt(3, item.getCantidad());
                    pstDetalle.setBigDecimal(4, precio);
                    pstDetalle.setBigDecimal(5, subtotalLinea);
                    pstDetalle.addBatch();
                    
                    // Stock
                    pstStock.setInt(1, item.getCantidad());
                    pstStock.setInt(2, item.getIdElectrodomestico());
                    pstStock.addBatch();
                }
                
                pstDetalle.executeBatch();
                pstStock.executeBatch(); 
            }

            // 5. OBTENER LA FECHA Y ASIGNAR TODOS LOS CAMPOS AL POJO QUE SE DEVUELVE (CORRECCIÓN FINAL)
            
            // Obtener la Fecha
            try (PreparedStatement pstFecha = con.prepareStatement("SELECT fecha FROM Factura WHERE id_factura = ?")) {
                pstFecha.setInt(1, facturaGenerada.getIdFactura());
                try (ResultSet rsFecha = pstFecha.executeQuery()) {
                    if (rsFecha.next()) {
                         facturaGenerada.setFecha(rsFecha.getDate("fecha"));
                    }
                }
            }
            
            // ************ ASIGNACIÓN CRÍTICA ************
            // ESTA ASIGNACIÓN FINAL COPIA LOS DATOS CALCULADOS AL POJO PARA EL JSON DE RETORNO.
            facturaGenerada.setCedulaCliente(peticion.getCedulaCliente());
            facturaGenerada.setFormaPago(peticion.getFormaPago());
            facturaGenerada.setSubtotal(subtotal);
            facturaGenerada.setDescuento(descuento);
            facturaGenerada.setTotal(totalPagar);
            facturaGenerada.setIdCreditoBanco(idCreditoBanco);

            con.commit();
            return new RespuestaFacturacion(facturaGenerada); 

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (SQLException ex) {}
            return new RespuestaFacturacion("Error de sistema: " + e.getMessage()); 
        } finally {
            try { if (con != null) con.close(); } catch (SQLException e) {}
        }
    }
    
    // --- MÉTODOS AUXILIARES ---
    
    private Electrodomestico getProductoParaVenta(Connection con, int id) throws SQLException {
        String sql = "SELECT id_electrodomestico, nombre, descripcion, precio_venta, ruta_imagen, stock FROM Electrodomestico WHERE id_electrodomestico = ?";
        try (PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Electrodomestico e = new Electrodomestico();
                    e.setIdElectrodomestico(rs.getInt("id_electrodomestico"));
                    e.setNombre(rs.getString("nombre"));
                    e.setDescripcion(rs.getString("descripcion"));
                    e.setPrecioVenta(rs.getBigDecimal("precio_venta"));
                    e.setRutaImagen(rs.getString("ruta_imagen"));
                    e.setStock(rs.getInt("stock")); 
                    return e;
                } else {
                    return null;
                }
            }
        }
    }
    
    private BigDecimal getPrecioElectrodomestico(Connection con, int id) throws SQLException {
        Electrodomestico e = getProductoParaVenta(con, id);
        return (e != null) ? e.getPrecioVenta() : BigDecimal.ZERO;
    }
    
    public Factura getFacturaById(int idFactura) throws Exception {
        String sql = "SELECT id_factura, cedula_cliente, fecha, forma_pago, subtotal, descuento, total, id_credito_banco FROM Factura WHERE id_factura = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, idFactura);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Factura f = new Factura();
                    f.setIdFactura(rs.getInt("id_factura"));
                    f.setCedulaCliente(rs.getString("cedula_cliente"));
                    f.setFecha(rs.getDate("fecha"));
                    f.setFormaPago(rs.getString("forma_pago"));
                    f.setSubtotal(rs.getBigDecimal("subtotal"));
                    f.setDescuento(rs.getBigDecimal("descuento"));
                    f.setTotal(rs.getBigDecimal("total"));
                    f.setIdCreditoBanco(rs.getInt("id_credito_banco"));
                    return f;
                } else {
                    return null;
                }
            }
        }
    }
}