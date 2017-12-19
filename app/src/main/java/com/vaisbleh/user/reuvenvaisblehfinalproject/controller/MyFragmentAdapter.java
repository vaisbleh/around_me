package com.vaisbleh.user.reuvenvaisblehfinalproject.controller;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.vaisbleh.user.reuvenvaisblehfinalproject.fragments.FavoriteFragment;
import com.vaisbleh.user.reuvenvaisblehfinalproject.fragments.MainListFragment;
//adapter for search (mainList) and favorites fragments

/**
 * Created by User on 14-Sep-17.
 */

public class MyFragmentAdapter extends FragmentPagerAdapter {

    Fragment[] fragments;

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
        fragments = new Fragment[]{new MainListFragment(), new FavoriteFragment()};
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
                return "Search results";

            case 1:
                return "Favorites";
        }

        return  null;
    }
}
