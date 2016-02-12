package assignment2.android.hua.gr.android_er2.services;

import android.app.ActivityManager;
import android.content.Context;

public class CheckServices {
    Context context;

    /**
     * CheckServices Constructor
     * @param context the context
     */
    public CheckServices(Context context){
        this.context = context;
    }

    /**
     * Checks if a service is running
     * @param serviceClass the service's class
     * @return if the service is running
     */
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
