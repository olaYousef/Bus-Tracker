package com.example.bustrucker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Route extends AppCompatActivity {

    ListView listView;
    ArrayList<String> available;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        listView = findViewById(R.id.list);
        available = new ArrayList<String>();

        available.add("bus #1");
        available.add("bus #2");
        available.add("bus #3");
        available.add("bus #4");

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, available);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch(position){

                    case 0:

                        goTo("http://maps.google.com/maps?daddr=32.5519519,35.8617293");

                        break;

                    case 1:

                        goTo("http://maps.google.com/maps?daddr=32.535691,35.869500");

                        break;

                    case 2:

                        goTo("http://maps.google.com/maps?daddr=32.549173,35.856036");

                        break;
                }

            }
        });
//Ask for permession__________________________________________________________________________________________


    }

    private void goTo(String loc){

        isLocationPermessionGranted();

        //Check if device has enabled gps, internet and location services________________________________________________________
        if(isNetworkActive()){

            if(isLocationActive()){

                if(isGpsActive()){

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                            Uri.parse(loc));
                    startActivity(intent); }

                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Route.this);
                    builder.setTitle("warning").setMessage("Please open gps").setPositiveButton(android.R.string.ok
                            , new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    System.exit(0); }  }).setIcon(android.R.drawable.ic_menu_manage).show(); } }

            else{
                AlertDialog.Builder builder = new AlertDialog.Builder(Route.this);
                builder.setTitle("warning").setMessage("Please open gps").setPositiveButton(android.R.string.ok
                        , new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                System.exit(0); }  }).setIcon(android.R.drawable.ic_menu_manage).show(); } }

        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(Route.this);
            builder.setTitle("warning").setMessage("No internet connection").setPositiveButton(android.R.string.ok
                    , new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            System.exit(0); }  }).setIcon(android.R.drawable.ic_menu_manage).show(); }

    }

    private boolean isNetworkActive(){

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting(); }

    private boolean isGpsActive(){

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;

        try{
            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); }
        catch(Exception ex){}

        return gps_enabled; }

    private boolean isLocationActive(){

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean location_enabled = false;

        try{
            location_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER); }
        catch(Exception ex){}

        return location_enabled ; }

    private void isLocationPermessionGranted(){

        if(ActivityCompat.checkSelfPermission(Route.this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Route.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(Route.this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 1 ); }  }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return  true; }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

}
