package ec.edu.monster.model;

import java.util.List;

public class PeticionFactura {
    private String cedulaCliente;
    private String formaPago;
    private int numeroCuotas;
    private List<ItemFactura> items;

    public PeticionFactura(String cedula, String pago, int cuotas, List<ItemFactura> items) {
        this.cedulaCliente = cedula; 
        this.formaPago = pago; 
        this.numeroCuotas = cuotas; 
        this.items = items;
    }
    
    // Getters y Setters necesarios
    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }
    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }
    public int getNumeroCuotas() { return numeroCuotas; }
    public void setNumeroCuotas(int numeroCuotas) { this.numeroCuotas = numeroCuotas; }
    public List<ItemFactura> getItems() { return items; }
    public void setItems(List<ItemFactura> items) { this.items = items; }
}