package unipiloto.edu.transportecargaplus.Propietario;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
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
import unipiloto.edu.transportecargaplus.Main.NewUser;
import unipiloto.edu.transportecargaplus.R;

public class AsignacionCarga extends AppCompatActivity {
    String sEmail,sPassword,idusuario,email,emailConductor;
    String s;
    Bundle  IdPropietario;
    ConexionSQLiteHelper conn;
    Spinner spinerCarga,spinervehiculo;
    EditText origen,destino,dimenciones;
    Button asignar;
    private ArrayList<SolicitudVehicular> CargaLista;
    private ArrayList<String> ListaCarga;
    private ArrayList<Vehiculo> VehiculoLista;
    private ArrayList<String> ListaVehiculo;
    String idsolicitud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignacion_carga);
        IdPropietario= getIntent().getExtras();
        s= IdPropietario.getString("IdPropietario");
        Log.d("prueba",s);
        origen=findViewById(R.id.EditOrigenCarga);
        destino=findViewById(R.id.EditDestinoCarga);
        asignar=findViewById(R.id.buttonasiganrcarga);


        spinerCarga=findViewById(R.id.spinnerCarga);
        spinervehiculo=findViewById(R.id.spinnerVehiculos);
        listcarga("En proceso");
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,ListaCarga);
        spinerCarga.setAdapter(adapter);
        spinerCarga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position !=0){
                    origen.setText(CargaLista.get(position-1).getDireccionOrigen());
                    destino.setText(CargaLista.get(position-1).getDireccionDestino());
                    idsolicitud=CargaLista.get(position-1).getIdSolitud();
                    idusuario= CargaLista.get(position-1).getIdusuario();
                    BuscarUsuario(idusuario);
                }else{
                    origen.setText("");
                    destino.setText("");
                    idsolicitud="";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        sEmail="proyectapitransport@gmail.com";
        sPassword="Proyectoapi1";

        listaVehiculos(IdPropietario.getString("IdPropietario"));
        ArrayAdapter<CharSequence> adapter2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item,ListaVehiculo);
        spinervehiculo.setAdapter(adapter2);

        spinervehiculo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position !=0){
                   buscarConductor(VehiculoLista.get(position-1).getIdconductor());
                }else{


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        asignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             cliente();
             conductor();

            }
        });

        // email


    }
    public void cliente(){
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
                    +"Direccion de origen:"+origen.getText().toString()+"\n"
                    +"Direccion de destino: "+destino.getText().toString() +"\n"
                    +"Estado del envio: En proceso \n"
                    +"Numero de solicitud:"+idsolicitud+"\n"+"\n"
                    +"Gracias por su colaboracion y atencion \n".trim());
            //sen email

            new SendMail().execute(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        actualizarSolicitud();
    }

    public void conductor(){
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
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(emailConductor.trim()) );
            //email subjet
            message.setSubject("NUEVA CARGA ASIGANDA".trim());
            //eMAIL MESSAGE
            message.setText("Carga asiganda con la siguiente informacion: \n "+"\n"
                    +"Direccion de origen:"+origen.getText().toString()+"\n"
                    +"Direccion de destino: "+destino.getText().toString() +"\n"
                    +"Estado del envio: En proceso \n"
                    +"Numero de solicitud:"+idsolicitud+"\n"+"\n"
                    +"Gracias por su colaboracion y atencion \n".trim());
            //sen email

            new SendMail().execute(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        actualizarSolicitud();
    }
    public void buscarConductor(String id ){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();
        Usuario envio=null;
        //Usu= new ArrayList<Usuario>();
        //
        Cursor cursor=dbs.rawQuery("SELECT * FROM USUARIO WHERE IDUSUARIO=? ", new String[]{id});
        while (cursor.moveToNext()){
            envio=new Usuario();
            envio.setEmail(cursor.getString(3));
            emailConductor=cursor.getString(3);
        }
    }
    private class SendMail extends AsyncTask<Message,String,String> {
        //
        private ProgressDialog progesDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //crear
            progesDialog =progesDialog.show( AsignacionCarga.this,"PLEASE WAIT",
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

                AlertDialog.Builder builder = new AlertDialog.Builder(AsignacionCarga.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color= '#509324'>Success</font>"));
                builder.setMessage("mail sen successfully.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        atras();

                    }
                });
                builder.show();
            }else{
                Toast.makeText(getApplicationContext(),"Somenthing went wrong ?", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void listcarga(String name){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_solicitud",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();
        SolicitudVehicular envio=null;
        CargaLista= new ArrayList<SolicitudVehicular>();
        //
        Cursor cursor=dbs.rawQuery("SELECT * FROM SOLICITUD WHERE ESTADO=? ", new String[]{name});
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
            CargaLista.add(envio);

        }
        obtenerLista();
    }

    private void obtenerLista() {
        ListaCarga=new ArrayList<String>();
        ListaCarga.add("Slecciones");
        for (int i=0;i<CargaLista.size();i++){
            ListaCarga.add("ORIGEN: "+CargaLista.get(i).getDireccionOrigen());
        }
    }

    public void listaVehiculos(String name){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_vehiculos",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();
        Vehiculo envio=null;
        VehiculoLista= new ArrayList<Vehiculo>();
        //
        Cursor cursor=dbs.rawQuery("SELECT * FROM VEHICULOS WHERE IDPROPIETARIO=? ", new String[]{name});
        while (cursor.moveToNext()){
            envio=new Vehiculo();
            envio.setIdvehiculo(cursor.getString(0));
            envio.setPlaca(cursor.getString(1));
            envio.setMarca(cursor.getString(2));
            envio.setIdpropietario(cursor.getString(3));
            envio.setIdconductor(cursor.getString(4));
            VehiculoLista.add(envio);

        }
        obtenerLista2();
    }

    public void obtenerLista2() {
        ListaVehiculo=new ArrayList<String>();
        ListaVehiculo.add("Slecciones");
        for (int i=0;i<VehiculoLista.size();i++){
            ListaVehiculo.add("PLACA: "+VehiculoLista.get(i).getPlaca());
        }
    }

    public void actualizarSolicitud(){
             conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_solicitud",null,1);
            SQLiteDatabase dbs = conn.getWritableDatabase();
            String[] parametro = {idsolicitud};
            ContentValues values =new ContentValues();
            Log.d("prueba1",idsolicitud);
            values.put(Utilidades.CAMPO_ESTADO,"Carga Asignada");
             values.put(Utilidades.CAMPO_IDVEHICULO,"1");
            dbs.update(Utilidades.TABLA_SOLICITUD,values,Utilidades.CAMPO_IDSOLICITUD+"=?",parametro);
            Toast.makeText(getApplicationContext(),"se actualizo",Toast.LENGTH_LONG).show();
            dbs.close();




    }
    public void atras (){

        finish();

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




}