package com.maneesha14w.movietracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper { //class that extends SQLiteOpenHelper that will manage all db re

    //db vars
    private static final String DATABASE_NAME = "MoviesTracker.db";
    protected static final String TABLE_NAME = "MoviesTracker";

    //columns
    private static final String COLUMN_ID = "_id";
    protected static final String COLUMN_TITLE = "title";
    protected static final String COLUMN_YEAR = "year";
    protected static final String COLUMN_DIRECTOR = "director";
    protected static final String COLUMN_ACTORS = "actors";
    protected static final String COLUMN_RATING = "rating";
    protected static final String COLUMN_REVIEW = "review";
    protected static final String COLUMN_FAVORITE = "favorite";
    private static final String TAG = "DB_HELPER";

    // constructor
    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    //create db with id that auto increments with each entry
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE " + TABLE_NAME +
                        "(_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT, year INTEGER, director TEXT, actors TEXT, rating INTEGER, review TEXT, favorite BOOL)");
    }

    //version upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //insert data into db from register activity
    public void insertData(String title, int year, String director, String actors, int rating, String review) {
        SQLiteDatabase db = this.getWritableDatabase(); //writable db returned

        ContentValues cv = new ContentValues(); //enter all columns
        cv.put(DbHelper.COLUMN_TITLE, title);
        cv.put(DbHelper.COLUMN_YEAR, year);
        cv.put(DbHelper.COLUMN_DIRECTOR, director);
        cv.put(DbHelper.COLUMN_ACTORS, actors);
        cv.put(DbHelper.COLUMN_RATING, rating);
        cv.put(DbHelper.COLUMN_REVIEW, review);
        cv.put(DbHelper.COLUMN_FAVORITE, 0); //default set 0, bool so 0 is false and 1 is true

        long id = db.insert(DbHelper.TABLE_NAME, null, cv); //insert statement returning int
        //if id is -1 some error has happened
        if (id == -1) {
            Log.d("LOG", "insertData: failed");
        } else {
            Log.d("LOG", "insertData: was successful");
        }
    }

    // method that returns all data for listview
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COLUMN_TITLE;
        return db.rawQuery(query, null);
    }

    //returns id of movie title
    public Cursor getId(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COLUMN_ID + " FROM " + TABLE_NAME + " WHERE " + COLUMN_TITLE + " = '" + name.trim() + "'";
        return db.rawQuery(query, null);
    }


    //get all favorites
    public Cursor getAllFavorites() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_FAVORITE + " = '" + 1 + "'";
        return db.rawQuery(query, null);
    }

    //get all non favorites
    public Cursor getAllNonFavorites() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_FAVORITE + " = '" + 0 + "'";
        return db.rawQuery(query, null);
    }

    //edits the favorites column of the movie with id which are passed as params
    public void addToFavorite(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COLUMN_FAVORITE + " = '" + 1 + "' WHERE " + COLUMN_ID + " = '" + id +
                "' AND " + COLUMN_TITLE + " = '" + name + "'";
        Log.d(TAG, "updateFavorite: query: " + query);
        Log.d(TAG, "updateFavorite: Setting id to: " + id);
        db.execSQL(query);
    }

    //remove from favorites
    public void removeFromFavorite(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COLUMN_FAVORITE + " = '" + 0 + "' WHERE " + COLUMN_ID + " = '" + id +
                "' AND " + COLUMN_TITLE + " = '" + name + "' AND " + COLUMN_FAVORITE + " = '" + 1 + "'";
        Log.d(TAG, "removeFavorite: query: " + query);
        Log.d(TAG, "removeFavorite: Setting id to: " + id);
        db.execSQL(query);
    }

    //get all details of a movie
    public Cursor getMovieDetails(int id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = '" + id + "' AND " + COLUMN_TITLE + " = '" + name + "'";
        Log.d(TAG, "getMovieDetails: " + query);
        return db.rawQuery(query, null);
    }

    // column updater that checks the param passed to verify what value to update
    public void columnUpdater(int id, String name, String columnToBeChanged, String newValue) {
        boolean isInt = false;
        String keyPhrase;
        SQLiteDatabase db = this.getWritableDatabase();
        switch (columnToBeChanged) {
            case "year":
                keyPhrase = COLUMN_YEAR;
                isInt = true;
                break;
            case "director":
                keyPhrase = COLUMN_DIRECTOR;
                break;
            case "actors":
                keyPhrase = COLUMN_ACTORS;
                break;
            case "review":
                keyPhrase = COLUMN_REVIEW;
                break;
            case "rating":
                keyPhrase = COLUMN_RATING;
                isInt = true;
                break;
            case "favorite":
                keyPhrase = COLUMN_FAVORITE;
                isInt = true;
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + columnToBeChanged);
        }

        // for uniformity
        if (isInt) {
            int newIntValue = Integer.parseInt(newValue);
            String query = "UPDATE " + TABLE_NAME + " SET " + keyPhrase + " = '" + newIntValue + "' WHERE " + COLUMN_ID + " = '" + id +
                    "' AND " + COLUMN_TITLE + " = '" + name + "'";
            db.execSQL(query);
        } else {
            String query = "UPDATE " + TABLE_NAME + " SET " + keyPhrase + " = '" + newValue + "' WHERE " + COLUMN_ID + " = '" + id +
                    "' AND " + COLUMN_TITLE + " = '" + name + "'";
            db.execSQL(query);
        }


    }
}
