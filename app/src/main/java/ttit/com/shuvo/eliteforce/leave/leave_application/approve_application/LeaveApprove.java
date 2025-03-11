package ttit.com.shuvo.eliteforce.leave.leave_application.approve_application;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
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
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.basic_model.SelectApproveReqList;
import ttit.com.shuvo.eliteforce.dialogue_box.DialogueText;
import ttit.com.shuvo.eliteforce.dialogue_box.ForwardDialogue;
import ttit.com.shuvo.eliteforce.dialogue_box.SelectApproveReq;
import ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.dialogue_box.ForwardHistoryDial;
import ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.model.ForwardHistoryList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class LeaveApprove extends AppCompatActivity {
    public static int number = 0;
    public static String hintLA = "";
    public static String textLA = "";

    public static int forwardFromLeave = 0;

    public static int fromLApp = 0;

    CardView afterSelecting;
    LinearLayout afterSelectingButton;
    LinearLayout forLay;

    @SuppressLint("StaticFieldLeak")
    public static TextInputLayout commentsLay;

    TextInputEditText name;
    TextInputEditText empCode;
    TextInputEditText appDate;
    TextInputEditText title;
    TextInputEditText leaveType;
    TextInputEditText leaveBalance;
    TextInputEditText fromDate;
    TextInputEditText toDate;
    TextInputEditText totalDays;
    TextInputEditText reason;
    public static TextInputEditText comments;

    Button approve;
    Button forward;
    Button reject;
    Button fh;

    String emp_name = "";
    String emp_id = "";
    String app_date = "";
    String call_title = "";
    String lc_id = "";
    String leave_type = "";
    String leave_bal = "";
    String from_date = "";
    String to_date = "";
    String total_day = "";
    String reason_desc = "";

    String sl_check = "0";
    String approveSuccess = "";
    String rejectSuccess = "";

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
    public static String req_code_leave = "";
    public static String la_id = "";
    public static String la_emp_id = "";

    public static TextInputEditText requestCodeLeave;

    public static ArrayList<SelectApproveReqList> leaveReqList;

    public static ArrayList<ForwardHistoryList> forwardHistoryLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approve);

        afterSelecting = findViewById(R.id.after_request_selecting_leave_approve);
        afterSelectingButton = findViewById(R.id.button_visiblity_leave_lay);
        forLay = findViewById(R.id.forward_layout_leave);

        requestCodeLeave = findViewById(R.id.request_code_leave_approve);
        name = findViewById(R.id.name_leave_approve);
        empCode = findViewById(R.id.id_leave_approve);
        appDate = findViewById(R.id.now_date_leave_approve);
        title = findViewById(R.id.calling_title_leave_approve);
        leaveType = findViewById(R.id.leave_type_leave_approve);
        leaveBalance = findViewById(R.id.leave_balance_leave_approve);
        fromDate = findViewById(R.id.from_date_leave_approve);
        toDate = findViewById(R.id.to_date_leave_approve);
        totalDays = findViewById(R.id.total_days_leave_approve);
        reason = findViewById(R.id.reason_leave_approve);
        comments = findViewById(R.id.comments_given_leave_approve);
        commentsLay = findViewById(R.id.comments_given_layout_leave_approve);

        approve = findViewById(R.id.approve_button_leave);
        forward = findViewById(R.id.forward_button_leave);
        reject = findViewById(R.id.reject_button_leave);
        fh = findViewById(R.id.forward_history_button_leave);

        emp_code = userInfoLists.get(0).getUserName();
        user_id = userInfoLists.get(0).getEmp_id();

        leaveReqList = new ArrayList<>();
        forwardHistoryLists = new ArrayList<>();

        requestCodeLeave.setOnClickListener(v -> {
            fromLApp = 1;
            SelectApproveReq selectRequest = new SelectApproveReq();
            selectRequest.show(getSupportFragmentManager(),"Request");
        });

        requestCodeLeave.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getReqData();
            }
        });

        fh.setOnClickListener(v -> {
            forwardFromLeave = 1;
            ForwardHistoryDial forwardHistoryDial = new ForwardHistoryDial();
            forwardHistoryDial.show(getSupportFragmentManager(),"Forward");
        });

        comments.setOnClickListener(v -> {
            number = 1;
            hintLA = Objects.requireNonNull(commentsLay.getHint()).toString();
            textLA = Objects.requireNonNull(comments.getText()).toString();
            DialogueText dialogueText = new DialogueText();
            dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
        });

        approve.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(LeaveApprove.this);
            builder.setTitle("Approve Leave!")
                    .setMessage("Do you want approve this leave?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        // checking it is sick leave or not
                        if (lc_id.equals("2")) {
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(LeaveApprove.this);
                            builder1.setTitle("Prescription Check!")
                                    .setMessage("Did you checked prescription of the applicant?")
                                    .setPositiveButton("YES", (dialog1, which1) -> {
                                        sl_check = "1";
                                        textLA = Objects.requireNonNull(comments.getText()).toString();
                                        leaveApproveProcess();
                                    })
                                    .setNegativeButton("NO", (dialog12, which12) -> {
                                        sl_check = "0";
                                        textLA = Objects.requireNonNull(comments.getText()).toString();
                                        leaveApproveProcess();
                                    });
                            AlertDialog alert = builder1.create();
                            alert.show();

                        } 
                        else {
                            textLA = Objects.requireNonNull(comments.getText()).toString();
                            leaveApproveProcess();
                        }

                    })
                    .setNegativeButton("NO", (dialog, which) -> {

                    });
            AlertDialog alert = builder.create();
            alert.show();
        });

        forward.setOnClickListener(v -> {
            forwardFromLeave = 1;
            ForwardDialogue forwardDialogue = new ForwardDialogue(LeaveApprove.this);
            forwardDialogue.show(getSupportFragmentManager(),"FORWARD");
        });

        reject.setOnClickListener(v -> {

            textLA = Objects.requireNonNull(comments.getText()).toString();

            if (textLA.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please mention reason", Toast.LENGTH_SHORT).show();
            }
            else {

                AlertDialog.Builder builder = new AlertDialog.Builder(LeaveApprove.this);
                builder.setTitle("Reject Leave!")
                        .setMessage("Do you want reject this leave?")
                        .setPositiveButton("YES", (dialog, which) -> leaveRejectProcess())
                        .setNegativeButton("NO", (dialog, which) -> {

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        getReqLists();
    }

    @Override
    public void onBackPressed() {
        req_code_leave = "";
        la_id = "";
        la_emp_id = "";
        leaveReqList = new ArrayList<>();
        finish();
    }

    public void getReqLists() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        leaveReqList = new ArrayList<>();

//        String leaveReqUrl = api_url_front+"leaveRequest/leaveReqLists/"+user_id+"/"+emp_code+"";
        String leaveReqUrl = api_url_front+"leaveRequest/newRequestList";

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApprove.this);

        StringRequest req = new StringRequest(Request.Method.GET, leaveReqUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reqListInfo = array.getJSONObject(i);

                        String la_app_code_new = reqListInfo.getString("la_app_code")
                                .equals("null") ? "" : reqListInfo.getString("la_app_code");
                        String la_emp_id_new = reqListInfo.getString("la_emp_id")
                                .equals("null") ? "" : reqListInfo.getString("la_emp_id");
                        String emp_code_new = reqListInfo.getString("emp_code")
                                .equals("null") ? "" : reqListInfo.getString("emp_code");
                        String emp_name_new = reqListInfo.getString("emp_name")
                                .equals("null") ? "" : reqListInfo.getString("emp_name");
                        String la_date_new = reqListInfo.getString("la_date")
                                .equals("null") ? "" : reqListInfo.getString("la_date");
                        String la_leave_days_new = reqListInfo.getString("la_leave_days")
                                .equals("null") ? "" : reqListInfo.getString("la_leave_days");
                        String la_id_new = reqListInfo.getString("la_id")
                                .equals("null") ? "" : reqListInfo.getString("la_id");

                        leaveReqList.add(new SelectApproveReqList(la_app_code_new,emp_name_new,emp_code_new,
                                la_date_new,la_leave_days_new,la_id_new,la_emp_id_new));

                    }
                }

                connected = true;
                updateReqLists();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateReqLists();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateReqLists();
        });

        requestQueue.add(req);
    }

    private void updateReqLists() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
                fromLApp = 1;
                SelectApproveReq selectRequest = new SelectApproveReq();
                selectRequest.show(getSupportFragmentManager(),"Request");
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getReqLists();
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
            AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getReqLists();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    public void getReqData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        dataIn = false;
        inDataaa = false;

        emp_name = "";
        emp_id = "";
        app_date = "";
        call_title = "";
        leave_type = "";
        leave_bal = "";
        from_date = "";
        to_date = "";
        total_day = "";
        reason_desc = "";
        lc_id = "";

        forwardHistoryLists = new ArrayList<>();

        System.out.println("BEFORE API CALL: "+req_code_leave);

        String reqDataUrl = api_url_front+"leaveRequest/getReqData/"+la_id+"";
        String forwardHisUrl = api_url_front+"leaveRequest/getForwardHistory/"+la_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApprove.this);

        StringRequest forHisReq = new StringRequest(Request.Method.GET, forwardHisUrl, response -> {
            dataIn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject forHisInfo = array.getJSONObject(i);

                        String forward_by = forHisInfo.getString("forward_by")
                                .equals("null") ? "" : forHisInfo.getString("forward_by");
                        String lad_recommendation = forHisInfo.getString("lad_recommendation")
                                .equals("null") ? "" : forHisInfo.getString("lad_recommendation");
                        String forward_to = forHisInfo.getString("forward_to")
                                .equals("null") ? "" : forHisInfo.getString("forward_to");

                        forwardHistoryLists.add(new ForwardHistoryList(forward_by,lad_recommendation,forward_to));
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

        StringRequest reqDataReq = new StringRequest(Request.Method.GET, reqDataUrl, response -> {
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
                                .equals("null") ? "" : reqDataInfo.getString("emp_code");
                        emp_name = reqDataInfo.getString("emp_name")
                                .equals("null") ? "" : reqDataInfo.getString("emp_name");
                        call_title = reqDataInfo.getString("la_calling_title")
                                .equals("null") ? "" : reqDataInfo.getString("la_calling_title");
                        app_date = reqDataInfo.getString("la_date")
                                .equals("null") ? "" : reqDataInfo.getString("la_date");
                        lc_id = reqDataInfo.getString("la_lc_id")
                                .equals("null") ? "" : reqDataInfo.getString("la_lc_id");
                        leave_type = reqDataInfo.getString("leave_type")
                                .equals("null") ? "" : reqDataInfo.getString("leave_type");
                        from_date = reqDataInfo.getString("la_from_date")
                                .equals("null") ? "" : reqDataInfo.getString("la_from_date");
                        to_date = reqDataInfo.getString("la_to_date")
                                .equals("null") ? "" : reqDataInfo.getString("la_to_date");
                        total_day = reqDataInfo.getString("la_leave_days")
                                .equals("null") ? "" : reqDataInfo.getString("la_leave_days");
                        leave_bal= reqDataInfo.getString("leave_balance")
                                .equals("null") ? "" : reqDataInfo.getString("leave_balance");
                        reason_desc = reqDataInfo.getString("la_reason")
                                .equals("null") ? "" : reqDataInfo.getString("la_reason");
                    }
                }

                requestQueue.add(forHisReq);
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
            if (inDataaa) {
                afterSelecting.setVisibility(View.VISIBLE);
                afterSelectingButton.setVisibility(View.VISIBLE);

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

                if (leave_type == null) {
                    leaveType.setText("");
                } else {
                    leaveType.setText(leave_type);
                }

                if (leave_bal == null) {
                    leaveBalance.setText("");
                } else {
                    leaveBalance.setText(leave_bal);
                }

                if (from_date == null) {
                    fromDate.setText("");
                } else {
                    fromDate.setText(from_date);
                }

                if (to_date == null) {
                    toDate.setText("");
                } else {
                    toDate.setText(to_date);
                }

                if (total_day == null) {
                    totalDays.setText("");
                } else {
                    totalDays.setText(total_day);
                }

                if (reason_desc == null) {
                    reason.setText("");
                } else {
                    reason.setText(reason_desc);
                }

                if (forwardHistoryLists.size() == 0) {
                    forLay.setVisibility(View.GONE);
                } else {
                    forLay.setVisibility(View.VISIBLE);
                }

                dataIn = false;
                inDataaa = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getReqData();
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
            AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getReqData();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    public void leaveApproveProcess() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        appppppprrrrr = false;
        isApprovedd = false;
        isApprovedChecked = false;

        approveSuccess = "";

        String approveUrl = api_url_front+"leaveRequest/approveLeave";

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApprove.this);

        StringRequest approveReq = new StringRequest(Request.Method.POST, approveUrl, response -> {
            appppppprrrrr = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    isApprovedd = true;
                    isApprovedChecked = updated_req.toLowerCase(Locale.ENGLISH).equals("ok");
                    approveSuccess = updated_req;
                }
                else {
                    System.out.println(string_out);
                    isApprovedd = false;
                }
                updateAfterApprove();
            }
            catch (JSONException e) {
                e.printStackTrace();
                isApprovedd = false;
                updateAfterApprove();
            }
        }, error -> {
            error.printStackTrace();
            appppppprrrrr = false;
            isApprovedd = false;
            updateAfterApprove();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_LA_ID",la_id);
                headers.put("P_EMP_CODE",emp_code);
                headers.put("P_SL_CHECK",sl_check);
                headers.put("P_TEXT_COMMENTS",textLA);
                return headers;
            }
        };

        requestQueue.add(approveReq);
    }

    private void updateAfterApprove() {
        waitProgress.dismiss();
        if (appppppprrrrr) {
            if (isApprovedd) {
                if (isApprovedChecked) {
                    req_code_leave = "";
                    la_id = "";
                    la_emp_id = "";
                    leaveReqList = new ArrayList<>();
                    System.out.println("INSERTED");

                    AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                            .setMessage("Leave Approved Successfully")
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
                    Toast.makeText(getApplicationContext(), approveSuccess, Toast.LENGTH_SHORT).show();
                }
                appppppprrrrr = false;
                isApprovedd = false;
                isApprovedChecked = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    leaveApproveProcess();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                leaveApproveProcess();
                dialog.dismiss();
            });
        }
    }

    public void leaveRejectProcess() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);

        rejectSuccess = "";

        rrreeejjjeecctt = false;
        isRejected = false;
        isRejectedChecked = false;

        String rejectUrl = api_url_front+"leaveRequest/rejectLeave";

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveApprove.this);

        StringRequest rejectReq = new StringRequest(Request.Method.POST, rejectUrl, response -> {
            rrreeejjjeecctt = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    isRejected = true;
                    isRejectedChecked = updated_req.toLowerCase(Locale.ENGLISH).equals("ok");
                    rejectSuccess = updated_req;
                }
                else {
                    System.out.println(string_out);
                    isRejected = false;
                }
                updateAfterReject();
            }
            catch (JSONException e) {
                e.printStackTrace();
                isRejected = false;
                updateAfterReject();
            }
        }, error -> {
            error.printStackTrace();
            rrreeejjjeecctt = false;
            isRejected = false;
            updateAfterReject();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_LA_ID",la_id);
                headers.put("P_EMP_CODE",emp_code);
                headers.put("P_TEXT_COMMENTS",textLA);
                return headers;
            }
        };

        requestQueue.add(rejectReq);
    }

    private void updateAfterReject() {
        waitProgress.dismiss();
        if (rrreeejjjeecctt) {
            if (isRejected) {
                if (isRejectedChecked) {
                    req_code_leave = "";
                    la_id = "";
                    la_emp_id = "";
                    leaveReqList = new ArrayList<>();
                    System.out.println("INSERTED");

                    AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                            .setMessage("Leave Rejected Successfully")
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
                    Toast.makeText(getApplicationContext(), rejectSuccess, Toast.LENGTH_SHORT).show();
                }
                rrreeejjjeecctt = false;
                isRejected = false;
                isRejectedChecked = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    leaveRejectProcess();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(LeaveApprove.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                leaveRejectProcess();
                dialog.dismiss();
            });
        }
    }
}