package com.example.lostandfound;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<String> locations;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        locations = getIntent().getStringArrayListExtra("locations");
        Toast.makeText(this," "+locations,Toast.LENGTH_SHORT).show();

        // Initialize Places
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyAEVvW0OQofWFVU1fiZ-FfpxmSXOio3dvM");
        }

        // Setup AutocompleteSupportFragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);

        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

                    // Return location immediately
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("latitude", latLng.latitude);
                    resultIntent.putExtra("longitude", latLng.longitude);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }

            @Override
            public void onError(@NonNull com.google.android.gms.common.api.Status status) {
                Toast.makeText(MapsActivity.this, "Error: " + status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Load map
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (locations != null && !locations.isEmpty()) {
            Geocoder geocoder = new Geocoder(this);
            LatLngBounds.Builder builder = new LatLngBounds.Builder();

            for (String loc : locations) {
                try {
                    List<Address> addresses = geocoder.getFromLocationName(loc, 1);
                    if (addresses != null && !addresses.isEmpty()) {
                        Address address = addresses.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(latLng).title(loc));
                        builder.include(latLng);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            final View mapView = getSupportFragmentManager().findFragmentById(R.id.map).getView();
            if (mapView != null) {
                mapView.post(() -> {
                    try {
                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
                    } catch (Exception e) {
                        e.printStackTrace();
                        // fallback if newLatLngBounds fails
                        if (!locations.isEmpty()) {
                            try {
                                List<Address> addresses = geocoder.getFromLocationName(locations.get(0), 1);
                                if (addresses != null && !addresses.isEmpty()) {
                                    LatLng latLng = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));
                                }
                            } catch (IOException ignored) {}
                        }
                    }
                });
            }
        } else {
            mMap.setOnMapClickListener(latLng -> {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f));

                Intent resultIntent = new Intent();
                resultIntent.putExtra("latitude", latLng.latitude);
                resultIntent.putExtra("longitude", latLng.longitude);
                setResult(RESULT_OK, resultIntent);
                finish();
            });
        }
    }


}
