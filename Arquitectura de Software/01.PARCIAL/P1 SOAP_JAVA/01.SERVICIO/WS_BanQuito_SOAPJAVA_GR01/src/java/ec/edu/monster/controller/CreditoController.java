package ec.edu.monster.controller;

import ec.edu.monster.model.Conexion;
import java.sql.*;

public class CreditoController {

    public boolean esSujetoCredito(String cedula) {
        try (Connection con = Conexion.getConnection()) {

            // 1️⃣ Verificar si es cliente del banco
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM cliente WHERE cedula = ?");
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                System.out.println("Cliente no encontrado.");
                return false;
            }

            int idCliente = rs.getInt("id_cliente");
            int edad = rs.getInt("edad");
            String estadoCivil = rs.getString("estado_civil");

            // 2️⃣ Verificar que tenga un depósito en el último mes
            ps = con.prepareStatement(
                "SELECT COUNT(*) AS total FROM movimiento WHERE id_cliente=? AND tipo='DEPOSITO' AND fecha >= DATE_SUB(CURDATE(), INTERVAL 1 MONTH)");
            ps.setInt(1, idCliente);
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt("total") == 0) {
                System.out.println("No tiene depósitos recientes.");
                return false;
            }

            // 3️⃣ Verificar edad si es casado
            if (estadoCivil.equalsIgnoreCase("Casado") && edad < 25) {
                System.out.println("Casado y menor de 25 años, no califica.");
                return false;
            }

            // 4️⃣ Verificar crédito activo
            ps = con.prepareStatement(
                "SELECT COUNT(*) AS creditos FROM credito WHERE id_cliente=? AND estado='ACTIVO'");
            ps.setInt(1, idCliente);
            rs = ps.executeQuery();
            if (rs.next() && rs.getInt("creditos") > 0) {
                System.out.println("Tiene crédito activo.");
                return false;
            }

            System.out.println("✅ Cliente sujeto de crédito.");
            return true;

        } catch (Exception e) {
            System.out.println("Error en esSujetoCredito: " + e.getMessage());
            return false;
        }
    }
}
