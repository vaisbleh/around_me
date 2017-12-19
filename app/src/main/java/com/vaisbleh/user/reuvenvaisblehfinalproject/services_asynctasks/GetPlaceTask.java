package com.vaisbleh.user.reuvenvaisblehfinalproject.services_asynctasks;

import android.os.AsyncTask;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Place;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

//get place details

/**
 * Created by jbt on 24/09/2017.
 */

public class GetPlaceTask extends AsyncTask<String, Void, Place> {

    private OnWebResultListener listener;

    public GetPlaceTask(OnWebResultListener listener) {
        this.listener = listener;
    }

    @Override
    protected Place doInBackground(String... strings) {

        HttpsURLConnection connection = null;
        BufferedReader reader;
        StringBuilder builder = new StringBuilder();

        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/place/details/json?placeid=" + strings[0] + "&key=AIzaSyBFUdC0MhBIl2wKkQ0O4DPSf4cgQehJ2p8");
            connection = (HttpsURLConnection) url.openConnection();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = reader.readLine();
            while (line != null){
                builder.append(line);
                line = reader.readLine();
            }
            JSONObject root = new JSONObject(builder.toString());
            String status = root.getString("status");
            if(status.equals("OK")){
                JSONObject result = root.getJSONObject("result");
                String name = result.getString("name");
                String address = result.getString("formatted_address");
                String phone ="";
                if(result.has("international_phone_number")) {
                    phone = result.getString("international_phone_number");
                }
                String picReference = "";
                if(result.has("photos")) {
                    JSONArray photos = result.getJSONArray("photos");
                    JSONObject photo = photos.getJSONObject(0);
                    picReference = photo.getString("photo_reference");
                    }
                JSONObject geometry = result.getJSONObject("geometry");
                JSONObject location = geometry.getJSONObject("location");
                double lat = location.getDouble("lat");
                double lon = location.getDouble("lng");

                return new Place(strings[0], name, address, picReference, phone,  lon, lat);


            }else {
                return null;
            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }


        return null;
    }

    @Override
    protected void onPostExecute(Place place) {
        super.onPostExecute(place);
        listener.showSelectedPlace(place);
    }

    public interface OnWebResultListener{
        void showSelectedPlace(Place place);
    }
}
