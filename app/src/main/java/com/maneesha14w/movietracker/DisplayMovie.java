package com.maneesha14w.movietracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class DisplayMovie extends AppCompatActivity {

    private static final String TAG = "TAG";
    private DbHelper dbHelper;
    private ListView listView;
    private ArrayList<String> dataList;
    private ArrayList<String> movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_movie);

        listView = findViewById(R.id.lv_movies);
        dbHelper = new DbHelper(this);
        movieList = new ArrayList<>();
        fillListView();
    }

    private void fillListView() {

        // get data and append to an arraylist
        Cursor data = dbHelper.getAllData();
        dataList = new ArrayList<>();
        while (data.moveToNext()) {
            //loop through values from the database in column then add it to the ArrayList
            dataList.add(data.getString(1));
        }

        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, dataList);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

    }


    public void addToFavorites(View view) {

            SparseBooleanArray isChecked = listView.getCheckedItemPositions();

            String valueHolder = "" ;
            int i = 0 ;
            String tempString;
            while (i < isChecked.size()) {

                if (isChecked.valueAt(i)) {
                    tempString = dataList.get(isChecked.keyAt(i));
                    if (!movieList.contains(tempString)) {
                        movieList.add(dataList.get(isChecked.keyAt(i)));
                    }
                }
                else {
                    tempString = dataList.get(isChecked.keyAt(i));
                    if (movieList.contains(tempString)) {
                        movieList.remove(tempString);
                    }
                }

                i++ ;

            }

            for (String movie: movieList) {
                Log.d(TAG, "addToFavorites: "+ movie+ " \n");
            }
    }

//            Cursor data = dbHelper.getId();
//
//            int itemId = -1;
//            while (data.moveToNext()){
//                itemId = data.getInt(0);
//            }
//            if (itemId > -1) {
//                Toast.makeText(this, itemId, Toast.LENGTH_SHORT).show();
//            }
//            else {
//                Toast.makeText(this, "No ID associated with that name", Toast.LENGTH_SHORT).show();
//            }
//        }

    //}
}