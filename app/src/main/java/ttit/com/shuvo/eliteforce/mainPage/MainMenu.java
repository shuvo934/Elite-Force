package ttit.com.shuvo.eliteforce.mainPage;

import static ttit.com.shuvo.eliteforce.dashboard.Dashboard.alarmManager;
import static ttit.com.shuvo.eliteforce.dashboard.Dashboard.selectedImage;
import static ttit.com.shuvo.eliteforce.dashboard.Dashboard.trackerAvailable;
import static ttit.com.shuvo.eliteforce.login.Login.isApproved;
import static ttit.com.shuvo.eliteforce.login.Login.isLeaveApproved;
import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.Calendar;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.Attendance;
import ttit.com.shuvo.eliteforce.attendance.trackService.Service;
import ttit.com.shuvo.eliteforce.dashboard.Dashboard;
import ttit.com.shuvo.eliteforce.directory.Directory;
import ttit.com.shuvo.eliteforce.employeeInfo.EmployeeInformation;
import ttit.com.shuvo.eliteforce.leave.Leave;
import ttit.com.shuvo.eliteforce.login.Login;
import ttit.com.shuvo.eliteforce.payRoll.PayRollInfo;
import ttit.com.shuvo.eliteforce.scheduler.Uploader;

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

    ImageView dashB;
    ImageView logoutImage;

    SharedPreferences sharedPreferences;
    SharedPreferences sharedSchedule;
    SharedPreferences attendanceWidgetPreferences;

    public static final String SCHEDULING_FILE = "SCHEDULING FILE_EF_HR";
    public static final String SCHEDULING_EMP_ID = "SCHEDULING EMP ID";
    public static final String TRIGGERING = "TRIGGER TRUE FALSE";

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

    public static final String WIDGET_FILE = "WIDGET_FILE_EF_HR";
    public static final String WIDGET_EMP_ID = "WIDGET_EMP_ID";
    public static final String WIDGET_TRACKER_FLAG = "WIDGET_TRACKER_FLAG";
    TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        attendanceWidgetPreferences = getSharedPreferences(WIDGET_FILE,MODE_PRIVATE);
        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        sharedSchedule = getSharedPreferences(SCHEDULING_FILE, MODE_PRIVATE);

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
        welcomeText = findViewById(R.id.welcome_text_view_main_menu);

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

        dashB.setOnClickListener(v -> {
            Intent intent = new Intent(MainMenu.this, Dashboard.class);
            intent.putExtra("FROMMAINMENU", false);
            startActivity(intent);
            finish();
        });

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

                                Intent intent1 = new Intent(MainMenu.this, Uploader.class);
                                PendingIntent pendingIntent = null;
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                    pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent1,PendingIntent.FLAG_IMMUTABLE);
                                }
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
    public void onBackPressed() {
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
}