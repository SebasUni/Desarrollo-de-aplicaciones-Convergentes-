package unipiloto.edu.transportecargaplus.Propietario;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import unipiloto.edu.transportecargaplus.Cliente.SolicitarTransporte;
import unipiloto.edu.transportecargaplus.Controlador.ConexionSQLiteHelper;
import unipiloto.edu.transportecargaplus.Entidades.Usuario;
import unipiloto.edu.transportecargaplus.Entidades.Vehiculo;
import unipiloto.edu.transportecargaplus.Main.InformacionUsuario;
import unipiloto.edu.transportecargaplus.R;

public class MainPropietario extends AppCompatActivity {
    String s;
    Bundle  IdPropietario;
    private ArrayList<Vehiculo> VehiculosLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_propietario);
        IdPropietario= getIntent().getExtras();
        s= IdPropietario.getString("IdPropietario");
        Log.d("prueba",s);
    }
    public void AgregarCarro(View v){
        Intent intent = new Intent(this, AgregarCarro.class);
        intent.putExtra("IdPropietario",IdPropietario.getString("IdPropietario"));
        startActivity(intent);

    }
    public void buttonasignarconductor(View v){
        Intent intent = new Intent(this, AsignarConductor.class);
        intent.putExtra("IdPropietario",IdPropietario.getString("IdPropietario"));
        startActivity(intent);

    }
    public void buttonasignarcarga(View v){
        Intent intent = new Intent(this, AsignacionCarga.class);
        intent.putExtra("IdPropietario",IdPropietario.getString("IdPropietario"));
        startActivity(intent);

    }
    public void buttonaKmrecorrido(View v){
        Intent intent = new Intent(this, KmRecorrido.class);
        intent.putExtra("IdPropietario",IdPropietario.getString("IdPropietario"));
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_salir,menu);
        getMenuInflater().inflate(R.menu.menu_usuario,menu);
        return true;
    }
    //asigancion del menu


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id =item.getItemId();
        if (id== R.id.salir){
            finish();
        }else{
                if (id== R.id.menuusuario){
                    Intent intent = new Intent(this, InformacionUsuario.class);
                    intent.putExtra("usuario",IdPropietario.getString("IdPropietario"));
                    startActivity(intent);
                }
            }

        return super.onOptionsItemSelected(item);
    }

}