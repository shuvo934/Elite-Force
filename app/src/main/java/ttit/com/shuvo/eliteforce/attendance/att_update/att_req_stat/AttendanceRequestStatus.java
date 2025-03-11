package ttit.com.shuvo.eliteforce.attendance.att_update.att_req_stat;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import ttit.com.shuvo.eliteforce.attendance.att_update.att_req_stat.adapters.AttendanceReqStatusAdapter;
import ttit.com.shuvo.eliteforce.attendance.att_update.att_req_stat.arraylists.AttendanceReqStatusList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class AttendanceRequestStatus extends AppCompatActivity {

    TextView statusNot;
    RecyclerView statusView;
    AttendanceReqStatusAdapter statusAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<AttendanceReqStatusList> statusLists;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";

    Logger logger = Logger.getLogger(AttendanceRequestStatus.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_request_status);

        statusView = findViewById(R.id.att_req_status_list_view);
        statusNot = findViewById(R.id.att_req_status_not_found_msg);

        emp_id = userInfoLists.get(0).getEmp_id();
        statusLists = new ArrayList<>();

        statusView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        statusView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(statusView.getContext(),DividerItemDecoration.VERTICAL);
        statusView.addItemDecoration(dividerItemDecoration);

        getAttendReqStatus();
    }

    public void getAttendReqStatus() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        statusLists = new ArrayList<>();

        String url = api_url_front+"attendance/getEmpWiseAttReqStatus?p_emp_id="+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

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

                        String arm_id = statusInfo.getString("arm_id")
                                .equals("null") ? "" : statusInfo.getString("arm_id");
                        String arm_reason = statusInfo.getString("arm_reason")
                                .equals("null") ? "" : statusInfo.getString("arm_reason");
                        String arm_add_during_cause = statusInfo.getString("arm_add_during_cause")
                                .equals("null") ? "" : statusInfo.getString("arm_add_during_cause");
                        String att_date = statusInfo.getString("att_date")
                                .equals("null") ? "" : statusInfo.getString("att_date");
                        String att_time = statusInfo.getString("att_time")
                                .equals("null") ? "" : statusInfo.getString("att_time");
                        String time_type = statusInfo.getString("time_type")
                                .equals("null") ? "" : statusInfo.getString("time_type");
                        String arm_approved = statusInfo.getString("arm_approved")
                                .equals("null") ? "" : statusInfo.getString("arm_approved");
                        String arm_comments = statusInfo.getString("arm_comments")
                                .equals("null") ? "" : statusInfo.getString("arm_comments");
                        String approver_name = statusInfo.getString("approver_name")
                                .equals("null") ? "" : statusInfo.getString("approver_name");
                        String designation = statusInfo.getString("designation")
                                .equals("null") ? "" : statusInfo.getString("designation");


                        statusLists.add(new AttendanceReqStatusList(arm_id,arm_reason,arm_add_during_cause,
                                att_date,att_time,time_type,
                                arm_approved,arm_comments,approver_name,designation));
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
                statusAdapter = new AttendanceReqStatusAdapter(statusLists, AttendanceRequestStatus.this);

                statusView.setAdapter(statusAdapter);

                if (statusLists.isEmpty()) {
                    statusView.setVisibility(View.GONE);
                    statusNot.setVisibility(View.VISIBLE);
                } else {
                    statusView.setVisibility(View.VISIBLE);
                    statusNot.setVisibility(View.GONE);
                }

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAttendReqStatus();
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
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAttendReqStatus();
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