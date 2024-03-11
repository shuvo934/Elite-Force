package ttit.com.shuvo.eliteforce.leave.leave_application;

import static ttit.com.shuvo.eliteforce.login.Login.isLeaveApproved;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.leave.leave_application.application_status.LeaveApplicationStatus;
import ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.LeaveApprove;
import ttit.com.shuvo.eliteforce.leave.leave_application.new_application.NewLeaveApplication;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class LeaveApplication extends AppCompatActivity {
    MaterialCardView newApp;
    MaterialCardView appStat;
    MaterialCardView leaveApprove;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String user_id = "";
    int isLeaveApprovedCheck = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application);

        newApp = findViewById(R.id.leave_application_new_app);
        appStat = findViewById(R.id.leave_application_status_show);
        leaveApprove = findViewById(R.id.leave_req_approval);

        if (isLeaveApproved > 0) {
            leaveApprove.setVisibility(View.VISIBLE);
        } else {
            leaveApprove.setVisibility(View.GONE);
        }

        user_id = userInfoLists.get(0).getUserName();

        newApp.setOnClickListener(v -> {
            Intent intent = new Intent(LeaveApplication.this, NewLeaveApplication.class);
            startActivity(intent);
        });

        appStat.setOnClickListener(v -> {
            Intent intent = new Intent(LeaveApplication.this, LeaveApplicationStatus.class);
            startActivity(intent);
        });

        leaveApprove.setOnClickListener(v -> {
            Intent intent = new Intent(LeaveApplication.this, LeaveApprove.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLeaveApproveButtonCheck();
    }

    public void getLeaveApproveButtonCheck() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        isLeaveApprovedCheck = 0;

        String leaveAppUrl = api_url_front +"approval_flag/getLeaveApproval/"+user_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApplication.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, leaveAppUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);
                        isLeaveApprovedCheck = Integer.parseInt(info.getString("l_val")
                                .equals("null") ? "0" : info.getString("l_val"));
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

        requestQueue.add(stringRequest);
    }

    private void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (isLeaveApprovedCheck > 0) {
                    leaveApprove.setVisibility(View.VISIBLE);
                } else {
                    leaveApprove.setVisibility(View.GONE);
                }

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveApplication.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getLeaveApproveButtonCheck();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(LeaveApplication.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getLeaveApproveButtonCheck();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}