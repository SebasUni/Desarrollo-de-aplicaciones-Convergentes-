package unipiloto.edu.transportecargaplus.Main;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import unipiloto.edu.transportecargaplus.Conductor.Viajes;
import unipiloto.edu.transportecargaplus.Controlador.ConexionSQLiteHelper;
import unipiloto.edu.transportecargaplus.Controlador.Utilidades;
import unipiloto.edu.transportecargaplus.Entidades.Usuario;
import unipiloto.edu.transportecargaplus.Propietario.AsignacionCarga;
import unipiloto.edu.transportecargaplus.R;

public class NewUser extends AppCompatActivity {
    EditText nombre,apellido,correo,password, infoidpropietario, telefono;
    TextView idpropietario;
    int Existe;
    private ArrayList<Usuario> UsuarioLista;
    private ArrayList<String> ListaUsurio;
    Spinner roles;
    String rol;
    private ArrayList<Usuario> UsuarioLista2;
    private ArrayList<Usuario> solicitudLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);
        nombre= findViewById(R.id.NewEdiNombre);
        apellido= findViewById(R.id.NewEdiApellido);
        correo= findViewById(R.id.NewEdiEmail);
        password= findViewById(R.id.NewEdiPassword);
        infoidpropietario= findViewById(R.id.NewEdiIdpropietario);
        idpropietario= findViewById(R.id.NewTextIdpropietario);
        telefono=findViewById(R.id.NewEdiTelfono);

        roles=findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.roles, android.R.layout.simple_spinner_item);
        roles.setAdapter(adapter);


        roles.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //rol.setText(parent.getItemAtPosition(position).toString());
                rol=parent.getItemAtPosition(position).toString();
                if (position==1){

                    idpropietario.setVisibility(View.VISIBLE);
                    infoidpropietario.setVisibility(View.VISIBLE);

                }else{

                   // idpropietario.setVisibility(View.INVISIBLE);
                    //infoidpropietario.setVisibility(View.INVISIBLE);
                   // infoidpropietario.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void registrarUsuario(View v){
        Busca();
        Log.d("prueba",Existe+"");
        if (Existe==1){
            alerta();
        }else{
            ListarUsuarios();
            ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_usuarios", null,1);
            SQLiteDatabase db= conn.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(Utilidades.CAMPO_IDUSUARIO,UsuarioLista.size()+1);
            values.put(Utilidades.CAMPO_NOMBRE,nombre.getText().toString());
            values.put(Utilidades.CAMPO_APELLIDO,apellido.getText().toString());
            values.put(Utilidades.CAMPO_CORREO,correo.getText().toString());
            values.put(Utilidades.CAMPO_PASSWORD,password.getText().toString());
            values.put(Utilidades.CAMPO_ROL,rol);
            values.put(Utilidades.CAMPO_IDPROPIETARIO,infoidpropietario.getText().toString());
            values.put(Utilidades.CAMPO_TELEFONO,telefono.getText().toString());
            Long idResultante=db.insert(Utilidades.TABLA_USUARIO,Utilidades.CAMPO_IDUSUARIO,values);

            Toast.makeText( getApplicationContext(),"Id Registro:"+ idResultante, Toast.LENGTH_SHORT).show();

            String sEmail = "proyectapitransport@gmail.com";
            String sPassword="Proyectoapi1";
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
                message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(correo.getText().toString().trim()) );
                Log.d("prueba",correo.toString());
                //email subjet
                message.setSubject("INFORMACION DE REGISTRO".trim());
                //eMAIL MESSAGE
                message.setText("Te has registrado correctamente : \n ".trim());
                //sen email

                new SendMail().execute(message);
            } catch (MessagingException e) {
                e.printStackTrace();
            }

        }




    }
    public void ListarUsuarios(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_usuarios", null, 1);
        SQLiteDatabase dbs = conn.getReadableDatabase();
        Usuario envio=null;
        UsuarioLista= new ArrayList<Usuario>();

        Cursor cursor=dbs.rawQuery("SELECT * FROM USUARIO", null);
        while (cursor.moveToNext()){
           envio=new Usuario();
           envio.setIdusuario(cursor.getString(0));
           envio.setNombre(cursor.getString(1));
           envio.setApellido(cursor.getString(2));
           envio.setEmail(cursor.getString(3));
           envio.setPassword(cursor.getString(4));
           envio.setIdpropietario(cursor.getString(5));
           UsuarioLista.add(envio);

        }

    }

    private void Busca(){
            Existe=0;
            ConexionSQLiteHelper conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
            SQLiteDatabase dbs = conn.getReadableDatabase();

            solicitudLista= new ArrayList<Usuario>();
            //
            Cursor cursor=dbs.rawQuery("SELECT * FROM USUARIO WHERE CORREO=? ", new String[]{correo.getText().toString()});
            while (cursor.moveToNext()){

                Existe=1;

            }




    }
    public  void alerta(){
        AlertDialog.Builder builder = new AlertDialog.Builder(NewUser.this);
        builder.setCancelable(false);
        builder.setTitle(Html.fromHtml("<font color= '#509324'>INFORMACION DE SOLICITUD</font>"));
        builder.setMessage("Usuario ya registrado ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //informar al usuario de que se recogio el paquete
                //actualizar informacion del usuario

                dialog.dismiss();


            }
        });
        builder.show();
    }
    private class SendMail extends AsyncTask<Message,String,String> {
        //
        private ProgressDialog progesDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //crear
            progesDialog =progesDialog.show( NewUser.this,"PLEASE WAIT",
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

                AlertDialog.Builder builder = new AlertDialog.Builder(NewUser.this);
                builder.setCancelable(false);
                builder.setTitle(Html.fromHtml("<font color= '#509324'>INFORMACION DE USUARIO</font>"));
                builder.setMessage("la actualizacion de datos se ha hecho correctamente .");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();


                    }
                });
                builder.show();
            }else{
                // Toast.makeText(getApplicationContext(),"Somenthing went wrong ?", Toast.LENGTH_SHORT).show();
                // Log.d("prueba",email);
            }
        }
    }


}
