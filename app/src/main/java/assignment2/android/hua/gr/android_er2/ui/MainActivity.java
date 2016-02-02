package assignment2.android.hua.gr.android_er2.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.broadcastReceivers.GPSStartedReceiver;
import assignment2.android.hua.gr.android_er2.services.GPSTracker;
import assignment2.android.hua.gr.android_er2.services.GetDataFromServer;

public class MainActivity extends ActionBarActivity {

    GPSStartedReceiver receiver = new GPSStartedReceiver();
    boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFirst();

        setContentView(R.layout.activity_main);

        Intent intent = new Intent(getApplicationContext(), GetDataFromServer.class);
        startService(intent);

        this.registerReceiver(receiver,
                new IntentFilter("android.location.PROVIDERS_CHANGED"));
        intent = new Intent();
        intent.setAction
                ("assignment2.android.hua.gr.android_er2.broadcastReceiver.GPSStartedReceiver");
        sendBroadcast(intent);
    }

    private void isFirst(){
        SharedPreferences sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        first = sharedPref.getBoolean
                (getString(R.string.first_time_run), true);

        if (first){
            Intent i = new Intent(this, FirstActivity.class);
            startActivity(i);
        }
    }


    @Override
    public void onBackPressed() {
        // Do nothing
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service
                : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onPause() {
        if (!isMyServiceRunning(GetDataFromServer.class)) {
            Intent intent = new Intent(getApplicationContext(), GetDataFromServer.class);
            startService(intent);
        }

        super.onPause();
    }

    @Override
    public void onDestroy(){
        unregisterReceiver(receiver);

        if (isMyServiceRunning(GPSTracker.class)) {
            Intent intent = new Intent(getApplicationContext(), GPSTracker.class);
            stopService(intent);
        }

        if (!isMyServiceRunning(GetDataFromServer.class)) {
            Intent intent = new Intent(getApplicationContext(), GetDataFromServer.class);
            startService(intent);
        }

        super.onDestroy();
    }

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
//    add test comment
}
