package ec.edu.monster.model.dto;

import java.util.List;

public class PeticionFactura {
    private String cedulaCliente;
    private String formaPago; // "Efectivo" o "Credito"
    private int numeroCuotas; // 0 si es en efectivo
    private List<ItemFactura> items;

    // Getters y Setters...
    public String getCedulaCliente() { return cedulaCliente; }
    public void setCedulaCliente(String cedulaCliente) { this.cedulaCliente = cedulaCliente; }
    public String getFormaPago() { return formaPago; }
    public void setFormaPago(String formaPago) { this.formaPago = formaPago; }
    public int getNumeroCuotas() { return numeroCuotas; }
    public void setNumeroCuotas(int numeroCuotas) { this.numeroCuotas = numeroCuotas; }
    public List<ItemFactura> getItems() { return items; }
    public void setItems(List<ItemFactura> items) { this.items = items; }
}