package com.example.bustrucker;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import id.zelory.compressor.Compressor;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.theartofdev.edmodo.cropper.CropImage.*;
import static java.security.AccessController.getContext;


public class Driver extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener ,OnMapReadyCallback {

    private GoogleMap mMap;

    private FusedLocationProviderClient client ;

    double latitude;
    double longitude;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();
    String uid = user.getUid();

    DatabaseReference fire;
    StorageReference mImageStorage;

    String name, mobile;

    ArrayList<Double> latarr;
    ArrayList<Double> lngarr;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        latarr = new ArrayList<Double>();
        lngarr = new ArrayList<Double>();

        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        BottomNavigationView passenger_nv= findViewById(R.id.navigation);
        
        requestPermission() ;

        fire = FirebaseDatabase.getInstance().getReference().child("Driver").child(uid);
        fire.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("Name").getValue().toString();
                mobile = dataSnapshot.child("Phone").getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        passenger_nv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.emergency:

                        AlertDialog.Builder builder = new AlertDialog.Builder(Driver.this);
                        builder.setTitle("Driver Profile");
                        builder.setMessage("Name : "+name+"\nMobile : "+mobile);
                        builder.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("Pick Image", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent gallary = new Intent();
                                gallary.setType("image/*");
                                gallary.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(Intent.createChooser(gallary, "image picker"), 1994);
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();

                        break;

                    case R.id.call:
                        client=LocationServices.getFusedLocationProviderClient(Driver.this) ;
                        client.getLastLocation().addOnSuccessListener(Driver.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {

                                latitude=location.getLatitude() ;
                                longitude =location.getLongitude() ;

                                fire = FirebaseDatabase.getInstance().getReference().child("BussLoc").child(uid);
                                fire.child("lng").setValue(longitude+"");
                                fire.child("lat").setValue(latitude+"").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                        Toast.makeText(getApplicationContext(), "started tracking !!!!, show my location on the map",Toast.LENGTH_LONG).show();
                                    }
                                });

                            }
                        }).addOnFailureListener(Driver.this, new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Driver.this, "can't get current location,please open GPS !!!!", Toast.LENGTH_LONG).show();
                            }
                        });
                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync((OnMapReadyCallback) Driver.this);

                        StopSigns();


                        break;

                    case R.id.add:
                        Intent intent = new Intent(Driver.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                }
                return true;

            }
        });

    }

    private void StopSigns() {

        DatabaseReference fire2 = FirebaseDatabase.getInstance().getReference().child("Points");
        fire2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                latarr.clear();
                lngarr.clear();

                Map<String, Object> users = (Map<String, Object>) dataSnapshot.getValue();

                for(Map.Entry<String, Object> entry : users.entrySet()){

                    Map single = (Map) entry.getValue();
                    lngarr.add(Double.parseDouble(single.get("lng").toString()));
                    latarr.add(Double.parseDouble(single.get("lat").toString())); }

                for(int i=0; i<latarr.size(); i++){
                    LatLng point = new LatLng(latarr.get(i), lngarr.get(i));
                    mMap.addMarker(new MarkerOptions().position(point)); }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this ,new String[]{ACCESS_FINE_LOCATION} , 1);
    }




    //this method call implicitly when driver press satart
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.start:
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", "0797201234", null)));
                break;
        }


        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (item.getItemId()){
            case R.id.mobile:
                Toast.makeText(getApplicationContext(), "Mobile Button",Toast.LENGTH_LONG).show();
                break;
            case R.id.today:

                break;
            case R.id.out:
                Toast.makeText(getApplicationContext(), "Logout Button",Toast.LENGTH_LONG).show();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1994 && resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            CropImage.activity(imageUri).start(Driver.this); }

        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK){

                Uri resultUri = result.getUri();
                File Thumb_filePath = new File(resultUri.getPath());
                Bitmap Thumb_bitmap = new Compressor(Driver.this).setMaxWidth(200).setMaxHeight(200)
                        .setQuality(50).compressToBitmap(Thumb_filePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 25, baos);
                final byte [] thumb_byte = baos.toByteArray();

                Date dat =new Date();

                StorageReference filePath = mImageStorage.child("prof").child(dat + ".jpg");
                final StorageReference thumb_filePath = mImageStorage.child("prof").child("min").child(dat + ".jpg");
                fire = FirebaseDatabase.getInstance().getReference().child("Driver").child(uid).child("image");
                filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()){
                            final String download_url = task.getResult().getDownloadUrl().toString();
                            UploadTask uploadTask = thumb_filePath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                    if(task.isSuccessful()){

                                        fire.setValue(download_url).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){


                                                    Toast.makeText(Driver.this, "Upload done", Toast.LENGTH_LONG).show();


                                                }     }
                                        }); }
                                    else{
                                        Toast.makeText(Driver.this, "Error, try again", Toast.LENGTH_LONG).show();
                                    }     }
                            }); }     }  }); }
            else{
                Toast.makeText(Driver.this, "Error, try again 2", Toast.LENGTH_LONG).show();
            } }
    }


}
