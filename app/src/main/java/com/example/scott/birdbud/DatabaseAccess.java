package com.example.scott.birdbud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scott on 4/27/2017.
 */

public class DatabaseAccess extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BirdBud.db";
    private static final int DATABASE_VERSION = 19;
    private static final String tableInfo = "birdinfo";
    private static final String tableMarkers = "mapmarkers";

    SQLiteDatabase db;

    public DatabaseAccess(Context context)
    {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + tableInfo + " (name text primary key not null , "
                + "image integer not null , sname text , call integer , about text );");
        db.execSQL("create table if not exists " + tableMarkers + " (name text not null , "
                + "xval text not null , yval text not null , date text not null , notes text);");

        BirdEntry goldfinch = new BirdEntry(R.drawable.goldfinch, R.raw.goldfinch, "American Goldfinch", "Spinus Tristus", "A small finch with a short, conical bill and a small head, long wings, and short, notched tail. \n\nThe goldfinch’s main natural habitats are weedy fields and floodplains, where plants such as thistles and asters are common. \n\nAmerican goldfinches are the state bird of New Jersey, Washington, and Iowa.");
        //BirdEntry goldfinch = new BirdEntry(R.drawable.goldfinch, R.raw.goldfinch, "Default name", "Default Scientific", "Default bio. \n\nDefault habitat. \n\nDefault fun fact.");
        //BirdEntry goldfinch = new BirdEntry(R.drawable.goldfinch, R.raw.goldfinch, "Default name", "Default Scientific", "Default bio. \n\nDefault habitat. \n\nDefault fun fact.");
        //BirdEntry goldfinch = new BirdEntry(R.drawable.goldfinch, R.raw.goldfinch, "Default name", "Default Scientific", "Default bio. \n\nDefault habitat. \n\nDefault fun fact.");
        //BirdEntry goldfinch = new BirdEntry(R.drawable.goldfinch, R.raw.goldfinch, "Default name", "Default Scientific", "Default bio. \n\nDefault habitat. \n\nDefault fun fact.");BirdEntry goldfinch = new BirdEntry(R.drawable.goldfinch, R.raw.goldfinch, "Default name", "Default Scientific", "Default bio. \n\nDefault habitat. \n\nDefault fun fact.");
        //BirdEntry goldfinch = new BirdEntry(R.drawable.goldfinch, R.raw.goldfinch, "Default name", "Default Scientific", "Default bio. \n\nDefault habitat. \n\nDefault fun fact.");
        //BirdEntry goldfinch = new BirdEntry(R.drawable.goldfinch, R.raw.goldfinch, "Default name", "Default Scientific", "Default bio. \n\nDefault habitat. \n\nDefault fun fact.");
        //BirdEntry goldfinch = new BirdEntry(R.drawable.goldfinch, R.raw.goldfinch, "Default name", "Default Scientific", "Default bio. \n\nDefault habitat. \n\nDefault fun fact.");

        //Marker victor = new Marker("American Goldfinch", 41.7317, -92.2980);

        addBird(goldfinch, db);
        //addMarker(victor, db);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableInfo);
        db.execSQL("DROP TABLE IF EXISTS " + tableMarkers);
        this.onCreate(db);
    }

    /* This function would be used to add entries to database from user side
    public long addBird(BirdEntry b)
    {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        Cursor cursor = db.rawQuery("select * from users", null);
        int count = cursor.getCount();

        values.put("id", count);
        values.put("user", user);
        values.put("pass", pass);

        //Do not add if there is already an instance of that username
        long conflict = db.insertWithOnConflict(table, null,  values ,SQLiteDatabase.CONFLICT_IGNORE);

        db.close();

        return conflict;
    }*/

    //Call this to hard-code in some fields
    public void addBird(BirdEntry b, SQLiteDatabase db)
    {
        //db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        db.rawQuery("select * from " + tableInfo, null);

        values.put("image", b.getImageId());
        values.put("name", b.getName());
        values.put("call", b.getCall());
        values.put("sname", b.getsName());
        values.put("about", b.getAbout());

        db.insert(tableInfo, null, values);
    }

    //Function retrieves list containing all of the entries in the database
    public ArrayList<BirdEntry> getBirdEntries()
    {
        db = this.getReadableDatabase();
        int image;
        String name;
        ArrayList<BirdEntry> allBirds = new ArrayList<BirdEntry>();

        Cursor cursor = db.rawQuery("select image , name from "+ tableInfo, null);
        if (cursor.moveToFirst())
        {
            do {
                image = cursor.getInt(0);
                name = cursor.getString(1);
                BirdEntry b = new BirdEntry(image, name);
                allBirds.add(b);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return allBirds;
    }

    //Function fetches the stored integers for an entry.
    public ArrayList<Integer> getImageAndCall(String name)
    //Pre: name is already in the database, is unique
    {
        db = this.getReadableDatabase();
        int image;
        int call;
        ArrayList<Integer> infoList = new ArrayList<Integer>();

        Cursor cursor = db.rawQuery("select image , call from "+ tableInfo + " where TRIM(name) = '" + name.trim() + "'", null);
        cursor.moveToFirst();

        image = cursor.getInt(0);
        infoList.add(image);
        call = cursor.getInt(1);
        infoList.add(call);

        cursor.close();
        db.close();
        return infoList;
    }

    //Function fetches the stored strings for an entry.
    public ArrayList<String> getStrings(String name)
    //Pre: name is already in the database, is unique
    {
        db = this.getReadableDatabase();
        String sName;
        String about;
        ArrayList<String> infoList = new ArrayList<String>();

        Cursor cursor = db.rawQuery("select sname , about from "+ tableInfo + " where TRIM(name) = '"+name+"'", null);
        cursor.moveToFirst();

        sName = cursor.getString(0);
        infoList.add(sName);
        about = cursor.getString(1);
        infoList.add(about);

        cursor.close();
        db.close();
        return infoList;
    }

    /* Used to test database
    //Call this to hard-code add markers. Will take other version to dynamically add
    public void addMarker(Marker m, SQLiteDatabase db)
    {
        //db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        db.rawQuery("select * from " + tableMarkers, null);

        values.put("name", m.getName());
        values.put("xval", m.getXval());
        values.put("yval", m.getYval());

        db.insert(tableMarkers, null, values);
    }*/

    //This function would be used to add entries to database from user side
    public long addMarker(Marker m)
    {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        db.rawQuery("select * from " + tableMarkers, null);

        values.put("name", m.getName());
        values.put("xval", m.getXval());
        values.put("yval", m.getYval());
        values.put("date", m.getDate());
        values.put("notes", m.getNotes());

        db.insert(tableMarkers, null, values);

        //Do not add if there is already an instance of that username
        long conflict = db.insertWithOnConflict(tableMarkers, null,  values ,SQLiteDatabase.CONFLICT_IGNORE);

        db.close();

        return conflict;
    }


    public ArrayList<Marker> getMarkers(String name)
    {
        db = this.getReadableDatabase();
        ArrayList<Marker> infoList = new ArrayList<Marker>();

        Cursor cursor = db.rawQuery("select xval , yval , date , notes from "+ tableMarkers + " where TRIM(name) = '" + name.trim() + "'", null);
        cursor.moveToFirst();

        if (cursor.moveToFirst())
        {
            do {
                String xval = cursor.getString(0);
                String yval = cursor.getString(1);
                String date = cursor.getString(2);
                String notes = cursor.getString(3);

                Marker m = new Marker(name, xval, yval, date, notes);
                infoList.add(m);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return infoList;
    }
}
