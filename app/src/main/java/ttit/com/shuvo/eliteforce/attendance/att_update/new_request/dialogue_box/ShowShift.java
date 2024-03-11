package ttit.com.shuvo.eliteforce.attendance.att_update.new_request.dialogue_box;

import static ttit.com.shuvo.eliteforce.attendance.att_update.new_request.AttUpNewRequest.shift_osm_id;
import static ttit.com.shuvo.eliteforce.attendance.att_update.new_request.AttUpNewRequest.showShiftNumber;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_update.new_request.adapters.ShowShiftAdapter;
import ttit.com.shuvo.eliteforce.attendance.att_update.new_request.model.ShowShiftList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class ShowShift extends AppCompatDialogFragment {

    RecyclerView apptRecyclerView;
    ShowShiftAdapter showShiftAdapter;
    RecyclerView.LayoutManager apptLayout;


    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    AppCompatActivity activity;

    ArrayList<ShowShiftList> showShiftLists;

    String osm_id = "";
    AlertDialog showShiftdialog;

    Context mContext;

    public ShowShift(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.show_shift, null);

        activity = (AppCompatActivity) view.getContext();

        showShiftLists = new ArrayList<>();

        if (showShiftNumber == 1) {
            osm_id = shift_osm_id;
        }

        apptRecyclerView = view.findViewById(R.id.all_shift_time_view);
        apptRecyclerView.setHasFixedSize(true);
        apptLayout = new LinearLayoutManager(getContext());
        apptRecyclerView.setLayoutManager(apptLayout);

        builder.setView(view);

        showShiftdialog = builder.create();

        showShiftdialog.setCancelable(false);
        showShiftdialog.setCanceledOnTouchOutside(false);

        showShiftdialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", (dialog, which) -> {
            showShiftNumber = 0;
            dialog.dismiss();
        });

        getShiftDetails();

        return showShiftdialog;
    }

    public void getShiftDetails() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        showShiftLists = new ArrayList<>();

        String url = api_url_front+"attendanceUpdateReq/getShiftDetails/"+osm_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url , response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String osm_name = jsonObject.getString("osm_name")
                        .equals("null") ? "" : jsonObject.getString("osm_name");
                String osm_start_time = jsonObject.getString("osm_start_time")
                        .equals("null") ? "" : jsonObject.getString("osm_start_time");
                String osm_late_after = jsonObject.getString("osm_late_after")
                        .equals("null") ? "" : jsonObject.getString("osm_late_after");
                String osm_early_before = jsonObject.getString("osm_early_before")
                        .equals("null") ? "" : jsonObject.getString("osm_early_before");
                String osm_end_time = jsonObject.getString("osm_end_time")
                        .equals("null") ? "" : jsonObject.getString("osm_end_time");
                String osm_extd_out_time = jsonObject.getString("osm_extd_out_time")
                        .equals("null") ? "" : jsonObject.getString("osm_extd_out_time");

                showShiftLists.add(new ShowShiftList(osm_name,osm_start_time,osm_late_after,
                        osm_early_before,osm_end_time,osm_extd_out_time));

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
                showShiftAdapter = new ShowShiftAdapter(showShiftLists,getContext());
                apptRecyclerView.setAdapter(showShiftAdapter);
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

                    getShiftDetails();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    showShiftdialog.dismiss();
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

                getShiftDetails();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                showShiftdialog.dismiss();
            });
        }
    }
}
