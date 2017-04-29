package com.example.scott.birdbud;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Scott on 4/27/2017.
 */

public class BirdInfo extends AppCompatActivity implements View.OnClickListener, DialogInterface.OnDismissListener {

    ImageView img;
    TextView txtName, txtSName, txtAbout;
    Button bCall, bMap, bEntry;

    int imageId;
    String name;
    String sName;
    String about;
    int call;
    Uri uri;

    SharedPreferences s;
    SharedPreferences.Editor e;
    DatabaseAccess access = new DatabaseAccess(this);

    String notes, latLongString, formattedDate;
    double x, y;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birdinfo);

        initViews();

        assignInfo();

        initLocation();
    }

    private void initViews() {
        s = getSharedPreferences("BIRDS", 0);
        e = s.edit();

        name = s.getString("bird", "");
        img = (ImageView) findViewById(R.id.img);
        img.setOnClickListener(this);
        txtName = (TextView) findViewById(R.id.txtName);
        txtSName = (TextView) findViewById(R.id.txtSName);
        txtAbout = (TextView) findViewById(R.id.txtAbout);
        txtAbout.setMovementMethod(new ScrollingMovementMethod());
        bCall = (Button) findViewById(R.id.btnCall);
        bCall.setOnClickListener(this);
        bMap = (Button) findViewById(R.id.btnMap);
        bMap.setOnClickListener(this);
        bMap.setText("View " + name + " markers");
        bEntry = (Button) findViewById(R.id.btnEntry);
        bEntry.setOnClickListener(this);
        bEntry.setText("Add new " + name + " marker");
    }

    private void assignInfo() {
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

    public BirdInfo() {

    }

    public BirdInfo(int id, String text) {
        this.imageId = id;
        this.name = text;
        uri = null;
    }

    public BirdInfo(Uri u, String text) {
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

    public void initLocation() {                //Get location
        String context = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getSystemService(context);
        //Select criteria for provider
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(true);
        criteria.setPowerRequirement(Criteria.POWER_LOW);
        //Select provider
        String provider = locationManager.getBestProvider(criteria, true);
        //if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        updateWithNewLocation(location);
            //Update location constantly
        locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
        //}
    }

    private final LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            updateWithNewLocation(location);
        }

        public void onProviderDisabled(String provider) {
            updateWithNewLocation(null);
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status,
                                    Bundle extras) {
        }
    };

    private void updateWithNewLocation(Location location) {

        String lastLocation;
        TextView myLocationText;
        String addressString = "No address found";

        lastLocation = "No last known location";
        if (location != null) {
            x= location.getLatitude();
            y= location.getLongitude();

            Geocoder gc = new Geocoder(this, Locale.getDefault());
            try {
                List<Address> addresses = gc.getFromLocation(x, y, 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);

                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++)
                        sb.append(address.getAddressLine(i)).append("\n");

                    sb.append(address.getLocality()).append("\n");
                    sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                }
                addressString = sb.toString();
            } catch (IOException e) {}


        } else {
            latLongString = "No location found";
        }
        //"Your Current Position is:\n" + latLongString + "\n" + addressString );
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

            case R.id.btnMap:
                Intent I2 = new Intent("com.example.Scott.Database.MapsActivity");
                startActivity(I2);

                break;

            case R.id.btnEntry:
                //Get date
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("MMM dd yyyy");
                formattedDate = df.format(c.getTime());



                //Get notes
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setOnDismissListener(this);
                alertDialog.setTitle("Write a note:");

                final EditText input = new EditText(this);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Add entry",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int which) {
                                // Write your code here to execute after dialog
                                notes = input.getText().toString();
                            }
                        });
                alertDialog.show();

                break;

        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Marker m = new Marker(name, x, y, formattedDate, notes);
        access.addMarker(m);
        Toast.makeText(this, "New marker added! Check it in the map!", Toast.LENGTH_SHORT).show();

    }
}
