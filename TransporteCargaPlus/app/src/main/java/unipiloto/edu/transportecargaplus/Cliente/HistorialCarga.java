package unipiloto.edu.transportecargaplus.Cliente;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import unipiloto.edu.transportecargaplus.Controlador.ConexionSQLiteHelper;
import unipiloto.edu.transportecargaplus.Entidades.SolicitudVehicular;
import unipiloto.edu.transportecargaplus.Entidades.Vehiculo;
import unipiloto.edu.transportecargaplus.R;

public class HistorialCarga extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    String s;
    Bundle  IdCliente;
    private ArrayList<SolicitudVehicular> solicitudLista;
    private ArrayList<String> ListaSolicitud;
    Spinner informacionHistorial;
    TextView origen, destino,estado,idsolicitud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_carga);
        IdCliente= getIntent().getExtras();
        s=IdCliente.getString("IdCliente");
        Log.d("prueba",s);

        origen= findViewById(R.id.textOrigen);
        idsolicitud= findViewById(R.id.textIdSolicitud);
        destino=findViewById(R.id.textDestino);
        estado=findViewById(R.id.textEstado);

        informacionHistorial=findViewById(R.id.spinnerHistorial);
        listaSolicitud(IdCliente.getString("IdCliente"));
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,ListaSolicitud);
        informacionHistorial.setAdapter(adapter);

        informacionHistorial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if (position != 0){
                    idsolicitud.setText(solicitudLista.get(position-1).getIdSolitud());
                    origen.setText(solicitudLista.get(position-1).getDireccionOrigen());
                    destino.setText(solicitudLista.get(position-1).getDireccionDestino());
                    estado.setText(solicitudLista.get(position-1).getEstado());
                    if (estado.getText().toString().equals("CANCELADO")){
                        estado.setTextColor(Color.RED);
                    }else{
                        estado.setTextColor(Color.GREEN);
                    }

                }else{
                    idsolicitud.setText("");
                    origen.setText("");
                    destino.setText("");
                    estado.setText("");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        }
        return super.onOptionsItemSelected(item);
    }
}