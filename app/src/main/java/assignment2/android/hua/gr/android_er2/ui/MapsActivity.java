package assignment2.android.hua.gr.android_er2.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import assignment2.android.hua.gr.android_er2.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback {

    private String location;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Prepare and show a progress dialog
        dialog = new ProgressDialog(this);
        dialog.setMessage(getApplicationContext().getResources().getString(R.string.loading_location));
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(true);
        dialog.show();

        Intent intent = getIntent();
        // Get the location to display on map
        location = intent.getExtras().getString("location");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // Dismiss progress dialog
    private void progressDialogDismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //GoogleMap map;
        //map = googleMap;

        // Parse the location string
        String delimiter = "[,]";
        String[] tokens = location.split(delimiter);

        // Get latitude and longitude from the split location String
        double latitude = Double.parseDouble(tokens[0]);
        double longitude = Double.parseDouble(tokens[1]);

        // Create a new latLong from the location
        LatLng latLng = new LatLng(latitude, longitude);

        // Add a marker and zoom the camera to the location
        googleMap.addMarker(new MarkerOptions().position(latLng).title("User's last location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    @Override
    public void onMapLoaded() {
        // Dismiss the progress dialog
        progressDialogDismiss();
    }
}