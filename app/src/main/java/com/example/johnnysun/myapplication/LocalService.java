package com.example.johnnysun.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.johnnysun.myapplication.Event.BatteryEvent;
import de.greenrobot.event.EventBus;

/**
 * LocalEvent class.
 * Created by JohnnySun on 2015/10/10.
 */
public class LocalService extends Service{

    // Unique Identification Number for the Notification.
    // We use it on Notification start, and to cancel it.
    private  static  final int ONGOING_NOTIFICATION_ID = 1;

    private MyReceiver BatteryReceiver = new MyReceiver();
    private NotificationCompat.Builder mBuilder;
    private NotificationManager mNotificationmanager;

    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {
        LocalService getService() {
            return LocalService.this;
        }
    }

    // This method will be called when a MessageEvent is posted
    /**
     * Show a notification while battery status is changed."
     */
    public void onEvent(BatteryEvent event) {
        int level = event.mBundle.getInt("Level", 0);
        int voltage = event.mBundle.getInt("Voltage", 0);
        double f_temperature = event.mBundle.getDouble("f_Temperature", 0);
        String plug = event.mBundle.getString("Plug");

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        String str_notification_list = settings.getString("priority_list", "-2");
        int notification_priority = Integer.parseInt(str_notification_list);

        PendingIntent mIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Battery Info")
                        .setContentText(level + "% " +
                                voltage + "mV " +
                                f_temperature + "C" +
                                " Changer " + plug)
                        .setWhen(0)
                        // .setVisibility(NotificationCompat.VISIBILITY_SECRET)
                        .setContentIntent(mIntent);
        mBuilder.setPriority(notification_priority);

        /*mNotificationmanager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotificationmanager.notify(0, mBuilder.build());*/
        startForeground(ONGOING_NOTIFICATION_ID, mBuilder.build());
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);
        this.registerReceiver(BatteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        EventBus.getDefault().unregister(this);
        stopForeground(true);

        // Tell the user we stopped.
        Toast.makeText(this, "local_service_stopped", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients.  See
    // RemoteService for a more complete example.
    private final IBinder mBinder = new LocalBinder();

}
