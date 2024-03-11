package ttit.com.shuvo.eliteforce.leave.leave_application.application_status;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.basic_model.StatusList;
import ttit.com.shuvo.eliteforce.leave.leave_application.application_status.adapters.LeaveAppStatusAdapter;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class LeaveApplicationStatus extends AppCompatActivity {

    TextView nostatus;
    RecyclerView statusView;
    LeaveAppStatusAdapter statusAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<StatusList> leaveAppStatus;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_application_status);

        emp_id = userInfoLists.get(0).getEmp_id();

        statusView = findViewById(R.id.leave_application_status_list_view);

        nostatus = findViewById(R.id.no_status_found_msg_leave);

        leaveAppStatus = new ArrayList<>();

        statusView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        statusView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(statusView.getContext(),DividerItemDecoration.VERTICAL);
        statusView.addItemDecoration(dividerItemDecoration);

        getLeaveStatus();
    }

    public void getLeaveStatus() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        leaveAppStatus = new ArrayList<>();

        String url = api_url_front+"leaveRequest/leaveReqStat/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApplicationStatus.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject leaveStatInfo = array.getJSONObject(i);

                        String la_app_code_new = leaveStatInfo.getString("la_app_code")
                                .equals("null") ? "" : leaveStatInfo.getString("la_app_code");
                        String la_approved_new = leaveStatInfo.getString("la_approved")
                                .equals("null") ? "" : leaveStatInfo.getString("la_approved");
                        String la_date_new = leaveStatInfo.getString("la_date")
                                .equals("null") ? "" : leaveStatInfo.getString("la_date");
                        String leave_type_new = leaveStatInfo.getString("leave_type")
                                .equals("null") ? "" : leaveStatInfo.getString("leave_type");
                        String la_from_date_new = leaveStatInfo.getString("la_from_date")
                                .equals("null") ? "" : leaveStatInfo.getString("la_from_date");
                        String la_to_date_new = leaveStatInfo.getString("la_to_date")
                                .equals("null") ? "" : leaveStatInfo.getString("la_to_date");
                        String la_leave_days_new = leaveStatInfo.getString("la_leave_days")
                                .equals("null") ? "" : leaveStatInfo.getString("la_leave_days");
                        String emp_name_new = leaveStatInfo.getString("emp_name")
                                .equals("null") ? "" : leaveStatInfo.getString("emp_name");
                        String canceller = leaveStatInfo.getString("emp_name_other")
                                .equals("null") ? null : leaveStatInfo.getString("emp_name_other");

                        leaveAppStatus.add(new StatusList(la_app_code_new,la_approved_new,la_date_new,
                                leave_type_new,la_from_date_new,la_to_date_new,la_leave_days_new,
                                emp_name_new,canceller));

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
                statusAdapter = new LeaveAppStatusAdapter(leaveAppStatus, LeaveApplicationStatus.this);

                statusView.setAdapter(statusAdapter);

                if (leaveAppStatus.size() == 0) {
                    statusView.setVisibility(View.GONE);
                    nostatus.setVisibility(View.VISIBLE);
                }
                else {
                    statusView.setVisibility(View.VISIBLE);
                    nostatus.setVisibility(View.GONE);
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveApplicationStatus.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getLeaveStatus();
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
            AlertDialog dialog = new AlertDialog.Builder(LeaveApplicationStatus.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getLeaveStatus();
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