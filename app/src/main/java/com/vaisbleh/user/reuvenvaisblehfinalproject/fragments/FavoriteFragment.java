package com.vaisbleh.user.reuvenvaisblehfinalproject.fragments;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import com.vaisbleh.user.reuvenvaisblehfinalproject.R;
import com.vaisbleh.user.reuvenvaisblehfinalproject.controller.PlaceAdapter;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Constants;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Place;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements View.OnClickListener {


    public FavoriteFragment() {
        // Required empty public constructor
    }

    private PlaceAdapter adapter;
    private Spinner favoritesSpinner;
    private RecyclerView favoritesList;
    private Context context;
    private ContentResolver contentResolver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        contentResolver = context.getContentResolver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorite, container, false);
        favoritesSpinner = v.findViewById(R.id.favoritesSpinner);
        String selectBy = favoritesSpinner.getSelectedItem().toString();
        favoritesList = v.findViewById(R.id.favoritesList);
        favoritesList.setLayoutManager(new LinearLayoutManager(getContext()));

        if(savedInstanceState == null) {
            adapter = new PlaceAdapter(context, getAdapterArray(selectBy), Constants.FROM_FRAGMENT_FAVORITES);
        }else {
            ArrayList<Place>places = savedInstanceState.getParcelableArrayList("places");
            adapter = new PlaceAdapter(context, places, Constants.FROM_FRAGMENT_FAVORITES);
        }

        favoritesList.setAdapter(adapter);


        v.findViewById(R.id.btnFavoritesSelectBy).setOnClickListener(this);




        return v;
    }


    private  ArrayList <Place> getAdapterArray(String selectBy){ // get arrayList from DB for adapter
        ArrayList<Place> places= new ArrayList<>();
        Cursor cursor;
        if(selectBy.equals("all")){ // get all places
            cursor = contentResolver.query(Constants.CONTENT_URI_FAVORITES, null, null, null, null);
            while (cursor.moveToNext()){
                String id = cursor.getString(cursor.getColumnIndex(Constants.COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(Constants.COL_NAME));
                String address = cursor.getString(cursor.getColumnIndex(Constants.COL_ADDRESS));
                double lat = cursor.getDouble(cursor.getColumnIndex(Constants.COL_LAT));
                double lon = cursor.getDouble(cursor.getColumnIndex(Constants.COL_LON));
                String iconLink = cursor.getString(cursor.getColumnIndex(Constants.COL_ICON));
                String type = cursor.getString(cursor.getColumnIndex(Constants.COL_TYPE));
                places.add(new Place(id, name, address, iconLink, lon, lat,type));
            }
            cursor.close();
            return places;
        }else { //get places by type

            cursor = contentResolver.query(Constants.CONTENT_URI_FAVORITES, null, Constants.COL_TYPE + "= '" + selectBy +"' ", null, null);
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(Constants.COL_ID));
                String name = cursor.getString(cursor.getColumnIndex(Constants.COL_NAME));
                String address = cursor.getString(cursor.getColumnIndex(Constants.COL_ADDRESS));
                double lat = cursor.getDouble(cursor.getColumnIndex(Constants.COL_LAT));
                double lon = cursor.getDouble(cursor.getColumnIndex(Constants.COL_LON));
                String iconLink = cursor.getString(cursor.getColumnIndex(Constants.COL_ICON));
                String type = cursor.getString(cursor.getColumnIndex(Constants.COL_TYPE));
                places.add(new Place(id, name, address, iconLink, lon, lat, type));
            }
            cursor.close();
            return places;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnFavoritesSelectBy:

                String selectBy = favoritesSpinner.getSelectedItem().toString();
                adapter.refresh(getAdapterArray(selectBy));

                break;
        }
    }

    public void refreshAdapter () {

        adapter.refresh(getAdapterArray(favoritesSpinner.getSelectedItem().toString()));
    }

    public void clearAdapter(){
        adapter.clear();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Place> places = adapter.getAdapterArray();
        outState.putParcelableArrayList("places", places);
    }
}
