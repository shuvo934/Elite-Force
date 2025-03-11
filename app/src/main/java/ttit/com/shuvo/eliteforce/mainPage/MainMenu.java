package ttit.com.shuvo.eliteforce.mainPage;

import static ttit.com.shuvo.eliteforce.dashboard.Dashboard.alarmManager;
import static ttit.com.shuvo.eliteforce.dashboard.Dashboard.selectedImage;
import static ttit.com.shuvo.eliteforce.dashboard.Dashboard.trackerAvailable;
import static ttit.com.shuvo.eliteforce.login.Login.isApproved;
import static ttit.com.shuvo.eliteforce.login.Login.isLeaveApproved;
import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
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
import static ttit.com.shuvo.eliteforce.utility.Constants.SCHEDULING_EMP_ID;
import static ttit.com.shuvo.eliteforce.utility.Constants.SCHEDULING_FILE;
import static ttit.com.shuvo.eliteforce.utility.Constants.TRIGGERING;
import static ttit.com.shuvo.eliteforce.utility.Constants.USER_F_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.USER_L_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.USER_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.USR_LOGIN_NAME;
import static ttit.com.shuvo.eliteforce.utility.Constants.WIDGET_EMP_ID;
import static ttit.com.shuvo.eliteforce.utility.Constants.WIDGET_FILE;
import static ttit.com.shuvo.eliteforce.utility.Constants.WIDGET_TRACKER_FLAG;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.Attendance;
import ttit.com.shuvo.eliteforce.attendance.give_attendance.arraylists.AreaLists;
import ttit.com.shuvo.eliteforce.attendance.give_attendance.dialogs.AttendanceRequest;
import ttit.com.shuvo.eliteforce.check_in_out.CheckInOut;
import ttit.com.shuvo.eliteforce.fleet.driver_assignment.DriverAssignment;
import ttit.com.shuvo.eliteforce.movement_reg.MovementRegister;
import ttit.com.shuvo.eliteforce.attendance.trackService.Service;
import ttit.com.shuvo.eliteforce.directory.Directory;
import ttit.com.shuvo.eliteforce.employeeInfo.EmployeeInformation;
import ttit.com.shuvo.eliteforce.fleet.FleetRequisition;
import ttit.com.shuvo.eliteforce.leave.Leave;
import ttit.com.shuvo.eliteforce.login.Login;
import ttit.com.shuvo.eliteforce.login.model.UserDesignation;
import ttit.com.shuvo.eliteforce.login.model.UserInfoList;
import ttit.com.shuvo.eliteforce.payRoll.PayRollInfo;
import ttit.com.shuvo.eliteforce.recruitment.RecruitmentApproval;
import ttit.com.shuvo.eliteforce.scheduler.Uploader;
import ttit.com.shuvo.eliteforce.tracking.VehicleTracking;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class MainMenu extends AppCompatActivity {

    TextView userName;
    TextView designation;
    TextView department;
    ImageView userImage;

    MaterialCardView personalInfo;
    MaterialCardView attendance;
    MaterialCardView leave;
    MaterialCardView payRoll;
    MaterialCardView directory;
    MaterialCardView fleetReq;
    MaterialCardView recruitApp;
    MaterialCardView movReg;
    MaterialCardView checkIn;
    MaterialCardView vhTracking;

    TextView todayDate;
    TextView inTimeAtt;
    TextView outTimeAtt;

    TextView leaveName;
    TextView lvFromDate;
    TextView lvToDate;

    TextView totalFlAssign;
    TextView comFlAssign;
    TextView penFlAssign;

    TextView totalMove;

    ImageButton refresh;

    LinearLayout attLayout;
    LinearLayout leaveLayout;
    LinearLayout flAssignLayout;
    LinearLayout totMoveLayout;
    CircularProgressIndicator circularProgressIndicator;

    ImageView dashB;
    ImageView logoutImage;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedSchedule;
    SharedPreferences attendanceWidgetPreferences;

    TextView welcomeText;
    boolean isIsp = false;

    String intTimeAttBot = "";
    String outTimeAttBot = "";
    String leave_name = "";
    String leave_frm_date = "";
    String leave_to_date = "";
    String emp_id = "";
    String lastDateForAttBot = "";
    String today_date = "";
    String di_id = "";
    String tot_move = "";
    String tot_fl_assign = "";
    String com_fl_assign = "";
    String pen_fl_assign = "";

    FloatingActionButton floatingActionButton;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationRequest locationRequest;

    String inTime = "";
    String address = "";
    LatLng[] lastLatLongitude = {new LatLng(0, 0)};
    String lat = "";
    String lon = "";
    Timestamp ts;
    String timeToShow = "";
    String officeLatitude = "";
    String officeLongitude = "";
    String coverage = "";
    String machineCode = "";

    ArrayList<AreaLists> areaLists;

    Logger logger = Logger.getLogger(MainMenu.class.getName());

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    boolean imageFound = false;

    LinearLayout lastLay;
    BottomAppBar bottomBarLay;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        attendanceWidgetPreferences = getSharedPreferences(WIDGET_FILE,MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        boolean loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);
        sharedSchedule = getSharedPreferences(SCHEDULING_FILE, MODE_PRIVATE);

        if(loginfile) {
            String userName = sharedPreferences.getString(USER_NAME, null);
            String userFname = sharedPreferences.getString(USER_F_NAME, null);
            String userLname = sharedPreferences.getString(USER_L_NAME,null);
            String email = sharedPreferences.getString(EMAIL,null);
            String contact = sharedPreferences.getString(CONTACT, null);
            String emp_id_login = sharedPreferences.getString(EMP_ID_LOGIN,null);
            String usr_login_name = sharedPreferences.getString(USR_LOGIN_NAME,null);
            boolean isp = sharedPreferences.getBoolean(ISP_USR_TF,false);
            isIsp = isp;

            userInfoLists = new ArrayList<>();
            userInfoLists.add(new UserInfoList(userName,userFname,userLname,email,contact,emp_id_login,usr_login_name,isp));

            String jsm_code = sharedPreferences.getString(JSM_CODE, null);
            String jsm_name = sharedPreferences.getString(JSM_NAME, null);
            String jsd_id = sharedPreferences.getString(JSD_ID_LOGIN,null);
            String jsd_obj = sharedPreferences.getString(JSD_OBJECTIVE,null);
            String dept_name = sharedPreferences.getString(DEPT_NAME, null);
            String div_name = sharedPreferences.getString(DIV_NAME, null);
            String desg_name = sharedPreferences.getString(DESG_NAME, null);
            String desg_priority = sharedPreferences.getString(DESG_PRIORITY,null);
            String joining = sharedPreferences.getString(JOINING_DATE, null);
            String div_id = sharedPreferences.getString(DIV_ID,null);
            String dept_id = sharedPreferences.getString(DEPT_ID,null);
            String desig_id = sharedPreferences.getString(DESIG_ID,null);

            userDesignations = new ArrayList<>();
            userDesignations.add(new UserDesignation(jsm_code,jsm_name,jsd_id,jsd_obj,dept_name,div_name,desg_name,desg_priority,joining,div_id,dept_id,desig_id));

            isApproved = sharedPreferences.getInt(IS_ATT_APPROVED,0);
            isLeaveApproved = sharedPreferences.getInt(IS_LEAVE_APPROVED,0);
        }
        else {
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainMenu.this);
            builder.setTitle("No Data Found!")
                    .setIcon(R.drawable.elite_force_logo)
                    .setMessage("We could not found any data according to your user id. Please login again.")
                    .setPositiveButton("OK", (dialog, which) -> {


                        userInfoLists.clear();
                        userDesignations.clear();
                        userInfoLists = new ArrayList<>();
                        userDesignations = new ArrayList<>();
                        isApproved = 0;
                        isLeaveApproved = 0;

                        SharedPreferences.Editor widgetEditor = attendanceWidgetPreferences.edit();
                        widgetEditor.remove(WIDGET_EMP_ID);
                        widgetEditor.remove(WIDGET_TRACKER_FLAG);
                        widgetEditor.apply();
                        widgetEditor.commit();

                        SharedPreferences.Editor editor1 = sharedPreferences.edit();
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
                        editor1.apply();
                        editor1.commit();

                        if (trackerAvailable == 1) {
                            SharedPreferences.Editor editor = sharedSchedule.edit();
                            editor.remove(SCHEDULING_EMP_ID);
                            editor.remove(TRIGGERING);
                            editor.apply();
                            editor.commit();

                            Intent intent1 = new Intent(MainMenu.this, Uploader.class);
                            PendingIntent pendingIntent;
                            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, PendingIntent.FLAG_IMMUTABLE);
                            alarmManager.cancel(pendingIntent);
                        }

                        Intent intent = new Intent(MainMenu.this, Login.class);
                        startActivity(intent);
                        finish();
                    });
            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.setCanceledOnTouchOutside(false);
            alert.show();
        }

        dashB = findViewById(R.id.dashboard_icon);
        logoutImage = findViewById(R.id.log_out_icon_main_menu);

        userName = findViewById(R.id.user_name);
        department = findViewById(R.id.user_depart);
        designation = findViewById(R.id.user_desg);
        userImage = findViewById(R.id.user_pic);

        personalInfo = findViewById(R.id.personal_info);
        attendance = findViewById(R.id.attendanceee);
        leave = findViewById(R.id.leave_all);
        payRoll = findViewById(R.id.pay_roll_info);
        directory = findViewById(R.id.directory);
        fleetReq = findViewById(R.id.fleet);
        recruitApp = findViewById(R.id.recruitment_approval_rec_center);
        movReg = findViewById(R.id.movement_register_button);
        checkIn = findViewById(R.id.check_in_check_out);
        vhTracking = findViewById(R.id.vehicle_tracking_main_menu);

        todayDate = findViewById(R.id.today_date_notice_in_main_menu);
        inTimeAtt = findViewById(R.id.att_in_time_in_main_menu);
        outTimeAtt = findViewById(R.id.att_out_time_in_main_menu);

        leaveName = findViewById(R.id.leave_type_name_in_main_menu);
        lvFromDate = findViewById(R.id.leave_from_date_in_main_menu);
        lvToDate = findViewById(R.id.leave_to_date_in_main_menu);

        totalFlAssign = findViewById(R.id.total_fl_assign_in_main_menu);
        comFlAssign = findViewById(R.id.completed_fl_assign_in_main_menu);
        penFlAssign = findViewById(R.id.pending_fl_assign_in_main_menu);

        totalMove = findViewById(R.id.total_movement_in_day_in_main_menu);

        refresh = findViewById(R.id.refresh_summary_in_main_menu);

        attLayout = findViewById(R.id.attendance_layout_main_menu);
        leaveLayout = findViewById(R.id.leave_layout_main_menu);
        flAssignLayout = findViewById(R.id.fleet_assignment_layout_main_menu);
        totMoveLayout = findViewById(R.id.total_movement_layout_main_menu);
        circularProgressIndicator = findViewById(R.id.progress_indicator_main_menu);
        circularProgressIndicator.setVisibility(View.GONE);

        bottomBarLay = findViewById(R.id.bottomAppbar);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        lastLay = findViewById(R.id.last_layout_of_main_menu);

        welcomeText = findViewById(R.id.welcome_text_view_main_menu);

        floatingActionButton = findViewById(R.id.attendance_shortcut_main_menu);

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String wt;
        if (currentHour >= 4 && currentHour <= 11) {
            wt = "GOOD MORNING,";
        }
        else if (currentHour >= 12 && currentHour <= 16) {
            wt = "GOOD AFTERNOON,";
        }
        else if (currentHour >= 17 && currentHour <= 22) {
            wt = "GOOD EVENING,";
        }
        else {
            wt = "HELLO,";
        }
        welcomeText.setText(wt);

        if (selectedImage != null) {
            Glide.with(getApplicationContext())
                    .load(selectedImage)
                    .fitCenter()
                    .into(userImage);
        }
        else {
            userImage.setImageResource(R.drawable.profile);
        }

        if (!userInfoLists.isEmpty()) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            String empFullName = firstname+" "+lastName;
            userName.setText(empFullName);
        }

        if (!userDesignations.isEmpty()) {
            String jsmName = userDesignations.get(0).getJsm_name();
            if (jsmName == null) {
                jsmName = "";
            }
            designation.setText(jsmName);

            String deptName = userDesignations.get(0).getDiv_name();
            if (deptName == null) {
                deptName = "";
            }
            department.setText(deptName);
        }

        emp_id = userInfoLists.get(0).getEmp_id();

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dd = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
        today_date = dd.format(c);

        String tt = "TODAY ("+today_date+")";
        todayDate.setText(tt);

        personalInfo.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, EmployeeInformation.class);
            startActivity(intent);
        });

        attendance.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, Attendance.class);
            startActivity(intent);
        });

        leave.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, Leave.class);
            startActivity(intent);
        });

        payRoll.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, PayRollInfo.class);
            startActivity(intent);
        });

        directory.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, Directory.class);
            startActivity(intent);
        });

        fleetReq.setOnClickListener(v -> {
            Intent intent;
            if (isIsp) {
                intent = new Intent(MainMenu.this, FleetRequisition.class);
            }
            else {
                intent = new Intent(MainMenu.this, DriverAssignment.class);
            }
            startActivity(intent);
        });

        movReg.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, MovementRegister.class);
            startActivity(intent);
        });

        checkIn.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenu.this, CheckInOut.class);
            startActivity(intent);
        });

        vhTracking.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenu.this, VehicleTracking.class);
            startActivity(intent);
        });

        if (!isIsp) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) personalInfo.getLayoutParams();
            int pixels = (int) (getResources().getDimension(com.intuit.sdp.R.dimen._15sdp));
            layoutParams.setMarginEnd(pixels);
            personalInfo.setLayoutParams(layoutParams);
            attendance.setVisibility(View.GONE);
            bottomBarLay.setVisibility(View.GONE);
            leave.setVisibility(View.GONE);
            payRoll.setVisibility(View.GONE);
            directory.setVisibility(View.GONE);
            checkIn.setVisibility(View.GONE);
            vhTracking.setVisibility(View.GONE);
            flAssignLayout.setVisibility(View.VISIBLE);
            attLayout.setVisibility(View.GONE);
            leaveLayout.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.GONE);
            di_id = userInfoLists.get(0).getUsr_name();
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) lastLay.getLayoutParams();
            int pixel = (int) (getResources().getDimension(com.intuit.sdp.R.dimen._5sdp));
            lp.setMargins(0,0,0,pixel);

        }
        else {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) personalInfo.getLayoutParams();
            int pixels = (int) (getResources().getDimension(com.intuit.sdp.R.dimen._8sdp));
            layoutParams.setMarginEnd(pixels);
            personalInfo.setLayoutParams(layoutParams);
            attendance.setVisibility(View.VISIBLE);
            leave.setVisibility(View.VISIBLE);
            payRoll.setVisibility(View.VISIBLE);
            directory.setVisibility(View.VISIBLE);
            checkIn.setVisibility(View.VISIBLE);
            vhTracking.setVisibility(View.VISIBLE);
            flAssignLayout.setVisibility(View.GONE);
            attLayout.setVisibility(View.VISIBLE);
            leaveLayout.setVisibility(View.GONE);
            bottomBarLay.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.VISIBLE);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) lastLay.getLayoutParams();
            int pixel = (int) (getResources().getDimension(com.intuit.sdp.R.dimen._40sdp));
            lp.setMargins(0,0,0,pixel);
        }
