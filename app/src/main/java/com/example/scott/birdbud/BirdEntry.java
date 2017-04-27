package com.example.scott.birdbud;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Scott on 4/27/2017.
 */

public class BirdEntry extends AppCompatActivity {

    int id;
    String text;
    Uri uri;

    public BirdEntry(int id, String text){
        this.id = id;
        this.text = text;
        uri = null;
    }

    public BirdEntry(Uri u, String text){
        uri = u;
        this.text = text;
        id = -1;
    }

    public int getImageId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public Uri getUri() {
        return uri;
    }
}
