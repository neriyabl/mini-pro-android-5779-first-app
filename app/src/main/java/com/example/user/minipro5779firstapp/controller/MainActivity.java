package com.example.user.minipro5779firstapp.controller;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.minipro5779firstapp.R;
import com.example.user.minipro5779firstapp.model.backend.Backend;
import com.example.user.minipro5779firstapp.model.backend.BackendFactory;
import com.example.user.minipro5779firstapp.model.entities.ClientRequest;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import static android.support.constraint.Constraints.TAG;

/**
 * the first app in the android project
 * <p>
 * this is the client app in this app we create a request to get a drive
 * the request sent to the database in firebase and all drivers in the area
 * get notification about the new request
 * <p>
 * <p>
 * ---list of critical things:
 * <p>
 * 1. change the path to this 'MainActivity' in the AndroidManifest.xml
 * <p>
 * 2. import the google play service dependencies {
 * implementation 'com.google.android.gms:play-services-location:15.0.1'
 * implementation 'com.google.android.gms:play-services-places:15.0.1'
 * }
 * <p>
 * 3. update the version of google-services classpath in the Top-level build file {
 * classpath 'com.google.gms:google-services:4.0.0'
 * }
 * <p>
 * 4. check the version of firebase dependencies {
 * implementation 'com.google.firebase:firebase-core:16.0.1'
 * implementation 'com.google.firebase:firebase-database:16.0.1'
 * }
 * <p>
 * 5. add the permission in the AndroidManifest.xml {
 * <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 * <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 * }
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //fregment from google to get location autocomplete field
    private PlaceAutocompleteFragment destination;

    //the user location
    private Location location;
    //the destination location
    private Location dest = null;
    //the text-view to show the user location
    private TextView locationText;

    //the text fields of rest fields in the request
    private EditText name;
    private EditText phone;
    private EditText email;

    //if the location not found in this button we try again to find the user location
    private Button find_location;
    //the send request button
    private Button sendBtn;


    //the service provider to get the client location
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ----- fins views -----
        destination = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.location);
        locationText = findViewById(R.id.locationText);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);

        find_location = findViewById(R.id.find_location);
        sendBtn = findViewById(R.id.sendBtn);

        find_location.setOnClickListener(this);
        sendBtn.setOnClickListener(this);


        destination.setHint("Search your destination...");
        // ----- set the destination location wen the client select location in the fragment
        destination.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                dest = new Location(LocationManager.GPS_PROVIDER);
                dest.setLatitude(place.getLatLng().latitude);
                dest.setLongitude(place.getLatLng().longitude);
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

        //run the function on the background and add onSuccess listener
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Location _location) {
                        // Got last known location. In some rare situations this can be null.
                        if (_location != null) {
                            //format the location to text address in this case we show just the city
                            geocoding.getAddressFromLocation(_location, getApplicationContext(), new GeocoderHandler(locationText));
                            //save the location
                            location = _location;
                        } else
                            locationText.setText("not found");
                    }
                });
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void onClick(View v) {
        //try again to get the last known location
        if (v.getId() == R.id.find_location) getLocation();

        //add the request to the data base
        else if (v.getId() == R.id.sendBtn) {

            final Backend backend = BackendFactory.getBackend();
            final Location source = location;
            if (dest == null) dest = source;
            final String _name = name.getText().toString();
            final String _phone = phone.getText().toString();
            final String _email = email.getText().toString();
            if (source != null && dest != null && _name.length() > 0 && _phone.length() > 0 && _email.length() > 0) {
                new AsyncTask<Context, Void, Void>() {

                    @Override
                    protected Void doInBackground(Context... contexts) {
                        backend.addRequest(new ClientRequest(_name, _phone, _email, source.getLatitude(),
                                source.getLongitude(), dest.getLatitude(), dest.getLongitude()), contexts[0]);
                        return null;
                    }
                }.execute(this);
            } else {
                Toast.makeText(this, "Missing details", Toast.LENGTH_LONG).show();
            }
        }
    }


    /**
     * handler to show the results of Geocoder in the UI:
     */
    @SuppressLint("HandlerLeak")
    private class GeocoderHandler extends Handler {

        TextView view;

        //the view to handle
        GeocoderHandler(TextView v) {
            view = v;
        }

        //showing result geocoding
        @Override
        public void handleMessage(Message message) {
            String result;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    result = bundle.getString("address");
                    break;
                default:
                    result = "can't parse the location";
            }
            //update view
            view.setText(result);
        }
    }


    /**
     * this class convert a lat long to address in another thread
     */
    static class geocoding {

        public static void getAddressFromLocation(
                final Location location, final Context context, final Handler handler) {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                    String result = null;
                    try {
                        List<Address> list = geocoder.getFromLocation(
                                location.getLatitude(), location.getLongitude(), 1);
                        if (list != null && list.size() > 0) {
                            Address address = list.get(0);
                            // sending back first address line and locality
                            result = address.getAddressLine(0) + ", " + address.getLocality();
                        } else if (list != null) {
                            DecimalFormat format = new DecimalFormat("##.####");
                            result = "cant parse: " + format.format(location.getLatitude()) + " , " + format.format(location.getLongitude())
                                    + "\nClick to see the location on the map";
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Impossible to connect to Geocoder", e);
                    } finally {
                        Message msg = Message.obtain();
                        msg.setTarget(handler);
                        if (result != null) {
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putString("address", result);
                            msg.setData(bundle);
                        } else
                            msg.what = 0;
                        msg.sendToTarget();
                    }
                }
            };
            thread.start();
        }
    }
}
