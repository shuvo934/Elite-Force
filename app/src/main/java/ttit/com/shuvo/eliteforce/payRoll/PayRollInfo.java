package ttit.com.shuvo.eliteforce.payRoll;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.LargeValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.card.MaterialCardView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.basic_model.SalaryMonthList;
import ttit.com.shuvo.eliteforce.payRoll.advance_details.AdvanceDetails;
import ttit.com.shuvo.eliteforce.payRoll.pay_slip.PaySlip;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class PayRollInfo extends AppCompatActivity {

    MaterialCardView paySlip;
    MaterialCardView advance;

    BarChart chart;
    TextView refresh;

    ArrayList<BarEntry> NoOfEmp;
    ArrayList<String> year;

    ArrayList<SalaryMonthList> months;
    ArrayList<SalaryMonthList> salaryMonthLists;

    ArrayList<String> monthName;
    ArrayList<String> salary;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    String emp_id = "";
    String formattedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_roll_info);

        paySlip = findViewById(R.id.pay_slip);
        advance = findViewById(R.id.advance_details);
        refresh = findViewById(R.id.refresh_graph);
        chart = findViewById(R.id.barchart);

        emp_id = userInfoLists.get(0).getEmp_id();

        months = new ArrayList<>();
        salaryMonthLists = new ArrayList<>();

        monthName = new ArrayList<>();
        salary = new ArrayList<>();

        NoOfEmp = new ArrayList<>();
        year = new ArrayList<>();

        chartInit();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat mom = new SimpleDateFormat("MMMM",Locale.ENGLISH);
        String monnn = mom.format(c);

        String text = "Month: "+monnn;
        refresh.setText(text);

        formattedDate = df.format(c);

        Calendar cal =  Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);
        String previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        cal.add(Calendar.MONTH, -1);
        previousMonthYear  = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(cal.getTime());
        previousMonthYear = previousMonthYear.toUpperCase();
        months.add(new SalaryMonthList(previousMonthYear,"0"));
        System.out.println(previousMonthYear);

        paySlip.setOnClickListener(v -> {
            Intent intent = new Intent(PayRollInfo.this, PaySlip.class);
            startActivity(intent);
        });

        advance.setOnClickListener(v -> {
            Intent intent = new Intent(PayRollInfo.this, AdvanceDetails.class);
            startActivity(intent);
        });

        refresh.setOnClickListener(v -> {
            Date c1 = Calendar.getInstance().getTime();

            String formattedYear;
            String monthValue;
            String lastformattedYear;
            String lastdateView;

            SimpleDateFormat df1 = new SimpleDateFormat("yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf = new SimpleDateFormat("MM",Locale.ENGLISH);

            formattedYear = df1.format(c1);
            monthValue = sdf.format(c1);
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
                String yearName;

                if (month == 1) {
                    monthName = "January";
                    mon = "JAN";
                } else if (month == 2) {
                    monthName = "February";
                    mon = "FEB";
                } else if (month == 3) {
                    monthName = "March";
                    mon = "MAR";
                } else if (month == 4) {
                    monthName = "April";
                    mon = "APR";
                } else if (month == 5) {
                    monthName = "May";
                    mon = "MAY";
                } else if (month == 6) {
                    monthName = "June";
                    mon = "JUN";
                } else if (month == 7) {
                    monthName = "July";
                    mon = "JUL";
                } else if (month == 8) {
                    monthName = "August";
                    mon = "AUG";
                } else if (month == 9) {
                    monthName = "September";
                    mon = "SEP";
                } else if (month == 10) {
                    monthName = "October";
                    mon = "OCT";
                } else if (month == 11) {
                    monthName = "November";
                    mon = "NOV";
                } else if (month == 12) {
                    monthName = "December";
                    mon = "DEC";
                }

                yearName  = String.valueOf(year);
                yearName = yearName.substring(yearName.length()-2);


                formattedDate = "15-"+mon+"-"+yearName;
                //selected_date = "01-"+mon+"-"+yearName;
                String tt = "Month: " +monthName;
                refresh.setText(tt);
                SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                Date today1 = null;
                try {
                    today1 = sss.parse(formattedDate);
                }
                catch (ParseException e) {
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
                    formattedDate =  llll+ "-" + mon +"-"+ yearName;

                    months = new ArrayList<>();

                    calendar11.add(Calendar.MONTH, -1);
                    String previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);

                    calendar11.add(Calendar.MONTH, -1);
                    previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);

                    calendar11.add(Calendar.MONTH, -1);
                    previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);

                    calendar11.add(Calendar.MONTH, -1);
                    previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);

                    calendar11.add(Calendar.MONTH, -1);
                    previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);

                    calendar11.add(Calendar.MONTH, -1);
                    previousMonthYear1 = new SimpleDateFormat("MMM-yy", Locale.ENGLISH).format(calendar11.getTime());
                    previousMonthYear1 = previousMonthYear1.toUpperCase();
                    months.add(new SalaryMonthList(previousMonthYear1,"0"));
                    System.out.println(previousMonthYear1);
                }

                getSalaryGraph();
                chart.resetZoom();
                chart.fitScreen();
            });

        });

        getSalaryGraph();
    }

    private void chartInit() {
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);

        chart.setDrawBarShadow(false);
        chart.setDrawGridBackground(false);

        chart.getAxisLeft().setDrawGridLines(true);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setDrawGridLines(false);

        chart.getLegend().setFormToTextSpace(10);
        chart.getLegend().setStackSpace(10);
        chart.getLegend().setYOffset(10);
        chart.setExtraOffsets(0,0,0,20);

        // zoom and touch disabled
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.getAxisRight().setEnabled(false);
        chart.getAxisLeft().setAxisMinimum(0);
        chart.getLegend().setEnabled(false);
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

        RequestQueue requestQueue = Volley.newRequestQueue(PayRollInfo.this);

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
                                .equals("null") ? "" : salaryMonthInfo.getString("net_salary");
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

    private void updateSalaryGraph() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                monthName = new ArrayList<>();
                salary = new ArrayList<>();
                NoOfEmp = new ArrayList<>();

                for (int i = 0; i < salaryMonthLists.size(); i++) {
                    for (int j = 0; j < months.size(); j++) {
                        String month = months.get(j).getMonth();
                        month = month.substring(0, month.length() -3);
                        if (month.equals(salaryMonthLists.get(i).getMonth())) {
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
                    NoOfEmp.add(new BarEntry(i, Float.parseFloat(salary.get(i)),i));
                }


                BarDataSet bardataset = new BarDataSet(NoOfEmp, "Months");
                chart.animateY(1000);

                BarData data1 = new BarData(bardataset);
                bardataset.setColors(ColorTemplate.VORDIPLOM_COLORS);

                bardataset.setBarBorderColor(Color.DKGRAY);
                bardataset.setValueTextSize(11);
                chart.setData(data1);

                chart.getXAxis().setValueFormatter(new MyAxisValueFormatter(monthName));
                chart.getAxisLeft().setValueFormatter(new LargeValueFormatter());
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(PayRollInfo.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getSalaryGraph();
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
            AlertDialog dialog = new AlertDialog.Builder(PayRollInfo.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getSalaryGraph();
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