package assignment2.android.hua.gr.android_er2.ui;

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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    String location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Intent intent = getIntent();
        location = intent.getExtras().getString("location");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap map;
        map = googleMap;

        String delimiter = "[,]";
        String[] tokens = location.split(delimiter);

        double latitude = Double.parseDouble(tokens[0]);
        double longitude = Double.parseDouble(tokens[1]);

        // Add a marker and move the camera
        LatLng latLng = new LatLng(latitude, longitude);
        map.addMarker(new MarkerOptions().position(latLng).title("User's last location"));
        map.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    }
}
