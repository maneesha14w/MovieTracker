package com.maneesha14w.movietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class EditMovies extends AppCompatActivity {

    private static final String TAG = "EDIT_MOVIE";
    private DbHelper dbHelper; // dbHelper obj
    private ListView listView; // listView var to setAdapter to
    private ArrayList<String> dataList; //arrayList of all data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_movies);

        listView = findViewById(R.id.lv_editMovies);
        dbHelper = new DbHelper(this);
        fillListView();

    }

    private void fillListView() {
        // get data and append to an datList arrayList
        Cursor data = dbHelper.getAllData();
        dataList = new ArrayList<>();

        while (data.moveToNext()) { //while has next line of data
            //loop through values from the database in column then add it to the ArrayList
            dataList.add(data.getString(1)); //add titles
        }

        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = parent.getItemAtPosition(position).toString();
                Log.d(TAG, "onItemClick: You clicked on " + title);

                Cursor data = dbHelper.getId(title);
                int itemId = -1; //default value
                while (data.moveToNext()) { //while has next line of data
                    itemId = data.getInt(0); //get id
                    if (itemId > -1) {
                        //new activity which shows all info about a movie
                        Intent intent = new Intent(EditMovies.this, MovieInfo.class);
                        intent.putExtra("id", itemId);
                        intent.putExtra("title", title);
                        startActivity(intent);
                    }
                }

            }
        });
    }

}