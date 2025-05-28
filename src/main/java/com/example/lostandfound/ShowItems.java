package com.example.lostandfound;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lostandfound.AdvertAdapter;
import com.example.lostandfound.AdvertItem;
import com.example.lostandfound.DBHelper;
import com.example.lostandfound.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShowItems extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdvertAdapter adapter;
    private List<AdvertItem> allItems;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_items);

        recyclerView = findViewById(R.id.recyclerView); // Use only ONE recyclerView202
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);

        // Fetch all items (Lost + Found)
        allItems = dbHelper.getAllAdvertItems();

        adapter = new AdvertAdapter(allItems);
        recyclerView.setAdapter(adapter);
    }
}
