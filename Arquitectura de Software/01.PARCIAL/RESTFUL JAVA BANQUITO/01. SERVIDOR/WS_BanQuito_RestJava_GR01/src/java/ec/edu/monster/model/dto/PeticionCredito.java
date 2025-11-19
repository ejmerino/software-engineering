package ec.edu.monster.model.dto;

// No necesitamos getters/setters si los campos son públicos,
// pero es buena práctica para Gson.
public class PeticionCredito {
    private String cedula;
    private double precioElectrodomestico;
    private int numeroCuotas;

    // Getters y Setters
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public double getPrecioElectrodomestico() { return precioElectrodomestico; }
    public void setPrecioElectrodomestico(double precioElectrodomestico) { this.precioElectrodomestico = precioElectrodomestico; }
    public int getNumeroCuotas() { return numeroCuotas; }
    public void setNumeroCuotas(int numeroCuotas) { this.numeroCuotas = numeroCuotas; }
}