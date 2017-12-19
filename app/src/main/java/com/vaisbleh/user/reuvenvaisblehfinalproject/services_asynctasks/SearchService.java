package com.vaisbleh.user.reuvenvaisblehfinalproject.services_asynctasks;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

//get search results

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 *
 */
public class SearchService extends IntentService {

    public SearchService() {
        super("SearchService");
    }

    private String extraStatus = "";


    @Override
    protected void onHandleIntent(Intent intent) {

        getContentResolver().delete(Constants.CONTENT_URI_SEARCH_RESULTS, null, null);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String radius = sp.getString(Constants.PREF_RADIUS_KEY, "500");
        double myLon = sp.getFloat(Constants.SP_LON, 0);
        double myLat = sp.getFloat(Constants.SP_LAT, 0);
        String keyword = intent.getStringExtra(Constants.KEYWORD_EXTRA);
        if(keyword == null){
            keyword = "";
        }
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        String parameters = "location=" + myLat + "," + myLon + "&radius=" + radius + "&keyword=" + keyword;
        String key = "&key=AIzaSyBFUdC0MhBIl2wKkQ0O4DPSf4cgQehJ2p8";


        HttpsURLConnection jsonConnection = null;
        HttpsURLConnection iconConnection = null;


        try {
            String fullUrl = url + parameters + key;
            String nextPageToken  ;

            do {

                nextPageToken =  "";
                BufferedReader reader = null;
                StringBuilder builder = new StringBuilder();

                URL searchUrl = new URL(fullUrl);
                jsonConnection = (HttpsURLConnection) searchUrl.openConnection();
                if (jsonConnection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                    extraStatus = "no internet connection";
                    Intent broadcastIntent = new Intent(Constants.ACTION_SEARCH);
                    broadcastIntent.putExtra(Constants.EXTRA_STATUS, extraStatus);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
                    return;
                }

                reader = new BufferedReader(new InputStreamReader(jsonConnection.getInputStream()));
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }

                JSONObject rootObj = new JSONObject(builder.toString());
                String status = rootObj.getString("status");
                if (status.equals("ZERO_RESULTS")) {
                    extraStatus = "no results";
                    Intent broadcastIntent = new Intent(Constants.ACTION_SEARCH);
                    broadcastIntent.putExtra(Constants.EXTRA_STATUS, extraStatus);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

                    return;
                } else {
                    JSONArray results = rootObj.getJSONArray("results");
                    for (int i = 0; i < results.length(); i++) {
                        JSONObject result = results.getJSONObject(i);
                        String name = result.getString("name");
                        String id = result.getString("place_id");
                        String address = result.getString("vicinity");
                        String iconLink = result.getString("icon");
                        JSONObject geometry = result.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        double lat = location.getDouble("lat");
                        double lon = location.getDouble("lng");
                        JSONArray types = result.getJSONArray("types");
                        String type = types.getString(0);


                        Bitmap icon = null;

                        URL iconUrl = new URL(iconLink);
                        iconConnection = (HttpsURLConnection) iconUrl.openConnection();

                        if (iconConnection.getResponseCode() != HttpsURLConnection.HTTP_OK) {
                            return;
                        }
                        icon = BitmapFactory.decodeStream(iconConnection.getInputStream());

                        String iconName = "myIcon" + System.currentTimeMillis() + ".png";//name of icon
                        FileOutputStream outputStream = null; //save icon to storage
                        extraStatus = "connected";

                        try {
                            outputStream = this.openFileOutput(iconName, this.MODE_PRIVATE);
                            icon.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            if (outputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                        }

                            ContentValues values = new ContentValues();
                            values.put(Constants.COL_ID, id);
                            values.put(Constants.COL_NAME, name);
                            values.put(Constants.COL_ADDRESS, address);
                            values.put(Constants.COL_LAT, lat);
                            values.put(Constants.COL_LON, lon);
                            values.put(Constants.COL_ICON, iconName);
                            values.put(Constants.COL_TYPE, type);

                            getContentResolver().insert(Constants.CONTENT_URI_SEARCH_RESULTS, values); //insert place to DB



                    }


                }

                if(rootObj.has("next_page_token")){
                    nextPageToken = rootObj.getString("next_page_token");
                }
                    fullUrl = url + "pagetoken=" + nextPageToken + key;
                Intent broadcastIntent = new Intent(Constants.ACTION_SEARCH);
                broadcastIntent.putExtra(Constants.EXTRA_STATUS, extraStatus);
                LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);

            }while (!nextPageToken.isEmpty());

        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {

            if(jsonConnection != null){
                jsonConnection.disconnect();
            }
            if(iconConnection != null){
                iconConnection.disconnect();
            }

        }


    }

   
}
