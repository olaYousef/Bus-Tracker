package com.example.bustrucker;

import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class AdminActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private FusedLocationProviderClient client;

    double latitude;
    double longitude;

    ArrayList<LatLng> points;
    ArrayList<Double> latarr;
    ArrayList<Double> lngarr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        points = new ArrayList<LatLng>();
        latarr = new ArrayList<Double>();
        lngarr = new ArrayList<Double>();

        client = LocationServices.getFusedLocationProviderClient(AdminActivity.this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation().addOnSuccessListener(AdminActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                latitude = location.getLatitude();
                longitude = location.getLongitude();
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync((OnMapReadyCallback) AdminActivity.this);
                LatLng busstation = new LatLng(latitude, longitude);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(busstation));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busstation, 15f));

            }
        });
        client.getLastLocation().addOnFailureListener(AdminActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdminActivity.this, "can't get current location,please open GPS !!!!", Toast.LENGTH_LONG).show();
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync((OnMapReadyCallback) AdminActivity.this);

        DatabaseReference fire2 = FirebaseDatabase.getInstance().getReference().child("Points");
        fire2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                latarr.clear();
                lngarr.clear();

                Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();

                for (Map.Entry<String, Object> entry : users.entrySet()) {

                    Map single = (Map) entry.getValue();
                    lngarr.add(Double.parseDouble(single.get("lng").toString()));
                    latarr.add(Double.parseDouble(single.get("lat").toString()));
                }

                for (int i = 0; i < latarr.size(); i++) {
                    LatLng point = new LatLng(latarr.get(i), lngarr.get(i));
                    mMap.addMarker(new MarkerOptions().position(point));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                mMap.addMarker(new MarkerOptions().position(point));
                points.add(point);
                Date date = new Date();
                DatabaseReference fire = FirebaseDatabase.getInstance().getReference().child("Points");
                fire.child(date + "").child("lng").setValue(point.longitude + "");
                fire.child(date + "").child("lat").setValue(point.latitude + "").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(AdminActivity.this, "Stop point added !", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        client.getLastLocation();
        // Add a marker in Sydney and move the camera
        LatLng busstation = new LatLng(latitude, longitude);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(busstation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busstation, 15f));
    }
}