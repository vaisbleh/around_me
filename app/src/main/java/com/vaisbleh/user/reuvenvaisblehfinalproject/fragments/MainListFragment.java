package com.vaisbleh.user.reuvenvaisblehfinalproject.fragments;


import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.vaisbleh.user.reuvenvaisblehfinalproject.R;
import com.vaisbleh.user.reuvenvaisblehfinalproject.controller.PlaceAdapter;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Constants;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Place;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainListFragment extends Fragment implements View.OnClickListener{


    public MainListFragment() {
        // Required empty public constructor
    }

    private PlaceAdapter adapter;
    private Spinner searchSpinner;
    private SearchReceiver searchReceiver;
    private RecyclerView listPlaces;
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
        View v = inflater.inflate(R.layout.fragment_main_list, container, false);
        searchSpinner = v.findViewById(R.id.searchSpinner);
        String selectBy = searchSpinner.getSelectedItem().toString();



        listPlaces = v.findViewById(R.id.listPlaces);
        listPlaces.setLayoutManager(new LinearLayoutManager(getContext()));

        if(savedInstanceState == null) {
            adapter = new PlaceAdapter(context, getAdapterArray(selectBy), Constants.FROM_FRAGMENT_SEARCH);
        }else {
            ArrayList<Place>places = savedInstanceState.getParcelableArrayList("places");
            adapter = new PlaceAdapter(context, places, Constants.FROM_FRAGMENT_SEARCH);

        }

        listPlaces.setAdapter(adapter);

        searchReceiver = new SearchReceiver();
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(searchReceiver, new IntentFilter(Constants.ACTION_SEARCH));


        v.findViewById(R.id.btnSelectBy).setOnClickListener(this);

        return v;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btnSelectBy:

                String selectBy = searchSpinner.getSelectedItem().toString();
                adapter.refresh(getAdapterArray(selectBy));

                break;
        }
    }



    private  ArrayList <Place> getAdapterArray(String selectBy){ //get Array list for adapter
        ArrayList<Place> places= new ArrayList<>();
        Cursor cursor;
        if(selectBy.equals("all")){ // get all places
            cursor = contentResolver.query(Constants.CONTENT_URI_SEARCH_RESULTS, null, null, null, null);
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

            cursor = contentResolver.query(Constants.CONTENT_URI_SEARCH_RESULTS, null, Constants.COL_TYPE + "= '" + selectBy +"' ", null, null);
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



    public class SearchReceiver extends BroadcastReceiver{ //when search results are received

        @Override
        public void onReceive(Context context, Intent intent) {

           String status =  intent.getStringExtra(Constants.EXTRA_STATUS);
            if(status.equals("connected")){
                adapter.refresh(getAdapterArray("all"));
            }else if(status.equals("no results")) {
                adapter.refresh(getAdapterArray("all"));
                Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
            }else if(status.equals("no internet connection")){
                Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
            }

            searchSpinner.setSelection(0);
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(searchReceiver);
    }



    public void refreshAdapter () {

        adapter.refresh(getAdapterArray(searchSpinner.getSelectedItem().toString()));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Place>places = adapter.getAdapterArray();
        outState.putParcelableArrayList("places", places);
    }
}
