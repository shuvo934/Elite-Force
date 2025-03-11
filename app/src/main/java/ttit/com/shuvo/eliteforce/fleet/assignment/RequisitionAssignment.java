package ttit.com.shuvo.eliteforce.fleet.assignment;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_update.req_approve.AttUpReqApprove;
import ttit.com.shuvo.eliteforce.basic_model.SelectApproveReqList;
import ttit.com.shuvo.eliteforce.basic_model.TwoItemLists;
import ttit.com.shuvo.eliteforce.fleet.assignment.arraylists.DriverList;
import ttit.com.shuvo.eliteforce.fleet.assignment.arraylists.RequisitionToApproveList;
import ttit.com.shuvo.eliteforce.fleet.assignment.arraylists.VehicleList;
import ttit.com.shuvo.eliteforce.fleet.assignment.dialogs.SelectRequisitionDialog;
import ttit.com.shuvo.eliteforce.fleet.assignment.dialogs.VehicleSelectDialog;
import ttit.com.shuvo.eliteforce.fleet.assignment.interfaces.ReqSelectListener;
import ttit.com.shuvo.eliteforce.fleet.assignment.interfaces.VhSelectListener;
import ttit.com.shuvo.eliteforce.fleet.fleet_requisition.NewRequisition;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class RequisitionAssignment extends AppCompatActivity implements ReqSelectListener, VhSelectListener {

    TextInputEditText requisitionToken;
    public static String token_no = "";
    public static String requisition_id = "";
    public static String requester_name = "";
    public static String request_date = "";

    ArrayList<RequisitionToApproveList> requisition;

    CardView afterSelectingReq;

    AutoCompleteTextView assignVehicle;
    public static String vi_id = "";
    public static String vh_name = "";
    public static String vh_reg_name = "";
    public static String di_id = "";
    public static String driver_name = "";
    public static String driver_emp_id = "";
    TextView vhMissing;

    ArrayList<VehicleList> vehicleLists;

    TextInputLayout driverLay;
    AutoCompleteTextView assignDriver;
    TextView driverMissing;

    ArrayList<DriverList> driverLists;

    TextInputEditText driverMobile;
    String driver_mobile = "";
    TextView errDriverMob;

    TextInputEditText driverAck;
    String driver_ack = "";

    TextInputEditText driverRemarks;
    String driver_remarks = "";

    AmazingSpinner reqStatus;
    String req_status = "";
    TextView statusMissing;
    ArrayList<TwoItemLists> reqStatusList;

    LinearLayout buttonLay;
    Button save;

    WaitProgress waitProgress = new WaitProgress();

    private Boolean conn = false;
    private Boolean connected = false;
    boolean selectedFromItems = false;
    String user_name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requisition_assignment);

        requisitionToken = findViewById(R.id.requisition_token_req_approve);
        requisition = new ArrayList<>();

        afterSelectingReq = findViewById(R.id.after_request_selecting_att_approve);
        afterSelectingReq.setVisibility(View.GONE);

        assignVehicle = findViewById(R.id.assign_vehicle_for_req_spinner);
        vhMissing = findViewById(R.id.assign_vehicle_missing_msg);
        vhMissing.setVisibility(View.GONE);

        driverLay = findViewById(R.id.spinner_layout_assign_driver_for_req);
        driverLay.setEnabled(false);
        assignDriver = findViewById(R.id.assign_driver_for_req_spinner);
        driverMissing = findViewById(R.id.assign_driver_missing_msg);
        driverMissing.setVisibility(View.GONE);

        driverMobile = findViewById(R.id.driver_mobile_no);
        errDriverMob = findViewById(R.id.error_of_driver_mob_no);
        errDriverMob.setVisibility(View.GONE);

        driverAck = findViewById(R.id.driver_acknowledgement_req_assign);
        driverRemarks = findViewById(R.id.driver_remarks_req_assign);

        reqStatus = findViewById(R.id.requisition_status_req_assign);
        statusMissing = findViewById(R.id.req_status_missing_msg_in_req_assign);
        statusMissing.setVisibility(View.GONE);

        buttonLay = findViewById(R.id.button_visiblity_lay_req_ass);
        buttonLay.setVisibility(View.GONE);
        save = findViewById(R.id.save_button_req_assign);

        if (!userInfoLists.isEmpty()) {
            user_name = userInfoLists.get(0).getUserName();
        }

        reqStatusList = new ArrayList<>();
        reqStatusList.add(new TwoItemLists("1","Approve"));
        reqStatusList.add(new TwoItemLists("2","Hold"));
        reqStatusList.add(new TwoItemLists("0","Reject"));

        requisitionToken.setOnClickListener(view -> {
            SelectRequisitionDialog selectRequisitionDialog = new SelectRequisitionDialog(requisition,RequisitionAssignment.this);
            selectRequisitionDialog.show(getSupportFragmentManager(),"REQ");
        });

        assignVehicle.setOnClickListener(view -> {
            VehicleSelectDialog vehicleSelectDialog = new VehicleSelectDialog(vehicleLists,RequisitionAssignment.this);
            vehicleSelectDialog.show(getSupportFragmentManager(),"VHL");
        });

        assignDriver.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    driverMissing.setVisibility(View.GONE);
                }
            }
        });

        assignDriver.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            for (int i = 0; i <driverLists.size(); i++) {
                if (name.equals(driverLists.get(i).getDi_full_name())) {
                    di_id = driverLists.get(i).getDi_id();
                    driver_name = driverLists.get(i).getDi_full_name();
                    driver_emp_id = driverLists.get(i).getDi_emp_id();
                }
            }

            selectedFromItems = true;
            driverMissing.setVisibility(View.GONE);
            closeKeyBoard();
        });

        assignDriver.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String ss = assignDriver.getText().toString();
                if (!selectedFromItems) {
                    di_id = "";
                    for (int i = 0; i < driverLists.size(); i++) {
                        if (ss.equals(driverLists.get(i).getDi_full_name())) {
                            di_id = driverLists.get(i).getDi_id();
                            driver_name = driverLists.get(i).getDi_full_name();
                            driver_emp_id = driverLists.get(i).getDi_emp_id();
                        }
                    }
                    if (di_id.isEmpty()) {
                        if (ss.isEmpty()) {
                            driverMissing.setVisibility(View.VISIBLE);
                            String cmt = "Please Assign Driver";
                            driverMissing.setText(cmt);
                        }
                        else {
                            driverMissing.setVisibility(View.VISIBLE);
                            String cmt = "Invalid Driver";
                            driverMissing.setText(cmt);
                        }
                    }

                }
                else {
                    selectedFromItems = false;
                }
            }
        });

        assignDriver.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    assignDriver.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        driverMobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().startsWith("0")) {
                    if (editable.toString().length() == 11) {
                        errDriverMob.setVisibility(View.GONE);
                    }
                    else {
                        errDriverMob.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    errDriverMob.setVisibility(View.VISIBLE);
                }
            }
        });

        driverMobile.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        driverAck.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        driverRemarks.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        reqStatus.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            req_status = "";
            for (int i = 0; i <reqStatusList.size(); i++) {
                if (name.equals(reqStatusList.get(i).getName())) {
                    req_status = reqStatusList.get(i).getId();
                }
            }
            statusMissing.setVisibility(View.GONE);
        });

        save.setOnClickListener(v -> {
            driver_mobile = Objects.requireNonNull(driverMobile.getText()).toString();
            driver_ack = Objects.requireNonNull(driverAck.getText()).toString();
            driver_remarks = Objects.requireNonNull(driverRemarks.getText()).toString();

            if (!requisition_id.isEmpty()) {
                if (!vi_id.isEmpty()) {
                    if (!di_id.isEmpty()) {
                        if (driver_mobile.isEmpty()) {
                            // ---
                            if (!req_status.isEmpty()) {
                                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
                                alertDialogBuilder.setTitle("Requisition Assignment")
                                        .setMessage("Do you want to save this requisition assignment?")
                                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                                            saveAssignment();
                                            dialogInterface.dismiss();
                                        })
                                        .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.setCanceledOnTouchOutside(false);
                                alertDialog.setCancelable(false);
                                alertDialog.show();
                            }
                            else {
                                statusMissing.setVisibility(View.VISIBLE);
                                Toast.makeText(this, "Please Select Requisition Status", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            if (driver_mobile.startsWith("0")) {
                                if (driver_mobile.length() == 11) {
                                    // ---
                                    if (!req_status.isEmpty()) {
                                        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
                                        alertDialogBuilder.setTitle("Requisition Assignment")
                                                .setMessage("Do you want to save this requisition assignment?")
                                                .setPositiveButton("Yes", (dialogInterface, i) -> {
                                                    saveAssignment();
                                                    dialogInterface.dismiss();
                                                })
                                                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.setCanceledOnTouchOutside(false);
                                        alertDialog.setCancelable(false);
                                        alertDialog.show();
                                    }
                                    else {
                                        statusMissing.setVisibility(View.VISIBLE);
                                        Toast.makeText(this, "Please Select Requisition Status", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else {
                                    errDriverMob.setVisibility(View.VISIBLE);
                                    Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                errDriverMob.setVisibility(View.VISIBLE);
                                Toast.makeText(this, "Invalid Number", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    else {
                        driverMissing.setVisibility(View.VISIBLE);
                        Toast.makeText(this, "Please Assign Driver", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    vhMissing.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Please Asiign Vehicle", Toast.LENGTH_SHORT).show();;
                }
            }
        });

        getAllData();

    }

    @Override
    public void onBackPressed() {
        token_no = "";
        requisition_id = "";
        requester_name = "";
        request_date = "";
        vi_id = "";
        vh_name = "";
        vh_reg_name = "";
        di_id = "";
        driver_name = "";
        driver_emp_id = "";
        finish();
    }

    private void closeKeyBoard() {
        View view = getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void getAllData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        requisition = new ArrayList<>();
        vehicleLists = new ArrayList<>();
        driverLists = new ArrayList<>();

        String url = api_url_front+"fleet_requisition/getRequisitionList";
        String v_url = api_url_front+"fleet_requisition/getVehicle";
        String d_url = api_url_front+"fleet_requisition/getDriver";

        RequestQueue requestQueue = Volley.newRequestQueue(RequisitionAssignment.this);

        StringRequest drReq = new StringRequest(Request.Method.GET, d_url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reqListInfo = array.getJSONObject(i);


                        String di_id_new = reqListInfo.getString("di_id")
                                .equals("null") ? "" : reqListInfo.getString("di_id");
                        String di_full_name = reqListInfo.getString("di_full_name")
                                .equals("null") ? "" : reqListInfo.getString("di_full_name");
                        String di_nick_name = reqListInfo.getString("di_nick_name")
                                .equals("null") ? "" : reqListInfo.getString("di_nick_name");
                        String di_emp_id_new = reqListInfo.getString("di_emp_id")
                                .equals("null") ? "" : reqListInfo.getString("di_emp_id");

                        driverLists.add(new DriverList(di_id_new,di_full_name,di_nick_name,di_emp_id_new));
                    }
                }
                connected = true;
                updateInterface();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInterface();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInterface();
        });

        StringRequest vhReq = new StringRequest(Request.Method.GET, v_url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reqListInfo = array.getJSONObject(i);

                        String vi_id_new = reqListInfo.getString("vi_id")
                                .equals("null") ? "" : reqListInfo.getString("vi_id");
                        String year = reqListInfo.getString("year")
                                .equals("null") ? "" : reqListInfo.getString("year");
                        String model = reqListInfo.getString("model")
                                .equals("null") ? "" : reqListInfo.getString("model");
                        String name = reqListInfo.getString("name")
                                .equals("null") ? "" : reqListInfo.getString("name");
                        String reg_no = reqListInfo.getString("reg_no")
                                .equals("null") ? "" : reqListInfo.getString("reg_no");
                        String di_id_new = reqListInfo.getString("di_id")
                                .equals("null") ? "" : reqListInfo.getString("di_id");
                        String di_emp_id = reqListInfo.getString("di_emp_id")
                                .equals("null") ? "" : reqListInfo.getString("di_emp_id");
                        String di_name = reqListInfo.getString("di_name")
                                .equals("null") ? "" : reqListInfo.getString("di_name");

                        vehicleLists.add(new VehicleList(vi_id_new,year,model,name,reg_no,di_id_new,di_emp_id,di_name));
                    }
                }
                requestQueue.add(drReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInterface();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInterface();
        });

        StringRequest reqListReq = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reqListInfo = array.getJSONObject(i);

                        String fr_pk = reqListInfo.getString("fr_pk")
                                .equals("null") ? "" : reqListInfo.getString("fr_pk");
                        String fr_code = reqListInfo.getString("fr_code")
                                .equals("null") ? "" : reqListInfo.getString("fr_code");
                        String divm_name = reqListInfo.getString("divm_name")
                                .equals("null") ? "" : reqListInfo.getString("divm_name");
                        String dept_name = reqListInfo.getString("dept_name")
                                .equals("null") ? "" : reqListInfo.getString("dept_name");
                        String req_date = reqListInfo.getString("req_date")
                                .equals("null") ? "" : reqListInfo.getString("req_date");
                        String fr_from_address = reqListInfo.getString("fr_from_address")
                                .equals("null") ? "" : reqListInfo.getString("fr_from_address");
                        String from_date = reqListInfo.getString("from_date")
                                .equals("null") ? "" : reqListInfo.getString("from_date");
                        String to_date = reqListInfo.getString("to_date")
                                .equals("null") ? "" : reqListInfo.getString("to_date");
                        String fr_to_address = reqListInfo.getString("fr_to_address")
                                .equals("null") ? "" : reqListInfo.getString("fr_to_address");
                        String vh_type = reqListInfo.getString("vh_type")
                                .equals("null") ? "" : reqListInfo.getString("vh_type");
                        String fr_qty = reqListInfo.getString("fr_qty")
                                .equals("null") ? "" : reqListInfo.getString("fr_qty");
                        String fr_travelers_qty = reqListInfo.getString("fr_travelers_qty")
                                .equals("null") ? "" : reqListInfo.getString("fr_travelers_qty");
                        String emp_supervisor_emp_id = reqListInfo.getString("emp_supervisor_emp_id")
                                .equals("null") ? "" : reqListInfo.getString("emp_supervisor_emp_id");
                        String emp_name = reqListInfo.getString("emp_name")
                                .equals("null") ? "" : reqListInfo.getString("emp_name");
                        String requester_supervisor = reqListInfo.getString("requester_supervisor")
                                .equals("null") ? "" : reqListInfo.getString("requester_supervisor");

                        requisition.add(new RequisitionToApproveList(fr_pk,fr_code,divm_name,
                                dept_name,req_date,fr_from_address,from_date,to_date,fr_to_address,vh_type,fr_qty,fr_travelers_qty,
                                emp_supervisor_emp_id,emp_name,requester_supervisor));
                    }
                }
                requestQueue.add(vhReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInterface();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInterface();
        });

        requestQueue.add(reqListReq);
    }

    private void updateInterface() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < driverLists.size(); i++) {
                    type.add(driverLists.get(i).getDi_full_name());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RequisitionAssignment.this,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);
                assignDriver.setAdapter(arrayAdapter);

                ArrayList<String> type1 = new ArrayList<>();
                for(int i = 0; i < reqStatusList.size(); i++) {
                    type1.add(reqStatusList.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(RequisitionAssignment.this,R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);
                reqStatus.setAdapter(arrayAdapter1);

                SelectRequisitionDialog selectRequisitionDialog = new SelectRequisitionDialog(requisition,RequisitionAssignment.this);
                selectRequisitionDialog.show(getSupportFragmentManager(),"REQ");
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(RequisitionAssignment.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAllData();
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
            AlertDialog dialog = new AlertDialog.Builder(RequisitionAssignment.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAllData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    @Override
    public void onSelected() {
        requisitionToken.setText(token_no);
        afterSelectingReq.setVisibility(View.VISIBLE);
        buttonLay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVehicleSelect(String name,
                                String id,
                                String rg_no,
                                String di_id_new,
                                String di_name,
                                String di_emp_id) {
        vi_id = id;
        vh_name = name;
        vh_reg_name = rg_no;
        di_id = di_id_new;
        driver_name = di_name;
        driver_emp_id = di_emp_id;

        String vh = vh_name + " (" + vh_reg_name + ")";
        assignVehicle.setText(vh);
        vhMissing.setVisibility(View.GONE);
        driverLay.setEnabled(true);
        assignDriver.setText(driver_name);
        driverMissing.setVisibility(View.GONE);
    }

    public void saveAssignment() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        String reqUrl = api_url_front+"fleet_requisition/saveAssignment";

        RequestQueue requestQueue = Volley.newRequestQueue(RequisitionAssignment.this);

        StringRequest requisitionReq = new StringRequest(Request.Method.POST, reqUrl, response -> {
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
                e.printStackTrace();
                connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_REQUISION_ID",requisition_id);
                headers.put("P_VI_ID",vi_id);
                headers.put("P_DI_ID",di_id);
                headers.put("P_DRIVER_EMP_ID",driver_emp_id);
                headers.put("P_DRIVER_NUMBER",driver_mobile);
                headers.put("P_ACKNOLEDGEMENT",driver_ack);
                headers.put("P_REMARKS",driver_remarks);
                headers.put("P_USER",user_name);
                headers.put("P_SUBMITED_BY",requester_name);
                headers.put("P_SUBMITED_DATE",request_date);
                headers.put("P_APPROVAL_BY",user_name);
                headers.put("P_REQUISITION_STATUS",req_status);
                return  headers;
            }
        };

        requestQueue.add(requisitionReq);
    }

    private  void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(RequisitionAssignment.this);
                builder
                        .setMessage("Requisition Assigned successfully")
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                            token_no = "";
                            requisition_id = "";
                            requester_name = "";
                            request_date = "";
                            vi_id = "";
                            vh_name = "";
                            vh_reg_name = "";
                            di_id = "";
                            driver_name = "";
                            driver_emp_id = "";
                            finish();
                        });

                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(RequisitionAssignment.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    saveAssignment();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(RequisitionAssignment.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                saveAssignment();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}