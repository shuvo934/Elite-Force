package ttit.com.shuvo.eliteforce.tracking;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;

import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.ui.IconGenerator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.fleet.assignment.arraylists.VehicleList;
import ttit.com.shuvo.eliteforce.fleet.assignment.dialogs.VehicleSelectDialog;
import ttit.com.shuvo.eliteforce.fleet.assignment.interfaces.VhSelectListener;
import ttit.com.shuvo.eliteforce.tracking.arraylists.AllVehicleLocationList;

public class VehicleTracking extends AppCompatActivity implements OnMapReadyCallback, VhSelectListener {

    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationRequest locationRequest;

    Spinner layer;

    AppCompatAutoCompleteTextView activeVehicle;
    ArrayList<VehicleList> vehicleLists;

    ArrayList<AllVehicleLocationList> allVehicleLocationLists;


    List<Marker> allMarkers;
    List<Marker> allDetailsMarker;
    List<Circle> allLocationAccCircle;

    TextView textView;
    IconGenerator iconGenerator;
    View inflatedView;


    Handler timerHandler = new Handler();
    long startTime = 0;
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            getUpdatedLocation();
            //Toast.makeText(getApplicationContext(), String.format("%d:%02d", minutes, seconds),Toast.LENGTH_SHORT).show();
            //timerTextView.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 30000);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_tracking);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.live_vehicle_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();

        activeVehicle = findViewById(R.id.movement_vehicle_spinner);

        vehicleLists = new ArrayList<>();

        layer = findViewById(R.id.spinner_layer);

        iconGenerator = new IconGenerator(VehicleTracking.this);
        iconGenerator.setBackground(AppCompatResources.getDrawable(this,R.drawable.bg_custom_marker));
        inflatedView = View.inflate(VehicleTracking.this, R.layout.marker_custom, null);
        textView = inflatedView.findViewById(R.id.test_text);

