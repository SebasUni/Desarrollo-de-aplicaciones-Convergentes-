package unipiloto.edu.transportecargaplus.Controlador;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import unipiloto.edu.transportecargaplus.Controlador.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {


    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
         db.execSQL(Utilidades.CREAR_TABLA_USUARIO);
         db.execSQL(Utilidades.CREAR_TABLA_VEHICULO);
         db.execSQL(Utilidades.CREAR_TABLA_SOLICITUD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        db.execSQL("DROP TABLE IF EXISTS vehiculos");
        db.execSQL("DROP TABLE IF EXISTS solicitud");

       // onCreate(db);
    }
}