package ec.edu.monster.model;

import java.math.BigDecimal;

public class Electrodomestico {
    private int idElectrodomestico;
    private String nombre;
    private String descripcion;
    private BigDecimal precioVenta;
    private String rutaImagen;
    private int stock; // <-- NUEVO CAMPO

    public int getIdElectrodomestico() { return idElectrodomestico; }
    public void setIdElectrodomestico(int idElectrodomestico) { this.idElectrodomestico = idElectrodomestico; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) { this.precioVenta = precioVenta; }
    public String getRutaImagen() { return rutaImagen; }
    public void setRutaImagen(String rutaImagen) { this.rutaImagen = rutaImagen; }
    // --- Getters y Setters para Stock ---
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    
    @Override
    public String toString() {
        return "ID: " + idElectrodomestico + 
               " | Producto: " + nombre + 
               " | Precio: $" + precioVenta + 
               " | Stock: " + stock;
    }
}