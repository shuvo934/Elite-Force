package ttit.com.shuvo.eliteforce.attendance.att_update.new_request;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
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
import java.util.logging.Level;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_update.new_request.dialogue_box.ShowAttendance;
import ttit.com.shuvo.eliteforce.attendance.att_update.new_request.dialogue_box.ShowShift;
import ttit.com.shuvo.eliteforce.attendance.att_update.new_request.model.AttendanceReqType;
import ttit.com.shuvo.eliteforce.attendance.att_update.new_request.model.LocUpdateList;
import ttit.com.shuvo.eliteforce.attendance.att_update.new_request.model.ReasonList;
import ttit.com.shuvo.eliteforce.basic_model.SelectAllList;
import ttit.com.shuvo.eliteforce.dialogue_box.DialogueText;
import ttit.com.shuvo.eliteforce.dialogue_box.SelectAll;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class AttUpNewRequest extends AppCompatActivity {

    public static int dialogText = 0;
//    public static int showAttendanceNumber = 0;
//    public static int showShiftNumber = 0;

    TextView errorTime;
//    TextView errorLocation;
//    public static TextView errorShift;
    TextView errorReason;
    public static TextView errorApprover;
    public static TextView errorReasonDesc;

//    LinearLayout after;
    TextInputLayout dateUpdateLay;
    public static TextInputLayout reasonLay;
    public static TextInputLayout addStaLay;

    TextInputLayout inTimeLay;
    TextInputLayout outTimeLay;

//    TextInputLayout shiftTestLay;
//    public static TextInputEditText shiftTestEdit;

    TextInputLayout approverTestLay;
    public static TextInputEditText approverTestEdit;

//    TextInputEditText machCode;
//    TextInputEditText machType;
    TextInputEditText userName;
    TextInputEditText userID;
    TextInputEditText todayDate;
    TextInputEditText dateUpdated;
    TextInputEditText arrivalTimeUpdated;
    TextInputEditText departTimeUpdated;
    public static TextInputEditText reasonDesc;
    public static TextInputEditText addressStation;

//    Spinner locUpdate;
//    Spinner reqType;
//    Spinner attenType;
    Spinner reasonType;

//    Button existingAtt;
//    public static Button showShoftTime;
    Button update;
    LinearLayout updateButtonEnable;

    MaterialButton arriClear;
    MaterialButton deptClear;

    public static ArrayList<SelectAllList> selectAllLists;
//    ArrayList<SelectAllList> allShiftDetails;

//    ArrayList<SelectAllList> allApproverDivision;
//    ArrayList<SelectAllList> allApproverWithoutDiv;
//    ArrayList<SelectAllList> allApproverEmp;
    ArrayList<SelectAllList> allSelectedApprover;

//    ArrayList<LocUpdateList> locUpdateLists;
//    ArrayList<String> onlyLocationLists;

//    ArrayList<String> reqList;

//    ArrayList<AttendanceReqType> attendanceReqTypes;
//    ArrayList<String> attenTypeList;

    ArrayList<ReasonList> reasonLists;
    ArrayList<String> reasonName;


//    ArrayAdapter<String> locUpdateAdapter;
//    ArrayAdapter<String> reqTypeAdapter;
    ArrayAdapter<String> attenTypeAdapter;
    ArrayAdapter<String> reasonAdapter;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean machineCon = false;
    private Boolean machineConnn = false;
    private Boolean connected = false;
//    private Boolean reasonCon = false;
//    private Boolean reasonConnnn = false;
    private Boolean insertCon = false;
    private Boolean insertConnn = false;
    private Boolean isInserted = false;

    private int mYear, mMonth, mDay, mHour, mMinute;

    String emp_id = "";
    String emp_name = "";
    String user_id = "";

//    String updatedLocationName = "";
//    String selected_loc_id = "";

//    String machineCode = "";
//    String machineType = "";

//    public static String dateToShow = "";

    String selected_request = "";
    String selected_attendance_type= "General";
//    public static String selected_shift_name = "";
//    public static String selected_shift_id = "";
//    public static String shift_osm_id = "";
    String selected_reason_name = "";
    public static String selected_approver_name = "";

    public static String hint = "";
    public static int number = 0;
    public static String text = "";

//    String desig_priority = "";
//    String divm_id = "";
//    String approval_band = "";
//    int count_approv_emp = 0;

    String selected_update_date = "";
    String selected_reason_desc = "";
    String selected_address_station = "";
    String now_date = "";
    String selected_reason_id = "";
    public static String selected_approver_id = "";
    String arrival_time = "";
    String depart_time = "";

    String selected_jsm_id = "";
    String selected_dept_id = "";
    String selected_divm_id = "";
    String calling_title = "";

    LinearLayout afterSelectingDate;
    TextInputEditText intime;
    TextInputEditText outtime;
    TextInputEditText machArrTime;
    TextInputEditText machDepTime;
    TextInputEditText exShNa;
    TextInputEditText exLocNa;

    String shiftin = "";
    String shiftout = "";
    String machineArr = "";
    String machiDep = "";
    String shiftName = "";
    String locName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_att_up_new_request);

//        shiftTestLay = findViewById(R.id.shift_description_Layout);
//        shiftTestEdit = findViewById(R.id.shift_description);

        approverTestLay = findViewById(R.id.approver_description_Layout);
        approverTestEdit = findViewById(R.id.approver_description);

        inTimeLay = findViewById(R.id.select_in_time_from);
        outTimeLay = findViewById(R.id.select_out_time_from);

        errorTime = findViewById(R.id.error_input_time);
//        errorLocation = findViewById(R.id.error_input_location);
//        errorShift = findViewById(R.id.error_input_shift);
        errorReason = findViewById(R.id.error_input_reason);
        errorApprover = findViewById(R.id.error_input_approver);
        errorReasonDesc = findViewById(R.id.error_input_reason_desc);

//        existingAtt = findViewById(R.id.existing_att_time);
//        showShoftTime = findViewById(R.id.show_shift_item_button);
        update = findViewById(R.id.update_req_button);
        updateButtonEnable = findViewById(R.id.linearLayout_new_request_att_send);
        arriClear = findViewById(R.id.arrival_clear);
        deptClear = findViewById(R.id.departure_clear);

//        after = findViewById(R.id.afterselecting);
        dateUpdateLay = findViewById(R.id.date_updated_layout);
        addStaLay = findViewById(R.id.address_station_layout);
        reasonLay = findViewById(R.id.reason_description_Layout);

