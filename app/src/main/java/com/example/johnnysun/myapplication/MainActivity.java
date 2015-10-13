package com.example.johnnysun.myapplication;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import com.example.johnnysun.myapplication.Event.BatteryEvent;

import de.greenrobot.event.EventBus;


public class MainActivity extends AppCompatActivity {

    static private TextView view;
    private NotificationCompat.Builder mBuilder;
    String plug;

    void update_battery_view() {
        view = (TextView) findViewById(R.id.textview1);
        Intent mIntent = this.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        int level = mIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        int voltage = mIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        int  temperature = mIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        double f_temperature = temperature/10.0;
        if (mIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0) == 0)
            plug = "Not plug";
        else
            plug = "Plugged";

        view.setText("Level: "+level+"\n"+
                "Voltage: "+voltage+"\n"+
                "Plug Status: "+plug+"\n"+
                "Temperature: "+f_temperature+"\n");
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    // This method will be called when a MessageEvent is posted
    public void onEvent(BatteryEvent event) {
        int level = event.mBundle.getInt("Level", 0);
        int voltage = event.mBundle.getInt("Voltage", 0);
        double f_temperature = event.mBundle.getDouble("f_Temperature", 0);
        String plug = event.mBundle.getString("Plug");
        view.setText("Level: " + level + "\n" +
                "Voltage: " + voltage + "\n" +
                "Plug Status: " + plug + "\n" +
                "Temperature: " + f_temperature + "\n");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent ServiceIndent = new Intent(MainActivity.this, LocalService.class);
        startService(ServiceIndent);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        update_battery_view();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Refreshing Battery Status...", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                update_battery_view();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void go_setting(MenuItem item) {
        Intent mIntent = new Intent(MainActivity.this, SettingsActivity.class);
        startActivity(mIntent);
    }
}
