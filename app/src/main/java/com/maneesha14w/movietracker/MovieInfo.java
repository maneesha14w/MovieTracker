package com.maneesha14w.movietracker;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MovieInfo extends AppCompatActivity {

    private static final String TAG = "MOVIE_INFO";
    private DbHelper dbHelper; // dbHelper obj
    private EditText et_yearInfo, et_directorInfo, et_actorsInfo, et_reviewInfo;
    private TextView tv_title;
    private RatingBar ratingBar;
    private RadioGroup rGroup;
    private RadioButton rb_yes, rb_no;
    private int id;
    private String title;
    private int isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_info);

        dbHelper = new DbHelper(this);

        et_yearInfo = findViewById(R.id.et_yearInfo);
        et_directorInfo = findViewById(R.id.et_directorInfo);
        et_actorsInfo = findViewById(R.id.et_actorsInfo);
        et_reviewInfo = findViewById(R.id.et_reviewInfo);
        ratingBar = findViewById(R.id.ratingBar);
        rGroup = findViewById(R.id.rg_favorite);
        tv_title = findViewById(R.id.tv_movieTitle);
        rb_no = findViewById(R.id.rb_no);
        rb_yes = findViewById(R.id.rb_yes);

        Intent passedIntent = getIntent();
        id = passedIntent.getIntExtra("id", -1);
        title = passedIntent.getStringExtra("title");

        tv_title.setText(title);
        dataLoader();
    }

    private void dataLoader() {
        Cursor data = dbHelper.getMovieDetails(id, title);

        int itemId = -1; //default value
        while (data.moveToNext()) {
            et_yearInfo.setText(String.valueOf(data.getInt(2))); //get year
            et_directorInfo.setText(data.getString(3)); //get director
            et_actorsInfo.setText(data.getString(4)); // get actors
            ratingBar.setRating(data.getFloat(5)); //get rating
            et_reviewInfo.setText(data.getString(6)); //get review
            if (data.getInt(7) == 1) {
                rb_yes.setChecked(true);
            }
            else {
                rb_no.setChecked(true);
            }
        }
    }


    public void updateBtnClicked(View view) {


        String year = et_yearInfo.getText().toString().trim();
        String director = et_directorInfo.getText().toString().trim();
        String actors = et_actorsInfo.getText().toString().trim();
        String review = et_reviewInfo.getText().toString().trim();
        String isFavoriteStr="";
        int rating = (int) ratingBar.getRating();
        boolean noError = true;

        if (year.isEmpty() || director.isEmpty() || actors.isEmpty() || review.isEmpty()) {
            Toaster("Please make sure all fields are entered!");
            noError = false;
        } else {
            try {
                isFavoriteStr = ((RadioButton)findViewById(rGroup.getCheckedRadioButtonId())).getText().toString().toLowerCase();

                if (isFavoriteStr.equals("yes")) {
                    isFavorite = 1;
                }
                else {
                    isFavorite = 0;
                }
                if (Integer.parseInt(year) < 1895) {
                    Toaster("Movie cannot be made before the year 1895");
                    noError = false;
                    et_yearInfo.getText().clear();
                }
            } catch (Exception e) {
                Toaster("Year should be a number.");
                noError = false;
            }
        }

        if (noError){
            dbHelper.columnUpdater(id, title, "year", year);
            dbHelper.columnUpdater(id, title, "director", director);
            dbHelper.columnUpdater(id, title, "actors", actors);
            dbHelper.columnUpdater(id, title, "review", review);
            dbHelper.columnUpdater(id, title, "rating", String.valueOf(rating));
            dbHelper.columnUpdater(id, title,"favorite", String.valueOf(isFavorite));

            Toaster(title + " has been updated!");
        }



    }


    public void Toaster(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}