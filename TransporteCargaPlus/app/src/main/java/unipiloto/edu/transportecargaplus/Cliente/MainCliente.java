package unipiloto.edu.transportecargaplus.Cliente;

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

public class MainCliente extends AppCompatActivity {
    String s;
    Bundle IdCliente;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_cliente);
        IdCliente= getIntent().getExtras();
        s=IdCliente.getString("IdCliente");
        Log.d("prueba",s);

    }

    public void ButonSolicitartransportecarga(View v){
        Intent intent = new Intent(this, SolicitarTransporte.class);
        intent.putExtra("IdCliente",IdCliente.getString("IdCliente"));
        startActivity(intent);

    }
    public void Butoninformacion(View v){
        Intent intent = new Intent(this, InformacionCarga.class);
        intent.putExtra("IdCliente",IdCliente.getString("IdCliente"));
        startActivity(intent);

    }
    public void Butonhistorial(View v){
        Intent intent = new Intent(this, HistorialCarga.class);
        intent.putExtra("IdCliente",IdCliente.getString("IdCliente"));
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_salir,menu);
        getMenuInflater().inflate(R.menu.menu_agregarcarga,menu);
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
            if (id== R.id.agregarcarga){
                Intent intent = new Intent(this, SolicitarTransporte.class);
                intent.putExtra("IdCliente",IdCliente.getString("IdCliente"));
                startActivity(intent);
            }else{
                if (id== R.id.menuusuario){
                    Intent intent = new Intent(this, InformacionUsuario.class);
                    intent.putExtra("usuario",IdCliente.getString("IdCliente"));
                    startActivity(intent);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }
}