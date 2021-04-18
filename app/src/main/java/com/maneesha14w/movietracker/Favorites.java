package com.maneesha14w.movietracker;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class Favorites extends AppCompatActivity {

    //vars
    private static final String TAG = "FAVORITES";
    private DbHelper dbHelper; // dbHelper obj
    private ListView listView; // listView var to setAdapter to
    private ArrayList<String> deselectedMovieList; // movies that have been deselected
    private ArrayList<String> reFavoredList; // movies that have been selected again
    private ArrayList<String> favoritesList; //all favorites

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        listView = findViewById(R.id.lv_favorites);
        dbHelper = new DbHelper(this);
        deselectedMovieList = new ArrayList<>(); //deselected movies
        reFavoredList = new ArrayList<>(); //deselected movies that were reselected
        fillListView();
    }

    private void fillListView() {

        // get data and append to an deselectedMovieList arrayList
        Cursor data = dbHelper.getAllFavorites();
        favoritesList = new ArrayList<>();


        while (data.moveToNext()) { //while has next line of data
            //loop through values from the database in column then add it to the ArrayList
            favoritesList.add(data.getString(1)); //adds titles
        }

        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, favoritesList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //checkbox

        //setting all checkboxes to ticked
        for (int i = 0; i < adapter.getCount(); i++) {
            listView.setItemChecked(i, true);
        }

    }

    //saveBtn clicked
    public void deselectFavorites(View view) {
        // SparseBooleanArray
        SparseBooleanArray isChecked = listView.getCheckedItemPositions();

        //vars
        int i = 0, counter = 0;
        String tempString = "";


        while (i < isChecked.size()) { //checkboxes that are checked
            tempString = favoritesList.get(i);
            if (!isChecked.valueAt(i) && !deselectedMovieList.contains(tempString)) { //new value
                deselectedMovieList.add(tempString);
            }
            if (isChecked.valueAt(i) && deselectedMovieList.contains(tempString)) { //old value
                deselectedMovieList.remove(tempString);
                reFavoredList.add(tempString); //movie has been added back into favourites
            }
            i++;
        }

        // end of loop
        //Log.d(TAG, "deselectFavorites: " + deselectedMovieList.size());
        for (String movie : deselectedMovieList) { //loop through list of unchecked movies
            Cursor data = dbHelper.getId(movie);
            int itemId = -1; //default value
            while (data.moveToNext()) { //while has next line of data
                itemId = data.getInt(0); //get id
                if (itemId > -1) {
                    Log.d(TAG, "addToFavorites: " + itemId + "\n");
                    dbHelper.removeFromFavorite(itemId, movie); //updates favorites.
                } else {
                    Toaster("No ID associated with that name");
                    Log.d(TAG, "addToFavorites: No ID associated with that name");
                }

            }
        }

        for (String movie : reFavoredList) { //loop through list of checked movies
            //get id of passed movie
            Cursor data = dbHelper.getId(movie);

            int itemId = -1; //default value
            while (data.moveToNext()) { //while has next line of data
                itemId = data.getInt(0); //get id
                if (itemId > -1) {
                    Log.d(TAG, "addToFavorites: " + itemId + "\n");
                    dbHelper.addToFavorite(itemId, movie); //updates favorites.
                } else {
                    Toaster("No ID associated with that name");
                    Log.d(TAG, "addToFavorites: No ID associated with that name");
                }
            }
        }

        Toaster("Updated.");
    }

    //}

    public void Toaster(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}