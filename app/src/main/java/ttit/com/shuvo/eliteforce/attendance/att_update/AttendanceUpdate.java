package ttit.com.shuvo.eliteforce.attendance.att_update;

import static ttit.com.shuvo.eliteforce.login.Login.isApproved;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_update.att_req_stat.AttendanceRequestStatus;
import ttit.com.shuvo.eliteforce.attendance.att_update.new_request.AttUpNewRequest;
import ttit.com.shuvo.eliteforce.attendance.att_update.req_approve.AttUpReqApprove;
import ttit.com.shuvo.eliteforce.attendance.att_update.req_status.AttUpdateStatus;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class AttendanceUpdate extends AppCompatActivity {

    MaterialCardView attUpdate;
    MaterialCardView attStatus;
    MaterialCardView attReqStatus;
    MaterialCardView attApprove;
    RelativeLayout attApprSelectLay;
    RelativeLayout attApprCountLay;
    TextView attApprCount;
//    String approve_count = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String userName = "";
    int isApprovedCheckAgain = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_update);

        attUpdate = findViewById(R.id.atten_update_req);
        attStatus = findViewById(R.id.attendane_update_status);
        attReqStatus = findViewById(R.id.atten_req_approval_stat);
        attApprSelectLay = findViewById(R.id.att_up_req_approve_lay);
        attApprove = findViewById(R.id.atten_update_req_approval);
        attApprCountLay = findViewById(R.id.att_up_req_count_lay);
        attApprCount = findViewById(R.id.att_up_req_count_in_attendance);

        userName = userInfoLists.get(0).getUserName();

//        if (isApproved > 0) {
//            attApprSelectLay.setVisibility(View.VISIBLE);
//        } else {
//            attApprSelectLay.setVisibility(View.GONE);
//        }
        if (userInfoLists.get(0).getEmp_id().equals("88")) {
            attApprSelectLay.setVisibility(View.VISIBLE);
        }
        else {
            attApprSelectLay.setVisibility(View.GONE);
        }

        attUpdate.setOnClickListener(v -> {
                Intent intent = new Intent(AttendanceUpdate.this, AttUpNewRequest.class);
                startActivity(intent);
        });

        attStatus.setOnClickListener(v -> {
            Intent intent = new Intent(AttendanceUpdate.this, AttUpdateStatus.class);
            startActivity(intent);
        });

        attReqStatus.setOnClickListener(v -> {
            Intent intent = new Intent(AttendanceUpdate.this, AttendanceRequestStatus.class);
            startActivity(intent);
        });

        attApprove.setOnClickListener(v -> {
            Intent intent = new Intent(AttendanceUpdate.this, AttUpReqApprove.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        approvedButtonCheck();
    }

    public void approvedButtonCheck() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        attApprCountLay.setVisibility(View.GONE);
        conn = false;
        connected = false;

        isApprovedCheckAgain = 0;

        String attendanceAppUrl = api_url_front+"approval_flag/getAttendanceApproval/"+userName+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceUpdate.this);

        StringRequest attendAppReq = new StringRequest(Request.Method.GET,attendanceAppUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject attAppInfo = array.getJSONObject(i);
                        isApprovedCheckAgain = Integer.parseInt(attAppInfo.getString("val")
                                .equals("null") ? "0" : attAppInfo.getString("val"));
                    }
                }
                connected = true;
                updateLay();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLay();
            }
        },error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLay();
        });

        requestQueue.add(attendAppReq);
    }

    private void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
//                if (isApprovedCheckAgain > 0) {
//                    attApprove.setVisibility(View.VISIBLE);
//                } else {
//                    attApprove.setVisibility(View.GONE);
//                }

                if (isApprovedCheckAgain > 0) {
                    attApprCountLay.setVisibility(View.VISIBLE);
                    attApprCount.setText(String.valueOf(isApprovedCheckAgain));
                }
                else {
                    attApprCountLay.setVisibility(View.GONE);
                }

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    approvedButtonCheck();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    finish();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AttendanceUpdate.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                approvedButtonCheck();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }
}