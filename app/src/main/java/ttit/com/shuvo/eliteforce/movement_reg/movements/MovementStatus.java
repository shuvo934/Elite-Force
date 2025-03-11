package ttit.com.shuvo.eliteforce.movement_reg.movements;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.os.Bundle;
import android.util.Base64;
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

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;

import oracle.jdbc.rowset.OracleSerialBlob;
import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.movement_reg.movements.adapters.MovementStatusAdapter;
import ttit.com.shuvo.eliteforce.movement_reg.movements.arraylists.MovementList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class MovementStatus extends AppCompatActivity {

    TextView statusNot;
    RecyclerView statusView;
    MovementStatusAdapter statusAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<MovementList> statusLists;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";
    String di_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_status);

        statusView = findViewById(R.id.movement_status_list_view);
        statusNot = findViewById(R.id.movement_status_not_found_msg);

        emp_id = userInfoLists.get(0).getEmp_id();
        if (!userInfoLists.get(0).isIsp_user()) {
            di_id = userInfoLists.get(0).getUsr_name();
        }
        statusLists = new ArrayList<>();

        statusView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        statusView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(statusView.getContext(),DividerItemDecoration.VERTICAL);
        statusView.addItemDecoration(dividerItemDecoration);

        getMoveStatus();
    }

    public void getMoveStatus() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        statusLists = new ArrayList<>();

        String url = api_url_front+"movement/getMovementList?p_emp_id="+emp_id+"&p_di_id="+di_id;

        RequestQueue requestQueue = Volley.newRequestQueue(MovementStatus.this);

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

                        String fmr_code = statusInfo.getString("fmr_code")
                                .equals("null") ? "" : statusInfo.getString("fmr_code");
                        String vehicle_name = statusInfo.getString("vehicle_name")
                                .equals("null") ? "" : statusInfo.getString("vehicle_name");
                        String di_full_name = statusInfo.getString("di_full_name")
                                .equals("null") ? "" : statusInfo.getString("di_full_name");
                        String ad_name = statusInfo.getString("ad_name")
                                .equals("null") ? "" : statusInfo.getString("ad_name");
                        String m_date = statusInfo.getString("m_date")
                                .equals("null") ? "" : statusInfo.getString("m_date");
                        String fmr_movement_type = statusInfo.getString("fmr_movement_type")
                                .equals("null") ? "" : statusInfo.getString("fmr_movement_type");
                        String fmr_movement_details = statusInfo.getString("fmr_movement_details")
                                .equals("null") ? "" : statusInfo.getString("fmr_movement_details");
                        String fra_pk = statusInfo.getString("fra_pk")
                                .equals("null") ? "Not Found" : statusInfo.getString("fra_pk");
                        String map_value = statusInfo.getString("map_value")
                                .equals("null") ? "0" : statusInfo.getString("map_value");
                        String fmrd_id = statusInfo.getString("fmrd_id")
                                .equals("null") ? "" : statusInfo.getString("fmrd_id");
                        String fmrd_move_file = statusInfo.getString("fmrd_move_file")
                                .equals("null") ? "" : statusInfo.getString("fmrd_move_file");
                        String fr_code = statusInfo.getString("fr_code")
                                .equals("null") ? "" : statusInfo.getString("fr_code");

                        Blob blob = null;

                        if (!fmrd_move_file.isEmpty()) {
                            byte[] decodedString = Base64.decode(fmrd_move_file,Base64.DEFAULT);
                            blob = new OracleSerialBlob(decodedString);
                        }

                        statusLists.add(new MovementList(fmr_code,vehicle_name,di_full_name,ad_name,m_date,
                                fmr_movement_type,fmr_movement_details,fra_pk,map_value,fmrd_id,blob,fr_code));
                    }
                }
                connected = true;
                updateLayout();
            }
            catch (JSONException | SQLException e) {
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
                statusAdapter = new MovementStatusAdapter(statusLists, MovementStatus.this);
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
                AlertDialog dialog = new AlertDialog.Builder(MovementStatus.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getMoveStatus();
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
            AlertDialog dialog = new AlertDialog.Builder(MovementStatus.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getMoveStatus();
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