package ttit.com.shuvo.eliteforce.fleet.driver_assignment;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.fleet.driver_assignment.adapters.DriverAssignmentAdapter;
import ttit.com.shuvo.eliteforce.fleet.driver_assignment.arraylists.DriverAssignmentList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class DriverAssignment extends AppCompatActivity {

    TextView noAssignment;
    RecyclerView assignmentView;
    DriverAssignmentAdapter driverAssignmentAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<DriverAssignmentList> driverAssignmentLists;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";
    String di_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_assignment);

        noAssignment = findViewById(R.id.driver_assignment_not_found_msg);
        noAssignment.setVisibility(View.GONE);
        assignmentView = findViewById(R.id.driver_assignment_list_view);

        emp_id = userInfoLists.get(0).getEmp_id();
        di_id = userInfoLists.get(0).getUsr_name();

        driverAssignmentLists = new ArrayList<>();

        assignmentView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        assignmentView.setLayoutManager(layoutManager);

        getAssignmentStatus();
    }

    public void getAssignmentStatus() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        driverAssignmentLists = new ArrayList<>();

        String url = api_url_front+"fleet_requisition/getAssignedListForDriver?p_di_id="+di_id;

        RequestQueue requestQueue = Volley.newRequestQueue(DriverAssignment.this);

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

                        String fra_pk = statusInfo.getString("fra_pk")
                                .equals("null") ? "" : statusInfo.getString("fra_pk");
                        String fra_requision_id = statusInfo.getString("fra_requision_id")
                                .equals("null") ? "" : statusInfo.getString("fra_requision_id");
                        String fra_fleet_vi_id = statusInfo.getString("fra_fleet_vi_id")
                                .equals("null") ? "" : statusInfo.getString("fra_fleet_vi_id");
                        String fr_code = statusInfo.getString("fr_code")
                                .equals("null") ? "" : statusInfo.getString("fr_code");
                        String visit_purpose = statusInfo.getString("visit_purpose")
                                .equals("null") ? "" : statusInfo.getString("visit_purpose");
                        String requester_name = statusInfo.getString("requester_name")
                                .equals("null") ? "" : statusInfo.getString("requester_name");
                        String fr_requester_mobile_no = statusInfo.getString("fr_requester_mobile_no")
                                .equals("null") ? "" : statusInfo.getString("fr_requester_mobile_no");
                        String from_location = statusInfo.getString("from_location")
                                .equals("null") ? "" : statusInfo.getString("from_location");
                        String from_date = statusInfo.getString("from_date")
                                .equals("null") ? "Not Found" : statusInfo.getString("from_date");
                        String to_location = statusInfo.getString("to_location")
                                .equals("null") ? "0" : statusInfo.getString("to_location");
                        String to_date = statusInfo.getString("to_date")
                                .equals("null") ? "" : statusInfo.getString("to_date");
                        String vh_type = statusInfo.getString("vh_type")
                                .equals("null") ? "" : statusInfo.getString("vh_type");
                        String fr_travelers_qty = statusInfo.getString("fr_travelers_qty")
                                .equals("null") ? "" : statusInfo.getString("fr_travelers_qty");
                        String year = statusInfo.getString("year")
                                .equals("null") ? "" : statusInfo.getString("year");
                        String model = statusInfo.getString("model")
                                .equals("null") ? "" : statusInfo.getString("model");
                        String name = statusInfo.getString("name")
                                .equals("null") ? "" : statusInfo.getString("name");
                        String reg_no = statusInfo.getString("reg_no")
                                .equals("null") ? "" : statusInfo.getString("reg_no");
                        String fra_driver_acknoledgement = statusInfo.getString("fra_driver_acknoledgement")
                                .equals("null") ? "" : statusInfo.getString("fra_driver_acknoledgement");
                        String fra_remarks = statusInfo.getString("fra_remarks")
                                .equals("null") ? "" : statusInfo.getString("fra_remarks");

                        driverAssignmentLists.add(new DriverAssignmentList(fra_pk,fra_requision_id,fra_fleet_vi_id,fr_code,visit_purpose,requester_name,
                                fr_requester_mobile_no,from_location,from_date,to_location,to_date,vh_type,fr_travelers_qty,
                                year,model,name,reg_no,fra_driver_acknoledgement,fra_remarks));
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
                driverAssignmentAdapter = new DriverAssignmentAdapter(driverAssignmentLists, DriverAssignment.this);
                assignmentView.setAdapter(driverAssignmentAdapter);

                if (driverAssignmentLists.isEmpty()) {
                    assignmentView.setVisibility(View.GONE);
                    noAssignment.setVisibility(View.VISIBLE);
                } else {
                    assignmentView.setVisibility(View.VISIBLE);
                    noAssignment.setVisibility(View.GONE);
                }

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(DriverAssignment.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAssignmentStatus();
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
            AlertDialog dialog = new AlertDialog.Builder(DriverAssignment.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAssignmentStatus();
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