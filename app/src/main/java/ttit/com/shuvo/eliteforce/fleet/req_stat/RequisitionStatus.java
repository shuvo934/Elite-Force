package ttit.com.shuvo.eliteforce.fleet.req_stat;

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

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.fleet.req_stat.adapters.RequisitionAdapter;
import ttit.com.shuvo.eliteforce.fleet.req_stat.arraylists.RequisitionList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class RequisitionStatus extends AppCompatActivity {

    TextView statusNot;
    RecyclerView statusView;
    RequisitionAdapter statusAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<RequisitionList> statusLists;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_status);

        statusView = findViewById(R.id.requisition_status_list_view);
        statusNot = findViewById(R.id.requisition_status_not_found_msg);

        emp_id = userInfoLists.get(0).getEmp_id();
        statusLists = new ArrayList<>();

        statusView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        statusView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(statusView.getContext(),DividerItemDecoration.VERTICAL);
        statusView.addItemDecoration(dividerItemDecoration);

        getReqStatus();
    }

    public void getReqStatus() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        statusLists = new ArrayList<>();

        String url = api_url_front+"fleet_requisition/getRequisitionStatus?p_emp_id="+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(RequisitionStatus.this);

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

                        String fr_code = statusInfo.getString("fr_code")
                                .equals("null") ? "" : statusInfo.getString("fr_code");
                        String req_stat = statusInfo.getString("req_stat")
                                .equals("null") ? "" : statusInfo.getString("req_stat");
                        String req_date = statusInfo.getString("req_date")
                                .equals("null") ? "" : statusInfo.getString("req_date");
                        String from_location = statusInfo.getString("from_location")
                                .equals("null") ? "" : statusInfo.getString("from_location");
                        String from_date = statusInfo.getString("from_date")
                                .equals("null") ? "" : statusInfo.getString("from_date");
                        String to_location = statusInfo.getString("to_location")
                                .equals("null") ? "" : statusInfo.getString("to_location");
                        String to_date = statusInfo.getString("to_date")
                                .equals("null") ? "" : statusInfo.getString("to_date");
                        String vh_type = statusInfo.getString("vh_type")
                                .equals("null") ? "Not Found" : statusInfo.getString("vh_type");
                        String fr_qty = statusInfo.getString("fr_qty")
                                .equals("null") ? "0" : statusInfo.getString("fr_qty");
                        String fr_travelers_qty = statusInfo.getString("fr_travelers_qty")
                                .equals("null") ? "0" : statusInfo.getString("fr_travelers_qty");


                        statusLists.add(new RequisitionList(fr_code,req_stat,req_date,from_location,from_date,to_location,to_date,vh_type,fr_qty,fr_travelers_qty));
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
                statusAdapter = new RequisitionAdapter(statusLists, RequisitionStatus.this);
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
                AlertDialog dialog = new AlertDialog.Builder(RequisitionStatus.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getReqStatus();
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
            AlertDialog dialog = new AlertDialog.Builder(RequisitionStatus.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getReqStatus();
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