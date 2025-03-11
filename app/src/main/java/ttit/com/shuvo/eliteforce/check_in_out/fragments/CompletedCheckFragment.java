package ttit.com.shuvo.eliteforce.check_in_out.fragments;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.dewinjm.monthyearpicker.MonthFormat;
import com.github.dewinjm.monthyearpicker.MonthYearPickerDialogFragment;

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
import ttit.com.shuvo.eliteforce.check_in_out.adapters.CompletedCheckAdapter;
import ttit.com.shuvo.eliteforce.check_in_out.arraylists.CompletedCheckList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class CompletedCheckFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    RecyclerView compCheckView;
    RecyclerView.LayoutManager layoutManager;
    CompletedCheckAdapter completedCheckAdapter;

    ArrayList<CompletedCheckList> completedCheckLists;

    CardView cardView;
    TextView monthSelection;

    String emp_id;
    String lastDate = "";
    String beginDate = "";

    public CompletedCheckFragment() {
        // Required empty public constructor
    }

    Context mContext;

    AppCompatActivity activity;

    public CompletedCheckFragment(Context context) {
        this.mContext = context;
    }

    public static CompletedCheckFragment newInstance(String param1, String param2) {
        CompletedCheckFragment fragment = new CompletedCheckFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_check, container, false);
        activity = (AppCompatActivity) view.getContext();

        compCheckView = view.findViewById(R.id.completed_check_in_list);
        cardView = view.findViewById(R.id.month_selection_card_com_ci);
        monthSelection = view.findViewById(R.id.month_selection_for_check_in);

        completedCheckLists = new ArrayList<>();

        compCheckView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        compCheckView.setLayoutManager(layoutManager);

        emp_id = userInfoLists.get(0).getEmp_id();

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        lastDate = df.format(c);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy", Locale.ENGLISH);

        beginDate = sdf.format(c);
        beginDate = "01-"+beginDate;

        SimpleDateFormat mon = new SimpleDateFormat("MMMM",Locale.ENGLISH);

        String mmm = mon.format(c);

        String text = "Month: "+mmm;
        monthSelection.setText(text);

        cardView.setOnClickListener(v -> {

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



            dialogFragment.show(activity.getSupportFragmentManager(), null);

            dialogFragment.setOnDateSetListener((year, monthOfYear) -> {
                System.out.println(year);
                System.out.println(monthOfYear);

                int month = monthOfYear + 1;
                String monthName = "";
                String mon1 = "";
                String yearName = "";

                if (month == 1) {
                    monthName = "January";
                    mon1 = "JAN";
                } else if (month == 2) {
                    monthName = "February";
                    mon1 = "FEB";
                } else if (month == 3) {
                    monthName = "March";
                    mon1 = "MAR";
                } else if (month == 4) {
                    monthName = "April";
                    mon1 = "APR";
                } else if (month == 5) {
                    monthName = "May";
                    mon1 = "MAY";
                } else if (month == 6) {
                    monthName = "June";
                    mon1 = "JUN";
                } else if (month == 7) {
                    monthName = "July";
                    mon1 = "JUL";
                } else if (month == 8) {
                    monthName = "August";
                    mon1 = "AUG";
                } else if (month == 9) {
                    monthName = "September";
                    mon1 = "SEP";
                } else if (month == 10) {
                    monthName = "October";
                    mon1 = "OCT";
                } else if (month == 11) {
                    monthName = "November";
                    mon1 = "NOV";
                } else if (month == 12) {
                    monthName = "December";
                    mon1 = "DEC";
                }

                yearName  = String.valueOf(year);
                yearName = yearName.substring(yearName.length()-2);


                beginDate = "01-"+ mon1 +"-"+yearName;
                //selected_date = "01-"+mon+"-"+yearName;
                String mText = "Month: "+ monthName;
                monthSelection.setText(mText);

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

                getCheckList();
            });

        });

        getCheckList();
        return view;
    }

    public void getCheckList() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);

        conn = false;
        connected = false;
        completedCheckLists = new ArrayList<>();

        String url = api_url_front+"checkInOut/getCompletedCheckIn?p_emp_id="+emp_id+"&first_date="+beginDate+"&last_date="+lastDate;

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empTranscriptFirst_1 = array.getJSONObject(i);

                        String cior_register_no = empTranscriptFirst_1.getString("cior_register_no")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_register_no");

                        String cior_company_info = empTranscriptFirst_1.getString("cior_company_info")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_company_info");

                        String check_date = empTranscriptFirst_1.getString("check_date")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("check_date");

                        String cior_company_loc_info = empTranscriptFirst_1.getString("cior_company_loc_info")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_company_loc_info");
                        cior_company_loc_info = transformText(cior_company_loc_info);

                        String cior_lat_val = empTranscriptFirst_1.getString("cior_lat_val")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_lat_val");

                        String cior_long_val = empTranscriptFirst_1.getString("cior_long_val")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_long_val");

                        String in_time = empTranscriptFirst_1.getString("in_time")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("in_time");

                        String cior_in_remarks = empTranscriptFirst_1.getString("cior_in_remarks")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_in_remarks");

                        String cior_out_remarks = empTranscriptFirst_1.getString("cior_out_remarks")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_out_remarks");

                        String out_time = empTranscriptFirst_1.getString("out_time")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("out_time");

                        String cior_blob = empTranscriptFirst_1.optString("cior_blob");

                        Bitmap bitmap = null;
                        if (!cior_blob.equals("null") && !cior_blob.isEmpty()) {
                            byte[] decodedString = Base64.decode(cior_blob,Base64.DEFAULT);
                            bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                        }

                        completedCheckLists.add(new CompletedCheckList(cior_register_no,cior_company_info,
                                check_date,cior_company_loc_info,cior_lat_val,cior_long_val,in_time,cior_in_remarks,cior_out_remarks,out_time,bitmap));
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


        requestQueue.add(request);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {

                completedCheckAdapter = new CompletedCheckAdapter(completedCheckLists, mContext);
                compCheckView.setAdapter(completedCheckAdapter);

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getCheckList();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    ((Activity)mContext).finish();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getCheckList();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                ((Activity)mContext).finish();
                dialog.dismiss();
            });
        }
    }

    //    --------------------------Transforming Bangla Text-----------------------------
    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }
}