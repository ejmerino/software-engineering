package ec.edu.monster.service;

import ec.edu.monster.model.Cliente;
import ec.edu.monster.model.Electrodomestico;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CatalogoService {

    private final String URL = "jdbc:mariadb://localhost:3307/db_comercializadora?useGSSAPI=false";
    private final String USER = "root";
    private final String PASSWORD = "1234"; // <-- REVISA TU CLAVE

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("org.mariadb.jdbc.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ==========================
    //   CRUD ELECTRODOMÉSTICOS
    // ==========================

    // --- LEER TODOS (READ) ---
    public List<Electrodomestico> listarTodos() throws Exception {
        List<Electrodomestico> lista = new ArrayList<>();
        String sql = "SELECT id_electrodomestico, nombre, descripcion, " +
                     "precio_venta, stock, ruta_imagen " +
                     "FROM Electrodomestico";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql);
             ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Electrodomestico e = new Electrodomestico();
                e.setIdElectrodomestico(rs.getInt("id_electrodomestico"));
                e.setNombre(rs.getString("nombre"));
                e.setDescripcion(rs.getString("descripcion"));
                e.setPrecioVenta(rs.getBigDecimal("precio_venta"));
                e.setStock(rs.getInt("stock"));
                e.setRutaImagen(rs.getString("ruta_imagen"));
                lista.add(e);
            }
        } catch (Exception e) {
            throw new Exception("Error al listar electrodomésticos: " + e.getMessage(), e);
        }
        return lista;
    }

    // --- LEER PRODUCTO POR ID ---
    public Electrodomestico getElectrodomesticoPorId(int id) throws Exception {
        String sql = "SELECT id_electrodomestico, nombre, descripcion, " +
                     "precio_venta, stock, ruta_imagen " +
                     "FROM Electrodomestico WHERE id_electrodomestico = ?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Electrodomestico e = new Electrodomestico();
                    e.setIdElectrodomestico(rs.getInt("id_electrodomestico"));
                    e.setNombre(rs.getString("nombre"));
                    e.setDescripcion(rs.getString("descripcion"));
                    e.setPrecioVenta(rs.getBigDecimal("precio_venta"));
                    e.setStock(rs.getInt("stock"));
                    e.setRutaImagen(rs.getString("ruta_imagen"));
                    return e;
                } else {
                    return null;
                }
            }
        } catch (Exception ex) {
            throw new Exception("Error al buscar producto: " + ex.getMessage(), ex);
        }
    }

    // --- CREAR (CREATE) ---
    public Electrodomestico crear(Electrodomestico e) throws Exception {
        String sql = "INSERT INTO Electrodomestico " +
                     "(nombre, descripcion, precio_venta, stock, ruta_imagen) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pst.setString(1, e.getNombre());
            pst.setString(2, e.getDescripcion());
            pst.setBigDecimal(3, e.getPrecioVenta() != null ? e.getPrecioVenta() : BigDecimal.ZERO);
            pst.setInt(4, e.getStock());
            pst.setString(5, e.getRutaImagen());
            pst.executeUpdate();

            try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    e.setIdElectrodomestico(generatedKeys.getInt(1));
                }
            }
        } catch (Exception ex) {
            throw new Exception("Error al crear electrodoméstico: " + ex.getMessage(), ex);
        }
        return e;
    }

    // --- ACTUALIZAR (UPDATE) ---
    public void actualizar(Electrodomestico e) throws Exception {
        String sql = "UPDATE Electrodomestico " +
                     "SET nombre = ?, descripcion = ?, precio_venta = ?, " +
                     "stock = ?, ruta_imagen = ? " +
                     "WHERE id_electrodomestico = ?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, e.getNombre());
            pst.setString(2, e.getDescripcion());
            pst.setBigDecimal(3, e.getPrecioVenta());
            pst.setInt(4, e.getStock());
            pst.setString(5, e.getRutaImagen());
            pst.setInt(6, e.getIdElectrodomestico());
            pst.executeUpdate();
        } catch (Exception ex) {
            throw new Exception("Error al actualizar electrodoméstico: " + ex.getMessage(), ex);
        }
    }

    // --- ELIMINAR (DELETE) ---
    public void eliminar(int id) throws Exception {
        String sql = "DELETE FROM Electrodomestico WHERE id_electrodomestico = ?";

        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setInt(1, id);
            pst.executeUpdate();
        } catch (Exception ex) {
            throw new Exception("Error al eliminar electrodoméstico: " + ex.getMessage(), ex);
        }
    }

    // ==========================
    //   CLIENTES TIENDA
    // ==========================

    // --- CREAR CLIENTE ---
    public Cliente crearCliente(Cliente c) throws Exception {
        String sql = "INSERT INTO Cliente (cedula, nombres, apellidos, direccion, telefono, email) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, c.getCedula());
            pst.setString(2, c.getNombres());
            pst.setString(3, c.getApellidos());
            pst.setString(4, c.getDireccion());
            pst.setString(5, c.getTelefono());
            pst.setString(6, c.getEmail());
            pst.executeUpdate();

        } catch (Exception ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("Duplicate entry")) {
                throw new Exception("Error: La cédula " + c.getCedula() + " ya está registrada en la tienda.", ex);
            }
            throw new Exception("Error al crear cliente: " + ex.getMessage(), ex);
        }
        return c;
    }

    // --- BUSCAR CLIENTE POR CÉDULA ---
    public Cliente getClientePorCedula(String cedula) throws Exception {
        String sql = "SELECT * FROM Cliente WHERE cedula = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {

            pst.setString(1, cedula);
            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    Cliente c = new Cliente();
                    c.setCedula(rs.getString("cedula"));
                    c.setNombres(rs.getString("nombres"));
                    c.setApellidos(rs.getString("apellidos"));
                    c.setDireccion(rs.getString("direccion"));
                    c.setTelefono(rs.getString("telefono"));
                    c.setEmail(rs.getString("email"));
                    return c;
                } else {
                    return null;
                }
            }
        } catch (Exception ex) {
            throw new Exception("Error al buscar cliente: " + ex.getMessage(), ex);
        }
    }
}
