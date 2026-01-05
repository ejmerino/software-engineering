package ec.edu.monster.model;

public class ItemFactura {
    private int idElectrodomestico;
    private int cantidad;

    public ItemFactura(int id, int cant) { 
        this.idElectrodomestico = id; 
        this.cantidad = cant; 
    }
    
    // Getters y Setters
    public int getIdElectrodomestico() { return idElectrodomestico; }
    public void setIdElectrodomestico(int idElectrodomestico) { this.idElectrodomestico = idElectrodomestico; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
}