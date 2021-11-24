package unipiloto.edu.transportecargaplus.Main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

import unipiloto.edu.transportecargaplus.Cliente.InformacionCarga;
import unipiloto.edu.transportecargaplus.Controlador.ConexionSQLiteHelper;
import unipiloto.edu.transportecargaplus.Controlador.Utilidades;
import unipiloto.edu.transportecargaplus.Entidades.SolicitudVehicular;
import unipiloto.edu.transportecargaplus.Entidades.Usuario;
import unipiloto.edu.transportecargaplus.R;

public class InformacionUsuario extends AppCompatActivity {
    String s;
    ConexionSQLiteHelper conn;
    Bundle usuario;
    TextView nombre,apellido,email,cambiarContrasena,textcontraseña,textconfirmacion;
    EditText contrasena,confirmacioncontra;
    EditText telefono;
    String telefono2,sPassword,sEmail;
    Button actualizar;
    private ArrayList<Usuario> solicitudLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_usuario);
        usuario= getIntent().getExtras();
        s=usuario.getString("usuario");
        Log.d("prueba",s);
        nombre=findViewById(R.id.EditNombre);
        apellido=findViewById(R.id.EditApelliddo);
        email=findViewById(R.id.EditEmail);
        telefono=findViewById(R.id.EditTelefono);
        listaSolicitud(s);
        //boton
        actualizar=findViewById(R.id.buttonActualziar);

        //cambio de contraseña
        textcontraseña=findViewById(R.id.textViewContraseña);
        textconfirmacion=findViewById(R.id.textViewConfirmacionContr);
        contrasena=findViewById(R.id.editTextContrasena);
        confirmacioncontra=findViewById(R.id.editTextConfirmacioncontra);

        cambiarContrasena=findViewById(R.id.textCambiarContra);
        cambiarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cambiarContrasena.setVisibility(View.INVISIBLE);
                contrasena.setVisibility(View.VISIBLE);
                confirmacioncontra.setVisibility(View.VISIBLE);
                textconfirmacion.setVisibility(View.VISIBLE);
                textcontraseña.setVisibility(View.VISIBLE);

            }
        });

        //accion del boton actualizar
        sEmail="proyectapitransport@gmail.com";
        sPassword="Proyectoapi1";
        actualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(contrasena.getText().toString().equals(confirmacioncontra.getText().toString()) && !contrasena.getText().toString().equals("")){
                    actualizar(s,contrasena.getText().toString());
                    cambiarContrasena.setVisibility(View.VISIBLE);
                    contrasena.setVisibility(View.INVISIBLE);
                    confirmacioncontra.setVisibility(View.INVISIBLE);
                    textconfirmacion.setVisibility(View.INVISIBLE);
                    textcontraseña.setVisibility(View.INVISIBLE);
                    contrasena.setText("");
                    confirmacioncontra.setText("");
                    //alerta al usuario por correo del cmbio de contraseña



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
                        message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email.getText().toString().trim()) );
                        //email subjet
                        message.setSubject("INFROMACION DE SU CUENTA DE TRASPORTES ".trim());
                        //eMAIL MESSAGE
                        message.setText("Su contraseña fue actualizada en la aplicacacion : \n "+"\n"
                                +"Gracias por su atencion".trim());
                        //sen email

                        new SendMail().execute(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }




                }else{
                    if (!telefono.getText().toString().equals("") && !telefono.getText().toString().equals(telefono2)  ){
                        actualizartelefono(s);

                        //alerta al usuario por correo del cmbio de contraseña



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
                            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email.getText().toString().trim()) );
                            //email subjet
                            message.setSubject("INFROMACION DE SU CUENTA DE TRASPORTES ".trim());
                            //eMAIL MESSAGE
                            message.setText("Su contraseña fue actualizada en la aplicacacion : \n "+"\n"
                                    +"Gracias por su atencion".trim());
                            //sen email

                            new SendMail().execute(message);
                        } catch (MessagingException e) {
                            e.printStackTrace();
                        }



                    }else{

                        AlertaMensaje("No hay informacion que actualizar ");
                    }
                }


                contrasena.setText("");
                confirmacioncontra.setText("");
            }
        });
    }
    public void listaSolicitud(String name){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();

        solicitudLista= new ArrayList<Usuario>();
        //
        Cursor cursor=dbs.rawQuery("SELECT * FROM USUARIO WHERE IDUSUARIO=? ", new String[]{name});
        while (cursor.moveToNext()){

            nombre.setText(cursor.getString(1));
            apellido.setText(cursor.getString(2));
            email.setText(cursor.getString(3));
            telefono.setText(cursor.getString(7));
            telefono2=cursor.getString(7);

        }

    }

    public void actualizartelefono(String idusuario){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        SQLiteDatabase dbs = conn.getWritableDatabase();
        String[] parametro = {idusuario};
        ContentValues values =new ContentValues();
        Log.d("prueba1",idusuario);
        values.put(Utilidades.CAMPO_TELEFONO,telefono.getText().toString());
        dbs.update(Utilidades.TABLA_USUARIO,values,Utilidades.CAMPO_IDUSUARIO+"=?",parametro);
       // Toast.makeText(getApplicationContext(),"se actualizo",Toast.LENGTH_LONG).show();
        dbs.close();

    }
    public void actualizar(String idusuario,String pasword){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_usuarios",null,1);
        SQLiteDatabase dbs = conn.getWritableDatabase();
        String[] parametro = {idusuario};
        ContentValues values =new ContentValues();
        Log.d("prueba1",idusuario);
        values.put(Utilidades.CAMPO_TELEFONO,telefono.getText().toString());
        values.put(Utilidades.CAMPO_PASSWORD,pasword);
        dbs.update(Utilidades.TABLA_USUARIO,values,Utilidades.CAMPO_IDUSUARIO+"=?",parametro);
       // Toast.makeText(getApplicationContext(),"se actualizo",Toast.LENGTH_LONG).show();
        dbs.close();

    }
    public void AlertaMensaje(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(InformacionUsuario.this);
        builder.setCancelable(false);
        builder.setTitle(Html.fromHtml("<font color= '#509324'>INFORMACION DE SOLICITUD</font>"));
        builder.setMessage(msg);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();


            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_salir,menu);
        return true;
    }
    //asigancion del menu


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if (id== R.id.salir){
            finish();
        }else{

        }

        return super.onOptionsItemSelected(item);
    }




    private class SendMail extends AsyncTask<Message,String,String> {
        //
        private ProgressDialog progesDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //crear
            progesDialog =progesDialog.show( InformacionUsuario.this,"PLEASE WAIT",
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

                AlertDialog.Builder builder = new AlertDialog.Builder(InformacionUsuario.this);
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