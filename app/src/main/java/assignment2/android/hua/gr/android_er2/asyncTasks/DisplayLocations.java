package assignment2.android.hua.gr.android_er2.asyncTasks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.database.DataManagement;
import assignment2.android.hua.gr.android_er2.model.User;
import assignment2.android.hua.gr.android_er2.network.NetworkHelper;
import assignment2.android.hua.gr.android_er2.ui.MapsActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class DisplayLocations extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    private ListView listView;
    private final ProgressDialog dialog;
    private Context context;
    private ArrayList<User> userData;
    private String location;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;

    /**
     * DisplayLocations Constructor
     *
     * @param activity the activity
     * @param listView the listView
     * @param context  the context
     */
    public DisplayLocations(Activity activity, ListView listView, Context context) {

        this.activity = activity;
        this.context = context;
        this.dialog = new ProgressDialog(activity);
        this.userData = new ArrayList<>();
        this.listView = listView;
    }

    /**
     * Gets all the users data from the mobile's db
     */
    void fetchData() {
        DataManagement dm = new DataManagement(context);
        userData = dm.getAllUsersFromDB();
    }

    /**
     * Checks if the progress dialog is showing.<br>
     * If yes, it dismisses it.
     */
    private void progressDialogDismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    /**
     * Checks if Google Api is available.<br>
     * If yes, it starts MapActivity.<br>
     * If not, it shows a toast message to the user.
     */
    private void startRegistrationService() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(activity);

        if (code == ConnectionResult.SUCCESS) {
            Intent i = new Intent(activity, MapsActivity.class);
            // Pass the location to the Maps activity
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
        super.onPreExecute();

        // Prepare and show a progress dialog
        dialog.setMessage(context.getResources().getString(R.string.loading_array));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
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

        // Get every user's name and position and add them in an ArrayList
        for (User u : userData) {
            names.add(u.getUsername());
            positions.add(u.getCurrent_location());
        }

        // If there are no positions, display proper message
        if (positions.isEmpty())
            Toast.makeText(context, R.string.empty_array, Toast.LENGTH_LONG).show();

            // Otherwise, fill the list
        else {

            ArrayAdapter<String> adapter = new ArrayAdapter<>(activity,
                    android.R.layout.simple_list_item_1, names);

            // Set the listView with the names
            listView.setAdapter(adapter);

            // Create a Click Listener
            AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView parent, View v, int position, long id) {
                    location = positions.get(position);

                    // If the users doesn't have any location saved, display proper message
                    if (location == null || location.equals(""))
                        Toast.makeText(context, R.string.no_location, Toast.LENGTH_SHORT).show();

                        // Otherwise, if Network is available,
                        // prepare to show the user's location on Google Maps
                    else {
                        NetworkHelper networkHelper = new NetworkHelper(activity);
                        if (networkHelper.isNetworkAvailable()) {
                            Toast.makeText(activity, R.string.loading_map, Toast.LENGTH_SHORT).show();
                            startRegistrationService();
                        } else
                            networkHelper.showSettingsAlert();
                    }
                }
            };

            // Set click listener on the list's names
            listView.setOnItemClickListener(mMessageClickedHandler);
        }
    }
}