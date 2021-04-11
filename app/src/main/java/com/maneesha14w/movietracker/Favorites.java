package com.maneesha14w.movietracker;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    //vars
    private static final String TAG = "DISPLAY_MOVIE";
    private DbHelper dbHelper; // dbHelper obj
    private ListView listView; // listView var to setAdapter to
    private ArrayList<String> deselectedMovieList; // movies that have been deselected

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        listView = findViewById(R.id.lv_favorites);
        dbHelper = new DbHelper(this);
        deselectedMovieList = new ArrayList<>();
        fillListView();
    }

    private void fillListView() {

        // get data and append to an deselectedMovieList arrayList
        Cursor data = dbHelper.getAllData();
        deselectedMovieList = new ArrayList<>();


        while (data.moveToNext()) { //while has next line of data
            //loop through values from the database in column then add it to the ArrayList
            deselectedMovieList.add(data.getString(1)); //adds titles
        }

        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, deselectedMovieList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //checkbox

        //setting all checkboxes to ticked
        for (int i = 0; i < adapter.getCount(); i++) {
            listView.setItemChecked(i, true);
        }

    }

    public void deselectFavorites(View view) {

    }
}