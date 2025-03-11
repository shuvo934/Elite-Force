package ttit.com.shuvo.eliteforce.attendance.att_update.new_request.dialogue_box;

//import static ttit.com.shuvo.eliteforce.attendance.att_update.new_request.AttUpNewRequest.dateToShow;
//import static ttit.com.shuvo.eliteforce.attendance.att_update.new_request.AttUpNewRequest.showAttendanceNumber;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class ShowAttendance extends AppCompatDialogFragment {
    TextInputEditText intime;
    TextInputEditText latetime;
    TextInputEditText outtime;
    TextInputEditText machArrTime;
    TextInputEditText machDepTime;
    TextInputEditText exShNa;
    TextInputEditText exLocNa;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    AppCompatActivity activity;

    String emp_id = "";
    String date = "";

    String shiftin = "";
    String shiftout = "";
    String lateArr = "";
    String machineArr = "";
    String machiDep = "";
    String shiftName = "";
    String locName = "";

    AlertDialog showAttdialog;

    Context mContext;

    public ShowAttendance(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.show_attendance, null);
        activity = (AppCompatActivity) view.getContext();
        emp_id = userInfoLists.get(0).getEmp_id();

        intime = view.findViewById(R.id.shift_in_time);
        latetime = view.findViewById(R.id.late_arrival_time);
        outtime = view.findViewById(R.id.shift_out_time);
        machArrTime = view.findViewById(R.id.mach_arri_time);
        machDepTime = view.findViewById(R.id.mach_depa_time);
        exShNa = view.findViewById(R.id.existing_shift_name);
        exLocNa = view.findViewById(R.id.existing_loc_name);


//        if (showAttendanceNumber == 1) {
//            date = dateToShow;
//        }

        builder.setView(view);

        showAttdialog = builder.create();

        showAttdialog.setCancelable(false);
        showAttdialog.setCanceledOnTouchOutside(false);

        showAttdialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", (dialog, which) -> {
//            showAttendanceNumber = 0;
            dialog.dismiss();
        });

        getAttendanceShow();

        return showAttdialog;
    }

    public void getAttendanceShow() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        shiftin = "";
        shiftout = "";
        lateArr = "";
        machineArr = "";
        machiDep = "";
        shiftName = "";
        locName = "";

        String url = api_url_front+"attendanceUpdateReq/showAttendance?emp_id="+emp_id+"&att_date="+date+"";

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
                        JSONObject statInfo = array.getJSONObject(i);

                        shiftin = statInfo.getString("dac_start_time")
                                .equals("null") ? null : statInfo.getString("dac_start_time");
                        lateArr = statInfo.getString("dac_late_after")
                                .equals("null") ? null : statInfo.getString("dac_late_after");
                        shiftout = statInfo.getString("dac_end_time")
                                .equals("null") ? null : statInfo.getString("dac_end_time");
                        machineArr = statInfo.getString("dac_mac_in_time")
                                .equals("null") ? null : statInfo.getString("dac_mac_in_time");
                        machiDep = statInfo.getString("dac_mac_out_time")
                                .equals("null") ? null : statInfo.getString("dac_mac_out_time");
                        shiftName = statInfo.getString("osm_name")
                                .equals("null") ? null : statInfo.getString("osm_name");
                        locName = statInfo.getString("coa_name")
                                .equals("null") ? null : statInfo.getString("coa_name");
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
                String noTimeMsg = "No Time Found";
                if (shiftin == null) {
                    intime.setText(noTimeMsg);
                }
                else {
                    if (shiftin.isEmpty()) {
                        intime.setText(noTimeMsg);
                    }
                    else {
                        intime.setText(shiftin);
                    }
                }

                if (shiftout == null) {
                    outtime.setText(noTimeMsg);
                }
                else {
                    if (shiftout.isEmpty()) {
                        outtime.setText(noTimeMsg);
                    }
                    else {
                        outtime.setText(shiftout);
                    }
                }

                if (lateArr == null) {
                    latetime.setText(noTimeMsg);
                }
                else {
                    if (lateArr.isEmpty()) {
                        latetime.setText(noTimeMsg);
                    }
                    else {
                        latetime.setText(lateArr);
                    }
                }

                if (machineArr == null) {
                    machArrTime.setText(noTimeMsg);
                }
                else {
                    if (machineArr.isEmpty()) {
                        machArrTime.setText(noTimeMsg);
                    }
                    else {
                        machArrTime.setText(machineArr);
                    }
                }

                if (machiDep == null) {
                    machDepTime.setText(noTimeMsg);
                }
                else {
                    if (machiDep.isEmpty()) {
                        machDepTime.setText(noTimeMsg);
                    }
                    else {
                        machDepTime.setText(machiDep);
                    }
                }

                String noShiftMsg = "No Shift Found";
                if (shiftName == null) {
                    exShNa.setText(noShiftMsg);
                }
                else {
                    if (shiftName.isEmpty()) {
                        exShNa.setText(noShiftMsg);
                    }
                    else {
                        exShNa.setText(shiftName);
                    }
                }

                String noLocMsg = "No Location Found";
                if (locName == null) {
                    exLocNa.setText(noLocMsg);
                }
                else {
                    if (locName.isEmpty()) {
                        exLocNa.setText(noLocMsg);
                    }
                    else {
                        exLocNa.setText(locName);
                    }
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAttendanceShow();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    showAttdialog.dismiss();

                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAttendanceShow();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                showAttdialog.dismiss();
            });
        }
    }
}
