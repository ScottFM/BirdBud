package com.example.scott.birdbud;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Scott on 4/27/2017.
 */

public class BirdInfo extends AppCompatActivity implements View.OnClickListener {

    ImageView img;
    TextView txtName, txtSName, txtAbout;
    Button bCall;

    int imageId;
    String name;
    String sName;
    String about;
    int call;
    Uri uri;

    SharedPreferences s;
    SharedPreferences.Editor e;

    DatabaseAccess access = new DatabaseAccess(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birdinfo);

        initViews();

        assignInfo();
    }

    private void initViews()
    {
        img = (ImageView) findViewById(R.id.img);
        img.setOnClickListener(this);
        txtName = (TextView) findViewById(R.id.txtName);
        txtSName = (TextView) findViewById(R.id.txtSName);
        txtAbout = (TextView) findViewById(R.id.txtAbout);
        bCall = (Button) findViewById(R.id.btnCall);
        bCall.setOnClickListener(this);

        s = getSharedPreferences("BIRDS", 0);
        e = s.edit();
    }

    private void assignInfo()
    {
        name = s.getString("bird", "");
        txtName.setText(name);

        //Fetch info from database
        ArrayList imgAndCall = access.getImageAndCall(name);
        //image at [0]
        imageId = (Integer) imgAndCall.get(0);
        img.setImageResource(imageId);
        //call at [1]
        call = (Integer) imgAndCall.get(1);

        ArrayList strings = access.getStrings(name);
        //scientific name at [0]
        sName = String.valueOf(strings.get(0));
        txtSName.setText(sName);
        //about at [1]
        about = String.valueOf(strings.get(1));
        txtAbout.setText(about);
    }

    public BirdInfo(){

    }

    public BirdInfo(int id, String text){
        this.imageId = id;
        this.name = text;
        uri = null;
    }

    public BirdInfo(Uri u, String text){
        this.uri = u;
        this.name = text;
        this.imageId = -1;
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnCall:
                MediaPlayer mp = MediaPlayer.create(BirdInfo.this, call);
                mp.start();
                break;
            case R.id.img:

                e.putInt("image", imageId);
                e.commit();

                Intent I = new Intent("com.example.Scott.Database.Fullscreen");
                startActivity(I);

                break;
        }
    }
}
