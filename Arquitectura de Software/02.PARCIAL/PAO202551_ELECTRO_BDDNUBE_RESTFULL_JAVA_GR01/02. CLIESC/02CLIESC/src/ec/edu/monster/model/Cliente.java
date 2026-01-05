package ec.edu.monster.model;

import java.util.Date;

public class Cliente {

    private String cedula;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String email;
    
    // CAMPOS NUEVOS (Necesarios para que el Backend no rechace el registro)
    private Date fechaNacimiento;
    private String estadoCivil;

    public Cliente() {
    }

    // Constructor completo
    public Cliente(String cedula, String nombres, String apellidos, String direccion, String telefono, String email, Date fechaNacimiento, String estadoCivil) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
        this.fechaNacimiento = fechaNacimiento;
        this.estadoCivil = estadoCivil;
    }

    // Constructor de compatibilidad (para tu código viejo que no enviaba fechas)
    public Cliente(String cedula, String nombres, String apellidos, String direccion, String telefono, String email) {
        this.cedula = cedula;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    // --- GETTERS Y SETTERS ---

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // --- GETTERS Y SETTERS DE LOS CAMPOS NUEVOS ---
    
    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEstadoCivil() {
        return estadoCivil;
    }

    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    @Override
    public String toString() {
        return "Cliente{" + "cedula=" + cedula + ", nombres=" + nombres + ", apellidos=" + apellidos + '}';
    }
}