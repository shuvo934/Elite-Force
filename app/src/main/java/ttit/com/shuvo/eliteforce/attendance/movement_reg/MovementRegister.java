package ttit.com.shuvo.eliteforce.attendance.movement_reg;

import static ttit.com.shuvo.eliteforce.attendance.trackService.model.DistanceCalculator.CalculationByDistance;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.trackService.Service;
import ttit.com.shuvo.eliteforce.basic_model.TwoItemLists;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class MovementRegister extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    TextView todayDate;
    String today_date = "";

    AmazingSpinner clientSpinner;
    String selected_ad_id = "";
    ArrayList<TwoItemLists> clientLists;
    AmazingSpinner typeSpinner;
    String selected_type = "";
    ArrayList<TwoItemLists> typeLists;

    TextInputEditText movementPurpose, carryAmnt, startLocationText, endLocationText;
    String mov_purpose = "";
    String carry_amnt = "";
    String st_loc_text = "";
    String end_loc_text = "";

    MaterialCardView movementButton;
    TextView nameMov;

    private GoogleApiClient googleApiClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    String emp_id = "";
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    public static ArrayList<LatLng> autoGpxLatLng;
    private String lineStart = "0";
    private Boolean isStart = false;
    private Boolean autoLineValue = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_register);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        todayDate = findViewById(R.id.today_date_time_movement_reg);
        clientSpinner = findViewById(R.id.movement_client_spinner);
        typeSpinner = findViewById(R.id.movement_type_spinner);

        movementPurpose = findViewById(R.id.movement_purpose_text);
        carryAmnt = findViewById(R.id.carry_amount_movement);
        startLocationText = findViewById(R.id.start_location_movement);
        endLocationText = findViewById(R.id.end_location_movement);

        movementButton = findViewById(R.id.movement_start_end_button);
        nameMov = findViewById(R.id.name_of_movement);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.movement_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        emp_id = userInfoLists.get(0).getEmp_id();

        clientLists = new ArrayList<>();
        typeLists = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
        today_date = simpleDateFormat.format(calendar.getTime());
        todayDate.setText(today_date);

        typeLists.add(new TwoItemLists("1","CIT"));
        typeLists.add(new TwoItemLists("2","Ground Service"));

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < typeLists.size(); i++) {
            type.add(typeLists.get(i).getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        typeSpinner.setAdapter(arrayAdapter);

        clientSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            selected_ad_id = "";
            for (int i = 0; i <clientLists.size(); i++) {
                if (name.equals(clientLists.get(i).getName())) {
                    selected_ad_id = clientLists.get(i).getId();
                }
            }
        });

        typeSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            selected_type = "";
            for (int i = 0; i <typeLists.size(); i++) {
                if (name.equals(typeLists.get(i).getName())) {
                    selected_type = typeLists.get(i).getId();
                }
            }
        });

        movementPurpose.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        carryAmnt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        startLocationText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        endLocationText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        if (isMyServiceRunning()) {
            String tt = "END MOVEMENT";
            nameMov.setText(tt);
        } else {
            String tt = "START MOVEMENT";
            nameMov.setText(tt);
        }

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        assert data != null;
                        System.out.println("EKHANE ASHE CHECK: " + data);
                    }
                });

        getData();
    }

    @Override
    public void onBackPressed() {
        if (isStart) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MovementRegister.this);
            builder
                    .setMessage("Movement Progress is Running. Do you want to close ?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                        finish();
                    })
                    .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

            AlertDialog alert = builder.create();
            alert.show();
        }
        else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
            finish();
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng[] lastLatLongitude = {new LatLng(0, 0)};
        mMap.getUiSettings().setZoomControlsEnabled(true);
        final PolylineOptions[] nop = {new PolylineOptions().width(5).color(Color.RED).geodesic(true)};
        final int[] local = {0};
        MarkerOptions mp = new MarkerOptions();
        final LatLng[] autoPreLatlng = {new LatLng(0, 0)};
        final Double[] w = {0.0};

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (isStart) {
                        Log.i("LocationFused ", location.toString());
                        lastLatLongitude[0] = new LatLng(location.getLatitude(), location.getLongitude());
                        if (local[0] == 0) {

                            local[0]++;
                            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                            autoPreLatlng[0] = ll;
                            nop[0].add(ll);
                            mMap.addPolyline(nop[0]);
                            mp.position(ll);
                            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
                            mp.anchor((float) 0.5, (float) 0.5);
                            autoGpxLatLng.add(ll);
                            mp.snippet("0 KM");
                            mp.title("Starting Point");
                            mMap.addMarker(mp);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18));
                        } else {

                            local[0]++;
                            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                            Double distance = CalculationByDistance(autoPreLatlng[0], ll);

                            Log.i("Distance", distance.toString());

                            if (distance >= 0.01) {

                                nop[0].add(ll);
                                mMap.addPolyline(nop[0]);
                                mp.position(ll);
                                mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
                                mp.anchor((float) 0.5, (float) 0.5);
                                w[0] = w[0] + distance;
                                mp.snippet(String.format(Locale.ENGLISH,"%.3f", w[0]) + " KM");
                                mp.title("Road Point");
                                mMap.addMarker(mp);
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 18));
                                autoGpxLatLng.add(ll);
                                autoPreLatlng[0] = ll;
                            }

                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLongitude[0], 18));
                    }
                }
            }
        };

        movementButton.setOnClickListener(v -> {
            mov_purpose = Objects.requireNonNull(movementPurpose.getText()).toString();
            carry_amnt = Objects.requireNonNull(carryAmnt.getText()).toString();
            st_loc_text = Objects.requireNonNull(startLocationText.getText()).toString();
            end_loc_text = Objects.requireNonNull(endLocationText.getText()).toString();

            if (!selected_ad_id.isEmpty()) {
                if (!selected_type.isEmpty()) {
                    if (!mov_purpose.isEmpty()) {
                        if (isStart) {
                            System.out.println("Service Stopped");

                            String tt = "START MOVEMENT";
                            nameMov.setText(tt);

                            local[0] = 0;
                            autoLineValue = false;
                            lineStart = "0";
                            isStart = false;

//                            Log.i("Last Location", lastLatLongitude.toString());

                            LatLng lastLatlng = lastLatLongitude[0];
                            nop[0].add(lastLatlng);
                            mMap.addPolyline(nop[0]);
                            mp.position(lastLatlng);
                            autoGpxLatLng.add(lastLatlng);
                            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
                            mp.anchor((float) 0.5, (float) 0.5);
                            Double distance = CalculationByDistance(autoPreLatlng[0], lastLatlng);
                            w[0] = w[0] + distance;
                            mp.snippet(String.format(Locale.ENGLISH,"%.3f", w[0]) + " KM");
                            mp.title("End Point");
                            mMap.addMarker(mp);

                            nop[0] = new PolylineOptions().width(5).color(Color.RED).geodesic(true);

                            w[0] = 0.0;
                            autoGpxLatLng.clear();
                            autoGpxLatLng = new ArrayList<>();
                            local[0] = 0;
                            autoPreLatlng[0] = new LatLng(0, 0);
                            lastLatLongitude[0] = new LatLng(0,0);

                            AlertDialog.Builder builder = new AlertDialog.Builder(MovementRegister.this);
                            builder.setTitle("Success!")
                                    .setMessage("Movement Registered Successfully")
                                    .setPositiveButton("OK", (dialog, which) -> finish());
                            AlertDialog alert = builder.create();
                            alert.setCancelable(false);
                            alert.setCanceledOnTouchOutside(false);
                            alert.show();

                        }
                        else {
                            System.out.println("Service Started");

                            AlertDialog.Builder builder = new AlertDialog.Builder(MovementRegister.this);
                            builder
                                    .setMessage("Do you want to start Movement?")
                                    .setPositiveButton("Yes", (dialog, which) -> startLocTracker())
                                    .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

                            AlertDialog alert = builder.create();
                            alert.show();

//                                startService();
//                                nameMov.setText("END MOVEMENT");
                        }
                    }
                    else {
                        Toast.makeText(MovementRegister.this, "Please Write Movement Purpose", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(MovementRegister.this, "Please Select Movement Type", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(MovementRegister.this, "Please Select Client", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startLocTracker() {

        autoGpxLatLng = new ArrayList<>();
        lineStart = "2";
        isStart = true;
        String tt = "END MOVEMENT";
        nameMov.setText(tt);
        autoLineValue = true;
    }

    public void zoomToUserLocation(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(location -> {
//                Log.i("lattt", location.toString());
            LatLng latLng;

            if (location != null) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }
            else {
                latLng = new LatLng(23.6850, 90.3563);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
            }

        });
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void enableGPS() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(MovementRegister.this)
                    .addOnConnectionFailedListener(MovementRegister.this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                    .setWaitForAccurateLocation(false)
                    .setMinUpdateIntervalMillis(1000)
                    .setMaxUpdateDelayMillis(2000)
                    .build();
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);

            // **************************
            builder.setAlwaysShow(true); // this is the key ingredient
            // **************************

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(result1 -> {
                final Status status = result1.getStatus();
                final LocationSettingsStates state = result1
                        .getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        Log.i("Exit", "3");
                        zoomToUserLocation();

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i("Exit", "4");
                        try {
                            status.startResolutionForResult(MovementRegister.this, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.i("Exit", "5");
                        break;
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                String result = data.getStringExtra("result");
                zoomToUserLocation();
                Log.i("Hoise ", "1");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.i("Hoise ", "2");
                finish();
            }
        }
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Service.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public void startService() {

        Intent serviceIntent = new Intent(this, Service.class);
        serviceIntent.putExtra("inputExtra", 1);

        startService(serviceIntent);

    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, Service.class);
        stopService(serviceIntent);

    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    public void getData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        clientLists = new ArrayList<>();

        String clientUrl = api_url_front+"movement/getClient";
        RequestQueue requestQueue = Volley.newRequestQueue(MovementRegister.this);

        StringRequest clientReq = new StringRequest(Request.Method.GET, clientUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject depInfo = array.getJSONObject(i);
                        String ad_id = depInfo.getString("ad_id")
                                .equals("null") ? "" : depInfo.getString("ad_id");
                        String ad_name = depInfo.getString("ad_name")
                                .equals("null") ? "" : depInfo.getString("ad_name");

                        clientLists.add(new TwoItemLists(ad_id,ad_name));
                    }
                }
                connected = true;
                updateLay();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

        requestQueue.add(clientReq);
    }

    private void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < clientLists.size(); i++) {
                    type.add(clientLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                clientSpinner.setAdapter(arrayAdapter);

                enableGPS();

            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(MovementRegister.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getData();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    finish();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(MovementRegister.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}