package unipiloto.edu.transportecargaplus.Propietario;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import unipiloto.edu.transportecargaplus.Controlador.ConexionSQLiteHelper;
import unipiloto.edu.transportecargaplus.Entidades.Usuario;
import unipiloto.edu.transportecargaplus.Entidades.Vehiculo;
import unipiloto.edu.transportecargaplus.R;

public class KmRecorrido extends AppCompatActivity {

    private Bundle IdPropietario;
    int viajes=0;
    ArrayList<Vehiculo> VehiculosLista;
    ArrayList<String> ListaVehiculos;
    Spinner SpinerListaVehiculos;
    ConexionSQLiteHelper conn;
    TextView placa,marca,conductor,consumo,km,ViajesRealizados;
    String s;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_km_recorrido);
        IdPropietario= getIntent().getExtras();
        s = IdPropietario.getString("IdPropietario");
        Log.d("prueba",s);

        placa=findViewById(R.id.EdInfoPlaca);
        marca= findViewById(R.id.EdInfoMarca);
        conductor=findViewById(R.id.EdInfoConductor);
        km=findViewById(R.id.EdInfoKmRecorrido);
        consumo=findViewById(R.id.EdInfoConsumo);
        ViajesRealizados= findViewById(R.id.EdInfoViajes);


        SpinerListaVehiculos=findViewById(R.id.spinnerVehiculosInforme);

        listvehiculos(IdPropietario.getString("IdPropietario"));
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,ListaVehiculos);
        SpinerListaVehiculos.setAdapter(adapter);

        SpinerListaVehiculos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (position != 0){
                    placa.setText(VehiculosLista.get(position-1).getPlaca());
                    marca.setText(VehiculosLista.get(position-1).getMarca());
                    conductor.setText(VehiculosLista.get(position-1).getIdconductor());
                    km.setText(VehiculosLista.get(position-1).getKmRecorrido());
                    double cunsumo= Double.parseDouble(VehiculosLista.get(position-1).getKmRecorrido());
                    double total =0.11*cunsumo;
                    Log.d("consumo",total+"");
                    consumo.setText(String.format("%.3f", total)+"L");
                    ViajesRealizados(VehiculosLista.get(position-1).getIdvehiculo());
                    ViajesRealizados.setText(viajes+"");

                }else{

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void listvehiculos(String name){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_vehiculos",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();
        Vehiculo envio=null;
        VehiculosLista= new ArrayList<Vehiculo>();
        //
        Cursor cursor=dbs.rawQuery("SELECT * FROM VEHICULOS WHERE IDPROPIETARIO=? ", new String[]{name});
        while (cursor.moveToNext()){
            envio=new Vehiculo();
            envio.setIdvehiculo(cursor.getString(0));
            envio.setPlaca(cursor.getString(1));
            envio.setMarca(cursor.getString(2));
            envio.setIdpropietario(cursor.getString(3));
            envio.setIdconductor(cursor.getString(4));
            envio.setKmRecorrido(cursor.getString(5));
            VehiculosLista.add(envio);

        }
        obtenerLista();
    }
    private void obtenerLista() {
        ListaVehiculos=new ArrayList<String>();
        ListaVehiculos.add("Slecciones");
        for (int i=0;i<VehiculosLista.size();i++){
            ListaVehiculos.add("Placa: "+VehiculosLista.get(i).getPlaca());
        }

    }
    public void ViajesRealizados(String id){
        conn= new ConexionSQLiteHelper(getApplicationContext(),"bd_solicitud",null,1);
        SQLiteDatabase dbs = conn.getReadableDatabase();


        //
        Cursor cursor=dbs.rawQuery("SELECT * FROM SOLICITUD WHERE IDVEHICULO=? ", new String[]{id});
        while (cursor.moveToNext()){

           if(cursor.getString(6).equals("Entregado")){
               viajes=viajes+1;
           }

        }


    }
}