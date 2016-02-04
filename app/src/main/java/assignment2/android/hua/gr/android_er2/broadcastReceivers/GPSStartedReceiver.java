package assignment2.android.hua.gr.android_er2.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.widget.Toast;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.network.NetworkHelper;
import assignment2.android.hua.gr.android_er2.services.CheckServices;
import assignment2.android.hua.gr.android_er2.services.GPSTracker;

public class GPSStartedReceiver extends BroadcastReceiver {

    // Start service 5 seconds after GPS is enabled
    private static final long WAIT_TIME = 1000 * 5;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (intent.getAction().equals("android.location.PROVIDERS_CHANGED")) {
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
            else {
                CheckServices checkServices = new CheckServices(context);

                if (checkServices.isMyServiceRunning(GPSTracker.class)) {
                    Toast.makeText(context,
                            context.getResources().getString(R.string.stopped_tracking),
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, GPSTracker.class);
                    context.stopService(i);
                }
            }
        }
    }
}