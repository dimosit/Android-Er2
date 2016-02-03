package assignment2.android.hua.gr.android_er2.ui;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.asyncTasks.DisplayLocations;

public class LocationsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new DisplayLocations(LocationsActivity.this, (ListView) findViewById(R.id.list),
                getApplicationContext()).execute();
    }

}
