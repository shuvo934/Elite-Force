package ttit.com.shuvo.eliteforce.attendance.give_attendance;

import static ttit.com.shuvo.eliteforce.attendance.Attendance.live_tracking_flag;
import static ttit.com.shuvo.eliteforce.attendance.Attendance.tracking_flag;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.trackService.Service;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class AttendanceGive extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    TextView currLoc;
    TextView checkInTime;
    CardView chekInButton;
    TextView nameOfCheckIN;
    private GoogleApiClient googleApiClient;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    String inTime = "";
    String address = "";
    String emp_id = "";
    String timeKey = "last time";
    String getTime = "";
    String lat = "";
    String lon = "";
    SharedPreferences preferences;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    Timestamp ts;
    ImageView autoStartIcon;

    ActivityResultLauncher<Intent> someActivityResultLauncher;
    String officeLatitude = "";
    String officeLongitude = "";
    String coverage = "";
    String machineCode = "3";
    String last_time = "";
    String today_date = "";
    String timeToShow = "";
    TextClock digitalClock;
    TextView todayTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_give);

        currLoc = findViewById(R.id.text_of_cu_loc);
        currLoc.setVisibility(View.GONE);
        checkInTime = findViewById(R.id.check_int_time);
        chekInButton = findViewById(R.id.check_in_time_button);
        nameOfCheckIN = findViewById(R.id.name_of_punch);
        autoStartIcon = findViewById(R.id.app_auto_start_icon);
        digitalClock = findViewById(R.id.text_clock_give_att);
        todayTime = findViewById(R.id.today_date_time_give_att);
        autoStartIcon.setVisibility(View.GONE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();

        Intent intent = getIntent();
        last_time = intent.getStringExtra("LAST_TIME");
        today_date = intent.getStringExtra("TODAY_DATE");

        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(),R.font.poppins_bold);
        digitalClock.setTypeface(typeface);

        todayTime.setText(today_date);

        if (tracking_flag == 1) {

            if (isMyServiceRunning()) {
                String text = "PUNCH & STOP TRACKER";
                nameOfCheckIN.setText(text);
            }
            else {
                String text = "PUNCH & START TRACKER";
                nameOfCheckIN.setText(text);
            }
        }
        else {
            String text = "PUNCH";
            nameOfCheckIN.setText(text);
        }

        emp_id = userInfoLists.get(0).getEmp_id();

        preferences = getSharedPreferences(emp_id,MODE_PRIVATE);

        getTime = preferences.getString(timeKey,null);

        String lt = "Your last recorded time : " + last_time;
        checkInTime.setText(lt);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

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

        autoStartIcon.setOnClickListener(v -> PermissionsAll());
    }

    public void PermissionsAll() {
        final Boolean[] paise = {false};
        final Intent[] POWERMANAGER_INTENTS = {

                new Intent().setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity")),
                new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity")),
                new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity")),
                new Intent().setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.appcontrol.activity.StartupAppControlActivity")),
                new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.startupapp.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.oppo.safe", "com.oppo.safe.permission.startup.StartupAppListActivity")),
                new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity")),
                new Intent().setComponent(new ComponentName("com.iqoo.secure", "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager")),
                new Intent().setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity")),
                new Intent().setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity")),
                new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.battery.ui.BatteryActivity")),
                new Intent().setComponent(new ComponentName("com.samsung.android.lool", "com.samsung.android.sm.ui.battery.BatteryActivity")),
                new Intent().setComponent(new ComponentName("com.htc.pitroad", "com.htc.pitroad.landingpage.activity.LandingPageActivity")),
                new Intent().setComponent(new ComponentName("com.asus.mobilemanager", "com.asus.mobilemanager.MainActivity")),
                new Intent().setComponent(new ComponentName("com.transsion.phonemanager", "com.itel.autobootmanager.activity.AutoBootMgrActivity"))
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Check Auto Start Permission!")
                .setMessage("Check the App Auto Start Option is On or Off. Auto Start On will provide better solution for the service in the background.")
                .setPositiveButton("Check", (dialog, which) -> {

                    for (Intent intent : POWERMANAGER_INTENTS)
                        if (getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null) {
                            paise[0] = true;
                            someActivityResultLauncher.launch(intent);
                            break;
                        }
                    if (!paise[0]){
                        Toast.makeText(getApplicationContext(),"Could not find Auto Start Permission Settings.",Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Don't Check", (dialog, which) -> {

                });
        AlertDialog alert = builder.create();
        alert.show();


    }

    public void startService() {
        Intent serviceIntent = new Intent(this, Service.class);
        serviceIntent.putExtra("inputExtra", live_tracking_flag);
        startService(serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, Service.class);
        stopService(serviceIntent);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        final LatLng[] lastLatLongitude = {new LatLng(0, 0)};

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss aa", Locale.ENGLISH);
        SimpleDateFormat dftoShow = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {

                    Log.i("LocationFused ", location.toString());
                    lastLatLongitude[0] = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastLatLongitude[0], 18));
                    System.out.println(lastLatLongitude[0]);
                    lat = String.valueOf(lastLatLongitude[0].latitude);
                    lon = String.valueOf(lastLatLongitude[0].longitude);
                    Date c = Calendar.getInstance().getTime();
                    Date date = new Date();
                    ts = new Timestamp(date.getTime());
                    System.out.println(ts);
                    inTime = df.format(c);
                    timeToShow = dftoShow.format(c);
                    System.out.println("IN TIME : " + inTime);

                }
            }
        };

        chekInButton.setOnClickListener(v -> {
            if (!inTime.isEmpty()) {
                LatLng c_latLng = new LatLng(0,0);
                if (officeLatitude != null && officeLongitude != null) {
                    if (!officeLatitude.isEmpty() && !officeLongitude.isEmpty()) {
                        c_latLng = new LatLng(Double.parseDouble(officeLatitude),Double.parseDouble(officeLongitude));
                    }
                }
                if (c_latLng.latitude != 0 && c_latLng.longitude != 0) {
                    float[] distance = new float[1];
                    Location.distanceBetween(c_latLng.latitude,c_latLng.longitude,lastLatLongitude[0].latitude,lastLatLongitude[0].longitude,distance);

                    float radius = 0;
                    if (coverage != null) {
                        if (!coverage.isEmpty()) {
                            radius = Float.parseFloat(coverage);
                        }
                    }

                    if (radius == 0) {
                        machineCode = "3";
                    }
                    else {
                        machineCode = "1";
                    }

                    if (distance[0] <= radius || radius == 0) {
                        if (tracking_flag == 1 ) {
                            if (isMyServiceRunning()) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                                builder.setTitle("Attendance!")
                                        .setMessage("Do you want to punch & stop your tracker?")
                                        .setPositiveButton("YES", (dialog, which) -> new CheckAddress().execute())
                                        .setNegativeButton("NO", (dialog, which) -> {

                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                                builder.setTitle("Attendance!")
                                        .setMessage("Do you want to punch & start your tracker?")
                                        .setPositiveButton("YES", (dialog, which) -> new CheckAddress().execute())
                                        .setNegativeButton("NO", (dialog, which) -> {

                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                            builder.setTitle("Punch Attendance!")
                                    .setMessage("Do you want to punch now?")
                                    .setPositiveButton("YES", (dialog, which) -> new CheckAddress().execute())
                                    .setNegativeButton("NO", (dialog, which) -> {

                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"You are not around your office area",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    machineCode = "3";
                    if (tracking_flag == 1) {
                        if (isMyServiceRunning()) {

                            AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                            builder.setTitle("Attendance!")
                                    .setMessage("Do you want to punch & stop your tracker?")
                                    .setPositiveButton("YES", (dialog, which) -> new CheckAddress().execute())
                                    .setNegativeButton("NO", (dialog, which) -> {

                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                            builder.setTitle("Attendance!")
                                    .setMessage("Do you want to punch & start your tracker?")
                                    .setPositiveButton("YES", (dialog, which) -> new CheckAddress().execute())
                                    .setNegativeButton("NO", (dialog, which) -> {

                                    });
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                        builder.setTitle("Punch Attendance!")
                                .setMessage("Do you want to punch now?")
                                .setPositiveButton("YES", (dialog, which) -> new CheckAddress().execute())
                                .setNegativeButton("NO", (dialog, which) -> {

                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(),"Please wait for getting the location",Toast.LENGTH_SHORT).show();
            }
        });

        getOfficeLocation();
    }

    @Override
    public void onBackPressed() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        finish();
    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(AttendanceGive.this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (Geocoder.isPresent()) {
                assert addresses != null;
                Address obj = addresses.get(0);
                address = obj.getAddressLine(0);
                System.out.println("Ekhane ashbe 1st");
            } else {
                address = "";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            address = "";
        }
    }

    public void zoomToUserLocation(){
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
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            } else {
                latLng = new LatLng(23.6850, 90.3563);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
            }
        });

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private void enableGPS() {
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(AttendanceGive.this)
                    .addOnConnectionFailedListener(AttendanceGive.this).build();
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
                            status.startResolutionForResult(AttendanceGive.this, 1000);
                        }
                        catch (IntentSender.SendIntentException e) {
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

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Service.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                zoomToUserLocation();
                Log.i("Hoise ", "1");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("Hoise ", "2");
                finish();
            }
        }
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", Objects.requireNonNull(e.getMessage()));
        }
        return connected;
    }

    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }

        return false;
    }

    public class CheckAddress extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            waitProgress.show(getSupportFragmentManager(),"WaitBar");
            waitProgress.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {
                conn = true;
                getAddress(Double.parseDouble(lat),Double.parseDouble(lon));
            }
            else {
                conn = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (conn) {
                conn = false;
                giveAttendance();
            }
            else {
                waitProgress.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    new CheckAddress().execute();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
    }

    public void giveAttendance() {
        conn = false;
        connected = false;

        String attendaceUrl = api_url_front+"attendance/giveAttendance";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceGive.this);

        StringRequest attReq = new StringRequest(Request.Method.POST, attendaceUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    connected = true;
                }
                else {
                    System.out.println(string_out);
                    connected = false;
                }
                updateLayout();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_PUNCH_TIME",ts.toString());
                headers.put("P_MACHINE_CODE",machineCode);
                headers.put("P_LATITUDE",lat);
                headers.put("P_LONGITUDE",lon);
                headers.put("P_ADDRESS",address);
                return  headers;
            }
        };

        requestQueue.add(attReq);
    }

    private  void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                System.out.println("Ekhane Ashbe 3rd");
                checkInTime.setVisibility(View.VISIBLE);

                String ss = "Your last recorded time : "+timeToShow;
                checkInTime.setText(ss);

                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(timeKey);
                editor.putString(timeKey,ss);
                editor.apply();
                editor.commit();
                String puncher = "";
                if (address.isEmpty()) {

                    address = "No Address found for ("+lat+", "+lon+")";
                    puncher = "Punched at "+ timeToShow + " in ("+address+")";
                }
                else {
                    puncher = "Punched at "+ timeToShow + " in "+address;
                }
                currLoc.setText(puncher);
                currLoc.setVisibility(View.VISIBLE);

                if (tracking_flag == 1) {
                    if (isMyServiceRunning()) {
                        System.out.println("Service Stopped");
                        stopService();
                        String text = "PUNCH & START TRACKER";
                        nameOfCheckIN.setText(text);
                    }
                    else {
                        System.out.println("Service Started");
                        startService();
                        String text = "PUNCH & STOP TRACKER";
                        nameOfCheckIN.setText(text);
                    }
                }

                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceGive.this);
                builder
                        .setMessage("Your Attendance is Recorded!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());

                AlertDialog alert = builder.create();
                alert.show();

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    new CheckAddress().execute();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                new CheckAddress().execute();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    public void getOfficeLocation() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        String offLocationUrl = api_url_front+"attendance/getOffLatLong/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceGive.this);

        StringRequest offLocReq = new StringRequest(Request.Method.GET, offLocationUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject offLocInfo = array.getJSONObject(i);
                        officeLatitude = offLocInfo.getString("coa_latitude").equals("null") ? null : offLocInfo.getString("coa_latitude");
                        officeLongitude = offLocInfo.getString("coa_longitude").equals("null") ? null : offLocInfo.getString("coa_longitude");
                        coverage = offLocInfo.getString("coa_coverage").equals("null") ? null : offLocInfo.getString("coa_coverage");
                    }
                }
                connected = true;
                updateInfo();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        requestQueue.add(offLocReq);
    }

    private void updateInfo() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                enableGPS();

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getOfficeLocation();
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
            AlertDialog dialog = new AlertDialog.Builder(AttendanceGive.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getOfficeLocation();
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