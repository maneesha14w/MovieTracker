package com.maneesha14w.movietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class Favorites extends AppCompatActivity {

    //vars
    private static final String TAG = "DISPLAY_MOVIE";
    private DbHelper dbHelper; // dbHelper obj
    private ListView listView; // listView var to setAdapter to

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);


    }

    public void deselectFavorites(View view) {

    }
}