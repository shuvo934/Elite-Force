package ttit.com.shuvo.eliteforce.leave.leave_status;

import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.applandeo.materialcalendarview.CalendarDay;
import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.CalendarWeekDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.leave.leave_status.model.EmpLeaveDateList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class LeaveStatus extends AppCompatActivity {

    TextInputEditText selectMonth;
    TextInputLayout selectMonthLay;

    CardView statusReport;

    CalendarView calendarView;
    String selected_date = "";
    String selected_date_day = "";
    Calendar startDates;
    Calendar endDates;

    String first_date = "";
    String last_date = "";

    String emp_id = "";
    String emp_name = "";
    String user_id = "";
    String desg = "";
    String dept = "";


    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    ArrayList<EmpLeaveDateList> empLeaveDateLists;
    Logger logger = Logger.getLogger(LeaveStatus.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_status);

        selectMonth = findViewById(R.id.select_month);
        selectMonthLay = findViewById(R.id.select_month_lay);

        statusReport = findViewById(R.id.leave_status_report_card);

        calendarView = findViewById(R.id.calendarView_new);

        emp_id = userInfoLists.get(0).getEmp_id();

        if (!userInfoLists.isEmpty()) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            emp_name = firstname+" "+lastName;
        }

        user_id = userInfoLists.get(0).getUserName();

        if (!userDesignations.isEmpty()) {
            desg = userDesignations.get(0).getJsm_name();
            if (desg == null) {
                desg = "";
            }

            dept = userDesignations.get(0).getDept_name();
            if (dept == null) {
                dept = "";
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

                first_date = "01-"+mon+"-"+yearName;

                SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

                Date today = null;
                try {
                    today = sss.parse(first_date);
                } catch (ParseException e) {
                    logger.log(Level.WARNING,e.getMessage(),e);
                }

                if (today != null) {
                    startDates = Calendar.getInstance();
                    startDates.setTime(today);
                    startDates.set(Calendar.DAY_OF_MONTH,1);
                    startDates.set(Calendar.HOUR_OF_DAY,0);
                    startDates.set(Calendar.MINUTE,0);
                    startDates.set(Calendar.SECOND,0);
                    startDates.set(Calendar.MILLISECOND,0);

                    endDates = Calendar.getInstance();
                    endDates.setTime(today);
                    endDates.set(Calendar.DAY_OF_MONTH,endDates.getActualMaximum(Calendar.DAY_OF_MONTH));
                    endDates.set(Calendar.HOUR_OF_DAY,0);
                    endDates.set(Calendar.MINUTE,0);
                    endDates.set(Calendar.SECOND,0);
                    endDates.set(Calendar.MILLISECOND,0);

                    calendarView.setMinimumDate(startDates);
                    calendarView.setMaximumDate(endDates);
                    calendarView.setFirstDayOfWeek(CalendarWeekDay.SATURDAY);
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
                    last_date =  llll+ "-" + mon +"-"+ yearName;
                }
                String tt = monthName + "-" + year;
                selectMonth.setText(tt);
                selectMonthLay.setHint("Month");

                statusReport.setVisibility(View.GONE);

                getStatus();
            });

        });

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        Date c = calendar.getTime();
        SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        first_date = sss.format(c);

        calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);

        c = calendar.getTime();
        last_date = sss.format(c);

        SimpleDateFormat month_date = new SimpleDateFormat("MMMM",Locale.ENGLISH);
        String month_name = month_date.format(c);
        month_name = month_name.toUpperCase();

        SimpleDateFormat presentYear = new SimpleDateFormat("yyyy",Locale.ENGLISH);
        String yyyy = presentYear.format(c);

        String tt = month_name+"-"+yyyy;
        selectMonth.setText(tt);
        selectMonthLay.setHint("Month");

        statusReport.setVisibility(View.GONE);

        startDates = Calendar.getInstance();
        startDates.set(Calendar.DAY_OF_MONTH,1);
        startDates.set(Calendar.HOUR_OF_DAY,0);
        startDates.set(Calendar.MINUTE,0);
        startDates.set(Calendar.SECOND,0);
        startDates.set(Calendar.MILLISECOND,0);

        endDates = Calendar.getInstance();
        endDates.set(Calendar.DAY_OF_MONTH,endDates.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDates.set(Calendar.HOUR_OF_DAY,0);
        endDates.set(Calendar.MINUTE,0);
        endDates.set(Calendar.SECOND,0);
        endDates.set(Calendar.MILLISECOND,0);

        calendarView.setMinimumDate(startDates);
        calendarView.setMaximumDate(endDates);
        calendarView.setFirstDayOfWeek(CalendarWeekDay.SATURDAY);
        calendarView.setSwipeEnabled(false);

        calendarView.setOnCalendarDayClickListener(calendarDay -> {
            Calendar ccc = calendarDay.getCalendar();

            if (ccc.getTime().getTime() >= startDates.getTime().getTime() && ccc.getTime().getTime() <= endDates.getTime().getTime()) {
                for (int i = 0; i < empLeaveDateLists.size(); i++) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.ENGLISH);
                    String lad_date = empLeaveDateLists.get(i).getD_date();
                    Date date;
                    try {
                        date = dateFormat.parse(lad_date);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                    if (date != null) {
                        if (date.equals(ccc.getTime())) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM, yyyy", Locale.ENGLISH);
                            SimpleDateFormat dayNameFormat = new SimpleDateFormat("EEEE", Locale.ENGLISH);
                            selected_date = simpleDateFormat.format(ccc.getTime());
                            selected_date_day = dayNameFormat.format(ccc.getTime());

                            int current_bal = Integer.parseInt(empLeaveDateLists.get(i).getCurrent_balance());
                            String leave_name = empLeaveDateLists.get(i).getLeave_name();
                            String up_come = empLeaveDateLists.get(i).getUpcoming();

                            showBottomSheetDialog(leave_name,current_bal,up_come);
                            break;
                        }
                    }
                }
            }
        });

        getStatus();
    }

    public void getStatus() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        empLeaveDateLists = new ArrayList<>();

        String url = api_url_front+"leave/getEmpLeaveDate?first_date="+first_date+"&last_date="+last_date+"&emp_id="+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(LeaveStatus.this);

        StringRequest blReq = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject blInfo = array.getJSONObject(i);

                        String d_date = blInfo.getString("d_date")
                                .equals("null") ? "" : blInfo.getString("d_date");
                        String date_day = blInfo.getString("date_day")
                                .equals("null") ? "" : blInfo.getString("date_day");
                        String leave = blInfo.getString("leave")
                                .equals("null") ? "" : blInfo.getString("leave");
                        String short_code = blInfo.getString("short_code")
                                .equals("null") ? "" : blInfo.getString("short_code");
                        String bal = blInfo.getString("bal")
                                .equals("null") ? "0" : blInfo.getString("bal");
                        String upcoming = blInfo.getString("upcoming")
                                .equals("null") ? "0" : blInfo.getString("upcoming");

                        empLeaveDateLists.add(new EmpLeaveDateList(d_date,date_day,leave,short_code,bal,upcoming));
                    }
                }
                connected = true;
                updateLay();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                connected = false;
                updateLay();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            conn = false;
            connected = false;
            updateLay();
        });

        requestQueue.add(blReq);
    }

    private void updateLay() {
        if (conn) {
            if (connected) {
                statusReport.setVisibility(View.VISIBLE);
                List<CalendarDay> events = new ArrayList<>();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.ENGLISH);
                Handler handler = new Handler();
                handler.postDelayed(() -> {
                    if (!empLeaveDateLists.isEmpty()) {
                        for (int i = 0; i < empLeaveDateLists.size(); i++) {
                            String lad_date = empLeaveDateLists.get(i).getD_date();
                            Date date;
                            try {
                                date = dateFormat.parse(lad_date);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                            if (date != null) {
                                Calendar cc = Calendar.getInstance();
                                cc.setTime(date);
                                CalendarDay calendarDay = new CalendarDay(cc);
                                calendarDay.setImageResource(R.drawable.leave_noc);
                                calendarDay.setBackgroundDrawable(AppCompatResources.getDrawable(this,R.drawable.calendar_event_background));
                                calendarDay.setLabelColor(R.color.elite_red);
                                events.add(calendarDay);
                            }
                        }
                    }

                    Date today = null;
                    try {
                        today = dateFormat.parse(first_date);
                    } catch (ParseException e) {
                        logger.log(Level.WARNING,e.getMessage(),e);
                    }

                    assert today != null;

                    Calendar testStartdates = Calendar.getInstance();
                    testStartdates.setTime(today);
                    testStartdates.set(Calendar.DAY_OF_MONTH,1);

                    Calendar testEndDates = Calendar.getInstance();
                    testEndDates.setTime(today);
                    testEndDates.set(Calendar.DAY_OF_MONTH,testEndDates.getActualMaximum(Calendar.DAY_OF_MONTH));

                    SimpleDateFormat dayNameFormat3 = new SimpleDateFormat("EEE", Locale.ENGLISH);
                    do {
                        String text;
                        Calendar ddc = Calendar.getInstance();
                        if (testStartdates.getTime().equals(startDates.getTime())) {
                            text = dayNameFormat3.format(testStartdates.getTime());
                            if (text.contains("Fri")) {
                                ddc.setTime(testStartdates.getTime());
                                CalendarDay calendarDay = new CalendarDay(ddc);
                                calendarDay.setLabelColor(R.color.elite_red);
                                events.add(calendarDay);
                            }
                        }

                        testStartdates.add(Calendar.DAY_OF_MONTH,1);
                        text = dayNameFormat3.format(testStartdates.getTime());
                        if (text.contains("Fri")) {
                            ddc.setTime(testStartdates.getTime());
                            CalendarDay calendarDay = new CalendarDay(ddc);
                            calendarDay.setLabelColor(R.color.elite_red);
                            System.out.println(text + ": " + testStartdates.getTime());
                            events.add(calendarDay);
                        }
                    }
                    while (!testStartdates.getTime().equals(testEndDates.getTime()));

                    calendarView.setCalendarDays(events);

                    try {
                        calendarView.setDate(startDates);
                    } catch (OutOfDateRangeException e) {
                        throw new RuntimeException(e);
                    }
                    waitProgress.dismiss();

                },1000);
            }
            else {
                waitProgress.dismiss();
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
            waitProgress.dismiss();
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

    private void showBottomSheetDialog(String  lv_name, int cur_bal,String upcoming) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(LeaveStatus.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_leave_status);
        TextView today_date = bottomSheetDialog.findViewById(R.id.leave_date_for_emp_in_status);
        TextView nameType = bottomSheetDialog.findViewById(R.id.leave_type_in_leave_status);
        TextView bf_lv = bottomSheetDialog.findViewById(R.id.before_leave_bal_in_leave_status);
        TextView cr_lv = bottomSheetDialog.findViewById(R.id.current_leave_balance_in_leave_staus);
        LinearLayout con_not_happ = bottomSheetDialog.findViewById(R.id.consumption_not_happ_layout);
        LinearLayout con_happ = bottomSheetDialog.findViewById(R.id.consumption_happ_layout);

        assert nameType != null;
        assert today_date != null;
        assert bf_lv != null;
        assert cr_lv != null;
        assert con_not_happ != null;
        assert con_happ != null;

        String dd = selected_date+"\n"+selected_date_day;
        today_date.setText(dd);
        nameType.setText(lv_name);
        if (upcoming.equals("1")) {
            bf_lv.setText(String.valueOf(cur_bal));
            cr_lv.setText(String.valueOf(cur_bal));
            con_not_happ.setVisibility(View.VISIBLE);
            con_happ.setVisibility(View.GONE);
        }
        else {
            int prev_bal = cur_bal + 1;
            bf_lv.setText(String.valueOf(prev_bal));
            cr_lv.setText(String.valueOf(cur_bal));
            con_not_happ.setVisibility(View.GONE);
            con_happ.setVisibility(View.VISIBLE);
        }

        bottomSheetDialog.show();
    }
}