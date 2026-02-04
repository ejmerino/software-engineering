package ec.edu.monster.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AccesoDB {

    private static final String URL =
        "jdbc:mysql://localhost:3307/eurekabank"
        + "?useSSL=false"
        + "&allowPublicKeyRetrieval=true"
        + "&serverTimezone=America/Guayaquil"
        + "&characterEncoding=utf8";

    private static final String USER = "root";   // AJUSTA
    private static final String PASS = "1234";   // AJUSTA

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            throw new SQLException("No se encontró el driver MySQL.", e);
        }
    }
}
