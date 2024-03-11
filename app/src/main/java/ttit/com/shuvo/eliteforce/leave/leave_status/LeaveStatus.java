package ttit.com.shuvo.eliteforce.leave.leave_status;

import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class LeaveStatus extends AppCompatActivity {

    TextInputEditText selectMonth;
    TextInputLayout selectMonthLay;
    TextView errorReport;
    TextView fromMonth;

    CardView statusReport;

    MaterialButton download;

    TextInputEditText name;
    TextInputEditText id;
    TextInputEditText designation;
    TextInputEditText department;

    TextView balanceEL;
    TextView balanceCL;
    TextView balanceSL;
    TextView balanceML;

    TextView consumEL;
    TextView consumCL;
    TextView consumSL;
    TextView consumML;

    private ProgressDialog pDialog;

    String emp_id = "";
    String emp_name = "";
    String user_id = "";
    String desg = "";
    String dept = "";

    String selected_date = "";
    String selected_month_full = "";
    String year_full = "";
    String showDate = "";

    String URL = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    private Boolean downConn = false;

    ArrayList<String> leaveCode;
    ArrayList<String> consumptionValue;
    ArrayList<String> balanceValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_status);

        selectMonth = findViewById(R.id.select_month);
        selectMonthLay = findViewById(R.id.select_month_lay);

        download = findViewById(R.id.download_leave_status_report);

        name = findViewById(R.id.name_leave_status);
        id = findViewById(R.id.id_leave_status);
        designation = findViewById(R.id.str_designation_leave_status);
        department = findViewById(R.id.departmnet_leave_status);

        errorReport = findViewById(R.id.error_report_msg_for_no_entry);
        fromMonth = findViewById(R.id.from_to_month_year);
        statusReport = findViewById(R.id.leave_status_report_card);

        balanceEL = findViewById(R.id.balance_el);
        balanceCL = findViewById(R.id.balance_cl);
        balanceSL = findViewById(R.id.balance_sl);
        balanceML = findViewById(R.id.balance_ml);

        consumEL = findViewById(R.id.consumption_el);
        consumCL = findViewById(R.id.consumption_cl);
        consumSL = findViewById(R.id.consumption_sl);
        consumML = findViewById(R.id.consumption_ml);

        leaveCode = new ArrayList<>();
        consumptionValue = new ArrayList<>();
        balanceValue = new ArrayList<>();

        leaveCode.add("EL");
        leaveCode.add("CL");
        leaveCode.add("SL");
        leaveCode.add("ML");

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

        if (userDesignations.size() != 0) {
            desg = userDesignations.get(0).getJsm_name();
            if (desg == null) {
                desg = "";
            }
            designation.setText(desg);

            dept = userDesignations.get(0).getDept_name();
            if (dept == null) {
                dept = "";
            }
            department.setText(dept);
        }

        id.setText(user_id);

        selectMonth.setOnClickListener(v -> {
            Date c = Calendar.getInstance().getTime();
            String formattedDate;
            SimpleDateFormat df = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            formattedDate = df.format(c);


            int yearSelected;
            int monthSelected;
            MonthFormat monthFormat = MonthFormat.LONG;
            String customTitle = "Select Month";
            // Use the calendar for create ranges
            Calendar calendar = Calendar.getInstance();
            yearSelected = calendar.get(Calendar.YEAR);
            monthSelected = calendar.get(Calendar.MONTH);
            calendar.clear();
            calendar.set(2000, 0, 1); // Set minimum date to show in dialog
            long minDate = calendar.getTimeInMillis(); // Get milliseconds of the modified date

            calendar.clear();
            calendar.set(Integer.parseInt(formattedDate), 11, 31); // Set maximum date to show in dialog
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
                String yearName;

                if (month == 1) {
                    monthName = "JANUARY";
                    mon = "JAN";
                } else if (month == 2) {
                    monthName = "FEBRUARY";
                    mon = "FEB";
                } else if (month == 3) {
                    monthName = "MARCH";
                    mon = "MAR";
                } else if (month == 4) {
                    monthName = "APRIL";
                    mon = "APR";
                } else if (month == 5) {
                    monthName = "MAY";
                    mon = "MAY";
                } else if (month == 6) {
                    monthName = "JUNE";
                    mon = "JUN";
                } else if (month == 7) {
                    monthName = "JULY";
                    mon = "JUL";
                } else if (month == 8) {
                    monthName = "AUGUST";
                    mon = "AUG";
                } else if (month == 9) {
                    monthName = "SEPTEMBER";
                    mon = "SEP";
                } else if (month == 10) {
                    monthName = "OCTOBER";
                    mon = "OCT";
                } else if (month == 11) {
                    monthName = "NOVEMBER";
                    mon = "NOV";
                } else if (month == 12) {
                    monthName = "DECEMBER";
                    mon = "DEC";
                }

                yearName  = String.valueOf(year);
                yearName = yearName.substring(yearName.length()-2);

                selected_month_full = monthName;
                year_full = String.valueOf(year);
                selected_date = "15-"+mon+"-"+yearName;
                SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                Date today = null;
                try {
                    today = sss.parse(selected_date);
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

                    SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.ENGLISH);
                    String llll = sdff.format(lastDayOfMonth);
                    selected_date =  llll+ "-" + mon +"-"+ yearName;
                }
                String tt = monthName + "-" + year;
                selectMonth.setText(tt);
                selectMonthLay.setHint("Month");

                errorReport.setVisibility(View.GONE);
                download.setVisibility(View.GONE);
                statusReport.setVisibility(View.GONE);

                showDate = Objects.requireNonNull(selectMonth.getText()).toString();

                getStatus();
            });

        });

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sss = new SimpleDateFormat("MMM-yy", Locale.ENGLISH);
        selected_date = sss.format(c);
        String selected_date1 = "15-" + selected_date;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

        Date today = null;
        try {
            today = simpleDateFormat.parse(selected_date1);
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

            SimpleDateFormat sdff = new SimpleDateFormat("dd",Locale.ENGLISH);
            String llll = sdff.format(lastDayOfMonth);
            selected_date1 =  llll+ "-" +selected_date;
            selected_date = selected_date1;
        }
        System.out.println(selected_date);


        SimpleDateFormat month_date = new SimpleDateFormat("MMMM",Locale.ENGLISH);
        String month_name = month_date.format(c);
        month_name = month_name.toUpperCase();
        System.out.println(month_name);
        selected_month_full = month_name;

        SimpleDateFormat presentYear = new SimpleDateFormat("yyyy",Locale.ENGLISH);
        String yyyy = presentYear.format(c);
        year_full = yyyy;

        String tt = month_name+"-"+yyyy;
        selectMonth.setText(tt);
        selectMonthLay.setHint("Month");

        showDate = Objects.requireNonNull(selectMonth.getText()).toString();

        errorReport.setVisibility(View.GONE);
        download.setVisibility(View.GONE);
        statusReport.setVisibility(View.GONE);

        download.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(LeaveStatus.this);
            builder.setTitle("Download Leave Status!")
                    .setMessage("Do you want to download this report?")
                    .setPositiveButton("YES", (dialog, which) -> new DownloadPDF().execute())
                    .setNegativeButton("NO", (dialog, which) -> {

                    });
            AlertDialog alert = builder.create();
            alert.show();
        });

        getStatus();
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
        downConn = true;
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

                Download(URL, "Leave Status "+showDate);

            } else {
                downConn = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            pDialog.dismiss();
            if (downConn) {
                Toast.makeText(getApplicationContext(), "Downloading...", Toast.LENGTH_SHORT).show();
                downConn = false;
            }
            else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                AlertDialog dialog = new AlertDialog.Builder(LeaveStatus.this)
                        .setMessage("Please Check Your Internet Connection")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

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

    public void getStatus() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        balanceValue = new ArrayList<>();
        consumptionValue = new ArrayList<>();

        String blUrl = api_url_front+"leave/getLeaveStatus_balance/"+emp_id+"/"+selected_date+"";
        String consumUrl = api_url_front+"leave/getLeaveStatus_consume/"+emp_id+"/"+selected_date+"";

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveStatus.this);

        StringRequest consReq = new StringRequest(Request.Method.GET, consumUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject consInfo = array.getJSONObject(i);
                        String val = consInfo.getString("leave_consumption")
                                .equals("null") ? "0" : consInfo.getString("leave_consumption");
                        consumptionValue.add(val);
                    }
                }

                String criteria = "Month,Year :"+selected_month_full+", "+year_full+", Employee ID:"+user_id+"";
                URL = "http://103.56.208.123:7778/reports/rwservlet?hrselite+report=D:\\ELITE_FORCE\\Reports\\EMP_LEAVE_CONS_BAL.rep+EMPID="+emp_id+"+P_DATE='"+selected_date+"'+CRITERIA='"+criteria+"'";
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

        StringRequest blReq = new StringRequest(Request.Method.GET, blUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject blInfo = array.getJSONObject(i);

                        String val = blInfo.getString("leave_balance")
                                .equals("null") ? "0" : blInfo.getString("leave_balance");

                        balanceValue.add(val);
                    }
                }
                requestQueue.add(consReq);
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

        requestQueue.add(blReq);
    }

    private void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                statusReport.setVisibility(View.VISIBLE);
                download.setVisibility(View.VISIBLE);

                consumEL.setText(consumptionValue.get(0));
                consumCL.setText(consumptionValue.get(1));
                consumSL.setText(consumptionValue.get(2));
                consumML.setText(consumptionValue.get(3));

                balanceEL.setText(balanceValue.get(0));
                balanceCL.setText(balanceValue.get(1));
                balanceSL.setText(balanceValue.get(2));
                balanceML.setText(balanceValue.get(3));

                String tt = "For Month-Year: "+ showDate;
                fromMonth.setText(tt);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(LeaveStatus.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getStatus();
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
            AlertDialog dialog = new AlertDialog.Builder(LeaveStatus.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getStatus();
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