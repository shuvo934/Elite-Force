package ttit.com.shuvo.eliteforce.employeeInfo.jobDesc;

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

public class JobDescription extends AppCompatActivity {

    TextView no_job;
    RecyclerView job_list;
    JobAdapter jobAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<JobDescDetails> jobDescDetails;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_description);

        emp_id = userInfoLists.get(0).getEmp_id();
        no_job = findViewById(R.id.no_job);
        job_list = findViewById(R.id.job_desc_list);

        jobDescDetails = new ArrayList<>();

        job_list.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        job_list.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(job_list.getContext(),DividerItemDecoration.VERTICAL);
        job_list.addItemDecoration(dividerItemDecoration);

        getJobDescription();
    }

    public void getJobDescription() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        jobDescDetails = new ArrayList<>();

        String jobDescUrl = api_url_front+"emp_information/getJobDescription/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(JobDescription.this);

        StringRequest jobDescReq = new StringRequest(Request.Method.GET, jobDescUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    int j = 0;
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jobDescInfo = array.getJSONObject(i);

                        String ejd_jsdd_name = jobDescInfo.getString("ejd_jsdd_name")
                                .equals("null") ? "" : jobDescInfo.getString("ejd_jsdd_name");

                        ejd_jsdd_name = transformText(ejd_jsdd_name);

                        j++;
                        jobDescDetails.add(new JobDescDetails(String.valueOf(j),ejd_jsdd_name));
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
        },error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(jobDescReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {

                jobAdapter = new JobAdapter(jobDescDetails, JobDescription.this);
                job_list.setAdapter(jobAdapter);

                if (jobDescDetails.size() == 0) {
                    no_job.setVisibility(View.VISIBLE);
                    job_list.setVisibility(View.GONE);
                } else {
                    no_job.setVisibility(View.GONE);
                    job_list.setVisibility(View.VISIBLE);
                }

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(JobDescription.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getJobDescription();
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
            AlertDialog dialog = new AlertDialog.Builder(JobDescription.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getJobDescription();
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