package unipiloto.edu.transportecargaplus.Entidades;

public class FichaRastreo {
    private String idorden;
    private String idusuario;
    private String horarecogida;
    private String horallegada;
    private String ubicacion;

    public FichaRastreo(String idorden, String idusuario, String horarecogida, String horallegada, String ubicacion) {
        this.idorden = idorden;
        this.idusuario = idusuario;
        this.horarecogida = horarecogida;
        this.horallegada = horallegada;
        this.ubicacion = ubicacion;
    }

    public String getIdorden() {
        return idorden;
    }

    public void setIdorden(String idorden) {
        this.idorden = idorden;
    }

    public String getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(String idusuario) {
        this.idusuario = idusuario;
    }

    public String getHorarecogida() {
        return horarecogida;
    }

    public void setHorarecogida(String horarecogida) {
        this.horarecogida = horarecogida;
    }

    public String getHorallegada() {
        return horallegada;
    }

    public void setHorallegada(String horallegada) {
        this.horallegada = horallegada;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }
}
