package ec.edu.eurekabank.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AccesoDB {
    
    // CAMBIO IMPORTANTE AQUÍ:
    // 1. Cambiamos 'localhost' por tu IP: 159.65.46.108
    // 2. Agregamos '&allowPublicKeyRetrieval=true' para evitar errores de conexión con Docker/MySQL 8
    private static final String URL = "jdbc:mysql://159.65.46.108:3306/eureka_bank?useSSL=false&allowPublicKeyRetrieval=true";
    
    // Estas credenciales coinciden con tu docker-compose (root/root)
    private static final String USER = "root";
    private static final String PASS = "ContraseñaSegur444Hola4xd";

    public AccesoDB() {
    }
    
    public static Connection getConnection() throws SQLException {
        Connection cn = null;
        try {
            // Driver moderno
            String driver = "com.mysql.cj.jdbc.Driver";
            
            // Cargar el driver
            Class.forName(driver); // El .newInstance() ya no es necesario en versiones nuevas de Java, pero no estorba.
            
            // Obtener la conexión
            cn = DriverManager.getConnection(URL, USER, PASS);
            
        } catch (SQLException e) {
            System.out.println("Error de conexión SQL: " + e.getMessage()); // Agregado para ver errores en consola
            throw e;
        } catch (ClassNotFoundException e){
            throw new SQLException("ERROR: No se encuentra el driver de MySQL (mysql-connector-j).");
        } catch (Exception e){
            e.printStackTrace();
            throw new SQLException("ERROR: No se tiene acceso el servidor");
        }
        return cn;
    }
}
