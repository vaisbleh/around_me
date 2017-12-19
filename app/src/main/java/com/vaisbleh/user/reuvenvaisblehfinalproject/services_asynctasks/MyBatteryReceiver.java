package com.vaisbleh.user.reuvenvaisblehfinalproject.services_asynctasks;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jbt on 24/09/2017.
 */

public class MyBatteryReceiver extends BroadcastReceiver {

    OnBatteryChargingListener listener;

    public MyBatteryReceiver(OnBatteryChargingListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = "";


        switch (intent.getAction()){
            case Intent.ACTION_POWER_CONNECTED:
                message = "charging";
                break;

            case Intent.ACTION_POWER_DISCONNECTED:
                message = "no charging";
        }

        listener.batteryCharging(message);
    }

    public interface OnBatteryChargingListener{
        void batteryCharging (String message);
    }
}
