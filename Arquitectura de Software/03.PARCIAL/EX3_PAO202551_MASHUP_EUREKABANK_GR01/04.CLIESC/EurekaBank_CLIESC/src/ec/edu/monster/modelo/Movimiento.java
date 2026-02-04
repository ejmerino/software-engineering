package ec.edu.monster.modelo;

public class Movimiento {
    private String fecha;
    private String tipo;
    private double importe;
    private Double saldo;          // puede venir nulo del backend
    private Double saldoCalculado; // lo calculamos en el cliente

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getImporte() { return importe; }
    public void setImporte(double importe) { this.importe = importe; }

    public Double getSaldo() { return saldo; }
    public void setSaldo(Double saldo) { this.saldo = saldo; }

    public Double getSaldoCalculado() { return saldoCalculado; }
    public void setSaldoCalculado(Double saldoCalculado) { this.saldoCalculado = saldoCalculado; }
}
