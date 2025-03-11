package ttit.com.shuvo.eliteforce.leave.leave_balance;

import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.leave.leave_balance.adpaters.LeaveBalanceAdapter;
import ttit.com.shuvo.eliteforce.leave.leave_balance.model.LeaveBalanceList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class LeaveBalance extends AppCompatActivity {

    TextInputEditText name;
    TextInputEditText join;
    TextInputEditText title;

    TextView year;
    TextView noDataFound;
    String formattedDate = "";

    RecyclerView leaveView;
    RecyclerView.LayoutManager layoutManager;
    LeaveBalanceAdapter leaveBalanceAdapter;


    ArrayList<LeaveBalanceList> leaveBalanceLists;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";
    String yearrr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_balance);

        name = findViewById(R.id.name_leave);
        join = findViewById(R.id.joining_leave);
        title = findViewById(R.id.calling_title_leave);
        year = findViewById(R.id.from_to_year);
        leaveView = findViewById(R.id.leave_list_view);
        noDataFound = findViewById(R.id.no_data_found_leave_balance_msg);
        noDataFound.setVisibility(View.GONE);

        leaveBalanceLists = new ArrayList<>();

        if (!userInfoLists.isEmpty()) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            String empFullName = firstname+" "+lastName;
            name.setText(empFullName);
            emp_id = userInfoLists.get(0).getEmp_id();
        }

        if (!userDesignations.isEmpty()) {
            String jsmName = userDesignations.get(0).getJsm_name();
            if (jsmName == null) {
                jsmName = "";
            }

            String joinDate = userDesignations.get(0).getJoining_date();
            if (joinDate == null) {
                joinDate = "";
            }
            title.setText(jsmName);
            join.setText(joinDate);
        }

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        formattedDate = df.format(c);

        leaveView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        leaveView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(leaveView.getContext(),DividerItemDecoration.VERTICAL);
        leaveView.addItemDecoration(dividerItemDecoration);

        getLeaveBalance();
    }

    public void getLeaveBalance() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        leaveBalanceLists = new ArrayList<>();
        yearrr = "";

        String url = api_url_front+"leave/getLeaveBalance/"+emp_id+"/"+formattedDate+"";

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveBalance.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject leaveBalanceInfo = array.getJSONObject(i);

                        String now_year = leaveBalanceInfo.getString("now_year")
                                .equals("null") ? "" : leaveBalanceInfo.getString("now_year");
                        String lc_name = leaveBalanceInfo.getString("lc_name")
                                .equals("null") ? "" : leaveBalanceInfo.getString("lc_name");
                        String lc_short_code = leaveBalanceInfo.getString("lc_short_code")
                                .equals("null") ? "" : leaveBalanceInfo.getString("lc_short_code");
                        String lbd_balance_qty = leaveBalanceInfo.getString("lbd_balance_qty")
                                .equals("null") ? "0" : leaveBalanceInfo.getString("lbd_balance_qty");
                        String lbd_opening_qty = leaveBalanceInfo.getString("lbd_opening_qty")
                                .equals("null") ? "0" : leaveBalanceInfo.getString("lbd_opening_qty");
                        String lbd_current_qty = leaveBalanceInfo.getString("lbd_current_qty")
                                .equals("null") ? "0" : leaveBalanceInfo.getString("lbd_current_qty");
                        String lbd_taken_qty = leaveBalanceInfo.getString("lbd_taken_qty")
                                .equals("null") ? "0" : leaveBalanceInfo.getString("lbd_taken_qty");
                        String lbd_cash_taken_qty = leaveBalanceInfo.getString("lbd_cash_taken_qty")
                                .equals("null") ? "0" : leaveBalanceInfo.getString("lbd_cash_taken_qty");
                        String lbd_transfer_qty = leaveBalanceInfo.getString("lbd_transfer_qty")
                                .equals("null") ? "0" : leaveBalanceInfo.getString("lbd_transfer_qty");

                        if (!lbd_opening_qty.equals("0") || !lbd_current_qty.equals("0")) {
                            leaveBalanceLists.add(new LeaveBalanceList(lc_name,lc_short_code,lbd_opening_qty,
                                    lbd_current_qty,lbd_taken_qty,lbd_transfer_qty,lbd_cash_taken_qty,lbd_balance_qty));
                        }

                        yearrr = now_year;
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
                if (leaveBalanceLists.size() == 0) {
                    noDataFound.setVisibility(View.VISIBLE);
                }
                else {
                    noDataFound.setVisibility(View.GONE);
                }
                leaveBalanceAdapter = new LeaveBalanceAdapter(leaveBalanceLists, LeaveBalance.this);
                leaveView.setAdapter(leaveBalanceAdapter);

                String tt = "YEAR: "+yearrr;
                year.setText(tt);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveBalance.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getLeaveBalance();
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
            AlertDialog dialog = new AlertDialog.Builder(LeaveBalance.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getLeaveBalance();
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