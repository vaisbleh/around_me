package com.vaisbleh.user.reuvenvaisblehfinalproject.db;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class PlacesProvider extends ContentProvider {

    private PlacesDBHelper helper;
    public PlacesProvider() {
    }

    @Override
    public boolean onCreate() {
        helper = new PlacesDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db  = helper.getReadableDatabase();
        return db.query(uri.getLastPathSegment(), projection, selection, selectionArgs, null, null, sortOrder);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count =  db.delete(uri.getLastPathSegment(), selection, selectionArgs);
        db.close();
        return count;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = helper.getWritableDatabase();
        long rowId = db.insert(uri.getLastPathSegment(), null, values);
        db.close();
        return Uri.withAppendedPath(uri, rowId+"");
    }


    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = helper.getWritableDatabase();
        int count = db.update(uri.getLastPathSegment(), values, selection, selectionArgs);
        db.close();
        return count;
    }

    @Override
    public String getType(Uri uri) {

        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
