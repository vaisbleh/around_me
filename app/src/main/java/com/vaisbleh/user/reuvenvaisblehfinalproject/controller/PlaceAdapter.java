package com.vaisbleh.user.reuvenvaisblehfinalproject.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.vaisbleh.user.reuvenvaisblehfinalproject.R;
import com.vaisbleh.user.reuvenvaisblehfinalproject.activities.MapActivity;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Constants;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Place;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by User on 18-Sep-17.
 */

public class PlaceAdapter extends RecyclerView.Adapter <PlaceAdapter.PlaceHolder>{


    private Context context;
    private ArrayList<Place> places = new ArrayList<>();
    private int fromFragment;
    private int position;
    private SendToMainListener listener;



    public PlaceAdapter(Context context, ArrayList<Place> places, int fromFragment) {
        this.context = context;
        this.places = places;
        this.fromFragment = fromFragment;
        listener = (SendToMainListener) context;

    }

    public ArrayList<Place> getAdapterArray(){
        return places;
    }

    public void refresh (ArrayList<Place> newPlaces){
        places.clear();
        places.addAll(newPlaces);
        notifyDataSetChanged();
    }

    public void clear(){
        places.clear();
        notifyDataSetChanged();
    }

    public void remove(Place place){
        places.remove(place);
        notifyItemRemoved(position);
    }

