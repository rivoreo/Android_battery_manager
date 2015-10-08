package com.example.johnnysun.myapplication;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


import de.greenrobot.event.EventBus;


public class MainActivity extends AppCompatActivity {

    static private TextView view;
    private  NotificationCompat.Builder mBuilder;

    /*static public class IncomingHandler extends Handler {
        private final WeakReference<MainActivity> mActivity;

        IncomingHandler(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }
        @Override
        public void handleMessage(Message inputMessage) {
            switch (inputMessage.what) {
                case 1:
                    Bundle mBundle = inputMessage.getData();
                    int level = mBundle.getInt("Level", 0);
                    int voltage = mBundle.getInt("Voltage", 0);
                    view.setText("Level: " + level + "\n" +
                            "Voltage: " + voltage + "\n");
                    break;
                default:
                    super.handleMessage(inputMessage);
            }
    }

    }
    public IncomingHandler mHandler = new IncomingHandler(this);*/
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
        view.setText("Level: "+level+"\n"+
                            "Voltage: "+voltage+"\n");

        mBuilder =
                new NotificationCompat.Builder(MainActivity.this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Battery Info")
                        .setContentText(level+"% "+ voltage+"mV ");
        //Create a Intent
        Intent MainIntent = new Intent(MainActivity.this, MainActivity.class);
        //Pending
        PendingIntent MainPendingIntent = PendingIntent.getActivity(MainActivity.this, 0,
                MainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(MainPendingIntent);
        NotificationManager mNotificationmanager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationmanager.notify(0, mBuilder.build());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        view = (TextView) findViewById(R.id.textview1);
        //this.registerReceiver(BatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    /*private BroadcastReceiver BatteryReceiver = new BroadcastReceiver() {
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

            view.setText("Level: "+level+"\n"+
                                "Voltage: "+voltage+"\n"+
                                "Temperature: "+f_temperature+"\n"+
                                "Plugged: "+plug+"\n");
            mBuilder =
                    new NotificationCompat.Builder(MainActivity.this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Battery Info")
                            .setContentText(level+"% "+ voltage+"mV "+f_temperature+"C "+plug);
            //Create a Intent
            Intent MainIntent = new Intent(MainActivity.this, MainActivity.class);
            //Pending
            PendingIntent MainPendingIntent = PendingIntent.getActivity(MainActivity.this, 0, MainIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(MainPendingIntent);
            NotificationManager mNotificationmanager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationmanager.notify(0, mBuilder.build());

        }
    };*/

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
}
