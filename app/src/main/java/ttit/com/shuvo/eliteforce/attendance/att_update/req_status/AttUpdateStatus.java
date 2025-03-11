package ttit.com.shuvo.eliteforce.attendance.att_update.req_status;

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
import ttit.com.shuvo.eliteforce.attendance.att_update.req_status.adapters.StatusAdapter;
import ttit.com.shuvo.eliteforce.basic_model.StatusList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class AttUpdateStatus extends AppCompatActivity {

    TextView statusNot;
    RecyclerView statusView;
    StatusAdapter statusAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<StatusList> statusLists;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att_update_status);

        statusView = findViewById(R.id.status_list_view);
        statusNot = findViewById(R.id.status_not_found_msg);

        emp_id = userInfoLists.get(0).getEmp_id();
        statusLists = new ArrayList<>();

        statusView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        statusView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(statusView.getContext(),DividerItemDecoration.VERTICAL);
        statusView.addItemDecoration(dividerItemDecoration);

        getAttendStatus();
    }

    public void getAttendStatus() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        statusLists = new ArrayList<>();

        String url = api_url_front+"attendanceStatus/attStatus/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttUpdateStatus.this);

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

                        String darm_app_code = statusInfo.getString("darm_app_code")
                                .equals("null") ? "" : statusInfo.getString("darm_app_code");
                        String darm_approved = statusInfo.getString("darm_approved")
                                .equals("null") ? "" : statusInfo.getString("darm_approved");
                        String darm_date = statusInfo.getString("darm_date")
                                .equals("null") ? "" : statusInfo.getString("darm_date");
                        String darm_req_type = statusInfo.getString("darm_req_type")
                                .equals("null") ? "" : statusInfo.getString("darm_req_type");
                        String darm_update_date = statusInfo.getString("darm_update_date")
                                .equals("null") ? "" : statusInfo.getString("darm_update_date");
                        String arrival_time = statusInfo.getString("arrival_time")
                                .equals("null") ? "" : statusInfo.getString("arrival_time");
                        String departure_time = statusInfo.getString("departure_time")
                                .equals("null") ? "" : statusInfo.getString("departure_time");
                        String emp_name = statusInfo.getString("emp_name")
                                .equals("null") ? "Admin of HR" : statusInfo.getString("emp_name");


                        statusLists.add(new StatusList(darm_app_code,darm_approved,darm_date,
                                darm_req_type,darm_update_date,arrival_time,
                                departure_time,emp_name,null));
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

        requestQueue.add(stringRequest);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                statusAdapter = new StatusAdapter(statusLists, AttUpdateStatus.this);

                statusView.setAdapter(statusAdapter);

                if (statusLists.size() == 0) {
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
                AlertDialog dialog = new AlertDialog.Builder(AttUpdateStatus.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAttendStatus();
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
            AlertDialog dialog = new AlertDialog.Builder(AttUpdateStatus.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAttendStatus();
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