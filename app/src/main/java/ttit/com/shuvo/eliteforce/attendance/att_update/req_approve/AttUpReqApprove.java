package ttit.com.shuvo.eliteforce.attendance.att_update.req_approve;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import ttit.com.shuvo.eliteforce.basic_model.SelectApproveReqList;
import ttit.com.shuvo.eliteforce.dialogue_box.DialogueText;
import ttit.com.shuvo.eliteforce.dialogue_box.ForwardDialogue;
import ttit.com.shuvo.eliteforce.dialogue_box.SelectApproveReq;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class AttUpReqApprove extends AppCompatActivity {

    public static int number = 0;
    public static String hint = "";
    public static String text = "";

    public static int forwardFromAtt = 0;

    public static int fromAttApp = 0;

    CardView afterSelecting;
    LinearLayout afterSelectingButton;
    LinearLayout inLay;
    LinearLayout outLay;
    LinearLayout forLay;

    public static TextInputLayout commentsLay;

    public static TextInputEditText requestCode;
    TextInputEditText name;
    TextInputEditText empCode;
    TextInputEditText appDate;
    TextInputEditText title;
    TextInputEditText requestType;
    TextInputEditText shiftUpdated;
    TextInputEditText dateUpdated;
    TextInputEditText inUpdated;
    TextInputEditText outUpdated;
    TextInputEditText machineIn;
    TextInputEditText machineOut;
    TextInputEditText shift;
    TextInputEditText reason;
    TextInputEditText reasonDesc;
    public static TextInputEditText comments;
    TextInputEditText forwardedBy;
    TextInputEditText forwardComm;

    Button approve;
    Button forward;
    Button reject;

    public static ArrayList<SelectApproveReqList> selectApproveReqLists;

    WaitProgress waitProgress = new WaitProgress();

    private Boolean conn = false;
    private Boolean connected = false;
    private Boolean dataIn = false;
    private Boolean inDataaa = false;

    private Boolean isApprovedd = false;
    private Boolean isApprovedChecked = false;
    private Boolean appppppprrrrr = false;

    private Boolean isRejected = false;
    private Boolean isRejectedChecked = false;
    private Boolean rrreeejjjeecctt = false;


    String emp_code = "";
    String user_id = "";
    public static String req_code = "";
    public static String darm_id = "";
    public static String darm_emp_id = "";

    String emp_name = "";
    String emp_id = "";
    String app_date = "";
    String call_title = "";
    String req_type = "";
    String shift_update = "";
    String date_update = "";
    String arr_time = "";
    String dep_time = "";
    String mac_in = "";
    String mac_out = "";
    String current_shift = "";
    String reason_type = "";
    String reason_desc = "";
    String forwarded_by = "";
    String forward_comm = "";
    String approvedEmpId = "";
    String jobCalling = "";
    String jsmID = "";
    String deptId = "";
    String divmId = "";
    String nowUpdateDate = "";
    String jobEmail = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att_up_req_approve);

        afterSelecting = findViewById(R.id.after_request_selecting_att_approve);
        afterSelectingButton = findViewById(R.id.button_visiblity_lay);
        inLay = findViewById(R.id.in_time_lay_att_approve);
        outLay = findViewById(R.id.out_time_lay_att_approve);
        forLay = findViewById(R.id.forward_layout);

        requestCode = findViewById(R.id.request_code_att_approve);
        name = findViewById(R.id.name_att_att_approve);
        empCode = findViewById(R.id.id_att_att_approve);
        appDate = findViewById(R.id.now_date_att_att_approve);
        title = findViewById(R.id.calling_title_att_approve);
        requestType = findViewById(R.id.req_type_att_approve);
        shiftUpdated = findViewById(R.id.shift_update_att_approve);
        dateUpdated = findViewById(R.id.date_to_be_updated_att_approve);
        inUpdated = findViewById(R.id.arrival_time_to_be_updated_att_approve);
        outUpdated = findViewById(R.id.departure_time_to_be_updated_att_approve);
        machineIn = findViewById(R.id.machine_in_time_att_approve);
        machineOut = findViewById(R.id.machine_out_time_att_approve);
        shift = findViewById(R.id.current_shift_att_approve);
        reason = findViewById(R.id.reason_type_att_approve);
        reasonDesc = findViewById(R.id.reason_description_att_approve);
        comments = findViewById(R.id.comments_given_att_approve);
        commentsLay = findViewById(R.id.comments_given_layout_att_approve);
        forwardedBy = findViewById(R.id.forwarded_by_att_approve);
        forwardComm = findViewById(R.id.forward_comment_att_approve);

        approve = findViewById(R.id.approve_button_att);
        forward = findViewById(R.id.forward_button_att);
        reject = findViewById(R.id.reject_button_att);

        selectApproveReqLists = new ArrayList<>();

        emp_code = userInfoLists.get(0).getUserName();
        user_id = userInfoLists.get(0).getEmp_id();

        requestCode.setOnClickListener(v -> {
            fromAttApp = 1;
            SelectApproveReq selectRequest = new SelectApproveReq();
            selectRequest.show(getSupportFragmentManager(),"Request");
        });

        requestCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getData();
            }
        });

        comments.setOnClickListener(v -> {
            number = 1;
            hint = Objects.requireNonNull(commentsLay.getHint()).toString();
            text = Objects.requireNonNull(comments.getText()).toString();
            DialogueText dialogueText = new DialogueText();
            dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
        });

        approve.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AttUpReqApprove.this);
            builder.setTitle("Approve Request!")
                    .setMessage("Do you want approve this request?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        text = Objects.requireNonNull(comments.getText()).toString();
                        approveAttReq();
                    })
                    .setNegativeButton("NO", (dialog, which) -> {

                    });
            AlertDialog alert = builder.create();
            alert.show();
        });

        reject.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(AttUpReqApprove.this);
            builder.setTitle("Reject Request!")
                    .setMessage("Do you want reject this request?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        text = Objects.requireNonNull(comments.getText()).toString();
                        rejectAttReq();
                    })
                    .setNegativeButton("NO", (dialog, which) -> {

                    });
            AlertDialog alert = builder.create();
            alert.show();
        });

        forward.setOnClickListener(v -> {
            forwardFromAtt = 1;
            ForwardDialogue forwardDialogue = new ForwardDialogue(AttUpReqApprove.this);
            forwardDialogue.show(getSupportFragmentManager(),"FORWARD");
        });

        getRequestList();
    }

    @Override
    public void onBackPressed() {
        req_code = "";
        darm_id = "";
        darm_emp_id = "";
        selectApproveReqLists = new ArrayList<>();
        finish();
    }

    // Attendance Update Request List
    public void getRequestList() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        selectApproveReqLists = new ArrayList<>();

        String url = api_url_front+"attendanceUpdateReq/getRequestList/"+emp_code+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttUpReqApprove.this);

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
                        
                        String darm_app_code_new = reqListInfo.getString("darm_app_code")
                                .equals("null") ? "" : reqListInfo.getString("darm_app_code");
                        String emp_name_new = reqListInfo.getString("emp_name")
                                .equals("null") ? "" : reqListInfo.getString("emp_name");
                        String emp_code_new = reqListInfo.getString("emp_code")
                                .equals("null") ? "" : reqListInfo.getString("emp_code");
                        String darm_date_new = reqListInfo.getString("darm_date")
                                .equals("null") ? "" : reqListInfo.getString("darm_date");
                        String darm_update_date_new = reqListInfo.getString("darm_update_date")
                                .equals("null") ? "" : reqListInfo.getString("darm_update_date");
                        String darm_id_new = reqListInfo.getString("darm_id")
                                .equals("null") ? "" : reqListInfo.getString("darm_id");
                        String darm_emp_id_new = reqListInfo.getString("darm_emp_id")
                                .equals("null") ? "" : reqListInfo.getString("darm_emp_id");

                        selectApproveReqLists.add(new SelectApproveReqList(darm_app_code_new,emp_name_new,emp_code_new,
                                darm_date_new,darm_update_date_new,darm_id_new,darm_emp_id_new));
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

        requestQueue.add(reqListReq);
    }

    private void updateInterface() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttUpReqApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getRequestList();
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
            AlertDialog dialog = new AlertDialog.Builder(AttUpReqApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getRequestList();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    // Attendance Update Request Data
    public void getData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        dataIn = false;
        inDataaa = false;

        emp_name = "";
        emp_id = "";
        app_date = "";
        call_title = "";
        req_type = "";
        shift_update = "";
        date_update = "";
        arr_time = "";
        dep_time = "";
        mac_in = "";
        mac_out = "";
        current_shift = "";
        reason_type = "";
        reason_desc = "";
        forwarded_by = "";
        forward_comm = "";

        String url = api_url_front+"attendanceUpdateReq/getReqData?emp_id="+user_id+"&darm_app_code="+req_code+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttUpReqApprove.this);

        StringRequest reqDataReq = new StringRequest(Request.Method.GET, url, response -> {
            dataIn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reqDataInfo = array.getJSONObject(i);

                        emp_id = reqDataInfo.getString("emp_code")
                                .equals("null") ? null : reqDataInfo.getString("emp_code");
                        emp_name = reqDataInfo.getString("emp_name")
                                .equals("null") ? null : reqDataInfo.getString("emp_name");
                        call_title = reqDataInfo.getString("job_calling_title")
                                .equals("null") ? null : reqDataInfo.getString("job_calling_title");
                        app_date = reqDataInfo.getString("darm_date")
                                .equals("null") ? null : reqDataInfo.getString("darm_date");
                        req_type = reqDataInfo.getString("darm_req_type")
                                .equals("null") ? null : reqDataInfo.getString("darm_req_type");
                        shift_update = reqDataInfo.getString("shift")
                                .equals("null") ? null : reqDataInfo.getString("shift");
                        date_update = reqDataInfo.getString("darm_update_date")
                                .equals("null") ? null : reqDataInfo.getString("darm_update_date");
                        arr_time = reqDataInfo.getString("arrival_time")
                                .equals("null") ? null : reqDataInfo.getString("arrival_time");
                        dep_time = reqDataInfo.getString("departure_time")
                                .equals("null") ? null : reqDataInfo.getString("departure_time");
                        reason_type = reqDataInfo.getString("reason")
                                .equals("null") ? null : reqDataInfo.getString("reason");
                        reason_desc = reqDataInfo.getString("darm_reason")
                                .equals("null") ? null : reqDataInfo.getString("darm_reason");
                        mac_in = reqDataInfo.getString("mac_in_time")
                                .equals("null") ? null : reqDataInfo.getString("mac_in_time");
                        mac_out = reqDataInfo.getString("mac_out_time")
                                .equals("null") ? null : reqDataInfo.getString("mac_out_time");
                        current_shift = reqDataInfo.getString("current_shift")
                                .equals("null") ? null : reqDataInfo.getString("current_shift");
                        forward_comm = reqDataInfo.getString("dard_recommendation")
                                .equals("null") ? null : reqDataInfo.getString("dard_recommendation");
                        forwarded_by = reqDataInfo.getString("forwarder_name")
                                .equals("null") ? null : reqDataInfo.getString("forwarder_name");
                    }
                }
                inDataaa = true;
                updateLayout();
            }
            catch (JSONException e) {
                e.printStackTrace();
                inDataaa = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            dataIn = false;
            inDataaa = false;
            updateLayout();
        });

        requestQueue.add(reqDataReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (dataIn) {
            afterSelecting.setVisibility(View.VISIBLE);
            afterSelectingButton.setVisibility(View.VISIBLE);
            if (inDataaa) {
                if (emp_name == null) {
                    name.setText("");
                } else {
                    name.setText(emp_name);
                }

                if (emp_id == null) {
                    empCode.setText("");
                } else {
                    empCode.setText(emp_id);
                }

                if (app_date == null) {
                    appDate.setText("");
                } else {
                    appDate.setText(app_date);
                }

                if (call_title == null) {
                    title.setText("");
                } else {
                    title.setText(call_title);
                }

                if (req_type == null) {
                    requestType.setText("");
                } else {
                    requestType.setText(req_type);
                }

                if (shift_update == null) {
                    shiftUpdated.setText("");
                } else {
                    shiftUpdated.setText(shift_update);
                }

                if (date_update == null) {
                    dateUpdated.setText("");
                } else {
                    dateUpdated.setText(date_update);
                }

                if (arr_time == null) {
                    inUpdated.setText("");
                    inLay.setVisibility(View.GONE);
                } else {
                    inUpdated.setText(arr_time);
                    inLay.setVisibility(View.VISIBLE);
                }

                if (dep_time == null) {
                    outUpdated.setText("");
                    outLay.setVisibility(View.GONE);
                } else {
                    outUpdated.setText(dep_time);
                    outLay.setVisibility(View.VISIBLE);
                }

                if (mac_in == null) {
                    machineIn.setText("");
                } else {
                    machineIn.setText(mac_in);
                }

                if (mac_out == null) {
                    machineOut.setText("");
                } else {
                    machineOut.setText(mac_out);
                }

                if (current_shift == null) {
                    shift.setText("");
                } else {
                    shift.setText(current_shift);
                }

                if (reason_type == null) {
                    reason.setText("");
                } else {
                    reason.setText(reason_type);
                }

                if (reason_desc == null) {
                    reasonDesc.setText("");
                } else {
                    reasonDesc.setText(reason_desc);
                }

                if (forwarded_by == null) {
                    forLay.setVisibility(View.GONE);
                    forwardedBy.setText("");
                } else {
                    forwardedBy.setText(forwarded_by);
                    forLay.setVisibility(View.VISIBLE);
                }

                if (forward_comm == null) {
                    forwardComm.setText("");
                } else {
                    forwardComm.setText(forward_comm);
                }

                dataIn = false;
                inDataaa = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttUpReqApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getData();
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
            AlertDialog dialog = new AlertDialog.Builder(AttUpReqApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    // Attendance Update Request Approve Process
    public void approveAttReq() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        appppppprrrrr = false;
        isApprovedd = false;
        isApprovedChecked = false;

        approvedEmpId = "";
        jobCalling = "";
        jsmID = "";
        deptId = "";
        divmId = "";
        nowUpdateDate = "";
        jobEmail = "";

        String approverDataUrl = api_url_front+"attendanceUpdateReq/getApproverData/"+emp_code+"";
        String approveAttUrl = api_url_front+"attendanceUpdateReq/approveAttReq";

        RequestQueue requestQueue = Volley.newRequestQueue(AttUpReqApprove.this);

        StringRequest approveReq = new StringRequest(Request.Method.POST, approveAttUrl, response -> {
            appppppprrrrr = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    isApprovedChecked = true;
                    isApprovedd = updated_req.equals("true");
                }
                else {
                    System.out.println(string_out);
                    isApprovedChecked = false;
                }
                updateApprovedReq();
            }
            catch (JSONException e) {
                e.printStackTrace();
                isApprovedChecked = false;
                updateApprovedReq();
            }
        }, error -> {
            error.printStackTrace();
            appppppprrrrr = false;
            isApprovedChecked = false;
            updateApprovedReq();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_DARM_ID",darm_id);
                headers.put("P_EMP_ID",approvedEmpId);
                headers.put("P_CALLING_TITLE",jobCalling);
                headers.put("P_JSM_ID",jsmID);
                headers.put("P_DEPT_ID",deptId);
                headers.put("P_DIVM_ID",divmId);
                headers.put("P_NOW_UPDATE_DATE",nowUpdateDate);
                headers.put("P_COMMENTS",text);
                headers.put("P_DARM_EMP_ID",darm_emp_id);
                headers.put("P_UPDATE_DATE",date_update);
                return headers;
            }
        };

        StringRequest appDataReq = new StringRequest(Request.Method.GET, approverDataUrl, response -> {
            appppppprrrrr = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject appDataInfo = array.getJSONObject(i);

                        approvedEmpId = appDataInfo.getString("emp_id")
                                .equals("null") ? "" : appDataInfo.getString("emp_id");
                        jobCalling = appDataInfo.getString("job_calling_title")
                                .equals("null") ? "" : appDataInfo.getString("job_calling_title");
                        jsmID = appDataInfo.getString("jsm_id")
                                .equals("null") ? "" : appDataInfo.getString("jsm_id");
                        deptId = appDataInfo.getString("dept_id")
                                .equals("null") ? "" : appDataInfo.getString("dept_id");
                        divmId = appDataInfo.getString("divm_id")
                                .equals("null") ? "" : appDataInfo.getString("divm_id");
                        nowUpdateDate = appDataInfo.getString("update_date")
                                .equals("null") ? "" : appDataInfo.getString("update_date");
                        jobEmail = appDataInfo.getString("job_email")
                                .equals("null") ? "" : appDataInfo.getString("job_email");
                        
                    }
                }

                requestQueue.add(approveReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                isApprovedChecked = false;
                updateApprovedReq();
            }
        }, error -> {
            error.printStackTrace();
            appppppprrrrr = false;
            isApprovedChecked = false;
            updateApprovedReq();
        });

        requestQueue.add(appDataReq);
    }

    private void updateApprovedReq() {
        waitProgress.dismiss();
        if (appppppprrrrr) {
            if (isApprovedChecked) {
                if (isApprovedd) {
                    req_code = "";
                    darm_id = "";
                    darm_emp_id = "";
                    selectApproveReqLists = new ArrayList<>();
                    System.out.println("INSERTED");

                    AlertDialog dialog = new AlertDialog.Builder(AttUpReqApprove.this)
                            .setMessage("Request Approved Successfully")
                            .setPositiveButton("OK", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {
                        dialog.dismiss();
                        finish();
                    });

                }
                else {
                    Toast.makeText(getApplicationContext(), "Already Updated by Another User", Toast.LENGTH_SHORT).show();
                }

                appppppprrrrr = false;
                isApprovedd = false;
                isApprovedChecked = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttUpReqApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();


                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    approveAttReq();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AttUpReqApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();


            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                approveAttReq();
                dialog.dismiss();
            });
        }
    }

    // Attendance Request Reject Process
    public void rejectAttReq() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        rrreeejjjeecctt = false;
        isRejected = false;
        isRejectedChecked = false;

        approvedEmpId = "";
        jobCalling = "";
        jsmID = "";
        deptId = "";
        divmId = "";
        nowUpdateDate = "";
        jobEmail = "";

        String rejecterDataUrl = api_url_front+"attendanceUpdateReq/getApproverData/"+emp_code+"";
        String rejectAttUrl = api_url_front+"attendanceUpdateReq/rejectAttReq";

        RequestQueue requestQueue = Volley.newRequestQueue(AttUpReqApprove.this);

        StringRequest rejectReq = new StringRequest(Request.Method.POST, rejectAttUrl, response -> {
            rrreeejjjeecctt = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    isRejectedChecked = true;
                    isRejected = updated_req.equals("true");
                }
                else {
                    System.out.println(string_out);
                    isRejectedChecked = false;
                }
                updateRejectedReq();
            }
            catch (JSONException e) {
                isRejectedChecked = false;
                updateRejectedReq();
            }
        }, error -> {
            error.printStackTrace();
            rrreeejjjeecctt = false;
            isRejectedChecked = false;
            updateRejectedReq();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_DARM_ID",darm_id);
                headers.put("P_EMP_ID",approvedEmpId);
                headers.put("P_CALLING_TITLE",jobCalling);
                headers.put("P_JSM_ID",jsmID);
                headers.put("P_DEPT_ID",deptId);
                headers.put("P_DIVM_ID",divmId);
                headers.put("P_NOW_UPDATE_DATE",nowUpdateDate);
                headers.put("P_COMMENTS",text);
                return headers;
            }
        };

        StringRequest rejDataReq = new StringRequest(Request.Method.GET, rejecterDataUrl, response -> {
            rrreeejjjeecctt = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject rejDataInfo = array.getJSONObject(i);

                        approvedEmpId = rejDataInfo.getString("emp_id")
                                .equals("null") ? "" : rejDataInfo.getString("emp_id");
                        jobCalling = rejDataInfo.getString("job_calling_title")
                                .equals("null") ? "" : rejDataInfo.getString("job_calling_title");
                        jsmID = rejDataInfo.getString("jsm_id")
                                .equals("null") ? "" : rejDataInfo.getString("jsm_id");
                        deptId = rejDataInfo.getString("dept_id")
                                .equals("null") ? "" : rejDataInfo.getString("dept_id");
                        divmId = rejDataInfo.getString("divm_id")
                                .equals("null") ? "" : rejDataInfo.getString("divm_id");
                        nowUpdateDate = rejDataInfo.getString("update_date")
                                .equals("null") ? "" : rejDataInfo.getString("update_date");
                        jobEmail = rejDataInfo.getString("update_date")
                                .equals("null") ? "" : rejDataInfo.getString("update_date");
                    }
                }

                requestQueue.add(rejectReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                isRejectedChecked = false;
                updateRejectedReq();
            }
        }, error -> {
            error.printStackTrace();
            rrreeejjjeecctt = false;
            isRejectedChecked = false;
            updateRejectedReq();
        });

        requestQueue.add(rejDataReq);
    }

    private void updateRejectedReq() {
        waitProgress.dismiss();
        if (rrreeejjjeecctt) {
            if (isRejectedChecked) {
                if (isRejected) {
                    req_code = "";
                    darm_id = "";
                    darm_emp_id = "";
                    selectApproveReqLists = new ArrayList<>();
                    System.out.println("INSERTED");

                    AlertDialog dialog = new AlertDialog.Builder(AttUpReqApprove.this)
                            .setMessage("Request Rejected Successfully")
                            .setPositiveButton("OK", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> {
                        dialog.dismiss();
                        finish();
                    });

                }
                else {
                    Toast.makeText(getApplicationContext(), "Already Updated by Another User", Toast.LENGTH_SHORT).show();
                }

                rrreeejjjeecctt = false;
                isRejected = false;
                isRejectedChecked = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttUpReqApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    rejectAttReq();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AttUpReqApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                rejectAttReq();
                dialog.dismiss();
            });
        }
    }
}