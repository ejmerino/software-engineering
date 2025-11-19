package ec.edu.monster.model;

// POJO para el Cliente de la tienda
public class Cliente {

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
    private String cedula;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String email;

    // (Añadir Getters y Setters para todos los campos)
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    // ... etc. ...
}