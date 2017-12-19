package com.vaisbleh.user.reuvenvaisblehfinalproject.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.vaisbleh.user.reuvenvaisblehfinalproject.R;
import com.vaisbleh.user.reuvenvaisblehfinalproject.controller.MapFragmentAdapter;
import com.vaisbleh.user.reuvenvaisblehfinalproject.controller.MyFragmentAdapter;
import com.vaisbleh.user.reuvenvaisblehfinalproject.controller.PlaceAdapter;
import com.vaisbleh.user.reuvenvaisblehfinalproject.fragments.DetailsFragment;
import com.vaisbleh.user.reuvenvaisblehfinalproject.fragments.FavoriteFragment;
import com.vaisbleh.user.reuvenvaisblehfinalproject.fragments.MainListFragment;
import com.vaisbleh.user.reuvenvaisblehfinalproject.fragments.MapFragment;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Constants;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Place;
import com.vaisbleh.user.reuvenvaisblehfinalproject.services_asynctasks.GetPlaceTask;
import com.vaisbleh.user.reuvenvaisblehfinalproject.services_asynctasks.MyBatteryReceiver;


public class MainActivity extends AppCompatActivity implements LocationListener,
        DialogInterface.OnClickListener, PlaceAdapter.SendToMainListener, MyBatteryReceiver.OnBatteryChargingListener, GetPlaceTask.OnWebResultListener, MapFragment.GoToDetailsListener, View.OnClickListener {

    private boolean isTablet;
    private boolean gotLocation = false;
    private LocationManager locationManager;
    private int count = 0;
    private String[] providerNames;
    private RelativeLayout searchingLayout;
    private CoordinatorLayout mainLayout;
    private SharedPreferences sp;
    private AlertDialog dialogDeleteAllFavorites;
    private MyFragmentAdapter myfragmentAdapter;
    private MapFragmentAdapter mapFragmentAdapter;
    private MyBatteryReceiver receiver;
    private MapFragment mapFragment;
    private FavoriteFragment favoriteFragment;
    private DetailsFragment detailsFragment;
    private MainListFragment mainListFragment;
    private View searchBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myTool);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        //checking status of charging
        receiver = new MyBatteryReceiver(this);
        IntentFilter filter = new IntentFilter(Intent.ACTION_POWER_CONNECTED);
        filter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        registerReceiver(receiver, filter);



                myfragmentAdapter = new MyFragmentAdapter(getSupportFragmentManager());
                ViewPager pager = (ViewPager) findViewById(R.id.pager);
                pager.setAdapter(myfragmentAdapter);
                pager.setCurrentItem(0);

                TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
                tabLayout.setupWithViewPager(pager);

                searchBtn = findViewById(R.id.btnSearch);
                searchBtn.setOnClickListener(this);



                if (findViewById(R.id.conteinerTablet) != null) { // loading if use tablet
                    isTablet = true;
                    mapFragmentAdapter = new MapFragmentAdapter(getSupportFragmentManager());
                    ViewPager mapPager = (ViewPager) findViewById(R.id.mapPager);
                    mapPager.setAdapter(mapFragmentAdapter);

                    TabLayout mapTabs = (TabLayout) findViewById(R.id.mapTabs);
                    mapTabs.setupWithViewPager(mapPager);


                }
                sp.edit()
                        .putBoolean(Constants.IS_TABLET, isTablet)
                        .apply();

             if(savedInstanceState == null) { // Saving References to fragments first time

                 favoriteFragment = (FavoriteFragment) myfragmentAdapter.getItem(1);
                 mainListFragment = (MainListFragment) myfragmentAdapter.getItem(0);
                 if(isTablet){
                     mapFragment = (MapFragment) mapFragmentAdapter.getItem(0);
                     detailsFragment = (DetailsFragment) mapFragmentAdapter.getItem(1);
                 }

            }else { //after turning
                FragmentManager fm = getSupportFragmentManager();
                    favoriteFragment = (FavoriteFragment) fm.getFragment(savedInstanceState, Constants.FAVORITES_FRAGMENT_STATE);
                    mainListFragment = (MainListFragment) fm.getFragment(savedInstanceState,Constants.MAIN_LIST_FRAGMENT_STATE);
                 if(isTablet){
                    mapFragment = (MapFragment) fm.getFragment(savedInstanceState, Constants.MAP_FRAGMENT_STATE);
                    detailsFragment = (DetailsFragment) fm.getFragment(savedInstanceState, Constants.DETAILS_FRAGMENT_STATE);
                }
            }





        searchingLayout = (RelativeLayout) findViewById(R.id.searchingLayout);
        mainLayout = (CoordinatorLayout) findViewById(R.id.mainLayout);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //array of providers
        providerNames = new String[]{locationManager.GPS_PROVIDER, locationManager.NETWORK_PROVIDER, locationManager.PASSIVE_PROVIDER};
        if(savedInstanceState == null) {
            searchingLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.INVISIBLE);
            getLocation();
        }else {
            searchingLayout.setVisibility(View.INVISIBLE);
            mainLayout.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch: //find places near user location
                Intent searchIntent = new Intent(this, SearchActivity.class);
                ActivityOptionsCompat transitionOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, searchBtn, "search");
                startActivity(searchIntent, transitionOptions.toBundle());

                break;
        }
    }


    // option menu**************************************
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_manu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.settings:
                startActivity(new Intent(this, PrefActivity.class));
                break;

            case R.id.deleteFavorites:

                dialogDeleteAllFavorites = new AlertDialog.Builder(this)
                        .setTitle("Delete all favorites?")
                        .setMessage("Are you sure")
                        .setPositiveButton("YES", this)
                        .setNegativeButton("NO", this)
                        .create();
                dialogDeleteAllFavorites.show();


                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if(dialogInterface == dialogDeleteAllFavorites){
            switch (i){
                case DialogInterface.BUTTON_POSITIVE:
                    getContentResolver().delete(Constants.CONTENT_URI_FAVORITES, null, null);
                    Toast.makeText(this, "favorites deleted", Toast.LENGTH_SHORT).show();
                    favoriteFragment.clearAdapter();
                    break;
            }


        }
    }
    //****************************************************************************



    @Override
    protected void onRestart() { // refresh fragments after return from preference or other activity
        super.onRestart();
        getLocation();
        mainListFragment.refreshAdapter();
        favoriteFragment.refreshAdapter();
    }



    //***************************  methods for to get the best location
    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }
        locationManager.removeUpdates(this);
        locationManager.requestLocationUpdates(providerNames[count], 5000, 200, this);

        changeProvider(); // check if location got, if not - change provider.
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
            getLocation();
        } else {
            Toast.makeText(this, "Can't show your location on the map...", Toast.LENGTH_SHORT).show();
        }

    }
        // if location not got, change provider.
    private void changeProvider(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(gotLocation == false){
                    if(count < 2) {
                        count++;
                    }
                    getLocation();

                }
            }
        }, 7000L);

    }

    @Override
    public void onLocationChanged(Location location) {

    gotLocation = true;
        float myLat = (float) location.getLatitude();
        float myLon = (float) location.getLongitude();
        sp.edit()
                .putFloat(Constants.SP_LAT, myLat)
                .putFloat(Constants.SP_LON, myLon)
                .apply();

        if(count != 0){
            searchNewProvider(); // if location provider is not GPS, look for GPS
        }
        mainListFragment.refreshAdapter();
        favoriteFragment.refreshAdapter();
        searchingLayout.setVisibility(View.INVISIBLE);
        mainLayout.setVisibility(View.VISIBLE);



    }
    // if location provider is not GPS, look for GPS
    public void searchNewProvider(){
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                count = 0;
                getLocation();
            }
        }, 300000L);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}
    @Override
    public void onProviderEnabled(String s) {}
    @Override
    public void onProviderDisabled(String s) {
        gotLocation = false;
        count = 0;
        getLocation();
    }
//***************************************

    // refresh favorites after add new item to favorites
    @Override
    public void refreshFavorites() {
        favoriteFragment.refreshAdapter();
    }

    @Override // get selected place and transfer it to map and detail fragments. only for tablet
    public void goToMap(String id) {
        new GetPlaceTask(this).execute(id);
    }

    @Override // show received place to map and details
                       public void showSelectedPlace(Place place) {

        mapFragment.showSelectedPlace(place);
    }


    @Override //transfer place to details. only for tablet
    public void getDetails(Place place) {
        detailsFragment.setPlace(place);
    }


    // notification of changing charging status
    @Override
    public void batteryCharging(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
        locationManager.removeUpdates(this);
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fm = getSupportFragmentManager();
        fm.putFragment(outState, Constants.FAVORITES_FRAGMENT_STATE, favoriteFragment);
        fm.putFragment(outState, Constants.MAIN_LIST_FRAGMENT_STATE, mainListFragment);
        if (isTablet) {
            fm.putFragment(outState, Constants.DETAILS_FRAGMENT_STATE, detailsFragment);
            fm.putFragment(outState, Constants.MAP_FRAGMENT_STATE, mapFragment);
        }
    }


}
