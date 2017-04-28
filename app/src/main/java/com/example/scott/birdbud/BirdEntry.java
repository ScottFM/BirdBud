package com.example.scott.birdbud;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Scott on 4/27/2017.
 */

public class BirdEntry extends AppCompatActivity {

    int imageId;
    String name;
    String sName;
    String about;
    int call;

    public BirdEntry(int image, int call, String name, String sName, String about)
    {
        this.imageId = image;
        this.call = call;
        this.name = name;
        this.sName = sName;
        this.about = about;
    }

    public BirdEntry(int image, String name)
    {
        this.imageId = image;
        this.name = name;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public void setCall(int call) {
        this.call = call;
    }

    public int getImageId() {
        return imageId;
    }

    public String getName() {
        return name;
    }

    public String getsName() {
        return sName;
    }

    public String getAbout() {
        return about;
    }

    public int getCall() {
        return call;
    }
}