//        dashB.setOnClickListener(v -> {
//            Intent intent = new Intent(MainMenu.this, Dashboard.class);
//            intent.putExtra("FROMMAINMENU", false);
//            startActivity(intent);
//            finish();
//        });

        recruitApp.setOnClickListener(view -> {
            Intent intent = new Intent(MainMenu.this, RecruitmentApproval.class);
            startActivity(intent);
        });

        floatingActionButton.setOnClickListener(v -> attendanceShortcutTriggered());

        logoutImage.setOnClickListener(v -> {
            if (isMyServiceRunning()) {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainMenu.this);
                builder.setMessage("Your Tracking Service is running. You can not Log Out while Running this Service!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(MainMenu.this);
                builder.setTitle("LOG OUT!")
                        .setIcon(R.drawable.elite_force_logo)
                        .setMessage("Do you want to Log Out?")
                        .setPositiveButton("YES", (dialog, which) -> {


                            userInfoLists.clear();
                            userDesignations.clear();
                            userInfoLists = new ArrayList<>();
                            userDesignations = new ArrayList<>();
                            isApproved = 0;
                            isLeaveApproved = 0;

                            SharedPreferences.Editor widgetEditor = attendanceWidgetPreferences.edit();
                            widgetEditor.remove(WIDGET_EMP_ID);
                            widgetEditor.remove(WIDGET_TRACKER_FLAG);
                            widgetEditor.apply();
                            widgetEditor.commit();

                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
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
                            editor1.apply();
                            editor1.commit();

                            if (trackerAvailable == 1) {
                                SharedPreferences.Editor editor = sharedSchedule.edit();
                                editor.remove(SCHEDULING_EMP_ID);
                                editor.remove(TRIGGERING);
                                editor.apply();
                                editor.commit();

                                Intent intent1 = new Intent(MainMenu.this, Uploader.class);
                                PendingIntent pendingIntent;
                                pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1, PendingIntent.FLAG_IMMUTABLE);
                                alarmManager.cancel(pendingIntent);
                            }

                            Intent intent = new Intent(MainMenu.this, Login.class);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("NO", (dialog, which) -> {

                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
                        .setTitle("EXIT!")
                        .setMessage("Do You Want to Exit?")
                        .setIcon(R.drawable.elite_force_logo)
                        .setPositiveButton("Yes", (dialog, which) -> finish())
                        .setNegativeButton("No", (dialog, which) -> {
                            //Do nothing
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        refresh.setOnClickListener(view -> {
            Date time = Calendar.getInstance().getTime();
            SimpleDateFormat dfffff = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
            lastDateForAttBot = dfffff.format(time);
            getEmpTodayAttData();
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.my_info_menu) {
                Intent intent = new Intent(MainMenu.this, EmployeeInformation.class);
                startActivity(intent);
            }
            else if (item.getItemId() == R.id.attendance_menu) {
                Intent intent = new Intent(MainMenu.this, Attendance.class);
                startActivity(intent);
            }
            else if (item.getItemId() == R.id.leave_menu) {
                Intent intent = new Intent(MainMenu.this, Leave.class);
                startActivity(intent);
            }
            else if (item.getItemId() == R.id.pay_roll_menu) {
                Intent intent = new Intent(MainMenu.this, PayRollInfo.class);
                startActivity(intent);
            }
            return true;
        });

        getAllData();
    }

    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Service.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat dfffff = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);
        lastDateForAttBot = dfffff.format(c);
        getEmpTodayAttData();
    }

    public void getEmpTodayAttData() {
        attLayout.setVisibility(View.GONE);
        leaveLayout.setVisibility(View.GONE);
        flAssignLayout.setVisibility(View.GONE);
        totMoveLayout.setVisibility(View.GONE);
        circularProgressIndicator.setVisibility(View.VISIBLE);
        intTimeAttBot = "";
        outTimeAttBot = "";
        tot_move = "";
        tot_fl_assign = "";
        com_fl_assign = "";
        pen_fl_assign = "";
        leave_name = "";
        leave_frm_date = "";
        leave_to_date = "";

        String todayAttDataUrl = api_url_front+"attendance/getTodayAttData/"+emp_id+"/"+lastDateForAttBot;
        String moveCountUrl = api_url_front+"movement/getMoveCountDaily?p_emp_id="+emp_id+"&p_di_id="+di_id;
        String flCountUrl = api_url_front+"fleet_requisition/getSumAssignmentDaily?p_di_id="+di_id;
        String upLeaveUrl = api_url_front+"leave/getUpcomingLeave?emp_id="+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest flCountReq = new StringRequest(Request.Method.GET, flCountUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        tot_fl_assign = info.getString("total_assign")
                                .equals("null") ? "0" : info.getString("total_assign");
                        com_fl_assign = info.getString("completed_assign")
                                .equals("null") ? "0" : info.getString("completed_assign");
                        pen_fl_assign = info.getString("pending_assign")
                                .equals("null") ? "0" : info.getString("pending_assign");
                    }
                }
                updateLayout();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            updateLayout();
        });

        StringRequest moveCountReq = new StringRequest(Request.Method.GET,moveCountUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        tot_move = info.getString("total_move")
                                .equals("null") ? "0" : info.getString("total_move");
                    }
                }
                if (isIsp) {
                    updateLayout();
                }
                else {
                    requestQueue.add(flCountReq);
                }
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            updateLayout();
        });

        StringRequest upLeaveReq = new StringRequest(Request.Method.GET, upLeaveUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    JSONObject info = array.getJSONObject(0);
                    leave_frm_date = info.getString("from_date")
                            .equals("null") ? "" : info.getString("from_date");
                    leave_to_date = info.getString("to_date")
                            .equals("null") ? "" : info.getString("to_date");
                    leave_name = info.getString("lc_name")
                            .equals("null") ? "" : info.getString("lc_name");
                }
                requestQueue.add(moveCountReq);
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            updateLayout();
        });

        StringRequest todayAttReq = new StringRequest(Request.Method.GET,todayAttDataUrl, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject todayAttDataInfo = array.getJSONObject(i);
                        intTimeAttBot = todayAttDataInfo.getString("dac_in_date_time")
                                .equals("null") ? "" : todayAttDataInfo.getString("dac_in_date_time");
                        outTimeAttBot = todayAttDataInfo.getString("dac_out_date_time")
                                .equals("null") ? "" : todayAttDataInfo.getString("dac_out_date_time");
                    }
                }
                requestQueue.add(upLeaveReq);
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            updateLayout();
        });

        requestQueue.add(todayAttReq);
    }

    public void updateLayout() {
        circularProgressIndicator.setVisibility(View.GONE);
        totMoveLayout.setVisibility(View.VISIBLE);
        if (isIsp) {
            attLayout.setVisibility(View.VISIBLE);
            if (leave_name.isEmpty()) {
                leaveLayout.setVisibility(View.GONE);
            }
            else {
                leaveLayout.setVisibility(View.VISIBLE);
            }
            bottomBarLay.setVisibility(View.VISIBLE);
            floatingActionButton.setVisibility(View.VISIBLE);
            flAssignLayout.setVisibility(View.GONE);
        }
        else {
            attLayout.setVisibility(View.GONE);
            leaveLayout.setVisibility(View.GONE);
            bottomBarLay.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.GONE);
            flAssignLayout.setVisibility(View.VISIBLE);
        }

        if (intTimeAttBot != null) {
            if (intTimeAttBot.isEmpty()) {
                intTimeAttBot = "--:--";
            }
        }
        else {
            intTimeAttBot = "--:--";
        }

        if (outTimeAttBot != null) {
            if (outTimeAttBot.isEmpty()) {
                outTimeAttBot = "--:--";
            }
        }
        else {
            outTimeAttBot = "--:--";
        }

        leaveName.setText(leave_name);
        lvFromDate.setText(leave_frm_date);
        lvToDate.setText(leave_to_date);

        inTimeAtt.setText(intTimeAttBot);
        outTimeAtt.setText(outTimeAttBot);

        if (tot_move.isEmpty()) {
            tot_move = "00";
        }
        else {
            if (tot_move.length() == 1) {
                tot_move = "0"+tot_move;
            }
        }

        totalMove.setText(tot_move);

        if (tot_fl_assign.isEmpty()) {
            tot_fl_assign = "00";
        }
        else {
            if (tot_fl_assign.length() == 1) {
                tot_fl_assign = "0"+tot_fl_assign;
            }
        }

        if (com_fl_assign.isEmpty()) {
            com_fl_assign = "00";
        }
        else {
            if (com_fl_assign.length() == 1) {
                com_fl_assign = "0"+com_fl_assign;
            }
        }

        if (pen_fl_assign.isEmpty()) {
            pen_fl_assign = "00";
        }
        else {
            if (pen_fl_assign.length() == 1) {
                pen_fl_assign = "0"+pen_fl_assign;
            }
        }

        totalFlAssign.setText(tot_fl_assign);
        comFlAssign.setText(com_fl_assign);
        penFlAssign.setText(pen_fl_assign);
    }

    public void getAllData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        selectedImage = null;
        imageFound = false;

        String userImageUrl = api_url_front+"utility/getUserImage/"+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(MainMenu.this);

        StringRequest imageReq = new StringRequest(Request.Method.GET, userImageUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject userImageInfo = array.getJSONObject(i);
                        String emp_image = userImageInfo.getString("emp_image");
                        if (emp_image.equals("null") || emp_image.isEmpty()) {
                            System.out.println("NULL IMAGE");
                            imageFound = false;
                        }
                        else {
                            byte[] decodedString = Base64.decode(emp_image,Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);

                            if (bitmap != null) {
                                imageFound = true;
                                selectedImage = bitmap;
                            }
                            else {
                                imageFound = false;
                            }
                        }
                    }
                }
                connected = true;
                updateInterface();
            }
            catch (JSONException e) {
                connected = false;
                logger.log(Level.WARNING,e.getMessage(),e);
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            logger.log(Level.WARNING,error.getMessage(),error);
            updateInterface();
        });

        requestQueue.add(imageReq);
    }

    public void updateInterface() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (imageFound) {
                    Glide.with(getApplicationContext())
                            .load(selectedImage)
                            .fitCenter()
                            .into(userImage);
                }
                else {
                    userImage.setImageResource(R.drawable.profile);
                }
                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("There is a network issue in the server. Please Try later")
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
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(this)
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
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        int action = motionEvent.getAction();
//        if (action == MotionEvent.ACTION_DOWN) {
//            downRawX = motionEvent.getRawX();
//            downRawY = motionEvent.getRawY();
//            dX = view.getX() - downRawX;
//            dY = view.getY() - downRawY;
//            return true; // Consumed
//        }
//        else if (action == MotionEvent.ACTION_MOVE) {
//            int viewWidth = view.getWidth();
//            int viewHeight = view.getHeight();
//
//            View viewParent = (View)view.getParent();
//            int parentWidth = viewParent.getWidth();
//            int parentHeight = viewParent.getHeight();
//
//            float newX = motionEvent.getRawX() + dX;
//            newX = Math.max(0, newX); // Don't allow the FAB past the left hand side of the parent
//            newX = Math.min(parentWidth - viewWidth, newX); // Don't allow the FAB past the right hand side of the parent
//
//            float newY = motionEvent.getRawY() + dY;
//            newY = Math.max(0, newY); // Don't allow the FAB past the top of the parent
//            newY = Math.min(parentHeight - viewHeight, newY); // Don't allow the FAB past the bottom of the parent
//
//            view.animate()
//                    .x(newX)
//                    .y(newY)
//                    .setDuration(0)
//                    .start();
//
//            return true; // Consumed
//
//        }
//        else if (action == MotionEvent.ACTION_UP) {
//
//            float upRawX = motionEvent.getRawX();
//            float upRawY = motionEvent.getRawY();
//
//            float upDX = upRawX - downRawX;
//            float upDY = upRawY - downRawY;
//
//            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
//                return view.performClick();
//            }
//            else { // A drag
//                return true; // Consumed
//            }
//
//        }
//        else {
//            return view.onTouchEvent(motionEvent);
//        }
//    }

    public void attendanceShortcutTriggered() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        attendanceWidgetPreferences = getSharedPreferences(WIDGET_FILE,MODE_PRIVATE);
        emp_id = attendanceWidgetPreferences.getString(WIDGET_EMP_ID,"");
        String tracker_flag = attendanceWidgetPreferences.getString(WIDGET_TRACKER_FLAG,"");

        if (!emp_id.isEmpty()) {
            if (tracker_flag.equals("1")) {
                showSystemMessage("Your tracking flag is active. You need to give attendance from inside Attendance Module.",0);
            }
            else {
                doWork();
            }
        }
        else {
            showSystemMessage("Could Not Found Employee ID",0);
        }
    }

    public void doWork() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(1500)
                .build();

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (gps) {
            startLocationUpdates();
        }
        else {
            showSystemMessage("Your GPS is disabled. Please enable it and try again.",1);
        }
    }

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                System.out.println("ADADAD!!!!!A!!!");
                Log.i("LocationFused ", location.toString());
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss aa", Locale.ENGLISH);
                SimpleDateFormat dftoShow = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);
                lastLatLongitude[0] = new LatLng(location.getLatitude(), location.getLongitude());
                lat = String.valueOf(lastLatLongitude[0].latitude);
                lon = String.valueOf(lastLatLongitude[0].longitude);
                Date c = Calendar.getInstance().getTime();
                Date date = new Date();
                ts = new Timestamp(date.getTime());
                inTime = df.format(c);
                timeToShow = dftoShow.format(c);
                //getAddress(lastLatLongitude[0].latitude,lastLatLongitude[0].longitude);
                stopLocationUpdate();
            }
        }
    };

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            showSystemMessage("Please check your Location Permission to access this feature.",0);
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
        System.out.println("ADADADA");
    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        getOfficeLocation();
    }

    public void getOfficeLocation() {
        areaLists = new ArrayList<>();
        String offLocationUrl = api_url_front+"attendance/getAreaCoverage?p_emp_id="+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest offLocReq = new StringRequest(Request.Method.GET, offLocationUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject offLocInfo = array.getJSONObject(i);
                        officeLatitude = offLocInfo.getString("coa_latitude").equals("null") ? "" : offLocInfo.getString("coa_latitude");
                        officeLongitude = offLocInfo.getString("coa_longitude").equals("null") ? "" : offLocInfo.getString("coa_longitude");
                        coverage = offLocInfo.getString("coa_coverage").equals("null") ? "" : offLocInfo.getString("coa_coverage");
                        String coa_id = offLocInfo.getString("coa_id").equals("null") ? "" : offLocInfo.getString("coa_id");

                        areaLists.add(new AreaLists(officeLatitude,officeLongitude,coverage,coa_id));
                    }
                }
                connected = true;
                updateInfo();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                connected = false;
                updateInfo();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            conn = false;
            connected = false;
            updateInfo();
        });

        requestQueue.add(offLocReq);
    }

    public void updateInfo() {
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
                if (!inTime.isEmpty()) {
                    LatLng c_latLng = new LatLng(0,0);
                    boolean found = false;
                    float[] distance = new float[1];
                    float prev_distance = 0;
                    String prev_mach_code = "";
                    if (!areaLists.isEmpty()) {
                        for (int i = 0; i < areaLists.size(); i++) {
                            officeLatitude = areaLists.get(i).getLatitude();
                            officeLongitude = areaLists.get(i).getLongitude();
                            coverage = areaLists.get(i).getCoverage();

                            if (officeLatitude != null && officeLongitude != null) {
                                if (!officeLatitude.isEmpty() && !officeLongitude.isEmpty()) {
                                    c_latLng = new LatLng(Double.parseDouble(officeLatitude),Double.parseDouble(officeLongitude));
                                }
                            }

                            if (c_latLng.latitude != 0 && c_latLng.longitude != 0) {
                                Location.distanceBetween(c_latLng.latitude,c_latLng.longitude,lastLatLongitude[0].latitude,lastLatLongitude[0].longitude,distance);

                                float radius = 0;
                                if (coverage != null) {
                                    if (!coverage.isEmpty()) {
                                        radius = Float.parseFloat(coverage);
                                    }
                                }

                                machineCode = areaLists.get(i).getCoa_id();

                                if (distance[0] <= radius) {
                                    found = true;
                                    prev_mach_code = machineCode;
                                    break;
                                }
                                else {
                                    float dd = distance[0] - radius;
                                    if (prev_distance == 0) {
                                        prev_distance = dd;
                                        prev_mach_code = machineCode;
                                    }
                                    else if (dd < prev_distance){
                                        prev_distance = dd;
                                        prev_mach_code = machineCode;
                                    }
                                }
                            }
                        }

                        machineCode = prev_mach_code;

                        boolean finalFound = found;
                        float finalPrev_distance = prev_distance;

                        checkAddress(finalFound, finalPrev_distance);
//                        if (found) {
//                            checkAddress();
//                        }
//                        else {
//                            showSystemMessage("You are not around your office area. You are "+Math.round(prev_distance)+ " meters away from your office area",0);
//                        }
                    }
                    else {
                        showSystemMessage("You don't have any assign area. Please contact with administrator",0);
                    }
                }
                else {
                    showSystemMessage("Failed to get Location. Please Try Again",0);
                }
            }
            else {
                showSystemMessage("There is a network issue in the server. Please Try later.",0);
            }
        }
        else {
            showSystemMessage("Please Check Your Internet Connection.",0);
        }
    }

    public void checkAddress(boolean found, float distance) {
        new Thread(() -> {
            getAddress(Double.parseDouble(lat),Double.parseDouble(lon));
            runOnUiThread(() -> {
                if (found) {
                    giveAttendance();
                }
                else {
                    waitProgress.dismiss();
                    AttendanceRequest attendanceRequest = new AttendanceRequest(MainMenu.this, emp_id, ts.toString(),machineCode,lat,lon,address,distance);
                    attendanceRequest.show(getSupportFragmentManager(),"ATT_REQ");
                }
            });
        }).start();
    }

