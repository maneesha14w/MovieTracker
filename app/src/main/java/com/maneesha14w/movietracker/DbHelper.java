package com.maneesha14w.movietracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public void insertData(String title, int year, String director, String actors, int rating, String review) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(DbHelper.COLUMN_TITLE, title);
        cv.put(DbHelper.COLUMN_YEAR, year);
        cv.put(DbHelper.COLUMN_DIRECTOR, director);
        cv.put(DbHelper.COLUMN_ACTORS, actors);
        cv.put(DbHelper.COLUMN_RATING, rating);
        cv.put(DbHelper.COLUMN_REVIEW, review);
        cv.put(DbHelper.COLUMN_FAVORITE, 0);

        long id = db.insert(DbHelper.TABLE_NAME, null, cv);
        if (id == -1) {
            Log.d("LOG", "insertData: failed");
        } else {
            Log.d("LOG", "insertData: was successful");
        }
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " +COLUMN_TITLE;
        return db.rawQuery(query, null);
    }

    public Cursor getId(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " +COLUMN_ID+ " FROM " +TABLE_NAME+ " WHERE " +COLUMN_TITLE+ " = '" +name+ "'";
        return db.rawQuery(query, null);
    }
}
