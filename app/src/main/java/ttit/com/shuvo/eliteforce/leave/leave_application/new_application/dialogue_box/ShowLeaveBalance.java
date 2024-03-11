package ttit.com.shuvo.eliteforce.leave.leave_application.new_application.dialogue_box;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.leave.leave_application.new_application.adapters.LeaveBalanceFroAPPAdapter;
import ttit.com.shuvo.eliteforce.leave.leave_application.new_application.model.LeaveBalanceForAPPList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class ShowLeaveBalance extends AppCompatDialogFragment {

    RecyclerView apptRecyclerView;
    LeaveBalanceFroAPPAdapter leaveBalanceFroAPPAdapter;
    RecyclerView.LayoutManager apptLayout;
    TextView noData;

    WaitProgress waitProgress = new WaitProgress();
    Boolean conn = false;
    Boolean connected = false;

    AppCompatActivity activity;

    ArrayList<LeaveBalanceForAPPList> leaveBalanceForAPPLists;

    String emp_id = "";
    String formattedDate = "";

    AlertDialog lBdialog;

    Context mContext;

    public ShowLeaveBalance(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.show_leave_balance, null);

        noData = view.findViewById(R.id.no_data_found_lb_msg);
        noData.setVisibility(View.GONE);

        activity = (AppCompatActivity) view.getContext();

        leaveBalanceForAPPLists = new ArrayList<>();

        emp_id = userInfoLists.get(0).getEmp_id();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        formattedDate = df.format(c);

        apptRecyclerView = view.findViewById(R.id.all_leave_balance_list);
        apptRecyclerView.setHasFixedSize(true);
        apptLayout = new LinearLayoutManager(getContext());
        apptRecyclerView.setLayoutManager(apptLayout);

        builder.setView(view);

        lBdialog = builder.create();

        lBdialog.setCancelable(false);
        lBdialog.setCanceledOnTouchOutside(false);

        lBdialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", (dialog, which) -> dialog.dismiss());

        getLeaveBalance();

        return lBdialog;
    }

    public void getLeaveBalance() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        leaveBalanceForAPPLists = new ArrayList<>();

        String url = api_url_front+"leave/show_leave_balance/"+emp_id+"/"+formattedDate+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

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

                        String lc_name = leaveBalanceInfo.getString("lc_name")
                                .equals("null") ? "" : leaveBalanceInfo.getString("lc_name");
                        String lbd_balance_qty = leaveBalanceInfo.getString("lbd_balance_qty")
                                .equals("null") ? "" : leaveBalanceInfo.getString("lbd_balance_qty");

                        leaveBalanceForAPPLists.add(new LeaveBalanceForAPPList(lc_name,lbd_balance_qty));

                    }
                }
                connected = true;
                updateDial();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateDial();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateDial();
        });

        requestQueue.add(stringRequest);
    }

    private void updateDial() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                leaveBalanceFroAPPAdapter = new LeaveBalanceFroAPPAdapter(leaveBalanceForAPPLists,getContext());
                apptRecyclerView.setAdapter(leaveBalanceFroAPPAdapter);
                if (leaveBalanceForAPPLists.size() == 0) {
                    noData.setVisibility(View.VISIBLE);
                }
                else {
                    noData.setVisibility(View.GONE);
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
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
                    lBdialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(mContext)
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
                lBdialog.dismiss();
            });
        }
    }
}
