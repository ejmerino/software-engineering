package ec.edu.monster.model;

/**
 * POJO (Molde) para recibir el objeto Cliente desde el servidor.
 * Se usa en el cliente de consola para GSON.
 */
public class Cliente {
    
    // Estos nombres deben coincidir con el JSON que envía el servidor
    private String cedula;
    private String nombres;
    private String apellidos;
    private String direccion;
    private String telefono;
    private String email;

    // Constructor vacío (necesario para GSON)
    public Cliente() {
    }

    // Getters y Setters
    
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
}