//        machCode = findViewById(R.id.updated_machine_code);
//        machType = findViewById(R.id.updated_machine_type);
        userName = findViewById(R.id.name_att_update);
        userID = findViewById(R.id.id_att_update);
        todayDate = findViewById(R.id.now_date_att_update);
        dateUpdated = findViewById(R.id.date_to_be_updated);
        arrivalTimeUpdated = findViewById(R.id.arrival_time_to_be_updated);
        departTimeUpdated = findViewById(R.id.departure_time_to_be_updated);
        addressStation = findViewById(R.id.address_outside_sta);
        reasonDesc = findViewById(R.id.reason_description);

//        locUpdate = findViewById(R.id.spinner_loc_updated);
//        reqType = findViewById(R.id.spinner_req_type);
//        attenType = findViewById(R.id.spinner_attendance_type);
        reasonType = findViewById(R.id.spinner_reason_type_updated);

        afterSelectingDate = findViewById(R.id.after_selecting_date_to_be_updated_att_req);
        afterSelectingDate.setVisibility(View.GONE);
        intime = findViewById(R.id.shift_in_time_att_req);
        outtime = findViewById(R.id.shift_out_time_att_req);
        machArrTime = findViewById(R.id.machine_int_time_att_req);
        machDepTime = findViewById(R.id.machine_out_time_att_req);
        exShNa = findViewById(R.id.existing_shift_name_att_req);
        exLocNa = findViewById(R.id.existing_loc_name_att_req);

//        selected_shift_id = "";
        selected_approver_id = "";

        selectAllLists = new ArrayList<>();

//        allShiftDetails = new ArrayList<>();
//        allApproverDivision = new ArrayList<>();
//        allApproverWithoutDiv = new ArrayList<>();
//        allApproverEmp = new ArrayList<>();
        allSelectedApprover = new ArrayList<>();

//        locUpdateLists = new ArrayList<>();

//        attendanceReqTypes = new ArrayList<>();

        reasonLists = new ArrayList<>();

//        onlyLocationLists = new ArrayList<>();
//        reqList = new ArrayList<>();
//        attenTypeList = new ArrayList<>();
        reasonName = new ArrayList<>();

        reasonName.add("Select");

//        onlyLocationLists.add("Select");

//        reqList.add("Select");
//        reqList.add("PRE");
//        reqList.add("POST");

//        attendanceReqTypes.add(new AttendanceReqType("Early","Early Departure Information"));
//        attendanceReqTypes.add(new AttendanceReqType("Late","Late Arrival Information"));
//        attendanceReqTypes.add(new AttendanceReqType("General","Work Time Update"));

//        attenTypeList.add("Select");

//        for (int i = 0 ; i < attendanceReqTypes.size(); i++) {
//            attenTypeList.add(attendanceReqTypes.get(i).getAttendance_req_details());
//        }

        emp_id = userInfoLists.get(0).getEmp_id();

        emp_name = userInfoLists.get(0).getUser_fname() + " " + userInfoLists.get(0).getUser_lname();

        user_id = userInfoLists.get(0).getUserName();

        userName.setText(emp_name);
        userID.setText(user_id);

        Date c = Calendar.getInstance().getTime();

        String formattedDate = "";

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        formattedDate = df.format(c);

        todayDate.setText(formattedDate);

        // Location Update Spinner
//        locUpdateAdapter = new ArrayAdapter<String>(
//                this,R.layout.item_country,onlyLocationLists){
//            @Override
//            public boolean isEnabled(int position){
//                // Disable the first item from Spinner
//                // First item will be use for hint
//                return position != 0;
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        @NonNull ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = view.findViewById(R.id.tvCountry);
//                if(position == 0){
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//        locUpdate.setGravity(Gravity.END);
//        locUpdateAdapter.setDropDownViewResource(R.layout.item_country);
//        locUpdate.setAdapter(locUpdateAdapter);

        // Selecting Location From Spinner
//        locUpdate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if(position > 0){
//                    updatedLocationName = (String) parent.getItemAtPosition(position);
//                    errorLocation.setVisibility(View.GONE);
//
//                    // Notify the selected item text
//                    for (int i = 0; i < locUpdateLists.size(); i++) {
//                        if (updatedLocationName.equals(locUpdateLists.get(i).getLocation())) {
//                            selected_loc_id = locUpdateLists.get(i).getLocID();
//
//                            System.out.println(selected_loc_id);
//                        }
//                    }
//                    getMachineData();
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        // Request Type Spinner
//        reqTypeAdapter = new ArrayAdapter<String>(
//                this,R.layout.item_country,reqList){
//            @Override
//            public boolean isEnabled(int position){
//                // Disable the first item from Spinner
//                // First item will be use for hint
//                return position != 0;
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        @NonNull ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = view.findViewById(R.id.tvCountry);
//                if(position == 0){
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//        reqType.setGravity(Gravity.END);
//        reqTypeAdapter.setDropDownViewResource(R.layout.item_country);
//        reqType.setAdapter(reqTypeAdapter);

        // Selecting Request Type
