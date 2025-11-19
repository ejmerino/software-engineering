package ec.edu.monster.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestConexion {

    // Configura tus datos de conexión
    private static final String URL = "jdbc:mariadb://localhost:3307/db_banquito";
    private static final String USER = "root";
    private static final String PASSWORD = "1234"; // <-- CAMBIA ESTO

    public static void main(String[] args) {
        
        System.out.println("Intentando conectar a la base de datos (Prueba JDBC)...");
        
        try {
            // 1. Cargar el driver
            Class.forName("org.mariadb.jdbc.Driver");
            
            // 2. Intentar la conexión
            Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
            
            System.out.println("¡ÉXITO! Conexión JDBC establecida a " + URL);
            con.close();
            
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: No se encontró el driver de MariaDB.");
            System.err.println("Asegúrate de que el archivo .jar esté en 'Libraries'.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("ERROR: Falló la conexión a la base de datos.");
            System.err.println("Verifica la URL, usuario, contraseña y que MariaDB/MySQL esté corriendo.");
            e.printStackTrace();
        }
    }
}