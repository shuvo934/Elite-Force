package ttit.com.shuvo.eliteforce.attendance.att_report.map_view;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ttit.com.shuvo.eliteforce.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        assert mapFragment != null;
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

        googleMap.getUiSettings().setZoomControlsEnabled(true);

        Intent intent = getIntent();
        String lat = intent.getStringExtra("lat");
        String lon = intent.getStringExtra("lon");
        String time = intent.getStringExtra("time");
        String date = intent.getStringExtra("date");
        String status = intent.getStringExtra("status");


        MarkerOptions mp = new MarkerOptions();
        if (lat == null || lat.isEmpty()) {
            lat = "0.0";
        }
        if (lon == null || lon.isEmpty()) {
            lon = "0.0";
        }
        LatLng ll = new LatLng(Double.parseDouble(lat),Double.parseDouble(lon));
        String snippet = date+","+time;

        mp.position(ll);
        mp.title(status);
        mp.snippet(snippet);
        googleMap.addMarker(mp);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ll, 18));
    }
}