//        reqType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if(position > 0){
//                    selected_request = (String) parent.getItemAtPosition(position);
//                    dateUpdated.setText("");
//                    dateUpdateLay.setHint("Select Update Date");
//                    dateUpdateLay.setHelperText("");
//                    existingAtt.setVisibility(View.GONE);
//
//                    // Notify the selected item text
//                    System.out.println(selected_request);
//                    if (!selected_request.isEmpty() && !selected_attendance_type.isEmpty()) {
//
//                        System.out.println(1);
//                        after.setVisibility(View.VISIBLE);
//                        updateButtonEnable.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        // Attendance Type Spinner
//        attenTypeAdapter = new ArrayAdapter<String>(
//                this,R.layout.item_country,attenTypeList){
//            @Override
//            public boolean isEnabled(int position){
//                // Disable the first item from Spinner
//                // First item will be use for hint
//                return position != 0;
//            }
//            @Override
//            public View getDropDownView(int position, View convertView,
//                                        @NonNull ViewGroup parent) {
//                View view = super.getDropDownView(position, convertView, parent);
//                TextView tv = view.findViewById(R.id.tvCountry);
//                if(position == 0){
//                    // Set the hint text color gray
//                    tv.setTextColor(Color.GRAY);
//                }
//                else {
//                    tv.setTextColor(Color.BLACK);
//                }
//                return view;
//            }
//        };
//        attenType.setGravity(Gravity.END);
//        attenTypeAdapter.setDropDownViewResource(R.layout.item_country);
//        attenType.setAdapter(attenTypeAdapter);
//
//        // Selecting Attendance Type
//        attenType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                if(position > 0){
//                    selected_attendance_type = (String) parent.getItemAtPosition(position);
//
//                    for (int i = 0; i < attendanceReqTypes.size(); i++) {
//                        if (selected_attendance_type.equals(attendanceReqTypes.get(i).getAttendance_req_details())) {
//                            selected_attendance_type = attendanceReqTypes.get(i).getAttendance_req();
//
//                            System.out.println(selected_attendance_type);
//                        }
//                    }
//
//                    getReasonData();
//                    // Notify the selected item text
//                    if (!selected_attendance_type.isEmpty()) {
//                        System.out.println(2);
//                        after.setVisibility(View.VISIBLE);
//                        updateButtonEnable.setVisibility(View.VISIBLE);
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

        // Reason Type Spinner
        reasonAdapter = new ArrayAdapter<String>(
                this,R.layout.item_country,reasonName){
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
                TextView tv = view.findViewById(R.id.tvCountry);
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
        reasonType.setGravity(Gravity.END);
        reasonAdapter.setDropDownViewResource(R.layout.item_country);
        reasonType.setAdapter(reasonAdapter);

        // Selecting Reason Type
        reasonType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position > 0){
                    selected_reason_name = (String) parent.getItemAtPosition(position);
                    errorReason.setVisibility(View.GONE);

                    for (int i = 0; i < reasonLists.size(); i++) {
                        if (selected_reason_name.equals(reasonLists.get(i).getReason_name())) {
                            selected_reason_id = reasonLists.get(i).getReason_id();

                            System.out.println(selected_reason_id);
                        }
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // Date to be Updated
        dateUpdated.setOnClickListener(v -> {
            final Calendar c1 = Calendar.getInstance();
            Date ldate = null;
            try {
                ldate = df.parse(selected_update_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (ldate != null) {
                c1.setTime(ldate);
            }
            mYear = c1.get(Calendar.YEAR);
            mMonth = c1.get(Calendar.MONTH);
            mDay = c1.get(Calendar.DAY_OF_MONTH);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AttUpNewRequest.this, (view, year, month, dayOfMonth) -> {

                    String monthName = "";
                    String dayOfMonthName;
                    String yearName;
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
                    String tt = dayOfMonthName + "-" + monthName + "-" + yearName;
                    dateUpdated.setText(tt);
                    selected_update_date = tt;

                    String today = Objects.requireNonNull(todayDate.getText()).toString();

                    Date nowDate = null;
                    Date givenDate = null;

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
                    try {
                        nowDate = sdf.parse(today);
                        givenDate = sdf.parse(selected_update_date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if (nowDate != null && givenDate != null) {
                        if (givenDate.equals(nowDate)) {
                            selected_request = "PRE";
                            dateUpdateLay.setHelperText("");
                            dateUpdateLay.setHint("Update Date");
                            getAttendanceShow();
                        }
                        else if (givenDate.before(nowDate)) {
                            selected_request = "POST";
                            dateUpdateLay.setHelperText("");
                            dateUpdateLay.setHint("Update Date");
                            getAttendanceShow();
                        }
                        else {
                            dateUpdateLay.setHelperText("Date Not Found");
                            dateUpdateLay.setHint("Select Update Date");
                            dateUpdateLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
                            dateUpdated.setText("");
                            afterSelectingDate.setVisibility(View.GONE);
                        }
                    }

//                    if (selected_request.equals("PRE")) {
//
//                        String today = Objects.requireNonNull(todayDate.getText()).toString();
//                        String updateDate = Objects.requireNonNull(dateUpdated.getText()).toString();
//
//                        Date nowDate = null;
//                        Date givenDate = null;
//
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
//                        try {
//                            nowDate = sdf.parse(today);
//                            givenDate = sdf.parse(updateDate);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        if (nowDate != null && givenDate != null) {
//
//                            if (givenDate.after(nowDate) || givenDate.equals(nowDate)) {
//                                String text = dayOfMonthName + "-" + monthName + "-" + yearName;
//                                dateUpdated.setText(text);
//                                dateUpdateLay.setHelperText("");
//                                dateUpdateLay.setHint("Update Date");
//                                existingAtt.setVisibility(View.VISIBLE);
//                                System.out.println("AHSE let");
//                            }
//                            else {
//                                dateUpdateLay.setHelperText("Requested Date never Less than Application Date");
//                                dateUpdateLay.setHint("Select Update Date");
//                                dateUpdateLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
//                                dateUpdated.setText("");
//                                existingAtt.setVisibility(View.GONE);
//                            }
//                        }
//                    }
//                    else if (selected_request.equals("POST")) {
//
//                        String today = Objects.requireNonNull(todayDate.getText()).toString();
//                        String updateDate = Objects.requireNonNull(dateUpdated.getText()).toString();
//
//                        Date nowDate = null;
//                        Date givenDate = null;
//
//                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
//                        try {
//                            nowDate = sdf.parse(today);
//                            givenDate = sdf.parse(updateDate);
//                        } catch (ParseException e) {
//                            e.printStackTrace();
//                        }
//
//                        if (nowDate != null && givenDate != null) {
//
//                            if (givenDate.before(nowDate)) {
//                                String text = dayOfMonthName + "-" + monthName + "-" + yearName;
//                                dateUpdated.setText(text);
//                                dateUpdateLay.setHelperText("");
//                                dateUpdateLay.setHint("Update Date");
//                                existingAtt.setVisibility(View.VISIBLE);
//                                System.out.println("AHSE POST");
//                            }
//                            else {
//                                dateUpdateLay.setHelperText("Requested Date should be Less than Application Date");
//                                dateUpdateLay.setHint("Select Update Date");
//                                dateUpdateLay.setHelperTextColor(ColorStateList.valueOf(Color.RED));
//                                dateUpdated.setText("");
//                                existingAtt.setVisibility(View.GONE);
//                            }
//                        }
//                    }
                }, mYear, mMonth, mDay);
                Calendar calendar = Calendar.getInstance();
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTime().getTime());
                datePickerDialog.show();
            }
        });

        // Time to be Updated - Arrival
        arrivalTimeUpdated.setOnClickListener(v -> {
            final Calendar c12 = Calendar.getInstance();
            mHour = c12.get(Calendar.HOUR_OF_DAY);
            mMinute = c12.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(AttUpNewRequest.this, (view, hourOfDay, minute) -> {

                String AM_PM ;
                if(hourOfDay < 12) {
                    AM_PM = "AM";
                    if (hourOfDay == 0) {
                        hourOfDay = 12;
                    }
                } else {
                    AM_PM = "PM";
                    if (hourOfDay > 12 ) {
                        hourOfDay = hourOfDay - 12;
                    }
                }
                String tt = String.valueOf(minute);
                if (tt.length() == 1) {
                    tt = "0"+tt;
                    String text = hourOfDay + ":" + tt + ":00 " + AM_PM;
                    arrivalTimeUpdated.setText(text);
                } else {
                    String text = hourOfDay + ":" + minute + ":00 " + AM_PM;
                    arrivalTimeUpdated.setText(text);
                }
                inTimeLay.setHint("In Time");
                arriClear.setVisibility(View.VISIBLE);
                errorTime.setVisibility(View.GONE);
            },mHour, mMinute,false);
            timePickerDialog.show();
        });

        // Time to be updated - Departure
        departTimeUpdated.setOnClickListener(v -> {
            final Calendar c13 = Calendar.getInstance();
            mHour = c13.get(Calendar.HOUR_OF_DAY);
            mMinute = c13.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(AttUpNewRequest.this, (view, hourOfDay, minute) -> {

                String AM_PM ;
                if(hourOfDay < 12) {
                    AM_PM = "AM";
                    if (hourOfDay == 0) {
                        hourOfDay = 12;
                    }
                } else {
                    AM_PM = "PM";
                    if (hourOfDay > 12 ) {
                        hourOfDay = hourOfDay - 12;
                    }
                }
                String tt = String.valueOf(minute);
                if (tt.length() == 1) {
                    tt = "0"+tt;
                    String text = hourOfDay + ":" + tt + ":00 " + AM_PM;
                    departTimeUpdated.setText(text);
                }
                else {
                    String text = hourOfDay + ":" + minute + ":00 " + AM_PM;
                    departTimeUpdated.setText(text);
                }
                outTimeLay.setHint("Out Time");
                deptClear.setVisibility(View.VISIBLE);
                errorTime.setVisibility(View.GONE);
            },mHour, mMinute,false);
            timePickerDialog.show();
        });

        arriClear.setOnClickListener(v -> {
            arrivalTimeUpdated.setText("");
            inTimeLay.setHint("Select In Time");
            arriClear.setVisibility(View.GONE);
        });

        deptClear.setOnClickListener(v -> {
            departTimeUpdated.setText("");
            outTimeLay.setHint("Select Out Time");
            deptClear.setVisibility(View.GONE);
        });

        // Reason Description
        reasonDesc.setOnClickListener(v -> {
            number = 1;
            hint = Objects.requireNonNull(reasonLay.getHint()).toString();
            text = Objects.requireNonNull(reasonDesc.getText()).toString();
            DialogueText dialogueText = new DialogueText();
            dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
        });

        // Address out of Station
        addressStation.setOnClickListener(v -> {
            number = 2;
            hint = Objects.requireNonNull(addStaLay.getHint()).toString();
            text = Objects.requireNonNull(addressStation.getText()).toString();
            DialogueText dialogueText = new DialogueText();
            dialogueText.show(getSupportFragmentManager(),"TEXTEDIT");
        });

//        shiftTestEdit.setOnClickListener(v -> {
//            dialogText = 1;
//            selectAllLists = allShiftDetails;
//            SelectAll selectAll = new SelectAll();
//            selectAll.show(getSupportFragmentManager(),"TEXTEDIT");
//        });

        approverTestEdit.setOnClickListener(v -> {
            dialogText = 2;
            selectAllLists = allSelectedApprover;
            SelectAll selectAll = new SelectAll();
            selectAll.show(getSupportFragmentManager(),"TEXTEDIT");
        });

//        existingAtt.setOnClickListener(v -> {
//            showAttendanceNumber = 1;
//            dateToShow = Objects.requireNonNull(dateUpdated.getText()).toString();
//            if (!dateToShow.isEmpty()) {
//                ShowAttendance showAttendance = new ShowAttendance(AttUpNewRequest.this);
//                showAttendance.show(getSupportFragmentManager(),"Attendance");
//            }
//        });

//        showShoftTime.setOnClickListener(v -> {
//            showShiftNumber = 1;
//            shift_osm_id = selected_shift_id;
//            ShowShift showShift = new ShowShift(AttUpNewRequest.this);
//            showShift.show(getSupportFragmentManager(),"Shift");
//        });

        update.setOnClickListener(v -> {

            now_date = Objects.requireNonNull(todayDate.getText()).toString();
            selected_update_date = Objects.requireNonNull(dateUpdated.getText()).toString();
            arrival_time = Objects.requireNonNull(arrivalTimeUpdated.getText()).toString();
            depart_time = Objects.requireNonNull(departTimeUpdated.getText()).toString();
            selected_reason_desc = Objects.requireNonNull(reasonDesc.getText()).toString();
            selected_address_station = Objects.requireNonNull(addressStation.getText()).toString();
            
            if (!selected_update_date.isEmpty()) {
                if (arrival_time.isEmpty() && depart_time.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Check Time to be Updated",Toast.LENGTH_SHORT).show();
                    errorTime.setVisibility(View.VISIBLE);
                }
                else {
                    errorTime.setVisibility(View.GONE);
                    if (!selected_reason_id.isEmpty()) {
                        errorReason.setVisibility(View.GONE);

                        if (!selected_reason_desc.isEmpty()) {
                            errorReasonDesc.setVisibility(View.GONE);

                            if (!allSelectedApprover.isEmpty()) {
                                if (!selected_approver_id.isEmpty()) {
                                    errorApprover.setVisibility(View.GONE);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(AttUpNewRequest.this);
                                    builder.setTitle("Attendance Update Request!")
                                            .setMessage("Do you want send this request?")
                                            .setPositiveButton("YES", (dialog, which) -> insertReq())
                                            .setNegativeButton("NO", (dialog, which) -> {

                                            });
                                    AlertDialog alert = builder.create();
                                    alert.show();

                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Please Check Approver Name",Toast.LENGTH_SHORT).show();
                                    errorApprover.setVisibility(View.VISIBLE);
                                }
                            }
                            else {
                                errorApprover.setVisibility(View.GONE);

                                AlertDialog.Builder builder = new AlertDialog.Builder(AttUpNewRequest.this);
                                builder.setTitle("Attendance Update Request!")
                                        .setMessage("Do you want send this request?")
                                        .setPositiveButton("YES", (dialog, which) -> insertReq())
                                        .setNegativeButton("NO", (dialog, which) -> {

                                        });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please Check Reason Description",Toast.LENGTH_SHORT).show();
                            errorReasonDesc.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Please Check Reason Type",Toast.LENGTH_SHORT).show();
                        errorReason.setVisibility(View.VISIBLE);
                    }
                }

//                    if (!selected_loc_id.isEmpty()) {
//                        errorLocation.setVisibility(View.GONE);
//
//                        if (!selected_shift_id.isEmpty()) {
//                            System.out.println(selected_shift_id);
//                            errorShift.setVisibility(View.GONE);
//
//
//                        }
//                        else {
//                            Toast.makeText(getApplicationContext(),"Please Check Shift to be Updated",Toast.LENGTH_SHORT).show();
//                            errorShift.setVisibility(View.VISIBLE);
//                        }
//                    }
//                    else {
//                        Toast.makeText(getApplicationContext(),"Please Check Location to be Updated",Toast.LENGTH_SHORT).show();
//                        errorLocation.setVisibility(View.VISIBLE);
//                    }
            }
            else {
                Toast.makeText(getApplicationContext(),"Please Check Date to be Updated",Toast.LENGTH_SHORT).show();
                dateUpdateLay.setHelperText("Please Provide 'Date to be Updated'");
            }
        });

        getDataInfo();
    }

    @Override
    public void onBackPressed() {
//        selected_shift_id = "";
        selected_approver_id = "";
        finish();
    }

    //--------------------Getting Data------------------------
    public void getDataInfo() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

//        locUpdateLists = new ArrayList<>();
//        allShiftDetails = new ArrayList<>();

//        allApproverDivision = new ArrayList<>();
//        allApproverWithoutDiv = new ArrayList<>();
//        allApproverEmp = new ArrayList<>();
        allSelectedApprover = new ArrayList<>();

        reasonLists = new ArrayList<>();

        String url = api_url_front+"attendanceUpNewReq/getReasonData/"+selected_attendance_type+"";
//        String companyOfficeUrl = api_url_front+"attendanceUpNewReq/getCompanyOffice";
//        String shiftsUrl = api_url_front+"attendanceUpNewReq/getShiftLists";
        String empInfoUrl = api_url_front+"attendanceUpNewReq/getEmpInfo/"+emp_id+"";
//        String forwardListsWithDivUrl = api_url_front+"attendanceUpNewReq/getForward_ApproverListWithDiv/"+emp_id+"";
//        String forwardWithoutDivUrl = api_url_front+"attendanceUpNewReq/getFor_AppListWIthoutDiv/"+emp_id+"";
//        String forwardAllUrl = api_url_front+"attendanceUpNewReq/getAllFor_AppList/"+emp_id+"";
//        String desigPriorUrl = api_url_front+"forwardReq/getDesigPriority/"+emp_id+"";
        String approverUrl = api_url_front+"attendanceUpNewReq/getAttApprover?p_emp_id="+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(AttUpNewRequest.this);

//        StringRequest desigPriorReq = new StringRequest(Request.Method.GET, desigPriorUrl, response -> {
//            conn = true;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject desigPrInfo = array.getJSONObject(i);
//                        desig_priority  = desigPrInfo.getString("desig_priority")
//                                .equals("null") ? "" : desigPrInfo.getString("desig_priority");
//                        divm_id = desigPrInfo.getString("jsm_divm_id")
//                                .equals("null") ? "" : desigPrInfo.getString("jsm_divm_id");
//
//                        System.out.println("designation1: " + desig_priority);
//                    }
//                }
//
//                if (!desig_priority.isEmpty()) {
//                    System.out.println("designation2: " + desig_priority);
//                    getForwarderApproverList();
//                }
//                else {
//                    connected = true;
//                    updateLay();
//                }
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//                connected = false;
//                updateLay();
//            }
//        }, error -> {
//            error.printStackTrace();
//            conn = false;
//            connected = false;
//            updateLay();
//        });

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reasonInfo = array.getJSONObject(i);

                        String aurrm_id = reasonInfo.getString("aurrm_id")
                                .equals("null") ? "" : reasonInfo.getString("aurrm_id");
                        String aurrm_name = reasonInfo.getString("aurrm_name")
                                .equals("null") ? "" : reasonInfo.getString("aurrm_name");

                        reasonLists.add(new ReasonList(aurrm_id,aurrm_name));
                    }
                }
                connected = true;
                updateLay();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

        StringRequest forwardAllReq = new StringRequest(Request.Method.GET, approverUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject forwardInfo = array.getJSONObject(i);

                        String emp_id_new = forwardInfo.getString("emp_id")
                                .equals("null") ? "" : forwardInfo.getString("emp_id");
                        String emp_name_new  = forwardInfo.getString("emp_name")
                                .equals("null") ? "" : forwardInfo.getString("emp_name");
                        String job_calling_title_new = forwardInfo.getString("job_calling_title")
                                .equals("null") ? "" : forwardInfo.getString("job_calling_title");
                        String jsm_name_new = forwardInfo.getString("jsm_name")
                                .equals("null") ? "" : forwardInfo.getString("jsm_name");
                        String divm_name_new = forwardInfo.getString("divm_name")
                                .equals("null") ? "" : forwardInfo.getString("divm_name");

                        allSelectedApprover.add(new SelectAllList(emp_id_new,emp_name_new,job_calling_title_new,
                                jsm_name_new,divm_name_new));
                    }
                }
                connected = true;
                requestQueue.add(stringRequest);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

//        StringRequest forwardWithoutDivReq = new StringRequest(Request.Method.GET, forwardWithoutDivUrl, response -> {
//            conn = true;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject forwardInfo = array.getJSONObject(i);
//
//                        String emp_id_new = forwardInfo.getString("emp_id")
//                                .equals("null") ? "" : forwardInfo.getString("emp_id");
//                        String emp_name_new  = forwardInfo.getString("emp_name")
//                                .equals("null") ? "" : forwardInfo.getString("emp_name");
//                        String job_calling_title_new = forwardInfo.getString("job_calling_title")
//                                .equals("null") ? "" : forwardInfo.getString("job_calling_title");
//                        String jsm_name_new = forwardInfo.getString("jsm_name")
//                                .equals("null") ? "" : forwardInfo.getString("jsm_name");
//                        String divm_name_new = forwardInfo.getString("divm_name")
//                                .equals("null") ? "" : forwardInfo.getString("divm_name");
//
//                        allApproverWithoutDiv.add(new SelectAllList(emp_id_new,emp_name_new,job_calling_title_new,
//                                jsm_name_new,divm_name_new));
//                    }
//                }
//
//                requestQueue.add(forwardAllReq);
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//                connected = false;
//                updateLay();
//            }
//        }, error -> {
//            error.printStackTrace();
//            conn = false;
//            connected = false;
//            updateLay();
//        });
//
//        StringRequest forwardWithDivReq = new StringRequest(Request.Method.GET, forwardListsWithDivUrl, response -> {
//            conn = true;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject forwardInfo = array.getJSONObject(i);
//
//                        String emp_id_new = forwardInfo.getString("emp_id")
//                                .equals("null") ? "" : forwardInfo.getString("emp_id");
//                        String emp_name_new  = forwardInfo.getString("emp_name")
//                                .equals("null") ? "" : forwardInfo.getString("emp_name");
//                        String job_calling_title_new = forwardInfo.getString("job_calling_title")
//                                .equals("null") ? "" : forwardInfo.getString("job_calling_title");
//                        String jsm_name_new = forwardInfo.getString("jsm_name")
//                                .equals("null") ? "" : forwardInfo.getString("jsm_name");
//                        String divm_name_new = forwardInfo.getString("divm_name")
//                                .equals("null") ? "" : forwardInfo.getString("divm_name");
//
//
//                        allApproverDivision.add(new SelectAllList(emp_id_new,emp_name_new,job_calling_title_new,
//                                jsm_name_new,divm_name_new));
//                    }
//                }
//
//                requestQueue.add(forwardWithoutDivReq);
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//                connected = false;
//                updateLay();
//            }
//        }, error -> {
//            error.printStackTrace();
//            conn = false;
//            connected = false;
//            updateLay();
//        });

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

                requestQueue.add(forwardAllReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLay();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLay();
        });

