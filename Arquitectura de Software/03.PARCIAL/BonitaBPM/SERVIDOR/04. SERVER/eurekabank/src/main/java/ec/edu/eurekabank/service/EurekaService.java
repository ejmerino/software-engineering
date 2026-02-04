package ec.edu.eurekabank.service;

import ec.edu.eurekabank.db.AccesoDB;
import ec.edu.eurekabank.model.Movimiento;
import ec.edu.eurekabank.model.Cuenta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EurekaService {

    /**
     * Obtiene el saldo actual de una cuenta
     * Requerido por BPMN para validación antes de retiro/transferencia
     * @param cuenta Número de cuenta
     * @return Saldo de la cuenta
     */
    public double obtenerSaldo(String cuenta) {
        Connection cn = null;
        double saldo = 0;
        String sql = "SELECT dec_cuensaldo FROM cuenta WHERE chr_cuencodigo = ? AND vch_cuenestado = 'ACTIVO'";

        try {
            cn = AccesoDB.getConnection();
            PreparedStatement pstm = cn.prepareStatement(sql);
            pstm.setString(1, cuenta);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                saldo = rs.getDouble("dec_cuensaldo");
            } else {
                throw new RuntimeException("Cuenta no encontrada o inactiva");
            }
            rs.close();
            pstm.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar saldo: " + e.getMessage());
        } finally {
            try {
                if (cn != null) cn.close();
            } catch (Exception e) {
            }
        }
        return saldo;
    }

    /**
     * Valida si una cuenta existe y está activa
     * Requerido por BPMN para validación de datos de entrada
     * @param cuenta Número de cuenta
     * @return true si la cuenta es válida y activa
     */
    public boolean validarCuentaActiva(String cuenta) {
        Connection cn = null;
        boolean existe = false;
        String sql = "SELECT COUNT(1) AS total FROM cuenta WHERE chr_cuencodigo = ? AND vch_cuenestado = 'ACTIVO'";

        try {
            cn = AccesoDB.getConnection();
            PreparedStatement pstm = cn.prepareStatement(sql);
            pstm.setString(1, cuenta);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                existe = rs.getInt("total") == 1;
            }
            rs.close();
            pstm.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar cuenta: " + e.getMessage());
        } finally {
            try {
                if (cn != null) cn.close();
            } catch (Exception e) {
            }
        }
        return existe;
    }

    public List<Movimiento> leerMovimientos(String cuenta) {
        Connection cn = null;
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT \n"
            + " m.chr_cuencodigo cuenta, \n"
            + " m.int_movinumero nromov, \n"
            + " m.dtt_movifecha fecha, \n"
            + " t.vch_tipodescripcion tipo, \n"
            + " t.vch_tipoaccion accion, \n"
            + " m.dec_moviimporte importe \n"
            + "FROM tipomovimiento t INNER JOIN movimiento m \n"
            + "ON t.chr_tipocodigo = m.chr_tipocodigo \n"
            + "WHERE m.chr_cuencodigo = ? \n"
            + "ORDER BY m.int_movinumero DESC";

        try {
            cn = AccesoDB.getConnection();
            PreparedStatement pstm = cn.prepareStatement(sql);
            pstm.setString(1, cuenta);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Movimiento rec = new Movimiento();
                rec.setCuenta(rs.getString("cuenta"));
                rec.setNromov(rs.getInt("nromov"));
                rec.setFecha(rs.getDate("fecha"));
                rec.setTipo(rs.getString("tipo"));
                rec.setAccion(rs.getString("accion"));
                rec.setImporte(rs.getDouble("importe"));

                lista.add(rec);
            }
            rs.close();
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (cn != null) cn.close();
            } catch (Exception e) {
            }
        }
        return lista;
    }

    public void registrarDeposito(String cuenta, double importe, String codEmp) {
        ejecutarMovimiento(cuenta, importe, codEmp, "003", true);
    }

    public void registrarRetiro(String cuenta, double importe, String codEmp) {
        ejecutarMovimientoDescuento(cuenta, importe, codEmp, "004", false);
    }

    public void registrarTransferencia(String cuentaOrigen, String cuentaDestino, double importe, String codEmp) {
        Connection cn = null;
        try {
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false);

            // Retirar de la cuenta de origen
            ejecutarMovimientoInternoDescuento(cuentaOrigen, importe, codEmp, "009", cn, false);

            // Depositar en la cuenta de destino
            ejecutarMovimientoInterno(cuentaDestino, importe, codEmp, "008", cn, true);

            // Confirmar la transacción
            cn.commit();
        } catch (SQLException e) {
            try {
                if (cn != null) cn.rollback();
            } catch (Exception ex) {
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (cn != null) cn.close();
            } catch (Exception e) {
            }
        }
    }

    private void ejecutarMovimiento(String cuenta, double importe, String codEmp, String tipoMov, boolean permiteNegativo) {
        Connection cn = null;
        try {
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false);
            ejecutarMovimientoInterno(cuenta, importe, codEmp, tipoMov, cn, permiteNegativo);
            cn.commit();
        } catch (SQLException e) {
            try {
                if (cn != null) cn.rollback();
            } catch (Exception ex) {
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (cn != null) cn.close();
            } catch (Exception e) {
            }
        }
    }
    
    private void ejecutarMovimientoDescuento(String cuenta, double importe, String codEmp, String tipoMov, boolean permiteNegativo) {
        Connection cn = null;
        try {
            cn = AccesoDB.getConnection();
            cn.setAutoCommit(false);
            ejecutarMovimientoInternoDescuento(cuenta, importe, codEmp, tipoMov, cn, permiteNegativo);
            cn.commit();
        } catch (SQLException e) {
            try {
                if (cn != null) cn.rollback();
            } catch (Exception ex) {
            }
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (cn != null) cn.close();
            } catch (Exception e) {
            }
        }
    }
    
    

    private void ejecutarMovimientoInterno(String cuenta, double importe, String codEmp, String tipoMov, Connection cn, boolean permiteNegativo) throws SQLException {
        String sql = "SELECT dec_cuensaldo, int_cuencontmov "
                + "FROM cuenta "
                + "WHERE chr_cuencodigo = ? AND vch_cuenestado = 'ACTIVO' FOR UPDATE";
        PreparedStatement pstm = cn.prepareStatement(sql);
        pstm.setString(1, cuenta);
        ResultSet rs = pstm.executeQuery();

        if (!rs.next()) {
            throw new SQLException("ERROR: Cuenta no existe o no está activa.");
        }
        double saldo = rs.getDouble("dec_cuensaldo");
        int cont = rs.getInt("int_cuencontmov");
        rs.close();
        pstm.close();

        saldo += importe;
        if (!permiteNegativo && saldo < 0) {
            throw new SQLException("ERROR: Saldo insuficiente.");
        }

        cont++;
        sql = "UPDATE cuenta SET dec_cuensaldo = ?, int_cuencontmov = ? WHERE chr_cuencodigo = ?";
        pstm = cn.prepareStatement(sql);
        pstm.setDouble(1, saldo);
        pstm.setInt(2, cont);
        pstm.setString(3, cuenta);
        pstm.executeUpdate();
        pstm.close();

        sql = "INSERT INTO movimiento(chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte) "
                + "VALUES (?, ?, SYSDATE(), ?, ?, ?)";
        pstm = cn.prepareStatement(sql);
        pstm.setString(1, cuenta);
        pstm.setInt(2, cont);
        pstm.setString(3, codEmp);
        pstm.setString(4, tipoMov);
        pstm.setDouble(5, importe);
        pstm.executeUpdate();
        pstm.close();
    }
    
    
    private void ejecutarMovimientoInternoDescuento(String cuenta, double importe, String codEmp, String tipoMov, Connection cn, boolean permiteNegativo) throws SQLException {
        String sql = "SELECT dec_cuensaldo, int_cuencontmov "
                + "FROM cuenta "
                + "WHERE chr_cuencodigo = ? AND vch_cuenestado = 'ACTIVO' FOR UPDATE";
        PreparedStatement pstm = cn.prepareStatement(sql);
        pstm.setString(1, cuenta);
        ResultSet rs = pstm.executeQuery();

        if (!rs.next()) {
            throw new SQLException("ERROR: Cuenta no existe o no está activa.");
        }
        double saldo = rs.getDouble("dec_cuensaldo");
        int cont = rs.getInt("int_cuencontmov");
        rs.close();
        pstm.close();

        saldo -= importe;
        if (!permiteNegativo && saldo < 0) {
            throw new SQLException("ERROR: Saldo insuficiente.");
        }

        cont++;
        sql = "UPDATE cuenta SET dec_cuensaldo = ?, int_cuencontmov = ? WHERE chr_cuencodigo = ?";
        pstm = cn.prepareStatement(sql);
        pstm.setDouble(1, saldo);
        pstm.setInt(2, cont);
        pstm.setString(3, cuenta);
        pstm.executeUpdate();
        pstm.close();

        sql = "INSERT INTO movimiento(chr_cuencodigo, int_movinumero, dtt_movifecha, chr_emplcodigo, chr_tipocodigo, dec_moviimporte) "
                + "VALUES (?, ?, SYSDATE(), ?, ?, ?)";
        pstm = cn.prepareStatement(sql);
        pstm.setString(1, cuenta);
        pstm.setInt(2, cont);
        pstm.setString(3, codEmp);
        pstm.setString(4, tipoMov);
        pstm.setDouble(5, importe);
        pstm.executeUpdate();
        pstm.close();
    }
    
    public boolean login(String username, String password) {
        Connection cn = null;
        boolean acceso = false;
        String sql = "SELECT COUNT(1) AS total " +
                     "FROM usuario " +
                     "WHERE vch_emplusuario = ? " +
                     "AND vch_emplclave = SHA(?) " +
                     "AND vch_emplestado = 'ACTIVO'";

        try {
            cn = AccesoDB.getConnection();
            PreparedStatement pstm = cn.prepareStatement(sql);
            pstm.setString(1, username);
            pstm.setString(2, password);
            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                acceso = rs.getInt("total") == 1;
            }
            rs.close();
            pstm.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al validar login: " + e.getMessage());
        } finally {
            try {
                if (cn != null) cn.close();
            } catch (Exception e) {
            }
        }

        return acceso;
    }

    public List<Cuenta> leerBalances() {
        Connection cn = null;
        List<Cuenta> lista = new ArrayList<>();
        String sql = "SELECT " +
            "c.chr_cuencodigo AS numeroCuenta, " +
            "CONCAT(cl.vch_clienombre, ' ', cl.vch_cliepaterno, ' ', cl.vch_cliematerno) AS nombreCliente, " +
            "c.dec_cuensaldo AS saldo, " +
            "m.vch_monedescripcion AS moneda, " +
            "c.vch_cuenestado AS estado " +
            "FROM cuenta c " +
            "INNER JOIN cliente cl ON c.chr_cliecodigo = cl.chr_cliecodigo " +
            "INNER JOIN modena m ON c.chr_monecodigo = m.chr_monecodigo " +
            "WHERE c.vch_cuenestado = 'ACTIVO' " +
            "ORDER BY c.chr_cuencodigo";

        try {
            cn = AccesoDB.getConnection();
            PreparedStatement pstm = cn.prepareStatement(sql);
            ResultSet rs = pstm.executeQuery();

            while (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setNumeroCuenta(rs.getString("numeroCuenta"));
                cuenta.setNombreCliente(rs.getString("nombreCliente"));
                cuenta.setSaldo(rs.getDouble("saldo"));
                cuenta.setMoneda(rs.getString("moneda"));
                cuenta.setEstado(rs.getString("estado"));

                lista.add(cuenta);
            }
            rs.close();
            pstm.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al leer balances: " + e.getMessage());
        } finally {
            try {
                if (cn != null) cn.close();
            } catch (Exception e) {
            }
        }
        return lista;
    }

}
