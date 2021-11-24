package unipiloto.edu.transportecargaplus.Conductor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import unipiloto.edu.transportecargaplus.Main.InformacionUsuario;
import unipiloto.edu.transportecargaplus.Propietario.AgregarCarro;
import unipiloto.edu.transportecargaplus.R;

public class MainConductor extends AppCompatActivity {
    String s;
    Bundle  IdConductor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_conductor);
        IdConductor= getIntent().getExtras();
        s= IdConductor.getString("IdConductor");
        Log.d("prueba",s);
    }
    public void Viajes(View v){
        Intent intent = new Intent(this, Viajes.class);
        intent.putExtra("IdConductor",IdConductor.getString("IdConductor"));
        startActivity(intent);

    }
    public void Historial(View v){
        Intent intent = new Intent(this, HistorialViajes.class);
        intent.putExtra("IdConductor",IdConductor.getString("IdConductor"));
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
                intent.putExtra("usuario",IdConductor.getString("IdConductor"));
                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }
}