//        StringRequest shiftReq = new StringRequest(Request.Method.GET, shiftsUrl, response -> {
//            conn = true;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject shiftInfo = array.getJSONObject(i);
//
//                        String osm_id_new = shiftInfo.getString("osm_id")
//                                .equals("null") ? "" : shiftInfo.getString("osm_id");
//                        String osm_name_name = shiftInfo.getString("osm_name")
//                                .equals("null") ? "" : shiftInfo.getString("osm_name");
//                        String start_time_new = shiftInfo.getString("start_time")
//                                .equals("null") ? "" : shiftInfo.getString("start_time");
//                        String late_after_new = shiftInfo.getString("late_after")
//                                .equals("null") ? "" : shiftInfo.getString("late_after");
//                        String end_time_new = shiftInfo.getString("end_time")
//                                .equals("null") ? "" : shiftInfo.getString("end_time");
//
//                        allShiftDetails.add(new SelectAllList(osm_id_new,osm_name_name,start_time_new,
//                                late_after_new,end_time_new));
//
//                    }
//                }
//
//                requestQueue.add(empInfoReq);
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//                connected = false;
//                updateLay();
//            }
//        }, error -> {
//            error.printStackTrace();
//            conn = false;
//            connected = false;
//            updateLay();
//        });
//
//        StringRequest comOffReq = new StringRequest(Request.Method.GET, companyOfficeUrl, response -> {
//            conn = true;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject compOffInfo = array.getJSONObject(i);
//                        String coa = compOffInfo.getString("coa_id")
//                                .equals("null") ? "" : compOffInfo.getString("coa_id");
//                        String coa_name = compOffInfo.getString("coa_name")
//                                .equals("null") ? "" : compOffInfo.getString("coa_name");
//
//                        locUpdateLists.add(new LocUpdateList(coa,coa_name));
//                    }
//                }
//                requestQueue.add(shiftReq);
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//                connected = false;
//                updateLay();
//            }
//        }, error -> {
//            error.printStackTrace();
//            conn = false;
//            connected = false;
//            updateLay();
//        });

        requestQueue.add(empInfoReq);
    }

