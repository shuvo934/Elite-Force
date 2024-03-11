package ttit.com.shuvo.eliteforce.attendance.att_report;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import oracle.jdbc.rowset.OracleSerialBlob;
import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_report.adapters.AttenReportAdapter;
import ttit.com.shuvo.eliteforce.attendance.att_report.model.AttenReportList;
import ttit.com.shuvo.eliteforce.attendance.att_report.model.ReportInformation;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class AttendanceReport extends AppCompatActivity {

    private ProgressDialog pDialog;
    String firstDate = "";
    String firstDateToView = "";
    String lastDate = "";
    String lastDateToView = "";

    TextInputEditText selecetMonth;
    TextInputLayout selectMonthLay;

    MaterialButton downlaodReport;

    CardView report;
    RecyclerView reportview;
    RecyclerView.LayoutManager layoutManager;
    AttenReportAdapter attenReportAdapter;

    TextView fromToDate;
    TextInputEditText empName;
    TextInputEditText empJID;
    TextInputEditText empFunDes;
    TextInputEditText empDiv;
    TextInputEditText empDep;
    TextView dateToDate;

    TextView calenderDays;
    TextView totalWorking;
    TextView presentDays;
    TextView absentDays;
    TextView weeklyHolidays;
    TextView holidays;
    TextView workWeekend;
    TextView workHolidays;

    ArrayList<ReportInformation> reportInformations;
    ArrayList<AttenReportList> attenReportLists;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean infoConnected = false;
    private Boolean connected = false;


    LinearLayout attenData;
    LinearLayout attenDataNot;

    String emp_id = "";
    String URL = "";

    String selected_month_full = "";
    String year_full = "";

    String coa_id = "";
    String present_days = "";
    String absent_days = "";
    String weekend = "";
    String present_weekend = "";
    String hol = "";
    String present_holi = "";

    String working_days = "";

    int daysInMonth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_report);

        fromToDate = findViewById(R.id.from_to_date);
        empName = findViewById(R.id.name_report);
        empDep = findViewById(R.id.department_report);
        empDiv = findViewById(R.id.division_report);
        empFunDes = findViewById(R.id.func_designation_report);
        empJID = findViewById(R.id.id_report);
        dateToDate = findViewById(R.id.date_from_report);

        selecetMonth = findViewById(R.id.select_month_att_report);
        selectMonthLay = findViewById(R.id.select_month_att_report_lay);

        attenData = findViewById(R.id.attendancebefore_text);
        attenDataNot = findViewById(R.id.no_data_msg_attendance);

        downlaodReport = findViewById(R.id.download_report);

        report = findViewById(R.id.report_card);

        reportview = findViewById(R.id.attnd_list_view);

        calenderDays = findViewById(R.id.days_in_month);
        totalWorking = findViewById(R.id.working_days_in_month);
        presentDays = findViewById(R.id.present_days_in_month);
        absentDays = findViewById(R.id.absent_days_in_month);
        weeklyHolidays = findViewById(R.id.weekly_holidays_days_in_month);
        holidays = findViewById(R.id.holidays_days_in_month);
        workWeekend = findViewById(R.id.work_weekend_days_in_month);
        workHolidays = findViewById(R.id.work_on_holi_days_in_month);

        emp_id = userInfoLists.get(0).getEmp_id();

        reportInformations = new ArrayList<>();
        attenReportLists = new ArrayList<>();

        reportview.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        reportview.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(reportview.getContext(),DividerItemDecoration.VERTICAL);
        reportview.addItemDecoration(dividerItemDecoration);

        selecetMonth.setOnClickListener(v -> {

            Date c = Calendar.getInstance().getTime();

            String formattedYear = "";
            String monthValue = "";
            String lastformattedYear = "";
            String lastdateView = "";

            SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.ENGLISH);

            formattedYear = df.format(c);
            monthValue = sdf.format(c);
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

            // Use the calendar for create ranges
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
                String mon = "";
//                String yearName = "";

                if (month == 1) {
                    monthName = "JANUARY";
                    mon = "Jan";
                } else if (month == 2) {
                    monthName = "FEBRUARY";
                    mon = "Feb";
                } else if (month == 3) {
                    monthName = "MARCH";
                    mon = "Mar";
                } else if (month == 4) {
                    monthName = "APRIL";
                    mon = "Apr";
                } else if (month == 5) {
                    monthName = "MAY";
                    mon = "May";
                } else if (month == 6) {
                    monthName = "JUNE";
                    mon = "Jun";
                } else if (month == 7) {
                    monthName = "JULY";
                    mon = "Jul";
                } else if (month == 8) {
                    monthName = "AUGUST";
                    mon = "Aug";
                } else if (month == 9) {
                    monthName = "SEPTEMBER";
                    mon = "Sep";
                } else if (month == 10) {
                    monthName = "OCTOBER";
                    mon = "Oct";
                } else if (month == 11) {
                    monthName = "NOVEMBER";
                    mon = "Nov";
                } else if (month == 12) {
                    monthName = "DECEMBER";
                    mon = "Dec";
                }

//                yearName  = String.valueOf(year);
//                yearName = yearName.substring(yearName.length()-2);

                selected_month_full = monthName;
                year_full = String.valueOf(year);
                firstDate = "01-"+mon+"-"+year;
                System.out.println(firstDate);
                firstDateToView = "01 "+mon +", "+ year;;
                String text = monthName + "-" + year;
                selecetMonth.setText(text);
                selectMonthLay.setHint("Month");
                SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                Date today1 = null;
                try {
                    today1 = sss.parse(firstDate);
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
                    lastDate =  llll+ "-" + mon +"-"+ year;
                    System.out.println(lastDate);
                    lastDateToView = llll + " " + mon + ", " + year;
                }

                YearMonth yearMonthObject;

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    yearMonthObject = YearMonth.of(Integer.parseInt(year_full), month);
                    daysInMonth = yearMonthObject.lengthOfMonth();
                    System.out.println(daysInMonth);
                }

                getAttendanceReport();
            });
        });

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sss = new SimpleDateFormat("MMM-yyyy", Locale.ENGLISH);
        SimpleDateFormat sdff = new SimpleDateFormat("dd-MMM-yyyy",Locale.ENGLISH);
        SimpleDateFormat sdffff = new SimpleDateFormat("dd MMM, yyyy",Locale.ENGLISH);
        SimpleDateFormat sdffffdf = new SimpleDateFormat("MMM, yyyy",Locale.ENGLISH);
        firstDate = sss.format(c);
        firstDate = "01-" + firstDate;
        firstDateToView = "01 " +sdffffdf.format(c);
        System.out.println(firstDate);

        Date today = null;
        try {
            today = sdff.parse(firstDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar1 = Calendar.getInstance();
        if (today != null) {
            calendar1.setTime(today);
            calendar1.add(Calendar.MONTH, 1);
            calendar1.set(Calendar.DAY_OF_MONTH, 1);
            calendar1.add(Calendar.DATE, -1);

            Date lastDayOfMonth = calendar1.getTime();

            lastDate = sdff.format(lastDayOfMonth);
            lastDateToView = sdffff.format(lastDayOfMonth);
            System.out.println(lastDate);

        }

        SimpleDateFormat month_date = new SimpleDateFormat("MMMM",Locale.ENGLISH);
        String month_name = month_date.format(c);
        month_name = month_name.toUpperCase();
        System.out.println(month_name);

        SimpleDateFormat monthNumb = new SimpleDateFormat("MM",Locale.ENGLISH);
        String monthNNN = monthNumb.format(c);


        SimpleDateFormat presentYear = new SimpleDateFormat("yyyy",Locale.ENGLISH);
        String yyyy = presentYear.format(c);

        String text = month_name+"-"+yyyy;
        selecetMonth.setText(text);
        selectMonthLay.setHint("Month");

        YearMonth yearMonthObject;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            yearMonthObject = YearMonth.of(Integer.parseInt(yyyy), Integer.parseInt(monthNNN));
            daysInMonth = yearMonthObject.lengthOfMonth();
            System.out.println(daysInMonth);
        }

        downlaodReport.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceReport.this);
            builder.setTitle("Download Attendance Report!")
                    .setMessage("Do you want to download this report?")
                    .setPositiveButton("YES", (dialog, which) -> new DownloadPDF().execute())
                    .setNegativeButton("NO", (dialog, which) -> {
                    });
            AlertDialog alert = builder.create();
            alert.show();

        });

        getAttendanceReport();
    }

    public void Download(String url, String title) {

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        String tempTitle = title.replace(" ", "_");
        request.setTitle(tempTitle);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, tempTitle+".pdf");
        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        request.setMimeType("application/pdf");
        request.allowScanningByMediaScanner();
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        downloadManager.enqueue(request);
        infoConnected = true;
    }

    public boolean isConnected() {
        boolean connected = false;
        boolean isMobile = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
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

    public class DownloadPDF extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(getApplicationContext());
            pDialog.setMessage("Downloading...");
            pDialog.setCancelable(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (isConnected() && isOnline()) {

                Download(URL, firstDate+" to "+lastDate);

            } else {
                infoConnected = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();
            if (infoConnected) {
                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                infoConnected = false;
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(AttendanceReport.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

//                dialog.setCancelable(false);
//                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    new DownloadPDF().execute();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }

        }
    }

    public void getAttendanceReport() {

        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        infoConnected = false;

        attenReportLists = new ArrayList<>();
        reportInformations = new ArrayList<>();

        present_days = "";
        absent_days = "";
        weekend = "";
        present_weekend = "";
        hol = "";
        present_holi = "";

        working_days = "";

        String attUrl = api_url_front+"attendance/getAttReportData/"+emp_id+"/"+firstDate+"/"+lastDate+"";
        String reportInfoUrl = api_url_front+"attendance/getReportInfo/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceReport.this);

        StringRequest reportInfoReq = new StringRequest(Request.Method.GET, reportInfoUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject reportInfo = array.getJSONObject(i);

                        String emp_name = reportInfo.getString("emp_name")
                                .equals("null") ? "" : reportInfo.getString("emp_name");
                        String emp_code = reportInfo.getString("emp_code")
                                .equals("null") ? "" : reportInfo.getString("emp_code");
                        String jsm_code = reportInfo.getString("jsm_code")
                                .equals("null") ? "" : reportInfo.getString("jsm_code");
                        String jsm_name = reportInfo.getString("jsm_name")
                                .equals("null") ? "" : reportInfo.getString("jsm_name");
                        String job_shift = reportInfo.getString("job_shift")
                                .equals("null") ? "" : reportInfo.getString("job_shift");
                        String job_email = reportInfo.getString("job_email")
                                .equals("null") ? "" : reportInfo.getString("job_email");
                        String desig_priority = reportInfo.getString("desig_priority")
                                .equals("null") ? "" : reportInfo.getString("desig_priority");
                        String job_calling_title = reportInfo.getString("job_calling_title")
                                .equals("null") ? "" : reportInfo.getString("job_calling_title");
                        String dept_name = reportInfo.getString("dept_name")
                                .equals("null") ? "" : reportInfo.getString("dept_name");
                        String divm_name = reportInfo.getString("divm_name")
                                .equals("null") ? "" : reportInfo.getString("divm_name");
                        String coa_name_1 = reportInfo.getString("coa_name_1")
                                .equals("null") ? "" : reportInfo.getString("coa_name_1");
                        String coa_name_2 = reportInfo.getString("coa_name_2")
                                .equals("null") ? "" : reportInfo.getString("coa_name_2");
                        String job_actual_date = reportInfo.getString("job_actual_date")
                                .equals("null") ? "" : reportInfo.getString("job_actual_date");
                        String job_status = reportInfo.getString("job_status")
                                .equals("null") ? "" : reportInfo.getString("job_status");
                        String new_coa_id = reportInfo.getString("coa_id")
                                .equals("null") ? "" : reportInfo.getString("coa_id");

                        reportInformations.add(new ReportInformation(emp_name,emp_code,desig_priority,
                                jsm_name, job_calling_title, jsm_code,
                                job_status, job_shift, divm_name,
                                dept_name, job_actual_date, coa_name_1,
                                coa_name_2));
                        coa_id = new_coa_id;
                    }
                }
                getAttStatus(coa_id);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateLayout();
            }
        } ,error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLayout();
        });

        StringRequest attendanceReq = new StringRequest(Request.Method.GET, attUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject attRepInfo = array.getJSONObject(i);

                        String dac_in_date_time = attRepInfo.getString("dac_in_date_time")
                                .equals("null") ? null : attRepInfo.getString("dac_in_date_time");

                        String dac_late_after = attRepInfo.getString("dac_late_after")
                                .equals("null") ? "" : attRepInfo.getString("dac_late_after");
                        String dac_early_before = attRepInfo.getString("dac_early_before")
                                .equals("null") ? "" : attRepInfo.getString("dac_early_before");
//                        String dac_end_time = attRepInfo.getString("dac_end_time");

                        String dac_out_date_time = attRepInfo.getString("dac_out_date_time")
                                .equals("null") ? null : attRepInfo.getString("dac_out_date_time");

                        String dac_date1 = attRepInfo.getString("dac_date1")
                                .equals("null") ? "" : attRepInfo.getString("dac_date1");

                        String statusShort = attRepInfo.getString("dac_attn_status")
                                .equals("null") ? null : attRepInfo.getString("dac_attn_status");

//                        String month_year = attRepInfo.getString("month_year");
//                        String dac_overtime_avail_flag = attRepInfo.getString("dac_overtime_avail_flag");

                        String lc_name = attRepInfo.getString("lc_name")
                                .equals("null") ? null : attRepInfo.getString("lc_name");

//                        String dac_notes = attRepInfo.getString("dac_notes");
                        String osm_name = attRepInfo.getString("osm_name")
                                .equals("null") ? null : attRepInfo.getString("osm_name");
                        String coa_name = attRepInfo.getString("coa_name")
                                .equals("null") ? null : attRepInfo.getString("coa_name");
//                        String dac_ams_mechine_code = attRepInfo.getString("dac_ams_mechine_code");
//                        String coa_id = attRepInfo.getString("coa_id");
//                        String dac_late_flag = attRepInfo.getString("dac_late_flag");
//                        String dac_leave_consum_lc_id = attRepInfo.getString("dac_leave_consum_lc_id");
//                        String dac_leave_type = attRepInfo.getString("dac_leave_type");
//                        String dac_date = attRepInfo.getString("dac_date");

                        String inCode = attRepInfo.getString("in_machine_coa_id")
                                .equals("null") ? null : attRepInfo.getString("in_machine_coa_id");
                        String outCode = attRepInfo.getString("out_machine_coa_id")
                                .equals("null") ? null : attRepInfo.getString("out_machine_coa_id");

                        String in_lat = attRepInfo.getString("dac_in_attd_latitude")
                                .equals("null") ? "" : attRepInfo.getString("dac_in_attd_latitude");
                        String in_lon = attRepInfo.getString("dac_in_attd_longitude")
                                .equals("null") ? "" : attRepInfo.getString("dac_in_attd_longitude");
                        String out_lat = attRepInfo.getString("dac_out_attd_latitude")
                                .equals("null") ? "" : attRepInfo.getString("dac_out_attd_latitude");
                        String out_lon = attRepInfo.getString("dac_out_attd_longitude")
                                .equals("null") ? "" : attRepInfo.getString("dac_out_attd_longitude");

                        String elr_id = attRepInfo.getString("elr_id")
                                .equals("null") ? "" : attRepInfo.getString("elr_id");

                        String loc_file = attRepInfo.getString("loc_file")
                                            .equals("null") ? "" : attRepInfo.getString("loc_file");

                        String status = "";
                        String attStatus = "";

                        if (statusShort != null) {
                            if (dac_in_date_time != null && dac_out_date_time == null) {
                                status = "Out Miss";
                                attStatus = "Out Miss";
                            }else if (statusShort.equals("L") || statusShort.equals("LW") || statusShort.equals("LH")) {
                                status = lc_name == null ? "" : lc_name;
                                attStatus = "In Leave";
                            } else if (statusShort.equals("H")) {
                                status = "Holiday";
                                attStatus = "Off Day";
                            } else if (statusShort.equals("W")) {
                                status = "Weekend";
                                attStatus = "Off Day";
                            } else if (statusShort.equals("PL") || statusShort.equals("PLH") || statusShort.equals("PLW")) {
                                status = "Present & Leave";
                                attStatus = "Present on Leave Day";
                            } else if (statusShort.equals("PAT")) {
                                status = "Attended training";
                                attStatus = "White";
                            } else if (statusShort.equals("PHD") || statusShort.equals("PWD") || statusShort.equals("PLHD") || statusShort.equals("PLWD")) {
                                status = "Present & Day Off Taken";
                                attStatus = "White";
                            } else if (statusShort.equals("P") || statusShort.equals("PWWO") || statusShort.equals("PHWC") || statusShort.equals("PWWC") || statusShort.equals("PA")) {
                                status = "Present";
                                attStatus = "White";
                            } else if (statusShort.equals("PW") || statusShort.equals("PH")) {
                                status = "Present & Off Day";
                                attStatus = "Present on Off Day";
                            } else if (statusShort.equals("A")) {
                                status = "Absent";
                                attStatus = "Absent";
                            } else if (statusShort.equals("PT")) {
                                status = "Tour";
                                attStatus = "White";
                            } else if (statusShort.equals("SP")) {
                                status = "Suspend";
                                attStatus = "White";
                            } else {
                                status = "Absent";
                                attStatus = "Absent";
                            }
                        }
                        else {
                            status = "No Status";
                            attStatus = "White";
                        }

                        if (inCode != null && outCode != null) {
                            int in = Integer.parseInt(inCode);
                            int out = Integer.parseInt(outCode);
                            if (in != out ) {
                                attStatus = "Multi Station";
                            }
                        }

                        String shift = osm_name;

                        if (shift == null) {
                            shift = "";
                        }
                        if (lc_name != null) {
                            shift = "";
                        }
                        if (status.equals("Weekend")) {
                            shift = "";
                        }

                        String pl = coa_name;
                        if (pl == null) {
                            pl = "";
                        }

                        String inTime = dac_in_date_time;

                        Date in = null;
                        Date late = null;

                        String inStatus = "";

                        SimpleDateFormat tt = new SimpleDateFormat("hh:mm:ss aa", Locale.ENGLISH);
                        SimpleDateFormat newtt = new SimpleDateFormat("hh:mm aa",Locale.ENGLISH);

                        if (inTime == null) {
                            inTime = "";
                            inStatus = "";
                        }
                        else {
                            try {
                                in = tt.parse(inTime);
                                late = tt.parse(dac_late_after);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (in != null && late != null) {
                                inTime = newtt.format(in);
                                if (late.after(in)) {
                                    inStatus = "";
                                } else {
                                    inStatus = "Late";
                                }
                            }
                        }

                        String outTime = dac_out_date_time;

                        String outStatus = "";

                        if (outTime == null) {
                            outTime = "";
                            outStatus = "";
                        }
                        else {
                            try {
                                in = tt.parse(outTime);
                                late = tt.parse(dac_early_before);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (in != null && late != null) {
                                outTime = newtt.format(in);
                                if (late.after(in)) {
                                    outStatus = "Early";
                                } else {
                                    outStatus = "";
                                }
                            }
                        }

                        Blob blob = null;

                        if (!loc_file.isEmpty()) {
                            byte[] decodedString = Base64.decode(loc_file,Base64.DEFAULT);
                            blob = new OracleSerialBlob(decodedString);
                        }

                        attenReportLists.add(new AttenReportList(dac_date1,status,shift,pl,inTime,inStatus,outTime,outStatus,attStatus,in_lat,in_lon,out_lat,out_lon,blob,elr_id));
                    }
                }

                requestQueue.add(reportInfoReq);
            }
            catch (JSONException | SQLException e) {
                e.printStackTrace();
                connected = false;
                updateLayout();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateLayout();
        });

        requestQueue.add(attendanceReq);
    }

    public void getAttStatus(String coa_id_new) {

        String attStatUrl = api_url_front+"attendance/getAttStatus/"+emp_id+"/"+firstDate+"/"+lastDate+"";
        String workDaysUrl = api_url_front+"attendance/getWorkingDays/"+coa_id_new+"/"+firstDate+"/"+lastDate+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AttendanceReport.this);

        StringRequest workingDaysReq = new StringRequest(Request.Method.GET, workDaysUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject workDaysInfo = array.getJSONObject(i);

                        working_days = workDaysInfo.getString("work_days")
                                .equals("null") ? "0" : workDaysInfo.getString("work_days");

                    }
                }

                String criteria = "From: "+firstDate+" To "+lastDate+", Employee: "+reportInformations.get(0).getName()+"";
                URL = "http://103.56.208.123:7778/reports/rwservlet?hrselite+report=D:\\ELITE_FORCE\\Reports\\EMP_ATTENDANCE_PERSONAL.rep+EMPID="+emp_id+"+BEGIN_DATE='"+firstDate+"'+END_DATE='"+lastDate+"'+CRITERIA='"+criteria+"'";

                connected = true;
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
        });

        StringRequest attStatReq = new StringRequest(Request.Method.GET, attStatUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject attStatInfo = array.getJSONObject(i);

                        present_days = attStatInfo.getString("present_status")
                                .equals("null") ? "0" : attStatInfo.getString("present_status");
                        absent_days = attStatInfo.getString("absent_status")
                                .equals("null") ? "0" : attStatInfo.getString("absent_status");
                        weekend = attStatInfo.getString("weekend_status")
                                .equals("null") ? "0" : attStatInfo.getString("weekend_status");
                        present_weekend = attStatInfo.getString("present_weekend")
                                .equals("null") ? "0" : attStatInfo.getString("present_weekend");
                        hol = attStatInfo.getString("holiday_status")
                                .equals("null") ? "0" : attStatInfo.getString("holiday_status");
                        present_holi = attStatInfo.getString("present_holiday_status")
                                .equals("null") ? "0" : attStatInfo.getString("present_holiday_status");

                    }
                }
                requestQueue.add(workingDaysReq);
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
        });

        requestQueue.add(attStatReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                report.setVisibility(View.VISIBLE);
                downlaodReport.setVisibility(View.VISIBLE);
                attenReportAdapter = new AttenReportAdapter(attenReportLists, AttendanceReport.this);
                reportview.setAdapter(attenReportAdapter);

                if (attenReportLists.size() == 0) {
                    attenDataNot.setVisibility(View.VISIBLE);
                    attenData.setVisibility(View.GONE);
                } else {
                    attenData.setVisibility(View.VISIBLE);
                    attenDataNot.setVisibility(View.GONE);
                }

                for (int i = 0; i < reportInformations.size(); i++) {
                    empName.setText(reportInformations.get(i).getName());
                    empDep.setText(reportInformations.get(i).getDepartment());
                    empDiv.setText(reportInformations.get(i).getDivision());
                    empFunDes.setText(reportInformations.get(i).getFun_des());
                    empJID.setText(reportInformations.get(i).getId());
                }
                String date_tot_date = firstDateToView + " -- "+ lastDateToView;
                dateToDate.setText(date_tot_date);
                String from_to_date = "'From: " + firstDate+ " To "+lastDate+ ", Employee: "+ reportInformations.get(0).getName()+"'";
                fromToDate.setText(from_to_date);

                calenderDays.setText(String.valueOf(daysInMonth));
                totalWorking.setText(working_days);
                presentDays.setText(present_days);
                absentDays.setText(absent_days);
                weeklyHolidays.setText(weekend);
                holidays.setText(hol);
                workWeekend.setText(present_weekend);
                workHolidays.setText(present_holi);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AttendanceReport.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAttendanceReport();
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
            AlertDialog dialog = new AlertDialog.Builder(AttendanceReport.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAttendanceReport();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }
}