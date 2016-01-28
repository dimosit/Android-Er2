package assignment2.android.hua.gr.android_er2.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;


import java.util.ArrayList;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.database.DataManagement;
import assignment2.android.hua.gr.android_er2.model.User;
import assignment2.android.hua.gr.android_er2.ui.MapsActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Manos on 27/1/2016.
 */
public class DisplayLocations extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    private ListView listView;
    private ProgressDialog dialog;
    private Context context;
    private ArrayList<User> userData;
    private String location;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;

    /**
     * Constructor
     */
    public DisplayLocations(Activity activity, Context context) {

        this.activity = activity;
        this.context = context;
        this.dialog = new ProgressDialog(context);
        this.userData = new ArrayList<User>();
        this.listView = (ListView) activity.findViewById(R.id.list);
    }

    void fetchData() {
        DataManagement dm = new DataManagement();
        userData = dm.getAllUsersFromDB();
    }

    // dismiss progress dialog
    private void progressDialogDismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    private void showOnMap() {
        startRegistrationService();
    }

    private void startRegistrationService() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(activity);
        if (code == ConnectionResult.SUCCESS) {
            Intent i = new Intent(activity, MapsActivity.class);
            i.putExtra("location", location);
            activity.startActivity(i);
        } else if (api.isUserResolvableError(code) &&
                api.showErrorDialogFragment(activity, code, REQUEST_GOOGLE_PLAY_SERVICES)) {
            Toast.makeText(activity, R.string.wrong_location, Toast.LENGTH_LONG).show();
        } else {
            String str = GoogleApiAvailability.getInstance().getErrorString(code);
            Toast.makeText(activity, str, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPreExecute() {
        dialog.setMessage(context.getResources().getString(R.string.loading_array));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //show dialog in main activity
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        fetchData();
        return null;
    }

    @Override
    protected void onPostExecute(Void v) {
        progressDialogDismiss();

        ArrayList<String> names = new ArrayList<>();
        final ArrayList<String> positions = new ArrayList<>();

        for (User u : userData) {
            names.add(u.getUsername());
            positions.add(u.getCurrent_location());
        }

        if (positions.isEmpty())
            Toast.makeText(context, R.string.empty_array, Toast.LENGTH_LONG).show();

        else {

            ArrayAdapter<String> adapter = new ArrayAdapter<>(activity,
                    android.R.layout.simple_list_item_1, names);

            // set the listView with the names
            listView.setAdapter(adapter);

            AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    location = positions.get(position);
                    showOnMap();
                }
            };

            // set items click listener from the listView with the descriptions
            listView.setOnItemClickListener(mMessageClickedHandler);
        }
    }
}