package assignment2.android.hua.gr.android_er2.ui;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.asyncTasks.DisplayLocations;
import assignment2.android.hua.gr.android_er2.broadcastReceiver.GPSStartedReceiver;
import assignment2.android.hua.gr.android_er2.model.User;
import assignment2.android.hua.gr.android_er2.services.GPSTracker;

public class MainActivity extends ActionBarActivity {

    GPSStartedReceiver receiver = new GPSStartedReceiver();
    boolean first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst();
        setContentView(R.layout.activity_main);
    }

    private void isFirst(){
        SharedPreferences sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        first = sharedPref.getBoolean(getString(R.string.first_time_run), false);

        if (first){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.first_time_run), true);
            editor.apply();
            Intent intent = new Intent(this, FirstActivity.class);
            startActivity(intent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {

            if (serviceClass.getName().equals(service.service.getClassName()))
                return true;
        }

        return false;
    }

    public void saveLocation(View v) {
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.service_started),
                Toast.LENGTH_LONG).show();
        this.registerReceiver(receiver, null);
    }

    public void stopLocation(View v) {
        Intent intent = new Intent(this, GPSTracker.class);
        unregisterReceiver(receiver);

        if (isMyServiceRunning(GPSTracker.class))
            stopService(intent);
    }

    public void showLocations(View v){
        Intent intent = new Intent(this, LocationsActivity.class);
        startActivity(intent);
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
}
