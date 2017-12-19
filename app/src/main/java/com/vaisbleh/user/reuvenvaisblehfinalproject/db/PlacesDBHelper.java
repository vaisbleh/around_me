package com.vaisbleh.user.reuvenvaisblehfinalproject.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Constants;

/**
 * Created by User on 18-Sep-17.
 */

public class PlacesDBHelper extends SQLiteOpenHelper {

    public PlacesDBHelper(Context context) {
        super(context, Constants.PLACES_DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(String.format( "CREATE TABLE %s ( %s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s REAL, %s REAL, %s TEXT, %s TEXT)",
                Constants.TABLE_NAME_SEARCH_RESULTS, Constants.COL_ID, Constants.COL_NAME, Constants.COL_ADDRESS, Constants.COL_LAT, Constants.COL_LON,
                Constants.COL_ICON, Constants.COL_TYPE));

        sqLiteDatabase.execSQL(String.format( "CREATE TABLE %s ( %s TEXT PRIMARY KEY, %s TEXT, %s TEXT, %s REAL, %s REAL, %s TEXT, %s TEXT)",
                Constants.TABLE_NAME_FAVORITES, Constants.COL_ID, Constants.COL_NAME, Constants.COL_ADDRESS, Constants.COL_LAT, Constants.COL_LON,
                Constants.COL_ICON, Constants.COL_TYPE));


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
