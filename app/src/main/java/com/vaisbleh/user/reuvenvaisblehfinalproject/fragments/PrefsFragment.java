package com.vaisbleh.user.reuvenvaisblehfinalproject.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Toast;
import com.vaisbleh.user.reuvenvaisblehfinalproject.R;
import com.vaisbleh.user.reuvenvaisblehfinalproject.model.Constants;

/**
 * Created by jbt on 17/09/2017.
 */

public class PrefsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.prefs);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String unit = sp.getString(Constants.PREF_UNITS_KEY, Constants.METERS);
        String dist = sp.getString(Constants.PREF_RADIUS_KEY, "500");
        Preference units = findPreference(Constants.PREF_UNITS_KEY);
        Preference radius = findPreference(Constants.PREF_RADIUS_KEY);

        units.setSummary(unit);
        radius.setSummary(dist);

        units.setOnPreferenceChangeListener(this);
        radius.setOnPreferenceChangeListener(this);

    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()){
            case Constants.PREF_UNITS_KEY:
                preference.setSummary(newValue+"");
                break;

            case Constants.PREF_RADIUS_KEY:
                if(Integer.parseInt(newValue.toString())> 2000){
                    Toast.makeText(getActivity(), "max radius 2000 meter", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(Integer.parseInt(newValue.toString())<100){
                    Toast.makeText(getActivity(), "min radius 100 meter", Toast.LENGTH_SHORT).show();
                    return false;
                }
                preference.setSummary(newValue+"");
                break;
        }
        return true;
    }
}