//    public void getForwarderApproverList() {
//
//        String approvalBandUrl = api_url_front+"forwardReq/getApprovalBand/"+desig_priority+"";
//        String countApp1Url = api_url_front+"forwardReq/getCountApprovEmp/"+divm_id+"/"+desig_priority+"";
//        String countApp2Url = api_url_front+"forwardReq/getCountApprovEmp_2/"+desig_priority+"";
//
//        RequestQueue requestQueue = Volley.newRequestQueue(AttUpNewRequest.this);
//
//        StringRequest countApp2Req = new StringRequest(Request.Method.GET, countApp2Url, response -> {
//            conn = true;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject cApp2Info = array.getJSONObject(i);
//                        count_approv_emp = cApp2Info.getInt("cc2");
//                    }
//                }
//
//                if (count_approv_emp <= 0) {
//                    allSelectedApprover = allApproverWithoutDiv;
//                } else {
//                    allSelectedApprover = allApproverEmp;
//                }
//
//                connected = true;
//                updateLay();
//
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//                connected = false;
//                updateLay();
//            }
//        }, error -> {
//            error.printStackTrace();
//            conn = false;
//            connected = false;
//            updateLay();
//        });
//
//        StringRequest countApp1Req = new StringRequest(Request.Method.GET, countApp1Url, response -> {
//            conn = true;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject cApp1Info = array.getJSONObject(i);
//                        count_approv_emp = cApp1Info.getInt("cc");
//                    }
//                }
//                if (count_approv_emp <= 0) {
//                    requestQueue.add(countApp2Req);
//                }
//                else {
//                    allSelectedApprover = allApproverDivision;
//                    connected = true;
//                    updateLay();
//                }
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//                connected = false;
//                updateLay();
//            }
//        }, error -> {
//            error.printStackTrace();
//            conn = false;
//            connected = false;
//            updateLay();
//        });
//
//        StringRequest appBandReq = new StringRequest(Request.Method.GET, approvalBandUrl, response -> {
//            conn = true;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject appBandInfo = array.getJSONObject(i);
//                        approval_band = appBandInfo.getString("lah_approval_band")
//                                .equals("null") ? "" : appBandInfo.getString("lah_approval_band");
//
//                    }
//                }
//                if (!approval_band.isEmpty()) {
//                    requestQueue.add(countApp1Req);
//                }
//                else {
//                    connected = true;
//                    updateLay();
//                }
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//                connected = false;
//                updateLay();
//            }
//        }, error -> {
//            error.printStackTrace();
//            conn = false;
//            connected = false;
//            updateLay();
//        });
//
//        requestQueue.add(appBandReq);
//    }

    public void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
