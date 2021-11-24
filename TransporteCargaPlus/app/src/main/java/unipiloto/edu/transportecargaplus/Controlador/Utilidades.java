package unipiloto.edu.transportecargaplus.Controlador;

import java.security.PublicKey;
import java.security.SecureRandom;

public class Utilidades {

        //Constantes campos tabla usuario
        public static String TABLA_USUARIO="usuario";
        public static String CAMPO_IDUSUARIO="idusuario";
        public static String CAMPO_NOMBRE="nombre";
        public static String CAMPO_APELLIDO="apellido";
        public static String CAMPO_CORREO="correo";
        public static String CAMPO_PASSWORD="password";
        public static String CAMPO_ROL="rol";
        public static String CAMPO_IDPROPIETARIO="idpropietario";
        public static String CAMPO_TELEFONO="telefono";

        public static final String CREAR_TABLA_USUARIO="CREATE TABLE "+""+TABLA_USUARIO+"("+CAMPO_IDUSUARIO+" TEXT, "+CAMPO_NOMBRE+" TEXT,"+CAMPO_APELLIDO+" TEXT, "+CAMPO_CORREO+" TEXT,  "+CAMPO_PASSWORD+" TEXT, "+CAMPO_ROL+" TEXT,"+CAMPO_IDPROPIETARIO+" TEXT  ,"+CAMPO_TELEFONO+" TEXT)";

        //Constantes de cmapos de tambla vehiculos
        public static String TABLA_VEHICULO="vehiculos";
        public static String CAMPO_IDVEHICULO="idvehiculo";
        public static String CAMPO_PLACA="placa";
        public static String CAMPO_MARCA="marca";
        public static String CAMPO_IDCONDUCTOR="idconductor";
        public static String CAMPO_KMRECORRIDO="kmRecorrido";

        public static final String CREAR_TABLA_VEHICULO="CREATE TABLE "+""+TABLA_VEHICULO+"("+CAMPO_IDVEHICULO+" TEXT, "+CAMPO_PLACA+" TEXT,"+CAMPO_MARCA+" TEXT,"+CAMPO_IDPROPIETARIO+" TEXT,"+CAMPO_IDCONDUCTOR+" TEXT, "+CAMPO_KMRECORRIDO+" TEXT)";

        //solicitudes
        public  static String TABLA_SOLICITUD="solicitud";
        public  static String CAMPO_IDSOLICITUD="idsolicitud";
        public  static String CAMPO_DIRECCIONORIGEN="origen";
        public  static String CAMPO_DIRECCIONDESTINO="destino";
        public  static String CAMPO_TAMANOALTO="alto";
        public  static String CAMPO_TAMANOANCHO="ancho";
        public static String CAMPO_ESTADO="estado";
        public  static String CAMPO_HORARECOGIDA="horarecogida";
        public  static String CAMPO_HORALLEGADA="horallegada";
        public  static String CAMPO_UBICACION="ubicacion";

        //id vehiculo

        public static final String CREAR_TABLA_SOLICITUD="CREATE TABLE "+""+TABLA_SOLICITUD+"("+CAMPO_IDSOLICITUD+" TEXT, "+CAMPO_IDUSUARIO+" TEXT, "+CAMPO_DIRECCIONORIGEN+" TEXT, "+CAMPO_DIRECCIONDESTINO+" TEXT, "+CAMPO_TAMANOALTO+" TEXT, "+CAMPO_TAMANOANCHO+" TEXT, "+CAMPO_ESTADO+" TEXT, "+CAMPO_IDVEHICULO+" TEXT, "+CAMPO_HORARECOGIDA+" TEXT, "+CAMPO_HORALLEGADA+" TEXT, "+CAMPO_UBICACION+" TEXT )";

        //FICHA REGISTRO

       // public  static String TABLA_FICHA="ficha";
       // public  static String CAMPO_IDORDEN="idorden";
        //IDUSUARIO
       // public  static String CAMPO_HORARECOGIDA="origen";
        //public  static String CAMPO_HORALLEGADA="destino";
       // public  static String CAMPO_UBICACION="alto";

       // public static final String CREAR_TABLA_FICHA="CREATE TABLE "+""+TABLA_FICHA+"("+CAMPO_IDORDEN+" TEXT, "+CAMPO_IDUSUARIO+" TEXT, "+CAMPO_HORARECOGIDA+" TEXT, "+CAMPO_HORALLEGADA+" TEXT, "+CAMPO_UBICACION+" TEXT, )";

}
