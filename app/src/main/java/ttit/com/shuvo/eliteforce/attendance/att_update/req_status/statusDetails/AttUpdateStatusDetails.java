package ttit.com.shuvo.eliteforce.attendance.att_update.req_status.statusDetails;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class AttUpdateStatusDetails extends AppCompatActivity {

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    
    String emp_id = "";

    TextView status;
    TextView approver;
    TextView reqCode;

    TextInputEditText reqType;
    TextInputEditText attType;
    TextInputEditText locUpdate;
    TextInputEditText shift;
    TextInputEditText reason;
    TextInputEditText forwarder;
    TextInputEditText comm;

    CardView statusCard;

    LinearLayout approverLay;
    LinearLayout intimeLay;
    LinearLayout outTimeLay;

    TextInputEditText name;
    TextInputEditText id;
    TextInputEditText appDate;
    TextInputEditText upDate;
    TextInputEditText inTime;
    TextInputEditText outTime;
    TextInputEditText machCode;
    TextInputEditText machType;
    TextInputEditText reasonDesc;
    TextInputEditText address;

    String request = "";
    String statttt = "";
    String request_date = "";
    String update_date = "";
    String arrival= "";
    String departure = "";
    String apprrroovveerr = "";

    String request_type = "";
    String application_type = "";
    String locotion_updated = "";
    String machineCode = "";
    String machineType = "";
    String shiftName = "";
    String reasonName = "";
    String reasonDescription = "";
    String addressOut = "";
    String forwardTo = "";
    String locationId = "";
    String dateNow = "";
    String comments = "";
    
    TextView appRej;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att_update_status_details);

        status = findViewById(R.id.status_of_att_from_list);
        approver = findViewById(R.id.approver_name_by_from_list);
        reqCode = findViewById(R.id.request_code_from_list);
        reqType = findViewById(R.id.req_type_st_dt);
        attType = findViewById(R.id.att_type_st_dt);
        locUpdate = findViewById(R.id.loc_updated_st_dt);
        shift = findViewById(R.id.shift_to_be_st_dt);
        reason = findViewById(R.id.reason_type_st_dt);
        forwarder = findViewById(R.id.approver_description_st_dt);
        comm = findViewById(R.id.comments_from_approver);

        name = findViewById(R.id.name_att_st_dt);
        id = findViewById(R.id.id_att_st_dt);
        appDate = findViewById(R.id.now_date_att_st_dt);
        upDate = findViewById(R.id.date_to_be_updated_st_dt);
        inTime = findViewById(R.id.arrival_time_to_be_updated_st_dt);
        outTime = findViewById(R.id.departure_time_to_be_updated_st_dt);
        machCode = findViewById(R.id.updated_machine_code_st_dt);
        machType = findViewById(R.id.updated_machine_type_st_dt);
        reasonDesc = findViewById(R.id.reason_description_update_st_dt);
        address = findViewById(R.id.address_outside_sta_st_dt);
        
        appRej = findViewById(R.id.is_app_or_rej);

        statusCard = findViewById(R.id.status_card_from_list);

        approverLay = findViewById(R.id.approver_layout_from_list);
        intimeLay = findViewById(R.id.in_time_lay_st_dt);
        outTimeLay = findViewById(R.id.out_time_lay_st_dt);

        String firstname = userInfoLists.get(0).getUser_fname();
        String lastName = userInfoLists.get(0).getUser_lname();
        if (firstname == null) {
            firstname = "";
        }
        if (lastName == null) {
            lastName = "";
        }
        String empFullName = firstname+" "+lastName;
        name.setText(empFullName);

        id.setText(userInfoLists.get(0).getUserName());

        emp_id = userInfoLists.get(0).getEmp_id();

        Intent intent = getIntent();
        request = intent.getStringExtra("Request");
        statttt = intent.getStringExtra("Status");
        request_date = intent.getStringExtra("APP_DATE");
        update_date = intent.getStringExtra("UPDATE_DATE");
        arrival = intent.getStringExtra("ARRIVAL");
        departure = intent.getStringExtra("DEPARTURE");
        apprrroovveerr = intent.getStringExtra("APPROVER");

        reqCode.setText(request);

        switch (statttt) {
            case "PENDING":
                status.setText(statttt);
                statusCard.setCardBackgroundColor(Color.parseColor("#636e72"));
                approverLay.setVisibility(View.GONE);
                approver.setText("");
                break;
            case "APPROVED":
                status.setText(statttt);
                approverLay.setVisibility(View.VISIBLE);
                statusCard.setCardBackgroundColor(Color.parseColor("#1abc9c"));
                approver.setText(apprrroovveerr);
                String appBy = "Approved By:";
                appRej.setText(appBy);

                break;
            case "REJECTED":
                status.setText(statttt);
                statusCard.setCardBackgroundColor(Color.parseColor("#c0392b"));
                approverLay.setVisibility(View.VISIBLE);
                approver.setText(apprrroovveerr);
                String rejBy = "Rejected By:";
                appRej.setText(rejBy);
                break;
        }
        
        appDate.setText(request_date);
        upDate.setText(update_date);

        if (arrival.isEmpty()) {
            intimeLay.setVisibility(View.GONE);
        } else {
            inTime.setText(arrival);
            intimeLay.setVisibility(View.VISIBLE);
        }

        if (departure.isEmpty()) {
            outTimeLay.setVisibility(View.GONE);
        } else {
            outTime.setText(departure);
            outTimeLay.setVisibility(View.VISIBLE);
        }

        getAttendanceStatusDetails();
    }

    public void getAttendanceStatusDetails() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        request_type = "";
        application_type = "";
        reasonName = "";
        dateNow = "";
        locotion_updated = "";
        shiftName = "";
        reasonDescription = "";
        addressOut = "";
        forwardTo = "";
        locationId = "";
        comments = "";
        machineCode = "";
        machineType = "";

        String attStatUrl = api_url_front+"attendanceStatus/attStatusDetails?darm_app_code="+request+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttUpdateStatusDetails.this);

        StringRequest attStatReq = new StringRequest(Request.Method.GET, attStatUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject attStatInfo = array.getJSONObject(i);

                        request_type = attStatInfo.getString("darm_application_type")
                                .equals("null") ? "" : attStatInfo.getString("darm_application_type");
                        application_type = attStatInfo.getString("req_type")
                                .equals("null") ? "" : attStatInfo.getString("req_type");
                        reasonName = attStatInfo.getString("reason")
                                .equals("null") ? "" : attStatInfo.getString("reason");
                        dateNow = attStatInfo.getString("darm_date")
                                .equals("null") ? "" : attStatInfo.getString("darm_date");
                        locotion_updated = attStatInfo.getString("location")
                                .equals("null") ? "" : attStatInfo.getString("location");
                        shiftName = attStatInfo.getString("shift")
                                .equals("null") ? "" : attStatInfo.getString("shift");
                        reasonDescription = attStatInfo.getString("darm_reason")
                                .equals("null") ? "" : attStatInfo.getString("darm_reason");
                        addressOut = attStatInfo.getString("darm_add_during_cause")
                                .equals("null") ? null : attStatInfo.getString("darm_add_during_cause");
                        forwardTo = attStatInfo.getString("approver")
                                .equals("null") ? "Admin of HR" : attStatInfo.getString("approver");
                        locationId = attStatInfo.getString("darm_req_location_id")
                                .equals("null") ? null : attStatInfo.getString("darm_req_location_id");
                        comments = attStatInfo.getString("darm_comments")
                                .equals("null") ? null : attStatInfo.getString("darm_comments");
                    }
                }

                if (locationId != null) {
                    getMachineData();
                }
                else {
                    connected = true;
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
        });

        requestQueue.add(attStatReq);
    }

    public void getMachineData() {

        String url = api_url_front+"attendanceStatus/getMachineData/"+locationId+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttUpdateStatusDetails.this);

        StringRequest machineDataReq = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject machInfo = array.getJSONObject(i);
                        machineCode = machInfo.getString("ams_mechine_code")
                                .equals("null") ? "" : machInfo.getString("ams_mechine_code");
                        machineType = machInfo.getString("ams_attendance_type")
                                .equals("null") ? "" : machInfo.getString("ams_attendance_type");
                    }
                }
                connected = true;
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
        });

        requestQueue.add(machineDataReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                reqType.setText(request_type);
                attType.setText(application_type);
                locUpdate.setText(locotion_updated);
                machCode.setText(machineCode);
                machType.setText(machineType);
                shift.setText(shiftName);
                reason.setText(reasonName);
                reasonDesc.setText(reasonDescription);
                if (addressOut == null) {
                    String aText = "No Address Given";
                    address.setText(aText);
                } else {
                    address.setText(addressOut);
                }

                if (comments == null) {
                    comm.setText("");
                } else {
                    comm.setText(comments);
                }

                forwarder.setText(forwardTo);
                appDate.setText(dateNow);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttUpdateStatusDetails.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAttendanceStatusDetails();
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
            AlertDialog dialog = new AlertDialog.Builder(AttUpdateStatusDetails.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAttendanceStatusDetails();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }
}