//                if (locUpdateLists.size() != 0) {
//                    for (int i = 0; i < locUpdateLists.size(); i++) {
//                        onlyLocationLists.add(locUpdateLists.get(i).getLocation());
//                    }
//                }
//                locUpdateAdapter.notifyDataSetChanged();
                if (allSelectedApprover.isEmpty()) {
                    approverTestLay.setEnabled(false);
                    approverTestEdit.setText("Admin of HR");
                    approverTestEdit.setTextColor(Color.BLACK);
                }
                else if (allSelectedApprover.size() == 1) {
                    approverTestLay.setEnabled(true);
                    approverTestEdit.setText(allSelectedApprover.get(0).getFirst());
                    approverTestEdit.setTextColor(Color.BLACK);
                    selected_approver_id = allSelectedApprover.get(0).getId();
                    selected_approver_name = allSelectedApprover.get(0).getFirst();
                }
                else {
                    approverTestLay.setEnabled(true);
                    approverTestEdit.setText("Select");
                }

                errorApprover.setVisibility(View.GONE);

                reasonName = new ArrayList<>();
                reasonName.add("Select");

                if (!reasonLists.isEmpty()) {
                    for (int i = 0; i < reasonLists.size(); i++) {
                        reasonName.add(reasonLists.get(i).getReason_name());
                        reasonAdapter.setNotifyOnChange(true);
                    }
                }

                reasonAdapter.clear();
                reasonAdapter.addAll(reasonName);
                reasonAdapter.notifyDataSetChanged();
                reasonType.setSelection(0);

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttUpNewRequest.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getDataInfo();
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
            AlertDialog dialog = new AlertDialog.Builder(AttUpNewRequest.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getDataInfo();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

//    public void getMachineData() {
//        waitProgress.show(getSupportFragmentManager(),"WaitBar");
//        waitProgress.setCancelable(false);
//        machineConnn = false;
//        machineCon = false;
//
//        machineCode = "";
//        machineType = "";
//
//        String url = api_url_front+"attendanceUpNewReq/getMachineData/"+selected_loc_id+"";
//        RequestQueue requestQueue = Volley.newRequestQueue(AttUpNewRequest.this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
//            machineConnn = true;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject macInfo = array.getJSONObject(i);
//                        machineCode = macInfo.getString("ams_mechine_code")
//                                .equals("null") ? "" : macInfo.getString("ams_mechine_code");
//                        machineType = macInfo.getString("ams_attendance_type")
//                                .equals("null") ? "" : macInfo.getString("ams_attendance_type");
//                    }
//                }
//                machineCon = true;
//                updateMachine();
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//                machineCon = false;
//                updateMachine();
//            }
//        }, error -> {
//            error.printStackTrace();
//            machineConnn = false;
//            machineCon = false;
//            updateMachine();
//        });
//
//        requestQueue.add(stringRequest);
//    }
//
//    private void updateMachine() {
//        waitProgress.dismiss();
//        if (machineConnn) {
//            if (machineCon) {
//                if (!machineCode.isEmpty() && !machineType.isEmpty()) {
//                    machCode.setText(machineCode);
//                    machType.setText(machineType);
//                }
//
//                machineConnn = false;
//                machineCon = false;
//            }
//            else {
//                AlertDialog dialog = new AlertDialog.Builder(AttUpNewRequest.this)
//                        .setMessage("There is a network issue in the server. Please Try later.")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel",null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(v -> {
//
//                    getMachineData();
//                    dialog.dismiss();
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(v -> {
//                    dialog.dismiss();
//                    finish();
//                });
//            }
//        }
//        else {
//            AlertDialog dialog = new AlertDialog.Builder(AttUpNewRequest.this)
//                    .setMessage("Please Check Your Internet Connection")
//                    .setPositiveButton("Retry", null)
//                    .setNegativeButton("Cancel",null)
//                    .show();
//
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
//            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//            positive.setOnClickListener(v -> {
//
//                getMachineData();
//                dialog.dismiss();
//            });
//
//            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//            negative.setOnClickListener(v -> {
//                dialog.dismiss();
//                finish();
//            });
//        }
//    }

    public void getAttendanceShow() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        machineConnn = false;
        machineCon = false;

        shiftin = "";
        shiftout = "";
        machineArr = "";
        machiDep = "";
        shiftName = "";
        locName = "";

        System.out.println(emp_id);
        System.out.println(selected_update_date);
        String url = api_url_front+"attendanceUpdateReq/showAttendance?emp_id="+emp_id+"&att_date="+selected_update_date;

        RequestQueue requestQueue = Volley.newRequestQueue(AttUpNewRequest.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            machineCon = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject statInfo = array.getJSONObject(i);

                        shiftin = statInfo.getString("dac_start_time")
                                .equals("null") ? null : statInfo.getString("dac_start_time");
                        shiftout = statInfo.getString("dac_end_time")
                                .equals("null") ? null : statInfo.getString("dac_end_time");
                        machineArr = statInfo.getString("dac_mac_in_time")
                                .equals("null") ? "" : statInfo.getString("dac_mac_in_time");
                        machiDep = statInfo.getString("dac_mac_out_time")
                                .equals("null") ? "" : statInfo.getString("dac_mac_out_time");
                        shiftName = statInfo.getString("osm_name")
                                .equals("null") ? null : statInfo.getString("osm_name");
                        locName = statInfo.getString("coa_name")
                                .equals("null") ? null : statInfo.getString("coa_name");
                    }
                }
                machineConnn = true;
                updateDataLay();
            }
            catch (JSONException e) {
                e.printStackTrace();
                machineConnn = false;
                updateDataLay();
            }
        }, error -> {
            error.printStackTrace();
            machineCon = false;
            machineConnn = false;
            updateDataLay();
        });

        requestQueue.add(stringRequest);
    }

    private void updateDataLay() {
        waitProgress.dismiss();
        if (machineCon) {
            if (machineConnn) {
                afterSelectingDate.setVisibility(View.VISIBLE);
                String noTimeMsg = "No Time Found";
                if (shiftin == null) {
                    intime.setText(noTimeMsg);
                }
                else {
                    if (shiftin.isEmpty()) {
                        intime.setText(noTimeMsg);
                    }
                    else {
                        intime.setText(shiftin);
                    }
                }

                if (shiftout == null) {
                    outtime.setText(noTimeMsg);
                }
                else {
                    if (shiftout.isEmpty()) {
                        outtime.setText(noTimeMsg);
                    }
                    else {
                        outtime.setText(shiftout);
                    }
                }
                if (machineArr == null) {
                    machArrTime.setText(noTimeMsg);
                }
                else {
                    if (machineArr.isEmpty()) {
                        machArrTime.setText(noTimeMsg);
                    }
                    else {
                        machArrTime.setText(machineArr);
                    }
                }

                if (machiDep == null) {
                    machDepTime.setText(noTimeMsg);
                }
                else {
                    if (machiDep.isEmpty()) {
                        machDepTime.setText(noTimeMsg);
                    }
                    else {
                        machDepTime.setText(machiDep);
                    }
                }

                String noShiftMsg = "No Shift Found";
                if (shiftName == null) {
                    exShNa.setText(noShiftMsg);
                }
                else {
                    if (shiftName.isEmpty()) {
                        exShNa.setText(noShiftMsg);
                    }
                    else {
                        exShNa.setText(shiftName);
                    }
                }

                String noLocMsg = "No Location Found";
                if (locName == null) {
                    exLocNa.setText(noLocMsg);
                }
                else {
                    if (locName.isEmpty()) {
                        exLocNa.setText(noLocMsg);
                    }
                    else {
                        exLocNa.setText(locName);
                    }
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttUpNewRequest.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAttendanceShow();
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
            AlertDialog dialog = new AlertDialog.Builder(AttUpNewRequest.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAttendanceShow();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

//    public void getReasonData() {
//        waitProgress.show(getSupportFragmentManager(),"WaitBar");
//        waitProgress.setCancelable(false);
//        reasonConnnn = false;
//        reasonCon = false;
//
//        reasonLists = new ArrayList<>();
//
//        String url = api_url_front+"attendanceUpNewReq/getReasonData/"+selected_attendance_type+"";
//        RequestQueue requestQueue = Volley.newRequestQueue(AttUpNewRequest.this);
//
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
//            reasonConnnn = true;
//            try {
//                JSONObject jsonObject = new JSONObject(response);
//                String items = jsonObject.getString("items");
//                String count = jsonObject.getString("count");
//                if (!count.equals("0")) {
//                    JSONArray array = new JSONArray(items);
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject reasonInfo = array.getJSONObject(i);
//
//                        String aurrm_id = reasonInfo.getString("aurrm_id")
//                                .equals("null") ? "" : reasonInfo.getString("aurrm_id");
//                        String aurrm_name = reasonInfo.getString("aurrm_name")
//                                .equals("null") ? "" : reasonInfo.getString("aurrm_name");
//
//                        reasonLists.add(new ReasonList(aurrm_id,aurrm_name));
//                    }
//                }
//                reasonCon = true;
//                updateReason();
//            }
//            catch (JSONException e) {
//                e.printStackTrace();
//                reasonCon = false;
//                updateReason();
//            }
//        }, error -> {
//            error.printStackTrace();
//            reasonConnnn = false;
//            reasonCon = false;
//            updateReason();
//        });
//
//        requestQueue.add(stringRequest);
//    }

//    private void updateReason() {
//        waitProgress.dismiss();
//        if (reasonConnnn) {
//            if (reasonCon) {
//                reasonName = new ArrayList<>();
//                reasonName.add("Select");
//
//                if (reasonLists.size() != 0) {
//                    for (int i = 0; i < reasonLists.size(); i++) {
//                        reasonName.add(reasonLists.get(i).getReason_name());
//                        reasonAdapter.setNotifyOnChange(true);
//                        System.out.println(reasonLists.get(i).getReason_name());
//                    }
//                }
//
//                reasonAdapter.clear();
//                reasonAdapter.addAll(reasonName);
//                reasonAdapter.notifyDataSetChanged();
//                reasonType.setSelection(0);
//
//                reasonConnnn = false;
//                reasonCon = false;
//            }
//            else {
//                AlertDialog dialog = new AlertDialog.Builder(AttUpNewRequest.this)
//                        .setMessage("There is a network issue in the server. Please Try later.")
//                        .setPositiveButton("Retry", null)
//                        .setNegativeButton("Cancel",null)
//                        .show();
//
//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
//                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//                positive.setOnClickListener(v -> {
//
//                    getReasonData();
//                    dialog.dismiss();
//                });
//
//                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//                negative.setOnClickListener(v -> {
//                    dialog.dismiss();
//                    finish();
//                });
//            }
//        }
//        else {
//            AlertDialog dialog = new AlertDialog.Builder(AttUpNewRequest.this)
//                    .setMessage("Please Check Your Internet Connection")
//                    .setPositiveButton("Retry", null)
//                    .setNegativeButton("Cancel",null)
//                    .show();
//
//            dialog.setCancelable(false);
//            dialog.setCanceledOnTouchOutside(false);
//            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
//            positive.setOnClickListener(v -> {
//
//                getReasonData();
//                dialog.dismiss();
//            });
//
//            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
//            negative.setOnClickListener(v -> {
//                dialog.dismiss();
//                finish();
//            });
//        }
//    }

    //-------------------Sending Request-------------------------
    public void insertReq() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);

        insertConnn = false;
        insertCon = false;
        isInserted = false;

        String insertUrl = api_url_front+"attendanceUpNewReq/insertAttUpReq";

        RequestQueue requestQueue = Volley.newRequestQueue(AttUpNewRequest.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertUrl, response -> {
            insertConnn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                String updated_req = jsonObject.getString("updated_req");
                if (string_out.equals("Successfully Created")) {
                    insertCon = true;
                    isInserted = updated_req.equals("true");
                }
                else {
                    System.out.println(string_out);
                    insertCon = false;
                }
                updateInsertLay();
            }
            catch (JSONException e) {
                insertCon = false;
                updateInsertLay();
            }
        }, error -> {
            error.printStackTrace();
            insertConnn = false;
            insertCon = false;
            updateInsertLay();
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                System.out.println(machineArr);
                System.out.println(machiDep);
                headers.put("NOW_DATE", now_date);
                headers.put("P_ADDRESS",selected_address_station);
                headers.put("P_ARRIVAL_TIME",arrival_time);
                headers.put("P_CALLING_TITLE",calling_title);
                headers.put("P_DEPART_TIME", depart_time);
                headers.put("P_DEPT_ID",selected_dept_id);
                headers.put("P_DIVM_ID",selected_divm_id);
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_JSM_ID",selected_jsm_id);
                headers.put("P_LOC_ID","");
                headers.put("P_REASON_DETAILS",selected_reason_desc);
                headers.put("P_REASON_ID",selected_reason_id);
                headers.put("P_SELECTED_APPROVER_ID",selected_approver_id);
                headers.put("P_SELECTED_ATT_TYPE",selected_attendance_type);
                headers.put("P_SELECTED_REQUEST",selected_request);
                headers.put("P_SELECTED_UPDATE_DATE",selected_update_date);
                headers.put("P_SHIFT_ID","");
                headers.put("P_USER_ID",user_id);
                return headers;
            }
        };

        requestQueue.add(stringRequest);
    }

    private void updateInsertLay() {
        waitProgress.dismiss();
        if (insertConnn) {
            if (insertCon) {
                if (isInserted) {
//                    selected_shift_id = "";
                    selected_approver_id = "";
                    System.out.println("INSERTED");

                    AlertDialog dialog = new AlertDialog.Builder(AttUpNewRequest.this)
                            .setMessage("Request Sent Successfully")
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
                    Toast.makeText(getApplicationContext(), "Failed to Send Request.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttUpNewRequest.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    insertReq();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AttUpNewRequest.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                insertReq();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}