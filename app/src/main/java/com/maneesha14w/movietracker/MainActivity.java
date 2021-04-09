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

    public void registerMovie(View view) {
        Intent intent = new Intent(this, RegisterMovie.class);
        startActivity(intent);
    }

    public void displayMovie(View view) {
        Intent intent = new Intent(this, RegisterMovie.class);
        startActivity(intent);
    }

    public void seeFavorites(View view) {
        Intent intent = new Intent(this, RegisterMovie.class);
        startActivity(intent);
    }

    public void editMovies(View view) {
        Intent intent = new Intent(this, RegisterMovie.class);
        startActivity(intent);
    }

    public void seeRatings(View view) {
        Intent intent = new Intent(this, RegisterMovie.class);
        startActivity(intent);
    }

    public void search(View view) {
        Intent intent = new Intent(this, RegisterMovie.class);
        startActivity(intent);
    }
}