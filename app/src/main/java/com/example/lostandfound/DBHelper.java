package com.example.lostandfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "UserData.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase DB) {
        DB.execSQL("CREATE TABLE LostAndFound (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "unique_id TEXT UNIQUE, " +
                "postType TEXT, " +
                "name TEXT, " +
                "phone TEXT, " +
                "description TEXT, " +
                "date TEXT, " +
                "location TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase DB, int oldVersion, int newVersion) {
        DB.execSQL("DROP TABLE IF EXISTS LostAndFound");

        onCreate(DB);
    }





    public boolean insertAdvert(String postType, String name, String phone, String description, String date, String location) {
        SQLiteDatabase DB = this.getWritableDatabase();

        String uniqueId = UUID.randomUUID().toString(); // Generates a unique ID like "b9f6f396-9bb5-4df5-b22c-123456789abc"

        ContentValues contentValues = new ContentValues();
        contentValues.put("unique_id", uniqueId);  // Add this to your table schema
        contentValues.put("postType", postType);
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("description", description);
        contentValues.put("date", date);
        contentValues.put("location", location);

        long result = DB.insert("LostAndFound", null, contentValues);
        return result != -1;
    }




    public List<AdvertItem> getAllAdvertItems() {
        List<AdvertItem> advertList = new ArrayList<>();
        SQLiteDatabase DB = this.getReadableDatabase();
        Cursor cursor = DB.rawQuery("SELECT * FROM LostAndFound", null);

        if (cursor.moveToFirst()) {
            do {
                AdvertItem item = new AdvertItem(
                        cursor.getString(1),
                        cursor.getString(2), // post_type
                        cursor.getString(3), // name
                        cursor.getString(4), // phone
                        cursor.getString(5), // description
                        cursor.getString(6), // date
                        cursor.getString(7)  // location
                );
                advertList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return advertList;
    }



    public boolean updateAdvert(int id, String postType, String name, String phone, String description, String date, String location) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("postType", postType);
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("description", description);
        contentValues.put("date", date);
        contentValues.put("location", location);

        int result = DB.update("LostAndFound", contentValues, "id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }


    public boolean deleteAdvert(String id) {
        SQLiteDatabase DB = this.getWritableDatabase();
        int result = DB.delete("LostAndFound", "unique_id=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public List<String> getAllLocations() {
        List<String> locations = new ArrayList<>();
        SQLiteDatabase DB = this.getReadableDatabase();

        Cursor cursor = DB.rawQuery("SELECT location FROM LostAndFound", null);

        if (cursor.moveToFirst()) {
            do {
                // Since only one column is selected, use index 0 directly
                String location = cursor.getString(0);
                locations.add(location);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return locations;
    }



}

