package assignment2.android.hua.gr.android_er2.services;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Manos on 4/2/2016.
 */
public class CheckServices {
    Context context;

    public CheckServices(Context context){
        this.context = context;
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service
                : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
