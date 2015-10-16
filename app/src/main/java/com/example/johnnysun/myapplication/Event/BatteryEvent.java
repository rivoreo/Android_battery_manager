package so.libdll.batterymanager.Event;

import android.os.Bundle;

/**
 * Battery Event class
 * Created by JohnnySun on 2015/10/8.
 */
public class BatteryEvent {
    public final Bundle mBundle;

    public BatteryEvent(Bundle revBundle) {
        this.mBundle = revBundle;
    }
}
