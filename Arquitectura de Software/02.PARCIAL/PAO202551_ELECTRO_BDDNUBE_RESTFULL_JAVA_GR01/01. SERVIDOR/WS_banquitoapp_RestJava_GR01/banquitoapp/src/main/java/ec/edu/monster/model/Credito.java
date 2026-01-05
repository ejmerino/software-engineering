package ec.edu.monster.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "credito")
public class Credito implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_credito")
    private Integer idCredito;

    @Column(name = "monto_prestamo")
    private BigDecimal montoPrestamo;

    @Column(name = "tasa_interes_anual")
    private BigDecimal tasaInteresAnual;

    @Column(name = "numero_cuotas")
    private int numeroCuotas;

    @Column(name = "valor_cuota_fija")
    private BigDecimal valorCuotaFija;

    @Column(name = "fecha_aprobacion")
    @Temporal(TemporalType.DATE)
    private Date fechaAprobacion;

    @Column(name = "estado")
    private String estado;

    @ManyToOne
    @JoinColumn(name = "cedula_cliente", nullable = false)
    private Cliente cliente;

    // --- CORRECCIÓN IMPORTANTE: JsonIgnoreProperties ---
    @OneToMany(mappedBy = "credito", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("credito")
    private List<AmortizacionDetalle> amortizacion;

    public Credito() {}

    // Getters y Setters
    public Integer getIdCredito() { return idCredito; }
    public void setIdCredito(Integer idCredito) { this.idCredito = idCredito; }
    public BigDecimal getMontoPrestamo() { return montoPrestamo; }
    public void setMontoPrestamo(BigDecimal montoPrestamo) { this.montoPrestamo = montoPrestamo; }
    public BigDecimal getTasaInteresAnual() { return tasaInteresAnual; }
    public void setTasaInteresAnual(BigDecimal tasaInteresAnual) { this.tasaInteresAnual = tasaInteresAnual; }
    public int getNumeroCuotas() { return numeroCuotas; }
    public void setNumeroCuotas(int numeroCuotas) { this.numeroCuotas = numeroCuotas; }
    public BigDecimal getValorCuotaFija() { return valorCuotaFija; }
    public void setValorCuotaFija(BigDecimal valorCuotaFija) { this.valorCuotaFija = valorCuotaFija; }
    public Date getFechaAprobacion() { return fechaAprobacion; }
    public void setFechaAprobacion(Date fechaAprobacion) { this.fechaAprobacion = fechaAprobacion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<AmortizacionDetalle> getAmortizacion() { return amortizacion; }
    public void setAmortizacion(List<AmortizacionDetalle> amortizacion) { this.amortizacion = amortizacion; }
}