package com.maneesha14w.movietracker;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Search extends AppCompatActivity {
    private EditText et_search;
    private DbHelper dbHelper;
    private ListView lv_result;
    private ArrayList<String> results;
    private TextView tv_result;
    private ArrayList<String> alreadysearched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        et_search = findViewById(R.id.et_search);
        lv_result = findViewById(R.id.lv_result);
        tv_result = findViewById(R.id.tv_result);
        dbHelper = new DbHelper(this);
        results = new ArrayList<>();
        alreadysearched = new ArrayList<>();
    }

    public void searchQuery(View view) {
        String searchKeyword = et_search.getText().toString().trim().toLowerCase();
        if (searchKeyword.isEmpty()) {
            Toaster("Search Query cannot be empty!");
        }

        if (alreadysearched.contains(searchKeyword)){
            Toaster("Results already Displayed!");
        }

        else {
            alreadysearched.add(searchKeyword);
            Cursor data = dbHelper.getAllData();

            if (data.getCount() == 0) {
                Toaster("No such word in the database");
            }
            else {
                while (data.moveToNext()) {
                    if (data.getString(1).toLowerCase().contains(searchKeyword)) {
                        results.add(data.getString(1) + " directed by " + data.getString(3) + " ("+ data.getInt(2)+") ");
                    }
                    if (data.getString(3).toLowerCase().contains(searchKeyword)) {
                        results.add(data.getString(3) + " directed " + data.getString(1) + " ("+ data.getInt(2)+") ");
                    }
                    if (data.getString(4).toLowerCase().contains(searchKeyword)) {
                        String fullName = "";
                        ArrayList<String> actors = new ArrayList<>(Arrays.asList(data.getString(4).split(",")));
                        for (String name : actors) {
                            if (name.toLowerCase().trim().contains(searchKeyword)) {
                                fullName = name;
                            }
                        }
                        results.add(fullName+ " starred in " + data.getString(1) + " ("+ data.getInt(2)+") ");
                    }
                }

                showResult();
            }
        }

    }

    private void showResult() {
        //create the list adapter and set the adapter
        ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
        lv_result.setAdapter(adapter);
        tv_result.setVisibility(View.VISIBLE);
    }

    private void Toaster(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}