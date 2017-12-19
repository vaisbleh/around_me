package com.vaisbleh.user.reuvenvaisblehfinalproject.model;

import android.net.Uri;

//All constants of project

/**
 * Created by User on 18-Sep-17.
 */

public class Constants {

    //settings
    public final static String PREF_UNITS_KEY = "units";
    public final static String PREF_RADIUS_KEY = "radius";
    public final static String METERS = "Meters";
    public final static String YARDS = "Yards";

   //DB
    public final static String PLACES_DB_NAME = "places.db";
    public final static String TABLE_NAME_SEARCH_RESULTS = "searchResults";
    public final static String TABLE_NAME_FAVORITES = "favorites";
    public final static String COL_ID = "id";
    public final static String COL_NAME = "name";
    public final static String COL_ADDRESS = "address";
    public final static String COL_LON = "lon";
    public final static String COL_LAT = "lat";
    public final static String COL_ICON = "icon";
    public final static String COL_TYPE = "type";

    //share preferences
    public final static String SP_LAT = "lat";
    public final static String SP_LON = "lon";
    public final static String IS_TABLET = "isTablet";

    //service
    public final static String EXTRA_STATUS = "extraStatus";
    public final static String AUTHORITY = "com.vaisbleh.user.reuvenvaisblehfinalproject.places";
    public static final Uri CONTENT_URI_SEARCH_RESULTS = Uri.parse("content://" + AUTHORITY + "/" +  TABLE_NAME_SEARCH_RESULTS);
    public static final Uri CONTENT_URI_FAVORITES = Uri.parse("content://" + AUTHORITY + "/" +  TABLE_NAME_FAVORITES);
    public static final int FROM_FRAGMENT_SEARCH = 1;
    public static final int FROM_FRAGMENT_FAVORITES = 2;
    public static final String KEYWORD_EXTRA = "keyword";
    public static final String ACTION_SEARCH = "com.vaisbleh.user.reuvenvaisblehfinalproject.services_asynctasks.ACTION_SEARCH";


    //fragments
    public final static String MAIN_LIST_FRAGMENT_STATE = "mainListFrag";
    public final static String FAVORITES_FRAGMENT_STATE = "favoritesFrag";
    public final static String MAP_FRAGMENT_STATE = "mapFrag";
    public final static String DETAILS_FRAGMENT_STATE = "detailFrag";

   //adapter
    public static final String ID_EXTRA = "idExtra";













}
