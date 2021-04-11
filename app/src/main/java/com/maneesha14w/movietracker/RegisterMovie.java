package com.maneesha14w.movietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class RegisterMovie extends AppCompatActivity {
    private EditText et_title, et_year, et_director, et_actors, et_review, et_rating;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_movie);

        et_title = findViewById(R.id.et_title);
        et_year = findViewById(R.id.et_year);
        et_director = findViewById(R.id.et_director);
        et_actors = findViewById(R.id.et_actors);
        et_review = findViewById(R.id.et_review);
        et_rating = findViewById(R.id.et_rating);
    }

    //save btn click method
    public void saveMovie(View view) {

        dbHelper = new DbHelper(this);


        String title = et_title.getText().toString().trim();
        String director = et_director.getText().toString().trim();
        String actors = et_actors.getText().toString().trim();
        String review = et_review.getText().toString().trim();
        boolean proceedToInsert = true;

        int year = 0, rating = 0;
        try {
            year = Integer.parseInt(et_year.getText().toString().trim());
            if (year < 1895) {
                Toast.makeText(this, "Movie cannot be made before the year 1895", Toast.LENGTH_SHORT).show();
                et_year.getText().clear();
                proceedToInsert = false;
            }
        } catch (Exception e) {
            proceedToInsert = false;
        }

        try {
            rating = Integer.parseInt(et_rating.getText().toString().trim());
            if (rating < 1 || rating > 10) {
                Toast.makeText(this, "Rating can only be in the range 1 - 10", Toast.LENGTH_SHORT).show();
                et_rating.getText().clear();
                proceedToInsert = false;
            }
        } catch (Exception e) {
            proceedToInsert = false;
        }


        if (title.isEmpty() || director.isEmpty() || actors.isEmpty() || review.isEmpty() || et_year.getText().toString().isEmpty() || et_rating.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please make sure all fields are entered!", Toast.LENGTH_SHORT).show();
        } else if (!proceedToInsert) {

        } else {
            dbHelper.insertData(title, year, director, actors, rating, review);
            Toast.makeText(this, title + " ( " + year + " )" + " has been added successfully!", Toast.LENGTH_SHORT).show();
        }
    }

}