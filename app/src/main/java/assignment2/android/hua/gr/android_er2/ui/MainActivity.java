package assignment2.android.hua.gr.android_er2.ui;

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
import assignment2.android.hua.gr.android_er2.services.CheckServices;
import assignment2.android.hua.gr.android_er2.services.GPSTracker;
import assignment2.android.hua.gr.android_er2.services.GetDataFromServer;

public class MainActivity extends ActionBarActivity {

    private CheckServices checkServices = new CheckServices(this);
    private GPSStartedReceiver receiver = new GPSStartedReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFirst();

        setContentView(R.layout.activity_main);

        // Get all the users data and save them to the mobile's DB
        Intent intent = new Intent(getApplicationContext(), GetDataFromServer.class);
        startService(intent);

        // Register a broadcast receiver which listens to GPS provider changes
        this.registerReceiver(receiver,
                new IntentFilter("android.location.PROVIDERS_CHANGED"));
    }

    /**
     * If this app runs on a mobile phone for the first time after installation
     * or its data have been deleted, starts an activity for registration
     */
    private void isFirst() {
        boolean first;
        SharedPreferences sharedPref = this.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        first = sharedPref.getBoolean
                (getString(R.string.first_time_run), true);

        if (first) {
            Intent i = new Intent(this, FirstActivity.class);
            startActivity(i);
        }
    }

    /**
     * Starts an activity to show all the names of the users who are using this app in a list
     * @param v the view
     */
    public void showLocations(View v) {
        Intent i = new Intent(this, LocationsActivity.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }

    @Override
    public void onPause() {
        // Start service to get all the users data if it's not already running
        if (!checkServices.isMyServiceRunning(GetDataFromServer.class)) {
            Intent intent = new Intent(getApplicationContext(), GetDataFromServer.class);
            startService(intent);
        }

        super.onPause();
    }

    @Override
    public void onDestroy() {
        // Unregister the broadcast receiver
        unregisterReceiver(receiver);

        // Stop location tracking service if it is running
        if (checkServices.isMyServiceRunning(GPSTracker.class)) {
            Intent intent = new Intent(getApplicationContext(), GPSTracker.class);
            stopService(intent);
        }

        // Start service to get all the users data if it's not already running
        if (!checkServices.isMyServiceRunning(GetDataFromServer.class)) {
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
}
