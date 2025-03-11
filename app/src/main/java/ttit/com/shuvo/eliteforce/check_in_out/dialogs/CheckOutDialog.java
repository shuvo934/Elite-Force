package ttit.com.shuvo.eliteforce.check_in_out.dialogs;

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
import android.widget.Button;
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
import ttit.com.shuvo.eliteforce.check_in_out.interfaces.CheckOutSaveListener;
import ttit.com.shuvo.eliteforce.check_in_out.interfaces.ImageSaveListener;

public class CheckOutDialog extends AppCompatDialogFragment {

    LinearLayout fullLayout;
    CircularProgressIndicator circularProgressIndicator;
    MaterialButton reload;
    ImageView close;

    TextInputEditText remarks;
    TextView errorRemarks;
    String remarks_co = "";

    Button submit;

    private Boolean conn = false;
    private Boolean connected = false;
    private Boolean loading = false;

    AppCompatActivity activity;
    AlertDialog dialog;
    Context mContext;
    String cior_id = "";

    public CheckOutDialog(Context mContext, String cior_id) {
        this.mContext = mContext;
        this.cior_id = cior_id;
    }

    private CheckOutSaveListener checkOutSaveListener;
    Logger logger = Logger.getLogger("CheckOutDialog");

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        if (getActivity() instanceof CheckOutSaveListener)
            checkOutSaveListener = (CheckOutSaveListener) getActivity();

        View view = inflater.inflate(R.layout.check_out_dialog_layout, null);
        activity = (AppCompatActivity) view.getContext();

        fullLayout = view.findViewById(R.id.check_out_full_layout);
        circularProgressIndicator = view.findViewById(R.id.progress_indicator_check_out);
        circularProgressIndicator.setVisibility(View.GONE);
        reload = view.findViewById(R.id.reload_page_button_check_out);
        reload.setVisibility(View.GONE);

        close = view.findViewById(R.id.close_logo_of_check_out);
        remarks = view.findViewById(R.id.remarks_for_check_out);
        errorRemarks = view.findViewById(R.id.error_input_remarks_for_check_out);
        errorRemarks.setVisibility(View.GONE);

        submit = view.findViewById(R.id.check_out_submit_button);

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);

        remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    errorRemarks.setVisibility(View.VISIBLE);
                }
                else {
                    errorRemarks.setVisibility(View.GONE);
                }
            }
        });

        remarks.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    remarks.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });


        submit.setOnClickListener(view1 -> {
            remarks_co = Objects.requireNonNull(remarks.getText()).toString();
            if (remarks_co.isEmpty()) {
                errorRemarks.setVisibility(View.VISIBLE);
            }
            else {
                errorRemarks.setVisibility(View.GONE);
                insertCheckOut();
            }
        });

        reload.setOnClickListener(view1 -> {
            reload.setVisibility(View.GONE);
            insertCheckOut();
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

    public void insertCheckOut() {
        fullLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);
        reload.setVisibility(View.GONE);
        conn = false;
        connected = false;
        loading = true;

        String attendaceUrl = api_url_front+"checkInOut/updateCheckRegister";

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
                headers.put("P_REMARKS",remarks_co);
                headers.put("P_CIOR_ID",cior_id);
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

                Toast.makeText(mContext, "Check Out Submitted Successfully", Toast.LENGTH_SHORT).show();
                if (checkOutSaveListener != null)
                    checkOutSaveListener.onCheckOut();
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
