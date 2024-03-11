package ttit.com.shuvo.eliteforce.dashboard;

import static com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE;
import static ttit.com.shuvo.eliteforce.login.Login.isApproved;
import static ttit.com.shuvo.eliteforce.login.Login.isLeaveApproved;
import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.scheduler.Uploader.channelId;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.Attendance;
import ttit.com.shuvo.eliteforce.attendance.trackService.Service;
import ttit.com.shuvo.eliteforce.basic_model.SalaryMonthList;
import ttit.com.shuvo.eliteforce.employeeInfo.EmployeeInformation;
import ttit.com.shuvo.eliteforce.leave.Leave;
import ttit.com.shuvo.eliteforce.login.Login;
import ttit.com.shuvo.eliteforce.login.model.UserDesignation;
import ttit.com.shuvo.eliteforce.login.model.UserInfoList;
import ttit.com.shuvo.eliteforce.mainPage.MainMenu;
import ttit.com.shuvo.eliteforce.payRoll.PayRollInfo;
import ttit.com.shuvo.eliteforce.scheduler.Uploader;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class Dashboard extends AppCompatActivity implements View.OnTouchListener {
    TextView userName;
    TextView designation;
    TextView department;
    ImageView userImage;

    ImageView home;
    ImageView logout;

    BarChart salaryChart;
    ImageView refreshSalary;

    ArrayList<BarEntry> salaryValue;
    ArrayList<String> year;

    ArrayList<SalaryMonthList> months;
    ArrayList<SalaryMonthList> salaryMonthLists;

    ArrayList<String> monthName;
    ArrayList<String> salary;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    public static String emp_id_for_xml = "";
    String emp_id = "";
    String emp_code = "";
    String formattedDate = "";
    public static int trackerAvailable = 0;

    PieChart pieChart;
    ImageView refreshAttendance;

    String beginDate = "";
    String lastDate = "";

    String absent = "";
    String present = "";
    String leave = "";
    String holiday = "";
    String late = "";
    String early = "";

    ArrayList<PieEntry> NoOfEmp;

    public static ArrayList<String> lastTenDaysXml;

    String leaveDate = "";

    ArrayList<BarEntry> balance;
    ArrayList<BarEntry> earn;
    ArrayList<String> shortCode;

    BarChart leaveChart;
    ImageView refreshLeave;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedSchedule;
    SharedPreferences attendanceWidgetPreferences;
    public static AlarmManager alarmManager;

    CardView userCard;

    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_ELITE_FORCE_HR";

    public static final String USER_NAME = "USER_NAME";
    public static final String USER_F_NAME = "USER_F_NAME";
    public static final String USER_L_NAME = "USER_L_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String CONTACT = "CONTACT";
    public static final String EMP_ID_LOGIN = "EMP_ID";

    public static final String JSM_CODE = "JSM_CODE";
    public static final String JSM_NAME = "JSM_NAME";
    public static final String JSD_ID_LOGIN = "JSD_ID";
    public static final String JSD_OBJECTIVE = "JSD_OBJECTIVE";
    public static final String DEPT_NAME = "DEPT_NAME";
    public static final String DIV_NAME = "DIV_NAME";
    public static final String DESG_NAME = "DESG_NAME";
    public static final String DESG_PRIORITY = "DESG_PRIORITY";
    public static final String JOINING_DATE = "JOINING_DATE";
    public static final String DIV_ID = "DIV_ID";
    public static final String LOGIN_TF = "TRUE_FALSE";

    public static final String IS_ATT_APPROVED = "IS_ATT_APPROVED";
    public static final String IS_LEAVE_APPROVED = "IS_LEAVE_APPROVED";
    public static final String LIVE_FLAG = "LIVE_FLAG";

    public static final String SCHEDULING_FILE = "SCHEDULING FILE_EF_HR";
    public static final String SCHEDULING_EMP_ID = "SCHEDULING EMP ID";
    public static final String TRIGGERING = "TRIGGER TRUE FALSE";

    public static String FILE_OF_DAILY_ACTIVITY = "";

    public static final String WIDGET_FILE = "WIDGET_FILE_EF_HR";
    public static final String WIDGET_EMP_ID = "WIDGET_EMP_ID";
    public static final String WIDGET_TRACKER_FLAG = "WIDGET_TRACKER_FLAG";

    boolean loginLog_check;
    boolean checkEmpFlag = false;

    String android_id = "";
    String model = "";
    String brand = "";
    String ipAddress = "";
    String hostUserName = "";
    String osName = "";
    public static Bitmap selectedImage;
    boolean imageFound = false;
    TextView welcomeText;
    AppUpdateManager appUpdateManager;

    float dX;
    float dY;
    private static float downRawX, downRawY;
    private final static float CLICK_DRAG_TOLERANCE = 10;

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

    ActivityResultLauncher<IntentSenderRequest> activityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartIntentSenderForResult(),
                    result -> {
                        if (result.getResultCode() != RESULT_OK) {

                            MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Dashboard.this)
                                    .setTitle("Update Failed!")
                                    .setMessage("Failed to update the app. Please retry again.")
                                    .setIcon(R.drawable.elite_force_logo)
                                    .setPositiveButton("Retry", (dialog, which) -> getAppUpdate())
                                    .setNegativeButton("Cancel", (dialog, which) -> finish());
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    });
    FloatingActionButton floatingActionButton;

    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        userName = findViewById(R.id.user_name_dashboard);
        department = findViewById(R.id.user_depart_dashboard);
        designation = findViewById(R.id.user_desg_dashboard);
        userCard = findViewById(R.id.userinfo_card);
        userImage = findViewById(R.id.user_pic_dashboard);
        welcomeText = findViewById(R.id.welcome_text_view);
        floatingActionButton = findViewById(R.id.attendance_shortcut);

        home = findViewById(R.id.home_icon);
        logout = findViewById(R.id.log_out_icon);

        salaryChart = findViewById(R.id.barchart_salary_dashboard);
        refreshSalary = findViewById(R.id.refresh_graph_salary_dashboard);

        pieChart = findViewById(R.id.piechart_attendance_dashboard);
        refreshAttendance = findViewById(R.id.refresh_graph_attendance_dashboard);

        leaveChart = findViewById(R.id.multi_bar_chart_leave_dashboard);
        refreshLeave = findViewById(R.id.refresh_graph_leave_dashboard);

        appUpdateManager = AppUpdateManagerFactory.create(Dashboard.this);

        lastTenDaysXml = new ArrayList<>();

        floatingActionButton.setOnTouchListener(Dashboard.this);
        floatingActionButton.setOnClickListener(v -> attendanceShortcutTriggered());

        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String wt = "";
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

        Intent in = getIntent();
        loginLog_check = in.getBooleanExtra("FROMMAINMENU", true);

        System.out.println("Log Needed? :" +loginLog_check);

        attendanceWidgetPreferences = getSharedPreferences(WIDGET_FILE,MODE_PRIVATE);

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        boolean loginfile = sharedPreferences.getBoolean(LOGIN_TF,false);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if(loginfile) {
            if (loginLog_check) {
                String userName = sharedPreferences.getString(USER_NAME, null);
                String userFname = sharedPreferences.getString(USER_F_NAME, null);
                String userLname = sharedPreferences.getString(USER_L_NAME,null);
                String email = sharedPreferences.getString(EMAIL,null);
                String contact = sharedPreferences.getString(CONTACT, null);
                String emp_id_login = sharedPreferences.getString(EMP_ID_LOGIN,null);

                userInfoLists = new ArrayList<>();
                userInfoLists.add(new UserInfoList(userName,userFname,userLname,email,contact,emp_id_login));

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

                userDesignations = new ArrayList<>();
                userDesignations.add(new UserDesignation(jsm_code,jsm_name,jsd_id,jsd_obj,dept_name,div_name,desg_name,desg_priority,joining,div_id));

                isApproved = sharedPreferences.getInt(IS_ATT_APPROVED,0);
                isLeaveApproved = sharedPreferences.getInt(IS_LEAVE_APPROVED,0);
            }
        }

        emp_id = userInfoLists.get(0).getEmp_id();
        emp_code = userInfoLists.get(0).getUserName();

        if (userInfoLists.size() != 0) {
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

        if (userDesignations.size() != 0) {
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

        model = android.os.Build.MODEL;

        brand = Build.BRAND;

        ipAddress = getIPAddress(true);

        hostUserName = getHostName("localhost");

        StringBuilder builder = new StringBuilder();
        builder.append("ANDROID: ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException | IllegalAccessException | NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(": ").append(fieldName);
            }
        }

        osName = builder.toString();

        android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        home.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, MainMenu.class);
            startActivity(intent);
            finish();
        });

        SalaryInit();
        AttendanceInit();
        LeaveInit();

        emp_id_for_xml = emp_id;

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -12);

        for (int i = 0 ; i < 10 ;i ++) {
            cal.add(Calendar.DAY_OF_YEAR, +1);
            Date calTime = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
            String ddd = simpleDateFormat.format(calTime);

            ddd = ddd.toUpperCase();
            System.out.println(ddd);
            lastTenDaysXml.add(ddd);
        }

        getAppUpdate();

        logout.setOnClickListener(v -> {
            if (isMyServiceRunning()) {
                MaterialAlertDialogBuilder builder1 = new MaterialAlertDialogBuilder(Dashboard.this);
                builder1.setMessage("Your Tracking Service is running. You can not Log Out while Running this Service!")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
                AlertDialog alert = builder1.create();
                alert.show();
            } else {
                MaterialAlertDialogBuilder builder1 = new MaterialAlertDialogBuilder(Dashboard.this);
                builder1.setTitle("LOG OUT!")
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

                                Intent intent1 = new Intent(Dashboard.this, Uploader.class);
                                PendingIntent pendingIntent = null;
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1,PendingIntent.FLAG_IMMUTABLE);
                                }
                                alarmManager.cancel(pendingIntent);
                            }

                            Intent intent = new Intent(Dashboard.this, Login.class);
                            startActivity(intent);
                            finish();
                        })
                        .setNegativeButton("NO", (dialog, which) -> {

                        });
                AlertDialog alert = builder1.create();
                alert.show();
            }

        });

        refreshLeave.setOnClickListener(v -> {
            getLeaveGraph();
        });

        refreshAttendance.setOnClickListener(v -> {
            getAttendanceGraph();
        });

        refreshSalary.setOnClickListener(v -> {
            getSalaryGraph();
        });

        leaveChart.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Leave.class);
            startActivity(intent);
        });

        pieChart.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, Attendance.class);
            startActivity(intent);
        });

        salaryChart.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, PayRollInfo.class);
            startActivity(intent);
        });

        userCard.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, EmployeeInformation.class);
            startActivity(intent);
        });

        deleteSharedPreferences(getApplicationContext());
    }

    private void getAppUpdate() {
        System.out.println("HELLO1");
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(IMMEDIATE))  {
                waitProgress.dismiss();

                appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                        activityResultLauncher, AppUpdateOptions
                                .newBuilder(IMMEDIATE)
                                .build());
            }
            else {
                System.out.println("No update available");
                getAllData();
            }
        });
        appUpdateInfoTask.addOnFailureListener(e -> {
            System.out.println("FAILED TO LISTEN");
            getAllData();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {

                                appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                                        activityResultLauncher,AppUpdateOptions
                                                .newBuilder(IMMEDIATE)
                                                .build());
                            }
                        });
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

    public static void deleteSharedPreferences(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            System.out.println("Pai nai");

            for (int i = 0; i < lastTenDaysXml.size(); i++) {
                FILE_OF_DAILY_ACTIVITY = emp_id_for_xml +"_"+lastTenDaysXml.get(i)+"_track";
                System.out.println(FILE_OF_DAILY_ACTIVITY + " of " + lastTenDaysXml.get(i));
                File dir = new File(context.getApplicationInfo().dataDir, "shared_prefs/"+FILE_OF_DAILY_ACTIVITY+".xml");
                if(dir.exists()) {
                    System.out.println(true);
                    context.getSharedPreferences(FILE_OF_DAILY_ACTIVITY, MODE_PRIVATE).edit().clear().commit();
                    System.out.println(dir.toString());
                    boolean ddd = dir.delete();

                    if (ddd) {
                        System.out.println("Deleted");
                    }
                } else {
                    System.out.println(false);
                }
            }
        }
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        assert sAddr != null;
                        boolean isIPv4 = sAddr.indexOf(':')<0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim<0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } // for now eat exceptions
        return "";
    }

    public static String getHostName(String defValue) {
        try {
            @SuppressLint("DiscouragedPrivateApi") Method getString = Build.class.getDeclaredMethod("getString", String.class);
            getString.setAccessible(true);
            return Objects.requireNonNull(getString.invoke(null, "net.hostname")).toString();
        } catch (Exception ex) {
            return defValue;
        }
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(Dashboard.this)
                .setTitle("EXIT!")
                .setMessage("Do you want to exit?")
                .setIcon(R.drawable.elite_force_logo)
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", (dialog, which) -> {
                    //Do nothing
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "File Uploader Channel";
            String description = "Channel for Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createNotificationChannelForWidget() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.att_channel_id), "Attendance Info", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Attendance from Widget");

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }

    public void SalaryInit() {
        months = new ArrayList<>();
        salaryMonthLists = new ArrayList<>();

        monthName = new ArrayList<>();
        salary = new ArrayList<>();

        salaryChart.getDescription().setEnabled(false);
        salaryChart.setPinchZoom(false);

        salaryChart.setDrawBarShadow(false);
        salaryChart.setDrawGridBackground(false);

        salaryChart.getAxisLeft().setDrawGridLines(true);

        salaryValue = new ArrayList<>();

        year = new ArrayList<>();

        XAxis xAxis = salaryChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);

        salaryChart.getLegend().setFormToTextSpace(10);
        salaryChart.getLegend().setStackSpace(10);
        salaryChart.getLegend().setYOffset(10);
        salaryChart.setExtraOffsets(0,0,0,20);

        // zoom and touch disabled
        salaryChart.setScaleEnabled(false);
        salaryChart.setTouchEnabled(true);
        salaryChart.setDoubleTapToZoomEnabled(false);
        salaryChart.setClickable(true);
        salaryChart.setHighlightPerTapEnabled(false);
        salaryChart.setHighlightPerDragEnabled(false);
        //salaryChart.setOnTouchListener(null);

        salaryChart.getAxisRight().setEnabled(false);
        salaryChart.getAxisLeft().setAxisMinValue(0);
        salaryChart.getLegend().setEnabled(false);

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        formattedDate = df.format(c);

        Calendar cal =  Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);
        String previousMonthYear  = new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);
    }

    public void AttendanceInit() {

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        lastDate = df.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy", Locale.ENGLISH);

        beginDate = sdf.format(c);
        beginDate = "01-"+beginDate;

        NoOfEmp = new ArrayList<>();

        //pieChart.setCenterText("Attendance");
        pieChart.setDrawEntryLabels(true);
        pieChart.setCenterTextSize(12);
        pieChart.setHoleRadius(30);
        pieChart.setTransparentCircleRadius(20);

        pieChart.setEntryLabelTextSize(11);
        pieChart.setEntryLabelColor(Color.DKGRAY);
        pieChart.getDescription().setEnabled(false);
        pieChart.setTouchEnabled(true);
        pieChart.setClickable(true);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.setOnTouchListener(null);


        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setForm(Legend.LegendForm.CIRCLE);

        l.setTextSize(12);
        l.setWordWrapEnabled(false);
        l.setDrawInside(false);
        l.setYOffset(10f);
    }

    public void LeaveInit() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        leaveDate = df.format(c);

        balance = new ArrayList<>();
        earn = new ArrayList<>();
        shortCode = new ArrayList<>();

        leaveChart.getDescription().setEnabled(false);
        leaveChart.setPinchZoom(false);
        leaveChart.getAxisLeft().setDrawGridLines(true);
        leaveChart.getLegend().setStackSpace(20);
        leaveChart.getLegend().setYOffset(10);
        leaveChart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        leaveChart.setExtraOffsets(0,0,0,20);
        leaveChart.setScaleEnabled(false);
        leaveChart.setTouchEnabled(true);
        leaveChart.setDoubleTapToZoomEnabled(false);
        leaveChart.setHighlightPerTapEnabled(false);
        leaveChart.setHighlightPerDragEnabled(false);
        leaveChart.getAxisLeft().setAxisMinimum(0);

        leaveChart.getAxisRight().setEnabled(false);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN) {
            downRawX = event.getRawX();
            downRawY = event.getRawY();
            dX = v.getX() - downRawX;
            dY = v.getY() - downRawY;
            return true; // Consumed
        }
        else if (action == MotionEvent.ACTION_MOVE) {
            int viewWidth = v.getWidth();
            int viewHeight = v.getHeight();

            View viewParent = (View)v.getParent();
            int parentWidth = viewParent.getWidth();
            int parentHeight = viewParent.getHeight();

            float newX = event.getRawX() + dX;
            newX = Math.max(0, newX); // Don't allow the FAB past the left hand side of the parent
            newX = Math.min(parentWidth - viewWidth, newX); // Don't allow the FAB past the right hand side of the parent

            float newY = event.getRawY() + dY;
            newY = Math.max(0, newY); // Don't allow the FAB past the top of the parent
            newY = Math.min(parentHeight - viewHeight, newY); // Don't allow the FAB past the bottom of the parent

            v.animate()
                    .x(newX)
                    .y(newY)
                    .setDuration(0)
                    .start();

            return true; // Consumed

        }
        else if (action == MotionEvent.ACTION_UP) {

            float upRawX = event.getRawX();
            float upRawY = event.getRawY();

            float upDX = upRawX - downRawX;
            float upDY = upRawY - downRawY;

            if (Math.abs(upDX) < CLICK_DRAG_TOLERANCE && Math.abs(upDY) < CLICK_DRAG_TOLERANCE) { // A click
                return v.performClick();
            }
            else { // A drag
                return true; // Consumed
            }

        }
        else {
            return v.onTouchEvent(event);
        }
    }

    public static class MyAxisValueFormatter extends ValueFormatter {
        private final ArrayList<String> mvalues;
        public MyAxisValueFormatter(ArrayList<String> mvalues) {
            this.mvalues = mvalues;
        }

        @Override
        public String getAxisLabel(float value, AxisBase axis) {
            return (mvalues.get((int) value));
        }
    }

    public void getSalaryGraph() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        salaryMonthLists = new ArrayList<>();

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        String salaryDataUrl = api_url_front+"dashboard/getSalaryAndMonth/"+emp_id+"/"+formattedDate+"";

        StringRequest salaryMonthReq = new StringRequest(Request.Method.GET, salaryDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject salaryMonthInfo = array.getJSONObject(i);
                        String mon_name = salaryMonthInfo.getString("mon_name")
                                .equals("null") ? "" : salaryMonthInfo.getString("mon_name");
                        String net_salary = salaryMonthInfo.getString("net_salary")
                                .equals("null") ? "0" : salaryMonthInfo.getString("net_salary");
                        salaryMonthLists.add(new SalaryMonthList(mon_name,net_salary));
                    }
                }
                connected = true;
                updateSalaryGraph();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateSalaryGraph();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateSalaryGraph();
        });

        requestQueue.add(salaryMonthReq);
    }

    public void updateSalaryGraph() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                monthName = new ArrayList<>();
                salary = new ArrayList<>();
                salaryValue = new ArrayList<>();

                for (int i = 0; i < salaryMonthLists.size(); i++) {
                    for (int j = 0; j < months.size(); j++) {
                        if (months.get(j).getMonth().equals(salaryMonthLists.get(i).getMonth())) {
                            months.get(j).setSalary(salaryMonthLists.get(i).getSalary());
                        }
                    }
                }

                for (int i = months.size()-1; i >= 0; i--) {

                    monthName.add(months.get(i).getMonth());
                    salary.add(months.get(i).getSalary());

                }

                System.out.println(monthName);
                System.out.println(salary);

                for (int i = 0; i < salary.size(); i++) {
                    salaryValue.add(new BarEntry(i, Float.parseFloat(salary.get(i)),i));
                }

                BarDataSet bardataset = new BarDataSet(salaryValue, "Months");
                salaryChart.animateY(1000);

                BarData data1 = new BarData(bardataset);
                bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);

                bardataset.setBarBorderColor(Color.DKGRAY);
                bardataset.setValueTextSize(8);
                bardataset.setValueFormatter(new LargeValueFormatter());
                salaryChart.setData(data1);
                salaryChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
                salaryChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    getSalaryGraph();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                getSalaryGraph();
                dialog.dismiss();
            });
        }
    }

    public void getAttendanceGraph() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        NoOfEmp = new ArrayList<>();
        absent = "";
        present = "";
        leave = "";
        holiday = "";
        late = "";
        early = "";

        String attendDataUrl = api_url_front+"dashboard/getAttendanceData/"+beginDate+"/"+lastDate+"/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        StringRequest attendDataReq = new StringRequest(Request.Method.GET, attendDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject attendanceInfo = array.getJSONObject(i);
                        absent = attendanceInfo.getString("absent")
                                .equals("null") ? "0" : attendanceInfo.getString("absent");
                        present = attendanceInfo.getString("present")
                                .equals("null") ? "0" : attendanceInfo.getString("present");
                        leave = attendanceInfo.getString("leave")
                                .equals("null") ? "0" : attendanceInfo.getString("leave");
                        holiday = attendanceInfo.getString("holiday_weekend")
                                .equals("null") ? "0" : attendanceInfo.getString("holiday_weekend");
                        late = attendanceInfo.getString("late_count")
                                .equals("null") ? "0" : attendanceInfo.getString("late_count");
                        early = attendanceInfo.getString("early_count")
                                .equals("null") ? "0" : attendanceInfo.getString("early_count");
                    }
                    connected = true;
                }
                else {
                    connected = false;
                }
                updateAttendanceGraph();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateAttendanceGraph();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateAttendanceGraph();
        });

        requestQueue.add(attendDataReq);

    }

    public void updateAttendanceGraph() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                final int[] piecolors = new int[]{
                        Color.rgb(116, 185, 255),
                        Color.rgb(85, 239, 196),
                        Color.rgb(162, 155, 254),
                        Color.rgb(223, 230, 233),
                        Color.rgb(255, 234, 167),

                        Color.rgb(250, 177, 160),
                        Color.rgb(178, 190, 195),
                        Color.rgb(129, 236, 236),
                        Color.rgb(255, 118, 117),
                        Color.rgb(253, 121, 168),
                        Color.rgb(96, 163, 188)};


                if (absent != null) {
                    if (!absent.isEmpty()) {
                        if (!absent.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(absent), "Absent", 0));
                        }
                    }
                }

                if (present != null) {
                    if (!present.isEmpty()) {
                        if (!present.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(present),"Present", 1));
                        }
                    }
                }

                if (late != null) {
                    if (!late.isEmpty()) {
                        if (!late.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(late),"Late", 2));

                        }
                    }
                }

                if (early != null) {
                    if (!early.isEmpty()) {
                        if (!early.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(early),"Early", 3));
                        }
                    }
                }

                if (leave != null) {
                    if (!leave.isEmpty()) {
                        if (!leave.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(leave),"Leave", 4));
                        }
                    }
                }

                if (holiday != null) {
                    if (!holiday.isEmpty()) {
                        if (!holiday.equals("0")) {
                            NoOfEmp.add(new PieEntry(Float.parseFloat(holiday),"Holiday/Weekend", 5));
                        }
                    }
                }

                if (NoOfEmp.size() == 0) {
                    NoOfEmp.add(new PieEntry(1,"No Data Found", 6));

                }

                PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
                pieChart.animateXY(1000, 1000);
                pieChart.setEntryLabelColor(Color.TRANSPARENT);

                PieData data = new PieData(dataSet);
                dataSet.setValueFormatter(new ValueFormatter() {
                    @Override
                    public String getFormattedValue(float value) {
                        return String.valueOf((int) Math.floor(value));
                    }
                });
                String label = dataSet.getValues().get(0).getLabel();
                System.out.println(label);
                if (label.equals("No Data Found")) {
                    dataSet.setValueTextColor(Color.TRANSPARENT);
                } else {
                    dataSet.setValueTextColor(Color.BLACK);
                }
                dataSet.setHighlightEnabled(true);
                dataSet.setValueTextSize(12);

                int[] num = new int[NoOfEmp.size()];
                for (int i = 0; i < NoOfEmp.size(); i++) {
                    int neki = (int) NoOfEmp.get(i).getData();
                    num[i] = piecolors[neki];
                }

                dataSet.setColors(ColorTemplate.createColors(num));

                pieChart.setData(data);
                pieChart.invalidate();

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAttendanceGraph();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAttendanceGraph();
                dialog.dismiss();
            });
        }
    }

    public void getLeaveGraph() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        balance = new ArrayList<>();
        earn = new ArrayList<>();
        shortCode = new ArrayList<>();

        String leaveDataUrl = api_url_front+"dashboard/getLeaveData/"+emp_id+"/"+leaveDate+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

        StringRequest leaveDataReq = new StringRequest(Request.Method.GET, leaveDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject leaveInfo = array.getJSONObject(i);
                        String lc_short_code = leaveInfo.getString("lc_short_code")
                                .equals("null") ? "" : leaveInfo.getString("lc_short_code");
                        String balance_all = leaveInfo.getString("balance")
                                .equals("null") ? "0" : leaveInfo.getString("balance");
                        String quantity = leaveInfo.getString("quantity");

                        if (!quantity.equals("null") && !quantity.equals("NULL")) {
                            if (!quantity.equals("0")) {
                                balance.add(new BarEntry(i, Float.parseFloat(balance_all),i));
                                earn.add(new BarEntry(i, Float.parseFloat(quantity),i));
                                shortCode.add(lc_short_code);
                            }
                        }
                    }
                }
                connected = true;
                updateLeaveGraph();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLeaveGraph();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLeaveGraph();
        });

        requestQueue.add(leaveDataReq);
    }

    public void updateLeaveGraph() {
        waitProgress.dismiss();
        if (conn) {
            if(connected) {
                if (balance.size() == 0 || earn.size() == 0 || shortCode.size() == 0) {
                    // do nothing
                    System.out.println("NO DATA FOUND");
                }
                else {
                    XAxis xAxis = leaveChart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setDrawGridLines(false);
                    xAxis.setCenterAxisLabels(true);
                    xAxis.setAxisMinimum(0);
                    xAxis.setAxisMaximum(balance.size());
                    xAxis.setGranularity(1);


                    BarDataSet set1 = new BarDataSet(earn, "Earned Leave");
                    set1.setColor(ColorTemplate.VORDIPLOM_COLORS[3]);
                    set1.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) Math.floor(value));
                        }
                    });
                    BarDataSet set2 = new BarDataSet(balance, "Leave Balance");
                    set2.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) Math.floor(value));
                        }
                    });
                    set2.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);

                    float groupSpace = 0.04f;
                    float barSpace = 0.02f; // x2 dataset
                    float barWidth = 0.46f;

                    BarData leavedata = new BarData(set1, set2);
                    if (earn.size() > 5) {
                        leavedata.setValueTextSize(8);
                    }
                    else if (earn.size() > 3){
                        leavedata.setValueTextSize(10);
                    }
                    else {
                        leavedata.setValueTextSize(12);
                    }
                    leavedata.setBarWidth(barWidth); // set the width of each bar
                    leaveChart.animateY(1000);
                    leaveChart.setData(leavedata);
                    leaveChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
                    leaveChart.invalidate();

                    xAxis.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getAxisLabel(float value, AxisBase axis) {
                            if (value < 0 || value >= shortCode.size()) {
                                return null;
                            } else {
                                System.out.println(value);
                                System.out.println(axis);
                                System.out.println(shortCode.get((int)value));
                                return (shortCode.get((int) value));
                            }

                        }
                    });
                }

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getLeaveGraph();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getLeaveGraph();
                dialog.dismiss();
            });
        }
    }

    public void getAllData() {
        connected = false;
        conn = false;

        salaryMonthLists = new ArrayList<>();
        NoOfEmp = new ArrayList<>();
        absent = "";
        present = "";
        leave = "";
        holiday = "";
        late = "";
        early = "";
        imageFound = false;
        selectedImage = null;
        checkEmpFlag = false;

        balance = new ArrayList<>();
        earn = new ArrayList<>();
        shortCode = new ArrayList<>();

        String empFlagUrl = api_url_front+"dashboard/checkEmpUpdated/"+emp_id+"";
        String salaryDataUrl = api_url_front+"dashboard/getSalaryAndMonth/"+emp_id+"/"+formattedDate+"";
        String attendDataUrl = api_url_front+"dashboard/getAttendanceData/"+beginDate+"/"+lastDate+"/"+emp_id+"";
        String leaveDataUrl = api_url_front+"dashboard/getLeaveData/"+emp_id+"/"+leaveDate+"";
        String trackerFlagUrl = api_url_front+"utility/getTrackerFlag/"+emp_id+"";
        String loginLogUrl = api_url_front+"dashboard/loginLog";
        String userImageUrl = api_url_front+"utility/getUserImage/"+emp_code+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Dashboard.this);

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
                        if (emp_image.equals("null") || emp_image.equals("") ) {
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
                e.printStackTrace();
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateInterface();
        });

        StringRequest loginLogReq = new StringRequest(Request.Method.POST, loginLogUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    requestQueue.add(imageReq);
                }
                else {
                    connected = false;
                    updateInterface();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateInterface();
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String userName = userInfoLists.get(0).getUserName();
                String userId = userInfoLists.get(0).getEmp_id();
                if (userId != null) {
                    if (!userId.isEmpty()) {
                        System.out.println(userId);
                    } else {
                        userId = "0";
                    }
                } else {
                    userId = "0";
                }
                headers.put("P_IULL_USER_ID",userName);
                headers.put("P_IULL_CLIENT_HOST_NAME",brand+" "+model);
                headers.put("P_IULL_CLIENT_IP_ADDR",ipAddress);
                headers.put("P_IULL_CLIENT_HOST_USER_NAME",hostUserName);
                headers.put("P_IULL_SESSION_USER_ID",userId);
                headers.put("P_IULL_OS_NAME",osName);
                return headers;
            }
        };

        StringRequest trackerFlagReq = new StringRequest(Request.Method.GET, trackerFlagUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject trackerInfo = array.getJSONObject(i);
                        trackerAvailable = Integer.parseInt(trackerInfo.getString("emp_timeline_tracker_flag")
                                        .equals("null") ? "0" : trackerInfo.getString("emp_timeline_tracker_flag"));
                    }
                }
                if (loginLog_check) {
                    requestQueue.add(loginLogReq);
                }
                else {
                    requestQueue.add(imageReq);
                }
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateInterface();
        });

        StringRequest leaveDataReq = new StringRequest(Request.Method.GET, leaveDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject leaveInfo = array.getJSONObject(i);
                        String lc_short_code = leaveInfo.getString("lc_short_code")
                                .equals("null") ? "" : leaveInfo.getString("lc_short_code");
                        String balance_all = leaveInfo.getString("balance")
                                .equals("null") ? "0" : leaveInfo.getString("balance");
                        String quantity = leaveInfo.getString("quantity");

                        if (!quantity.equals("null") && !quantity.equals("NULL")) {
                            if (!quantity.equals("0")) {
                                balance.add(new BarEntry(i, Float.parseFloat(balance_all),i));
                                earn.add(new BarEntry(i, Float.parseFloat(quantity),i));
                                shortCode.add(lc_short_code);
                            }
                        }
                    }
                }
                requestQueue.add(trackerFlagReq);

            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateInterface();
        });

        StringRequest attendDataReq = new StringRequest(Request.Method.GET, attendDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject attendanceInfo = array.getJSONObject(i);
                        absent = attendanceInfo.getString("absent")
                                .equals("null") ? "0" : attendanceInfo.getString("absent");
                        present = attendanceInfo.getString("present")
                                .equals("null") ? "0" : attendanceInfo.getString("present");
                        leave = attendanceInfo.getString("leave")
                                .equals("null") ? "0" : attendanceInfo.getString("leave");
                        holiday = attendanceInfo.getString("holiday_weekend")
                                .equals("null") ? "0" : attendanceInfo.getString("holiday_weekend");
                        late = attendanceInfo.getString("late_count")
                                .equals("null") ? "0" : attendanceInfo.getString("late_count");
                        early = attendanceInfo.getString("early_count")
                                .equals("null") ? "0" : attendanceInfo.getString("early_count");
                    }
                    requestQueue.add(leaveDataReq);
                }
                else {
                    connected = false;
                    updateInterface();
                }
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateInterface();
        });

        StringRequest salaryMonthReq = new StringRequest(Request.Method.GET, salaryDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject salaryMonthInfo = array.getJSONObject(i);
                        String mon_name = salaryMonthInfo.getString("mon_name")
                                .equals("null") ? "" : salaryMonthInfo.getString("mon_name");
                        String net_salary = salaryMonthInfo.getString("net_salary")
                                .equals("null") ? "0" : salaryMonthInfo.getString("net_salary");

                        salaryMonthLists.add(new SalaryMonthList(mon_name,net_salary));
                    }
                }
                requestQueue.add(attendDataReq);
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateInterface();
        });

        StringRequest empFlagCheckReq = new StringRequest(Request.Method.GET, empFlagUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject info = array.getJSONObject(i);

                        String emp_flag = info.getString("emp_flag")
                                .equals("null") ? "0" : info.getString("emp_flag");

                        checkEmpFlag = emp_flag.equals("0");
                    }
                }
                System.out.println(checkEmpFlag);
                if (checkEmpFlag) {
                    requestQueue.add(salaryMonthReq);
                }
                else {
                    connected = true;
                    updateInterface();
                }
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateInterface();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateInterface();
        });

        requestQueue.add(empFlagCheckReq);
    }

    public void updateInterface() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (checkEmpFlag) {
                    monthName = new ArrayList<>();
                    salary = new ArrayList<>();
                    salaryValue = new ArrayList<>();

                    for (int i = 0; i < salaryMonthLists.size(); i++) {
                        for (int j = 0; j < months.size(); j++) {
                            if (months.get(j).getMonth().equals(salaryMonthLists.get(i).getMonth())) {
                                months.get(j).setSalary(salaryMonthLists.get(i).getSalary());
                            }
                        }
                    }

                    for (int i = months.size()-1; i >= 0; i--) {

                        monthName.add(months.get(i).getMonth());
                        salary.add(months.get(i).getSalary());

                    }

                    System.out.println(monthName);
                    System.out.println(salary);

                    for (int i = 0; i < salary.size(); i++) {
                        salaryValue.add(new BarEntry(i, Float.parseFloat(salary.get(i)),i));
                    }

                    BarDataSet bardataset = new BarDataSet(salaryValue, "Months");
                    salaryChart.animateY(1000);

                    BarData data1 = new BarData(bardataset);
                    bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);

                    bardataset.setBarBorderColor(Color.DKGRAY);
                    bardataset.setValueTextSize(8);
                    bardataset.setValueFormatter(new LargeValueFormatter());
                    salaryChart.setData(data1);
                    salaryChart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
                    salaryChart.getAxisLeft().setValueFormatter(new LargeValueFormatter());


                    // Pie Chart for Attendance
                    final int[] piecolors = new int[]{


                            Color.rgb(116, 185, 255),
                            Color.rgb(85, 239, 196),
                            Color.rgb(162, 155, 254),
                            Color.rgb(223, 230, 233),
                            Color.rgb(255, 234, 167),

                            Color.rgb(250, 177, 160),
                            Color.rgb(178, 190, 195),
                            Color.rgb(129, 236, 236),
                            Color.rgb(255, 118, 117),
                            Color.rgb(253, 121, 168),
                            Color.rgb(96, 163, 188)};


                    if (absent != null) {
                        if (!absent.isEmpty()) {
                            if (!absent.equals("0")) {
                                NoOfEmp.add(new PieEntry(Float.parseFloat(absent), "Absent", 0));
                            }
                        }
                    }

                    if (present != null) {
                        if (!present.isEmpty()) {
                            if (!present.equals("0")) {
                                NoOfEmp.add(new PieEntry(Float.parseFloat(present),"Present", 1));
                            }
                        }
                    }

                    if (late != null) {
                        if (!late.isEmpty()) {
                            if (!late.equals("0")) {
                                NoOfEmp.add(new PieEntry(Float.parseFloat(late),"Late", 2));

                            }
                        }
                    }

                    if (early != null) {
                        if (!early.isEmpty()) {
                            if (!early.equals("0")) {
                                NoOfEmp.add(new PieEntry(Float.parseFloat(early),"Early", 3));
                            }
                        }
                    }

                    if (leave != null) {
                        if (!leave.isEmpty()) {
                            if (!leave.equals("0")) {
                                NoOfEmp.add(new PieEntry(Float.parseFloat(leave),"Leave", 4));
                            }
                        }
                    }

                    if (holiday != null) {
                        if (!holiday.isEmpty()) {
                            if (!holiday.equals("0")) {
                                NoOfEmp.add(new PieEntry(Float.parseFloat(holiday),"Holiday/Weekend", 5));
                            }
                        }
                    }

                    if (NoOfEmp.size() == 0) {
                        NoOfEmp.add(new PieEntry(1,"No Data Found", 6));

                    }

                    PieDataSet dataSet = new PieDataSet(NoOfEmp, "");
                    pieChart.animateXY(1000, 1000);
                    pieChart.setEntryLabelColor(Color.TRANSPARENT);

                    PieData data = new PieData(dataSet);
                    dataSet.setValueFormatter(new ValueFormatter() {
                        @Override
                        public String getFormattedValue(float value) {
                            return String.valueOf((int) Math.floor(value));
                        }
                    });
                    String label = dataSet.getValues().get(0).getLabel();
                    System.out.println(label);
                    if (label.equals("No Data Found")) {
                        dataSet.setValueTextColor(Color.TRANSPARENT);
                    } else {
                        dataSet.setValueTextColor(Color.BLACK);
                    }
                    dataSet.setHighlightEnabled(true);
                    dataSet.setValueTextSize(12);

                    int[] num = new int[NoOfEmp.size()];
                    for (int i = 0; i < NoOfEmp.size(); i++) {
                        int neki = (int) NoOfEmp.get(i).getData();
                        num[i] = piecolors[neki];
                    }

                    dataSet.setColors(ColorTemplate.createColors(num));

                    pieChart.setData(data);
                    pieChart.invalidate();


                    // Leave Multi Bar
                    if (balance.size() == 0 || earn.size() == 0 || shortCode.size() == 0) {
                        // do nothing
                    } else {
                        XAxis xAxis = leaveChart.getXAxis();
                        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                        xAxis.setDrawGridLines(false);
                        xAxis.setCenterAxisLabels(true);
                        xAxis.setAxisMinimum(0);
                        xAxis.setAxisMaximum(balance.size());
                        xAxis.setGranularity(1);


                        BarDataSet set1 = new BarDataSet(earn, "Earned Leave");
                        set1.setColor(ColorTemplate.VORDIPLOM_COLORS[3]);
                        set1.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                return String.valueOf((int) Math.floor(value));
                            }
                        });
                        BarDataSet set2 = new BarDataSet(balance, "Leave Balance");
                        set2.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getFormattedValue(float value) {
                                return String.valueOf((int) Math.floor(value));
                            }
                        });
                        set2.setColor(ColorTemplate.VORDIPLOM_COLORS[2]);

                        float groupSpace = 0.04f;
                        float barSpace = 0.02f; // x2 dataset
                        float barWidth = 0.46f;

                        BarData leavedata = new BarData(set1, set2);
                        if (earn.size() > 5) {
                            leavedata.setValueTextSize(8);
                        }
                        else if (earn.size() > 3){
                            leavedata.setValueTextSize(10);
                        }
                        else {
                            leavedata.setValueTextSize(12);
                        }
                        leavedata.setBarWidth(barWidth); // set the width of each bar
                        leaveChart.animateY(1000);
                        leaveChart.setData(leavedata);
                        leaveChart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
                        leaveChart.invalidate();

                        xAxis.setValueFormatter(new ValueFormatter() {
                            @Override
                            public String getAxisLabel(float value, AxisBase axis) {
                                if (value < 0 || value >= shortCode.size()) {
                                    return null;
                                } else {
//                                System.out.println(value);
//                                System.out.println(axis);
//                                System.out.println(shortCode.get((int)value));
                                    return (shortCode.get((int) value));
                                }

                            }
                        });
                    }

                    createNotificationChannelForWidget();

                    if (trackerAvailable == 1) {
                        sharedSchedule = getSharedPreferences(SCHEDULING_FILE, MODE_PRIVATE);
                        boolean isNewLogin = sharedSchedule.getBoolean(TRIGGERING,false);

                        // Triggering Schedule
                        System.out.println("SCHEDULING TASK STARTED");
                        SharedPreferences.Editor editor = sharedSchedule.edit();
                        editor.remove(SCHEDULING_EMP_ID);
                        editor.remove(TRIGGERING);

                        editor.putString(SCHEDULING_EMP_ID,emp_id);
                        editor.putBoolean(TRIGGERING, true);
                        editor.apply();
                        editor.commit();
                        createNotificationChannel();

                        Intent intent = new Intent(Dashboard.this, Uploader.class);
                        PendingIntent pendingIntent = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent,PendingIntent.FLAG_IMMUTABLE);
                        }


                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 5);
                        calendar.set(Calendar.SECOND, 0);

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_HALF_HOUR,pendingIntent);
                    }

                    if (imageFound) {
                        Glide.with(Dashboard.this)
                                .load(selectedImage)
                                .fitCenter()
                                .into(userImage);
                    }
                    else {
                        userImage.setImageResource(R.drawable.profile);
                    }
                    conn = false;
                    connected = false;
                    checkEmpFlag = false;
                }
                else {
                    MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(Dashboard.this)
                            .setTitle("FORCED LOGOUT!")
                            .setMessage("Some Important Data have been changed according to your profile. That's why you have been forced logout from application. To continue please login again.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
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
                                    editor1.remove(LOGIN_TF);

                                    editor1.remove(IS_ATT_APPROVED);
                                    editor1.remove(IS_LEAVE_APPROVED);
                                    editor1.remove(LIVE_FLAG);
//                                    editor1.remove(DATABASE_NAME);
                                    editor1.apply();
                                    editor1.commit();

                                    if (trackerAvailable == 1) {
                                        sharedSchedule = getSharedPreferences(SCHEDULING_FILE, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedSchedule.edit();
                                        editor.remove(SCHEDULING_EMP_ID);
                                        editor.remove(TRIGGERING);
                                        editor.apply();
                                        editor.commit();

                                        Intent intent1 = new Intent(Dashboard.this, Uploader.class);
                                        PendingIntent pendingIntent = null;
                                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                            pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1,PendingIntent.FLAG_IMMUTABLE);
                                        }
                                        alarmManager.cancel(pendingIntent);
                                    }

                                    Intent intent = new Intent(Dashboard.this, Login.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    AlertDialog alert = dialogBuilder.create();
                    alert.setCanceledOnTouchOutside(false);
                    alert.setCancelable(false);
                    alert.show();
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    waitProgress.show(getSupportFragmentManager(),"WaitBar");
                    waitProgress.setCancelable(false);
                    getAllData();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    if (loginLog_check) {
                        userInfoLists.clear();
                        userDesignations.clear();
                        userInfoLists = new ArrayList<>();
                        userDesignations = new ArrayList<>();
                        isApproved = 0;
                        isLeaveApproved = 0;
                        dialog.dismiss();
                        finish();
                    } else {
                        dialog.dismiss();
                    }
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(Dashboard.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                waitProgress.show(getSupportFragmentManager(),"WaitBar");
                waitProgress.setCancelable(false);
                getAllData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                if (loginLog_check) {
                    userInfoLists.clear();
                    userDesignations.clear();
                    userInfoLists = new ArrayList<>();
                    userDesignations = new ArrayList<>();
                    isApproved = 0;
                    isLeaveApproved = 0;
                    dialog.dismiss();
                    finish();
                } else {
                    dialog.dismiss();
                }
            });
        }
    }

    public void attendanceShortcutTriggered() {
        attendanceWidgetPreferences = getSharedPreferences(WIDGET_FILE,MODE_PRIVATE);
        emp_id = attendanceWidgetPreferences.getString(WIDGET_EMP_ID,"");
        String tracker_flag = attendanceWidgetPreferences.getString(WIDGET_TRACKER_FLAG,"");

        if (!emp_id.isEmpty()) {
            if (tracker_flag.equals("1")) {
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.att_channel_id))
                        .setSmallIcon(R.drawable.notification_icon)
                        .setContentTitle("Attendance System")
                        .setContentText("Your tracking flag is active. You need to give attendance from the app.")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
                notificationManagerCompat.notify(222, builder.build());
            }
            else {
                doWork();
            }
        }
        else {
            Toast.makeText(this, "Please Login to Elite Force", Toast.LENGTH_SHORT).show();
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
                System.out.println(ts.toString());
                inTime = df.format(c);
                timeToShow = dftoShow.format(c);
                System.out.println("IN TIME : " + inTime);
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

            getNotification("Attendance System","Please check your Location Permission to access this feature.");
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper());
        System.out.println("ADADADA");
    }

    private void stopLocationUpdate() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        getOfficeLocation();
    }

    public void doWork() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
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
        } else {
            getNotification("Attendance System", "Your GPS is disabled. Please enable it and try again.");
        }
    }

    public void getOfficeLocation() {

        String offLocationUrl = api_url_front+"attendance/getOffLatLong/"+emp_id+"";

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
                        officeLatitude = offLocInfo.getString("coa_latitude").equals("null") ? null : offLocInfo.getString("coa_latitude");
                        officeLongitude = offLocInfo.getString("coa_longitude").equals("null") ? null : offLocInfo.getString("coa_longitude");
                        coverage = offLocInfo.getString("coa_coverage").equals("null") ? null : offLocInfo.getString("coa_coverage");
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

        requestQueue.add(offLocReq);
    }

    public void updateInfo() {
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
                if (!inTime.isEmpty()) {
                    LatLng c_latLng = new LatLng(0,0);
                    if (officeLatitude != null && officeLongitude != null) {
                        if (!officeLatitude.isEmpty() && !officeLongitude.isEmpty()) {
                            c_latLng = new LatLng(Double.parseDouble(officeLatitude),Double.parseDouble(officeLongitude));
                        }
                    }
                    if (c_latLng.latitude != 0 && c_latLng.longitude != 0) {
                        float[] distance = new float[1];
                        Location.distanceBetween(c_latLng.latitude,c_latLng.longitude,lastLatLongitude[0].latitude,lastLatLongitude[0].longitude,distance);

                        float radius = 0;
                        if (coverage != null) {
                            if (!coverage.isEmpty()) {
                                radius = Float.parseFloat(coverage);
                            }
                        }

                        if (radius == 0) {
                            machineCode = "3";
                        } else {
                            machineCode = "1";
                        }

                        if (distance[0] <= radius || radius == 0) {
                            new CheckAddress().execute();
                        }
                        else {
                            getNotification("Attendance System","You are not around your office area");
                        }
                    }
                    else {
                        machineCode = "3";
                        new CheckAddress().execute();
                    }
                }
                else {
                    getNotification("Attendance System","Failed to get Location");
                }
            }
            else {
                getNotification("Attendance System","There is a network issue in the server. Please Try later.");
            }
        }
        else {
            getNotification("Attendance System","Please Check Your Internet Connection.");
        }
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            @SuppressLint("MissingPermission") NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", Objects.requireNonNull(e.getMessage()));
        }
        return connected;
    }
    public boolean isOnline() {

        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException | InterruptedException e)          { e.printStackTrace(); }

        return false;
    }

    public class CheckAddress extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {
                conn = true;
                getAddress(Double.parseDouble(lat),Double.parseDouble(lon));
            }
            else {
                conn = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (conn) {
                conn = false;
                giveAttendance();
            }
            else {
                getNotification("Attendance System","Please Check Your Internet Connection.");
            }

        }
    }

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
            e.printStackTrace();
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
            public Map<String, String> getHeaders() throws AuthFailureError {
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

    private  void updateLayout() {
        if (conn) {
            if (connected) {
                connected = false;
                conn = false;
                getNotification("Attendance System", "Your Attendance is Recorded at "+timeToShow+".");

            }
            else {
                getNotification("Attendance System", "There is a network issue in the server. Please Try later.");
            }
        }
        else {
            getNotification("Attendance System", "Please Check Your Internet Connection.");
        }
    }

    public void getNotification(String title, String msg) {
        waitProgress.dismiss();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, getString(R.string.att_channel_id))
                .setSmallIcon(R.drawable.notification_icon)
                .setContentTitle(title)
                .setContentText(msg)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(222, builder.build());
    }
}