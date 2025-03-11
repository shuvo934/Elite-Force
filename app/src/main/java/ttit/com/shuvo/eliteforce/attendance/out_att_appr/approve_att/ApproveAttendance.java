package ttit.com.shuvo.eliteforce.attendance.out_att_appr.approve_att;

import static ttit.com.shuvo.eliteforce.attendance.Attendance.tracking_flag;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.give_attendance.AttendanceGive;
import ttit.com.shuvo.eliteforce.attendance.give_attendance.arraylists.AreaLists;
import ttit.com.shuvo.eliteforce.attendance.out_att_appr.AttendanceReqApprove;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class ApproveAttendance extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    TextView empName;
    TextView empDesig;
    TextView empDepDiv;

    TextView attDateTime;
    TextView attAddress;
    TextView attDistance;
    TextView attReason;
    TextInputEditText comments;
    TextView commentError;

    Button reject;
    Button approve;

    String lat = "";
    String lng = "";
    String emp_name = "";
    String emp_desig = "";
    String emp_dep = "";
    String emp_div = "";
    String att_date = "";
    String att_time = "";
    String att_address = "";
    String att_distance = "";
    String att_reason = "";
    String arm_id = "";
    String da_id = "";
    String req_emp_id = "";
    String approve_no = "";
    String comment_text = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    ArrayList<AreaLists> areaLists;

    String emp_id = "";

    Logger logger = Logger.getLogger(ApproveAttendance.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_attendance);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.attendance_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        empName = findViewById(R.id.emp_name_att_req_approve);
        empDesig = findViewById(R.id.emp_designation_att_req_approve);
        empDepDiv = findViewById(R.id.emp_dept_div_att_req_approve);

        attDateTime = findViewById(R.id.att_time_for_att_approve);
        attAddress = findViewById(R.id.att_adds_for_att_approve);
        attDistance = findViewById(R.id.att_distance_for_att_approve);
        attReason = findViewById(R.id.reason_for_att_approve);

        comments = findViewById(R.id.comments_for_att_req_rejected);
        commentError = findViewById(R.id.error_input_reason_for_att_req_rej);
        commentError.setVisibility(View.GONE);

        reject = findViewById(R.id.att_req_reject_button);
        approve = findViewById(R.id.att_req_approve_button);

        emp_id = userInfoLists.get(0).getEmp_id();

        Intent intent = getIntent();
        lat = intent.getStringExtra("LAT");
        lng = intent.getStringExtra("LNG");
        emp_name = intent.getStringExtra("EMP_NAME");
        emp_div = intent.getStringExtra("DIVM_NAME");
        emp_desig = intent.getStringExtra("DESIG_NAME");
        emp_dep = intent.getStringExtra("DEPT_NAME");
        att_date = intent.getStringExtra("ATT_DATE");
        att_time = intent.getStringExtra("ATT_TIME");
        att_address = intent.getStringExtra("ATT_ADDS");
        att_reason = intent.getStringExtra("ATT_REASON");
        arm_id = intent.getStringExtra("ARM_ID");
        da_id = intent.getStringExtra("DA_ID");
        req_emp_id = intent.getStringExtra("EMP_ID");

        empName.setText(emp_name);
        empDesig.setText(emp_desig);
        String dpdv = emp_dep+", "+emp_div;
        empDepDiv.setText(dpdv);
        String dt = att_date+" --- "+att_time;
        attDateTime.setText(dt);
        attAddress.setText(att_address);
        attReason.setText(att_reason);

        comments.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    commentError.setVisibility(View.GONE);
                }
            }
        });

        comments.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    comments.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        approve.setOnClickListener(view -> {
            approve_no = "1";
            comment_text = Objects.requireNonNull(comments.getText()).toString();
            AlertDialog.Builder builder = new AlertDialog.Builder(ApproveAttendance.this);
            builder.setTitle("Approve Attendance!")
                    .setMessage("Do you want to approve this request?")
                    .setPositiveButton("YES", (dialog, which) -> setApproveReq(approve_no))
                    .setNegativeButton("NO", (dialog, which) -> {

                    });
            AlertDialog alert = builder.create();
            alert.show();
        });

        reject.setOnClickListener(view -> {
            approve_no = "2";
            comment_text = Objects.requireNonNull(comments.getText()).toString();
            if (comment_text.isEmpty()) {
                commentError.setVisibility(View.VISIBLE);
            }
            else {
                commentError.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(ApproveAttendance.this);
                builder.setTitle("Reject Attendance!")
                        .setMessage("Do you want to reject this request?")
                        .setPositiveButton("YES", (dialog, which) -> setApproveReq(approve_no))
                        .setNegativeButton("NO", (dialog, which) -> {

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        getOfficeLocation();

    }

    public void getOfficeLocation() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        areaLists = new ArrayList<>();

        String offLocationUrl = api_url_front+"attendance/getAreaCoverage?p_emp_id="+req_emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(ApproveAttendance.this);

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
                        String officeLatitude = offLocInfo.getString("coa_latitude").equals("null") ? "" : offLocInfo.getString("coa_latitude");
                        String officeLongitude = offLocInfo.getString("coa_longitude").equals("null") ? "" : offLocInfo.getString("coa_longitude");
                        String coverage = offLocInfo.getString("coa_coverage").equals("null") ? "" : offLocInfo.getString("coa_coverage");
                        String coa_id = offLocInfo.getString("coa_id").equals("null") ? "" : offLocInfo.getString("coa_id");

                        areaLists.add(new AreaLists(officeLatitude,officeLongitude,coverage,coa_id));
                    }
                }
                connected = true;
                updateInfo();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                connected = false;
                updateInfo();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
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

                if (!lat.isEmpty() && !lng.isEmpty()) {
                    LatLng empLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));

                    mMap.addMarker(new MarkerOptions()
                            .position(empLocation)
                            .title(att_time)
                            .snippet(att_address)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.emp_location)));

                    for (int i = 0; i < areaLists.size(); i++) {
                        mMap.addCircle(new CircleOptions()
                                .center(new LatLng(Float.parseFloat(areaLists.get(i).getLatitude()), Float.parseFloat(areaLists.get(i).getLongitude())))
                                .radius(Integer.parseInt(areaLists.get(i).getCoverage()))
                                .strokeColor(getColor(R.color.elite_red))
                                .strokeWidth(4F)
                                .fillColor(getColor(R.color.elite_grey_a)));

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Float.parseFloat(areaLists.get(i).getLatitude()), Float.parseFloat(areaLists.get(i).getLongitude())))
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.off_loc_mark)));
                    }

                    LatLng c_latLng = new LatLng(0,0);
                    LatLng destined_location = new LatLng(0,0);
                    float[] distance = new float[1];
                    float prev_distance = 0;

                    if (!areaLists.isEmpty()) {
                        for (int i = 0; i < areaLists.size(); i++) {
                            String officeLatitude = areaLists.get(i).getLatitude();
                            String officeLongitude = areaLists.get(i).getLongitude();
                            String coverage = areaLists.get(i).getCoverage();

                            if (officeLatitude != null && officeLongitude != null) {
                                if (!officeLatitude.isEmpty() && !officeLongitude.isEmpty()) {
                                    c_latLng = new LatLng(Double.parseDouble(officeLatitude),Double.parseDouble(officeLongitude));
                                }
                            }

                            if (c_latLng.latitude != 0 && c_latLng.longitude != 0) {
                                Location.distanceBetween(c_latLng.latitude,c_latLng.longitude,empLocation.latitude,empLocation.longitude,distance);

                                float radius = 0;
                                if (coverage != null) {
                                    if (!coverage.isEmpty()) {
                                        radius = Float.parseFloat(coverage);
                                    }
                                }

                                if (distance[0] > radius) {
                                    float dd = distance[0] - radius;
                                    if (prev_distance == 0) {
                                        prev_distance = dd;
                                        destined_location = c_latLng;
                                    }
                                    else if (dd < prev_distance) {
                                        prev_distance = dd;
                                        destined_location = c_latLng;
                                    }
                                }
                            }
                        }

                        att_distance = String.valueOf(Math.round(prev_distance));
                        String ad = att_distance +" Meters";
                        attDistance.setText(ad);

                        PolylineOptions option = new PolylineOptions()
                                .width(17)
                                .color(Color.parseColor("#d63031"))
                                .geodesic(true)
                                .clickable(true)
                                .zIndex(1);

                        option.add(empLocation);
                        option.add(destined_location);

                        mMap.addPolyline(option);

                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(empLocation, 17));
                    }
                }

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(ApproveAttendance.this)
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
            AlertDialog dialog = new AlertDialog.Builder(ApproveAttendance.this)
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

    public void setApproveReq(String approveDisApprove) {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        String attendaceUrl = api_url_front+"attendance/approveAttReq";

        RequestQueue requestQueue = Volley.newRequestQueue(ApproveAttendance.this);

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
                logger.log(Level.WARNING,e.getMessage(),e);
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            conn = false;
            connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_APPROVE",approveDisApprove);
                headers.put("P_ARM_ID",arm_id);
                headers.put("P_COMMENT", comment_text);
                return  headers;
            }
        };

        requestQueue.add(attReq);
    }

    private  void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {

                if (approve_no.equals("1")) {
                    Toast.makeText(this, "Attendance Approved Successfully", Toast.LENGTH_SHORT).show();
                }
                else if (approve_no.equals("2")) {
                    Toast.makeText(this, "Attendance Rejected Successfully", Toast.LENGTH_SHORT).show();
                }
                finish();

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(ApproveAttendance.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    setApproveReq(approve_no);
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(ApproveAttendance.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                setApproveReq(approve_no);
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}