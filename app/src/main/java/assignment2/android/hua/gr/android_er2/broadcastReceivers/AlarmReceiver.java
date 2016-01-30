package assignment2.android.hua.gr.android_er2.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import assignment2.android.hua.gr.android_er2.services.GPSTracker;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!(intent.getAction().matches("android.location.PROVIDERS_CHANGED"))) {

            Intent i = new Intent(context, GPSTracker.class);
            context.startService(i);
        }
    }
}