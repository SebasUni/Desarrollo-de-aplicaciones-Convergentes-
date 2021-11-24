package unipiloto.edu.transportecargaplus.Entidades;

public class Usuario {
    private String idusuario;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String rol;
    private String idpropietario;
    private String telefono;

    public Usuario(String idusuario, String nombre, String apellido, String email, String password, String rol, String idpropietario,String telefono) {
        this.idusuario = idusuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = email;
        this.password = password;
        this.rol = rol;
        this.idpropietario = idpropietario;
        this.telefono=telefono;
    }
    public Usuario() {

    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getIdpropietario() {
        return idpropietario;
    }

    public void setIdpropietario(String idpropietario) {
        this.idpropietario = idpropietario;
    }
}
