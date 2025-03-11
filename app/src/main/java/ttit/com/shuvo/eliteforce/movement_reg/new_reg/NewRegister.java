package ttit.com.shuvo.eliteforce.movement_reg.new_reg;

import static ttit.com.shuvo.eliteforce.attendance.Attendance.tracking_flag;
import static ttit.com.shuvo.eliteforce.attendance.trackService.model.DistanceCalculator.CalculationByDistance;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.give_attendance.AttendanceGive;
import ttit.com.shuvo.eliteforce.attendance.trackService.Service;
import ttit.com.shuvo.eliteforce.attendance.trackService.model.LatLngTimeList;
import ttit.com.shuvo.eliteforce.basic_model.TwoItemLists;
import ttit.com.shuvo.eliteforce.movement_reg.interfaces.TimerFinishedListener;
import ttit.com.shuvo.eliteforce.utility.TimerProgress;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class NewRegister extends AppCompatActivity implements OnMapReadyCallback, TimerFinishedListener {

    private GoogleMap mMap;
    TextView todayDate;
    String today_date = "";

    TextView startLocationText;
    TextView endLocationText;
    String st_loc_text = "";
    String end_loc_text = "";

    TextInputLayout clientSpinnerLay;
    AutoCompleteTextView clientSpinner;
    String selected_ad_id = "";
    String selected_client_name = "";
    ArrayList<TwoItemLists> clientLists;
    TextView clientError;

    TextInputLayout typeSpinnerLay;
    AmazingSpinner typeSpinner;
    String selected_type = "";
    ArrayList<TwoItemLists> typeLists;
    TextView typeMissingMsg;

    TextInputLayout movementPurposeLay;
    TextInputEditText movementPurpose;
    String mov_purpose = "";
    TextView movePurposeMissingMsg;

    TextInputLayout carryAmntLay;
    TextInputEditText carryAmnt;
    String carry_amnt = "";

    LinearLayout driverLay;

    TextView reqToken;
    String req_token = "";

    TextView vehicleName;
    String vhl_name = "";

    MaterialCardView movementButton;
    TextView nameMov;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    String emp_id = "";
    String usr_name = "";
    ActivityResultLauncher<Intent> someActivityResultLauncher;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    boolean selectedFromItems = false;

    private String lineStart = "0";
    private Boolean isStart = false;
    private Boolean autoLineValue = false;
    public String length_multi = "";
    public ArrayList<String> trk;
    public final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";
    public final String TAG_GPX = "<gpx"
            + " xmlns=\"http://www.topografix.com/GPX/1/1\""
            + " version=\"1.1\""
            + " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\""
            + " xsi:schemaLocation=\"http://www.topografix.com/GPX/1/1 http://www.topografix.com/GPX/1/1/gpx.xsd\">";

    public String gpxContent = "";
    Location lastLocation = null;
    final int[] local = {0};
    final int[] localLive = {0};
    final LatLng[] autoPreLatlng = {new LatLng(0, 0)};
    final Double[] w = {0.0};
    final PolylineOptions[] nop = {new PolylineOptions().width(5).color(Color.RED).geodesic(true)};
    MarkerOptions mp = new MarkerOptions();
    String lastTime = "";
    double lastDistance = 0.0;
    private final int ONE_MINUTES = 1000 * 60;
    public Location previousBestLocation = null;
    public Location firstLocation = null;
    public ArrayList<LatLngTimeList> locationLists;
    LatLng lastlatLng = null;
    String lat = "";
    String lon = "";

    String di_id = "";
    String vi_id = "";
    String requi_id = "";
    String distance = "";
    String totalTime = "";
    String stoppedTime = "";
    String fra_pk = "";
    String out_fmr_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_register);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        todayDate = findViewById(R.id.today_date_time_movement_reg);

        startLocationText = findViewById(R.id.start_location_movement);
        endLocationText = findViewById(R.id.end_location_movement);

        clientSpinnerLay = findViewById(R.id.spinner_layout_movement_client);
        clientSpinnerLay.setEnabled(true);
        clientSpinner = findViewById(R.id.movement_client_spinner);
        clientError = findViewById(R.id.movement_client_error_msg);
        clientError.setVisibility(View.GONE);

        typeSpinnerLay = findViewById(R.id.spinner_layout_movement_type);
        typeSpinnerLay.setEnabled(true);
        typeSpinner = findViewById(R.id.movement_type_spinner);
        typeMissingMsg = findViewById(R.id.movement_type_missing_msg);
        typeMissingMsg.setVisibility(View.GONE);

        movementPurposeLay = findViewById(R.id.movement_purpose_layout);
        movementPurposeLay.setEnabled(true);
        movementPurpose = findViewById(R.id.movement_purpose_text);
        movePurposeMissingMsg = findViewById(R.id.movement_purpose_missing_msg);
        movePurposeMissingMsg.setVisibility(View.GONE);

        carryAmntLay = findViewById(R.id.carry_amount_movement_layout);
        carryAmntLay.setEnabled(true);
        carryAmnt = findViewById(R.id.carry_amount_movement);

        driverLay = findViewById(R.id.for_drivers_layout);
        driverLay.setVisibility(View.GONE);
        reqToken = findViewById(R.id.requisition_token_for_movement);
        vehicleName = findViewById(R.id.vehicle_name_for_movement);

        movementButton = findViewById(R.id.movement_start_end_button);
        nameMov = findViewById(R.id.name_of_movement);

        Intent intent = getIntent();
        int fl = intent.getIntExtra("FROM_REQ",0);
        if (fl == 0) {
            driverLay.setVisibility(View.GONE);
        }
        else {
            requi_id = intent.getStringExtra("REQ_ID");
            vi_id = intent.getStringExtra("VI_ID");
            vhl_name = intent.getStringExtra("VI_NAME");
            req_token = intent.getStringExtra("REQ_TOKEN");
            fra_pk = intent.getStringExtra("FRA_PK");
            di_id = userInfoLists.get(0).getUsr_name();
            driverLay.setVisibility(View.VISIBLE);
            reqToken.setText(req_token);
            vehicleName.setText(vhl_name);
        }

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
        usr_name = userInfoLists.get(0).getUsr_name();

        clientLists = new ArrayList<>();
        typeLists = new ArrayList<>();

        typeLists.add(new TwoItemLists("1","CIT Service"));
        typeLists.add(new TwoItemLists("2","Guard Service"));
        typeLists.add(new TwoItemLists("3","Visitor Receive"));
        typeLists.add(new TwoItemLists("4","Others"));

        ArrayList<String> type = new ArrayList<>();
        for(int i = 0; i < typeLists.size(); i++) {
            type.add(typeLists.get(i).getName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

        typeSpinner.setAdapter(arrayAdapter);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
        today_date = simpleDateFormat.format(calendar.getTime());
        todayDate.setText(today_date);

        clientSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    clientError.setVisibility(View.GONE);
                }
            }
        });

        clientSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            selected_ad_id = "";
            selected_client_name = "";
            for (int i = 0; i <clientLists.size(); i++) {
                if (name.equals(clientLists.get(i).getName())) {
                    selected_ad_id = clientLists.get(i).getId();
                    selected_client_name = clientLists.get(i).getName();
                }
            }

            selectedFromItems = true;
            clientError.setVisibility(View.GONE);
            closeKeyBoard();
        });

        clientSpinner.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String ss = clientSpinner.getText().toString();
                if (!selectedFromItems) {
                    selected_ad_id = "";
                    for (int i = 0; i < clientLists.size(); i++) {
                        if (ss.equals(clientLists.get(i).getName())) {
                            selected_ad_id = clientLists.get(i).getId();
                            selected_client_name = clientLists.get(i).getName();
                        }
                    }
                    if (selected_ad_id.isEmpty()) {
                        if (ss.isEmpty()) {
                            clientError.setVisibility(View.GONE);
                        }
                        else {
                            clientError.setVisibility(View.VISIBLE);
                        }
                    }
                }
                else {
                    selectedFromItems = false;
                }
            }
        });

        clientSpinner.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    clientSpinner.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
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
            typeMissingMsg.setVisibility(View.GONE);
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

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isStart) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(NewRegister.this);
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
        });

        getData();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (isStart) {
                        Log.i("LocationFused ", location.toString());
//                        lastLatLongitude[0] = new LatLng(location.getLatitude(), location.getLongitude());
                        lastLocation = location;
                        if (local[0] == 0) {

                            local[0]++;
                            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                            autoPreLatlng[0] = ll;
                            length_multi = (String.format(Locale.ENGLISH,"%.3f", w[0]) + " KM");

                            nop[0].add(ll);
                            mMap.addPolyline(nop[0]);
                            mp.position(ll);
                            mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
                            mp.anchor((float) 0.5, (float) 0.5);
                            mp.snippet("0 KM");
                            mp.title("Starting Point");
                            mMap.addMarker(mp);

                            firstLocation = location;
                            Date date = Calendar.getInstance().getTime();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
                            String time = simpleDateFormat.format(date);
                            locationLists.add(new LatLngTimeList(ll,time));

                            startGetAddress(ll.latitude,ll.longitude);
                        }
                        else {

                            local[0]++;
                            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                            Double distance = CalculationByDistance(autoPreLatlng[0], ll);

                            Date date = Calendar.getInstance().getTime();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
                            String time = simpleDateFormat.format(date);
                            Log.i("Distance", distance.toString());

                            long timeData = location.getTime() - firstLocation.getTime();
                            double hour = (double) timeData / (ONE_MINUTES * 60);
                            int speed = (int) (distance / hour);
                            System.out.println("SPEED FOR GPX: " + speed + " KM/H");
                            firstLocation = location;

                            lastlatLng = ll;
                            lastTime = time;
                            lastDistance = distance;

                            if (distance >= 0.03) {

                                nop[0].add(ll);
                                mMap.addPolyline(nop[0]);
                                mp.position(ll);
                                mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
                                mp.anchor((float) 0.5, (float) 0.5);
                                mp.snippet(String.format(Locale.ENGLISH,"%.3f", w[0]) + " KM");
                                mp.title("Road Point");
                                mMap.addMarker(mp);

                                w[0] = w[0] + distance;
                                autoPreLatlng[0] = ll;
                                length_multi = (String.format(Locale.ENGLISH,"%.3f", w[0]) + " KM");

                                locationLists.add(new LatLngTimeList(ll,time));
                            }

                        }

                        if (!vi_id.isEmpty()) {
                            if (localLive[0] == 0) {

                                localLive[0]++;

                                System.out.println("LOCATION FIRST: "+ location);
                                previousBestLocation = location;
                                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

                                String lat = String.valueOf(location.getLatitude());
                                String lon = String.valueOf(location.getLongitude());
                                String spd = "0 KM/H";
                                String add = getAddress(ll.latitude,ll.longitude);
                                String acc = String.valueOf(location.getAccuracy());
                                String bear = String.valueOf(location.getBearing());
                                UpdateLocation(lat,lon,spd,add,acc,bear,"0");


                            }
                            else {
                                if (previousBestLocation != null) {

                                    localLive[0]++;
                                    long timeData = location.getTime() - previousBestLocation.getTime();
                                    if (timeData >= ONE_MINUTES) {

                                        System.out.println("LOCATION OTHER: "+ location);
                                        LatLng prell = new LatLng(previousBestLocation.getLatitude(), previousBestLocation.getLongitude());
                                        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                                        double distance = CalculationByDistance(prell, ll);
                                        System.out.println("DISTANCE: "+ distance);
                                        System.out.println("TIME DATA: " + timeData);
                                        double hour = (double) timeData / (ONE_MINUTES * 60);
                                        System.out.println("HOUR: "+ hour);
                                        int speed = (int) (distance / hour);
                                        System.out.println("SPEED: " + speed + " KM/H");
                                        System.out.println("BEARING: " + String.valueOf(location.getBearing()));
                                        previousBestLocation = location;

                                        String lat = String.valueOf(location.getLatitude());
                                        String lon = String.valueOf(location.getLongitude());
                                        String spd = String.valueOf(speed) + " KM/H";
                                        String add = getAddress(ll.latitude,ll.longitude);
                                        String acc = String.valueOf(location.getAccuracy());
                                        String bear = String.valueOf(location.getBearing());

                                        UpdateLocation(lat,lon,spd,add,acc,bear,"0");
                                    }
                                }
                            }
                        }

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()), 18));
                    }
                }
            }
        };

        movementButton.setOnClickListener(v -> {
            String ss = clientSpinner.getText().toString();
            mov_purpose = Objects.requireNonNull(movementPurpose.getText()).toString();
            carry_amnt = Objects.requireNonNull(carryAmnt.getText()).toString();
            if (ss.isEmpty() && selected_ad_id.isEmpty()) {
                if (!selected_type.isEmpty()) {
                    if (!mov_purpose.isEmpty()) {
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewRegister.this);
                            if (isStart) {
                                builder
                                        .setMessage("Do you want to End Movement?")
                                        .setPositiveButton("Yes", (dialog, which) -> {
                                            dialog.dismiss();
                                            TimerProgress timerProgress = new TimerProgress();
                                            timerProgress.show(getSupportFragmentManager(),"Timer");
                                        })
                                        .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else {
                                builder
                                        .setMessage("Do you want to Start Movement?")
                                        .setPositiveButton("Yes", (dialog, which) -> {
                                            dialog.dismiss();
                                            TimerProgress timerProgress = new TimerProgress();
                                            timerProgress.show(getSupportFragmentManager(),"Timer");
                                        })
                                        .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewRegister.this);
                            builder
                                    .setMessage("You need to Turn On your GPS location to use this service. Please Turn On your GPS Location")
                                    .setPositiveButton("OK", (dialog, which) -> gpsCheck())
                                    .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                    else {
                        movePurposeMissingMsg.setVisibility(View.VISIBLE);
                        Toast.makeText(NewRegister.this, "Please Write Movement Purpose", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    typeMissingMsg.setVisibility(View.VISIBLE);
                    Toast.makeText(NewRegister.this, "Please Select Movement Type", Toast.LENGTH_SHORT).show();
                }
            }
            else if (!ss.isEmpty() && selected_ad_id.isEmpty()) {
                Toast.makeText(NewRegister.this, "Invalid Client", Toast.LENGTH_SHORT).show();
                clientError.setVisibility(View.VISIBLE);
            }
            else {
                if (!selected_type.isEmpty()) {
                    if (!mov_purpose.isEmpty()) {
                        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewRegister.this);
                            if (isStart) {
                                builder
                                        .setMessage("Do you want to End Movement?")
                                        .setPositiveButton("Yes", (dialog, which) -> {
                                            dialog.dismiss();
                                            TimerProgress timerProgress = new TimerProgress();
                                            timerProgress.show(getSupportFragmentManager(),"Timer");
                                        })
                                        .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                            else {
                                builder
                                        .setMessage("Do you want to Start Movement?")
                                        .setPositiveButton("Yes", (dialog, which) -> {
                                            dialog.dismiss();
                                            TimerProgress timerProgress = new TimerProgress();
                                            timerProgress.show(getSupportFragmentManager(),"Timer");
                                        })
                                        .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                        else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(NewRegister.this);
                            builder
                                    .setMessage("You need to Turn On your GPS location to use this service. Please Turn On your GPS Location")
                                    .setPositiveButton("OK", (dialog, which) -> gpsCheck())
                                    .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    }
                    else {
                        movePurposeMissingMsg.setVisibility(View.VISIBLE);
                        Toast.makeText(NewRegister.this, "Please Write Movement Purpose", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    typeMissingMsg.setVisibility(View.VISIBLE);
                    Toast.makeText(NewRegister.this, "Please Select Movement Type", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public String getAddress(double lat, double lng) {
        String adds;
        Geocoder geocoder = new Geocoder(NewRegister.this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (Geocoder.isPresent()) {
                if (addresses != null && !addresses.isEmpty()) {
                    Address obj = addresses.get(0);
                    adds = obj.getAddressLine(0);
                }
                else {
                    adds = "";
                }
                System.out.println("Ekhane ashbe 1st");
            } else {
                adds = "";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            adds = "";
        }
        return adds;
    }

    public void startGetAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(NewRegister.this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (Geocoder.isPresent()) {
                if (addresses != null && !addresses.isEmpty()) {
                    Address obj = addresses.get(0);
                    st_loc_text = obj.getAddressLine(0);
                }
                else {
                    st_loc_text = "";
                }
                System.out.println("Ekhane ashbe 1st");
            } else {
                st_loc_text = "";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            st_loc_text = "";
        }
        startLocationText.setText(st_loc_text);
    }

    public void endGetAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(NewRegister.this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (Geocoder.isPresent()) {
                if (addresses != null && !addresses.isEmpty()) {
                    Address obj = addresses.get(0);
                    end_loc_text = obj.getAddressLine(0);
                }
                else {
                    end_loc_text = "";
                }
                System.out.println("Ekhane ashbe 1st");
            } else {
                end_loc_text = "";
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            end_loc_text = "";
        }
    }

    @Override
    public void onTimeFinished() {
        if (isStart) {
            stopLocTracker();
        }
        else {
            startLocTracker();
        }
    }

    private void startLocTracker() {
        clientSpinnerLay.setEnabled(false);
        typeSpinnerLay.setEnabled(false);
        movementPurposeLay.setEnabled(false);
        carryAmntLay.setEnabled(false);
        locationLists = new ArrayList<>();
        lineStart = "2";
        String tt = "END MOVEMENT";
        nameMov.setText(tt);
        autoLineValue = true;
        length_multi = "";
        trk = new ArrayList<>();
        gpxContent = "";
        local[0] = 0;
        localLive[0] = 0;
        w[0] = 0.0;
        lastDistance = 0.0;
        lastlatLng = null;
        lastTime = "";
        autoPreLatlng[0] = new LatLng(0, 0);
        isStart = true;
    }

    private void stopLocTracker() {
        if (locationLists.size() > 1) {

            if (lastlatLng != null) {
                locationLists.add(new LatLngTimeList(lastlatLng,lastTime));
                w[0] = w[0] + lastDistance;
                length_multi = (String.format(Locale.ENGLISH,"%.3f", w[0]) + " KM");
            }

            distance = String.format(Locale.ENGLISH,"%.3f", w[0]);

            String fTime = locationLists.get(0).getTime();
            String lTime = locationLists.get(locationLists.size()-1).getTime();

            SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);

            Date first = null;
            Date last = null;

            try {
                first = sdfTime.parse(fTime);
                last = sdfTime.parse(lTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            if (first != null && last != null) {
                long millis =  last.getTime() - first.getTime();
                //System.out.println("Mili second: "+millis);
                totalTime = String.valueOf(millis);
            }

            long millissss = 0;
            for (int l = 0; l <locationLists.size(); l++) {
                if (l != locationLists.size() - 1) {
                    String oneTime = locationLists.get(l).getTime();
                    String twoTime = locationLists.get(l+1).getTime();

                    Date first_time = null;
                    Date second_time = null;

                    try {
                        first_time = sdfTime.parse(oneTime);
                        second_time = sdfTime.parse(twoTime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (first_time != null && second_time != null) {
                        long millis =  second_time.getTime() - first_time.getTime();
                        int hours = (int) (millis / (1000 * 60 * 60));
                        int mins = (int) ((millis / (1000 * 60)) % 60);

                        if (hours != 0 || mins >= 5) {
                            System.out.println("5 min er beshi");
                            millissss = millissss + millis;
                        }
                    }
                }
            }

            stoppedTime = String.valueOf(millissss);

            String start = "\t<trk>\n" +
                    "\t\t<name>TTIT</name>\n";
            String desc = "\t\t<desc>Length: " + length_multi + "</desc>\n";
            String trkseg = "\t\t<trkseg>\n";
            String trkpt = "";
            for (int b = 0; b < locationLists.size(); b++) {
                Log.i("Latlng :", locationLists.get(b).toString());
                trkpt += "\t\t\t<trkpt lat=\"" + locationLists.get(b).getLatLng().latitude + "\" lon=\"" + locationLists.get(b).getLatLng().longitude + "\">\n"+
                        "\t\t\t\t<time>"+locationLists.get(b).getTime()+"</time>\n"+
                        "\t\t\t</trkpt>\n";
            }
            String trksegFinish = "\t\t</trkseg>\n";
            String finish = "\t</trk>\n";

            trk.add(start + desc + trkseg + trkpt + trksegFinish + finish);

        }
        else if (locationLists.size() == 1) {
            for (int i = 0; i < locationLists.size(); i++) {

                String wpt = "\t<wpt lat=\""+ locationLists.get(i).getLatLng().latitude +"\" lon=\""+ locationLists.get(i).getLatLng().longitude+"\">\n" +
                        "\t\t<name>TTIT</name>\n" +
                        "\t\t<time>"+locationLists.get(i).getTime()+"</time>\n"+
                        "\t</wpt>";
                trk.add(wpt);
            }
        }

        LatLng last_Latlng;
        if (lastlatLng != null) {
            last_Latlng = lastlatLng;
        }
        else {
            last_Latlng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
        lat = String.valueOf(last_Latlng.latitude);
        lon = String.valueOf(last_Latlng.longitude);

        nop[0].add(last_Latlng);
        mMap.addPolyline(nop[0]);
        mp.position(last_Latlng);
        mp.icon(BitmapDescriptorFactory.fromResource(R.drawable.circle));
        mp.anchor((float) 0.5, (float) 0.5);
        mp.snippet(String.format(Locale.ENGLISH,"%.3f", w[0]) + " KM");
        mp.title("End Point");
        mMap.addMarker(mp);

        gpxContent = XML_HEADER + "\n" + TAG_GPX + "\n";
        for (int i = 0; i < trk.size(); i++) {
            gpxContent = gpxContent + "\n" + trk.get(i);
        }
        gpxContent = gpxContent + "\n</gpx>";

        if (!vi_id.isEmpty()) {
            long timeData = lastLocation.getTime() - previousBestLocation.getTime();
            LatLng prell = new LatLng(previousBestLocation.getLatitude(), previousBestLocation.getLongitude());
            LatLng ll = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            double distance = CalculationByDistance(prell, ll);
            double hour = (double) timeData / (ONE_MINUTES * 60);
            int speed = (int) (distance / hour);

            String lat = String.valueOf(lastLocation.getLatitude());
            String lon = String.valueOf(lastLocation.getLongitude());
            String spd = String.valueOf(speed) + " KM/H";
            String add = getAddress(ll.latitude,ll.longitude);
            String acc = String.valueOf(lastLocation.getAccuracy());
            String bear = String.valueOf(lastLocation.getBearing());

            UpdateLocation(lat,lon,spd,add,acc,bear,"1");
        }

        new CheckAddress().execute();
    }

    public void zoomToUserLocation(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (!mMap.isMyLocationEnabled()) {
            mMap.setMyLocationEnabled(true);
        }
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

    private void newEnableGps() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> zoomToUserLocation());

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(NewRegister.this,
                            1000);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });

    }

    private void gpsCheck() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {
            Toast.makeText(getApplicationContext(), "Please wait a second to get your location.", Toast.LENGTH_SHORT).show();
        });

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(NewRegister.this,
                            1111);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });

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
        else if (requestCode == 1111) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Please wait a second to get your location.", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.i("Hoise ", "2");
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
        RequestQueue requestQueue = Volley.newRequestQueue(NewRegister.this);

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

                newEnableGps();

            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(NewRegister.this)
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
            AlertDialog dialog = new AlertDialog.Builder(NewRegister.this)
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
                endGetAddress(Double.parseDouble(lat),Double.parseDouble(lon));
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
                endLocationText.setText(end_loc_text);
                movementDataProcess();
            }
            else {
                waitProgress.dismiss();
                AlertDialog dialog = new AlertDialog.Builder(NewRegister.this)
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

    public void movementDataProcess() {
        conn = false;
        connected = false;

        String moveDataUrl = api_url_front+"movement/insertMovement";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        byte[] bArray = null;
        if (!gpxContent.isEmpty()) {
            bArray = gpxContent.getBytes();
        }

        byte[] finalBArray = bArray;

        StringRequest moveReq = new StringRequest(Request.Method.POST, moveDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                out_fmr_id = jsonObject.getString("out_fmr_id");
                if (string_out.equals("Successfully Created")) {
                    connected = true;
                    if (fra_pk.isEmpty()) {
                        updateLayout();
                    }
                    else {
                        fleetAssignment();
                    }
                }
                else {
                    System.out.println(string_out);
                    connected = false;
                    updateLayout();
                }
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
            public byte[] getBody() throws AuthFailureError {
                return finalBArray;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_AD_ID",selected_ad_id);
                headers.put("P_DRIVER_DI_ID",di_id);
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_ENDING_LOCATION",end_loc_text);
                headers.put("P_MOVEMENT_DETAILS",mov_purpose);
                headers.put("P_MOVEMENT_TYPE",selected_type);
                headers.put("P_STARTING_LOCATOIN",st_loc_text);
                headers.put("P_VI_ID",vi_id);
                headers.put("P_USER",usr_name);
                headers.put("P_REQUI_ID",requi_id);
                headers.put("P_TOT_DISTANCE",distance);
                headers.put("P_TOT_TIME",totalTime);
                headers.put("P_TOT_STOP_TIME",stoppedTime);
                headers.put("P_CARRY_AMNT",carry_amnt);
                return  headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/binary";
            }
        };

        requestQueue.add(moveReq);
    }

    public void fleetAssignment() {
        conn = false;
        connected = false;

        String fleetUrl = api_url_front+"movement/updateFleetAssignment";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest moveReq = new StringRequest(Request.Method.POST, fleetUrl, response -> {
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
                headers.put("P_MOVE_ID",out_fmr_id);
                headers.put("P_FRA",fra_pk);
                return  headers;
            }
        };

        requestQueue.add(moveReq);
    }

    private  void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                String tt = "START MOVEMENT";
                nameMov.setText(tt);
                clientSpinnerLay.setEnabled(true);
                typeSpinnerLay.setEnabled(true);
                movementPurposeLay.setEnabled(true);
                carryAmntLay.setEnabled(true);

                local[0] = 0;
                localLive[0] = 0;
                autoLineValue = false;
                lineStart = "0";
                isStart = false;

                nop[0] = new PolylineOptions().width(5).color(Color.RED).geodesic(true);

                w[0] = 0.0;
                local[0] = 0;
                localLive[0] = 0;
                autoPreLatlng[0] = new LatLng(0, 0);
                lastLocation = null;

                AlertDialog.Builder builder = new AlertDialog.Builder(NewRegister.this);
                builder.setTitle("Success!")
                        .setMessage("Movement Successfully Registered.")
                        .setPositiveButton("OK", (dialog, which) -> finish());
                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.setCanceledOnTouchOutside(false);
                alert.show();

            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(NewRegister.this)
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
            AlertDialog dialog = new AlertDialog.Builder(NewRegister.this)
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

    public void UpdateLocation(String latitude,String longitude, String spd, String add, String acc, String bear, String isStopped) {

        String moveDataUrl = api_url_front+"movement/updateLiveLocation";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest moveReq = new StringRequest(Request.Method.POST, moveDataUrl, response -> {
            System.out.println("Update Done");
        }, error -> {
            System.out.println(error.getLocalizedMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("p_empid",emp_id);
                headers.put("p_lat",latitude);
                headers.put("p_long",longitude);
                headers.put("p_speed",spd);
                headers.put("p_address",add);
                headers.put("p_accuracy",acc);
                headers.put("p_bearing",bear);
                headers.put("p_di_id",di_id);
                headers.put("p_vi_id",vi_id);
                headers.put("p_is_stopped",isStopped);
                return headers;
            }
        };

        requestQueue.add(moveReq);
    }
}