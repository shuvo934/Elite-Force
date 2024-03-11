package ttit.com.shuvo.eliteforce.employeeInfo.performance;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
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
import ttit.com.shuvo.eliteforce.employeeInfo.jobDesc.adapters.JobAdapter;
import ttit.com.shuvo.eliteforce.employeeInfo.jobDesc.model.JobDescDetails;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class PerformanceApp extends AppCompatActivity {

    TextView no_gpi, no_kpi;
    RecyclerView gpiList;
    JobAdapter gpiAdapter;
    RecyclerView.LayoutManager layoutManager;

    RecyclerView kpiList;
    JobAdapter kpiAdapter;
    RecyclerView.LayoutManager layoutManager1;

    ArrayList<JobDescDetails> gpiDetails;
    ArrayList<JobDescDetails> kpiDetails;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performance_app);

        no_gpi = findViewById(R.id.no_gpi);
        no_kpi = findViewById(R.id.no_kpi);
        gpiList = findViewById(R.id.gpi_list);
        kpiList = findViewById(R.id.kpi_list);

        emp_id = userInfoLists.get(0).getEmp_id();
        gpiDetails = new ArrayList<>();
        kpiDetails = new ArrayList<>();

        gpiList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        gpiList.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(gpiList.getContext(),DividerItemDecoration.VERTICAL);
        gpiList.addItemDecoration(dividerItemDecoration);

        kpiList.setHasFixedSize(true);
        layoutManager1 = new LinearLayoutManager(this);
        kpiList.setLayoutManager(layoutManager1);
        DividerItemDecoration dividerItemDecoration1 = new DividerItemDecoration(kpiList.getContext(), DividerItemDecoration.VERTICAL);
        kpiList.addItemDecoration(dividerItemDecoration1);

        getGpiKpiDetails();
    }

    public void getGpiKpiDetails() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        gpiDetails = new ArrayList<>();
        kpiDetails = new ArrayList<>();

        String gpiUrl = api_url_front+"emp_information/getGpiDetails/"+emp_id+"";
        String kpiUrl = api_url_front+"emp_information/getKpiDetails/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(PerformanceApp.this);

        StringRequest kpiReq = new StringRequest(Request.Method.GET, kpiUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    int j = 0;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject kpiInfo = array.getJSONObject(i);

                        String eki_kpi_factor = kpiInfo.getString("eki_kpi_factor")
                                .equals("null") ? "" : kpiInfo.getString("eki_kpi_factor");

                        eki_kpi_factor = transformText(eki_kpi_factor);
                        j++;
                        kpiDetails.add(new JobDescDetails(String.valueOf(j),eki_kpi_factor));
                    }
                }
                connected = true;
                updateLayout();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        StringRequest gpiReq = new StringRequest(Request.Method.GET, gpiUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    int j = 0;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject gpiInfo = array.getJSONObject(i);
                        String egi_pgpi_factor = gpiInfo.getString("egi_pgpi_factor")
                                .equals("null") ? "" : gpiInfo.getString("egi_pgpi_factor");

                        egi_pgpi_factor = transformText(egi_pgpi_factor);
                        j++;
                        gpiDetails.add(new JobDescDetails(String.valueOf(j),egi_pgpi_factor));
                    }
                }
                requestQueue.add(kpiReq);
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(gpiReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {

                gpiAdapter = new JobAdapter(gpiDetails, PerformanceApp.this);
                kpiAdapter = new JobAdapter(kpiDetails,PerformanceApp.this);

                gpiList.setAdapter(gpiAdapter);
                kpiList.setAdapter(kpiAdapter);

                if (gpiDetails.size() == 0) {
                    no_gpi.setVisibility(View.VISIBLE);
                    gpiList.setVisibility(View.GONE);
                } else {
                    no_gpi.setVisibility(View.GONE);
                    gpiList.setVisibility(View.VISIBLE);
                }

                if (kpiDetails.size() == 0) {
                    no_kpi.setVisibility(View.VISIBLE);
                    kpiList.setVisibility(View.GONE);
                } else {
                    no_kpi.setVisibility(View.GONE);
                    kpiList.setVisibility(View.VISIBLE);
                }

                connected = false;
                conn = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(PerformanceApp.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getGpiKpiDetails();
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
            AlertDialog dialog = new AlertDialog.Builder(PerformanceApp.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getGpiKpiDetails();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {

                dialog.dismiss();
                finish();
            });
        }
    }

    //    --------------------------Transforming Bangla Text-----------------------------
    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }
}