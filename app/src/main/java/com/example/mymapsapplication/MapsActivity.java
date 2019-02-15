package com.example.mymapsapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
         locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
         locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
               /* mMap.clear();//Haritadaki bütün işaretleri siler
                System.out.println("My Location is: "+location.toString());
                LatLng userLocation =new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("My Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,11));//Kamera lokasyonumuza 11x büyüklüğünde zoomlar*/


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            //Kullanıcının son bilinen lokasyonu
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener);
            Location lastLocation= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng userLastLocation=new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().title("My Location").position(userLastLocation));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,10));
        }

        mMap.setOnMapLongClickListener(this);

/*        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));*/ //İstersek bu kısımda marker ekleyebiliriz.
    }
//İzinler kısmı
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(grantResults.length>0){
            if(requestCode==1){
                if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        Geocoder geocoder=new Geocoder(getApplicationContext(),Locale.getDefault());
        String address="";
        try{
            List<Address> addressList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
        if(addressList!=null&&addressList.size()>0){
            if(addressList.get(0).getThoroughfare()!=null){
                address+=addressList.get(0).getThoroughfare();
                if(addressList.get(0).getSubThoroughfare()!=null){
                    address+=addressList.get(0).getSubThoroughfare();
                }
            }
        }
        }
        catch(Exception e){}
        mMap.addMarker(new MarkerOptions().position(latLng).title(address));
    }

}
