package com.example.user.minipro5779firstapp.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.minipro5779firstapp.R;
import com.example.user.minipro5779firstapp.model.backend.Backend;
import com.example.user.minipro5779firstapp.model.backend.BackendFactory;
import com.example.user.minipro5779firstapp.model.entities.Client;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * ---list of critical things:
 *
 * 1. change the path to this 'MainActivity' in the AndroidManifest.xml
 *
 * 2. import the google play service dependencies {
 *         implementation 'com.google.android.gms:play-services-location:15.0.1'
 *         implementation 'com.google.android.gms:play-services-places:15.0.1'
 * }
 *
 * 3. update the version of google-services classpath in the Top-level build file {
 *     classpath 'com.google.gms:google-services:4.0.0'
 * }
 *
 * 4. check the version of firebase dependencies {
 *         implementation 'com.google.firebase:firebase-core:16.0.1'
 *         implementation 'com.google.firebase:firebase-database:16.0.1'
 * }
 *
 *5. add the permission in the AndroidManifest.xml {
 *         <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 *         <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 *}
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private PlaceAutocompleteFragment destination;
    private Location location;
    private Location dest=null;
    private TextView locationText;

    private EditText name;
    private EditText phone;
    private EditText email;

    private Button find_location;
    private Button sendBtn;

    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        destination = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.location);
        locationText = findViewById(R.id.locationText);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);

        find_location =findViewById(R.id.find_location);
        sendBtn = findViewById(R.id.sendBtn);

        find_location.setOnClickListener(this);
        sendBtn.setOnClickListener(this);


        destination.setHint("Search your destination...");
        destination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                dest = new Location(place.getId());
            }

            @Override
            public void onError(Status status) {
                System.out.print(status);
            }
        });
        getLocation();

    }


    /**
     * Get the last known location
     * link to more details:   https://developer.android.com/training/location/retrieve-current#java
     *
     */
    private void getLocation() {

        // check the Permission and request permissions if needed
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]
                    {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


        //get Provider location from the user location services
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());


        //run the function on the background and add onSuccess listener
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location _location) {
                        // Got last known location. In some rare situations this can be null.
                        if (_location != null) {
                            List<Address> addresses = null;
                            try {
                                //format the location to text address
                                addresses = geocoder.getFromLocation(_location.getLatitude(), _location.getLongitude(), 1);
                                locationText.setText(addresses.get(0).getLocality());
                                //save the location
                                location = _location;
                            } catch (IOException e) {
                                e.printStackTrace();
                                locationText.setText("not found");
                                Toast.makeText(getApplicationContext(),"fail to find your location",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View v) {
        //try again to get the last known location
        if(v.getId()==R.id.find_location){
            getLocation();
            return;
        }

        //add the request to the data base
        else if(v.getId()==R.id.sendBtn){

            final Backend backend = BackendFactory.getBackend();
            final Location source = location;
            if(dest==null)dest=source;
            final String _name = name.getText().toString();
            final String _phone = phone.getText().toString();
            final String _email = email.getText().toString();
            if(source!=null && dest!=null && _name.length()>0 && _phone.length()>0 && _email.length()>0){
                new AsyncTask<Context, Void, Void>(){

                    @Override
                    protected Void doInBackground(Context... contexts) {
                        backend.addRequest(new Client(_name,_phone,_email,source,dest),contexts[0]);
                        return null;
                    }
                }.execute(this);
            }
            else {
                Toast.makeText(this,"Missing details",Toast.LENGTH_LONG).show();
            }
        }
    }
}