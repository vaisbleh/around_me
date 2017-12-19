package com.vaisbleh.user.reuvenvaisblehfinalproject.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.vaisbleh.user.reuvenvaisblehfinalproject.fragments.DetailsFragment;
import com.vaisbleh.user.reuvenvaisblehfinalproject.fragments.MapFragment;

//adapter for map and details fragments

/**
 * Created by jbt on 17/09/2017.
 */

public class MapFragmentAdapter extends FragmentPagerAdapter {
    Fragment[] fragments;

    public MapFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[]{new MapFragment(), new DetailsFragment()};
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                return fragments[0];

            case 1:
                return  fragments[1];
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;

    }

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){

            case 0:
                return "Map";

            case 1:
                return "Details";
        }

        return  null;
    }


}
