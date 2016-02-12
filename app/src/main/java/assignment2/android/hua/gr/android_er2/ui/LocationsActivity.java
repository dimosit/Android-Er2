package assignment2.android.hua.gr.android_er2.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.asyncTasks.DisplayLocations;
import assignment2.android.hua.gr.android_er2.services.CheckServices;
import assignment2.android.hua.gr.android_er2.services.GetDataFromServer;

public class LocationsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*
         * If GetDataFromServer Service is not running, start it,
         * so that the list of the users will be updated
         */
        CheckServices checkServices = new CheckServices(getApplicationContext());
        if (!checkServices.isMyServiceRunning(GetDataFromServer.class)){
            Intent intent = new Intent(getApplicationContext(), GetDataFromServer.class);
            startService(intent);
        }

        new DisplayLocations(LocationsActivity.this, (ListView) findViewById(R.id.list),
                getApplicationContext()).execute();
    }

}
