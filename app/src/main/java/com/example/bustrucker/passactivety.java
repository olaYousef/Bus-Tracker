package com.example.bustrucker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;

public class passactivety extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Toolbar toolbar;
    FloatingActionButton locate,search;
    double latitude;
    double longitude;
    private FusedLocationProviderClient client ;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passactivety2);

        Toast.makeText(this, "pass", Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Driver").child("vFBazRDHytZB4ClXH8PDOJrKljE3");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                dataSnapshot.child("Seat");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
BottomNavigationView passenger_nv=(BottomNavigationView) findViewById(R.id.p_nv);
passenger_nv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.emergency:

                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync((OnMapReadyCallback) passactivety.this);

                AlertDialog.Builder builder = new AlertDialog.Builder(passactivety.this);
                builder.setTitle("call emergency");
                builder.setMessage("do you want to call 911");
                builder.setPositiveButton("call", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:911"));
                        startActivity(intent);
                    }
                }).setNegativeButton("cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                break;
            case R.id.add:
                Intent intent = new Intent(passactivety.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.call:
                Intent intent2 = new Intent(passactivety.this, path.class);
                startActivity(intent2);
                break;

        }
        return true;

    }
});

        client=LocationServices.getFusedLocationProviderClient(this) ;

        client.getLastLocation().addOnSuccessListener(passactivety.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                latitude=location.getLatitude() ;
                longitude =location.getLongitude() ;

            }
        }).addOnFailureListener(passactivety.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(passactivety.this, "can't get current location,please open GPS !!!!", Toast.LENGTH_LONG).show();
            }
        }) ;

            }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        client.getLastLocation();
        // Add a marker in Sydney and move the camera
        LatLng busstation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(busstation).title("my loc"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(busstation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busstation,15f));

    }

}
