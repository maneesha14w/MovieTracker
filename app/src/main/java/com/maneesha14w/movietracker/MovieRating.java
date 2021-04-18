package com.maneesha14w.movietracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MovieRating extends AppCompatActivity {
    //vars
    private static final String TAG = "MOVIE_RATING";
    private TextView movieTitle, tv_ratingTitle, tv_ratingValue;
    private String imageUrl;
    private AlertDialog alertDialog;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_rating);

        //strict mode policy overriding
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        movieTitle = findViewById(R.id.tv_movieTitleRating);
        tv_ratingTitle = findViewById(R.id.tv_ratingTitle);
        tv_ratingValue = findViewById(R.id.tv_ratingValue);
        imgView = findViewById(R.id.iv_moviePoster);
        loadDialog();

        //get title of movie
        Intent receivedIntent = getIntent();
        String title = receivedIntent.getStringExtra("title");
        NetworkThread netThread = new NetworkThread(title);
        new Thread(netThread).start();

    }

    private void loadDialog() { //the loading menu
        Activity activity = MovieRating.this;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        //inflation
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_box, null));
        builder.setCancelable(false);

        alertDialog = builder.create(); //creation
        alertDialog.show();
    }

    private void dialogDismiss() {
        alertDialog.dismiss();
    } //dismiss load screen animation

    //thread class that handles network
    class NetworkThread implements Runnable {

        private final String baseUrl = "https://imdb-api.com/en/API/"; //base url
        private final String apiKey = "/k_gg6jw7wv/"; //api key
        private String title; //title set in constructor

        NetworkThread(String title) {
            this.title = title;
        }


        @Override
        public void run() {
            String query = baseUrl + "Search" + apiKey + title; //query constructor

            try {
                doTitleOperation(query); //set title and image
            } catch (IOException e) {
                Log.d(TAG, "run: ");

            }
        }

        //get set title and image
        private void doTitleOperation(String query) throws IOException {
            JSONObject jsonObj = getJson(query);
            if (jsonObj == null) { //error
                runOnUiThread(() -> Toaster("Json is null."));
            } else {
                try {
                    String str = jsonObj.getString("results"); //results is an array
                    JSONArray array = new JSONArray(str); //construct array from str

                    String id, title;
                    JSONObject resultObj = array.getJSONObject(0); //get object at index 0 first item in search
                    id = resultObj.getString("id"); //get identifier id value for rating
                    imageUrl = resultObj.getString("image"); //get image url as well since already here
                    title = resultObj.getString("title");
                    runOnUiThread(() -> movieTitle.setText(title));
                    doRatingOperation(id);
                } catch (JSONException e) {
                    Log.d(TAG, "doOperation: Failed!");
                }
            }
        }

        //get rating from id
        private void doRatingOperation(String id) throws IOException {
            String query = baseUrl + "Ratings" + apiKey + id;
            JSONObject jsonObj = getJson(query);
            if (jsonObj == null) {
                runOnUiThread(() -> Toaster("Json is null."));
            } else {
                try {
                    String str = jsonObj.getString("imDb");
                    runOnUiThread(() -> {
                        tv_ratingTitle.setVisibility(View.VISIBLE);
                        tv_ratingValue.setText(str);
                        setImage();

                    });
                } catch (Exception e) {
                    Log.d(TAG, "doOperation: Failed!");
                }
            }

        }


        private void setImage() { //set image bitmap
            try {
                URL url = new URL(imageUrl);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                imgView.setImageBitmap(bmp);
            } catch (Exception e) {
                runOnUiThread(()-> Toaster("Error with Image."));
            }

            dialogDismiss();
        }



        // methd that handles queries via the internet
        private JSONObject getJson(String query) throws IOException {
            StringBuilder result = new StringBuilder();
            HttpURLConnection connection = null;
            InputStream is = null;

            try { //malformed url or url not found
                URL reqUrl = new URL(query);
                connection = (HttpURLConnection) reqUrl.openConnection();
                connection.connect();

                is = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));


                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line).append("\n");
                }

                return new JSONObject(result.toString());
            } catch (Exception e) {
                runOnUiThread(() -> Toaster("Movie Not Found!"));
            } finally { //closing
                connection.disconnect();
                is.close();
            }
            return null;
        }

    }

    private void Toaster(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}