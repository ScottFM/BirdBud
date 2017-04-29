package com.example.scott.birdbud;

/**
 * Created by Scott on 4/28/2017.
 */

import android.app.Fragment;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    GoogleMap mGoogleMap;
    MapView mMapView;
    View mView;
    String name;

    SharedPreferences s;
    SharedPreferences.Editor e;
    DatabaseAccess access = new DatabaseAccess(this);

    public MapsActivity(){}


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        s = getSharedPreferences("BIRDS", 0);
        e = s.edit();

        name = s.getString("bird", "");
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;

        mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(com.google.android.gms.maps.model.Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(com.google.android.gms.maps.model.Marker marker) {
                LinearLayout info = new LinearLayout(getBaseContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getBaseContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getBaseContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        ArrayList<Marker> allMarkers;
        if (name.equals("all"))
        {
            allMarkers = access.getAllMarkers();
        }
        else{
            allMarkers = access.getMarkers(name);
        }

        for (int i = 0; i < allMarkers.size(); i++)
        {
            Marker m = allMarkers.get(i);
            LatLng latLng = new LatLng(Double.valueOf(m.getXval()), Double.valueOf(m.getYval()));
            mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(m.getName()).snippet("\""+m.getNotes()+"\"" + "\n" + m.getDate()));
            if(i == 0)
            {
                CameraPosition newCam = CameraPosition.builder().target(latLng).zoom(10).build();
                googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(newCam));
            }

        }
    }


}