//    public void checkAddress() {
//        new Thread(() -> {
//            getAddress(Double.parseDouble(lat),Double.parseDouble(lon));
//            runOnUiThread(this::giveAttendance);
//        }).start();
//    }

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (Geocoder.isPresent()) {
                assert addresses != null;
                Address obj = addresses.get(0);
                address = obj.getAddressLine(0);
                System.out.println("Ekhane ashbe 1st");
            } else {
                address = "";
            }

        } catch (IOException e) {
            address = "";
        }
    }

    public void giveAttendance() {
        conn = false;
        connected = false;

        String attendaceUrl = api_url_front+"attendance/giveAttendance";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

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
                updateAttLayout();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                connected = false;
                updateAttLayout();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            conn = false;
            connected = false;
            updateAttLayout();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_PUNCH_TIME",ts.toString());
                headers.put("P_MACHINE_CODE",machineCode);
                headers.put("P_LATITUDE",lat);
                headers.put("P_LONGITUDE",lon);
                headers.put("P_ADDRESS",address);
                return  headers;
            }
        };

        requestQueue.add(attReq);
    }

    private  void updateAttLayout() {
        if (conn) {
            if (connected) {
                connected = false;
                conn = false;
                showSystemMessage("Your Attendance is Recorded at "+timeToShow+".",0);

            }
            else {
                showSystemMessage("There is a network issue in the server. Please Try later.",0);
            }
        }
        else {
            showSystemMessage("Please Check Your Internet Connection.",0);
        }
    }

    private void newEnableGps() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {
        });

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(MainMenu.this,
                            1000);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                assert data != null;
                Log.i("Hoise ", "1");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.i("Hoise ", "2");
            }
        }
    }

    public void showSystemMessage(String msg, int choice) {
        waitProgress.dismiss();
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(MainMenu.this)
                .setTitle("Attendance System")
                .setMessage(msg)
                .setIcon(R.drawable.notification_icon)
                .setPositiveButton("OK", (dialog, which) -> {
                    if (choice == 1) {
                        newEnableGps();
                    }
                    dialog.dismiss();
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}