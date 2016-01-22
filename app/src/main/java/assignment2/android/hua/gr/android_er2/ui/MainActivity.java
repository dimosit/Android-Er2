package assignment2.android.hua.gr.android_er2.ui;

import android.content.ComponentName;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.services.GPSTracker;

public class MainActivity extends ActionBarActivity {

    boolean recording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void saveLocation(View v){
        Intent intent = new Intent(getApplicationContext(), GPSTracker.class);

        // to kommati apo edw kai katw ua allaksei apla to evala etsi poli proxeira
        // argotera pithanon tha dimiourgisoume AlarmManager kai tha trexoume to service kathe 30 deutera
        // isws na mi xreiastei kan to button kai na ta valoume sthn onCreate epeidi de zitaei kati tetoio

        if(!recording) {	// if the device is not recording GPS Location
            recording = true;	// change the flag to true
            startService(intent);	// and start the service
            Toast.makeText(getApplicationContext(), "Recording...", Toast.LENGTH_SHORT).show();
        }

        else {
            stopService(intent);	// if the device is recording GPS Location stop the service
            recording = false;
            Toast.makeText(getApplicationContext(), "Recording Stopped!", Toast.LENGTH_SHORT).show();
        }
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
