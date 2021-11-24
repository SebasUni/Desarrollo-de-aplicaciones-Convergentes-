package unipiloto.edu.transportecargaplus.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import unipiloto.edu.transportecargaplus.Controlador.ConexionSQLiteHelper;
import unipiloto.edu.transportecargaplus.Controlador.Utilidades;
import unipiloto.edu.transportecargaplus.Entidades.SolicitudVehicular;
import unipiloto.edu.transportecargaplus.Entidades.Usuario;
import unipiloto.edu.transportecargaplus.Entidades.Vehiculo;
import unipiloto.edu.transportecargaplus.Propietario.AsignacionCarga;
import unipiloto.edu.transportecargaplus.R;

public class InformacionCarga extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    String s,idsolicitud;
    Bundle  IdPropietario;
    Button cancelar;
    private ArrayList<SolicitudVehicular> solicitudLista;
    private ArrayList<String> ListaSolicitud;
    Spinner informacion;
    EditText origen, destino,estado,placa,marca;
    private ArrayList<Vehiculo> VehiculoLista;
   String sEmail,sPassword,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_carga);
        IdPropietario= getIntent().getExtras();
        s= IdPropietario.getString("IdCliente");
        Log.d("pruebausuario",s);
        origen=findViewById(R.id.EditOrigeninformacion);
        destino=findViewById(R.id.EditDestinoInformacion);
        estado=findViewById(R.id.EditEstadoInformacion);
        placa=findViewById(R.id.EditPlacaInfromacion);
        marca=findViewById(R.id.EditMarcaInformacion);
        cancelar=findViewById(R.id.button9);

        informacion=findViewById(R.id.spinnerListasoliciud);
        listaSolicitud(IdPropietario.getString("IdCliente"));
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,ListaSolicitud);
        informacion.setAdapter(adapter);

        informacion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position !=0){
                    origen.setText(solicitudLista.get(position-1).getDireccionOrigen());
                    destino.setText(solicitudLista.get(position-1).getDireccionDestino());
                    estado.setText(solicitudLista.get(position-1).getEstado());
                    idsolicitud=solicitudLista.get(position-1).getIdSolitud();
                    BuscarUsuario(solicitudLista.get(position-1).getIdusuario());
                    if(estado.getText().toString().equals("En proceso") || estado.getText().toString().equals("CANCELADO")){
                        estado.setTextColor(Color.RED);
                    }else{
                        estado.setTextColor(Color.GREEN);
                    }

                    solicitudLista.get(position-1).getIdvehiculo();
                    if (!solicitudLista.get(position-1).getIdvehiculo().equals("Sin Asignar")){
                        listaSolicitud2(solicitudLista.get(position-1).getIdvehiculo());
                          placa.setText(VehiculoLista.get(0).getPlaca());
                         marca.setText(VehiculoLista.get(0).getMarca());
                        Log.d("prueba","entro");
                    }else{
                        placa.setText("Vehiculo sin asignar");
                        marca.setText("Vehiculo sin asignar");
                    }

                }else{
                    origen.setText("");
                    destino.setText("");
                    estado.setText("");
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sEmail="proyectapitransport@gmail.com";
        sPassword="Proyectoapi1";
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if (!estado.getText().toString().equals("CANELADO") && !estado.getText().toString().equals("")){


                //inicializacion propiedades email
                Properties properties= new Properties();
                properties.put("mail.smtp.auth","true");
                properties.put("mail.smtp.starttls.enable","true");
                properties.put("mail.smtp.host","smtp.gmail.com");
                properties.put("mail.smtp.port","587");

                //inicializ

                Session session = Session.getInstance(properties, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sEmail,sPassword);
                    }
                });

                try {
                    //mensaje sender
                    Message message= new MimeMessage(session);
                    message.setFrom(new InternetAddress(sEmail));
                    //recipient email
                    message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email.trim()) );
                    //email subjet
                    message.setSubject("INFROMACION DE SU PEDIDO".trim());
                    //eMAIL MESSAGE
                    message.setText("Su pedido ya fue asignado a un repartidor con la siguiente informacion: \n "+"\n"
                            +"Su pedido fue cancelado por su propia decicion".trim());
                    //sen email

                    new SendMail().execute(message);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }

                actualizarSolicitud();



            }else{
                AlertaMensaje("No puede CANCELAR una solicitud ya CANCELADA");
            }
            }
        });

    }
    private class SendMail extends AsyncTask<Message,String,String> {
        //
        private ProgressDialog progesDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //crear
            progesDialog =progesDialog.show( InformacionCarga.this,"PLEASE WAIT",
                    "Sending Mial....",true,false);
        }

        @Override
        protected String doInBackground(Message... messages) {
            try {

                Transport.send(messages[0]);
                return "Success";
            } catch (MessagingException e) {

                e.printStackTrace();
                return "Error";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progesDialog.dismiss();
            if (s.equals("Success")){

                AlertDialog.Builder builder = new AlertDialog.Builder(InformacionCarga.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color= '#509324'>INFORMACION DE SOLICITUD</font>"));
                builder.setMessage("La CANCELACION se ha hecho con exito, revise su correo.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();

                    }
                });
                builder.show();
            }else{
               // Toast.makeText(getApplicationContext(),"Somenthing went wrong ?", Toast.LENGTH_SHORT).show();
               // Log.d("prueba",email);
            }
        }
    }

    public void listaSolicitud(String name){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_solicitud",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();
        SolicitudVehicular envio=null;
        solicitudLista= new ArrayList<SolicitudVehicular>();
        //
        Cursor cursor=dbs.rawQuery("SELECT * FROM SOLICITUD WHERE IDUSUARIO=? ", new String[]{name});
        while (cursor.moveToNext()){
            envio=new SolicitudVehicular();
            envio.setIdSolitud(cursor.getString(0));
            envio.setIdusuario(cursor.getString(1));
            envio.setDireccionOrigen(cursor.getString(2));
            envio.setDireccionDestino(cursor.getString(3));
            envio.setTamanoAlto(cursor.getString(4));
            envio.setTamanoAncho(cursor.getString(5));
            envio.setEstado(cursor.getString(6));
            envio.setIdvehiculo(cursor.getString(7));

            solicitudLista.add(envio);

        }
        obtenerLista();
    }

    public void obtenerLista() {
        ListaSolicitud=new ArrayList<String>();
        ListaSolicitud.add("Slecciones");
        for (int i=0;i<solicitudLista.size();i++){
            ListaSolicitud.add("Origen: "+solicitudLista.get(i).getDireccionOrigen());
        }
    }

    public void listaSolicitud2(String name){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_vehiculos",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();
        Vehiculo envio=null;
        VehiculoLista= new ArrayList<Vehiculo>();
        //
        Cursor cursor=dbs.rawQuery("SELECT * FROM VEHICULOS WHERE IDVEHICULO=? ", new String[]{name});
        while (cursor.moveToNext()){
            envio=new Vehiculo();
            envio.setIdvehiculo(cursor.getString(0));
            envio.setPlaca(cursor.getString(1));
            envio.setMarca(cursor.getString(2));
            envio.setIdpropietario(cursor.getString(3));
            envio.setIdconductor(cursor.getString(4));
            VehiculoLista.add(envio);

        }
        obtenerLista();
    }

    public void actualizarSolicitud(){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_solicitud",null,1);
        SQLiteDatabase dbs = conn.getWritableDatabase();
        String[] parametro = {idsolicitud};
        ContentValues values =new ContentValues();
        Log.d("prueba1",idsolicitud);
        values.put(Utilidades.CAMPO_ESTADO,"CANCELADO");
        //values.put(Utilidades.CAMPO_IDVEHICULO,"1");
        dbs.update(Utilidades.TABLA_SOLICITUD,values,Utilidades.CAMPO_IDSOLICITUD+"=?",parametro);
        Toast.makeText(getApplicationContext(),"se actualizo",Toast.LENGTH_LONG).show();
        dbs.close();

    }
    public void BuscarUsuario(String id){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();
        Usuario envio=null;
        //Usu= new ArrayList<Usuario>();
        //
        Cursor cursor=dbs.rawQuery("SELECT * FROM USUARIO WHERE IDUSUARIO=? ", new String[]{id});
        while (cursor.moveToNext()){
            envio=new Usuario();
            envio.setEmail(cursor.getString(3));
            email=cursor.getString(3);
        }

    }
    public void AlertaMensaje(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(InformacionCarga.this);
        builder.setCancelable(false);
        builder.setTitle(Html.fromHtml("<font color= '#509324'>INFORMACION DE SOLICITUD</font>"));
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();

            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_salir,menu);
        getMenuInflater().inflate(R.menu.menu_eliminar,menu);
        return true;
    }
    //asigancion del menu


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if (id== R.id.salir){
            finish();
        }else{
            if (id == R.id.eliminaritem){
                if (!estado.getText().toString().equals("CANCELADO") && !estado.getText().toString().equals("")){


                    //inicializacion propiedades email
                    Properties properties= new Properties();
                    properties.put("mail.smtp.auth","true");
                    properties.put("mail.smtp.starttls.enable","true");
                    properties.put("mail.smtp.host","smtp.gmail.com");
                    properties.put("mail.smtp.port","587");

                    //inicializ

                    Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(sEmail,sPassword);
                        }
                    });

                    try {
                        //mensaje sender
                        Message message= new MimeMessage(session);
                        message.setFrom(new InternetAddress(sEmail));
                        //recipient email
                        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email.trim()) );
                        //email subjet
                        message.setSubject("INFROMACION DE SU PEDIDO".trim());
                        //eMAIL MESSAGE
                        message.setText("Su pedido ya fue asignado a un repartidor con la siguiente informacion: \n "+"\n"
                                +"Su pedido fue cancelado por su propia decicion".trim());
                        //sen email

                        new SendMail().execute(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

                    actualizarSolicitud();

                }else{
                    AlertaMensaje("No puede CANCELAR esta solicitud");
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}