package assignment2.android.hua.gr.android_er2.broadcastReceivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.widget.Toast;

import java.util.Calendar;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.network.NetworkHelper;
import assignment2.android.hua.gr.android_er2.services.GPSTracker;

/**
 * Created by Manos on 22/1/2016.
 */
public class GPSStartedReceiver extends BroadcastReceiver {

    // Start service 5 seconds after GPS is enabled
    private static final long WAIT_TIME = 1000 * 5;
    // Has been triggered
    private boolean triggered = false;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED") && !triggered) {
            triggered = true;
            NetworkHelper helper = new NetworkHelper(context);

            if (helper.isGpsAvailable()) {
                Toast.makeText(context,
                        context.getResources().getString(R.string.starting_in_5),
                        Toast.LENGTH_SHORT).show();

                new CountDownTimer(WAIT_TIME, 1000) {
                    public void onFinish() {
                        // When timer is finished
                        Intent intent = new Intent(context, GPSTracker.class);
                        context.startService(intent);
                    }

                    public void onTick(long millisUntilFinished) {
                    }
                }.start();
            }
        }
    }
}