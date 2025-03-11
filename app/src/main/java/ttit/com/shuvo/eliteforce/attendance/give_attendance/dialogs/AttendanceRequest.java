package ttit.com.shuvo.eliteforce.attendance.give_attendance.dialogs;

import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.eliteforce.R;

public class AttendanceRequest extends AppCompatDialogFragment {

    LinearLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;
    MaterialButton reload;
    ImageView close;

    TextView attReqMsg;
    String msg = "If you still want to give attendance, it will generate as an attendance request.";

//    TextView yesText;
//    TextView noText;
//    SwitchCompat switchCompat;

    LinearLayout reasonLay;
    TextInputEditText reason;
    TextView errorReason;
    String reason_desc = "";

    MaterialButton cancel;
    MaterialButton submit;

    private Boolean conn = false;
    private Boolean connected = false;
    private Boolean loading = false;

    AppCompatActivity activity;
    AlertDialog dialog;
    Context mContext;
    String emp_id;
    String time_stamp;
    String machine_code;
    String lat;
    String lng;
    String address;
    float distance;

    public AttendanceRequest(Context mContext, String emp_id, String time_stamp, String machine_code, String lat, String lng, String address, float distance) {
        this.mContext = mContext;
        this.emp_id = emp_id;
        this.time_stamp = time_stamp;
        this.machine_code = machine_code;
        this.lat = lat;
        this.lng = lng;
        this.address = address;
        this.distance = distance;
    }

    Logger logger = Logger.getLogger("AttendanceRequest");

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.attendance_req_dialog_layout, null);
        activity = (AppCompatActivity) view.getContext();

        fullLayout = view.findViewById(R.id.att_req_full_layout);
        circularProgressIndicator = view.findViewById(R.id.progress_indicator_att_req);
        circularProgressIndicator.setVisibility(View.GONE);
        reload = view.findViewById(R.id.reload_page_button_att_req);
        reload.setVisibility(View.GONE);

        close = view.findViewById(R.id.close_logo_of_att_req);
        attReqMsg = view.findViewById(R.id.attendance_request_msg_text);
        reasonLay = view.findViewById(R.id.reason_for_att_req_layout);
        reason = view.findViewById(R.id.reason_for_att_req);
        errorReason = view.findViewById(R.id.error_input_reason_for_att_req);

//        yesText = view.findViewById(R.id.yes_text_view);
//        noText = view.findViewById(R.id.no_text_view);
//        switchCompat = view.findViewById(R.id.yes_no_att_req_switch);

        cancel = view.findViewById(R.id.cancel_att_req);
        submit = view.findViewById(R.id.submit_att_req);

        reasonLay.setVisibility(View.VISIBLE);
        errorReason.setVisibility(View.GONE);

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);

        String ms = "You are "+Math.round(distance)+" meters away from your allocated area.\n" + msg;
        attReqMsg.setText(ms);

//        switchCompat.setOnCheckedChangeListener((compoundButton, b) -> {
//            if (b) {
//                yesText.setTextColor(mContext.getColor(R.color.black));
//                noText.setTextColor(mContext.getColor(R.color.elite_grey));
//                reasonLay.setVisibility(View.VISIBLE);
//                errorReason.setVisibility(View.GONE);
//            }
//            else {
//                noText.setTextColor(mContext.getColor(R.color.black));
//                yesText.setTextColor(mContext.getColor(R.color.elite_grey));
//                reasonLay.setVisibility(View.GONE);
//                errorReason.setVisibility(View.GONE);
//            }
//        });

        reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    errorReason.setVisibility(View.VISIBLE);
                }
                else {
                    errorReason.setVisibility(View.GONE);
                }
            }
        });

        reason.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    reason.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        cancel.setOnClickListener(view1 -> dialog.dismiss());

        submit.setOnClickListener(view1 -> {
            reason_desc = Objects.requireNonNull(reason.getText()).toString();
            if (reason_desc.isEmpty()) {
                errorReason.setVisibility(View.VISIBLE);
            }
            else {
                errorReason.setVisibility(View.GONE);
                giveAttendance();
            }
        });

        reload.setOnClickListener(view1 -> {
            reload.setVisibility(View.GONE);
            giveAttendance();
        });

        close.setOnClickListener(view1 -> {
            if (loading) {
                Toast.makeText(mContext, "Please Wait while loading", Toast.LENGTH_SHORT).show();
            }
            else {
                dialog.dismiss();
            }
        });

        return dialog;
    }

    private void closeKeyBoard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void giveAttendance() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        conn = false;
        connected = false;
        loading = true;

        String attendaceUrl = api_url_front+"attendance/giveOutAttendance";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest attReq = new StringRequest(Request.Method.POST, attendaceUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    connected = true;
                }
                else {
                    System.out.println(string_out);
                    connected = false;
                }
                updateLayout();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            conn = false;
            connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_PUNCH_TIME",time_stamp);
                headers.put("P_MACHINE_CODE",machine_code);
                headers.put("P_LATITUDE",lat);
                headers.put("P_LONGITUDE",lng);
                headers.put("P_ADDRESS",address);
                headers.put("P_REASON",reason_desc);
                return  headers;
            }
        };

        requestQueue.add(attReq);
    }
    private void updateLayout() {
        if(conn) {
            if (connected) {
                fullLayout.setVisibility(View.VISIBLE);
                circularProgressIndicator.setVisibility(View.GONE);
                reload.setVisibility(View.GONE);
                conn = false;
                connected = false;
                loading = false;

                Toast.makeText(mContext, "Attendance Request Sent Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
            else {
                alertMessage();
            }
        }
        else {
            alertMessage();
        }
    }

    public void alertMessage() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.GONE);
        reload.setVisibility(View.VISIBLE);
        loading = false;

        Toast.makeText(mContext, "Server problem or Internet not connected", Toast.LENGTH_SHORT).show();
    }
}
