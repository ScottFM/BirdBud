package com.example.scott.birdbud;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    RelativeLayout relativeLayout;
    ArrayList<BirdEntry> entryArray;
    private static CustomAdapter adapter;
    ListView lv;
    DatabaseAccess access = new DatabaseAccess(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);

        lv = (ListView) findViewById(R.id.lstMain);

        makeAndFillListView();
    }

    public void makeAndFillListView() {

        entryArray = new ArrayList<BirdEntry>();
        // Create the adapter to convert the array to views
        CustomAdapter adapter = new CustomAdapter(this, entryArray);
        // Attach the adapter to a ListView
        lv.setAdapter(adapter);

        ArrayList<BirdEntry> tempList = access.getBirdEntries();
        for (int i = 0; i < tempList.size(); i++) {
            int image = tempList.get(i).getImageId();
            String name = tempList.get(i).getText();
            addViewToList(new BirdEntry(image, name));
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                BirdEntry entry = entryArray.get(position);

                Toast.makeText(MainActivity.this, "You clicked on " + entry.getText(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void addViewToList(BirdEntry e){
        // Create the adapter to convert the array to views
        CustomAdapter adapter = new CustomAdapter(this, entryArray);
        // Attach the adapter to a ListView
        lv.setAdapter(adapter);
        entryArray.add(e);
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.btnAddView:
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 444);
                break;
        }*/
    }
}
