package ec.edu.monster.service;

import ec.edu.monster.model.dto.PeticionCredito;
import ec.edu.monster.model.dto.RespuestaCredito;
import ec.edu.monster.model.AmortizacionDetalle; 
import ec.edu.monster.model.dto.AmortizacionDetalleDTO;
import java.util.ArrayList;// La entidad
import java.util.List; // Para devolver una lista
import java.math.BigDecimal; // Para cálculos de dinero precisos
import java.math.RoundingMode;
import java.sql.Statement;
import ec.edu.monster.model.dto.RespuestaMonto;
import ec.edu.monster.model.dto.RespuestaValidacion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CreditoService {

    // ¡¡YA NO USAMOS @PersistenceContext NI EntityManager!!

    // --- CONEXIÓN JDBC ---
    // Usamos los datos de tu prueba exitosa
    private final String URL = "jdbc:mariadb://localhost:3307/db_banquito";
    private final String USER = "root";
    private final String PASSWORD = "1234"; // <-- CAMBIA ESTO

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        // Asegúrate de tener el driver (v2.7.10) en tus Libraries
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    /**
     * Lógica para el Punto 5 (CON JDBC)
     */
    public RespuestaValidacion validarSujetoDeCredito(String cedula) {
        
        // REGLA 1: Verificar si es cliente
        String estadoCivil = null;
        Date fechaNac = null;
        
        String sqlRule1 = "SELECT ESTADO_CIVIL, FECHA_NACIMIENTO FROM CLIENTE WHERE CEDULA = ?";
        
        try (Connection con = getConnection();
             PreparedStatement pstRule1 = con.prepareStatement(sqlRule1)) {
            
            pstRule1.setString(1, cedula);
            try (ResultSet rs = pstRule1.executeQuery()) {
                if (!rs.next()) {
                    return new RespuestaValidacion(false, "Rechazado: El solicitante no es cliente del banco.");
                }
                estadoCivil = rs.getString("ESTADO_CIVIL");
                fechaNac = rs.getDate("FECHA_NACIMIENTO");
            }
        } catch (Exception e) {
            return new RespuestaValidacion(false, "Error en Regla 1 (Cliente): " + e.getMessage());
        }

        // REGLA 2: Verificar depósito en el último mes
        String sqlRule2 = "SELECT COUNT(*) FROM MOVIMIENTO M " +
                          "JOIN CUENTA C ON M.NUM_CUENTA = C.NUM_CUENTA " +
                          "WHERE C.CEDULA = ? AND M.TIPO = 'DEP' AND M.FECHA >= (CURDATE() - INTERVAL 1 MONTH)";
        
        try (Connection con = getConnection();
             PreparedStatement pstRule2 = con.prepareStatement(sqlRule2)) {
            
            pstRule2.setString(1, cedula);
            try (ResultSet rs = pstRule2.executeQuery()) {
                rs.next();
                if (rs.getInt(1) == 0) {
                    return new RespuestaValidacion(false, "Rechazado: El cliente no registra depósitos en el último mes.");
                }
            }
        } catch (Exception e) {
            return new RespuestaValidacion(false, "Error en Regla 2 (Depósito): " + e.getMessage());
        }

        // REGLA 3: Verificar edad y estado civil
        LocalDate fechaNacimientoLD = new java.sql.Date(fechaNac.getTime()).toLocalDate();
        int edad = Period.between(fechaNacimientoLD, LocalDate.now()).getYears();

        if ("C".equalsIgnoreCase(estadoCivil) && edad < 25) {
             return new RespuestaValidacion(false, "Rechazado: El cliente es casado y menor de 25 años.");
        }

        // REGLA 4: Verificar que no tenga créditos activos
        String sqlRule4 = "SELECT COUNT(*) FROM Credito WHERE cedula_cliente = ? AND estado = 'Activo'";
        
        try (Connection con = getConnection();
             PreparedStatement pstRule4 = con.prepareStatement(sqlRule4)) {
            
            pstRule4.setString(1, cedula);
            try (ResultSet rs = pstRule4.executeQuery()) {
                rs.next();
                if (rs.getInt(1) > 0) {
                    return new RespuestaValidacion(false, "Rechazado: El cliente ya tiene un crédito activo.");
                }
            }
        } catch (Exception e) {
            return new RespuestaValidacion(false, "Error en Regla 4 (Crédito Activo): " + e.getMessage());
        }

        // APROBADO:
        return new RespuestaValidacion(true, "Aprobado: El cliente es sujeto de crédito.");
    }

    /**
     * Lógica para el Punto 6 (CON JDBC)
     */
    public RespuestaMonto calcularMontoMaximo(String cedula) {
        
        RespuestaValidacion validacion = this.validarSujetoDeCredito(cedula);
        if (!validacion.isEsAprobado()) {
            return new RespuestaMonto(false, 0, validacion.getMensaje());
        }

        double promDepositos = 0;
        double promRetiros = 0;
        
        String sqlDepositos = "SELECT AVG(m.VALOR) FROM MOVIMIENTO m " +
                              "JOIN CUENTA c ON m.NUM_CUENTA = c.NUM_CUENTA " +
                              "WHERE c.CEDULA = ? AND m.TIPO = 'DEP' AND m.FECHA >= (CURDATE() - INTERVAL 3 MONTH)";
        
        String sqlRetiros = "SELECT AVG(m.VALOR) FROM MOVIMIENTO m " +
                            "JOIN CUENTA c ON m.NUM_CUENTA = c.NUM_CUENTA " +
                            "WHERE c.CEDULA = ? AND m.TIPO = 'RET' AND m.FECHA >= (CURDATE() - INTERVAL 3 MONTH)";

        try (Connection con = getConnection();
             PreparedStatement pstDep = con.prepareStatement(sqlDepositos);
             PreparedStatement pstRet = con.prepareStatement(sqlRetiros)) {
            
            pstDep.setString(1, cedula);
            try (ResultSet rsDep = pstDep.executeQuery()) {
                if (rsDep.next()) {
                    promDepositos = rsDep.getDouble(1); // AVG devuelve 0 si no hay filas (o null, getDouble lo maneja)
                }
            }
            
            pstRet.setString(1, cedula);
            try (ResultSet rsRet = pstRet.executeQuery()) {
                if (rsRet.next()) {
                    promRetiros = rsRet.getDouble(1);
                }
            }
            
            double diferencia = promDepositos - promRetiros;
            if (diferencia <= 0) {
                return new RespuestaMonto(true, 0, "Cálculo exitoso. El monto máximo es 0.");
            }
            
            double montoMaximo = (diferencia * 0.60) * 9;
            return new RespuestaMonto(true, montoMaximo, "Cálculo de monto máximo exitoso.");

        } catch (Exception e) {
            return new RespuestaMonto(false, 0, "Error al calcular el monto: " + e.getMessage());
        }
    }
    
    /**
     * Lógica para el Punto 7
     * Otorga un crédito y genera la tabla de amortización.
     */
    public RespuestaCredito otorgarCredito(PeticionCredito peticion) {
        
        // 1. VALIDACIONES INICIALES
        // Reutilizamos la lógica del Punto 6
        RespuestaMonto montoRes = calcularMontoMaximo(peticion.getCedula());
        if (!montoRes.isEsSujetoDeCredito()) {
            return new RespuestaCredito(false, "Rechazado: " + montoRes.getMensaje());
        }

        if (peticion.getNumeroCuotas() < 3 || peticion.getNumeroCuotas() > 24) {
            return new RespuestaCredito(false, "Rechazado: El número de cuotas debe estar entre 3 y 24.");
        }

        if (peticion.getPrecioElectrodomestico() > montoRes.getMontoMaximo()) {
            return new RespuestaCredito(false, "Rechazado: El precio (USD " + peticion.getPrecioElectrodomestico() + 
                                               ") supera el monto máximo aprobado (USD " + montoRes.getMontoMaximo() + ").");
        }

        // 2. CÁLCULO DE LA CUOTA FIJA (Fórmula de la imagen)
        // Tasa de Interés Anual = 16% (0.16)
        // TasaPeríodo (Tasa Mensual) = (16% / 12) = 0.013333...
        double tasaInteresMensual = 0.16 / 12.0;
        double montoPrestamo = peticion.getPrecioElectrodomestico();
        int numCuotas = peticion.getNumeroCuotas();

        // Cuota = (Monto * Tasa) / (1 - (1 + Tasa)^-NumCuotas)
        double cuotaFija = (montoPrestamo * tasaInteresMensual) / 
                           (1 - Math.pow(1 + tasaInteresMensual, -numCuotas));
        
        // Redondear a 2 decimales para dinero
        BigDecimal cuotaFijaBD = new BigDecimal(cuotaFija).setScale(2, RoundingMode.HALF_UP);
        cuotaFija = cuotaFijaBD.doubleValue();

        Connection con = null;
        try {
            con = getConnection();
            // INICIAMOS LA TRANSACCIÓN
            con.setAutoCommit(false); 

            // 3. GUARDAR EL CRÉDITO (Tabla `Credito`)
            String sqlInsertCredito = "INSERT INTO Credito (cedula_cliente, monto_prestamo, tasa_interes_anual, numero_cuotas, valor_cuota_fija, fecha_aprobacion, estado) " +
                                      "VALUES (?, ?, 0.16, ?, ?, CURDATE(), 'Activo')";
            
            int idCreditoGenerado;
            
            try (PreparedStatement pstCredito = con.prepareStatement(sqlInsertCredito, Statement.RETURN_GENERATED_KEYS)) {
                pstCredito.setString(1, peticion.getCedula());
                pstCredito.setDouble(2, montoPrestamo);
                pstCredito.setInt(3, numCuotas);
                pstCredito.setDouble(4, cuotaFija);
                pstCredito.executeUpdate();
                
                try (ResultSet generatedKeys = pstCredito.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        idCreditoGenerado = generatedKeys.getInt(1);
                    } else {
                        throw new SQLException("Fallo al crear el crédito, no se obtuvo ID.");
                    }
                }
            }

            // 4. GENERAR Y GUARDAR TABLA DE AMORTIZACIÓN (Tabla `Amortizacion_Detalle`)
            String sqlInsertAmortizacion = "INSERT INTO Amortizacion_Detalle (id_credito, numero_cuota, fecha_pago_programada, valor_cuota, interes_pagado, capital_pagado, saldo_capital) " +
                                           "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement pstAmortizacion = con.prepareStatement(sqlInsertAmortizacion)) {
                
                double saldoCapital = montoPrestamo;
                LocalDate fechaPago = LocalDate.now().plusMonths(1); // Primer pago el próximo mes

                for (int i = 1; i <= numCuotas; i++) {
                    double interesPagado = new BigDecimal(saldoCapital * tasaInteresMensual).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    double capitalPagado = new BigDecimal(cuotaFija - interesPagado).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    saldoCapital = new BigDecimal(saldoCapital - capitalPagado).setScale(2, RoundingMode.HALF_UP).doubleValue();
                    
                    // Ajuste en la última cuota para que el saldo sea 0 exacto
                    if (i == numCuotas && saldoCapital != 0) {
                        capitalPagado = capitalPagado + saldoCapital;
                        saldoCapital = 0;
                    }

                    pstAmortizacion.setInt(1, idCreditoGenerado);
                    pstAmortizacion.setInt(2, i); // numero_cuota
                    pstAmortizacion.setDate(3, java.sql.Date.valueOf(fechaPago));
                    pstAmortizacion.setDouble(4, cuotaFija);
                    pstAmortizacion.setDouble(5, interesPagado);
                    pstAmortizacion.setDouble(6, capitalPagado);
                    pstAmortizacion.setDouble(7, saldoCapital);
                    
                    pstAmortizacion.addBatch(); // Añadimos al lote
                    
                    fechaPago = fechaPago.plusMonths(1); // Siguiente fecha de pago
                }
                pstAmortizacion.executeBatch(); // Ejecutamos todas las inserciones
            }
            
            // SI TODO FUE BIEN, CONFIRMAMOS LA TRANSACCIÓN
            con.commit(); 
            
            return new RespuestaCredito(true, "Crédito aprobado y generado exitosamente.", idCreditoGenerado);

        } catch (Exception e) {
            // SI ALGO FALLÓ, REVERTIMOS TODO
            try {
                if (con != null) con.rollback();
            } catch (SQLException ex) {
                // Error al hacer rollback
            }
            return new RespuestaCredito(false, "Error transaccional al generar el crédito: " + e.getMessage());
        } finally {
            try {
                if (con != null) con.close();
            } catch (SQLException e) {
                // Error al cerrar
            }
        }
    }
    /**
     * Lógica para el Punto 10
     * Devuelve la tabla de amortización para un crédito dado.
     * * Nota: Por simplicidad del examen, devolvemos las entidades JPA.
     * En un caso real, usaríamos DTOs.
     */
    public List<AmortizacionDetalleDTO> consultarTablaAmortizacion(int idCredito) throws Exception {
        
        List<AmortizacionDetalleDTO> tabla = new ArrayList<>(); // Usamos el DTO
        String sql = "SELECT * FROM Amortizacion_Detalle WHERE id_credito = ? ORDER BY numero_cuota ASC";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            
            pst.setInt(1, idCredito);
            
            try (ResultSet rs = pst.executeQuery()) {
                while (rs.next()) {
                    AmortizacionDetalleDTO detalle = new AmortizacionDetalleDTO(); // Creamos el DTO
                    detalle.setIdAmortizacion(rs.getInt("id_amortizacion"));
                    detalle.setNumeroCuota(rs.getInt("numero_cuota"));
                    detalle.setFechaPagoProgramada(rs.getDate("fecha_pago_programada"));
                    detalle.setValorCuota(rs.getBigDecimal("valor_cuota"));
                    detalle.setInteresPagado(rs.getBigDecimal("interes_pagado"));
                    detalle.setCapitalPagado(rs.getBigDecimal("capital_pagado"));
                    detalle.setSaldoCapital(rs.getBigDecimal("saldo_capital"));
                    
                    tabla.add(detalle);
                }
            }
            
            // Si la tabla está vacía (ID no existe), simplemente devuelve la lista vacía.
            return tabla;

        } catch (Exception e) {
            // Lanzamos la excepción para que el controlador la maneje
            throw new Exception("Error al consultar la tabla de amortización: " + e.getMessage());
        }
    }   
    
}