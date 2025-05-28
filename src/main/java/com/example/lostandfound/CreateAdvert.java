package com.example.lostandfound;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CreateAdvert extends AppCompatActivity {

    EditText nameEditText, phoneEditText, descriptionEditText, dateEditText, locationEditText;
    RadioGroup postTypeGroup;
    Button saveButton;
    Fragment auto_complete;

    FusedLocationProviderClient fusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        dbHelper = new DBHelper(this);

        postTypeGroup = findViewById(R.id.radioGroup_post_type);
        nameEditText = findViewById(R.id.editText_name);
        phoneEditText = findViewById(R.id.editText_phone);
        descriptionEditText = findViewById(R.id.editText_description);
        dateEditText = findViewById(R.id.editText_date);
        locationEditText = findViewById(R.id.editText_location);

        saveButton = findViewById(R.id.button_save);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        Button currentLocationButton = findViewById(R.id.current_location);
        currentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
            }
        });

        locationEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAdvert.this, MapsActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = postTypeGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    Toast.makeText(CreateAdvert.this, "Please select Post Type", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedRadioButton = findViewById(selectedId);
                String postType = selectedRadioButton.getText().toString();
                String name = nameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String description = descriptionEditText.getText().toString();
                String date = dateEditText.getText().toString();
                String location = locationEditText.getText().toString();

                boolean result = dbHelper.insertAdvert(postType, name, phone, description, date, location);

                if (result) {
                    Toast.makeText(CreateAdvert.this, "Advert saved successfully", Toast.LENGTH_SHORT).show();
                    clearForm();
                    finish();
                } else {
                    Toast.makeText(CreateAdvert.this, "Error saving advert", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // ✅ Correct location method outside onCreate
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();

                            Geocoder geocoder = new Geocoder(CreateAdvert.this, Locale.getDefault());
                            try {
                                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                                if (!addresses.isEmpty()) {
                                    String address = addresses.get(0).getAddressLine(0);
                                    locationEditText.setText(address);
                                } else {
                                    locationEditText.setText(latitude + ", " + longitude);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                locationEditText.setText(latitude + ", " + longitude);
                            }
                        } else {
                            Toast.makeText(CreateAdvert.this, "Unable to get location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // ✅ Permission result handler
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            double latitude = data.getDoubleExtra("latitude", 0.0);
            double longitude = data.getDoubleExtra("longitude", 0.0);

            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (!addresses.isEmpty()) {
                    String address = addresses.get(0).getAddressLine(0);
                    locationEditText.setText(address);
                } else {
                    locationEditText.setText(latitude + ", " + longitude);
                }
            } catch (IOException e) {
                e.printStackTrace();
                locationEditText.setText(latitude + ", " + longitude);
            }
        }
    }

    private void clearForm() {
        postTypeGroup.clearCheck();
        nameEditText.setText("");
        phoneEditText.setText("");
        descriptionEditText.setText("");
        dateEditText.setText("");
        locationEditText.setText("");
    }
}
