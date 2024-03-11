package ttit.com.shuvo.eliteforce.attendance;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_report.AttendanceReport;
import ttit.com.shuvo.eliteforce.attendance.att_update.AttendanceUpdate;
import ttit.com.shuvo.eliteforce.attendance.give_attendance.AttendanceGive;
import ttit.com.shuvo.eliteforce.attendance.movement_reg.MovementRegister;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class Attendance extends AppCompatActivity implements View.OnTouchListener {
    MaterialCardView attReport;
    MaterialCardView attUpdateall;
    MaterialCardView attGive;
    MaterialCardView movReg;

    PieChart pieChart;
    TextView refresh;
    FloatingActionButton floatingActionButton;

    int dateCount = 0;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";
    String emp_code = "";
    String beginDate = "";
    String lastDate = "";
    String lastDateForAttBot = "";
    String intTimeAttBot = "";
    String outTimeAttBot = "";
    String lastLtimAttBot = "";

    String absent = "";
    String present = "";
    String leave = "";
    String holiday = "";
    String late = "";
    String early = "";
    ArrayList<String> lastTenDays;
    ArrayList<String> lastTenDaysFromSQL;
    ArrayList<String> updatedFiles;
    ArrayList<String> updatedXml;

    public static int tracking_flag = 0;
    public static int live_tracking_flag = 0;

    ArrayList<PieEntry> NoOfEmp;

    SharedPreferences sharedPreferencesDA;
    public static String FILE_OF_DAILY_ACTIVITY = "";
    public static  String DISTANCE = "DISTANCE";
    public static  String TOTAL_TIME = "TOTAL_TIME";
    public static  String STOPPED_TIME = "STOPPED_TIME";

    float dX;
    float dY;
    private static float downRawX, downRawY;
    private final static float CLICK_DRAG_TOLERANCE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        attReport =findViewById(R.id.attendance_report);
        attUpdateall = findViewById(R.id.atten_update_all);
        attGive = findViewById(R.id.attendance_give);
        movReg = findViewById(R.id.movement_register_button);

        pieChart = findViewById(R.id.piechart_attendance);
        refresh = findViewById(R.id.refresh_graph_attendance);
        floatingActionButton = findViewById(R.id.floating_button_att_bottom);

        lastTenDays = new ArrayList<>();
        lastTenDaysFromSQL = new ArrayList<>();
        updatedFiles = new ArrayList<>();
        updatedXml = new ArrayList<>();

        emp_id = userInfoLists.get(0).getEmp_id();
        emp_code = userInfoLists.get(0).getUserName();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        lastDate = df.format(c);

        SimpleDateFormat dfffff = new SimpleDateFormat("dd-MMMM-yyyy", Locale.ENGLISH);

        lastDateForAttBot = dfffff.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy", Locale.ENGLISH);

        beginDate = sdf.format(c);
        beginDate = "01-"+beginDate;

        SimpleDateFormat mon = new SimpleDateFormat("MMMM",Locale.ENGLISH);

        String mmm = mon.format(c);

        String text = "Month: "+mmm;
        refresh.setText(text);

        NoOfEmp = new ArrayList<>();

        floatingActionButton.setOnTouchListener(Attendance.this);
        floatingActionButton.setOnClickListener(view -> showBottomSheetDialog());

        attReport.setOnClickListener(v -> {
            Intent intent = new Intent(Attendance.this, AttendanceReport.class);
            startActivity(intent);
        });

        attUpdateall.setOnClickListener(v -> {
            Intent intent = new Intent(Attendance.this, AttendanceUpdate.class);
            startActivity(intent);
        });

        attGive.setOnClickListener(v -> {
            Intent intent = new Intent(Attendance.this, AttendanceGive.class);
            intent.putExtra("LAST_TIME",lastLtimAttBot);
            intent.putExtra("TODAY_DATE",lastDateForAttBot);
            startActivity(intent);
        });

        movReg.setOnClickListener(v -> {
            Intent intent = new Intent(Attendance.this, MovementRegister.class);
            startActivity(intent);
        });

        attendanceChartInit();

        refresh.setOnClickListener(v -> {

            Date c1 = Calendar.getInstance().getTime();

            String formattedYear = "";
            String monthValue = "";
            String lastformattedYear = "";
            String lastdateView = "";

            SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf1 = new SimpleDateFormat("MM",Locale.ENGLISH);

            formattedYear = df1.format(c1);
            monthValue = sdf1.format(c1);
            int nowMonNumb = Integer.parseInt(monthValue);
            nowMonNumb = nowMonNumb - 1;
            int lastMonNumb = nowMonNumb - 5;

            if (lastMonNumb < 0) {
                lastMonNumb = lastMonNumb + 12;
                int formatY = Integer.parseInt(formattedYear);
                formatY = formatY - 1;
                lastformattedYear = String.valueOf(formatY);
            } else {
                lastformattedYear = formattedYear;
            }

            Date today = new Date();

            Calendar calendar1 = Calendar.getInstance();
            calendar1.setTime(today);

            calendar1.add(Calendar.MONTH, 1);
            calendar1.set(Calendar.DAY_OF_MONTH, 1);
            calendar1.add(Calendar.DATE, -1);

            Date lastDayOfMonth = calendar1.getTime();

            SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.ENGLISH);
            lastdateView = sdff.format(lastDayOfMonth);

            int yearSelected;
            int monthSelected;
            MonthFormat monthFormat = MonthFormat.LONG;
            String customTitle = "Select Month";

            Calendar calendar = Calendar.getInstance();
            yearSelected = calendar.get(Calendar.YEAR);
            monthSelected = calendar.get(Calendar.MONTH);
            calendar.clear();
            calendar.set(Integer.parseInt(lastformattedYear), lastMonNumb, 1); // Set minimum date to show in dialog
            long minDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

            calendar.clear();
            calendar.set(Integer.parseInt(formattedYear), nowMonNumb, Integer.parseInt(lastdateView)); // Set maximum date to show in dialog
            long maxDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

            // Create instance with date ranges values
            MonthYearPickerDialogFragment dialogFragment =  MonthYearPickerDialogFragment
                    .getInstance(monthSelected, yearSelected, minDate, maxDate, customTitle, monthFormat);



            dialogFragment.show(getSupportFragmentManager(), null);

            dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
                System.out.println(year);
                System.out.println(monthOfYear);

                int month = monthOfYear + 1;
                String monthName = "";
                String mon1 = "";
                String yearName = "";

                if (month == 1) {
                    monthName = "JANUARY";
                    mon1 = "JAN";
                } else if (month == 2) {
                    monthName = "FEBRUARY";
                    mon1 = "FEB";
                } else if (month == 3) {
                    monthName = "MARCH";
                    mon1 = "MAR";
                } else if (month == 4) {
                    monthName = "APRIL";
                    mon1 = "APR";
                } else if (month == 5) {
                    monthName = "MAY";
                    mon1 = "MAY";
                } else if (month == 6) {
                    monthName = "JUNE";
                    mon1 = "JUN";
                } else if (month == 7) {
                    monthName = "JULY";
                    mon1 = "JUL";
                } else if (month == 8) {
                    monthName = "AUGUST";
                    mon1 = "AUG";
                } else if (month == 9) {
                    monthName = "SEPTEMBER";
                    mon1 = "SEP";
                } else if (month == 10) {
                    monthName = "OCTOBER";
                    mon1 = "OCT";
                } else if (month == 11) {
                    monthName = "NOVEMBER";
                    mon1 = "NOV";
                } else if (month == 12) {
                    monthName = "DECEMBER";
                    mon1 = "DEC";
                }

                yearName  = String.valueOf(year);
                yearName = yearName.substring(yearName.length()-2);


                beginDate = "01-"+ mon1 +"-"+yearName;
                //selected_date = "01-"+mon+"-"+yearName;
                String mText = "Month: "+ monthName;
                refresh.setText(mText);

                SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                Date today1 = null;
                try {
                    today1 = sss.parse(beginDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                Calendar calendar11 = Calendar.getInstance();
                if (today1 != null) {
                    calendar11.setTime(today1);
                    calendar11.add(Calendar.MONTH, 1);
                    calendar11.set(Calendar.DAY_OF_MONTH, 1);
                    calendar11.add(Calendar.DATE, -1);

                    Date lastDayOfMonth1 = calendar11.getTime();

                    SimpleDateFormat sdff1 = new SimpleDateFormat("dd",Locale.ENGLISH);
                    String llll = sdff1.format(lastDayOfMonth1);
                    lastDate =  llll+ "-" + mon1 +"-"+ yearName;
                }

                getAttendanceGraph();
            });

        });

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -11);

        for (int i = 0 ; i < 10 ;i ++) {
            cal.add(Calendar.DAY_OF_YEAR, +1);
            Date calTime = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
            String ddd = simpleDateFormat.format(calTime);

            ddd = ddd.toUpperCase();
            System.out.println(ddd);
            lastTenDays.add(ddd);
        }

        getAttendanceGraph();
    }

    private void attendanceChartInit() {
        pieChart.setCenterText("Attendance");
        pieChart.setDrawEntryLabels(true);
        pieChart.setCenterTextSize(14);
        pieChart.setHoleRadius(40);
        pieChart.setTransparentCircleRadius(40);

        pieChart.setEntryLabelTextSize(11);
        pieChart.setEntryLabelColor(Color.DKGRAY);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.CENTER);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setXOffset(20);
        l.setTextSize(12);
        l.setWordWrapEnabled(false);
        l.setDrawInside(false);
        l.setYOffset(5f);

        pieChart.animateXY(1000, 1000);
    }

    private void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Attendance.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dial_attendance);
        TextView today_date_att_bot = bottomSheetDialog.findViewById(R.id.today_date_att_bottom);
        TextView inTime_att_bot = bottomSheetDialog.findViewById(R.id.in_time_att_bottom);
        TextView outTime_att_bot = bottomSheetDialog.findViewById(R.id.out_time_att_bottom);
        TextView lastTime = bottomSheetDialog.findViewById(R.id.last_time_att_bottom);
        if (today_date_att_bot != null) {
            today_date_att_bot.setText(lastDateForAttBot);
        }
        if (inTime_att_bot != null) {
            inTime_att_bot.setText(intTimeAttBot);
        }
        if (outTime_att_bot != null) {
            outTime_att_bot.setText(outTimeAttBot);
        }
        if (lastTime != null) {
            lastTime.setText(lastLtimAttBot);
        }
        bottomSheetDialog.show();
    }

    public void getAttendanceGraph() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        NoOfEmp = new ArrayList<>();
        absent = "";
        present = "";
        leave = "";
        holiday = "";
        late = "";
        early = "";

        String attendDataUrl = api_url_front+"dashboard/getAttendanceData/"+beginDate+"/"+lastDate+"/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Attendance.this);

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
                    getEmpData();
                }
                else {
                    connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(attendDataReq);
    }

    public void getEmpData() {
        final int[] job_id = {0};
        final String[] coa_id = {""};
        final String[] divm_id = {""};
        final String[] dept_id = {""};
        conn = false;
        connected = false;

        String empDataUrl = api_url_front+"attendance/getEmpData/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Attendance.this);

        StringRequest empDataReq = new StringRequest(Request.Method.GET, empDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empDataInfo = array.getJSONObject(i);
                        tracking_flag = Integer.parseInt(empDataInfo.getString("emp_timeline_tracker_flag")
                                        .equals("null") ? "0" : empDataInfo.getString("emp_timeline_tracker_flag"));

                        job_id[0] = Integer.parseInt(empDataInfo.getString("emp_job_id")
                                        .equals("null") ? "0" : empDataInfo.getString("emp_job_id"));

                        coa_id[0] = empDataInfo.getString("job_pri_coa_id")
                                .equals("null") ? "" : empDataInfo.getString("job_pri_coa_id");
                        divm_id[0] = empDataInfo.getString("jsm_divm_id")
                                .equals("null") ? "" : empDataInfo.getString("jsm_divm_id");
                        dept_id[0] = empDataInfo.getString("jsm_dept_id")
                                .equals("null") ? "" : empDataInfo.getString("jsm_dept_id");

                        live_tracking_flag = Integer.parseInt(empDataInfo.getString("emp_live_loc_tracker_flag")
                                                .equals("null") ? "0" : empDataInfo.getString("emp_live_loc_tracker_flag"));
                    }
                    if (tracking_flag == 1) {
                        getTrackerUploadDate(job_id[0],coa_id[0],divm_id[0],dept_id[0]);
                    }
                    else {
                        getEmpTodayAttData();
                    }
                }
                else {
                    connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(empDataReq);
    }

    public void getTrackerUploadDate(int job_id, String coa_id, String divm_id, String dept_id) {
        lastTenDaysFromSQL = new ArrayList<>();
        conn = false;
        connected = false;

        String trackerDataUrl = api_url_front+"attendance/getTrackUploadedDate/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Attendance.this);

        StringRequest trackerDataReq = new StringRequest(Request.Method.GET, trackerDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject trackerDataInfo = array.getJSONObject(i);

                        String elr_date = trackerDataInfo.getString("elr_date");
                        lastTenDaysFromSQL.add(elr_date);
                    }
                }
                dateCount = 0;
                getTrackerDate(job_id,coa_id,divm_id,dept_id);

            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        },error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(trackerDataReq);
    }

    public void getTrackerDate(int job_id, String coa_id, String divm_id, String dept_id) {
        boolean noDatetoUp = true;
        for (int i = 0; i < lastTenDays.size(); i++) {
            boolean dateFound = false;
            String date = lastTenDays.get(i);

            for (int j = 0; j < lastTenDaysFromSQL.size(); j++) {
                if (date.equals(lastTenDaysFromSQL.get(j))) {
                    dateFound = true;
                    System.out.println(date);
                }
            }
            if (!dateFound) {
                noDatetoUp = false;
                String fileName = emp_id+"_"+date+"_track";

                FILE_OF_DAILY_ACTIVITY = fileName;

                sharedPreferencesDA = getSharedPreferences(FILE_OF_DAILY_ACTIVITY, MODE_PRIVATE);

                String dist = sharedPreferencesDA.getString(DISTANCE,null);
                String totalTime = sharedPreferencesDA.getString(TOTAL_TIME,null);
                String stoppedTime = sharedPreferencesDA.getString(STOPPED_TIME,null);

                String stringFIle = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +  fileName +".gpx";

                File blobFile = new File(stringFIle);

                updatedXml.add(fileName);
                byte[] bytes = null;
                if (blobFile.exists()) {
                    dateCount++;
                    try {
                        bytes = method(blobFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updatedFiles.add(stringFIle);
                }
                uploadEmpTrackerFile(job_id,coa_id,divm_id,dept_id,date,bytes, blobFile,fileName,dist,totalTime,stoppedTime);
                break;
            }
            else {
                System.out.println("Ei "+date +" database e ase");
            }
        }
        if (noDatetoUp) {
            getEmpTodayAttData();
        }
    }

    public void uploadEmpTrackerFile(int job_id, String coa_id, String divm_id,
                                     String dept_id, String date, byte[] bytes, File blobFile,
                                     String fileName, String dist, String totalTime, String stoppedTime) {

        String uploadFileUrl = api_url_front+"attendance/uploadTrackerFile";

        RequestQueue requestQueue = Volley.newRequestQueue(Attendance.this);

        StringRequest uploadReq = new StringRequest(Request.Method.POST, uploadFileUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                System.out.println(string_out);
                if (string_out.equals("Successfully Created")) {
                    lastTenDaysFromSQL.add(date);
                    getTrackerDate(job_id,coa_id,divm_id,dept_id);
                }
                else {
                    System.out.println("EKHANE ASHE 3");
                    connected = false;
                    updateLayout();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                System.out.println("EKHANE ASHE 0");
                connected = false;
                updateLayout();
            }
        },error -> {
            error.printStackTrace();
            System.out.println("EKHANE ASHE -1");
            conn = false;
            connected = false;
            updateLayout();
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return bytes;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (blobFile.exists()) {
                    headers.put("P_ELR_ACTIVE","1");
                    headers.put("P_ELR_FILE_NAME",fileName);
                    headers.put("P_ELR_MIMETYPE","application/gpx+xml");
                    headers.put("P_ELR_FILETYPE",".gpx");
                }
                else {
                    headers.put("P_ELR_ACTIVE","0");
                    headers.put("P_ELR_FILE_NAME",null);
                    headers.put("P_ELR_MIMETYPE",null);
                    headers.put("P_ELR_FILETYPE",null);
                }
                headers.put("P_ELR_EMP_ID",emp_id);
                headers.put("P_ELR_JOB_ID",String.valueOf(job_id));
                headers.put("P_ELR_COA_ID",coa_id);
                headers.put("P_ELR_DIVM_ID",divm_id);
                headers.put("P_ELR_DEPT_ID",dept_id);
                headers.put("P_ELR_DATE",date);
                headers.put("P_ELR_USER",emp_code);
                headers.put("P_TOTAL_DISTANCE_KM",dist);
                headers.put("P_TOTAL_TIME",totalTime);
                headers.put("P_TOTAL_STOPPED_TIME",stoppedTime);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/binary";
            }
        };

        requestQueue.add(uploadReq);
    }

    public void getEmpTodayAttData() {
        intTimeAttBot = "";
        outTimeAttBot = "";
        lastLtimAttBot = "";

        conn = false;
        connected = false;

        String todayAttDataUrl = api_url_front+"attendance/getTodayAttData/"+emp_id+"/"+lastDateForAttBot+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Attendance.this);

        StringRequest todayAttReq = new StringRequest(Request.Method.GET,todayAttDataUrl, response -> {
            conn = true;
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
                connected = true;
                updateLayout();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(todayAttReq);
    }

    public static byte[] method(File file) throws IOException {
        FileInputStream fl = new FileInputStream(file);
        byte[] arr = new byte[(int) file.length()];
        fl.read(arr);
        fl.close();
        return arr;
    }

    private void updateLayout() {
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

                if (dateCount > 0) {
                    if (updatedFiles.size() != 0) {
                        for (int i = 0; i < updatedFiles.size(); i++) {
                            String stringFile = updatedFiles.get(i);
                            File blobFile = new File(stringFile);
                            if (blobFile.exists()) {
                                boolean deleted = blobFile.delete();
                                if (deleted) {
                                    System.out.println("Deleted");
                                }
                            }
                        }
                    }

                    if (updatedXml.size() != 0) {
                        for (int i = 0 ; i < updatedXml.size(); i++) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                File dir = new File(getApplicationInfo().dataDir, "shared_prefs/" + updatedXml.get(i)+ ".xml");
                                if(dir.exists()) {
                                    getSharedPreferences(updatedXml.get(i), MODE_PRIVATE).edit().clear().apply();
                                    boolean ddd = dir.delete();
                                    System.out.println(ddd);
                                } else {
                                    System.out.println(false);
                                }
                            }
                        }
                    }

                    if (dateCount == 1) {
                        AlertDialog dialog = new AlertDialog.Builder(Attendance.this)
                                .setTitle("Tracking Service!")
                                .setMessage(dateCount + " File Uploaded")
                                .setPositiveButton("OK",null)
                                .show();
                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(v -> dialog.dismiss());
                    } else {
                        AlertDialog dialog = new AlertDialog.Builder(Attendance.this)
                                .setTitle("Tracking Service!")
                                .setMessage(dateCount + " Files Uploaded")
                                .setPositiveButton("OK",null)
                                .show();
                        Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        positive.setOnClickListener(v -> dialog.dismiss());
                    }

                }

                if (intTimeAttBot != null) {
                    if (!intTimeAttBot.isEmpty()) {
                        intTimeAttBot = intTimeAttBot;
                    }
                    else {
                        intTimeAttBot = "--:--";
                    }
                }
                else {
                    intTimeAttBot = "--:--";
                }
                if (outTimeAttBot != null) {
                    if (!outTimeAttBot.isEmpty()) {
                        outTimeAttBot = outTimeAttBot;
                    }
                    else {
                        outTimeAttBot = "--:--";
                    }
                }
                else {
                    outTimeAttBot = "--:--";
                }
                if (!outTimeAttBot.equals("--:--") && !intTimeAttBot.equals("--:--")) {
                    lastLtimAttBot = outTimeAttBot;
                }
                else if (outTimeAttBot.equals("--:--") && !intTimeAttBot.equals("--:--")){
                    lastLtimAttBot = intTimeAttBot;
                }
                else {
                    lastLtimAttBot = "--:--";
                }
                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Attendance.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAttendanceGraph();
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
            AlertDialog dialog = new AlertDialog.Builder(Attendance.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAttendanceGraph();
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
}