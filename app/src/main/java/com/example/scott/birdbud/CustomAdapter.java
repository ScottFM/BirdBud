package com.example.scott.birdbud;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Scott on 4/27/2017.
 */

public class CustomAdapter extends ArrayAdapter<BirdEntry>{

    private ArrayList<BirdEntry> entryArray;

    public CustomAdapter(Context context, ArrayList<BirdEntry> entries) {
        super(context, 0, entries);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        BirdEntry entry = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.birdentry, parent, false);
        }
        // Lookup view for data population

        TextView text = (TextView) convertView.findViewById(R.id.textView);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
        // Populate the data into the template view using the data object
        text.setText(entry.getName());

        image.setImageResource(entry.getImageId());

        // Return the completed view to render on screen
        return convertView;

    }
}