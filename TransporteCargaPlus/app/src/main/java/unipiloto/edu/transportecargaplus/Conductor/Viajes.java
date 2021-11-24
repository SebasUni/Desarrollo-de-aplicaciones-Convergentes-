package unipiloto.edu.transportecargaplus.Conductor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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
import unipiloto.edu.transportecargaplus.Main.InformacionUsuario;
import unipiloto.edu.transportecargaplus.Propietario.AsignacionCarga;
import unipiloto.edu.transportecargaplus.R;

public class Viajes extends AppCompatActivity {
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    Location ubicacion;
    String s,sEmail,sPassword,email,id,idsolicitud;
    Bundle  IdConductor;
    ConexionSQLiteHelper conn;
    Button recoger,llevar;
    TextView origen, destino;
    private ArrayList<String> ListaCarga;
    private ArrayList<SolicitudVehicular> CargaLista;
    private ArrayList<Vehiculo> VehiculosLista;
    Spinner viajes;
    TextView distanceView;
    double kmRecorido;
    double distancia =0.0;
    private OdometerService odometer;
    private boolean bound= false;

    private ServiceConnection connection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder binder) {
            OdometerService.OdometerBinder odometerBinder= (OdometerService.OdometerBinder) binder;
            odometer=odometerBinder.getIdometer();
            bound=true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bound=false;

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viajes);
        distanceView=(TextView) findViewById(R.id.textView35);
        IdConductor= getIntent().getExtras();
        s= IdConductor.getString("IdConductor");
        Log.d("prueba",s);

        listvehiculos(IdConductor.getString("IdConductor"));
        listcarga(VehiculosLista.get(0).getIdvehiculo());

        origen= findViewById(R.id.textViewOrigen);
        destino=findViewById(R.id.textViewDestino);
        viajes=findViewById(R.id.spinnerVieajes);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,ListaCarga);
        viajes.setAdapter(adapter);

        //spiner de informacion
        viajes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position !=0 ){
                    origen.setText(CargaLista.get(position-1).getDireccionOrigen());
                    destino.setText(CargaLista.get(position-1).getDireccionDestino());
                    id=CargaLista.get(position-1).getIdusuario();
                    InfoUsuario(id);
                    idsolicitud=CargaLista.get(position-1).getIdSolitud();
                    if (CargaLista.get(position-1).getEstado().equals("Entregado")){
                        recoger.setVisibility(View.INVISIBLE);
                        llevar.setVisibility(View.INVISIBLE);
                    }else {
                        recoger.setVisibility(View.VISIBLE);
                        llevar.setVisibility(View.VISIBLE);
                        if (CargaLista.get(position-1).getEstado().equals("Recogida")){
                            recoger.setVisibility(View.INVISIBLE);

                        }else {
                            recoger.setVisibility(View.VISIBLE);
                            llevar.setVisibility(View.INVISIBLE);
                        }
                    }

                }else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //botones
        recoger=findViewById(R.id.buttonRecoger);
        recoger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!origen.getText().toString().equals("")){
                    mapas(origen.getText().toString());
                    miUbicacion();
                    AlertaMensaje(1);
                }

            }
        });

        llevar=findViewById(R.id.buttonEntrega);
        llevar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!destino.getText().toString().equals("")){
                    mapas(destino.getText().toString());
                    miUbicacion();
                    AlertaMensaje(2);
                }
            }
        });

        //CUENTA KILOMETROS
        mostratDistancia();


    }
    //tenemos las cargas asignadas al con conductor
    public void listcarga(String name){
        String carga ="Carga Asignada";
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_solicitud",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();
        SolicitudVehicular envio=null;
        CargaLista= new ArrayList<SolicitudVehicular>();
        //
        Cursor cursor=dbs.rawQuery("SELECT * FROM SOLICITUD WHERE IDVEHICULO=? ", new String[]{name});
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
            if(!cursor.getString(6).equals("Entregado")){
                CargaLista.add(envio);

            }


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

    // optenemos el id del vehiculo con el id del conductor
    public void listvehiculos(String name){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_vehiculos",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();
        Vehiculo envio=null;
        VehiculosLista= new ArrayList<Vehiculo>();
        //
        Cursor cursor=dbs.rawQuery("SELECT IDVEHICULO FROM VEHICULOS WHERE IDCONDUCTOR=? ", new String[]{name});
        while (cursor.moveToNext()){
            envio=new Vehiculo();
            envio.setIdvehiculo(cursor.getString(0));
            VehiculosLista.add(envio);

        }

    }
    //hacemos la rutas pertinentes
    public void mapas(String direccion){
        Uri uri = Uri.parse("geo:0,0?q="+direccion);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        if (intent.resolveActivity(getPackageManager())!= null){
            startActivity(intent);

        }


    }
    private void miUbicacion() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        ubicacion=location;
        Log.d("ubicacion", String.valueOf(location.getLongitude()));

    }
    public void AlertaMensaje(int r){
        AlertDialog.Builder builder = new AlertDialog.Builder(Viajes.this);
        builder.setCancelable(false);
        builder.setTitle(Html.fromHtml("<font color= '#509324'>INFORMACION DE SOLICITUD</font>"));
        builder.setMessage("Se recogio el paquete ");
        builder.setPositiveButton("Si ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //informar al usuario de que se recogio el paquete
                //actualizar informacion del usuario
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentDateandTime = simpleDateFormat.format(new Date());
                Log.d("hora",currentDateandTime);
                actualizar(r,currentDateandTime);
                actualizarKm();
                distancia =0.0;
                if (r==1){
                    EmailEnviar("Recogido",0);
                }else{
                    EmailEnviar("Entregado",0);
                }

                dialog.dismiss();


            }
        });
        builder.setNegativeButton("No ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Viajes.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color= '#509324'>INFORMACION DE SOLICITUD</font>"));
                builder.setMessage("Se recogio el paquete ");
                builder.setPositiveButton("Si ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //informar al usuario de que se recogio el paquete
                        //actualizar informacion del usuario
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                        String currentDateandTime = simpleDateFormat.format(new Date());
                        Log.d("hora",currentDateandTime);
                        actualizar(r,currentDateandTime);
                        if (r==1){
                            EmailEnviar("Recogido",0);
                        }else{
                            EmailEnviar("Entregado",0);
                        }

                        dialog.dismiss();


                    }
                });
                builder.setNegativeButton("No ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();


                    }
                });


            }
        });


        builder.show();
    }

    //tenemos el email del usuario
    public void InfoUsuario(String name){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();


        //
        Cursor cursor=dbs.rawQuery("SELECT * FROM USUARIO WHERE IDUSUARIO=? ", new String[]{name});
        while (cursor.moveToNext()){

            email=cursor.getString(3);
            Log.d("email: ", email.toString());
        }

    }

    //actualizar informacion de la solicitud
    public void actualizar(int n,String Hora){
        miUbicacion();
        String ubi= ubicacion.getLatitude() +","+ ubicacion.getLongitude();
        Log.d("ubicacion2: ", ubi);
        if (n==1){
            conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_solicitud",null,1);
            SQLiteDatabase dbs = conn.getWritableDatabase();
            String[] parametro = {idsolicitud};
            ContentValues values =new ContentValues();
            Log.d("prueba1",idsolicitud);

            values.put(Utilidades.CAMPO_ESTADO,"Recogida");
            values.put(Utilidades.CAMPO_HORARECOGIDA,Hora);
            values.put(Utilidades.CAMPO_UBICACION,ubi);

            dbs.update(Utilidades.TABLA_SOLICITUD,values,Utilidades.CAMPO_IDSOLICITUD+"=?",parametro);
            Toast.makeText(getApplicationContext(),"se actualizo",Toast.LENGTH_LONG).show();
            dbs.close();
        }else{
            conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_solicitud",null,1);
            SQLiteDatabase dbs = conn.getWritableDatabase();
            String[] parametro = {idsolicitud};
            ContentValues values =new ContentValues();
            Log.d("prueba1",idsolicitud);

            values.put(Utilidades.CAMPO_ESTADO,"Entregado");
            values.put(Utilidades.CAMPO_HORALLEGADA,Hora);
            values.put(Utilidades.CAMPO_UBICACION,ubi);

            dbs.update(Utilidades.TABLA_SOLICITUD,values,Utilidades.CAMPO_IDSOLICITUD+"=?",parametro);
            Toast.makeText(getApplicationContext(),"se actualizo",Toast.LENGTH_LONG).show();
            dbs.close();
        }



    }
 public void actualizarKm(){
         conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_vehiculos",null,1);
         SQLiteDatabase dbss = conn.getReadableDatabase();
         double km=0.0;

         //
         Cursor cursor=dbss.rawQuery("SELECT * FROM VEHICULOS WHERE IDCONDUCTOR=? ", new String[]{s});
         while (cursor.moveToNext()){

             km=cursor.getDouble(5);

         }
        double total =km + kmRecorido;

         conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_vehiculos",null,1);
         SQLiteDatabase dbs = conn.getWritableDatabase();
         String[] parametro = {s};
         ContentValues values =new ContentValues();
         Log.d("prueba1",s);

         values.put(Utilidades.CAMPO_KMRECORRIDO,total);


         dbs.update(Utilidades.TABLA_VEHICULO,values,Utilidades.CAMPO_IDCONDUCTOR+"=?",parametro);
         Toast.makeText(getApplicationContext(),"se actualizo",Toast.LENGTH_LONG).show();

         dbs.close();
 }

    protected  void onStart(){
        super.onStart();
        Intent intent= new Intent(this, OdometerService.class);
        bindService(intent,connection,Context.BIND_AUTO_CREATE);
    }
    protected  void onStop(){
        super.onStop();
        if(bound){
            unbindService(connection);
            bound=false;
        }
    }
    //mostreat cuentakilometros

    public void mostratDistancia(){

        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                distancia=0.0;
                if (bound && odometer != null){
                    distancia=odometer.getDistannce();
                    kmRecorido= distancia;
                }
                String distanciaStr= String.format(Locale.getDefault(),"%1$.2f", distancia);
                Log.d("distancia: ", distanciaStr);
                distanceView.setText(distanciaStr);
                handler.postDelayed(this,1000);
            }
        });
    }

    public void Reportar(View view){
        EmailEnviar("En proceso",1);
    }

    //enviar el email al usuario notificando
    public void EmailEnviar(String estado,int n){
        InfoUsuario(id);
        sEmail="proyectapitransport@gmail.com";
        sPassword="Proyectoapi1";
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
            if(n==0){
                //mensaje sender
                Message message= new MimeMessage(session);
                message.setFrom(new InternetAddress(sEmail));
                //recipient email

                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email.trim()) );
                //email subjet
                message.setSubject("INFROMACION DE SU PEDIDO".trim());
                //eMAIL MESSAGE
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentDateandTime = simpleDateFormat.format(new Date());
                Log.d("hora",currentDateandTime);
                message.setText("Su pedido ya fue recogido: \n "+"\n"
                        +"Direccion de origen:"+origen.getText().toString()+"\n"
                        +"Direccion de destino: "+destino.getText().toString() +"\n"
                        +"Estado del envio: "+estado+" \n"
                        +"Numero de solicitud:"+idsolicitud+"\n"
                        +"El dia:"+currentDateandTime+"\n"
                        +"Para saber en donde se encuentra su paquete inngrese al link, el cual la llevara a la ultima ubicacion reportada: https://maps.google.com/maps?daddr="+ubicacion.getLatitude()+","+ubicacion.getLongitude() +"\n"
                        +"Gracias por su colaboracion y atencion \n".trim());
                //sen email

                new SendMail().execute(message);
            }else{
                //mensaje sender
                Message message= new MimeMessage(session);
                message.setFrom(new InternetAddress(sEmail));
                //recipient email

                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email.trim()) );
                //email subjet
                message.setSubject("INFROMACION DE SU PEDIDO".trim());
                //eMAIL MESSAGE
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                String currentDateandTime = simpleDateFormat.format(new Date());
                Log.d("hora",currentDateandTime);
                message.setText("El repartidor esta teniendo problemas para llegar a su destino: \n "+"\n"
                        +"Direccion de origen:"+origen.getText().toString()+"\n"
                        +"Direccion de destino: "+destino.getText().toString() +"\n"
                        +"Estado del envio: "+estado+" \n"
                        +"Numero de solicitud:"+idsolicitud+"\n"
                        +"El dia:"+currentDateandTime+"\n"
                        +"Gracias por su colaboracion y atencion \n".trim());
                //sen email

                new SendMail().execute(message);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }


    }
    private class SendMail extends AsyncTask<Message,String,String> {
        //
        private ProgressDialog progesDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //crear
            progesDialog =progesDialog.show( Viajes.this,"PLEASE WAIT",
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

                AlertDialog.Builder builder = new AlertDialog.Builder(Viajes.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color= '#509324'>Success</font>"));
                builder.setMessage("mail sen successfully.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //atras();

                    }
                });
                builder.show();
            }else{
                Toast.makeText(getApplicationContext(),"Somenthing went wrong ?", Toast.LENGTH_SHORT).show();
            }
        }
    }
}