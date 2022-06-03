package com.example.cos420_application;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final static int MY_PERMISSION_FINE_LOCATION = 101;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapFrag);
        mapFragment.getMapAsync(this);

        //Deals with bottom navigation view
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        Button temporaryButton = (Button) findViewById(R.id.temporaryButton);

        temporaryButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(MapsActivity.this, Profile.class);
                startActivity(i);
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.map:
                        break;

                    case R.id.create:
                        Intent intent1 = new Intent(MapsActivity.this, CreateEventActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.profileNavView:
                        Intent intent2 = new Intent(MapsActivity.this, Profile.class);
                        startActivity(intent2);
                        break;
                }

                return false;
            }
        });

        /*This deals with displaying events stored in the database on the map view using the location
        that the user entered when they created the event.
         */
        addMarker();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        String provider;


        //Deals with adding the user's location to the map and asking for permission before doing it.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mMap.setMyLocationEnabled(true);

            /*This code takes the user's location after they approve of the app using it, and moving
            the map camera to it when the user opens the map view*/
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            provider = locationManager.getBestProvider(new Criteria(), false);

            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null){

                double lat, lng;
                lat = location.getLatitude();
                lng = location.getLongitude();
                LatLng userLocation = new LatLng(lat, lng);

                float zoomLevel = 10.0f;

                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, zoomLevel));

            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_FINE_LOCATION);
            }
        }

    }

    //This method deals with asking the user for their location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.setMyLocationEnabled(true);
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
        }
    }

    public void addMarker() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("events");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot ds : snapshot.getChildren()) {
                    String inputName = ds.child("name").getValue().toString();
                    String inputLocation = ds.child("location").getValue().toString();
                    String inputDate = ds.child("date").getValue().toString();
                    String inputTime = ds.child("time").getValue().toString();
                    String inputCapacity = ds.child("capacity").getValue().toString();

                    String snippet = (inputLocation + "\n" + inputDate + "\n" + inputTime + "\n" + inputCapacity);

                    Geocoder latLongLocation = new Geocoder(getApplicationContext(), Locale.getDefault());
                    List<Address> addressList = null;

                    int count = 0;

                    while (addressList == null && count < 3) {
                        try {
                            addressList = latLongLocation.getFromLocationName(inputLocation, 1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        count++;
                    }

                    if (addressList != null && addressList.size() > 0) {
                        Address address = (Address) addressList.get(0);
                        LatLng newEvent = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(newEvent).title(inputName).snippet(snippet));

                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(final Marker marker) {
                                String markerTitle = marker.getTitle();

                                String[] data = new String[4];
                                String snip = marker.getSnippet();

                                String builder = "";
                                int counter = 0;

                                for (int i = 0; i < snip.length(); i++) {
                                    if (i == snip.length() - 1) {
                                        builder += snip.substring(i, i + 1);
                                        data[counter] = builder;
                                        break;
                                    } else if (snip.substring(i, i + 1) == "\n") {
                                        data[counter] = builder;
                                        builder = "";
                                        counter++;
                                    } else {
                                        builder += snip.substring(i, i + 1);
                                    }
                                }

                                Intent i = new Intent(MapsActivity.this, DetailView.class);
                                i.putExtra("name", markerTitle);
                                i.putExtra("location", data[0]);
                                i.putExtra("date", data[1]);
                                i.putExtra("time", data[2]);
                                i.putExtra("capacity", data[3]);
                                i.putExtra("activity", "Maps");

                                startActivity(i);

                                return true;

                            }
                        });

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
