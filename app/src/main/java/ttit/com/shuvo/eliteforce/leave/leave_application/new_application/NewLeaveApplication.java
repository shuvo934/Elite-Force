package ttit.com.shuvo.eliteforce.leave.leave_application.new_application;

import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.basic_model.SelectAllList;
import ttit.com.shuvo.eliteforce.dialogue_box.DialogueText;
import ttit.com.shuvo.eliteforce.dialogue_box.SelectAll;
import ttit.com.shuvo.eliteforce.leave.leave_application.new_application.dialogue_box.SelectLeaveType;
import ttit.com.shuvo.eliteforce.leave.leave_application.new_application.dialogue_box.ShowLeaveBalance;
import ttit.com.shuvo.eliteforce.leave.leave_application.new_application.model.LeaveTypeList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class NewLeaveApplication extends AppCompatActivity {

    public static int showD = 0;
    public static Boolean isOtherReason = false;
    public static int leaveNumber = 0;
    public static int dialogText_leave = 0;

    public static String hint = "";
    public static String text = "";

    public static TextInputLayout leaveTypeLay;
    TextInputLayout dateOnLay;
    TextInputLayout dateToLay;
    public static TextInputLayout otherReasonInputLay;
    public static TextInputLayout leaveAddressLay;

    TextInputEditText name;
    TextInputEditText idEmployee;
    TextInputEditText todayDate;
    public static TextInputEditText leaveType;
    TextInputEditText dateOn;
    TextInputEditText dateTo;
    TextInputEditText totalDays;

    public static TextInputEditText reason;
    public static TextInputEditText otherReason;
    public static TextInputEditText leaveAddress;
    public static TextInputEditText workBackup;

    Spinner leaveApp;
    Spinner leaveDuration;

    ArrayList<String> leaveAppList;

    ArrayList<LeaveTypeList> leaveDurationAllLists;
    ArrayList<String> leaveDurList;

    ArrayAdapter<String> leaveAppAdapter;
    ArrayAdapter<String> leaveDurationAdapter;

    LinearLayout afterselecting;
    public static LinearLayout otherReasonLay;

    Button leaveBalance;
    Button apply;
    LinearLayout applyButtonEnable;

    TextView errorLeaveDuration;
    public static TextView errorReason;
    public static TextView errorBackup;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    private Boolean insertCon = false;
    private Boolean insertConnn = false;
    private Boolean insertedCon = false;

    private int mYear, mMonth, mDay;

    String emp_id = "";
    String emp_name = "";
    String user_id = "";
    String div_id = "";
    String formattedDate = "";

    public static String selected_leave_type_name = "";
    public static String selected_leave_type_id = "";
    String selected_application_type = "";
    String selected_leave_duration = "";
    String selected_leave_duration_id = "";
    public static String selected_reason = "";
    String selected_leave_address = "";
    public static String selected_worker = "";
    public static String selected_worker_id = "";
    String date_of_today = "";
    String selected_jsm_id = "";
    String calling_title = "";
    String selected_dept_id = "";
    String selected_divm_id = "";
    String selected_date_on_from = "";
    String selected_date_to = "";
    String selected_total_leave_days = "";
    String leaveAppCheck = "";

    ArrayList<LeaveTypeList> leaveTypeLists;
    ArrayList<LeaveTypeList> allreasonLists;
    public static ArrayList<LeaveTypeList> selectingIndivdual;
    public static ArrayList<SelectAllList> allWorkBackup;
    ArrayList<SelectAllList> workBackupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_leave_application);

        leaveTypeLay = findViewById(R.id.leave_type_Layout);
        dateOnLay = findViewById(R.id.date_on_leave_layout);
        dateToLay = findViewById(R.id.date_to_leave_layout);
        otherReasonInputLay = findViewById(R.id.other_reason_description_Layout);
        leaveAddressLay = findViewById(R.id.during_leave_address_layout);

        name = findViewById(R.id.name_leave_application);
        idEmployee = findViewById(R.id.id_leave_application);
        todayDate = findViewById(R.id.now_date_leave_application);
        leaveType = findViewById(R.id.leave_type);
        dateOn = findViewById(R.id.date_on_from);
        dateTo = findViewById(R.id.date_to_leave);
        totalDays = findViewById(R.id.total_days_from_to);
        reason = findViewById(R.id.reason_leave);
        otherReason = findViewById(R.id.other_reason_description);
        leaveAddress = findViewById(R.id.during_leave_address);
        workBackup = findViewById(R.id.work_backup_employee);

        leaveApp = findViewById(R.id.spinner_leave_application_type);
        leaveDuration = findViewById(R.id.spinner_leave_duration);

        afterselecting = findViewById(R.id.afterselecting_leave_app);
        otherReasonLay = findViewById(R.id.other_reason_description_Layout_layout);

        leaveBalance = findViewById(R.id.show_leave_balance);
        apply = findViewById(R.id.leave_new_application_button);
        applyButtonEnable = findViewById(R.id.linearLayout50_leave_application_apply_button);

        errorLeaveDuration = findViewById(R.id.error_input_leave_duration);
        errorReason = findViewById(R.id.error_input_reason_leave);
        errorBackup = findViewById(R.id.error_input_work_backup);

        leaveAppList = new ArrayList<>();

        leaveDurationAllLists = new ArrayList<>();
        leaveDurList = new ArrayList<>();

        leaveTypeLists = new ArrayList<>();
        allreasonLists = new ArrayList<>();
        selectingIndivdual = new ArrayList<>();
        workBackupList = new ArrayList<>();

        leaveDurationAllLists.add(new LeaveTypeList("0","Full Day"));
        leaveDurationAllLists.add(new LeaveTypeList("1","First Half"));
        leaveDurationAllLists.add(new LeaveTypeList("2","Second Half"));

        leaveDurList.add("Select");

        for (int i = 0 ; i < leaveDurationAllLists.size(); i++) {
            leaveDurList.add(leaveDurationAllLists.get(i).getTypeName());
        }

        leaveAppList.add("Select");
        leaveAppList.add("PRE");
        leaveAppList.add("POST");

        emp_id = userInfoLists.get(0).getEmp_id();

        if (userInfoLists.size() != 0) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            emp_name = firstname+" "+lastName;
            name.setText(emp_name);
        }

        user_id = userInfoLists.get(0).getUserName();

        div_id = userDesignations.get(0).getDiv_id();

        idEmployee.setText(user_id);

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        formattedDate = df.format(c);

        todayDate.setText(formattedDate);

        // Spinner Application Type
        leaveAppAdapter = new ArrayAdapter<String>(
                this,R.layout.item_country,leaveAppList){
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        leaveApp.setGravity(Gravity.END);
        leaveAppAdapter.setDropDownViewResource(R.layout.item_country);
        leaveApp.setAdapter(leaveAppAdapter);

        // Selecting Application Type
        leaveApp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    selected_application_type = (String) parent.getItemAtPosition(position);
                    dateOn.setText("");
                    dateOnLay.setHint("Select Date");
                    dateOnLay.setHelperText("");

                    dateTo.setText("");
                    dateToLay.setHint("Select Date");
                    dateToLay.setHelperText("");

                    totalDays.setText("0");

                    // Notify the selected item text
                    System.out.println(selected_application_type);
                    if (!selected_application_type.isEmpty()) {

                        System.out.println(1);
                        afterselecting.setVisibility(View.VISIBLE);
                        applyButtonEnable.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Spinner Leave Duration
        leaveDurationAdapter = new ArrayAdapter<String>(
                this,R.layout.item_country,leaveDurList){
            @Override
            public boolean isEnabled(int position){
                // Disable the first item from Spinner
                // First item will be use for hint
                return position != 0;
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(R.id.tvCountry);
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        leaveDuration.setGravity(Gravity.END);
        leaveDurationAdapter.setDropDownViewResource(R.layout.item_country);
        leaveDuration.setAdapter(leaveDurationAdapter);

        // Selecting Attendance Type
        leaveDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    selected_leave_duration = (String) parent.getItemAtPosition(position);
                    errorLeaveDuration.setVisibility(View.GONE);

                    for (int i = 0; i < leaveDurationAllLists.size(); i++) {
                        if (selected_leave_duration.equals(leaveDurationAllLists.get(i).getTypeName())) {
                            selected_leave_duration_id = leaveDurationAllLists.get(i).getId();

                            System.out.println(selected_leave_duration_id);
                        }
                    }
                    String value = Objects.requireNonNull(totalDays.getText()).toString();
                    if (!value.equals("0")) {
                        if (!selected_leave_duration_id.equals("0")) {

                            if (!value.contains(".")) {
                                double dd = Double.parseDouble(value);
                                dd = dd - 0.5;
                                totalDays.setText(String.valueOf(dd));
                            }
                        }
                        else {
                            if (value.contains(".")) {
                                double dd = Double.parseDouble(value);
                                dd = dd + 0.5;
                                int dddd = (int) dd;
                                totalDays.setText(String.valueOf(dddd));
                            }
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // DATE FROM/ON
        dateOn.setOnClickListener(v -> {
            final Calendar c1 = Calendar.getInstance();
            mYear = c1.get(Calendar.YEAR);
            mMonth = c1.get(Calendar.MONTH);
            mDay = c1.get(Calendar.DAY_OF_MONTH);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewLeaveApplication.this, (view, year, month, dayOfMonth) -> {

                    String monthName = "";
                    String dayOfMonthName = "";
                    String yearName = "";
                    month = month + 1;
                    if (month == 1) {
                        monthName = "JAN";
                    } else if (month == 2) {
                        monthName = "FEB";
                    } else if (month == 3) {
                        monthName = "MAR";
                    } else if (month == 4) {
                        monthName = "APR";
                    } else if (month == 5) {
                        monthName = "MAY";
                    } else if (month == 6) {
                        monthName = "JUN";
                    } else if (month == 7) {
                        monthName = "JUL";
                    } else if (month == 8) {
                        monthName = "AUG";
                    } else if (month == 9) {
                        monthName = "SEP";
                    } else if (month == 10) {
                        monthName = "OCT";
                    } else if (month == 11) {
                        monthName = "NOV";
                    } else if (month == 12) {
                        monthName = "DEC";
                    }

                    if (dayOfMonth <= 9) {
                        dayOfMonthName = "0" + dayOfMonth;
                    } else {
                        dayOfMonthName = String.valueOf(dayOfMonth);
                    }
                    yearName  = String.valueOf(year);
                    yearName = yearName.substring(yearName.length()-2);
                    System.out.println(yearName);
                    System.out.println(dayOfMonthName);
                    String dateOnText = dayOfMonthName + "-" + monthName + "-" + yearName;
                    dateOn.setText(dateOnText);

                    if (selected_application_type.equals("PRE")) {

                        String today = Objects.requireNonNull(todayDate.getText()).toString();
                        String updateDate = Objects.requireNonNull(dateOn.getText()).toString();

                        Date nowDate = null;
                        Date givenDate = null;

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
                        try {
                            nowDate = sdf.parse(today);
                            givenDate = sdf.parse(updateDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (nowDate != null && givenDate != null) {

                            if (givenDate.after(nowDate) || givenDate.equals(nowDate)) {

                                String datetoooo = "";
                                datetoooo = Objects.requireNonNull(dateTo.getText()).toString();

                                if (datetoooo.isEmpty()) {
                                    String te = dayOfMonthName + "-" + monthName + "-" + yearName;
                                    dateOn.setText(te);
                                    dateOnLay.setHelperText("");
                                    dateOnLay.setHint("Date");
                                    totalDays.setText("0");
                                    System.out.println("AHSE PRE");
                                }
                                else {

                                    Date date = null;
                                    try {
                                        date = sdf.parse(datetoooo);
                                    }
                                    catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (date != null) {
                                        if (givenDate.before(date) || givenDate.equals(date)) {
                                            String text = dayOfMonthName + "-" + monthName + "-" + yearName;
                                            dateOn.setText(text);
                                            dateOnLay.setHelperText("");
                                            dateOnLay.setHint("Date");
                                            System.out.println("AHSE PRE");
                                            long diff = date.getTime() - givenDate.getTime();
                                            long diffDays = diff / (24 * 60 * 60 * 1000);
                                            int ddd = (int) diffDays;
                                            ddd = ddd + 1;
                                            totalDays.setText(String.valueOf(ddd));
                                            if (selected_leave_duration_id.isEmpty() || selected_leave_duration_id.equals("0")) {
                                                totalDays.setText(String.valueOf(ddd));
                                            }
                                            else {
                                                Double value =  ddd - 0.5;
                                                totalDays.setText(String.valueOf(value));
                                            }
                                            System.out.println("Days: "+ddd);
                                        }
                                        else {
                                            dateOnLay.setHelperText("From Date should be Less than To Date");
                                            dateOnLay.setHint("Select Date");
                                            totalDays.setText("0");
                                            dateOnLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                            dateOn.setText("");
                                        }
                                    }
                                }


                            }
                            else {
                                dateOnLay.setHelperText("From Date never Less than Application Date");
                                dateOnLay.setHint("Select Date");
                                totalDays.setText("0");
                                dateOnLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                dateOn.setText("");
                            }
                        }
                    }
                    else if (selected_application_type.equals("POST")) {

                        String today = Objects.requireNonNull(todayDate.getText()).toString();
                        String updateDate = Objects.requireNonNull(dateOn.getText()).toString();

                        Date nowDate = null;
                        Date givenDate = null;

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
                        try {
                            nowDate = sdf.parse(today);
                            givenDate = sdf.parse(updateDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (nowDate != null && givenDate != null) {

                            if (givenDate.before(nowDate) || givenDate.equals(nowDate) ) {

                                String datetoooo = "";
                                datetoooo = Objects.requireNonNull(dateTo.getText()).toString();

                                if (datetoooo.isEmpty()) {
                                    String dt = dayOfMonthName + "-" + monthName + "-" + yearName;
                                    dateOn.setText(dt);
                                    dateOnLay.setHelperText("");
                                    dateOnLay.setHint("Date");
                                    totalDays.setText("0");
                                    System.out.println("AHSE POST");
                                }
                                else {

                                    Date date = null;
                                    try {
                                        date = sdf.parse(datetoooo);
                                    }
                                    catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (date != null) {
                                        if (givenDate.before(date) || givenDate.equals(date)) {
                                            String dot = dayOfMonthName + "-" + monthName + "-" + yearName;
                                            dateOn.setText(dot);
                                            dateOnLay.setHelperText("");
                                            dateOnLay.setHint("Date");
                                            System.out.println("AHSE POST");
                                            long diff = date.getTime() - givenDate.getTime();
                                            long diffDays = diff / (24 * 60 * 60 * 1000);
                                            int ddd = (int) diffDays;
                                            ddd = ddd + 1;
                                            totalDays.setText(String.valueOf(ddd));
                                            if (selected_leave_duration_id.isEmpty() || selected_leave_duration_id.equals("0")) {
                                                totalDays.setText(String.valueOf(ddd));
                                            }
                                            else {
                                                Double value =  ddd - 0.5;
                                                totalDays.setText(String.valueOf(value));
                                            }
                                            System.out.println("Days: "+ddd);
                                        }
                                        else {
                                            dateOnLay.setHelperText("From Date should be Less than To Date");
                                            dateOnLay.setHint("Select Date");
                                            totalDays.setText("0");
                                            dateOnLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                            dateOn.setText("");
                                        }
                                    }
                                }
                            }
                            else {
                                dateOnLay.setHelperText("From Date should be Less than Application Date");
                                dateOnLay.setHint("Select Date");
                                totalDays.setText("0");
                                dateOnLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                dateOn.setText("");
                            }
                        }
                    }

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // DATE TO
        dateTo.setOnClickListener(v -> {
            final Calendar c12 = Calendar.getInstance();
            mYear = c12.get(Calendar.YEAR);
            mMonth = c12.get(Calendar.MONTH);
            mDay = c12.get(Calendar.DAY_OF_MONTH);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewLeaveApplication.this, (view, year, month, dayOfMonth) -> {

                    String monthName = "";
                    String dayOfMonthName = "";
                    String yearName = "";
                    month = month + 1;
                    if (month == 1) {
                        monthName = "JAN";
                    } else if (month == 2) {
                        monthName = "FEB";
                    } else if (month == 3) {
                        monthName = "MAR";
                    } else if (month == 4) {
                        monthName = "APR";
                    } else if (month == 5) {
                        monthName = "MAY";
                    } else if (month == 6) {
                        monthName = "JUN";
                    } else if (month == 7) {
                        monthName = "JUL";
                    } else if (month == 8) {
                        monthName = "AUG";
                    } else if (month == 9) {
                        monthName = "SEP";
                    } else if (month == 10) {
                        monthName = "OCT";
                    } else if (month == 11) {
                        monthName = "NOV";
                    } else if (month == 12) {
                        monthName = "DEC";
                    }

                    if (dayOfMonth <= 9) {
                        dayOfMonthName = "0" + dayOfMonth;
                    } else {
                        dayOfMonthName = String.valueOf(dayOfMonth);
                    }
                    yearName  = String.valueOf(year);
                    yearName = yearName.substring(yearName.length()-2);
                    System.out.println(yearName);
                    System.out.println(dayOfMonthName);
                    String dateToText = dayOfMonthName + "-" + monthName + "-" + yearName;
                    dateTo.setText(dateToText);

                    if (selected_application_type.equals("PRE")) {

                        String today = Objects.requireNonNull(todayDate.getText()).toString();
                        String updateDate = Objects.requireNonNull(dateTo.getText()).toString();

                        Date nowDate = null;
                        Date givenDate = null;

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
                        try {
                            nowDate = sdf.parse(today);
                            givenDate = sdf.parse(updateDate);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (nowDate != null && givenDate != null) {

                            if (givenDate.after(nowDate) || givenDate.equals(nowDate)) {

                                String dateonnnn = "";
                                dateonnnn = Objects.requireNonNull(dateOn.getText()).toString();

                                if (dateonnnn.isEmpty()) {
                                    String te = dayOfMonthName + "-" + monthName + "-" + yearName;
                                    dateTo.setText(te);
                                    dateToLay.setHelperText("");
                                    dateToLay.setHint("Date");
                                    totalDays.setText("0");
                                    System.out.println("AHSE PRE");
                                }
                                else {

                                    Date date = null;
                                    try {
                                        date = sdf.parse(dateonnnn);
                                    }
                                    catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (date != null) {

                                        if (givenDate.after(date) || givenDate.equals(date)) {
                                            String text = dayOfMonthName + "-" + monthName + "-" + yearName;
                                            dateTo.setText(text);
                                            dateToLay.setHelperText("");
                                            dateToLay.setHint("Date");
                                            System.out.println("AHSE PRE");
                                            long diff = givenDate.getTime() - date.getTime();
                                            long diffDays = diff / (24 * 60 * 60 * 1000);
                                            int ddd = (int) diffDays;
                                            ddd = ddd + 1;
                                            totalDays.setText(String.valueOf(ddd));
                                            if (selected_leave_duration_id.isEmpty() || selected_leave_duration_id.equals("0")) {
                                                totalDays.setText(String.valueOf(ddd));
                                            }
                                            else {
                                                Double value =  ddd - 0.5;
                                                totalDays.setText(String.valueOf(value));
                                            }
                                            System.out.println("Days: "+ddd);
                                        }
                                        else {
                                            dateToLay.setHelperText("To Date should be Greater than From Date");
                                            dateToLay.setHint("Select Date");
                                            totalDays.setText("0");
                                            dateToLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                            dateTo.setText("");
                                        }
                                    }
                                }

                            }
                            else {
                                dateToLay.setHelperText("To Date never Less than Application Date");
                                dateToLay.setHint("Select Date");
                                totalDays.setText("0");
                                dateToLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                dateTo.setText("");
                            }
                        }
                    }
                    else if (selected_application_type.equals("POST")) {

                        String today = Objects.requireNonNull(todayDate.getText()).toString();
                        String updateDate = Objects.requireNonNull(dateTo.getText()).toString();

                        Date nowDate = null;
                        Date givenDate = null;

                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
                        try {
                            nowDate = sdf.parse(today);
                            givenDate = sdf.parse(updateDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (nowDate != null && givenDate != null) {

                            if (givenDate.before(nowDate) || givenDate.equals(nowDate)) {

                                String dateonnnn = "";
                                dateonnnn = Objects.requireNonNull(dateOn.getText()).toString();

                                if (dateonnnn.isEmpty()) {
                                    String dtt = dayOfMonthName + "-" + monthName + "-" + yearName;
                                    dateTo.setText(dtt);
                                    dateToLay.setHelperText("");
                                    dateToLay.setHint("Date");
                                    totalDays.setText("0");
                                    System.out.println("AHSE POST");
                                }
                                else {

                                    Date date = null;
                                    try {
                                        date = sdf.parse(dateonnnn);
                                    }
                                    catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                    if (date != null) {

                                        if (givenDate.after(date) || givenDate.equals(date)) {
                                            String tt = dayOfMonthName + "-" + monthName + "-" + yearName;
                                            dateTo.setText(tt);
                                            dateToLay.setHelperText("");
                                            dateToLay.setHint("Date");
                                            System.out.println("AHSE POST");
                                            long diff = givenDate.getTime() - date.getTime();
                                            long diffDays = diff / (24 * 60 * 60 * 1000);
                                            int ddd = (int) diffDays;
                                            ddd = ddd + 1;
                                            totalDays.setText(String.valueOf(ddd));
                                            if (selected_leave_duration_id.isEmpty() || selected_leave_duration_id.equals("0")) {
                                                totalDays.setText(String.valueOf(ddd));
                                            }
                                            else {
                                                Double value =  ddd - 0.5;
                                                totalDays.setText(String.valueOf(value));
                                            }
                                            System.out.println("Days: "+ddd);

                                        }
                                        else {
                                            dateToLay.setHelperText("To Date should be Greater than From Date");
                                            dateToLay.setHint("Select Date");
                                            totalDays.setText("0");
                                            dateToLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                            dateTo.setText("");
                                        }
                                    }
                                }
                            }
                            else {
                                dateToLay.setHelperText("To Date should be Less than Application Date");
                                dateToLay.setHint("Select Date");
                                totalDays.setText("0");
                                dateToLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                                dateTo.setText("");
                            }
                        }
                    }

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Show Leave Balance
        leaveBalance.setOnClickListener(v -> {
            ShowLeaveBalance showLeaveBalance = new ShowLeaveBalance(NewLeaveApplication.this);
            showLeaveBalance.show(getSupportFragmentManager(),"BALANCE");
        });

        // Select leave type
        leaveType.setOnClickListener(v -> {
            showD = 1;
            selectingIndivdual = leaveTypeLists;
            SelectLeaveType selectLeaveType = new SelectLeaveType();
            selectLeaveType.show(getSupportFragmentManager(),"LeaveType");
        });
        
        // Select Reason
        reason.setOnClickListener(v -> {
            showD = 2;
            selectingIndivdual = allreasonLists;
            SelectLeaveType selectLeaveType = new SelectLeaveType();
            selectLeaveType.show(getSupportFragmentManager(),"LeaveReason");
        });

        // Writing Other Reason
        otherReason.setOnClickListener(v -> {
            leaveNumber = 1;
            hint = Objects.requireNonNull(otherReasonInputLay.getHint()).toString();
            text = Objects.requireNonNull(otherReason.getText()).toString();
            DialogueText dialogueText = new DialogueText();
            dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
        });
        
        // Writing Leave Address
        leaveAddress.setOnClickListener(v -> {
            leaveNumber = 2;
            hint = Objects.requireNonNull(leaveAddressLay.getHint()).toString();
            text = Objects.requireNonNull(leaveAddress.getText()).toString();
            DialogueText dialogueText = new DialogueText();
            dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
        });
        
        // Selecting work backup eployee
        workBackup.setOnClickListener(v -> {
            dialogText_leave = 1;
            allWorkBackup = workBackupList;
            SelectAll selectAll = new SelectAll();
            selectAll.show(getSupportFragmentManager(),"TEXTEDIT");
        });

        apply.setOnClickListener(v -> {
            date_of_today = Objects.requireNonNull(todayDate.getText()).toString();
            selected_date_on_from = Objects.requireNonNull(dateOn.getText()).toString();
            selected_date_to = Objects.requireNonNull(dateTo.getText()).toString();
            selected_leave_address = Objects.requireNonNull(leaveAddress.getText()).toString();
            selected_total_leave_days = Objects.requireNonNull(totalDays.getText()).toString();

            selected_reason = Objects.requireNonNull(reason.getText()).toString();
            
            if (selected_reason.equals("Select")) {
                selected_reason = "";
            }

            if (!selected_leave_type_name.isEmpty() && !selected_leave_type_id.isEmpty()) {
                leaveTypeLay.setHelperText("");
                if (!selected_date_on_from.isEmpty()) {
                    dateOnLay.setHelperText("");
                    if (!selected_date_to.isEmpty()) {
                        dateToLay.setHelperText("");
                        if (!selected_leave_duration.isEmpty() && !selected_leave_duration_id.isEmpty()) {
                            errorLeaveDuration.setVisibility(View.GONE);
                            if (!selected_reason.isEmpty()) {
                                errorReason.setVisibility(View.GONE);
                                if (selected_reason.equals("Others")) {
                                    selected_reason = Objects.requireNonNull(otherReason.getText()).toString();
                                    if (!selected_reason.isEmpty()) {
                                        errorReason.setVisibility(View.GONE);
                                        if (!selected_worker.isEmpty() && !selected_worker_id.isEmpty()) {
                                            errorBackup.setVisibility(View.GONE);

                                            AlertDialog.Builder builder = new AlertDialog.Builder(NewLeaveApplication.this);
                                            builder.setTitle("Leave Application!")
                                                    .setMessage("Do you want apply this leave?")
                                                    .setPositiveButton("YES", (dialog, which) -> insertLeaveApply())
                                                    .setNegativeButton("NO", (dialog, which) -> {

                                                    });
                                            AlertDialog alert = builder.create();
                                            alert.show();
                                        }
                                        else {
                                            errorBackup.setVisibility(View.VISIBLE);
                                            Toast.makeText(getApplicationContext(),"Please Select Work Backup Employee",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else {
                                        errorReason.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(),"Please Select Reason",Toast.LENGTH_SHORT).show();
                                    }
                                } 
                                else {
                                    errorReason.setVisibility(View.GONE);
                                    if (!selected_worker.isEmpty() && !selected_worker_id.isEmpty()) {
                                        errorBackup.setVisibility(View.GONE);

                                        AlertDialog.Builder builder = new AlertDialog.Builder(NewLeaveApplication.this);
                                        builder.setTitle("Leave Application!")
                                                .setMessage("Do you want apply this leave?")
                                                .setPositiveButton("YES", (dialog, which) -> insertLeaveApply())
                                                .setNegativeButton("NO", (dialog, which) -> {

                                                });
                                        AlertDialog alert = builder.create();
                                        alert.show();

                                    } 
                                    else {
                                        errorBackup.setVisibility(View.VISIBLE);
                                        Toast.makeText(getApplicationContext(),"Please Select Work Backup Employee",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } 
                            else {
                                errorReason.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),"Please Select Reason",Toast.LENGTH_SHORT).show();
                            }
                        } 
                        else {
                            errorLeaveDuration.setVisibility(View.VISIBLE);
                            Toast.makeText(getApplicationContext(),"Please Select Leave Duration",Toast.LENGTH_SHORT).show();
                        }
                    } 
                    else {
                        dateToLay.setHelperText("Please Select Date To");
                        Toast.makeText(getApplicationContext(),"Please Select Date To",Toast.LENGTH_SHORT).show();
                    }
                } 
                else {
                    dateOnLay.setHelperText("Please Select Date On/ From");
                    Toast.makeText(getApplicationContext(),"Please Select Date On/ From",Toast.LENGTH_SHORT).show();
                }
            } 
            else {
                leaveTypeLay.setHelperText("Please Select Leave Type");
                Toast.makeText(getApplicationContext(),"Please Select Leave Type",Toast.LENGTH_SHORT).show();
            }
        });

        getInfo();
    }

    @Override
    public void onBackPressed() {
        showD = 0;
        isOtherReason = false;
        leaveNumber = 0;
        dialogText_leave = 0;

        hint = "";
        text = "";

        selected_leave_type_name = "";
        selected_leave_type_id = "";
        selected_reason = "";

        selected_worker = "";
        selected_worker_id = "";

        finish();
    }

    public void getInfo() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        leaveTypeLists = new ArrayList<>();
        allreasonLists = new ArrayList<>();
        selectingIndivdual = new ArrayList<>();
        workBackupList = new ArrayList<>();

        String leaveTypeUrl = api_url_front+"leave/getLeaveType/"+emp_id+"";
        String reasonsUrl = api_url_front+"leave/getReasons";
        String workBackupUrl = api_url_front+"leave/getWorkBackupEmp/"+div_id+"/"+emp_id+"";
        String empInfoUrl = api_url_front+"attendanceUpNewReq/getEmpInfo/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(NewLeaveApplication.this);

        StringRequest empInfoReq = new StringRequest(Request.Method.GET, empInfoUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empSomeInfo = array.getJSONObject(i);

                        selected_jsm_id = empSomeInfo.getString("jsm_id")
                                .equals("null") ? "" : empSomeInfo.getString("jsm_id");
                        calling_title = empSomeInfo.getString("job_calling_title")
                                .equals("null") ? "" : empSomeInfo.getString("job_calling_title");
                        selected_dept_id = empSomeInfo.getString("dept_id")
                                .equals("null") ? "" : empSomeInfo.getString("dept_id");
                        selected_divm_id = empSomeInfo.getString("divm_id")
                                .equals("null") ? "" : empSomeInfo.getString("divm_id");
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

        StringRequest workBackupReq = new StringRequest(Request.Method.GET, workBackupUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject workBackupInfo = array.getJSONObject(i);

                        String emp_id_new = workBackupInfo.getString("emp_id")
                                .equals("null") ? "" : workBackupInfo.getString("emp_id");
                        String emp_name_new = workBackupInfo.getString("emp_name")
                                .equals("null") ? "" : workBackupInfo.getString("emp_name");
                        String job_calling_title_new = workBackupInfo.getString("job_calling_title")
                                .equals("null") ? "" : workBackupInfo.getString("job_calling_title");
                        String jsm_name_new = workBackupInfo.getString("jsm_name")
                                .equals("null") ? "" : workBackupInfo.getString("jsm_name");
                        String divm_name_new = workBackupInfo.getString("divm_name")
                                .equals("null") ? "" : workBackupInfo.getString("divm_name");

                        workBackupList.add(new SelectAllList(emp_id_new,emp_name_new,job_calling_title_new,
                                jsm_name_new,divm_name_new));

                    }
                }
                requestQueue.add(empInfoReq);
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

        StringRequest reasonReq = new StringRequest(Request.Method.GET, reasonsUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reasonsInfo = array.getJSONObject(i);

                        String lr_id_new = reasonsInfo.getString("lr_id")
                                .equals("null") ? "" : reasonsInfo.getString("lr_id");
                        String lr_name_name = reasonsInfo.getString("lr_name")
                                .equals("null") ? "" : reasonsInfo.getString("lr_name");

                        allreasonLists.add(new LeaveTypeList(lr_id_new,lr_name_name));
                    }
                }
                requestQueue.add(workBackupReq);
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

        StringRequest leaveTypeReq = new StringRequest(Request.Method.GET, leaveTypeUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject leaveTypeInfo = array.getJSONObject(i);

                        String lc_name_new = leaveTypeInfo.getString("lc_name")
                                .equals("null") ? "" : leaveTypeInfo.getString("lc_name");
                        String lc_id_new = leaveTypeInfo.getString("lc_id")
                                .equals("null") ? "" : leaveTypeInfo.getString("lc_id");

                        leaveTypeLists.add(new LeaveTypeList(lc_id_new,lc_name_new));
                    }
                }

                requestQueue.add(reasonReq);
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

        requestQueue.add(leaveTypeReq);
    }

    private void updateInterface() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                allreasonLists.add(new LeaveTypeList("9999", "Others"));
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(NewLeaveApplication.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getInfo();
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
            AlertDialog dialog = new AlertDialog.Builder(NewLeaveApplication.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getInfo();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    public void insertLeaveApply() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        insertCon = false;
        insertConnn = false;
        insertedCon = false;

        leaveAppCheck = "";

        String url = api_url_front+"leave/applyLeave";

        RequestQueue requestQueue = Volley.newRequestQueue(NewLeaveApplication.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            insertConnn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    insertCon = true;
                    insertedCon = updated_req.equals("true");
                    leaveAppCheck = updated_req;
                }
                else {
                    System.out.println(string_out);
                    insertCon = false;
                }
                applyLeaveLayUpdate();
            }
            catch (JSONException e) {
                e.printStackTrace();
                insertCon = false;
                applyLeaveLayUpdate();
            }
        }, error -> {
            error.printStackTrace();
            insertConnn = false;
            insertCon = false;
            applyLeaveLayUpdate();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_LEAVE_TYPE",selected_leave_type_id);
                headers.put("P_SELECTED_DATE_FROM",selected_date_on_from);
                headers.put("P_SELECTED_DATE_TO",selected_date_to);
                headers.put("P_NOW_DATE",date_of_today);
                headers.put("P_LEAVE_ADDRESS",selected_leave_address);
                headers.put("P_TOTAL_LEAVE_DAYS",selected_total_leave_days);
                headers.put("P_DEP_ID",selected_dept_id);
                headers.put("P_APPLICATION_TYPE",selected_application_type);
                headers.put("P_CALLING_TITLE",calling_title);
                headers.put("P_DIV_ID",selected_divm_id);
                headers.put("P_USER_ID",user_id);
                headers.put("P_JSM_ID",selected_jsm_id);
                headers.put("P_SELECTED_REASON",selected_reason);
                headers.put("P_SELECTED_WORKER",selected_worker_id);
                headers.put("P_SELECTED_LEAVE_DURATION",selected_leave_duration_id);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void applyLeaveLayUpdate() {
        waitProgress.dismiss();
        if (insertConnn) {
            if (insertCon) {
                if (insertedCon) {
                    showD = 0;
                    isOtherReason = false;
                    leaveNumber = 0;
                    dialogText_leave = 0;

                    hint = "";
                    text = "";

                    selected_leave_type_name = "";
                    selected_leave_type_id = "";
                    selected_reason = "";

                    selected_worker = "";
                    selected_worker_id = "";

                    System.out.println("INSERTED");

                    AlertDialog dialog = new AlertDialog.Builder(NewLeaveApplication.this)
                            .setMessage("Applied Successfully")
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
                    AlertDialog dialog = new AlertDialog.Builder(NewLeaveApplication.this)
                            .setMessage(leaveAppCheck)
                            .setPositiveButton("OK", null)
                            .show();

                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                    positive.setOnClickListener(v -> dialog.dismiss());
                }

                insertedCon = false;
                insertCon = false;
                insertConnn = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(NewLeaveApplication.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    insertLeaveApply();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(NewLeaveApplication.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                insertLeaveApply();
                dialog.dismiss();
            });
        }
    }
}