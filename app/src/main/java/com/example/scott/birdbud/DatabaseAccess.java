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
    private static final int DATABASE_VERSION = 22;
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
        BirdEntry eagle = new BirdEntry(R.drawable.baldeagle, R.raw.baldeagle, "Bald Eagle", "Haliaeetus Leucocephalus", "The bald eagle has a heavy body, large head, and long, hooked bill. In flight, it holds its broad wings flat like a board. \n\nIt is found near large bodies of open water with an abundant food supply and old-growth trees for nesting. \n\nThe bald eagle was removed from the List of Endangered and Threatened Wildlife in the Lower 48 States on June 28, 2007.");
        BirdEntry rwb = new BirdEntry(R.drawable.redwingedblackbird, R.raw.rwb, "Red Winged Blackbird", "Agelaius Phoeniceus", "A stocky, broad-shouldered blackbird with a slender, conical bill and a medium-length tail. \n\nOften found in fresh and saltwater marshes, along watercourses, water hazards on golf courses, and wet roadsides, as well as drier meadows and old fields. \n\nThe red-winged blackbird is sexually dimorphic; the male is all black with a red shoulder and yellow wing bar, while the female is a nondescript dark brown.");
        BirdEntry cardinal = new BirdEntry(R.drawable.cardinal, R.raw.cardinal, "Northern Cardinal", "Cardinalis Cardinalis", "The Northern Cardinal is a fairly large, long-tailed songbird with a short, very thick bill and a prominent crest \n\nOften found in inhabited areas such as backyards, parks, woodlots, and shrubby forest edges. Northern Cardinals nest in dense tangles of shrubs and vines. \n\nIts sale as a cage bird was banned in the United States by the Migratory Bird Treaty Act of 1918.");
        BirdEntry owl = new BirdEntry(R.drawable.greathornedowl, R.raw.owl, "Great Horned Owl", "Bubo Virginianus", "These are large, thick-bodied owls with two prominent feathered tufts on the head. The wings are broad and rounded. \n\nOften found in woods, particularly young woods interspersed with fields or other open areas. \n\nThe great horned owl is one of the earliest nesting birds in North America, often laying eggs weeks or even months before other raptorial birds.");
        BirdEntry heron = new BirdEntry(R.drawable.heron, R.raw.heron, "Great Blue Heron", "Ardea Herodias", "Largest of the North American herons with long legs, a sinuous neck, and thick, daggerlike bill. Head, chest, and wing plumes give a shaggy appearance. \n\nOften found in saltwater and freshwater habitats, from open coasts, marshes, sloughs, riverbanks, and lakes to backyard goldfish ponds. They also forage in grasslands and agricultural fields. \n\nAn all-white population found only in the Caribbean and Florida was once treated as a separate species and known as the great white heron.");
        BirdEntry killdeer = new BirdEntry(R.drawable.killdeer, R.raw.killdeer, "Killdeer", "Charadrius Vociferus", "Killdeer have the characteristic large, round head, large eye, and short bill of all plovers. They are especially slender and lanky, with a long, pointed tail and long wings. \n\nOften found on open ground with low vegetation (or no vegetation at all), including lawns, golf courses, driveways, parking lots, and gravel-covered roofs, as well as pastures, fields, sandbars and mudflats. \n\nThe killdeer frequently uses a \"broken wing act\" to distract predators from the nest.");
        BirdEntry lark = new BirdEntry(R.drawable.lark, R.raw.meadowlark, "Western Meadowlark", "Sturnella neglecta", "The Western Meadowlark is the size of a robin but chunkier and shorter-tailed, with a flat head, long, slender bill, and a round-shouldered posture that nearly conceals its neck. \n\nOften found in the wide open spaces of native grasslands and agricultural fields for spring and summer breeding and winter foraging. \n\nThe western meadowlark is the state bird of six states: Montana, Kansas, Nebraska, North Dakota, Oregon, and Wyoming.");

        addBird(goldfinch, db);
        addBird(eagle, db);
        addBird(rwb, db);
        addBird(cardinal, db);
        addBird(owl, db);
        addBird(heron, db);
        addBird(killdeer, db);
        addBird(lark, db);



        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tableInfo);
        db.execSQL("DROP TABLE IF EXISTS " + tableMarkers);
        this.onCreate(db);
    }

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

    public ArrayList<Marker> getAllMarkers()
    {
        db = this.getReadableDatabase();
        ArrayList<Marker> infoList = new ArrayList<Marker>();

        Cursor cursor = db.rawQuery("select name , xval , yval , date , notes from "+ tableMarkers, null);
        cursor.moveToFirst();

        if (cursor.moveToFirst())
        {
            do {
                String name = cursor.getString(0);
                String xval = cursor.getString(1);
                String yval = cursor.getString(2);
                String date = cursor.getString(3);
                String notes = cursor.getString(4);

                Marker m = new Marker(name, xval, yval, date, notes);
                infoList.add(m);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return infoList;
    }

    //Remove markers for a single bird
    public void removeMarkers(String n)
    {
        db = this.getWritableDatabase();
        db.delete(tableMarkers, "name=?", new String[] {n});
    }
}
