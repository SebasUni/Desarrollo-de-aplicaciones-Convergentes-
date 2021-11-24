package unipiloto.edu.transportecargaplus.Conductor;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.Random;

public class OdometerService extends Service {
    private final IBinder binder = new OdometerBinder();
    private final Random random = new Random();
    private LocationListener listener ;
    public static final String PERMISSONN_STRING= Manifest.permission.ACCESS_FINE_LOCATION;
   private static double distanceInMeters;
   private static Location lasLocation=null;
   private LocationManager locManager;

    public OdometerService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
        listener=new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                if (lasLocation == null){
                    lasLocation=location;
                }
                distanceInMeters += location.distanceTo(lasLocation);
                lasLocation =location;
            }

        };
        LocationManager locManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this,PERMISSONN_STRING)== PackageManager.PERMISSION_GRANTED){
            String provider;
            provider = locManager.getBestProvider(new Criteria(),true );
            if (provider != null){
                locManager.requestLocationUpdates(provider,1000,1,listener);
            }
        }
    }
    public class OdometerBinder extends Binder {
            OdometerService getIdometer(){
                return OdometerService.this;
            }

    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return binder;
    }
    @Override
    public  void onDestroy(){
        super.onDestroy();
        if (locManager != null && listener != null){
            if (ContextCompat.checkSelfPermission(this,PERMISSONN_STRING) == PackageManager.PERMISSION_GRANTED){
                locManager.removeUpdates(listener);
            }
            locManager= null;
            listener=null;

        }
    }

    public double getDistannce() {
        return  this.distanceInMeters/1609.344;

    }
}