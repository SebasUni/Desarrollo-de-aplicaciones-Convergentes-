package unipiloto.edu.transportecargaplus.Propietario;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import unipiloto.edu.transportecargaplus.Controlador.ConexionSQLiteHelper;
import unipiloto.edu.transportecargaplus.Controlador.Utilidades;
import unipiloto.edu.transportecargaplus.Entidades.Vehiculo;
import unipiloto.edu.transportecargaplus.R;

public class AgregarCarro extends AppCompatActivity {

    private ArrayList<Vehiculo> VehiculosLista;
    String s;
    Bundle  IdPropietario;
    EditText placa, marca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_carro);
        IdPropietario= getIntent().getExtras();
        s= IdPropietario.getString("IdPropietario");
        Log.d("prueba",s);
        placa= findViewById(R.id.AgreCaTextPlaca);
        marca= findViewById(R.id.AgreCaTextMarca);
    }
    public void ListarVehiculos(){
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_vehiculos", null, 1);
        SQLiteDatabase dbs = conn.getReadableDatabase();

        Vehiculo envio;
        VehiculosLista= new ArrayList<>();
        Cursor cursor= dbs.rawQuery("SELECT * FROM VEHICULOS",null);
        while (cursor.moveToNext()){
            envio=new Vehiculo();
            envio.setIdvehiculo(cursor.getString(0));
            envio.setPlaca(cursor.getString(1));
            envio.setMarca(cursor.getString(2));
            VehiculosLista.add(envio);
        }
    }
    public void AgregarVehiculo(View v){
        ListarVehiculos();
        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this,"bd_vehiculos", null,1);
        SQLiteDatabase db= conn.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(Utilidades.CAMPO_IDVEHICULO,VehiculosLista.size()+1);
        values.put(Utilidades.CAMPO_PLACA,placa.getText().toString());
        values.put(Utilidades.CAMPO_MARCA,marca.getText().toString());
        values.put(Utilidades.CAMPO_IDPROPIETARIO,s);
        values.put(Utilidades.CAMPO_IDCONDUCTOR,"Sin Asignar");
        values.put(Utilidades.CAMPO_KMRECORRIDO,"0");

        Long idResultante=db.insert(Utilidades.TABLA_VEHICULO,Utilidades.CAMPO_IDVEHICULO,values);

        Toast.makeText( getApplicationContext(),"Id Registro:"+ idResultante, Toast.LENGTH_SHORT).show();


    }
}