    @Override
    public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PlaceHolder(LayoutInflater.from(context).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PlaceHolder holder, int position) {
            holder.bind(places.get(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }


    //*************************************************************
    public class PlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener{

        private TextView textName, textAddress, textDistance, textType;
        private ImageView iconView;
        private ImageButton btnMenu;
        private Place currentPlace;
        private CardView card;
        private SharedPreferences sp;
        boolean isTablet;


        public PlaceHolder(View itemView) {
            super(itemView);

            textName =  itemView.findViewById(R.id.textName);
            textAddress =  itemView.findViewById(R.id.textAddress);
            textDistance =  itemView.findViewById(R.id.textDistance);
            textType =  itemView.findViewById(R.id.textType);
            iconView = itemView.findViewById(R.id.iconView);
            btnMenu = itemView.findViewById(R.id.btnMenu);
            card = itemView.findViewById(R.id.card);
            btnMenu.setOnClickListener(this);
            card.setOnClickListener(this);
            textAddress.setOnClickListener(this);
            sp = PreferenceManager.getDefaultSharedPreferences(context);
            isTablet = sp.getBoolean(Constants.IS_TABLET, false);
        }

        public void bind(Place place){

            currentPlace = place;
            //get icon from storage
            FileInputStream inputStream = null;
            Bitmap icon = null;
            try {
                File filePath = context.getFileStreamPath(place.getIconLink());
                inputStream = new FileInputStream(filePath);
                icon = BitmapFactory.decodeStream(inputStream);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }finally {
                if(inputStream != null){
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            textName.setText(place.getName());
            textAddress.setText(place.getAddress());
            textType.setText(place.getType());
            iconView.setImageBitmap(icon);
            double placeLon = place.getLon();
            double placeLat = place.getLat();
            String units = sp.getString(Constants.PREF_UNITS_KEY, Constants.METERS);
            double myLon = sp.getFloat(Constants.SP_LON, 0);
            double myLat = sp.getFloat(Constants.SP_LAT, 0);
            int distanceInMeter = calcDistance(myLon, myLat, placeLon, placeLat); //calc distance between user and selected place in meters
            //check if user want to get distance in yards and calc
            if (units.equals(Constants.YARDS)){
                int distanceInYard = (int) (distanceInMeter * 1.09361);
                textDistance.setText(distanceInYard + " " + units);
           } else {
                textDistance.setText(distanceInMeter + " " + units);
          }


        }

        //calc distance between two places
        public int calcDistance(double myLon, double myLat, double placeLon, double placeLat){

            Location loc1 = new Location("");
            loc1.setLatitude(myLat);
            loc1.setLongitude(myLon);

            Location loc2 = new Location("");
            loc2.setLatitude(placeLat);
            loc2.setLongitude(placeLon);

            return (int) loc1.distanceTo(loc2);


        }

        @Override
        public void onClick(View view) {

                    switch (view.getId()){
                        case  R.id.btnMenu: //menu for each place search or favorites
                            if(fromFragment == Constants.FROM_FRAGMENT_SEARCH) {
                                PopupMenu popupMenuSearch = new PopupMenu(context, btnMenu);
                                popupMenuSearch.getMenuInflater().inflate(R.menu.pop_up_search_menu, popupMenuSearch.getMenu());
                                popupMenuSearch.setOnMenuItemClickListener(this);
                                popupMenuSearch.show();
                            }else {
                                PopupMenu popupMenuSearch = new PopupMenu(context, btnMenu);
                                popupMenuSearch.getMenuInflater().inflate(R.menu.pop_up_favorites_menu, popupMenuSearch.getMenu());
                                popupMenuSearch.setOnMenuItemClickListener(this);
                                popupMenuSearch.show();
                            }
                            break;

                        case R.id.card : //click on all place
                        case R.id.textAddress:
                            if(isTablet == false){ // if it phone - open map activity with place id
                                Intent mapIntent = new Intent(context, MapActivity.class);
                                mapIntent.putExtra(Constants.ID_EXTRA, currentPlace.getId().toString());
                               context.startActivity(mapIntent);

                           }else {//if it tablet - transfer place id to mainActivity
                                listener.goToMap(currentPlace.getId().toString());
                            }

                            break;
                    }

        }


        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if(fromFragment == Constants.FROM_FRAGMENT_SEARCH){
            switch (item.getItemId()) {
                case R.id.addToFavorites:
                    boolean isFavorite = false; //check if place already in favorites
                    Cursor cursor = context.getContentResolver().query(Constants.CONTENT_URI_FAVORITES, new  String[]{Constants.COL_ID},null , null, null);
                    while (cursor.moveToNext()){
                     String id = cursor.getString(cursor.getColumnIndex(Constants.COL_ID));
                        if(id.equals(currentPlace.getId().toString())){
                            isFavorite = true;
                        }
                    }
                    if(!isFavorite) { //add to favorites in db
                        ContentValues values = new ContentValues();
                        values.put(Constants.COL_ID, currentPlace.getId());
                        values.put(Constants.COL_NAME, currentPlace.getName());
                        values.put(Constants.COL_ADDRESS, currentPlace.getAddress());
                        values.put(Constants.COL_LAT, currentPlace.getLat());
                        values.put(Constants.COL_LON, currentPlace.getLon());
                        values.put(Constants.COL_ICON, currentPlace.getIconLink());
                        values.put(Constants.COL_TYPE, currentPlace.getType());
                        context.getContentResolver().insert(Constants.CONTENT_URI_FAVORITES, values);
                        Toast.makeText(context, "Place added to favorites", Toast.LENGTH_SHORT).show();
                        listener.refreshFavorites(); // refresh favorites fragment

                        return true;
                    }else {
                        Toast.makeText(context, "place already in favorites", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.share://share place

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "I love " + currentPlace.getName() + "\naddress: " + currentPlace.getAddress());
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);

                    break;
            }

            }else { //for favorites fragment
                switch (item.getItemId()){
                    case R.id.deleteFromFavorites: //delete place from favorites
                        context.getContentResolver().delete(Constants.CONTENT_URI_FAVORITES, Constants.COL_ID + "= '" + currentPlace.getId().toString() + "'", null);
                        position = getAdapterPosition();
                        remove(currentPlace);
                        break;

                    case R.id.share: //share place

                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "I love " + currentPlace.getName() + "\naddress: " + currentPlace.getAddress());
                        sendIntent.setType("text/plain");
                        context.startActivity(sendIntent);

                        break;

                }
            }
            return true;
        }
    }

    public interface SendToMainListener{
        void refreshFavorites();
        void goToMap(String id); //for tablet only
    }
}
