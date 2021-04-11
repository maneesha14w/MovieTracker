package com.maneesha14w.movietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // register btn click
    public void registerMovie(View view) {
        Intent intent = new Intent(this, RegisterMovie.class);
        startActivity(intent);
    }

    // displayMovie btn click
    public void displayMovie(View view) {
        Intent intent = new Intent(this, DisplayMovie.class);
        startActivity(intent);
    }

    //seeFavorites btn click
    public void seeFavorites(View view) {
        Intent intent = new Intent(this, Favorites.class);
        startActivity(intent);
    }

    // editMovies btn click
    public void editMovies(View view) {
        Intent intent = new Intent(this, EditMovies.class);
        startActivity(intent);
    }

    // seeRatings btn click
    public void seeRatings(View view) {
        Intent intent = new Intent(this, Ratings.class);
        startActivity(intent);
    }

    // searchBtn btn click
    public void search(View view) {
        Intent intent = new Intent(this, Search.class);
        startActivity(intent);
    }
}