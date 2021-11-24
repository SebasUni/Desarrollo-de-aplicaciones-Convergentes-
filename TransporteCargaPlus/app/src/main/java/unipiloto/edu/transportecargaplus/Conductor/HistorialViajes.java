package unipiloto.edu.transportecargaplus.Conductor;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import unipiloto.edu.transportecargaplus.Controlador.ConexionSQLiteHelper;
import unipiloto.edu.transportecargaplus.Entidades.SolicitudVehicular;
import unipiloto.edu.transportecargaplus.Entidades.Vehiculo;
import unipiloto.edu.transportecargaplus.R;

public class HistorialViajes extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    private ArrayList<String> ListaCarga;
    private ArrayList<SolicitudVehicular> CargaLista;
    private ArrayList<Vehiculo> VehiculosLista;
    Spinner spinerViajesHistorial;
    Bundle  IdConductor;
    String s;
    TextView origen,destino,estado,hora;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_viajes);

        origen= findViewById(R.id.textHistorialOrigen);
        destino=findViewById(R.id.textHistorialDestino);
        estado=findViewById(R.id.textHistorialEstado);
        hora=findViewById(R.id.textHistorialHora);

        IdConductor= getIntent().getExtras();
        s= IdConductor.getString("IdConductor");
        Log.d("prueba",s);

        listvehiculos(IdConductor.getString("IdConductor"));
        listcarga(VehiculosLista.get(0).getIdvehiculo());

        spinerViajesHistorial =findViewById(R.id.spinerViajesHistorial);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,ListaCarga);
        spinerViajesHistorial.setAdapter(adapter);

        spinerViajesHistorial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position != 0){
                    origen.setText(CargaLista.get(position-1).getDireccionOrigen());
                    destino.setText(CargaLista.get(position-1).getDireccionDestino());
                    estado.setText(CargaLista.get(position-1).getEstado());
                    hora.setText(CargaLista.get(position-1).getHorallegada());

                }else{
                    origen.setText("");
                    destino.setText("");
                    estado.setText("");
                    hora.setText("");

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
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
            envio.setHorarecogida(cursor.getString(8));
            envio.setHorallegada(cursor.getString(9));
            envio.setUbicacion(cursor.getString(10));
            if(cursor.getString(6).equals("Entregado")){
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
}