package com.example.johnnysun.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.NotificationCompat;

import de.greenrobot.event.EventBus;

/**
 * Boardcast receiver
 * Created by JohnnySun on 2015/10/8.
 */
public class MyReceiver extends BroadcastReceiver {

    //private  NotificationCompat.Builder mBuilder;

    @Override
    public void onReceive(Context context, Intent intent) {
        String plug;
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        int  temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        double f_temperature = temperature/10.0;

        if (intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == 0)
            plug = "Not plug";
        else
            plug = "Plugged";

           /* view.setText("Level: " + level + "\n" +
                    "Voltage: " + voltage + "\n" +
                    "Temperature: " + f_temperature + "\n" +
                    "Plugged: " + plug + "\n");*/

        Bundle bundle = new Bundle();
        bundle.putInt("Level", level);
        bundle.putInt("Voltage", voltage);
        bundle.putDouble("f_Temperature", f_temperature);
        bundle.putString("Plug", plug);
        BatteryEvent mEvent = new BatteryEvent(bundle);
        EventBus.getDefault().post(mEvent);
    }
}
