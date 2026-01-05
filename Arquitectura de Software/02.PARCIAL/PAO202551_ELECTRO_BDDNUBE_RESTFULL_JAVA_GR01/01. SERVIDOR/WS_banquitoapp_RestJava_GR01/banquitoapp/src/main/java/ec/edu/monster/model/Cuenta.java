package ec.edu.monster.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CUENTA")
public class Cuenta implements Serializable {

    @Id
    @Column(name = "NUM_CUENTA", length = 8)
    private String numCuenta;

    @Column(name = "SALDO", nullable = false) // No necesitas poner @Basic
    private BigDecimal saldo; // BigDecimal es perfecto para dinero

    @ManyToOne
    @JoinColumn(name = "CEDULA", nullable = false) // FK hacia Cliente
    private Cliente cliente;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Movimiento> movimientos;

    public Cuenta() {}

    // Generar Getters y Setters...
    public String getNumCuenta() { return numCuenta; }
    public void setNumCuenta(String numCuenta) { this.numCuenta = numCuenta; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}