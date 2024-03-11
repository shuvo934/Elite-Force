package ttit.com.shuvo.eliteforce.payRoll.advance_details;

import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class AdvanceDetails extends AppCompatActivity {

    TextInputEditText selectMonth;
    TextInputLayout selectMonthLay;

    TextView errorMsgMonth;
    
    CardView reportCard;

    TextView monthName;

    TextInputEditText empName;
    TextInputEditText id;
    TextInputEditText band;
    TextInputEditText strDes;
    TextInputEditText jobPosition;

    TextView advTaken;
    TextView advPaid;
    TextView scAdvPaid;
    TextView totalPaid;
    TextView scAdvAllTaken;
    TextView scAdvAllPaid;
    TextView scAdvAllPayable;


    String emp_id = "";

    String emp_name = "";
    String user_id = "";
    String ban = "";
    String str_DES = "";
    String job_pos = "";

    String selected_month_full = "";
    String year_full = "";
    String selected_date = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    
    String adv_taken = "";
    String adv_paid = "";
    String sc_adv_paid = "";
    String total_paid = "";
    String sc_adv_all_taken = "";
    String sc_adv_all_paid = "";
    String sc_adv_all_payable = "";

    String showDate = "";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_details);

        selectMonth = findViewById(R.id.select_month_advance);
        selectMonthLay = findViewById(R.id.select_month_advance_lay);

        errorMsgMonth = findViewById(R.id.error_msg_for_no_entry_advance);
        
        reportCard = findViewById(R.id.advance_report_card);

        monthName = findViewById(R.id.month_year_name_advance);

        empName = findViewById(R.id.name_advance);
        id = findViewById(R.id.id_advance);
        band = findViewById(R.id.band_advance);
        strDes = findViewById(R.id.str_des_advance);
        jobPosition = findViewById(R.id.job_pos_advance);

        advTaken = findViewById(R.id.advance_taken);
        advPaid = findViewById(R.id.advance_paid);
        scAdvPaid = findViewById(R.id.schedule_advance_paid);
        totalPaid = findViewById(R.id.total_advance_paid);
        scAdvAllTaken = findViewById(R.id.total_schedule_advance_taken);
        scAdvAllPaid = findViewById(R.id.total_scheduling_advance_paid);
        scAdvAllPayable = findViewById(R.id.sc_adv_paya_tot);

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
            user_id = userInfoLists.get(0).getUserName();
        }

        if (userDesignations.size() != 0) {
            str_DES = userDesignations.get(0).getJsm_name();
            if (str_DES == null) {
                str_DES = "";
            }
            ban = userDesignations.get(0).getDesg_priority();
            if (ban == null) {
                ban = "";
            }
            job_pos = userDesignations.get(0).getDesg_name();
            if (job_pos == null) {
                job_pos = "";
            }
        }

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
                selected_date = "01-"+mon+"-"+yearName;
                String tt = monthName + "-" + year;
                selectMonth.setText(tt);
                selectMonthLay.setHint("Month:");
                errorMsgMonth.setVisibility(View.GONE);

                reportCard.setVisibility(View.GONE);

                showDate = Objects.requireNonNull(selectMonth.getText()).toString();

                getAdvanceData();
            });

        });

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat sss = new SimpleDateFormat("MMM-yy", Locale.ENGLISH);
        selected_date = sss.format(c);
        selected_date = "01-" + selected_date;
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
        selectMonthLay.setHint("Month:");

        showDate = Objects.requireNonNull(selectMonth.getText()).toString();

        reportCard.setVisibility(View.GONE);
        errorMsgMonth.setVisibility(View.GONE);

        getAdvanceData();
    }

    public void getAdvanceData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        adv_taken = "";
        adv_paid = "";
        sc_adv_paid = "";
        total_paid = "";
        sc_adv_all_taken = "";
        sc_adv_all_paid = "";
        sc_adv_all_payable = "";

        String advTakenUrl = api_url_front+"advanceData/getAdvTaken/"+emp_id+"/"+selected_date+"";
        String advPaidUrl = api_url_front+"advanceData/getAdvancePaid/"+emp_id+"/"+selected_date+"";
        String schAdvTakenUrl = api_url_front+"advanceData/getSchAdvTaken/"+emp_id+"/"+selected_date+"";
        String schAdvPaidUrl = api_url_front+"advanceData/getSchAdvPaid/"+emp_id+"/"+selected_date+"";

        RequestQueue requestQueue = Volley.newRequestQueue(AdvanceDetails.this);

        StringRequest schAdvPaidReq = new StringRequest(Request.Method.GET, schAdvPaidUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject advInfo = array.getJSONObject(i);

                        sc_adv_all_paid = advInfo.getString("total_sch_paid")
                                .equals("null") ? "0" : advInfo.getString("total_sch_paid");
                    }
                }
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

        StringRequest schAdvTakReq = new StringRequest(Request.Method.GET, schAdvTakenUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject advInfo = array.getJSONObject(i);

                        sc_adv_all_taken = advInfo.getString("sch_adv_amt")
                                .equals("null") ? "0" : advInfo.getString("sch_adv_amt");
                    }
                }
                requestQueue.add(schAdvPaidReq);
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

        StringRequest advPaidReq = new StringRequest(Request.Method.GET, advPaidUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject advInfo = array.getJSONObject(i);

                        adv_paid = advInfo.getString("month_adv_paid")
                                .equals("null") ? "0" : advInfo.getString("month_adv_paid");
                        sc_adv_paid = advInfo.getString("month_sch_adv_paid")
                                .equals("null") ? "0" : advInfo.getString("month_sch_adv_paid");
                        total_paid = advInfo.getString("total_month_paid")
                                .equals("null") ? "0" : advInfo.getString("total_month_paid");

                    }
                }
                requestQueue.add(schAdvTakReq);
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

        StringRequest advTakReq = new StringRequest(Request.Method.GET, advTakenUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject advInfo = array.getJSONObject(i);

                        adv_taken = advInfo.getString("month_advance")
                                .equals("null") ? "0" : advInfo.getString("month_advance");
                    }
                }

                requestQueue.add(advPaidReq);
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

        requestQueue.add(advTakReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                errorMsgMonth.setVisibility(View.GONE);
                reportCard.setVisibility(View.VISIBLE);

                monthName.setText(Objects.requireNonNull(selectMonth.getText()).toString());

                empName.setText(emp_name);
                id.setText(user_id);
                band.setText(ban);
                strDes.setText(str_DES);
                jobPosition.setText(job_pos);

                advTaken.setText(adv_taken);
                advPaid.setText(adv_paid);
                scAdvPaid.setText(sc_adv_paid);
                totalPaid.setText(total_paid);
                scAdvAllTaken.setText(sc_adv_all_taken);
                scAdvAllPaid.setText(sc_adv_all_paid);

                if (sc_adv_all_taken != null && sc_adv_all_paid != null) {
                    if (!sc_adv_all_taken.isEmpty() && !sc_adv_all_paid.isEmpty()) {
                        int taken = Integer.parseInt(sc_adv_all_taken);
                        int paid = Integer.parseInt(sc_adv_all_paid);
                        int payable = taken - paid;
                        scAdvAllPayable.setText(String.valueOf(payable));
                    }
                    else {
                        scAdvAllPayable.setText("");
                    }
                }
                else {
                    scAdvAllPayable.setText("");
                }
                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AdvanceDetails.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getAdvanceData();
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
            AlertDialog dialog = new AlertDialog.Builder(AdvanceDetails.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getAdvanceData();
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