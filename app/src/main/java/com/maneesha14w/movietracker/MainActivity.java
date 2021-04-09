package com.maneesha14w.movietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void registerMovie(View view) {
        Toast.makeText(this, "Registered a Movie", Toast.LENGTH_SHORT).show();
    }

    public void displayMovie(View view) {

    }

    public void seeFavorites(View view) {

    }

    public void editMovies(View view) {

    }

    public void seeRatings(View view) {

    }

    public void search(View view) {

    }
}