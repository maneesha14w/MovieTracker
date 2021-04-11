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
    //ui vars
    private EditText et_title, et_year, et_director, et_actors, et_review, et_rating;
    //dbHelper obj
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
        boolean proceedToInsert = true; //bool flag on when to show toasts and if true by the end no errors

        int year = 0, rating = 0;

        try {
            year = Integer.parseInt(et_year.getText().toString().trim()); // if error in parsing flag is enabled
            if (year < 1895) {
                Toaster("Movie cannot be made before the year 1895");
                et_year.getText().clear();
                proceedToInsert = false;
            }
        } catch (Exception e) {
            proceedToInsert = false;
        }

        try {
            rating = Integer.parseInt(et_rating.getText().toString().trim()); // if error in parsing flag is enabled
            if (rating < 1 || rating > 10) {
                Toaster("Rating can only be in the range 1 - 10");
                et_rating.getText().clear();
                proceedToInsert = false;
            }
        } catch (Exception e) {
            proceedToInsert = false;
        }

        // if any of the string editTexts are empty
        if (title.isEmpty() || director.isEmpty() || actors.isEmpty() || review.isEmpty() || et_year.getText().toString().isEmpty() || et_rating.getText().toString().isEmpty()) {
            Toaster("Please make sure all fields are entered!");
        } else if (!proceedToInsert) {
            //do nothing Toasts have been handled separately
        } else {
            dbHelper.insertData(title, year, director, actors, rating, review); //insert data into db
            Toaster(title + " ( " + year + " )" + " has been added successfully!");
        }
    }

    // Toast method
    public void Toaster(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}