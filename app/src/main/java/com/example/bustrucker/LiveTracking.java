package com.example.bustrucker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class LiveTracking {

    //for location
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS =  1000 * 10;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 1000*5;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final String TAG = MapView.class.getSimpleName();

    private GoogleMap mMap;
    public static LatLng location;
    private String mLastUpdateTime;
    private Boolean mRequestingLocationUpdates;
    private Location mCurrentLocation;
    double latitude;
    double longitude;
    FirebaseDatabase database ;
    DatabaseReference myRef ;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationCallback mLocationCallback;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;

    String address = "";

    //calling in construtor
    //init() ; ///for start get location
    //restoreValuesFromBundle(savedInstanceState);
    //startLocationButtonClick();



    /**
     * Starting location updates
     * Check whether location settings are satisfied and then
     * location updates will be requested
     */
    /*
    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(getParent(), new OnSuccessListener<LocationSettingsResponse>() {///////getActivity
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i("", "All location settings are satisfied.");

                        //Toast.makeText(getApplicationContext(), "Started location updates!", Toast.LENGTH_SHORT).show();

                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());

                        mMap.setMyLocationEnabled(true);
                        updateLocationUI();
                    }
                })
                .addOnFailureListener(getParent(), new OnFailureListener() {////////////////getActivity()
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getParent(), REQUEST_CHECK_SETTINGS);//////////////getactivity
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_LONG).show();
                                break;
                        }

                        updateLocationUI();
                    }
                });
    }



    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    // OnClick(R.id.btn_start_location_updates)
    public void startLocationButtonClick() {
        // Requesting ACCESS_FINE_LOCATION using Dexter library
        Dexter.withActivity(getParent()) ////////////////////////////////////////////getActivty
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        mRequestingLocationUpdates = true;
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }



    private void restoreValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("is_requesting_updates")) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean("is_requesting_updates");
            }

            if (savedInstanceState.containsKey("last_known_location")) {
                mCurrentLocation = savedInstanceState.getParcelable("last_known_location");
            }

            if (savedInstanceState.containsKey("last_updated_on")) {
                mLastUpdateTime = savedInstanceState.getString("last_updated_on");
            }
        }

        updateLocationUI();
    }

    private void init() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        mSettingsClient = LocationServices.getSettingsClient(getApplicationContext());

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
                updateLocationUI();
            }
        };

        mRequestingLocationUpdates = false;
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();


    }



    private void updateLocationUI() {


        if (mCurrentLocation != null) {
            latitude = mCurrentLocation.getLatitude();
            longitude = mCurrentLocation.getLongitude();

            getAddress();
            location = new LatLng(latitude, longitude);


            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 15));


//            mDatabase.child("users").addChildEventListener(new ChildEventListener() {
//                @Override
//                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                    String k = dataSnapshot.getKey();
//
//
//                    mDatabaseusers = FirebaseDatabase.getInstance().getReference().child("users").child(k);
//
//                    mDatabaseusers.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                            User u = dataSnapshot.getValue(User.class);
//
//                            if (!u.getUserid().equals(mUser.getUid())) {
//                                location = new LatLng(u.getLatit(), u.getLongi());
//
//                                if (hashMapMarker.get(u.getUserid()) != null) {
//                                    Marker marker2 = hashMapMarker.get(u.getUserid());
//                                    marker2.remove();
//                                    hashMapMarker.remove(u.getUserid());
//                                }
//
//                                MarkerOptions markerOptions = new MarkerOptions().position(location)
//                                        .flat(true).title(u.getName());
//                                Marker marker = mMap.addMarker(markerOptions);
//                                hashMapMarker.put(u.getUserid(), marker);
//
//
////                                if (getActivity() != null)
////                                    loadImage(Glide.with(getActivity()), u, mMap);
//
//
//                            }
//
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                        }
//                    });
//
//
//                }
//
//
//                @Override
//                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//
//                }
//
//                @Override
//                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                }
//
//                @Override
//                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });

        }

    }



    public Address getAddress(double latitude, double longitude)
    {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude,longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            return addresses.get(0);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }



    public void getAddress()
    {

        Address locationAddress=getAddress(latitude,longitude);

        if(locationAddress!=null)
        {
            address = locationAddress.getAddressLine(0);
            String address1 = locationAddress.getAddressLine(1);
            String city = locationAddress.getLocality();
            String state = locationAddress.getAdminArea();
            String country = locationAddress.getCountryName();
            String postalCode = locationAddress.getPostalCode();

            String currentLocation;

            if(!TextUtils.isEmpty(address))
            {
                currentLocation=address;

                if (!TextUtils.isEmpty(address1))
                    currentLocation+="\n"+address1;

                if (!TextUtils.isEmpty(city))
                {
                    currentLocation+="\n"+city;

                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+=" - "+postalCode;
                }
                else
                {
                    if (!TextUtils.isEmpty(postalCode))
                        currentLocation+="\n"+postalCode;
                }

                if (!TextUtils.isEmpty(state))
                    currentLocation+="\n"+state;

                if (!TextUtils.isEmpty(country))
                    currentLocation+="\n"+country;

                System.out.println("current location : "+ currentLocation);

            }
        }
    }


*/

}
