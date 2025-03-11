package ttit.com.shuvo.eliteforce.login;

import static ttit.com.shuvo.eliteforce.utility.Constants.CONTACT;
import static ttit.com.shuvo.eliteforce.utility.Constants.DEPT_ID;
import static ttit.com.shuvo.eliteforce.utility.Constants.DEPT_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.DESG_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.DESG_PRIORITY;
import static ttit.com.shuvo.eliteforce.utility.Constants.DESIG_ID;
import static ttit.com.shuvo.eliteforce.utility.Constants.DIV_ID;
import static ttit.com.shuvo.eliteforce.utility.Constants.DIV_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.EMAIL;
import static ttit.com.shuvo.eliteforce.utility.Constants.EMP_ID_LOGIN;
import static ttit.com.shuvo.eliteforce.utility.Constants.ISP_USR_TF;
import static ttit.com.shuvo.eliteforce.utility.Constants.IS_ATT_APPROVED;
import static ttit.com.shuvo.eliteforce.utility.Constants.IS_LEAVE_APPROVED;
import static ttit.com.shuvo.eliteforce.utility.Constants.JOINING_DATE;
import static ttit.com.shuvo.eliteforce.utility.Constants.JSD_ID_LOGIN;
import static ttit.com.shuvo.eliteforce.utility.Constants.JSD_OBJECTIVE;
import static ttit.com.shuvo.eliteforce.utility.Constants.JSM_CODE;
import static ttit.com.shuvo.eliteforce.utility.Constants.JSM_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.LIVE_FLAG;
import static ttit.com.shuvo.eliteforce.utility.Constants.LOGIN_ACTIVITY_FILE;
import static ttit.com.shuvo.eliteforce.utility.Constants.LOGIN_TF;
import static ttit.com.shuvo.eliteforce.utility.Constants.MyPREFERENCES;
import static ttit.com.shuvo.eliteforce.utility.Constants.USER_F_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.USER_L_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.USER_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.USR_LOGIN_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.WIDGET_EMP_ID;
import static ttit.com.shuvo.eliteforce.utility.Constants.WIDGET_FILE;
import static ttit.com.shuvo.eliteforce.utility.Constants.WIDGET_TRACKER_FLAG;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;
import static ttit.com.shuvo.eliteforce.utility.Constants.checked;
import static ttit.com.shuvo.eliteforce.utility.Constants.user_emp_code;
import static ttit.com.shuvo.eliteforce.utility.Constants.user_password;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.login.model.UserDesignation;
import ttit.com.shuvo.eliteforce.login.model.UserInfoList;
import ttit.com.shuvo.eliteforce.mainPage.MainMenu;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class Login extends AppCompatActivity {
    TextInputEditText user;
    TextInputEditText pass;
    TextView login_failed;
    MaterialButton login;
    CheckBox checkBox;

    String userName = "";
    String password = "";
    public static int isApproved = 0;
    public static int isLeaveApproved = 0;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean adminConnected = false;
    private Boolean noUser = false;
    private Boolean connected = false;

    SharedPreferences sharedpreferences;

    SharedPreferences sharedLogin;

    SharedPreferences attendanceWidgetPreferences;

    String getUserName = "";
    String getPassword = "";
    boolean getChecked = false;

    String userId = "";
    public static ArrayList<UserInfoList> userInfoLists;
    public static ArrayList<UserDesignation> userDesignations;

    String emp_id = "";
    String emp_code = "";
    int live_loc_flag = 0;
    String tracker_flag = "";
    String di_id = "";

    Logger logger = Logger.getLogger(Login.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.user_name_given);
        pass = findViewById(R.id.password_given);
        checkBox = findViewById(R.id.remember_checkbox);

        login_failed = findViewById(R.id.email_pass_miss);

        login = findViewById(R.id.login_button);

        userInfoLists = new ArrayList<>();
        userDesignations = new ArrayList<>();

        sharedLogin = getSharedPreferences(LOGIN_ACTIVITY_FILE,MODE_PRIVATE);
        attendanceWidgetPreferences = getSharedPreferences(WIDGET_FILE,MODE_PRIVATE);

        sharedpreferences = getSharedPreferences(MyPREFERENCES,MODE_PRIVATE);
        getUserName = sharedpreferences.getString(user_emp_code,null);
        getPassword = sharedpreferences.getString(user_password,null);
        getChecked = sharedpreferences.getBoolean(checked,false);

        if (getUserName != null) {
            user.setText(getUserName);
        }
        if (getPassword != null) {
            pass.setText(getPassword);
        }
        checkBox.setChecked(getChecked);

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                login_failed.setVisibility(View.GONE);
            }
        });
        user.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                login_failed.setVisibility(View.GONE);
            }
        });

        pass.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    pass.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        login.setOnClickListener(v -> {

            closeKeyBoard();

            login_failed.setVisibility(View.GONE);
            userName = Objects.requireNonNull(user.getText()).toString();
            password = Objects.requireNonNull(pass.getText()).toString();

            if (!userName.isEmpty() && !password.isEmpty()) {
                if (!userName.equals("admin")) {
//                        new CheckLogin().execute();
                    loginCheck();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Admin can not login to this app", Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(getApplicationContext(), "Please Give User Name and Password", Toast.LENGTH_SHORT).show();
            }

        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(Login.this);
                builder.setTitle("EXIT!")
                        .setIcon(R.drawable.elite_force_logo)
                        .setMessage("Do you want to EXIT?")
                        .setPositiveButton("YES", (dialog, which) -> System.exit(0))
                        .setNegativeButton("NO", (dialog, which) -> {

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void closeKeyBoard () {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onTouchEvent (MotionEvent event){
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    public void loginCheck() {
        waitProgress.show(getSupportFragmentManager(), "WaitBar");
        waitProgress.setCancelable(false);
        userInfoLists = new ArrayList<>();
        userDesignations = new ArrayList<>();
        isApproved = 0;
        isLeaveApproved = 0;
        live_loc_flag = 0;
        conn = false;
        connected = false;
        infoConnected = false;
        userId = "";

        String useridUrl = api_url_front+"login/getUserId/"+userName+"/"+password;

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        StringRequest getUserId = new StringRequest(Request.Method.GET, useridUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject userIdInfo = array.getJSONObject(i);
                        userId = userIdInfo.getString("val")
                                .equals("null") ? "" : userIdInfo.getString("val");
                    }
                }
                if (userId.isEmpty() || userId.equals("-1")) {
//                    goToDashboard();
                    getDriverLogin();
                }
                else {
                    getEmpCode(userId);
                }
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }

        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        requestQueue.add(getUserId);
    }

    public void getEmpCode(String u_id) {
        emp_code = "";
        emp_id = "";
        adminConnected = false;
        noUser = false;
        String empCodeUrl = api_url_front+"login/getEmpCodebyUser/"+u_id;
        String userInfoUrl = api_url_front+"login/getUserInfo/"+u_id;

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        StringRequest userInfoRequest = new StringRequest(Request.Method.GET, userInfoUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject userInfo = array.getJSONObject(i);

                        String usr_fname = userInfo.getString("usr_fname")
                                .equals("null") ? "" : userInfo.getString("usr_fname");
                        String usr_lname = userInfo.getString("usr_lname")
                                .equals("null") ? "" : userInfo.getString("usr_lname");
                        String usr_email = userInfo.getString("usr_email")
                                .equals("null") ? "" : userInfo.getString("usr_email");
                        String usr_contact = userInfo.getString("usr_contact")
                                .equals("null") ? "" : userInfo.getString("usr_contact");
                        String usr_emp_id = userInfo.getString("usr_emp_id")
                                .equals("null") ? "0" : userInfo.getString("usr_emp_id");
                        String usr_name = userInfo.getString("usr_name")
                                .equals("null") ? "" : userInfo.getString("usr_name");

                        emp_id = usr_emp_id;
                        System.out.println(emp_id);
                        userInfoLists.add(new UserInfoList(emp_code,usr_fname,usr_lname,usr_email,usr_contact,usr_emp_id, usr_name,true));
                    }
                }
                getUserDetails();
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        StringRequest empCodeRequest = new StringRequest(Request.Method.GET, empCodeUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empCodeInfo = array.getJSONObject(i);
                        emp_code = empCodeInfo.getString("valu")
                                .equals("null") ? "0" : empCodeInfo.getString("valu");
                    }
                    if (emp_code.equals("0000")) {
                        adminConnected = true;
                        goToDashboard();
                    }
                    else if (!emp_code.equals("NO USER FOUND")) {
                        adminConnected = false;
                        noUser = false;
                        requestQueue.add(userInfoRequest);
                    }
                    else {
                        adminConnected = false;
                        noUser = true;
                        goToDashboard();
                    }
                }
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }

        },error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        requestQueue.add(empCodeRequest);
    }

    public void getDriverLogin() {
        di_id = "";
        String useridUrl = api_url_front+"login/getDriverUser?d_name="+userName+"&d_pass="+password;

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        StringRequest userInfoRequest = new StringRequest(Request.Method.GET, useridUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject userInfo = array.getJSONObject(i);

                        String usr_fname = userInfo.getString("usr_fname")
                                .equals("null") ? "" : userInfo.getString("usr_fname");
                        String usr_lname = userInfo.getString("usr_lname")
                                .equals("null") ? "" : userInfo.getString("usr_lname");
                        String usr_email = userInfo.getString("usr_email")
                                .equals("null") ? "" : userInfo.getString("usr_email");
                        String usr_contact = userInfo.getString("usr_contact")
                                .equals("null") ? "" : userInfo.getString("usr_contact");
                        String usr_emp_id = userInfo.getString("usr_emp_id")
                                .equals("null") ? "0" : userInfo.getString("usr_emp_id");
                        String usr_name = userInfo.getString("usr_name")
                                .equals("null") ? "" : userInfo.getString("usr_name");

                        emp_id = usr_emp_id;
                        di_id = usr_name;
                        userInfoLists.add(new UserInfoList("",usr_fname,usr_lname,usr_email,usr_contact,usr_emp_id, usr_name, false));
                    }
                }
                if (di_id.isEmpty()) {
                    goToDashboard();
                }
                else {
                    getDriverEmpCode();
                }
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        requestQueue.add(userInfoRequest);
    }

    public void getDriverEmpCode() {
        String empCodeUrl = api_url_front+"login/getDriverEmpCode?p_emp_id="+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        StringRequest empCodeRequest = new StringRequest(Request.Method.GET, empCodeUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empCodeInfo = array.getJSONObject(i);
                        emp_code = empCodeInfo.getString("valu")
                                .equals("null") ? "0" : empCodeInfo.getString("valu");
                    }
                    if (emp_code.equals("0000")) {
                        adminConnected = true;
                        goToDashboard();
                    }
                    else if (!emp_code.equals("NO USER FOUND")) {
                        adminConnected = false;
                        noUser = false;
                        userInfoLists.get(0).setUserName(emp_code);
                        getUserDetails();
                    }
                    else {
                        adminConnected = false;
                        noUser = true;
                        goToDashboard();
                    }
                }
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }

        },error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        requestQueue.add(empCodeRequest);
    }

    public void getUserDetails() {
        String designationUrl = api_url_front+"login/getUserDesignations/"+emp_id;
        String attendanceAppUrl = api_url_front+"approval_flag/getAttendanceApproval/"+emp_code;
        String leaveAppUrl = api_url_front+"approval_flag/getLeaveApproval/"+emp_code;
        String liveLocFlagUrl = api_url_front+"utility/getLiveLocationFlag/"+emp_code;
        String updateEmpFlagUrl = api_url_front+"login/updateEmpFlag";

        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);

        StringRequest updateFlag = new StringRequest(Request.Method.POST, updateEmpFlagUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    infoConnected = true;
                }
                else {
                    System.out.println(string_out);
                    connected = false;
                }
                goToDashboard();
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_EMP_ID", emp_id);
                return headers;
            }
        };

        StringRequest livLocFlReq = new StringRequest(Request.Method.GET, liveLocFlagUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject livLocFlInfo = array.getJSONObject(i);
                        live_loc_flag = Integer.parseInt(livLocFlInfo.getString("emp_live_loc_tracker_flag")
                                .equals("null") ? "0" : livLocFlInfo.getString("emp_live_loc_tracker_flag"));
                        tracker_flag = livLocFlInfo.getString("emp_timeline_tracker_flag")
                                .equals("null") ? "0" :livLocFlInfo.getString("emp_timeline_tracker_flag");
                    }
                }
                requestQueue.add(updateFlag);
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        StringRequest leaveAppReq = new StringRequest(Request.Method.GET, leaveAppUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject leaveAppInfo = array.getJSONObject(i);
                        isLeaveApproved = Integer.parseInt(leaveAppInfo.getString("l_val")
                                .equals("null") ? "0" : leaveAppInfo.getString("l_val"));
                    }
                }
                requestQueue.add(livLocFlReq);
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        },error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        StringRequest attendAppReq = new StringRequest(Request.Method.GET,attendanceAppUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject attAppInfo = array.getJSONObject(i);
                        isApproved = Integer.parseInt(attAppInfo.getString("val")
                                .equals("null") ? "0" : attAppInfo.getString("val"));
                    }
                }
                requestQueue.add(leaveAppReq);
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        },error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        StringRequest designationRequest = new StringRequest(Request.Method.GET, designationUrl, response -> {
            conn = true;
            try {
                connected = true;
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject desigInfo = array.getJSONObject(i);
                        String jsm_code = desigInfo.getString("jsm_code")
                                .equals("null") ? "" : desigInfo.getString("jsm_code");
                        String temp_title = desigInfo.getString("temp_title")
                                .equals("null") ? "" : desigInfo.getString("temp_title");
                        String jsd_id = desigInfo.getString("jsd_id")
                                .equals("null") ? "" : desigInfo.getString("jsd_id");
                        String jsd_objective = desigInfo.getString("jsd_objective")
                                .equals("null") ? "" : desigInfo.getString("jsd_objective");
                        String dept_name = desigInfo.getString("dept_name")
                                .equals("null") ? "" : desigInfo.getString("dept_name");
                        String divm_name = desigInfo.getString("divm_name")
                                .equals("null") ? "" : desigInfo.getString("divm_name");
                        String desig_name = desigInfo.getString("desig_name")
                                .equals("null") ? "" : desigInfo.getString("desig_name");
                        String desig_priority = desigInfo.getString("desig_priority")
                                .equals("null") ? "" : desigInfo.getString("desig_priority");
                        String joiningdate = desigInfo.getString("joiningdate")
                                .equals("null") ? "" : desigInfo.getString("joiningdate");
                        String divm_id = desigInfo.getString("divm_id")
                                .equals("null") ? "" : desigInfo.getString("divm_id");
                        String dept_id = desigInfo.getString("dept_id")
                                .equals("null") ? "" : desigInfo.getString("dept_id");
                        String desig_id = desigInfo.getString("desig_id")
                                .equals("null") ? "" : desigInfo.getString("desig_id");

                        userDesignations.add(new UserDesignation(jsm_code,temp_title,jsd_id,jsd_objective,dept_name,divm_name,desig_name,desig_priority,joiningdate,divm_id,dept_id,desig_id));
                    }
                }
                requestQueue.add(attendAppReq);
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                goToDashboard();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            goToDashboard();
        });

        requestQueue.add(designationRequest);
    }

    public void goToDashboard() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (!userId.equals("-1") && !userId.isEmpty()) {
                    if (adminConnected) {
                        Toast.makeText(getApplicationContext(),"Admin can not access this app.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (noUser) {
                            Toast.makeText(getApplicationContext(),"No User Found.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (infoConnected) {
                                if (checkBox.isChecked()) {
                                    System.out.println("Remembered");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.remove(user_emp_code);
                                    editor.remove(user_password);
                                    editor.remove(checked);
                                    editor.putString(user_emp_code,userName);
                                    editor.putString(user_password,password);
                                    editor.putBoolean(checked,true);
                                    editor.apply();
                                    editor.commit();
                                } else {
                                    System.out.println("Not Remembered");
                                }

                                SharedPreferences.Editor widgetEditor = attendanceWidgetPreferences.edit();
                                widgetEditor.remove(WIDGET_EMP_ID);
                                widgetEditor.remove(WIDGET_TRACKER_FLAG);

                                widgetEditor.putString(WIDGET_EMP_ID, userInfoLists.get(0).getEmp_id());
                                widgetEditor.putString(WIDGET_TRACKER_FLAG, tracker_flag);
                                widgetEditor.apply();
                                widgetEditor.commit();

                                SharedPreferences.Editor editor1 = sharedLogin.edit();
                                editor1.remove(USER_NAME);
                                editor1.remove(USER_F_NAME);
                                editor1.remove(USER_L_NAME);
                                editor1.remove(EMAIL);
                                editor1.remove(CONTACT);
                                editor1.remove(EMP_ID_LOGIN);
                                editor1.remove(USR_LOGIN_NAME);
                                editor1.remove(ISP_USR_TF);

                                editor1.remove(JSM_CODE);
                                editor1.remove(JSM_NAME);
                                editor1.remove(JSD_ID_LOGIN);
                                editor1.remove(JSD_OBJECTIVE);
                                editor1.remove(DEPT_NAME);
                                editor1.remove(DIV_NAME);
                                editor1.remove(DESG_NAME);
                                editor1.remove(DESG_PRIORITY);
                                editor1.remove(JOINING_DATE);
                                editor1.remove(DIV_ID);
                                editor1.remove(DEPT_ID);
                                editor1.remove(DESIG_ID);
                                editor1.remove(LOGIN_TF);

                                editor1.remove(IS_ATT_APPROVED);
                                editor1.remove(IS_LEAVE_APPROVED);
                                editor1.remove(LIVE_FLAG);
//                                editor1.remove(DATABASE_NAME);

                                editor1.putString(USER_NAME, userInfoLists.get(0).getUserName());
                                editor1.putString(USER_F_NAME, userInfoLists.get(0).getUser_fname());
                                editor1.putString(USER_L_NAME, userInfoLists.get(0).getUser_lname());
                                editor1.putString(EMAIL, userInfoLists.get(0).getEmail());
                                editor1.putString(CONTACT, userInfoLists.get(0).getContact());
                                editor1.putString(EMP_ID_LOGIN, userInfoLists.get(0).getEmp_id());
                                editor1.putString(USR_LOGIN_NAME, userInfoLists.get(0).getUsr_name());
                                editor1.putBoolean(ISP_USR_TF, userInfoLists.get(0).isIsp_user());

                                if (!userDesignations.isEmpty()) {
                                    editor1.putString(JSM_CODE, userDesignations.get(0).getJsm_code());
                                    editor1.putString(JSM_NAME, userDesignations.get(0).getJsm_name());
                                    editor1.putString(JSD_ID_LOGIN, userDesignations.get(0).getJsd_id());
                                    editor1.putString(JSD_OBJECTIVE, userDesignations.get(0).getJsd_objective());
                                    editor1.putString(DEPT_NAME, userDesignations.get(0).getDept_name());
                                    editor1.putString(DIV_NAME, userDesignations.get(0).getDiv_name());
                                    editor1.putString(DESG_NAME, userDesignations.get(0).getDesg_name());
                                    editor1.putString(DESG_PRIORITY, userDesignations.get(0).getDesg_priority());
                                    editor1.putString(JOINING_DATE, userDesignations.get(0).getJoining_date());
                                    editor1.putString(DIV_ID, userDesignations.get(0).getDiv_id());
                                    editor1.putString(DEPT_ID, userDesignations.get(0).getDept_id());
                                    editor1.putString(DESIG_ID, userDesignations.get(0).getDesig_id());
                                } else {
                                    editor1.putString(JSM_CODE, null);
                                    editor1.putString(JSM_NAME, null);
                                    editor1.putString(JSD_ID_LOGIN, null);
                                    editor1.putString(JSD_OBJECTIVE, null);
                                    editor1.putString(DEPT_NAME, null);
                                    editor1.putString(DIV_NAME, null);
                                    editor1.putString(DESG_NAME, null);
                                    editor1.putString(DESG_PRIORITY, null);
                                    editor1.putString(JOINING_DATE, null);
                                    editor1.putString(DIV_ID, null);
                                    editor1.putString(DEPT_ID, null);
                                    editor1.putString(DESIG_ID, null);
                                }

                                editor1.putBoolean(LOGIN_TF,true);

                                editor1.putInt(IS_ATT_APPROVED, isApproved);
                                editor1.putInt(IS_LEAVE_APPROVED, isLeaveApproved);
                                editor1.putInt(LIVE_FLAG,live_loc_flag);
//                                editor1.putString(DATABASE_NAME,DEFAULT_USERNAME);
                                editor1.apply();
                                editor1.commit();


                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                loginCheck();
                            }
                        }
                    }
                }
                else if (!di_id.isEmpty()) {
                    if (adminConnected) {
                        Toast.makeText(getApplicationContext(),"Admin can not access this app.",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        if (noUser) {
                            Toast.makeText(getApplicationContext(),"No User Found.",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (infoConnected) {
                                if (checkBox.isChecked()) {
                                    System.out.println("Remembered");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.remove(user_emp_code);
                                    editor.remove(user_password);
                                    editor.remove(checked);
                                    editor.putString(user_emp_code,userName);
                                    editor.putString(user_password,password);
                                    editor.putBoolean(checked,true);
                                    editor.apply();
                                    editor.commit();
                                } else {
                                    System.out.println("Not Remembered");
                                }

                                SharedPreferences.Editor widgetEditor = attendanceWidgetPreferences.edit();
                                widgetEditor.remove(WIDGET_EMP_ID);
                                widgetEditor.remove(WIDGET_TRACKER_FLAG);

                                widgetEditor.putString(WIDGET_EMP_ID, userInfoLists.get(0).getEmp_id());
                                widgetEditor.putString(WIDGET_TRACKER_FLAG, tracker_flag);
                                widgetEditor.apply();
                                widgetEditor.commit();

                                SharedPreferences.Editor editor1 = sharedLogin.edit();
                                editor1.remove(USER_NAME);
                                editor1.remove(USER_F_NAME);
                                editor1.remove(USER_L_NAME);
                                editor1.remove(EMAIL);
                                editor1.remove(CONTACT);
                                editor1.remove(EMP_ID_LOGIN);
                                editor1.remove(USR_LOGIN_NAME);
                                editor1.remove(ISP_USR_TF);

                                editor1.remove(JSM_CODE);
                                editor1.remove(JSM_NAME);
                                editor1.remove(JSD_ID_LOGIN);
                                editor1.remove(JSD_OBJECTIVE);
                                editor1.remove(DEPT_NAME);
                                editor1.remove(DIV_NAME);
                                editor1.remove(DESG_NAME);
                                editor1.remove(DESG_PRIORITY);
                                editor1.remove(JOINING_DATE);
                                editor1.remove(DIV_ID);
                                editor1.remove(DEPT_ID);
                                editor1.remove(DESIG_ID);
                                editor1.remove(LOGIN_TF);

                                editor1.remove(IS_ATT_APPROVED);
                                editor1.remove(IS_LEAVE_APPROVED);
                                editor1.remove(LIVE_FLAG);
//                                editor1.remove(DATABASE_NAME);

                                editor1.putString(USER_NAME, userInfoLists.get(0).getUserName());
                                editor1.putString(USER_F_NAME, userInfoLists.get(0).getUser_fname());
                                editor1.putString(USER_L_NAME, userInfoLists.get(0).getUser_lname());
                                editor1.putString(EMAIL, userInfoLists.get(0).getEmail());
                                editor1.putString(CONTACT, userInfoLists.get(0).getContact());
                                editor1.putString(EMP_ID_LOGIN, userInfoLists.get(0).getEmp_id());
                                editor1.putString(USR_LOGIN_NAME, userInfoLists.get(0).getUsr_name());
                                editor1.putBoolean(ISP_USR_TF, userInfoLists.get(0).isIsp_user());

                                if (!userDesignations.isEmpty()) {
                                    editor1.putString(JSM_CODE, userDesignations.get(0).getJsm_code());
                                    editor1.putString(JSM_NAME, userDesignations.get(0).getJsm_name());
                                    editor1.putString(JSD_ID_LOGIN, userDesignations.get(0).getJsd_id());
                                    editor1.putString(JSD_OBJECTIVE, userDesignations.get(0).getJsd_objective());
                                    editor1.putString(DEPT_NAME, userDesignations.get(0).getDept_name());
                                    editor1.putString(DIV_NAME, userDesignations.get(0).getDiv_name());
                                    editor1.putString(DESG_NAME, userDesignations.get(0).getDesg_name());
                                    editor1.putString(DESG_PRIORITY, userDesignations.get(0).getDesg_priority());
                                    editor1.putString(JOINING_DATE, userDesignations.get(0).getJoining_date());
                                    editor1.putString(DIV_ID, userDesignations.get(0).getDiv_id());
                                    editor1.putString(DEPT_ID, userDesignations.get(0).getDept_id());
                                    editor1.putString(DESIG_ID, userDesignations.get(0).getDesig_id());
                                } else {
                                    editor1.putString(JSM_CODE, null);
                                    editor1.putString(JSM_NAME, null);
                                    editor1.putString(JSD_ID_LOGIN, null);
                                    editor1.putString(JSD_OBJECTIVE, null);
                                    editor1.putString(DEPT_NAME, null);
                                    editor1.putString(DIV_NAME, null);
                                    editor1.putString(DESG_NAME, null);
                                    editor1.putString(DESG_PRIORITY, null);
                                    editor1.putString(JOINING_DATE, null);
                                    editor1.putString(DIV_ID, null);
                                    editor1.putString(DEPT_ID, null);
                                    editor1.putString(DESIG_ID, null);
                                }

                                editor1.putBoolean(LOGIN_TF,true);

                                editor1.putInt(IS_ATT_APPROVED, isApproved);
                                editor1.putInt(IS_LEAVE_APPROVED, isLeaveApproved);
                                editor1.putInt(LIVE_FLAG,live_loc_flag);
//                                editor1.putString(DATABASE_NAME,DEFAULT_USERNAME);
                                editor1.apply();
                                editor1.commit();


                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainMenu.class);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                loginCheck();
                            }
                        }
                    }
                }
                else {
                    login_failed.setVisibility(View.VISIBLE);
                }
                conn = false;
                connected = false;
                adminConnected = false;
                infoConnected = false;
                noUser = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    loginCheck();
                    dialog.dismiss();
                });
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            AlertDialog dialog = new AlertDialog.Builder(Login.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                loginCheck();
                dialog.dismiss();
            });
        }
    }
}