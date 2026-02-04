package ec.edu.eurekabank.model;

import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "cuenta")
public class Cuenta {
    private String numeroCuenta;
    private String nombreCliente;
    private double saldo;
    private String moneda;
    private String estado;

    public Cuenta() {
    }

    public Cuenta(String numeroCuenta, String nombreCliente, double saldo, String moneda, String estado) {
        this.numeroCuenta = numeroCuenta;
        this.nombreCliente = nombreCliente;
        this.saldo = saldo;
        this.moneda = moneda;
        this.estado = estado;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
