package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Button create,show;
        create=findViewById(R.id.create);
        show=findViewById(R.id.show);
        create.setOnClickListener(v->{
            Intent intent=new Intent(MainActivity.this,CreateAdvert.class);
            startActivity(intent);
        });
        show.setOnClickListener(v->{
            Intent intent=new Intent(MainActivity.this,ShowItems.class);
            startActivity(intent);
        });



// Example: Print all locations to the log

        Button showOnMap = findViewById(R.id.show_on_map);
        showOnMap.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(MainActivity.this);
            List<String> allLocations = dbHelper.getAllLocations();
            for (String loc : allLocations) {
                Log.d("Locations", loc);
            }



            Intent intent = new Intent(MainActivity.this, MapsActivity.class);
            intent.putStringArrayListExtra("locations", new ArrayList<>(allLocations));
            startActivity(intent);

        });


    }
}