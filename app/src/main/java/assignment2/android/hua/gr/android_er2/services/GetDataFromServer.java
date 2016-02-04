package assignment2.android.hua.gr.android_er2.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import assignment2.android.hua.gr.android_er2.asyncTasks.GetAllData;
import assignment2.android.hua.gr.android_er2.network.NetworkHelper;

public class GetDataFromServer extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        NetworkHelper networkHelper = new NetworkHelper(this);
        if (networkHelper.isNetworkAvailable())
            new GetAllData(GetDataFromServer.this).execute();
    }
}