package com.example.johnnysun.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Toast;

import com.example.johnnysun.myapplication.Event.BatteryEvent;

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

        //Make String for Toast use.
        String toast_string = "Battery: " + level + "%";
        //Taying Send Toast when battery status changed.
        Toast.makeText(context.getApplicationContext(), toast_string, Toast.LENGTH_LONG).show();

        //Use EventBus to broadcast event.
        //Package data to a bundle.
        Bundle bundle = new Bundle();
        bundle.putInt("Level", level);
        bundle.putInt("Voltage", voltage);
        bundle.putDouble("f_Temperature", f_temperature);
        bundle.putString("Plug", plug);

        //Use EventBus to broadcast event.
        BatteryEvent mEvent = new BatteryEvent(bundle);
        //Send event
        EventBus.getDefault().post(mEvent);
    }
}
