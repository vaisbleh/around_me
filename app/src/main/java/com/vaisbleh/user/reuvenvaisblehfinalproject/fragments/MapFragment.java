package com.vaisbleh.user.reuvenvaisblehfinalproject.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.vaisbleh.user.reuvenvaisblehfinalproject.R;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Constants;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Place;
import com.vaisbleh.user.reuvenvaisblehfinalproject.services_asynctasks.GetPlaceTask;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapFragment extends Fragment implements OnMapReadyCallback, GetPlaceTask.OnWebResultListener {

    private GoogleMap mMap;
    private String id;
    private SharedPreferences sp;
    private boolean isTablet, isFirst;
    private GoToDetailsListener listener;
    private Place currentPlace;



    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (GoToDetailsListener) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        isTablet = sp.getBoolean(Constants.IS_TABLET, false);
        if(savedInstanceState == null){
            isFirst = true;
        }else {
            isFirst = false;
            currentPlace = savedInstanceState.getParcelable("place");

        }
        return v;
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng jerusalem = new LatLng(31.778301, 35.2301663);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(jerusalem, 10));

        if(isTablet == false && isFirst == true) { // if it phone - get place details
            new GetPlaceTask(this).execute(id);
        }
        if(isFirst == false){
            if(currentPlace != null) {
                show(currentPlace);
            }
        }
    }

    public void showPlace(String id){ //get id from Activity
        this.id = id;

    }

    @Override
    public void showSelectedPlace(Place place) { //get place from task
        currentPlace = place;
        show(place);
        listener.getDetails(place);
    }

    public void show(Place place){
        mMap.clear();
        int radius = Integer.parseInt(sp.getString(Constants.PREF_RADIUS_KEY, "500"));
        int smallRad = 6 + (10 - (2000/radius));
        int bigRad = (int) (radius * 0.9);
        int zoom = 17 - (radius/500);
        if(zoom < 14){
            zoom = 14;
        }
        double myLat = (double) sp.getFloat(Constants.SP_LAT, 0);
        double myLon = (double) sp.getFloat(Constants.SP_LON, 0);
        LatLng myPlace = new LatLng(myLat, myLon);
        LatLng selectedPlace = new LatLng(place.getLat(), place.getLon());
        mMap.addCircle(new CircleOptions().center(myPlace).strokeColor(Color.RED).strokeWidth(0.1f).radius(smallRad).fillColor(Color.RED));
        mMap.addCircle(new CircleOptions().center(myPlace).radius(bigRad).strokeWidth(0.1f). strokeColor(Color.argb(25, 255, 50, 50)).fillColor(Color.argb(50, 255, 50, 50)));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedPlace, zoom));
        mMap.addMarker(new MarkerOptions().position(selectedPlace).title(place.getName()));
        mMap.addMarker(new MarkerOptions().position(myPlace).title("You are here"));
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("place", currentPlace);

    }

    public interface GoToDetailsListener {
        void getDetails(Place place);
    }



}
