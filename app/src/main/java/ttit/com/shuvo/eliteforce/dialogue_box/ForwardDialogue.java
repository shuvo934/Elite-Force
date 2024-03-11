package ttit.com.shuvo.eliteforce.dialogue_box;

import static ttit.com.shuvo.eliteforce.attendance.att_update.req_approve.AttUpReqApprove.darm_id;
import static ttit.com.shuvo.eliteforce.attendance.att_update.req_approve.AttUpReqApprove.forwardFromAtt;
import static ttit.com.shuvo.eliteforce.attendance.att_update.req_approve.AttUpReqApprove.req_code;
import static ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.LeaveApprove.forwardFromLeave;
import static ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.LeaveApprove.la_id;
import static ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.LeaveApprove.req_code_leave;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_update.req_approve.AttUpReqApprove;
import ttit.com.shuvo.eliteforce.basic_model.ForwardEMPList;
import ttit.com.shuvo.eliteforce.basic_model.SelectAllList;
import ttit.com.shuvo.eliteforce.dialogue_box.adapters.ForwardAdapter;
import ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.LeaveApprove;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class ForwardDialogue extends AppCompatDialogFragment implements ForwardAdapter.ClickedItem{
    TextInputEditText employeeName;
    TextInputLayout employeeNameLayout;

    LinearLayout lisLay;
    RecyclerView empListView;
    RecyclerView.LayoutManager layoutManager;
    ForwardAdapter forwardAdapter;

    TextInputLayout commLay;
    TextInputEditText comm;

    TextView noEmp;

    Button goBack;
    Button cont;

    AppCompatActivity activity;

    WaitProgress waitProgress = new WaitProgress();

    private Boolean conn = false;
    private Boolean connected = false;

    private Boolean isForwarded = false;
    private Boolean ffffoooorrrwww = false;
    private Boolean isForwardExe = false;

    private Boolean isForwardedLeave = false;
    private Boolean ffffoooorrrwwwllll = false;
    private Boolean isForwardExeLeave = false;
    
    ArrayList<ForwardEMPList> forwardEMPLists;
    ArrayList<SelectAllList> allSelectedApprover;
    ArrayList<SelectAllList> allApproverEmp;
    ArrayList<SelectAllList> allApproverWithoutDiv;
    ArrayList<SelectAllList> allApproverDivision;

    String emp_id = "";
    String desig_priority = "";
    String divm_id = "";
    String approval_band = "";
    int count_approv_emp = 0;
    
    String forward_to_id = "";
    String forward_comm = "";
    
    Context mContext;

    View view;
    AlertDialog forwarDialog;

    public ForwardDialogue(Context context) {
        this.mContext = context;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.forward_req_view, null);
        activity = (AppCompatActivity) view.getContext();

        employeeName = view.findViewById(R.id.emp_name_drop_down);
        employeeNameLayout = view.findViewById(R.id.emp_name_drop_down_lay);

        lisLay = view.findViewById(R.id.forward_list_view_approve_lay);
        empListView = view.findViewById(R.id.forward_list_view_approve);

        commLay = view.findViewById(R.id.comments_given_for_forward_lay);
        comm = view.findViewById(R.id.comments_given_for_forward);

        goBack = view.findViewById(R.id.forward_go_back);
        cont = view.findViewById(R.id.forward_continue);

        noEmp = view.findViewById(R.id.no_employee_msg);
        
        forwardEMPLists = new ArrayList<>();
        allApproverDivision = new ArrayList<>();
        allApproverEmp = new ArrayList<>();
        allApproverWithoutDiv = new ArrayList<>();
        allSelectedApprover = new ArrayList<>();

        emp_id = userInfoLists.get(0).getEmp_id();
        
        empListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        empListView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(empListView.getContext(),DividerItemDecoration.VERTICAL);
        empListView.addItemDecoration(dividerItemDecoration);

        builder.setView(view);

        forwarDialog = builder.create();
        forwarDialog.setCancelable(false);
        forwarDialog.setCanceledOnTouchOutside(false);

        comm.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    comm.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        employeeName.setOnClickListener(v -> {
            int vis = lisLay.getVisibility();
            if (vis == 0) {
                lisLay.setVisibility(View.GONE);
            } else {
                lisLay.setVisibility(View.VISIBLE);
            }
        });

        goBack.setOnClickListener(v -> forwarDialog.dismiss());

        cont.setOnClickListener(v -> {
            forward_comm = Objects.requireNonNull(comm.getText()).toString();

            if (forwardFromAtt == 1) {
                if (forward_comm.isEmpty()) {
                    Toast.makeText(getContext(),"Please enter forward comment",Toast.LENGTH_SHORT).show();
                } 
                else {
                    if (forward_to_id.isEmpty()) {
                        Toast.makeText(getContext(),"Please enter forward to employee",Toast.LENGTH_SHORT).show();
                    } 
                    else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                        builder1.setTitle("Forward Request!")
                                .setMessage("Do you want forward this request?")
                                .setPositiveButton("YES", (dialog, which) -> forwardAttReq())
                                .setNegativeButton("NO", (dialog, which) -> {

                                });
                        AlertDialog alert = builder1.create();
                        alert.show();
                    }
                }
            } 
            else if (forwardFromLeave == 1) {
                if (forward_comm.isEmpty()) {
                    Toast.makeText(getContext(),"Please enter forward comment",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (forward_to_id.isEmpty()) {
                        Toast.makeText(getContext(),"Please enter forward to employee",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
                        builder1.setTitle("Forward Leave Application!")
                                .setMessage("Do you want forward this leave application?")
                                .setPositiveButton("YES", (dialog, which) -> forwardLeaveReq())
                                .setNegativeButton("NO", (dialog, which) -> {

                                });
                        AlertDialog alert = builder1.create();
                        alert.show();
                    }
                }
            }
        });

        getForwardToEmp();

        return forwarDialog;
    }

    private void closeKeyBoard() {
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        lisLay.setVisibility(View.GONE);
        String name;

        name = forwardEMPLists.get(CategoryPosition).getEmpName();
        forward_to_id = forwardEMPLists.get(0).getEmpID();
        employeeName.setText(name);
        employeeName.setTextColor(Color.BLACK);
        employeeNameLayout.setHint("Employee Name");
    }

    // getting employee list to forward req
    public void getForwardToEmp() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        forwardEMPLists = new ArrayList<>();
        allApproverDivision = new ArrayList<>();
        allApproverEmp = new ArrayList<>();
        allApproverWithoutDiv = new ArrayList<>();
        allSelectedApprover = new ArrayList<>();

        desig_priority = "";
        divm_id = "";
        approval_band = "";
        count_approv_emp = 0;

        String approverDivUrl = api_url_front+"forwardReq/attReqApproverWithDiv/"+emp_id+"";
        String appWithoutDivUrl  = api_url_front+"forwardReq/attReqApproverWithoutDiv/"+emp_id+"";
        String allapproverUrl = api_url_front+"forwardReq/attReqAllApprover/"+emp_id+"";
        String desigPriorUrl = api_url_front+"forwardReq/getDesigPriority/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest desigPriorReq = new StringRequest(Request.Method.GET, desigPriorUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject desigPrInfo = array.getJSONObject(i);
                        desig_priority  = desigPrInfo.getString("desig_priority")
                                .equals("null") ? "" : desigPrInfo.getString("desig_priority");
                        divm_id = desigPrInfo.getString("jsm_divm_id")
                                .equals("null") ? "" : desigPrInfo.getString("jsm_divm_id");

                        System.out.println("designation1: " + desig_priority);
                    }
                }

                if (!desig_priority.isEmpty()) {
                    System.out.println("designation2: " + desig_priority);
                    getForwarderList();
                }
                else {
                    if (allSelectedApprover.size() != 0) {
                        for (int i = 0; i<allSelectedApprover.size(); i++) {
                            forwardEMPLists.add(new ForwardEMPList(allSelectedApprover.get(i).getId(),allSelectedApprover.get(i).getFirst(),allSelectedApprover.get(i).getSecond(),allSelectedApprover.get(i).getThird(),allSelectedApprover.get(i).getFourth()));
                        }
                    }
                    connected = true;
                    updateInfo();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        StringRequest allAppReq = new StringRequest(Request.Method.GET, allapproverUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject allAppInfo = array.getJSONObject(i);

                        String emp_id_new = allAppInfo.getString("emp_id");

                        String emp_name = allAppInfo.getString("emp_name")
                                .equals("null") ? "" : allAppInfo.getString("emp_name");
                        String job_calling_title = allAppInfo.getString("job_calling_title")
                                .equals("null") ? "" : allAppInfo.getString("job_calling_title");
                        String jsm_name = allAppInfo.getString("jsm_name")
                                .equals("null") ? "" : allAppInfo.getString("jsm_name");
                        String divm_name = allAppInfo.getString("divm_name")
                                .equals("null") ? "" : allAppInfo.getString("divm_name");

                        allApproverEmp.add(new SelectAllList(emp_id_new,emp_name,job_calling_title,jsm_name,divm_name));
                    }
                }

                requestQueue.add(desigPriorReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        StringRequest appWithoutDivReq = new StringRequest(Request.Method.GET, appWithoutDivUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject appWithoutDivInfo = array.getJSONObject(i);

                        String emp_id_new = appWithoutDivInfo.getString("emp_id");

                        String emp_name = appWithoutDivInfo.getString("emp_name")
                                .equals("null") ? "" : appWithoutDivInfo.getString("emp_name");
                        String job_calling_title = appWithoutDivInfo.getString("job_calling_title")
                                .equals("null") ? "" : appWithoutDivInfo.getString("job_calling_title");
                        String jsm_name = appWithoutDivInfo.getString("jsm_name")
                                .equals("null") ? "" : appWithoutDivInfo.getString("jsm_name");
                        String divm_name = appWithoutDivInfo.getString("divm_name")
                                .equals("null") ? "" : appWithoutDivInfo.getString("divm_name");

                        allApproverWithoutDiv.add(new SelectAllList(emp_id_new,emp_name,job_calling_title,jsm_name,divm_name));

                    }
                }

                requestQueue.add(allAppReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        StringRequest appDivReq = new StringRequest(Request.Method.GET, approverDivUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject appDivInfo = array.getJSONObject(i);
                        String emp_id_new = appDivInfo.getString("emp_id");

                        String emp_name = appDivInfo.getString("emp_name")
                                .equals("null") ? "" : appDivInfo.getString("emp_name");
                        String job_calling_title = appDivInfo.getString("job_calling_title")
                                .equals("null") ? "" : appDivInfo.getString("job_calling_title");
                        String jsm_name = appDivInfo.getString("jsm_name")
                                .equals("null") ? "" : appDivInfo.getString("jsm_name");
                        String divm_name = appDivInfo.getString("divm_name")
                                .equals("null") ? "" : appDivInfo.getString("divm_name");

                        allApproverDivision.add(new SelectAllList(emp_id_new,emp_name,job_calling_title,jsm_name,divm_name));
                    }
                }
                requestQueue.add(appWithoutDivReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        requestQueue.add(appDivReq);
    }

    public void getForwarderList() {

        String approvalBandUrl = api_url_front+"forwardReq/getApprovalBand/"+desig_priority+"";
        String countApp1Url = api_url_front+"forwardReq/getCountApprovEmp/"+divm_id+"/"+desig_priority+"";
        String countApp2Url = api_url_front+"forwardReq/getCountApprovEmp_2/"+desig_priority+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest countApp2Req = new StringRequest(Request.Method.GET, countApp2Url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cApp2Info = array.getJSONObject(i);
                        count_approv_emp = cApp2Info.getInt("cc2");
                    }
                }

                if (count_approv_emp <= 0) {
                    allSelectedApprover = allApproverWithoutDiv;
                } else {
                    allSelectedApprover = allApproverEmp;
                }

                if (allSelectedApprover.size() != 0) {
                    for (int i = 0; i<allSelectedApprover.size(); i++) {
                        forwardEMPLists.add(new ForwardEMPList(allSelectedApprover.get(i).getId(),allSelectedApprover.get(i).getFirst(),allSelectedApprover.get(i).getSecond(),allSelectedApprover.get(i).getThird(),allSelectedApprover.get(i).getFourth()));
                    }
                }
                connected = true;
                updateInfo();

            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        StringRequest countApp1Req = new StringRequest(Request.Method.GET, countApp1Url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject cApp1Info = array.getJSONObject(i);
                        count_approv_emp = cApp1Info.getInt("cc");
                    }
                }
                if (count_approv_emp <= 0) {
                    requestQueue.add(countApp2Req);
                }
                else {
                    allSelectedApprover = allApproverDivision;
                    if (allSelectedApprover.size() != 0) {
                        for (int i = 0; i<allSelectedApprover.size(); i++) {
                            forwardEMPLists.add(new ForwardEMPList(allSelectedApprover.get(i).getId(),allSelectedApprover.get(i).getFirst(),allSelectedApprover.get(i).getSecond(),allSelectedApprover.get(i).getThird(),allSelectedApprover.get(i).getFourth()));
                        }
                    }
                    connected = true;
                    updateInfo();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        StringRequest appBandReq = new StringRequest(Request.Method.GET, approvalBandUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject appBandInfo = array.getJSONObject(i);
                        approval_band = appBandInfo.getString("lah_approval_band")
                                .equals("null") ? "" : appBandInfo.getString("lah_approval_band");

                    }
                }
                if (!approval_band.isEmpty()) {
                    requestQueue.add(countApp1Req);
                }
                else {
                    if (allSelectedApprover.size() != 0) {
                        for (int i = 0; i<allSelectedApprover.size(); i++) {
                            forwardEMPLists.add(new ForwardEMPList(allSelectedApprover.get(i).getId(),allSelectedApprover.get(i).getFirst(),allSelectedApprover.get(i).getSecond(),allSelectedApprover.get(i).getThird(),allSelectedApprover.get(i).getFourth()));
                        }
                    }
                    connected = true;
                    updateInfo();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInfo();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInfo();
        });

        requestQueue.add(appBandReq);
    }

    private void updateInfo() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (forwardEMPLists.size() == 0) {
                    noEmp.setVisibility(View.VISIBLE);
                } else {
                    noEmp.setVisibility(View.GONE);
                    forwardAdapter = new ForwardAdapter(forwardEMPLists, getContext(), ForwardDialogue.this);
                    empListView.setAdapter(forwardAdapter);
                    forwardAdapter.notifyDataSetChanged();
                }
                conn = false;
                connected = false;
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

                    getForwardToEmp();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    forwarDialog.dismiss();
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

                getForwardToEmp();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                forwarDialog.dismiss();
            });
        }
    }

    // forward attendance update request
    public void forwardAttReq() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        ffffoooorrrwww = false;
        isForwardExe = false;
        isForwarded = false;

        String forwardAttReqUrl = api_url_front+"forwardReq/forwardAttReq";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest forwardAtt = new StringRequest(Request.Method.POST, forwardAttReqUrl, response -> {
            ffffoooorrrwww = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    isForwarded = true;
                    isForwardExe = updated_req.equals("true");
                }
                else {
                    System.out.println(string_out);
                    isForwarded = false;
                }
                updateLayout();
            }
            catch (JSONException e) {
                e.printStackTrace();
                isForwarded = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            ffffoooorrrwww = false;
            isForwarded = false;
            updateLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_DARM_ID",darm_id);
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_FORWARD_COMM",forward_comm);
                headers.put("P_FORWARD_TO_ID",forward_to_id);
                headers.put("P_REQ_CODE",req_code);
                return headers;
            }
        };

        requestQueue.add(forwardAtt);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (ffffoooorrrwww) {
            if (isForwarded) {
                if (isForwardExe) {
                    req_code = "";
                    darm_id = "";
                    AttUpReqApprove.darm_emp_id = "";
                    AttUpReqApprove.selectApproveReqLists = new ArrayList<>();
                    forwardFromAtt = 0;
                    System.out.println("INSERTED");
                    forwarDialog.dismiss();
                    AlertDialog dialog1 = new AlertDialog.Builder(activity)
                            .setMessage("Request Forwarded Successfully")
                            .setPositiveButton("OK", null)
                            .show();

                    dialog1.setCancelable(false);
                    dialog1.setCanceledOnTouchOutside(false);
                    Button positive = dialog1.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {

                        dialog1.dismiss();
                        ((Activity)mContext).finish();
                    });
                }
                else {
                    Toast.makeText(getContext(), "Already Updated by Another User", Toast.LENGTH_SHORT).show();
                }
                ffffoooorrrwww = false;
                isForwardExe = false;
                isForwarded = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    forwardAttReq();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                forwardAttReq();
                dialog.dismiss();
            });
        }
    }

    // forward leave request
    public void forwardLeaveReq() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        ffffoooorrrwwwllll = false;
        isForwardExeLeave = false;
        isForwardedLeave = false;

        String forwardAttReqUrl = api_url_front+"forwardReq/forwardLeaveReq";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest forLeaveReq = new StringRequest(Request.Method.POST, forwardAttReqUrl, response -> {
            ffffoooorrrwwwllll = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    isForwardedLeave = true;
                    isForwardExeLeave = updated_req.equals("true");
                }
                else {
                    System.out.println(string_out);
                    isForwardedLeave = false;
                }
                updateLayoutLeave();
            }
            catch (JSONException e) {
                e.printStackTrace();
                isForwardedLeave = false;
                updateLayoutLeave();
            }
        }, error -> {
            error.printStackTrace();
            ffffoooorrrwwwllll = false;
            isForwardedLeave = false;
            updateLayoutLeave();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_LA_ID",la_id);
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_FORWARD_COMM",forward_comm);
                headers.put("P_FORWARD_TO_ID",forward_to_id);
                return headers;
            }
        };

        requestQueue.add(forLeaveReq);
    }

    private void updateLayoutLeave() {
        waitProgress.dismiss();
        if (ffffoooorrrwwwllll) {
            if (isForwardedLeave) {
                if (isForwardExeLeave) {
                    req_code_leave = "";
                    la_id = "";
                    LeaveApprove.la_emp_id = "";
                    LeaveApprove.leaveReqList = new ArrayList<>();
                    forwardFromLeave = 0;
                    System.out.println("INSERTED");

                    forwarDialog.dismiss();

                    AlertDialog dialog1 = new AlertDialog.Builder(activity)
                            .setMessage("Leave Application Forwarded Successfully")
                            .setPositiveButton("OK", null)
                            .show();

                    dialog1.setCancelable(false);
                    dialog1.setCanceledOnTouchOutside(false);
                    Button positive = dialog1.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {

                        dialog1.dismiss();
                        ((Activity)mContext).finish();
                    });
                }
                else {
                    Toast.makeText(getContext(), "Already Updated by Another User", Toast.LENGTH_SHORT).show();
                }
                ffffoooorrrwwwllll = false;
                isForwardExeLeave = false;
                isForwardedLeave = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    forwardLeaveReq();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(activity)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                forwardLeaveReq();
                dialog.dismiss();
            });
        }
    }
}
