package assignment2.android.hua.gr.android_er2.services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.Settings;

import assignment2.android.hua.gr.android_er2.R;
import assignment2.android.hua.gr.android_er2.asyncTasks.SendLocation;
import assignment2.android.hua.gr.android_er2.network.NetworkHelper;

public class GPSTracker extends Service implements LocationListener {
    /**
     * Flag for GPS status
     */
    boolean isGPSEnabled = false;

    /**
     * Flag for network status
     */
    boolean isNetworkEnabled = false;

    /**
     * Flag for capability of location tracking
     */
    boolean canGetLocation = false;

    Location location;
    double latitude;
    double longitude;

    /**
     * The minimum time between updates in milliseconds
     */
    private static final long MIN_TIME_BW_UPDATES = 1000 * 25; // 25 seconds
    /**
     * The repeating time of the location tracking
     */
    private static final long REPEAT_TIME = 1000 * 30;

    /**
     * Location Manager
     */
    protected LocationManager locationManager;

    final Handler handler = new Handler();
    Thread thread;

    /**
     * Gets last recorded location and tracks it
     */
    private void trackLocation() {
        getLocation();
        SharedPreferences userDetails =
                getApplicationContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        int id = userDetails.getInt("MyId", 0);

        String locationString = locationToString();

        new SendLocation(id, locationString, getApplicationContext()).execute();
    }

    @Override
    public void onCreate() {
        trackLocation();

        // Track location again every 30 seconds
        thread = new Thread(new Runnable() {
            public void run() {
                trackLocation();
            }
        });
        handler.postDelayed(thread, REPEAT_TIME);
    }

    private String locationToString() {
        return String.valueOf(latitude)
                + "," + String.valueOf(longitude);
    }

    /**
     * Gets the user's location.<br>
     * If neither network nor GPS is enabled, shows an alert dialog.
     */
    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled)
                showSettingsAlert();

            else {
                this.canGetLocation = true;

                // First get location from the Network Provider
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            0, this);

                    if (locationManager != null) {
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }

                    }
                }

                // If GPS is enabled get lat/long using GPS Services
                if (isGPSEnabled) {

                    if (location == null) {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                0, this);

                        if (locationManager != null) {
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop using GPS listener
     * Calling this function will stop using GPS in your app
     */
    public void stopUsingGPS() {
        if (locationManager != null)
            locationManager.removeUpdates(GPSTracker.this);
    }

    /**
     * Function to get latitude
     */
    public double getLatitude() {
        if (location != null)
            latitude = location.getLatitude();

        return latitude;
    }

    /**
     * Function to get longitude
     */
    public double getLongitude() {
        if (location != null)
            longitude = location.getLongitude();

        return longitude;
    }

    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }

    /**
     * Function to show settings alert dialog
     * On pressing Settings button will launch Settings Options
     */
    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getApplicationContext());

        // Setting Dialog Title
        alertDialog.setTitle(getResources().getString(R.string.cannot_establish_GPS));

        // Setting Dialog Message
        alertDialog.setMessage(getResources().getString(R.string.GPS_settings_message));

        // Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                getApplicationContext().startActivity(intent);
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

    @Override
    public void onLocationChanged(Location location) {
        // Remove this listener
        locationManager.removeUpdates(this);
    }

    @Override
    public void onProviderDisabled(String provider) {
        NetworkHelper networkHelper = new NetworkHelper(getApplicationContext());
        networkHelper.isGpsAvailable();
        networkHelper.showGPSAlert();
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(thread);
    }
}
