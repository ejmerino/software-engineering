package ec.edu.monster.service;

import ec.edu.monster.model.Cliente;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    private final String URL = "jdbc:mariadb://localhost:3307/db_banquito";
    private final String USER = "root";
    private final String PASSWORD = "1234";

    private Connection getConnection() throws Exception {
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ========== CRUD ==========

    // LISTAR TODOS
    public List<Cliente> listar() throws Exception {
        String sql = "SELECT CEDULA, NOMBRE, FECHA_NACIMIENTO, ESTADO_CIVIL FROM CLIENTE";
        List<Cliente> lista = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Cliente c = new Cliente();
                c.setCedula(rs.getString("CEDULA"));
                c.setNombre(rs.getString("NOMBRE"));
                c.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));
                c.setEstadoCivil(rs.getString("ESTADO_CIVIL"));
                lista.add(c);
            }
        }
        return lista;
    }

    // BUSCAR POR CÉDULA
    public Cliente buscarPorCedula(String cedula) throws Exception {
        String sql = "SELECT CEDULA, NOMBRE, FECHA_NACIMIENTO, ESTADO_CIVIL FROM CLIENTE WHERE CEDULA = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, cedula);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setCedula(rs.getString("CEDULA"));
                    c.setNombre(rs.getString("NOMBRE"));
                    c.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));
                    c.setEstadoCivil(rs.getString("ESTADO_CIVIL"));
                    return c;
                }
            }
        }
        return null;
    }

    // CREAR
    public void crear(Cliente c) throws Exception {
        String sql = "INSERT INTO CLIENTE (CEDULA, NOMBRE, FECHA_NACIMIENTO, ESTADO_CIVIL) " +
                     "VALUES (?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, c.getCedula());
            pst.setString(2, c.getNombre());
            pst.setDate(3, new java.sql.Date(c.getFechaNacimiento().getTime()));
            pst.setString(4, c.getEstadoCivil());
            pst.executeUpdate();
        }
    }

    // ACTUALIZAR
    public void actualizar(Cliente c) throws Exception {
        String sql = "UPDATE CLIENTE SET NOMBRE = ?, FECHA_NACIMIENTO = ?, ESTADO_CIVIL = ? " +
                     "WHERE CEDULA = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, c.getNombre());
            pst.setDate(2, new java.sql.Date(c.getFechaNacimiento().getTime()));
            pst.setString(3, c.getEstadoCivil());
            pst.setString(4, c.getCedula());
            pst.executeUpdate();
        }
    }

    // ELIMINAR
    public void eliminar(String cedula) throws Exception {
        String sql = "DELETE FROM CLIENTE WHERE CEDULA = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, cedula);
            pst.executeUpdate();
        }
    }
}
