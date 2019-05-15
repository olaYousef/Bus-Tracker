package com.example.bustrucker;

import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class BusesLiveLocationActivity extends AppCompatActivity implements OnMapReadyCallback {


    private GoogleMap mMap ;
    FirebaseDatabase database ;
    DatabaseReference myRefForDivers ;
    DatabaseReference myRefForBuses ;

    ArrayList<DriverModel> drivers ;
    ArrayList<BusesModel> buses ;
    ArrayList<BusesModel> busesRunning ;


    double latitude ,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buses_live_location);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        buses=intent.getParcelableArrayListExtra( "busesList");
        drivers=intent.getParcelableArrayListExtra( "driversList");

        busesRunning=new ArrayList<>() ;

        String[] fromLoc = getResources().getStringArray(R.array.from);
        String[] toLoc = getResources().getStringArray(R.array.to);


        int fromPos = Integer.parseInt(intent.getStringExtra("fromPos"));
        int toPos = Integer.parseInt(intent.getStringExtra("toPos"));


            //readDataFromFirebase();

            //Toast.makeText(this, "driversSize= " + drivers.size(), Toast.LENGTH_LONG).show();//dont show because open map in passActivity


            ////this for get running buses depends on status .
            int numOfBuses = buses.size();
            for (int i = 0; i < numOfBuses; i++) {
                if (buses.get(i).getStatus())//boolean status
                {
                    busesRunning.add(buses.get(i));
                }
            }


            //this for get running buses for correct route .
            int numOfRunningBuses = busesRunning.size();
            for (int i = 0; i < numOfRunningBuses; i++) {
                if (busesRunning.get(i).getFromLoc().equals(fromLoc[fromPos]) && busesRunning.get(i).getToLoc().equals(toLoc[toPos])) {
                    ///put mark on map
                    latitude = busesRunning.get(i).getLatitude();
                    longitude = busesRunning.get(i).getLongitude();
                    //how to put many location
                    putLocationOnMap(latitude, longitude, busesRunning.get(i).getId());//not working good. but this logic is true


                }

            }//end for



    }



    public void   readDataFromFirebase()//try to get data from path activity because sync //temp
    {//maybe call function putlocation here


            database = FirebaseDatabase.getInstance();
            myRefForDivers = database.getReference("Drivers");
            myRefForBuses = database.getReference("Buses");

            myRefForDivers.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    drivers.add(dataSnapshot.getValue(DriverModel.class));

                    Toast.makeText(BusesLiveLocationActivity.this, "read data Driveres done ..", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(BusesLiveLocationActivity.this, "read data failed !! ..", Toast.LENGTH_LONG).show();


                }
            });


            myRefForBuses.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    buses.add(dataSnapshot.getValue(BusesModel.class));

                    Toast.makeText(BusesLiveLocationActivity.this, "read data Buses done ..", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });



    }

    public void putLocationOnMap(double latitude ,double longitude ,int id)
    {
        LatLng busstation = new LatLng(latitude, longitude);

        mMap.addMarker(new MarkerOptions().position(busstation).title(id+""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(busstation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busstation,15f));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
           mMap = googleMap;
        Toast.makeText(this, "driver size = " + drivers.size(), Toast.LENGTH_LONG).show();
//
//        // Add a marker in Sydney and move the camera
//        LatLng busstation = new LatLng(32.535167, 35.869561);
//        mMap.addMarker(new MarkerOptions().position(busstation).title("lol"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(busstation));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(busstation,15f));

    }
}
