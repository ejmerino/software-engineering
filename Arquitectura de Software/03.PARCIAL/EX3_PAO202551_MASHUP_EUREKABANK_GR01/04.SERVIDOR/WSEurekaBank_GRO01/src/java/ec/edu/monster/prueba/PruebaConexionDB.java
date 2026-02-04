/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.edu.monster.prueba;

import ec.edu.monster.db.AccesoDB;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author diego
 */
public class PruebaConexionDB {
    public static void main(String[] args) {
        try {
            Connection cn = AccesoDB.getConnection();
            System.out.println("Prueba de conexion exitosa");
            cn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}