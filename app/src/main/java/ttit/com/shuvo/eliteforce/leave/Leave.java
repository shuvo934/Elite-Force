package ttit.com.shuvo.eliteforce.leave;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
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
import ttit.com.shuvo.eliteforce.leave.leave_application.LeaveApplication;
import ttit.com.shuvo.eliteforce.leave.leave_balance.LeaveBalance;
import ttit.com.shuvo.eliteforce.leave.leave_status.LeaveStatus;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class Leave extends AppCompatActivity {

    MaterialCardView leaveBalance;
    MaterialCardView leaveApplication;
    MaterialCardView leaveStatus;

    TextView refresh;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";
    String leaveDate = "";

    ArrayList<BarEntry> balance;
    ArrayList<BarEntry> earn;
    ArrayList<String> shortCode;

    BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave);

        leaveBalance = findViewById(R.id.leave_balance_sheet);
        leaveApplication = findViewById(R.id.leave_application);
        leaveStatus = findViewById(R.id.leave_status);

        chart = findViewById(R.id.multi_bar_chart);

        refresh = findViewById(R.id.refresh_graph_leave);

        emp_id = userInfoLists.get(0).getEmp_id();

        balance = new ArrayList<>();
        earn = new ArrayList<>();
        shortCode = new ArrayList<>();

        chartInit();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        SimpleDateFormat mmm = new SimpleDateFormat("MMMM", Locale.ENGLISH);

        String monnnn = mmm.format(c);

        String tt = "Month: "+ monnnn;
        refresh.setText(tt);

        leaveDate = df.format(c);

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

                leaveDate = "01-"+mon+"-"+yearName;
                //selected_date = "01-"+mon+"-"+yearName;
                String mt = "Month: "+monthName;
                refresh.setText(mt);

                SimpleDateFormat sss = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

                Date today1 = null;
                try {
                    today1 = sss.parse(leaveDate);
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
                    leaveDate =  llll+ "-" + mon +"-"+ yearName;
                }
                System.out.println(leaveDate);

                getLeaveGraph();
            });

        });

        leaveBalance.setOnClickListener(v -> {
            Intent intent = new Intent(Leave.this, LeaveBalance.class);
            startActivity(intent);
        });

        leaveApplication.setOnClickListener(v -> {
                Intent intent = new Intent(Leave.this, LeaveApplication.class);
                startActivity(intent);
        });

        leaveStatus.setOnClickListener(v -> {
            Intent intent = new Intent(Leave.this, LeaveStatus.class);
            startActivity(intent);
        });

        getLeaveGraph();
    }

    private void chartInit() {
        chart.getDescription().setEnabled(false);
        chart.setPinchZoom(false);
        chart.getAxisLeft().setDrawGridLines(true);
        chart.getAxisLeft().setAxisMinimum(0);

//        chart.getLegend().setFormToTextSpace(10);
        chart.getLegend().setStackSpace(20);
        chart.getLegend().setYOffset(10);
        chart.getLegend().setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        chart.setExtraOffsets(0,0,0,20);

        // zoom and touch disabled
        chart.setScaleEnabled(false);
        chart.setTouchEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);

        chart.getAxisRight().setEnabled(false);
    }

    public void getLeaveGraph() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        connected = false;
        conn = false;

        balance = new ArrayList<>();
        earn = new ArrayList<>();
        shortCode = new ArrayList<>();

        BarData data = new BarData();
        chart.setData(data);
        chart.getData().clearValues();
        chart.notifyDataSetChanged();
        chart.clear();
        chart.invalidate();
        chart.fitScreen();

        String leaveDataUrl = api_url_front+"dashboard/getLeaveData/"+emp_id+"/"+leaveDate+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Leave.this);

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

    private void updateLeaveGraph() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (balance.size() == 0 || earn.size() == 0 || shortCode.size() == 0) {
                    // do nothing
                    System.out.println(balance.size());
                } else {
                    XAxis xAxis = chart.getXAxis();
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

                    BarData data = new BarData(set1, set2);
                    data.setValueTextSize(12);
                    data.setBarWidth(barWidth); // set the width of each bar
                    chart.animateY(1000);
                    chart.setData(data);
                    chart.groupBars(0, groupSpace, barSpace); // perform the "explicit" grouping
                    chart.invalidate();

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
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Leave.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    getLeaveGraph();
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
            AlertDialog dialog = new AlertDialog.Builder(Leave.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                getLeaveGraph();
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