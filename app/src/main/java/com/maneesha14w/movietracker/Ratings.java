package com.maneesha14w.movietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

public class Ratings extends AppCompatActivity {

    private String expression;
    private DbHelper dbHelper;
    private RadioGroup rGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ratings);
        dbHelper = new DbHelper(this);
        setRadioButtons();
    }

    private void setRadioButtons() {
        ArrayList<String> movieNames = new ArrayList<>();
        Cursor data = dbHelper.getAllData();

        while (data.moveToNext()) {
            movieNames.add(data.getString(1));
        }

        final RadioButton[] rBtns = new RadioButton[movieNames.size()];
        rGroup = findViewById(R.id.radioGroup);

        for (int i = 0; i < movieNames.size(); i++) {
            rBtns[i] = new RadioButton(this);
            rBtns[i].setText(movieNames.get(i));
            rBtns[i].setId(i);
            rGroup.addView(rBtns[i]);
        }
    }

    //find movie btn click
    public void findMovie(View view) {
        int id = rGroup.getCheckedRadioButtonId();
        RadioButton selectedBtn = findViewById(id);

        String title = selectedBtn.getText().toString();
        Intent intent = new Intent(Ratings.this, MovieRating.class);
        intent.putExtra("title", title);
        startActivity(intent);
    }

    public void Toaster(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


}