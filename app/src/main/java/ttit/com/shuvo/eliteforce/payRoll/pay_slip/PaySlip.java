package ttit.com.shuvo.eliteforce.payRoll.pay_slip;

import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.payRoll.pay_slip.dialogue_box.MonthSelectDialogue;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class PaySlip extends AppCompatActivity {

    public static TextInputEditText selectMonth;
    public static TextInputLayout selectMonthLay;

    public static TextView errorMsgMonth;

    MaterialButton download;

    CardView reportCard;

    TextView monthName;

    TextInputEditText empName;
    TextInputEditText id;
    TextInputEditText band;
    TextInputEditText strDes;
    TextInputEditText jobPosition;
    TextInputEditText division;
    TextInputEditText department;
    TextInputEditText officeLoc;
    TextInputEditText officeExt;
    TextInputEditText email;
    TextInputEditText mobile;
    TextInputEditText addCharge;

    TextView salaryData;
    LinearLayout salaryLay;

    TextView basic;
    TextView houseRent;
    TextView food;
    TextView conveyance;
    TextView grossSalary;
    TextView overTime;
    TextView attBonus;
    TextView otherAllow;
    TextView totalPerquisite;

    TextView providentFund;
    TextView leavePayText;
    TextView leavePay;
    TextView absentDeducText;
    TextView absentDeduc;
    TextView loanDeducOneTime;
    TextView loanDeducSched;
    TextView loanDeducProFun;
    TextView taxDeduc;
    TextView lunchDeduc;
    TextView stamp;
    TextView otherDeduc;
    TextView loanDeducBoard;
    TextView totalDeduc;

    TextView netPayment;
    TextView inWord;

    TextView accountName;
    TextView accountNo;
    TextView bankName;

    String data_available_count = "";

    public static String select_month_id = "";
    String emp_id = "";

    String emp_name = "";
    String user_id = "";
    String ban = "";
    String str_DES = "";
    String job_pos = "";
    String div = "";
    String dep = "";

    String off_loc = "";
    String off_ext = "";
    String email_name = "";
    String mobile_no = "";
    String charge = "";

    String basic_salary = "";
    String hou_rent = "";
    String food_cost = "";
    String conveyance_cost = "";
    String gross_salary = "";
    String over_time = "";
    String att_bon = "";
    String other_allow = "";
    String total_perqqq = "";

    String prov_fun = "";
    String leave_pay = "";
    String leave_pay_days = "";
    String abs_ded_days = "";
    String abs_ded = "";
    String loan_ded_one = "";
    String loan_ded_sche = "";
    String loan_ded_pf = "";
    String tax_ded = "";
    String lun_ded = "";
    String stam = "";
    String oth_ded = "";
    String loan_ded_boa = "";
    String total_ded = "";

    String total = "";
    String total_word = "";

    String acc_name = "";
    String acc_no = "";
    String bank = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    private Boolean downConn = false;

    private ProgressDialog pDialog;

    String URL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_slip);

        select_month_id = "";

        selectMonth = findViewById(R.id.select_month_pay_slip);
        selectMonthLay = findViewById(R.id.select_month_pay_slip_lay);

        errorMsgMonth = findViewById(R.id.error_msg_for_no_entry_pay_slip);

        download = findViewById(R.id.download_pay_slip_report);

        reportCard = findViewById(R.id.pay_slip_report_card);

        monthName = findViewById(R.id.month_year_name);

        empName = findViewById(R.id.name_pay_slip);
        id = findViewById(R.id.id_pay_slip);
        band = findViewById(R.id.band_pay_slip);
        strDes = findViewById(R.id.str_des_pay_slip);
        jobPosition = findViewById(R.id.job_pos_pay_slip);
        division = findViewById(R.id.division_pay_slip);
        department = findViewById(R.id.departmnet_pay_slip);
        officeLoc = findViewById(R.id.office_loca_pay_slip);
        officeExt = findViewById(R.id.office_extenstion_pay_slip);
        email = findViewById(R.id.email_pay_slip);
        mobile = findViewById(R.id.mobile_pay_slip);
        addCharge = findViewById(R.id.additional_charge_pay_slip);

        salaryData = findViewById(R.id.salary_data_msg);
        salaryLay = findViewById(R.id.salary_info_pay_slip_lay);

        basic = findViewById(R.id.basic_salary_pay_slip);
        houseRent = findViewById(R.id.house_rent_pay_slip);
        food = findViewById(R.id.food_pay_slip);
        conveyance = findViewById(R.id.conveyance_pay_slip);
        grossSalary = findViewById(R.id.gross_salary_pay_slip);
        overTime = findViewById(R.id.over_time_pay_slip);
        attBonus = findViewById(R.id.attendance_bonus_pay_slip);
        otherAllow = findViewById(R.id.other_allow_payment_pay_slip);
        totalPerquisite = findViewById(R.id.total_perquisite_pay_slip);

        providentFund = findViewById(R.id.provident_fund_pay_slip);
        leavePayText = findViewById(R.id.leave_without_pay_text);
        leavePay = findViewById(R.id.leave_without_pay_slip);
        absentDeducText = findViewById(R.id.absent_deduc_text);
        absentDeduc = findViewById(R.id.absent_deduc_pay_slip);
        loanDeducOneTime = findViewById(R.id.loan_deduc_one_time_pay_slip);
        loanDeducSched = findViewById(R.id.loan_deduc_shedule_pay_slip);
        loanDeducProFun = findViewById(R.id.loan_deduc_pf_pay_slip);
        taxDeduc = findViewById(R.id.tax_deduction_pay_slip);
        lunchDeduc = findViewById(R.id.lunch_deduc_pay_slip);
        stamp = findViewById(R.id.stamp_pay_slip);
        otherDeduc = findViewById(R.id.other_deduct_pay_slip);
        loanDeducBoard = findViewById(R.id.loan_deduc_from_board_pay_slip);
        totalDeduc = findViewById(R.id.total_deduction_pay_slip);

        netPayment = findViewById(R.id.net_payment_pay_slip);
        inWord = findViewById(R.id.in_word_pay_slip);

        accountName = findViewById(R.id.account_name_pay_slip);
        accountNo = findViewById(R.id.account_number_pay_slip);
        bankName = findViewById(R.id.bank_name_pay_slip);

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
            div = userDesignations.get(0).getDiv_name();
            if (div == null) {
                div = "";
            }
            dep = userDesignations.get(0).getDept_name();
            if (dep == null) {
                dep = "";
            }
        }

        email_name = userInfoLists.get(0).getEmail();

        selectMonth.setOnClickListener(v -> {
            MonthSelectDialogue monthSelectDialogue = new MonthSelectDialogue(PaySlip.this);
            monthSelectDialogue.show(getSupportFragmentManager(),"MONTH");
        });

        selectMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                errorMsgMonth.setVisibility(View.GONE);
                download.setVisibility(View.GONE);
                reportCard.setVisibility(View.GONE);
                getPaySlipData();
            }
        });

        download.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(PaySlip.this);
            builder.setTitle("Download Pay Slip!")
                    .setMessage("Do you want to download this pay slip?")
                    .setPositiveButton("YES", (dialog, which) -> new DownloadPDF().execute())
                    .setNegativeButton("NO", (dialog, which) -> {

                    });
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    @Override
    public void onBackPressed() {
        select_month_id = "";
        finish();
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
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
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

                Download(URL, "Pay Slip"+" "+select_month_id);

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
                AlertDialog dialog = new AlertDialog.Builder(PaySlip.this)
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

    public void getPaySlipData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        basic_salary = "";
        hou_rent = "";
        food_cost = "";
        conveyance_cost = "";
        gross_salary = "";
        over_time = "";
        att_bon = "";
        other_allow = "";
        total_perqqq = "";

        prov_fun = "";
        leave_pay = "";
        leave_pay_days = "";
        abs_ded_days = "";
        abs_ded = "";
        loan_ded_one = "";
        loan_ded_sche = "";
        loan_ded_pf = "";
        tax_ded = "";
        lun_ded = "";
        stam = "";
        oth_ded = "";
        loan_ded_boa = "";
        total_ded = "";

        total = "";
        total_word = "";

        acc_name = "";
        acc_no = "";
        bank = "";

        String officeExtraUrl = api_url_front+"paySlip/getOfficeExtra/"+emp_id+"";
        String dataCCUrl = api_url_front+"paySlip/getCount/"+emp_id+"/"+select_month_id+"";
        String paySlipDataUrl = api_url_front+"paySlip/getPaySlipDetails/"+emp_id+"/"+select_month_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(PaySlip.this);

        StringRequest paySlipReq = new StringRequest(Request.Method.GET, paySlipDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject paySlipInfo = array.getJSONObject(i);

                        att_bon = paySlipInfo.getString("sd_attd_bonus_amt")
                                .equals("null") ? "" : paySlipInfo.getString("sd_attd_bonus_amt");
                        acc_no = paySlipInfo.getString("ebam_acc_number")
                                .equals("null") ? "" : paySlipInfo.getString("ebam_acc_number");
                        acc_name = paySlipInfo.getString("ebam_bank_account_name")
                                .equals("null") ? "" : paySlipInfo.getString("ebam_bank_account_name");
                        bank = paySlipInfo.getString("ebam_bank_name")
                                .equals("null") ? "" : paySlipInfo.getString("ebam_bank_name");
                        basic_salary = paySlipInfo.getString("sd_basic")
                                .equals("null") ? "" : paySlipInfo.getString("sd_basic");
                        hou_rent = paySlipInfo.getString("sd_hr")
                                .equals("null") ? "" : paySlipInfo.getString("sd_hr");
                        food_cost = paySlipInfo.getString("sd_food_subsidy_amt")
                                .equals("null") ? "" : paySlipInfo.getString("sd_food_subsidy_amt");
                        abs_ded_days = paySlipInfo.getString("absent_days")
                                .equals("null") ? "" : paySlipInfo.getString("absent_days");
                        abs_ded = paySlipInfo.getString("sd_absent_amt")
                                .equals("null") ? "" : paySlipInfo.getString("sd_absent_amt");
                        prov_fun = paySlipInfo.getString("sd_pf")
                                .equals("null") ? "" : paySlipInfo.getString("sd_pf");
                        tax_ded = paySlipInfo.getString("sd_tax")
                                .equals("null") ? "" : paySlipInfo.getString("sd_tax");
                        oth_ded = paySlipInfo.getString("sd_oth_deduct")
                                .equals("null") ? "" : paySlipInfo.getString("sd_oth_deduct");
                        loan_ded_one = paySlipInfo.getString("sd_advance_deduct")
                                .equals("null") ? "" : paySlipInfo.getString("sd_advance_deduct");
                        conveyance_cost = paySlipInfo.getString("sd_ta")
                                .equals("null") ? "" : paySlipInfo.getString("sd_ta");
                        gross_salary = paySlipInfo.getString("sd_gross_sal")
                                .equals("null") ? "" : paySlipInfo.getString("sd_gross_sal");
                        stam = paySlipInfo.getString("sd_stamp")
                                .equals("null") ? "" : paySlipInfo.getString("sd_stamp");
                        leave_pay_days = paySlipInfo.getString("lwpaydays")
                                .equals("null") ? "" : paySlipInfo.getString("lwpaydays");
                        leave_pay = paySlipInfo.getString("sd_lwpay_amt")
                                .equals("null") ? "" : paySlipInfo.getString("sd_lwpay_amt");
                        over_time = paySlipInfo.getString("total_ot_amt")
                                .equals("null") ? "" : paySlipInfo.getString("total_ot_amt");
                        loan_ded_boa = paySlipInfo.getString("sd_md_advance_deduct")
                                .equals("null") ? "" : paySlipInfo.getString("sd_md_advance_deduct");
                        loan_ded_sche = paySlipInfo.getString("sd_sch_advance_deduct")
                                .equals("null") ? "" : paySlipInfo.getString("sd_sch_advance_deduct");
                        loan_ded_pf = paySlipInfo.getString("sd_pf_loan_deduct")
                                .equals("null") ? "" : paySlipInfo.getString("sd_pf_loan_deduct");
                        lun_ded = paySlipInfo.getString("sd_lunch_deduct")
                                .equals("null") ? "" : paySlipInfo.getString("sd_lunch_deduct");
                        other_allow = paySlipInfo.getString("other_allowances")
                                .equals("null") ? "" : paySlipInfo.getString("other_allowances");
                        total_perqqq = paySlipInfo.getString("total_salary")
                                .equals("null") ? "" : paySlipInfo.getString("total_salary");
                        total_ded = paySlipInfo.getString("total_deduction")
                                .equals("null") ? "" : paySlipInfo.getString("total_deduction");
                        total = paySlipInfo.getString("net_salary")
                                .equals("null") ? "" : paySlipInfo.getString("net_salary");
                        total_word = paySlipInfo.getString("salary_in_word")
                                .equals("null") ? "" : paySlipInfo.getString("salary_in_word");

                        if(!total_word.isEmpty()) {
                            int index = total_word.indexOf("Taka");
                            total_word = total_word.substring(index);
                        }
                    }
                }

                URL = "http://103.56.208.123:7778/reports/rwservlet?hrselite+report=D:\\ELITE_FORCE\\Reports\\PAY_SLIP.rep+EMPID="+emp_id+"+MMONTH='"+select_month_id+"'";
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

        StringRequest countReq = new StringRequest(Request.Method.GET, dataCCUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject countInfo = array.getJSONObject(i);

                        data_available_count = countInfo.getString("data_cc");
                    }
                }

                requestQueue.add(paySlipReq);
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

        StringRequest offExtReq = new StringRequest(Request.Method.GET, officeExtraUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject offExtInfo = array.getJSONObject(i);

                        off_loc = offExtInfo.getString("office")
                                .equals("null") ? "" : offExtInfo.getString("office");
                        off_ext = offExtInfo.getString("job_pabx_corporate")
                                .equals("null") ? "" : offExtInfo.getString("job_pabx_corporate");
                        mobile_no = offExtInfo.getString("mobile")
                                .equals("null") ? "" : offExtInfo.getString("mobile");
                        charge = offExtInfo.getString("job_ad_charge")
                                .equals("null") ? "" : offExtInfo.getString("job_ad_charge");
                    }
                }

                requestQueue.add(countReq);
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

        requestQueue.add(offExtReq);
    }

    private void updateInterface() {
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
                division.setText(div);
                department.setText(dep);

                String tt = "Not Applicable";

                if (email_name != null) {
                    if (!email_name.isEmpty()) {
                        email.setText(email_name);
                    }
                    else {
                        email.setText(tt);
                    }
                }
                else {
                    email.setText(tt);
                }


                if (off_loc != null) {
                    if (!off_loc.isEmpty()) {
                        officeLoc.setText(off_loc);
                    } else {
                        officeLoc.setText(tt);
                    }
                } else {
                    officeLoc.setText(tt);
                }

                if (off_ext != null) {
                    if (!off_ext.isEmpty()) {
                        officeExt.setText(off_ext);
                    } else {
                        officeExt.setText(tt);
                    }
                } else {
                    officeExt.setText(tt);
                }

                if (mobile_no != null) {
                    if (!mobile_no.isEmpty()) {
                        mobile.setText(mobile_no);
                    } else {
                        mobile.setText(tt);
                    }
                } else {
                    mobile.setText(tt);
                }

                if (charge != null) {
                    if (!charge.isEmpty()) {
                        addCharge.setText(charge);
                    } else {
                        addCharge.setText(tt);
                    }
                } else {
                    addCharge.setText(tt);
                }

                if (data_available_count.equals("1")) {
                    salaryLay.setVisibility(View.VISIBLE);
                    salaryData.setVisibility(View.GONE);
                    download.setVisibility(View.VISIBLE);
                } else {
                    salaryLay.setVisibility(View.GONE);
                    salaryData.setVisibility(View.VISIBLE);
                    download.setVisibility(View.GONE);
                }
                basic.setText(basic_salary);
                houseRent.setText(hou_rent);
                food.setText(food_cost);
                conveyance.setText(conveyance_cost);
                grossSalary.setText(gross_salary);
                overTime.setText(over_time);
                attBonus.setText(att_bon);
                otherAllow.setText(other_allow);
                totalPerquisite.setText(total_perqqq);

                providentFund.setText(prov_fun);
                String lpt = leavePayText.getText().toString() + leave_pay_days;
                leavePayText.setText(lpt);
                leavePay.setText(leave_pay);
                String adt = absentDeducText.getText().toString() + abs_ded_days;
                absentDeducText.setText(adt);
                absentDeduc.setText(abs_ded);
                loanDeducOneTime.setText(loan_ded_one);
                loanDeducSched.setText(loan_ded_sche);
                loanDeducProFun.setText(loan_ded_pf);
                taxDeduc.setText(tax_ded);
                lunchDeduc.setText(lun_ded);
                stamp.setText(stam);
                otherDeduc.setText(oth_ded);
                loanDeducBoard.setText(loan_ded_boa);
                totalDeduc.setText(total_ded);

                netPayment.setText(total);
                inWord.setText(total_word);

                accountName.setText(acc_name);
                accountNo.setText(acc_no);
                bankName.setText(bank);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(PaySlip.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getPaySlipData();
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
            AlertDialog dialog = new AlertDialog.Builder(PaySlip.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getPaySlipData();
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