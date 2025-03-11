package ttit.com.shuvo.eliteforce.attendance.out_att_appr;

import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_update.req_status.AttUpdateStatus;
import ttit.com.shuvo.eliteforce.attendance.att_update.req_status.adapters.StatusAdapter;
import ttit.com.shuvo.eliteforce.attendance.out_att_appr.adapters.AttendanceReqAdapter;
import ttit.com.shuvo.eliteforce.attendance.out_att_appr.arraylists.AttendanceReqList;
import ttit.com.shuvo.eliteforce.basic_model.StatusList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class AttendanceReqApprove extends AppCompatActivity {

    RecyclerView reqListView;
    RecyclerView.LayoutManager layoutManager;
    AttendanceReqAdapter attendanceReqAdapter;

    ArrayList<AttendanceReqList> attendanceReqLists;

    TextView noAttReq;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    Logger logger = Logger.getLogger(AttendanceReqApprove.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_req_approve);

        reqListView = findViewById(R.id.attendance_req_recycle_view);
        noAttReq = findViewById(R.id.no_attendance_req_found_message);
        noAttReq.setVisibility(View.GONE);

        reqListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        reqListView.setLayoutManager(layoutManager);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAttendReqList();
    }

    public void getAttendReqList() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        attendanceReqLists = new ArrayList<>();

        String url = api_url_front+"attendance/getAttendanceReqList";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceReqApprove.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject statusInfo = array.getJSONObject(i);

                        String da_id = statusInfo.getString("da_id")
                                .equals("null") ? "" : statusInfo.getString("da_id");
                        String emp_id = statusInfo.getString("emp_id")
                                .equals("null") ? "" : statusInfo.getString("emp_id");
                        String emp_code = statusInfo.getString("emp_code")
                                .equals("null") ? "" : statusInfo.getString("emp_code");
                        String emp_name = statusInfo.getString("emp_name")
                                .equals("null") ? "" : statusInfo.getString("emp_name");
                        String job_calling_title = statusInfo.getString("job_calling_title")
                                .equals("null") ? "" : statusInfo.getString("job_calling_title");
                        String da_divm_id = statusInfo.getString("da_divm_id")
                                .equals("null") ? "" : statusInfo.getString("da_divm_id");
                        String divm_name = statusInfo.getString("divm_name")
                                .equals("null") ? "" : statusInfo.getString("divm_name");
                        String da_dept_id = statusInfo.getString("da_dept_id")
                                .equals("null") ? "" : statusInfo.getString("da_dept_id");
                        String dept_name = statusInfo.getString("dept_name")
                                .equals("null") ? "" : statusInfo.getString("dept_name");
                        String osm_name = statusInfo.getString("osm_name")
                                .equals("null") ? "" : statusInfo.getString("osm_name");
                        String att_date = statusInfo.getString("att_date")
                                .equals("null") ? "" : statusInfo.getString("att_date");
                        String att_time = statusInfo.getString("att_time")
                                .equals("null") ? "" : statusInfo.getString("att_time");
                        String time_type = statusInfo.getString("time_type")
                                .equals("null") ? "" : statusInfo.getString("time_type");
                        String arm_id = statusInfo.getString("arm_id")
                                .equals("null") ? "" : statusInfo.getString("arm_id");
                        String arm_reason = statusInfo.getString("arm_reason")
                                .equals("null") ? "" : statusInfo.getString("arm_reason");
                        String arm_add_during_cause = statusInfo.getString("arm_add_during_cause")
                                .equals("null") ? "" : statusInfo.getString("arm_add_during_cause");
                        String da_attd_latitude = statusInfo.getString("da_attd_latitude")
                                .equals("null") ? "" : statusInfo.getString("da_attd_latitude");
                        String da_attd_longitude = statusInfo.getString("da_attd_longitude")
                                .equals("null") ? "" : statusInfo.getString("da_attd_longitude");
                        String att_appr = statusInfo.getString("att_appr")
                                .equals("null") ? "" : statusInfo.getString("att_appr");


                        attendanceReqLists.add(new AttendanceReqList(da_id,emp_id,emp_code, emp_name,job_calling_title,
                                da_divm_id, divm_name,da_dept_id,dept_name,osm_name,att_date,att_time,time_type,arm_id,
                                arm_reason,arm_add_during_cause,da_attd_latitude,da_attd_longitude,att_appr));
                    }
                }
                connected = true;
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
        });

        requestQueue.add(stringRequest);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                attendanceReqAdapter = new AttendanceReqAdapter(attendanceReqLists, AttendanceReqApprove.this);

                reqListView.setAdapter(attendanceReqAdapter);

                if (attendanceReqLists.isEmpty()) {
                    reqListView.setVisibility(View.GONE);
                    noAttReq.setVisibility(View.VISIBLE);
                } else {
                    reqListView.setVisibility(View.VISIBLE);
                    noAttReq.setVisibility(View.GONE);
                }

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceReqApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAttendReqList();
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
            AlertDialog dialog = new AlertDialog.Builder(AttendanceReqApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAttendReqList();
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