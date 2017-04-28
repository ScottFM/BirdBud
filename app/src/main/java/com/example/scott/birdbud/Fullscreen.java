package com.example.scott.birdbud;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

/**
 * Created by Scott on 4/27/2017.
 */

public class Fullscreen extends Activity implements View.OnClickListener{

    ImageView img;
    SharedPreferences s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen);
        setTheme(android.R.style.Theme_Black_NoTitleBar_Fullscreen); // (for Android Built In Theme)

        s = getSharedPreferences("BIRDS", 0);

        int id = s.getInt("image", 0);

        img = (ImageView) findViewById(R.id.img);
        img.setImageResource(id);
        img.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
