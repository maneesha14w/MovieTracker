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
    private static final String TAG = "MOVIE_RATING";
    private TextView movieTitle, tv_ratingTitle, tv_ratingValue;
    private String imageUrl;
    private AlertDialog alertDialog;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_rating);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        movieTitle = findViewById(R.id.tv_movieTitleRating);
        tv_ratingTitle = findViewById(R.id.tv_ratingTitle);
        tv_ratingValue = findViewById(R.id.tv_ratingValue);
        imgView = findViewById(R.id.iv_moviePoster);
        loadDialog();

        Intent receivedIntent = getIntent();
        String title = receivedIntent.getStringExtra("title");
        NetworkThread netThread = new NetworkThread(title);
        new Thread(netThread).start();

    }

    private void loadDialog() {
        Activity activity = MovieRating.this;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_box, null));
        builder.setCancelable(false);

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void dialogDismiss() {
        alertDialog.dismiss();
    }

    //thread class
    class NetworkThread implements Runnable {


        private final String baseUrl = "https://imdb-api.com/en/API/";
        private final String apiKey = "/k_gg6jw7wv/";
        private String title;

        NetworkThread(String title) {
            this.title = title;
        }


        @Override
        public void run() {
            String query = baseUrl + "Search" + apiKey + title;

            try {
                doTitleOperation(query);
            } catch (IOException e) {
                Log.d(TAG, "run: ");

            }

//            runOnUiThread(() -> ))

        }


        private void doTitleOperation(String query) throws IOException {
            JSONObject jsonObj = getJson(query);
            if (jsonObj == null) {
                runOnUiThread(() -> Toaster("Json is null."));
            } else {
                try {
                    String str = jsonObj.getString("results");
                    JSONArray array = new JSONArray(str);

                    String id, title;
                    JSONObject resultObj = array.getJSONObject(0);
                    id = resultObj.getString("id");
                    imageUrl = resultObj.getString("image");
                    title = resultObj.getString("title");
                    runOnUiThread(() -> movieTitle.setText(title));
                    doRatingOperation(id);
                } catch (JSONException e) {
                    Log.d(TAG, "doOperation: Failed!");
                }
            }
        }


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


        private void setImage() {
            try {
                URL url = new URL(imageUrl);
                Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                imgView.setImageBitmap(bmp);
            } catch (Exception e) {
                runOnUiThread(()-> Toaster("Error with Image."));
            }

            dialogDismiss();
        }




        private JSONObject getJson(String query) throws IOException {
            StringBuilder result = new StringBuilder();
            HttpURLConnection connection = null;
            InputStream is = null;

            try {
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
            } finally {
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