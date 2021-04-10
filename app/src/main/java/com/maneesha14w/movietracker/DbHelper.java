package com.maneesha14w.movietracker;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MoviesTracker.db";
    protected static final String TABLE_NAME = "MoviesTracker";

    //columns
    private static final String COLUMN_ID = "id";
    protected static final String COLUMN_TITLE = "title";
    protected static final String COLUMN_YEAR = "year";
    protected static final String COLUMN_DIRECTOR = "director";
    protected static final String COLUMN_ACTORS = "actors";
    protected static final String COLUMN_RATING = "rating";
    protected static final String COLUMN_REVIEW = "review";
    protected static final String COLUMN_FAVORITE= "favorite";



    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " +TABLE_NAME+
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, year INTEGER, director TEXT, actors TEXT, rating INTEGER, review TEXT, favorite BOOL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
