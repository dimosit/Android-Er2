package assignment2.android.hua.gr.android_er2.broadcastReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import java.util.Calendar;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.services.GPSTracker;

/**
 * Created by Manos on 22/1/2016.
 */
public class GPSStartedReceiver extends BroadcastReceiver {

    // Start service 5 seconds after GPS is enabled
    private static final long WAIT_TIME = 1000 * 5;
    // Restart service every 30 seconds
    private static final long REPEAT_TIME = 1000 * 30;

    @Override
    public void onReceive(final Context context, final Intent intent) {

        if (intent.getAction().equals("android.location.GPS_ENABLED_CHANGE")) {

            final Handler handler = new Handler();

            Toast.makeText(context,
                    context.getResources().getString(R.string.starting_in_5),
                    Toast.LENGTH_SHORT).show();

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AlarmManager service = (AlarmManager) context
                            .getSystemService(Context.ALARM_SERVICE);
                    Intent i = new Intent(context, GPSTracker.class);
                    PendingIntent pending = PendingIntent.getBroadcast(context, 0, i,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    Calendar cal = Calendar.getInstance();
                    // Start 30 seconds after boot completed
                    cal.add(Calendar.SECOND, 30);
                    // Fetch every 30 seconds
                    service.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                            cal.getTimeInMillis(), REPEAT_TIME, pending);
                }
            }, WAIT_TIME);

        }
    }
}