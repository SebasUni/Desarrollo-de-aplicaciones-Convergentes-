package unipiloto.edu.transportecargaplus.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ShareActionProvider;
import androidx.core.view.MenuItemCompat;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import unipiloto.edu.transportecargaplus.Controlador.ConexionSQLiteHelper;
import unipiloto.edu.transportecargaplus.Controlador.Utilidades;
import unipiloto.edu.transportecargaplus.Entidades.SolicitudVehicular;
import unipiloto.edu.transportecargaplus.Entidades.Vehiculo;
import unipiloto.edu.transportecargaplus.R;

public class SolicitarTransporte extends AppCompatActivity {

    EditText origen,destino,alto,ancho;
    private ArrayList<SolicitudVehicular> SolicitudLista;
    Bundle IdCliente;
    String idcliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitar_transporte);
        IdCliente= getIntent().getExtras();
        idcliente=IdCliente.getString("IdCliente");


        origen=findViewById(R.id.EditOrigen);
        destino=findViewById(R.id.EditDestino);
        alto=findViewById(R.id.EditAlto);
        ancho=findViewById(R.id.EditAncho);
    }
    public void ListarSolicitud(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_solicitud", null, 1);
        SQLiteDatabase dbs = conn.getReadableDatabase();

        SolicitudVehicular envio;
        SolicitudLista= new ArrayList<>();
        Cursor cursor= dbs.rawQuery("SELECT * FROM SOLICITUD",null);
        while (cursor.moveToNext()){
            envio=new SolicitudVehicular();
            envio.setIdSolitud(cursor.getString(0));
            envio.setDireccionOrigen(cursor.getString(1));
            envio.setDireccionDestino(cursor.getString(2));
            envio.setTamanoAlto(cursor.getString(3));
            envio.setTamanoAlto(cursor.getString(4));

            SolicitudLista.add(envio);
        }
    }
    public void AgregarSolicitudes(View v){
        ListarSolicitud();
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_solicitud", null,1);
        SQLiteDatabase db= conn.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Utilidades.CAMPO_IDSOLICITUD,SolicitudLista.size()+1);
        values.put(Utilidades.CAMPO_IDUSUARIO,idcliente);
        values.put(Utilidades.CAMPO_DIRECCIONORIGEN,origen.getText().toString());
        values.put(Utilidades.CAMPO_DIRECCIONDESTINO,destino.getText().toString());
        values.put(Utilidades.CAMPO_TAMANOALTO,alto.getText().toString());
        values.put(Utilidades.CAMPO_TAMANOANCHO,ancho.getText().toString());
        values.put(Utilidades.CAMPO_IDVEHICULO,"Sin Asignar");
        values.put(Utilidades.CAMPO_ESTADO,"En proceso");

        Long idResultante=db.insert(Utilidades.TABLA_SOLICITUD,Utilidades.CAMPO_IDVEHICULO,values);

        Toast.makeText( getApplicationContext(),"Id Registro:"+ idResultante, Toast.LENGTH_SHORT).show();
        AlertaMensaje("Su solicitudd se ha hecho correctamente.");

    }
    public void AlertaMensaje(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(SolicitarTransporte.this);
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



    //clase
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_salir,menu);
        getMenuInflater().inflate(R.menu.menu_compartir,menu);
        return true;
    }
    //asigancion del menu


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if (id== R.id.salir){
            finish();
        }else{
            if (id== R.id.agregarcarga){
                Intent intent = new Intent(this, SolicitarTransporte.class);
                intent.putExtra("IdCliente",IdCliente.getString("IdCliente"));
                startActivity(intent);
            }else{
                if (id== R.id.itemcompartir){
                        if (!origen.getText().toString().equals("") && !destino.getText().toString().equals("")){

                            Intent i = new Intent (Intent.ACTION_SEND);
                            i.setType ("texto / plano");
                            i.putExtra (Intent.EXTRA_SUBJECT, getResources (). getString (R.string.app_name));
                            String aux = "Informacion de solicitud de transporte \n";
                            aux = aux + "Origen: "+origen.getText().toString()+"\n"+"Destino: "+destino.getText().toString()+"\n"+"Estado: En proceso \n"+
                                    "Gracias por su atencion ";
                            i.putExtra (Intent.EXTRA_TEXT, aux);
                            startActivity (i);
                        }else{
                            AlertaMensaje("No puedes compartir dado a que no has rellenado la informacion solicitada");
                        }



                }
            }
        }

         return super.onOptionsItemSelected(item);
    }



}