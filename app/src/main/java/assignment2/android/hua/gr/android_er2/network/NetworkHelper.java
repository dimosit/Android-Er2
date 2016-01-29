package assignment2.android.hua.gr.android_er2.network;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.widget.Toast;

import assignment2.android.hua.gr.android_er2.R;

/**
 * Created by Manos on 29/1/2016.
 */
public class NetworkHelper {

    Context context;

    public NetworkHelper(Context context){
        this.context = context;
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean isGpsAvaiable(){
        boolean gps_check;
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        gps_check = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (gps_check == false){
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

            // Setting Dialog Title
            alertDialog.setTitle(context.getResources().getString(R.string.cannot_establish_GPS));

            // Setting Dialog Message
            alertDialog.setMessage(context.getResources().getString(R.string.GPS_settings_message));

            // Settings button
            alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(intent);
                }
            });

            // Cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();
        }
        return gps_check;
    }

    // Shows network settings to the user
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

        // Setting Dialog Title
        alertDialog.setTitle(context.getResources().getString(R.string.cannot_establish_Internet));

        // Setting Dialog Message
        alertDialog.setMessage(context.getResources().getString(R.string.Internet_settings_message));

        // Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                context.startActivity(intent);
            }
        });

        // Cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}