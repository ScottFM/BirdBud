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
    private static final int DATABASE_VERSION = 8;
    private static final String tableEntries = "listviewentries";
    private static final String tableInfo = "birdinfo";
    private static final String Id = "id";
    private static final String bImage = "image";
    private static final String bScientificName = "name";
    private static final String bCall = "call";
    private static final String bName = "name";
    private static final String bName = "name";

    SQLiteDatabase db;

    public DatabaseAccess(Context context)
    {
        super(context, DATABASE_NAME , null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table " + tableEntries + " (id integer primary key not null , "
                + "image integer not null , sname text not null , call );");
        db.execSQL("create table " + tableInfo + " (name text primary key not null , "
                + "image integer not null , name text not null);");
        BirdEntry goldfinch = new BirdEntry(R.drawable.goldfinch, "American Goldfinch");
        addBird(goldfinch, db);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableEntries);
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

        Cursor cursor = db.rawQuery("select * from " + tableEntries, null);
        int count = cursor.getCount();

        values.put("id", count);
        values.put("image", b.getImageId());
        values.put("name", b.getText());

        db.insert(tableEntries, null, values);
    }

    //Function retrieves list containing all of the entries in the database
    public ArrayList<BirdEntry> getBirdEntries()
    {
        db = this.getReadableDatabase();
        int image;
        String name;
        ArrayList<BirdEntry> allBirds = new ArrayList<BirdEntry>();

        Cursor cursor = db.rawQuery("select image , name from "+ tableEntries, null);
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

        db.close();
        return allBirds;
    }
}
