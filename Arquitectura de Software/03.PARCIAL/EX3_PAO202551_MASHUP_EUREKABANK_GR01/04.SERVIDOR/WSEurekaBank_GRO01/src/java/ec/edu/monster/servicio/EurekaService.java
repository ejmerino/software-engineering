package ec.edu.monster.servicio;

import ec.edu.monster.db.AccesoDB;
import ec.edu.monster.modelo.Movimiento;
import com.google.gson.Gson;
import ec.edu.monster.ws.EurekaWebSocket;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EurekaService {

    /** Lee movimientos por cuenta (conversión Timestamp -> String) */
    public List<Movimiento> leerMovimientos(String cuenta) {
        Connection cn = null;
        List<Movimiento> lista = new ArrayList<>();

        String sql = "SELECT "
                + " m.chr_cuencodigo cuenta, "
                + " m.int_movinumero nromov, "
                + " m.dtt_movifecha fecha, "
                + " t.vch_tipodescripcion tipo, "
                + " t.vch_tipoaccion accion, "
                + " m.dec_moviimporte importe "
                + "FROM tipomovimiento t INNER JOIN movimiento m "
                + "ON t.chr_tipocodigo = m.chr_tipocodigo "
                + "WHERE m.chr_cuencodigo = ? "
                + "ORDER BY m.int_movinumero ASC";

        try {
            cn = AccesoDB.getConnection();
            PreparedStatement pstm = cn.prepareStatement(sql);
            pstm.setString(1, cuenta);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                Movimiento bean = new Movimiento();
                bean.setCuenta(rs.getString("cuenta"));
                bean.setNromov(rs.getInt("nromov"));

                Timestamp ts = rs.getTimestamp("fecha");
                bean.setFecha(ts != null ? ts.toString() : null); // ← CORRECCIÓN

                bean.setTipo(rs.getString("tipo"));
                bean.setAccion(rs.getString("accion"));
                bean.setImporte(rs.getDouble("importe"));
                lista.add(bean);
            }
            rs.close();
            pstm.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try { if (cn != null) cn.close(); } catch (Exception ignored) {}
        }
        return lista;
    }

    /** Depósito (tipo 003 INGRESO) */
    // Ejemplo corregido en EurekaService.java
    public void registrarDeposito(String cuenta, double importe, String codEmp) {
        // 1. INTENTAR BLOQUEAR (Esto dispara el ROJO en las otras pestañas) [cite: 24]
        if (intentarBloquearCuenta(cuenta)) { 
            try {
                // ... tu lógica de depósito actual ...
                registrarMovimientoAtomic(cuenta, importe, codEmp, "003");
            } finally {
                // 2. LIBERAR (Esto dispara el VERDE de nuevo) [cite: 33]
                liberarCuenta(cuenta); 
            }
        } else {
            throw new RuntimeException("Cuenta bloqueada por otro cajero.");
        }
    }

    /** Retiro (tipo 004 RETIRO) con importe positivo */
    public void registrarRetiro(String cuenta, double importe, String codEmp) {
        if (importe <= 0) throw new RuntimeException("El importe del retiro debe ser > 0.");
        double saldoActual = calcularSaldo(cuenta);
        if (importe > saldoActual) throw new RuntimeException("Saldo insuficiente.");
        registrarMovimientoAtomic(cuenta, importe, codEmp, "004"); // 004 = RETIRO (SALIDA)
    }

    /** Transferencia: retiro en origen + depósito en destino (ambos positivos) */
    public void registrarTransferencia(String origen, String destino, double importe, String codEmp) {
        if (origen == null || destino == null || origen.equals(destino))
            throw new RuntimeException("Cuentas inválidas para transferencia.");
        if (importe <= 0) throw new RuntimeException("El importe debe ser > 0.");

        Connection cn = null;
        try {
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false);

            // ORIGEN: validar, leer saldo/contador, actualizar, insertar RETIRO 
            validarCuentaActiva(cn, origen);
            double saldoOrigen = leerSaldoCuenta(cn, origen);
            if (importe > saldoOrigen) throw new RuntimeException("Saldo insuficiente en cuenta origen.");
            int contOrigen = leerContadorMov(cn, origen) + 1;
            actualizarCuenta(cn, origen, saldoOrigen - importe, contOrigen);
            insertarMovimiento(cn, origen, contOrigen, codEmp, "009", importe);

            // DESTINO: validar, leer saldo/contador, actualizar, insertar DEPÓSITO
            validarCuentaActiva(cn, destino);
            double saldoDestino = leerSaldoCuenta(cn, destino);
            int contDestino = leerContadorMov(cn, destino) + 1;
            actualizarCuenta(cn, destino, saldoDestino + importe, contDestino);
            insertarMovimiento(cn, destino, contDestino, codEmp, "008", importe);

            cn.commit();
        } catch (SQLException e) {
            try { if (cn != null) cn.rollback(); } catch (Exception ignored) {}
            throw new RuntimeException(e.getMessage());
        } catch (Exception e) {
            try { if (cn != null) cn.rollback(); } catch (Exception ignored) {}
            throw new RuntimeException("ERROR en transferencia: " + e.getMessage());
        } finally {
            try { if (cn != null) cn.close(); } catch (Exception ignored) {}
        }
    }

    /** Utilidad: convertir lista a JSON */
    public String convertirMovimientosAJSON(List<Movimiento> lista) {
        return new Gson().toJson(lista);
    }

    // ==================== PRIVADOS (transaccionales) ====================

    private void registrarMovimientoAtomic(String cuenta, double importe, String codEmp, String tipocod) {
    Connection cn = null;
    try {
        cn = AccesoDB.getConnection();
        cn.setAutoCommit(false);

        validarCuentaActiva(cn, cuenta);

        double saldo = leerSaldoCuenta(cn, cuenta);
        int cont = leerContadorMov(cn, cuenta) + 1;

        // Lógica de saldo: suma si es Depósito (003) o Transferencia-INGRESO (008)
        boolean esIngreso = tipocod.equals("003") || tipocod.equals("008");
        double nuevoSaldo = esIngreso ? (saldo + importe) : (saldo - importe);
        
        if (nuevoSaldo < 0) throw new RuntimeException("Saldo insuficiente.");

        actualizarCuenta(cn, cuenta, nuevoSaldo, cont);
        insertarMovimiento(cn, cuenta, cont, codEmp, tipocod, importe);

        cn.commit();
    } catch (SQLException e) {
        try { if (cn != null) cn.rollback(); } catch (Exception ignored) {}
        throw new RuntimeException(e.getMessage());
    } catch (Exception e) {
        try { if (cn != null) cn.rollback(); } catch (Exception ignored) {}
        throw new RuntimeException("ERROR, en el proceso registrar movimiento.");
    } finally {
        try { if (cn != null) cn.close(); } catch (Exception ignored) {}
    }
}

    private void validarCuentaActiva(Connection cn, String cuenta) throws SQLException {
        String sql = "SELECT 1 FROM cuenta WHERE chr_cuencodigo=? AND vch_cuenestado='ACTIVO' FOR UPDATE";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cuenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new RuntimeException("Cuenta no existe o no está activa.");
            }
        }
    }

    private double leerSaldoCuenta(Connection cn, String cuenta) throws SQLException {
        String sql = "SELECT dec_cuensaldo FROM cuenta WHERE chr_cuencodigo=?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cuenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new RuntimeException("Cuenta no encontrada.");
                return rs.getDouble("dec_cuensaldo");
            }
        }
    }

    private int leerContadorMov(Connection cn, String cuenta) throws SQLException {
        String sql = "SELECT int_cuencontmov FROM cuenta WHERE chr_cuencodigo=?";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cuenta);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) throw new RuntimeException("Cuenta no encontrada.");
                return rs.getInt("int_cuencontmov");
            }
        }
    }

    private void actualizarCuenta(Connection cn, String cuenta, double nuevoSaldo, int cont) throws SQLException {
        String sql = "UPDATE cuenta SET dec_cuensaldo=?, int_cuencontmov=? "
                   + "WHERE chr_cuencodigo=? AND vch_cuenestado='ACTIVO'";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setDouble(1, nuevoSaldo);
            ps.setInt(2, cont);
            ps.setString(3, cuenta);
            ps.executeUpdate();
        }
    }

    private void insertarMovimiento(Connection cn, String cuenta, int cont, String codEmp, String tipocod, double importe) throws SQLException {
        String sql = "INSERT INTO movimiento(chr_cuencodigo,int_movinumero,dtt_movifecha,chr_emplcodigo,chr_tipocodigo,dec_moviimporte) "
                   + "VALUES(?,?,SYSDATE(),?,?,?)";
        try (PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, cuenta);
            ps.setInt(2, cont);
            ps.setString(3, codEmp);
            ps.setString(4, tipocod);
            ps.setDouble(5, importe); // SIEMPRE POSITIVO
            ps.executeUpdate();
        }
    }

    private double calcularSaldo(String cuenta) {
        List<Movimiento> lista = leerMovimientos(cuenta);
        double saldo = 0;
        for (Movimiento m : lista) {
            if ("INGRESO".equalsIgnoreCase(m.getAccion())) saldo += m.getImporte();
            else saldo -= m.getImporte();
        }
        return saldo;
    }
    
    // Método para intentar bloquear una cuenta antes de la transacción
    public boolean intentarBloquearCuenta(String cuenta) {
        Connection cn = null; // DECLARACIÓN LOCAL CORREGIDA
        PreparedStatement pstm = null;
        try {
            cn = AccesoDB.getConnection();
            // Solo bloquea si está LIBRE (Garantiza Exclusión Mutua)
            String sql = "UPDATE cuenta SET estado_bloqueo = 'BLOQUEADO' WHERE chr_cuencodigo = ? AND estado_bloqueo = 'LIBRE'";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, cuenta);

            int filasAfectadas = pstm.executeUpdate();
            if (filasAfectadas > 0) {
                // Notificación en tiempo real vía WebSocket
                EurekaWebSocket.notificarCambioEstado(cuenta, "BLOQUEADO");
                return true;
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } finally {
            // Siempre cerrar recursos en el servidor para estabilidad
            try { if (pstm != null) pstm.close(); } catch (Exception e) {}
            try { if (cn != null) cn.close(); } catch (Exception e) {}
        }
        return false;
    }

    public void liberarCuenta(String cuenta) {
        Connection cn = null; // DECLARACIÓN LOCAL CORREGIDA
        PreparedStatement pstm = null;
        try {
            cn = AccesoDB.getConnection();
            String sql = "UPDATE cuenta SET estado_bloqueo = 'LIBRE' WHERE chr_cuencodigo = ?";
            pstm = cn.prepareStatement(sql);
            pstm.setString(1, cuenta);
            pstm.executeUpdate();

            // Notificación de liberación (Cambio a color verde)
            EurekaWebSocket.notificarCambioEstado(cuenta, "LIBRE");
        } catch (SQLException e) { 
            e.printStackTrace(); 
        } finally {
            try { if (pstm != null) pstm.close(); } catch (Exception e) {}
            try { if (cn != null) cn.close(); } catch (Exception e) {}
        }
    }  
}