        List<String> categories = new ArrayList<>();
        categories.add("NORMAL");
        categories.add("TRAFFIC");
        categories.add("SATELLITE");
        categories.add("TERRAIN");
        categories.add("HYBRID");
        categories.add("NO LANDMARK");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_item, categories);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        layer.setAdapter(spinnerAdapter);
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        enableGPS();

        layer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = parent.getItemAtPosition(position).toString();
                switch (name) {
                    case "NORMAL":
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        try {
                            // Customise the styling of the base map using a JSON object defined
                            // in a raw resource file.
                            boolean success = googleMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            VehicleTracking.this, R.raw.normal));

                            if (!success) {
                                Log.i("Failed ", "Style parsing failed.");
                            }
                        } catch (Resources.NotFoundException e) {
                            Log.e("Style ", "Can't find style. Error: ", e);
                        }
                        mMap.setTrafficEnabled(false);
                        break;
                    case "SATELLITE":
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        mMap.setTrafficEnabled(false);
                        break;
                    case "TERRAIN":
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        mMap.setTrafficEnabled(false);
                        break;
                    case "HYBRID":
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        mMap.setTrafficEnabled(false);
                        break;
                    case "TRAFFIC":
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        try {
                            // Customise the styling of the base map using a JSON object defined
                            // in a raw resource file.
                            boolean success = googleMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            VehicleTracking.this, R.raw.normal));

                            if (!success) {
                                Log.i("Failed ", "Style parsing failed.");
                            }
                        } catch (Resources.NotFoundException e) {
                            Log.e("Style ", "Can't find style. Error: ", e);
                        }
                        mMap.setTrafficEnabled(true);
                        break;
                    case "NO LANDMARK":
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        try {
                            // Customise the styling of the base map using a JSON object defined
                            // in a raw resource file.
                            boolean success = googleMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            VehicleTracking.this, R.raw.no_landmark));

                            if (!success) {
                                Log.i("Failed ", "Style parsing failed.");
                            }
                        } catch (Resources.NotFoundException e) {
                            Log.e("Style ", "Can't find style. Error: ", e);
                        }
                        mMap.setTrafficEnabled(false);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                LinearLayout info = new LinearLayout(getApplicationContext());
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(getApplicationContext());
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(getApplicationContext());
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

        mMap.setOnMarkerClickListener(marker -> {
            boolean mm = false;
            for (int i = 0; i < allDetailsMarker.size(); i++) {
                if (marker.equals(allDetailsMarker.get(i))) {
                    mm = true;
                    break;
                }
            }
            return mm;
        });

        activeVehicle.setOnClickListener(view -> {
            VehicleSelectDialog vehicleSelectDialog = new VehicleSelectDialog(vehicleLists, VehicleTracking.this);
            vehicleSelectDialog.show(getSupportFragmentManager(),"VHL");
        });
    }

    private void enableGPS() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {
            zoomToUserLocation();
        });

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(VehicleTracking.this,
                            1001);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    public void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Ekhane", "1");
            return;
        }
        mMap.setMyLocationEnabled(true);

        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(location -> {
            LatLng latLng;
            if (location != null) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                System.out.println(latLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            } else {
                latLng = new LatLng(23.6850, 90.3563);
                System.out.println(latLng);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001) {
            if (resultCode == Activity.RESULT_OK) {
                zoomToUserLocation();
                Log.i("Hoise ", "1");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("Hoise ", "2");
                LatLng latLng = new LatLng(23.6850, 90.3563);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
            }
        }
    }

    public void setLocationMarkerForAll() {
        if (allVehicleLocationLists != null) {

            for (int i = 0; i < allVehicleLocationLists.size(); i++) {
                LatLng latLng = new LatLng(Double.parseDouble(allVehicleLocationLists.get(i).getLat()),Double.parseDouble(allVehicleLocationLists.get(i).getLng()));
                float bear = Float.parseFloat(allVehicleLocationLists.get(i).getBearing());
                float acc = Float.parseFloat(allVehicleLocationLists.get(i).getAccuracy());
                if (allMarkers != null) {
                    Marker marker = allMarkers.get(i);
                    if (marker == null) {
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng);
                        if (allVehicleLocationLists.get(i).getIs_stopped().equals("1")) {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_off));
                            markerOptions.snippet("Speed: 0 KM/H"+
                                                "\nAddress: "+allVehicleLocationLists.get(i).getAddress());
                        }
                        else {
                            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_on));
                            markerOptions.snippet("Driver: "+allVehicleLocationLists.get(i).getDi_name()+
                                                "\nSpeed: "+allVehicleLocationLists.get(i).getSpeed()+
                                                "\nAddress: "+allVehicleLocationLists.get(i).getAddress());
                        }
                        markerOptions.rotation(bear);
                        markerOptions.title(allVehicleLocationLists.get(i).getVh_name());
                        markerOptions.anchor((float) 0.5, (float) 0.5);
                        markerOptions.flat(true);
                        marker = mMap.addMarker(markerOptions);
                        allMarkers.set(i,marker);
                        System.out.println("NULL MARKER");
                    }
                    else{
                        marker.setPosition(latLng);
                        marker.setRotation(bear);
                        if (allVehicleLocationLists.get(i).getIs_stopped().equals("1")) {
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_off));
                            marker.setSnippet("Speed: 0 KM/H"+
                                    "\nAddress: "+allVehicleLocationLists.get(i).getAddress());
                        }
                        else {
                            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.car_on));
                            marker.setSnippet("Driver: "+allVehicleLocationLists.get(i).getDi_name()+
                                    "\nSpeed: "+allVehicleLocationLists.get(i).getSpeed()+
                                    "\nAddress: "+allVehicleLocationLists.get(i).getAddress());
                        }
                        marker.setTitle(allVehicleLocationLists.get(i).getVh_name());
                        allMarkers.set(i,marker);
                        System.out.println("NOT NULL MARKER");
                    }
                }

                if (allLocationAccCircle != null) {
                    Circle circle = allLocationAccCircle.get(i);
                    if (circle == null) {
                        CircleOptions circleOptions = new CircleOptions();
                        circleOptions.center(latLng);
                        circleOptions.strokeWidth(4);
                        circleOptions.strokeColor(Color.parseColor("#d95206"));
                        circleOptions.fillColor(Color.argb(30,242,165,21));
                        circleOptions.radius(acc);
                        circle = mMap.addCircle(circleOptions);
                        allLocationAccCircle.set(i,circle);
                    }
                    else {
                        circle.setCenter(latLng);
                        circle.setRadius(acc);
                        allLocationAccCircle.set(i,circle);
                    }
                }

                if (allDetailsMarker != null) {
                    Marker marker = allDetailsMarker.get(i);
                    if (marker == null) {
                        String text = allVehicleLocationLists.get(i).getVh_name()+"\n"+allVehicleLocationLists.get(i).getTime();
                        textView.setText(text);
                        iconGenerator.setContentView(inflatedView);
                        marker = mMap.addMarker(new MarkerOptions()
                                .position(latLng).anchor(0,0)
                                .icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())));
                        allDetailsMarker.set(i,marker);
                    }
                    else {
                        String text = allVehicleLocationLists.get(i).getVh_name()+"\n"+allVehicleLocationLists.get(i).getTime();
                        textView.setText(text);
                        iconGenerator.setContentView(inflatedView);
                        marker.setPosition(latLng);
                        marker.setIcon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon()));
                        allDetailsMarker.set(i,marker);
                    }
                }

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void getUpdatedLocation() {

        String v_url = api_url_front+"movement/getActiveVehicle";
        String loc_url = api_url_front+"movement/getVehcileLocation";

        vehicleLists = new ArrayList<>();
        allVehicleLocationLists = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, loc_url, response -> {

            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject location = array.getJSONObject(i);
                        String ell_id = location.getString("ell_id")
                                .equals("null") ? "" : location.getString("ell_id");
                        String emp__ID = location.getString("ell_emp_id")
                                .equals("null") ? "" : location.getString("ell_emp_id");
                        String lat = location.getString("ell_lat")
                                .equals("null") ? "" : location.getString("ell_lat");
                        String lng = location.getString("ell_long")
                                .equals("null") ? "" : location.getString("ell_long");
                        String time = location.getString("ell_time")
                                .equals("null") ? "" : location.getString("ell_time");
                        String speed = location.getString("ell_speed")
                                .equals("null") ? "" : location.getString("ell_speed");
//                             = location.getString("ell_address")
//                                    .equals("null") ? "" : location.getString("ell_address");
                        String acc = location.getString("ell_accuracy")
                                .equals("null") ? "" : location.getString("ell_accuracy");
                        String bear = location.getString("ell_bearing")
                                .equals("null") ? "" : location.getString("ell_bearing");
                        String di_id = location.getString("ell_di_id")
                                .equals("null") ? "" : location.getString("ell_di_id");
                        String vi_id_new = location.getString("ell_vi_id")
                                .equals("null") ? "" : location.getString("ell_vi_id");
                        String is_stopped = location.getString("ell_is_stopped")
                                .equals("null") ? "" : location.getString("ell_is_stopped");
                        String vh_name_new = location.getString("name")
                                .equals("null") ? "" : location.getString("name");
                        String vh_lc_plate = location.getString("reg_no")
                                .equals("null") ? "" : location.getString("reg_no");
                        String di_name = location.getString("di_full_name")
                                .equals("null") ? "" : location.getString("di_full_name");


                        vh_name_new = vh_name_new+"\n("+vh_lc_plate+")";

                        byte[] ptext = location.getString("ell_address").getBytes(ISO_8859_1);
                        String adds = new String(ptext, UTF_8);

                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                        Date date = dateFormat.parse(time);//You will get date object relative to server/client timezone wherever it is parsed
                        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm a",Locale.getDefault()); //If you need time just put specific format for time like 'HH:mm:ss'
                        assert date != null;
                        String dateStr = formatter.format(date);
                        System.out.println("Converted Date: "+dateStr);

                        Calendar calendar = Calendar.getInstance();
                        Date nowDate = calendar.getTime();

                        // adding 6 hours
                        long serverTime = date.getTime() + (1000*60*60*6);

                        long diff = nowDate.getTime() - serverTime;

                        long secondsInMilli = 1000;
                        long minutesInMilli = secondsInMilli * 60;
                        long hoursInMilli = minutesInMilli * 60;
                        long daysInMilli = hoursInMilli * 24;

                        long elapsedDays = diff / daysInMilli;
                        diff = diff % daysInMilli;

                        long elapsedHours = diff / hoursInMilli;
                        diff = diff % hoursInMilli;

                        long elapsedMinutes = diff / minutesInMilli;
                        diff = diff % minutesInMilli;

                        long elapsedSeconds = diff / secondsInMilli;

                        System.out.println( elapsedDays+ " Days, "+ elapsedHours+" hours, "+ elapsedMinutes+" mins, "+ elapsedSeconds+ " seconds ");

                        if (elapsedDays == 0) {
                            if (elapsedHours == 0) {
                                if (elapsedMinutes == 0) {
                                    time = "Just now";
                                } else {
                                    time =  elapsedMinutes+" mins ago";
                                }
                            } else {
                                time = elapsedHours+" hours "+ elapsedMinutes+" mins ago";
                            }
                        } else {
                            time = elapsedDays +" Days " + elapsedHours+" hours "+ elapsedMinutes+" mins ago";
                        }

                        allVehicleLocationLists.add(new AllVehicleLocationList(ell_id,emp__ID,lat,lng,time,speed,adds,acc,bear,di_id,vi_id_new,is_stopped,vh_name_new,vh_lc_plate,di_name));
                    }

                    if (allMarkers == null) {
                        allMarkers = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            allMarkers.add(null);
                        }
                    }
                    else {
                        if (allMarkers.size() < allVehicleLocationLists.size()) {
                            int sz = allVehicleLocationLists.size() - allMarkers.size();
                            for (int i = 0; i < sz; i++) {
                                allMarkers.add(null);
                            }
                        }
                    }
                    if (allDetailsMarker == null) {
                        allDetailsMarker = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            allDetailsMarker.add(null);
                        }
                    }
                    else {
                        if (allDetailsMarker.size() < allVehicleLocationLists.size()) {
                            int sz = allVehicleLocationLists.size() - allDetailsMarker.size();
                            for (int i = 0; i < sz; i++) {
                                allDetailsMarker.add(null);
                            }
                        }
                    }
                    if (allLocationAccCircle == null) {
                        allLocationAccCircle = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            allLocationAccCircle.add(null);
                        }
                    }
                    else {
                        if (allLocationAccCircle.size() < allVehicleLocationLists.size()) {
                            int sz = allVehicleLocationLists.size() - allLocationAccCircle.size();
                            for (int i = 0; i < sz; i++) {
                                allLocationAccCircle.add(null);
                            }
                        }
                    }
                    setLocationMarkerForAll();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Vehicle Location Not Found",Toast.LENGTH_SHORT).show();
                }
            }
            catch (JSONException | ParseException e) {
                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        }, error -> Log.i("Error", error.toString()));

        StringRequest vhReq = new StringRequest(Request.Method.GET, v_url, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reqListInfo = array.getJSONObject(i);

                        String vi_id_new = reqListInfo.getString("ell_vi_id")
                                .equals("null") ? "" : reqListInfo.getString("ell_vi_id");
                        String year = reqListInfo.getString("year")
                                .equals("null") ? "" : reqListInfo.getString("year");
                        String model = reqListInfo.getString("model")
                                .equals("null") ? "" : reqListInfo.getString("model");
                        String name = reqListInfo.getString("name")
                                .equals("null") ? "" : reqListInfo.getString("name");
                        String reg_no = reqListInfo.getString("reg_no")
                                .equals("null") ? "" : reqListInfo.getString("reg_no");

                        vehicleLists.add(new VehicleList(vi_id_new,year,model,name,reg_no,"","",""));
                    }
                }
                requestQueue.add(stringRequest);
            }
            catch (JSONException e) {
                System.out.println(e.getLocalizedMessage());
                requestQueue.add(stringRequest);
            }
        }, error -> {
            System.out.println(error.getLocalizedMessage());
            requestQueue.add(stringRequest);
        });

        requestQueue.add(vhReq);
    }

    @Override
    public void onVehicleSelect(String name, String id, String rg_no, String di_id_new, String di_name, String di_emp_id) {
        LatLng latLng = null;
        for (int i = 0; i < allVehicleLocationLists.size(); i++) {
            if (id.equals(allVehicleLocationLists.get(i).getVi_id())) {
                latLng = new LatLng(Double.parseDouble(allVehicleLocationLists.get(i).getLat()),Double.parseDouble(allVehicleLocationLists.get(i).getLng()));
                break;
            }
        }
        if (latLng != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        }
    }
}