package unipiloto.edu.transportecargaplus.Main;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import unipiloto.edu.transportecargaplus.Cliente.MainCliente;
import unipiloto.edu.transportecargaplus.Conductor.MainConductor;
import unipiloto.edu.transportecargaplus.Controlador.ConexionSQLiteHelper;
import unipiloto.edu.transportecargaplus.Controlador.Utilidades;
import unipiloto.edu.transportecargaplus.Entidades.Usuario;
import unipiloto.edu.transportecargaplus.Entidades.Vehiculo;
import unipiloto.edu.transportecargaplus.Propietario.MainPropietario;
import unipiloto.edu.transportecargaplus.R;

public class Login extends AppCompatActivity {
    ConexionSQLiteHelper conn;
    EditText correo,password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        correo= (EditText) findViewById(R.id.EdiEmail);
        password= (EditText) findViewById(R.id.EdiPassword);
    }
    public void CrearUsuario(View v){
        Intent intent = new Intent(this,NewUser.class);
        startActivity(intent);

    }
    public void Login(View v){
        consultar();

    }
    private void consultar() {
        conn = new ConexionSQLiteHelper(this,"bd_usuarios", null,1);
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={correo.getText().toString()};
        String[] campos={Utilidades.CAMPO_CORREO,Utilidades.CAMPO_PASSWORD,Utilidades.CAMPO_ROL,Utilidades.CAMPO_IDUSUARIO};
        Cursor cursor= db.query(Utilidades.TABLA_USUARIO,campos,Utilidades.CAMPO_CORREO+"=?", parametros, null,null,null);
        cursor.moveToFirst();
        try {
            if (correo.getText().toString().equals(cursor.getString(0))){
                if (password.getText().toString().equals(cursor.getString(1))){
                    if (cursor.getString(2).equals("Cliente")){
                        Intent intent = new Intent(this, MainCliente.class);
                        intent.putExtra("IdCliente",cursor.getString(3));
                        startActivity(intent);
                    }else{
                        if (cursor.getString(2).equals("Conductor")){
                            Intent intent = new Intent(this, MainConductor.class);
                            intent.putExtra("IdConductor",cursor.getString(3));
                            startActivity(intent);

                        }
                        else{
                            if (cursor.getString(2).equals("Propietario")){
                                Intent intent = new Intent(this,MainPropietario.class);
                                intent.putExtra("IdPropietario",cursor.getString(3));
                                startActivity(intent);
                            }
                        }
                    }

                }
            }
            cursor.close();
        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "CORREO O PASSWORD INCORRECTO", Toast.LENGTH_LONG).show();
        }

    }
}