package com.vaisbleh.user.reuvenvaisblehfinalproject.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import com.vaisbleh.user.reuvenvaisblehfinalproject.R;
import com.vaisbleh.user.reuvenvaisblehfinalproject.controller.MapFragmentAdapter;
import com.vaisbleh.user.reuvenvaisblehfinalproject.fragments.DetailsFragment;
import com.vaisbleh.user.reuvenvaisblehfinalproject.fragments.MapFragment;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Constants;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Place;
import com.vaisbleh.user.reuvenvaisblehfinalproject.services_asynctasks.MyBatteryReceiver;

public class MapActivity extends AppCompatActivity implements MyBatteryReceiver.OnBatteryChargingListener, MapFragment.GoToDetailsListener, View.OnClickListener,
                                                                DialogInterface.OnClickListener {
//Activity only for phone (not tablet);

    private MapFragmentAdapter mapFragmentAdapter;
    private MapFragment mapFragment;
    private DetailsFragment detailsFragment;
    private View searchBtn;
    private AlertDialog dialogDeleteAllFavorites;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Toolbar toolbar = (Toolbar) findViewById(R.id.myTool);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);


        mapFragmentAdapter = new MapFragmentAdapter(getSupportFragmentManager());
            ViewPager mapPager = (ViewPager) findViewById(R.id.mapPager);
            mapPager.setAdapter(mapFragmentAdapter);

            TabLayout mapTabs = (TabLayout) findViewById(R.id.mapTabs);
            mapTabs.setupWithViewPager(mapPager);



        mapFragment = (MapFragment) mapFragmentAdapter.getItem(0);
        detailsFragment = (DetailsFragment) mapFragmentAdapter.getItem(1);

        String id =  getIntent().getStringExtra(Constants.ID_EXTRA);
        mapFragment.showPlace(id);

        searchBtn = findViewById(R.id.btnSearch);
        searchBtn.setOnClickListener(this);



    }

    @Override // notification of changing charging status
    public void batteryCharging(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    @Override//transfer place to details
    public void getDetails(Place place) {
    detailsFragment.setPlace(place);
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
                    break;
            }


        }
    }
    //****************************************************************************

}
