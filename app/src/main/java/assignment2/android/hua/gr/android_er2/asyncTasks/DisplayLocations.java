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
import assignment2.android.hua.gr.android_er2.ui.MapsActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by Manos on 27/1/2016.
 */
public class DisplayLocations extends AsyncTask<Void, Void, Void> {

    private Activity activity;
    private ListView listView;
    private final ProgressDialog dialog;
    private Context context;
    private ArrayList<User> userData;
    private String location;
    public static final int REQUEST_GOOGLE_PLAY_SERVICES = 1972;

    /**
     * Constructor
     */
    public DisplayLocations(Activity activity, ListView listView, Context context) {

        this.activity = activity;
        this.context = context;
        this.dialog = new ProgressDialog(activity);
        this.userData = new ArrayList<>();
        this.listView = listView;
    }

    void fetchData() {
        DataManagement dm = new DataManagement(context);
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
        super.onPreExecute();

        dialog.setMessage(context.getResources().getString(R.string.loading_array));
        dialog.setIndeterminate(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //show dialog in main activity
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
                    if (location == null || location.equals(""))
                        Toast.makeText(context, R.string.no_location, Toast.LENGTH_LONG).show();
                    else
                        showOnMap();
                }
            };

            // set items click listener from the listView with the descriptions
            listView.setOnItemClickListener(mMessageClickedHandler);
        }
    }
}
