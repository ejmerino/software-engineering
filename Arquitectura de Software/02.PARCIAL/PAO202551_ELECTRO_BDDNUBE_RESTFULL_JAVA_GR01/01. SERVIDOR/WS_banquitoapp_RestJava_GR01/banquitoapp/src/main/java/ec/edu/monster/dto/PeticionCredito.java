package ec.edu.monster.dto;

public class PeticionCredito {
    private String cedula;
    private double precioElectrodomestico;
    private int numeroCuotas;

    public PeticionCredito() {
    }

    // Getters y Setters manuales
    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public double getPrecioElectrodomestico() {
        return precioElectrodomestico;
    }

    public void setPrecioElectrodomestico(double precioElectrodomestico) {
        this.precioElectrodomestico = precioElectrodomestico;
    }

    public int getNumeroCuotas() {
        return numeroCuotas;
    }

    public void setNumeroCuotas(int numeroCuotas) {
        this.numeroCuotas = numeroCuotas;
    }
}