package com.example.lostandfound;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Detailed extends AppCompatActivity {

    TextView postTypeTextView, descriptionTextView, dateTextView, locationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detailed);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DBHelper db = new DBHelper(this);

        // Bind views
        postTypeTextView = findViewById(R.id.postTypeTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        dateTextView = findViewById(R.id.dateTextView);
        locationTextView = findViewById(R.id.locationTextView);
        Button removeButton = findViewById(R.id.removeButton);

        Intent intent = getIntent();

        // Handle Remove button
        String id = intent.getStringExtra("id");
        removeButton.setOnClickListener(v -> {
            db.deleteAdvert(id);
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);

        });

        // Set data from intent
        if (intent != null) {
            postTypeTextView.setText(intent.getStringExtra("postType"));
            descriptionTextView.setText(intent.getStringExtra("description"));
            locationTextView.setText(intent.getStringExtra("location"));

            String dateString = intent.getStringExtra("date");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

            try {
                Date date = sdf.parse(dateString);
                long timeInMillis = date.getTime();

                String timeAgo = DateUtils.getRelativeTimeSpanString(
                        timeInMillis,
                        System.currentTimeMillis(),
                        DateUtils.MINUTE_IN_MILLIS
                ).toString();

                dateTextView.setText(timeAgo);

            } catch (Exception e) {
                dateTextView.setText(dateString); // fallback if parsing fails
            }
        }
    }
}
