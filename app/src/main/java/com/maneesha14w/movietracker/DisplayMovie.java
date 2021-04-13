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

public class DisplayMovie extends AppCompatActivity {

    //vars
    private static final String TAG = "DISPLAY_MOVIE";
    private DbHelper dbHelper; // dbHelper obj
    private ListView listView; // listView var to setAdapter to
    private ArrayList<String> dataList; //arrayList of all data
    private ArrayList<String> movieList; // movies that have been checked

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie);

        listView = findViewById(R.id.lv_movies);
        dbHelper = new DbHelper(this);
        movieList = new ArrayList<>();
        fillListView();
    }

    // populates listView
    private void fillListView() {

        // get data and append to an datList arrayList
        Cursor data = dbHelper.getAllNonFavorites();
        dataList = new ArrayList<>();

        while (data.moveToNext()) { //while has next line of data
            //loop through values from the database in column then add it to the ArrayList
            dataList.add(data.getString(1)); //add titles
        }

        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, dataList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); //checkbox setting by default all not checked

    }

    // Add to favorites btn is clicked
    public void addToFavorites(View view) {

        // SparseBooleanArray
        SparseBooleanArray isChecked = listView.getCheckedItemPositions();

        //none ticked
        if (listView.getCheckedItemPositions().size() == 0) {
            Toaster("Please check the box if you want to add it to favorites");
        }
        else {
            int i = 0;
            String tempString;
            //loop through array
            while (i < isChecked.size()) {
                //isChecked[i] is true
                if (isChecked.valueAt(i)) {
                    //get movie name
                    tempString = dataList.get(isChecked.keyAt(i)).trim();
                    if (!movieList.contains(tempString)) { //if not already added
                        movieList.add(dataList.get(isChecked.keyAt(i)));
                    }
                } else { //already in list
                    tempString = dataList.get(isChecked.keyAt(i));
                    if (movieList.contains(tempString)) {
                        movieList.remove(tempString);
                    }
                }

                i++;

            }
            //end of loop
            StringBuilder movieStr = new StringBuilder(); //display str
            for (String movie : movieList) { //loop through list of checked movies
                //get id of passed movie
                Cursor data = dbHelper.getId(movie);

                int itemId = -1; //default value
                while (data.moveToNext()) { //while has next line of data
                    itemId = data.getInt(0); //get id
                    if (itemId > -1) {
                        Log.d(TAG, "addToFavorites: " + itemId + "\n");
                        dbHelper.addToFavorite(itemId, movie); //updates favorites.
                        movieStr.append(" ").append(movie).append("\n");
                    } else {
                        Toaster("No ID associated with that name");
                        Log.d(TAG, "addToFavorites: No ID associated with that name");
                    }
                }
            }
            Toaster(movieStr + "\n Added to favorites.");
        }
    }

    //toast method.
    public void Toaster(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}

