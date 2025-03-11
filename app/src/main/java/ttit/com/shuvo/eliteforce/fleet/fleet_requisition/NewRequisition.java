package ttit.com.shuvo.eliteforce.fleet.fleet_requisition;

import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.basic_model.TwoItemLists;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class NewRequisition extends AppCompatActivity {

    ScrollView fullLayout;

    AmazingSpinner purposeOfReq;
    ArrayList<TwoItemLists> purposeLists;
    String selected_purpose_id = "";
    String selected_purpose_name = "";
    TextView errPurpose;

    TextInputEditText requestDate;
    String request_date = "";
    private int mYear, mMonth, mDay;

    AmazingSpinner flDiv;
    String fl_div_id = "";
    String fl_div_name = "";

    AmazingSpinner flDepSpinner;
    String fl_dep_id = "";
    String fl_dep_name = "";

    AmazingSpinner reqEmployee;
    String emp_name = "";
    String emp_id = "";
    String fl_desig_id = "";
    String fl_jsd_id = "";
    String user_name = "";

    TextInputEditText reqEmpMobNo;
    String req_emp_mob_no = "";
    TextView errMob;

    AmazingSpinner fromLocDivSpinner;
    ArrayList<TwoItemLists> divLists;
    String selected_from_loc_div_id = "";
    String selected_from_loc_div_name = "";

    AmazingSpinner fromLocDistSpinner;
    TextInputLayout fLDistSpinnLay;
    ArrayList<TwoItemLists> fldistLists;
    String selected_from_loc_dist_id = "";
    String selected_from_loc_dist_name = "";

    TextView errFromDivDis;

    TextInputLayout fromAreaLay;
    AutoCompleteTextView fromArea;
    ArrayList<TwoItemLists> flThanaLists;
    String from_loc_area = "";
    String selected_from_loc_dd_id = "";
    TextView errFromArea;

    TextInputEditText pickupPoint;
    String pickup_point = "";
    TextView errFromPickUp;

    TextInputEditText leavingDate;
    String selected_leave_date_time = "";
    TextView errFromLvDate;

    AmazingSpinner toLocDivSpinner;
    String selected_to_loc_div_id = "";
    String selected_to_loc_div_name = "";

    AmazingSpinner toLocDistSpinner;
    TextInputLayout tLDistSpinnLay;
    ArrayList<TwoItemLists> tldistLists;
    String selected_to_loc_dist_id = "";
    String selected_to_loc_dist_name = "";

    TextView errToDivDis;

    TextInputLayout toAreaLay;
    AutoCompleteTextView toArea;
    ArrayList<TwoItemLists> tlThanaLists;
    String to_loc_area = "";
    String selected_to_loc_dd_id = "";
    TextView errToArea;

    TextInputEditText dropPoint;
    String drop_point = "";
    TextView errDrpPoint;

    TextInputEditText returnDate;
    String selected_return_date_time = "";
    TextView errReturnDate;

    AmazingSpinner reqFleetType;
    ArrayList<TwoItemLists> reqFleetTypeList;
    String req_fleet_type_id = "";
    String req_fleet_type_name = "";
    TextView errFlType;

    TextInputEditText vehicleQty;
    String vh_qty = "";
    TextView errVhlQty;

    TextInputEditText travelersQty;
    String travelers_qty = "";
    TextView errTrvQty;

    MaterialCardView saveReq;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    boolean selectedFromItems = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_requisition);

        fullLayout = findViewById(R.id.fleet_req_full_layout);

        purposeOfReq = findViewById(R.id.fleet_req_purpose);
        errPurpose = findViewById(R.id.error_of_fleet_req_purpose);
        errPurpose.setVisibility(View.GONE);

        requestDate = findViewById(R.id.fleet_request_date);
        flDiv = findViewById(R.id.fleet_division);
        flDepSpinner = findViewById(R.id.fleet_department);
        reqEmployee = findViewById(R.id.fleet_req_employee);
        reqEmpMobNo = findViewById(R.id.fleet_req_mobile_no);
        errMob = findViewById(R.id.error_of_fleet_request_mob_no);
        errMob.setVisibility(View.GONE);

        fromLocDivSpinner = findViewById(R.id.fleet_area_division_from_loc);
        fromLocDistSpinner = findViewById(R.id.fleet_area_district_from_loc);
        fLDistSpinnLay = findViewById(R.id.spinner_layout_fleet_area_district_from_loc);
        fLDistSpinnLay.setEnabled(false);
        errFromDivDis = findViewById(R.id.error_of_fleet_from_division_district);
        errFromDivDis.setVisibility(View.GONE);

        fromAreaLay = findViewById(R.id.spinner_layout_fleet_area_address_from_loc);
        fromAreaLay.setEnabled(false);
        fromArea = findViewById(R.id.fleet_area_address_from_loc);
        errFromArea = findViewById(R.id.error_of_fleet_from_area_address);
        errFromArea.setVisibility(View.GONE);

        pickupPoint = findViewById(R.id.fleet_pickup_point);
        errFromPickUp = findViewById(R.id.error_of_fleet_from_pickup_point);
        errFromPickUp.setVisibility(View.GONE);

        leavingDate = findViewById(R.id.fleet_leaving_date);
        errFromLvDate = findViewById(R.id.error_of_fleet_leaving_date);
        errFromLvDate.setVisibility(View.GONE);

        toLocDivSpinner = findViewById(R.id.fleet_area_division_to_loc);
        toLocDistSpinner = findViewById(R.id.fleet_area_district_to_loc);
        tLDistSpinnLay = findViewById(R.id.spinner_layout_fleet_area_district_to_loc);
        tLDistSpinnLay.setEnabled(false);
        errToDivDis = findViewById(R.id.error_of_fleet_to_division_district);
        errToDivDis.setVisibility(View.GONE);

        toAreaLay = findViewById(R.id.spinner_layout_fleet_area_address_to_loc);
        toAreaLay.setEnabled(false);
        toArea = findViewById(R.id.fleet_area_address_to_loc);
        errToArea = findViewById(R.id.error_of_fleet_to_area_address);
        errToArea.setVisibility(View.GONE);

        dropPoint = findViewById(R.id.fleet_drop_point);
        errDrpPoint = findViewById(R.id.error_of_fleet_to_drop_point);
        errDrpPoint.setVisibility(View.GONE);

        returnDate = findViewById(R.id.fleet_return_date);
        errReturnDate = findViewById(R.id.error_of_fleet_return_date);
        errReturnDate.setVisibility(View.GONE);

        reqFleetType = findViewById(R.id.req_fleet_type);
        errFlType = findViewById(R.id.error_of_req_fleet_type);
        errFlType.setVisibility(View.GONE);

        vehicleQty = findViewById(R.id.fleet_vehicle_qty);
        errVhlQty = findViewById(R.id.error_of_fleet_vehicle_qty);
        errVhlQty.setVisibility(View.GONE);

        travelersQty = findViewById(R.id.fleet_travelers_qty);
        errTrvQty = findViewById(R.id.error_of_travelers_qty_fleet);
        errTrvQty.setVisibility(View.GONE);

        saveReq = findViewById(R.id.fleet_req_save_button);

        purposeLists = new ArrayList<>();
        divLists = new ArrayList<>();
        fldistLists = new ArrayList<>();
        flThanaLists = new ArrayList<>();
        tldistLists = new ArrayList<>();
        tlThanaLists = new ArrayList<>();
        reqFleetTypeList = new ArrayList<>();

        if (!userInfoLists.isEmpty()) {
            String firstname = userInfoLists.get(0).getUser_fname();
            String lastName = userInfoLists.get(0).getUser_lname();
            if (firstname == null) {
                firstname = "";
            }
            if (lastName == null) {
                lastName = "";
            }
            String empFullName = firstname+" "+lastName;
            emp_name = empFullName;
            reqEmployee.setText(empFullName);
            emp_id = userInfoLists.get(0).getEmp_id();
            user_name = userInfoLists.get(0).getUserName();
            req_emp_mob_no = userInfoLists.get(0).getContact();
        }

        reqEmpMobNo.setText(req_emp_mob_no);

        if (!userDesignations.isEmpty()) {
            fl_div_name = userDesignations.get(0).getDiv_name();
            fl_div_id = userDesignations.get(0).getDiv_id();
            fl_dep_id = userDesignations.get(0).getDept_id();
            fl_dep_name = userDesignations.get(0).getDept_name();
            fl_desig_id = userDesignations.get(0).getDesig_id();
            fl_jsd_id = userDesignations.get(0).getJsd_id();
            if (fl_div_name == null) {
                fl_div_name = "";
            }
            if (fl_dep_name == null) {
                fl_dep_name = "";
            }
            flDiv.setText(fl_div_name);
            flDepSpinner.setText(fl_dep_name);
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        request_date = simpleDateFormat.format(calendar.getTime());
        request_date = request_date.toUpperCase(Locale.ENGLISH);
        requestDate.setText(request_date);

        purposeOfReq.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            selected_purpose_id = "";
            for (int i = 0; i <purposeLists.size(); i++) {
                if (name.equals(purposeLists.get(i).getName())) {
                    selected_purpose_id = purposeLists.get(i).getId();
                    selected_purpose_name = purposeLists.get(i).getName();
                }
            }
            errPurpose.setVisibility(View.GONE);
        });

        requestDate.setOnClickListener(v -> {
            final Calendar c1 = Calendar.getInstance();
            mYear = c1.get(Calendar.YEAR);
            mMonth = c1.get(Calendar.MONTH);
            mDay = c1.get(Calendar.DAY_OF_MONTH);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewRequisition.this, (view, year, month, dayOfMonth) -> {

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
                    request_date = dayOfMonthName + "-" + monthName + "-" + yearName;
                    requestDate.setText(request_date);

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        reqEmpMobNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().startsWith("0")) {
                    if (editable.toString().length() == 11) {
                        errMob.setVisibility(View.GONE);
                    }
                    else {
                        errMob.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    errMob.setVisibility(View.VISIBLE);
                }
            }
        });

        reqEmpMobNo.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        fromLocDivSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            selected_from_loc_div_id = "";
            selected_from_loc_dist_name = "";
            selected_from_loc_dist_id = "";
            selected_from_loc_dd_id = "";
            from_loc_area = "";
            fLDistSpinnLay.setEnabled(false);
            fromLocDistSpinner.setText("");
            fromAreaLay.setEnabled(false);
            fromArea.setText("");

            for (int i = 0; i <divLists.size(); i++) {
                if (name.equals(divLists.get(i).getName())) {
                    selected_from_loc_div_id = divLists.get(i).getId();
                    selected_from_loc_div_name = divLists.get(i).getName();
                }
            }
            errFromDivDis.setVisibility(View.GONE);
            getDistrict(1);
        });

        fromLocDistSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            selected_from_loc_dd_id = "";
            from_loc_area = "";
            fromAreaLay.setEnabled(false);
            fromArea.setText("");
            for (int i = 0; i <fldistLists.size(); i++) {
                if (name.equals(fldistLists.get(i).getName())) {
                    selected_from_loc_dist_id = fldistLists.get(i).getId();
                    selected_from_loc_dist_name = fldistLists.get(i).getName();
                }
            }
            errFromDivDis.setVisibility(View.GONE);
            getArea(1);
        });

        fromArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().isEmpty()) {
                    errFromArea.setVisibility(View.GONE);
                }
            }
        });

        fromArea.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            for (int i = 0; i <flThanaLists.size(); i++) {
                if (name.equals(flThanaLists.get(i).getName())) {
                    from_loc_area = flThanaLists.get(i).getName();
                    selected_from_loc_dd_id = flThanaLists.get(i).getId();
                }
            }

            selectedFromItems = true;
            errFromArea.setVisibility(View.GONE);
            closeKeyBoard();
        });

        fromArea.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String ss = fromArea.getText().toString();
                if (!selectedFromItems) {
                    selected_from_loc_dd_id = "";
                    for (int i = 0; i < flThanaLists.size(); i++) {
                        if (ss.equals(flThanaLists.get(i).getName())) {
                            selected_from_loc_dd_id = flThanaLists.get(i).getId();
                            from_loc_area = flThanaLists.get(i).getName();
                        }
                    }
                    if (selected_from_loc_dd_id.isEmpty()) {
                        if (ss.isEmpty()) {
                            errFromArea.setVisibility(View.VISIBLE);
                            String cmt = "Please Select Area";
                            errFromArea.setText(cmt);
                        }
                        else {
                            errFromArea.setVisibility(View.VISIBLE);
                            String cmt = "Invalid Area";
                            errFromArea.setText(cmt);
                        }
                    }

                }
                else {
                    selectedFromItems = false;
                }
            }
        });

        fromArea.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    fromArea.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        pickupPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    errFromPickUp.setVisibility(View.VISIBLE);
                }
                else {
                    errFromPickUp.setVisibility(View.GONE);
                }
            }
        });

        pickupPoint.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        leavingDate.setOnClickListener(v -> {
            final Calendar c1 = Calendar.getInstance();
            mYear = c1.get(Calendar.YEAR);
            mMonth = c1.get(Calendar.MONTH);
            mDay = c1.get(Calendar.DAY_OF_MONTH);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewRequisition.this, (view, year, month, dayOfMonth) -> {

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
                    String select_date = dayOfMonthName + "-" + monthName + "-" + yearName;
                    showTimePicker(select_date,1);

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        toLocDivSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            selected_to_loc_div_id = "";
            selected_to_loc_dist_name = "";
            selected_to_loc_dist_id = "";
            to_loc_area = "";
            selected_to_loc_dd_id = "";
            tLDistSpinnLay.setEnabled(false);
            toLocDistSpinner.setText("");
            toAreaLay.setEnabled(false);
            toArea.setText("");

            for (int i = 0; i <divLists.size(); i++) {
                if (name.equals(divLists.get(i).getName())) {
                    selected_to_loc_div_id = divLists.get(i).getId();
                    selected_to_loc_div_name = divLists.get(i).getName();
                }
            }
            errToDivDis.setVisibility(View.GONE);
            getDistrict(2);
        });

        toLocDistSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            to_loc_area = "";
            selected_to_loc_dd_id = "";
            toAreaLay.setEnabled(false);
            toArea.setText("");
            for (int i = 0; i <tldistLists.size(); i++) {
                if (name.equals(tldistLists.get(i).getName())) {
                    selected_to_loc_dist_id = tldistLists.get(i).getId();
                    selected_to_loc_dist_name = tldistLists.get(i).getName();
                }
            }
            errToDivDis.setVisibility(View.GONE);
            getArea(2);
        });

        toArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    errToArea.setVisibility(View.GONE);
                }
            }
        });

        toArea.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            for (int i = 0; i <tlThanaLists.size(); i++) {
                if (name.equals(tlThanaLists.get(i).getName())) {
                    to_loc_area = tlThanaLists.get(i).getName();
                    selected_to_loc_dd_id = tlThanaLists.get(i).getId();
                }
            }

            selectedFromItems = true;
            errToArea.setVisibility(View.GONE);
            closeKeyBoard();
        });

        toArea.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String ss = toArea.getText().toString();
                if (!selectedFromItems) {
                    selected_to_loc_dd_id = "";
                    for (int i = 0; i < tlThanaLists.size(); i++) {
                        if (ss.equals(tlThanaLists.get(i).getName())) {
                            selected_to_loc_dd_id = tlThanaLists.get(i).getId();
                            to_loc_area = tlThanaLists.get(i).getName();
                        }
                    }
                    if (selected_to_loc_dd_id.isEmpty()) {
                        if (ss.isEmpty()) {
                            errToArea.setVisibility(View.VISIBLE);
                            String cmt = "Please Select Area";
                            errToArea.setText(cmt);
                        }
                        else {
                            errToArea.setVisibility(View.VISIBLE);
                            String cmt = "Invalid Area";
                            errToArea.setText(cmt);
                        }
                    }

                }
                else {
                    selectedFromItems = false;
                }
            }
        });

        toArea.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    toArea.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        dropPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    errDrpPoint.setVisibility(View.VISIBLE);
                }
                else {
                    errDrpPoint.setVisibility(View.GONE);
                }
            }
        });

        dropPoint.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        returnDate.setOnClickListener(v -> {
            final Calendar c1 = Calendar.getInstance();
            mYear = c1.get(Calendar.YEAR);
            mMonth = c1.get(Calendar.MONTH);
            mDay = c1.get(Calendar.DAY_OF_MONTH);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(NewRequisition.this, (view, year, month, dayOfMonth) -> {

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
                    String select_date = dayOfMonthName + "-" + monthName + "-" + yearName;
                    showTimePicker(select_date,2);

                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        reqFleetType.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            req_fleet_type_id = "";
            for (int i = 0; i <reqFleetTypeList.size(); i++) {
                if (name.equals(reqFleetTypeList.get(i).getName())) {
                    req_fleet_type_id = reqFleetTypeList.get(i).getId();
                    req_fleet_type_name = reqFleetTypeList.get(i).getName();
                }
            }
            errFlType.setVisibility(View.GONE);
        });

        vehicleQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    if (!editable.toString().equals("0")) {
                        errVhlQty.setVisibility(View.GONE);
                    }
                    else {
                        errVhlQty.setText("Invalid Qty");
                        errVhlQty.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    errVhlQty.setText("Please Write Vehicle Qty");
                    errVhlQty.setVisibility(View.VISIBLE);
                }
            }
        });

        vehicleQty.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        travelersQty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!editable.toString().isEmpty()) {
                    if (!editable.toString().equals("0")) {
                        errTrvQty.setVisibility(View.GONE);
                    }
                    else {
                        errTrvQty.setText("Invalid Qty");
                        errTrvQty.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    errTrvQty.setText("Please Write Travelers Qty");
                    errTrvQty.setVisibility(View.VISIBLE);
                }
            }
        });

        travelersQty.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        saveReq.setOnClickListener(v -> {
            req_emp_mob_no = Objects.requireNonNull(reqEmpMobNo.getText()).toString();
            from_loc_area = Objects.requireNonNull(fromArea.getText()).toString();
            pickup_point = Objects.requireNonNull(pickupPoint.getText()).toString();
            to_loc_area = Objects.requireNonNull(toArea.getText()).toString();
            drop_point = Objects.requireNonNull(dropPoint.getText()).toString();
            vh_qty = Objects.requireNonNull(vehicleQty.getText()).toString();
            travelers_qty = Objects.requireNonNull(travelersQty.getText()).toString();

            if (!selected_purpose_id.isEmpty()) {
                if (req_emp_mob_no.isEmpty()) {
                    // ---
                    if (!selected_from_loc_div_id.isEmpty() && !selected_from_loc_dist_id.isEmpty()) {
                        if (!from_loc_area.isEmpty()) {
                            if (!pickup_point.isEmpty()) {
                                if (!selected_leave_date_time.isEmpty()) {
                                    if (!selected_to_loc_div_id.isEmpty() && !selected_to_loc_dist_id.isEmpty()) {
                                        if (!to_loc_area.isEmpty()) {
                                            if (!drop_point.isEmpty()) {
                                                if (!selected_return_date_time.isEmpty()) {
                                                    if (!req_fleet_type_id.isEmpty()) {
                                                        if (!vh_qty.isEmpty()) {
                                                            if (!vh_qty.equals("0")) {
                                                                if (!travelers_qty.isEmpty()) {
                                                                    if (!travelers_qty.equals("0")) {
                                                                        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
                                                                        alertDialogBuilder.setTitle("New Requisition")
                                                                                .setMessage("Do you want to send new fleet requisition?")
                                                                                .setPositiveButton("Yes", (dialogInterface, i) -> {
                                                                                    sendRequisition();
                                                                                    dialogInterface.dismiss();
                                                                                })
                                                                                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                                                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                                                        alertDialog.setCanceledOnTouchOutside(false);
                                                                        alertDialog.setCancelable(false);
                                                                        alertDialog.show();
                                                                    }
                                                                    else {
                                                                        errTrvQty.setText("Invalid Qty");
                                                                        errTrvQty.setVisibility(View.VISIBLE);
                                                                        fullLayout.scrollTo(errTrvQty.getScrollX(), errTrvQty.getScrollY());
                                                                    }
                                                                }
                                                                else {
                                                                    errTrvQty.setText("Please Write Travelers Qty");
                                                                    errTrvQty.setVisibility(View.VISIBLE);
                                                                    fullLayout.scrollTo(errTrvQty.getScrollX(), errTrvQty.getScrollY());
                                                                }
                                                            }
                                                            else {
                                                                errVhlQty.setText("Invalid Qty");
                                                                errVhlQty.setVisibility(View.VISIBLE);
                                                                fullLayout.scrollTo(errVhlQty.getScrollX(), errVhlQty.getScrollY());
                                                            }
                                                        }
                                                        else {
                                                            errVhlQty.setText("Please Write Vehicle Qty");
                                                            errVhlQty.setVisibility(View.VISIBLE);
                                                            fullLayout.scrollTo(errVhlQty.getScrollX(), errVhlQty.getScrollY());
                                                        }
                                                    }
                                                    else {
                                                        errFlType.setVisibility(View.VISIBLE);
                                                        fullLayout.scrollTo(errFlType.getScrollX(), errFlType.getScrollY());
                                                    }
                                                }
                                                else {
                                                    errReturnDate.setVisibility(View.VISIBLE);
                                                    fullLayout.scrollTo(errReturnDate.getScrollX(), errReturnDate.getScrollY());
                                                }
                                            }
                                            else {
                                                errDrpPoint.setVisibility(View.VISIBLE);
                                                fullLayout.scrollTo(errDrpPoint.getScrollX(), errDrpPoint.getScrollY());
                                            }
                                        }
                                        else {
                                            errToArea.setVisibility(View.VISIBLE);
                                            fullLayout.scrollTo(errToArea.getScrollX(), errToArea.getScrollY());
                                        }
                                    }
                                    else {
                                        errToDivDis.setVisibility(View.VISIBLE);
                                        fullLayout.scrollTo(errToDivDis.getScrollX(), errToDivDis.getScrollY());
                                    }
                                }
                                else {
                                    errFromLvDate.setVisibility(View.VISIBLE);
                                    fullLayout.scrollTo(errFromLvDate.getScrollX(), errFromLvDate.getScrollY());
                                }
                            }
                            else {
                                errFromPickUp.setVisibility(View.VISIBLE);
                                fullLayout.scrollTo(errFromPickUp.getScrollX(), errFromPickUp.getScrollY());
                            }
                        }
                        else {
                            errFromArea.setVisibility(View.VISIBLE);
                            fullLayout.scrollTo(errFromArea.getScrollX(), errFromArea.getScrollY());
                        }
                    }
                    else {
                        errFromDivDis.setVisibility(View.VISIBLE);
                        fullLayout.scrollTo(errFromDivDis.getScrollX(), errFromDivDis.getScrollY());
                    }
                }
                else {
                    if (req_emp_mob_no.startsWith("0")) {
                        if (req_emp_mob_no.length() == 11) {
                            // ---
                            if (!selected_from_loc_div_id.isEmpty() && !selected_from_loc_dist_id.isEmpty()) {
                                if (!from_loc_area.isEmpty()) {
                                    if (!pickup_point.isEmpty()) {
                                        if (!selected_leave_date_time.isEmpty()) {
                                            if (!selected_to_loc_div_id.isEmpty() && !selected_to_loc_dist_id.isEmpty()) {
                                                if (!to_loc_area.isEmpty()) {
                                                    if (!drop_point.isEmpty()) {
                                                        if (!selected_return_date_time.isEmpty()) {
                                                            if (!req_fleet_type_id.isEmpty()) {
                                                                if (!vh_qty.isEmpty()) {
                                                                    if (!vh_qty.equals("0")) {
                                                                        if (!travelers_qty.isEmpty()) {
                                                                            if (!travelers_qty.equals("0")) {
                                                                                MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
                                                                                alertDialogBuilder.setTitle("New Requisition")
                                                                                        .setMessage("Do you want to send new fleet requisition?")
                                                                                        .setPositiveButton("Yes", (dialogInterface, i) -> {
                                                                                            sendRequisition();
                                                                                            dialogInterface.dismiss();
                                                                                        })
                                                                                        .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.dismiss());
                                                                                AlertDialog alertDialog = alertDialogBuilder.create();
                                                                                alertDialog.setCanceledOnTouchOutside(false);
                                                                                alertDialog.setCancelable(false);
                                                                                alertDialog.show();
                                                                            }
                                                                            else {
                                                                                errTrvQty.setText("Invalid Qty");
                                                                                errTrvQty.setVisibility(View.VISIBLE);
                                                                                fullLayout.scrollTo(errTrvQty.getScrollX(), errTrvQty.getScrollY());
                                                                            }
                                                                        }
                                                                        else {
                                                                            errTrvQty.setText("Please Write Travelers Qty");
                                                                            errTrvQty.setVisibility(View.VISIBLE);
                                                                            fullLayout.scrollTo(errTrvQty.getScrollX(), errTrvQty.getScrollY());
                                                                        }
                                                                    }
                                                                    else {
                                                                        errVhlQty.setText("Invalid Qty");
                                                                        errVhlQty.setVisibility(View.VISIBLE);
                                                                        fullLayout.scrollTo(errVhlQty.getScrollX(), errVhlQty.getScrollY());
                                                                    }
                                                                }
                                                                else {
                                                                    errVhlQty.setText("Please Write Vehicle Qty");
                                                                    errVhlQty.setVisibility(View.VISIBLE);
                                                                    fullLayout.scrollTo(errVhlQty.getScrollX(), errVhlQty.getScrollY());
                                                                }
                                                            }
                                                            else {
                                                                errFlType.setVisibility(View.VISIBLE);
                                                                fullLayout.scrollTo(errFlType.getScrollX(), errFlType.getScrollY());
                                                            }
                                                        }
                                                        else {
                                                            errReturnDate.setVisibility(View.VISIBLE);
                                                            fullLayout.scrollTo(errReturnDate.getScrollX(), errReturnDate.getScrollY());
                                                        }
                                                    }
                                                    else {
                                                        errDrpPoint.setVisibility(View.VISIBLE);
                                                        fullLayout.scrollTo(errDrpPoint.getScrollX(), errDrpPoint.getScrollY());
                                                    }
                                                }
                                                else {
                                                    errToArea.setVisibility(View.VISIBLE);
                                                    fullLayout.scrollTo(errToArea.getScrollX(), errToArea.getScrollY());
                                                }
                                            }
                                            else {
                                                errToDivDis.setVisibility(View.VISIBLE);
                                                fullLayout.scrollTo(errToDivDis.getScrollX(), errToDivDis.getScrollY());
                                            }
                                        }
                                        else {
                                            errFromLvDate.setVisibility(View.VISIBLE);
                                            fullLayout.scrollTo(errFromLvDate.getScrollX(), errFromLvDate.getScrollY());
                                        }
                                    }
                                    else {
                                        errFromPickUp.setVisibility(View.VISIBLE);
                                        fullLayout.scrollTo(errFromPickUp.getScrollX(), errFromPickUp.getScrollY());
                                    }
                                }
                                else {
                                    errFromArea.setVisibility(View.VISIBLE);
                                    fullLayout.scrollTo(errFromArea.getScrollX(), errFromArea.getScrollY());
                                }
                            }
                            else {
                                errFromDivDis.setVisibility(View.VISIBLE);
                                fullLayout.scrollTo(errFromDivDis.getScrollX(), errFromDivDis.getScrollY());
                            }
                        }
                        else {
                            errMob.setVisibility(View.VISIBLE);
                            fullLayout.scrollTo(errMob.getScrollX(), errMob.getScrollY());
                        }
                    }
                    else {
                        errMob.setVisibility(View.VISIBLE);
                        fullLayout.scrollTo(errMob.getScrollX(), errMob.getScrollY());
                    }
                }
            }
            else {
                errPurpose.setVisibility(View.VISIBLE);
                fullLayout.scrollTo(errPurpose.getScrollX(), errPurpose.getScrollY());
            }
        });

        getData();
    }

    private void showTimePicker(String selected_date, int loc) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(NewRequisition.this, (view, hourOfDay, minute) -> {

            String AM_PM ;
            if(hourOfDay < 12) {
                AM_PM = "AM";
                if (hourOfDay == 0) {
                    hourOfDay = 12;
                }
            }
            else {
                AM_PM = "PM";
                if (hourOfDay > 12 ) {
                    hourOfDay = hourOfDay - 12;
                }
            }

            String tt = String.valueOf(minute);
            if (tt.length() == 1) {
                tt = "0"+tt;
                String text = selected_date+", "+hourOfDay + ":" + tt + " "+AM_PM;
                if (loc == 1) {
                    selected_leave_date_time = text;
                    leavingDate.setText(text);
                    errFromLvDate.setVisibility(View.GONE);
                }
                else if (loc == 2) {
                    selected_return_date_time = text;
                    returnDate.setText(text);
                    errReturnDate.setVisibility(View.GONE);
                }
            }
            else {
                String text = selected_date+", "+hourOfDay + ":" + minute + " "+ AM_PM;
                if (loc == 1) {
                    selected_leave_date_time = text;
                    leavingDate.setText(text);
                    errFromLvDate.setVisibility(View.GONE);
                }
                else if (loc == 2) {
                    selected_return_date_time = text;
                    returnDate.setText(text);
                    errReturnDate.setVisibility(View.GONE);
                }
            }
        },mHour, mMinute,false);
        timePickerDialog.show();
    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        closeKeyBoard();
        return super.onTouchEvent(event);
    }

    public void getData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        purposeLists = new ArrayList<>();
        divLists = new ArrayList<>();
        reqFleetTypeList = new ArrayList<>();

        String purposeUrl = api_url_front+"fleet_requisition/getPurpose";
        String aDivUrl = api_url_front+"fleet_requisition/getAreaDivisions";
        String flTypeUrl = api_url_front+"fleet_requisition/getFleetType";

        RequestQueue requestQueue = Volley.newRequestQueue(NewRequisition.this);

        StringRequest aflTypeReq = new StringRequest(Request.Method.GET, flTypeUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject depInfo = array.getJSONObject(i);
                        String nu = depInfo.getString("ft_id")
                                .equals("null") ? "" : depInfo.getString("ft_id");
                        String dummy = depInfo.getString("ft_type_name")
                                .equals("null") ? "" : depInfo.getString("ft_type_name");

                        reqFleetTypeList.add(new TwoItemLists(nu,dummy));
                    }
                }
//                requestQueue.add(divDirReq);
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

        StringRequest aDivReq = new StringRequest(Request.Method.GET, aDivUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject depInfo = array.getJSONObject(i);
                        String div_id = depInfo.getString("div_id");
                        String div_name = depInfo.getString("div_name");

                        divLists.add(new TwoItemLists(div_id,div_name));
                    }
                }
                requestQueue.add(aflTypeReq);
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

        StringRequest purposeReq = new StringRequest(Request.Method.GET, purposeUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject depInfo = array.getJSONObject(i);
                        String nu = depInfo.getString("nu")
                                .equals("null") ? "" : depInfo.getString("nu");
                        String dummy = depInfo.getString("dummy")
                                .equals("null") ? "" : depInfo.getString("dummy");

                        purposeLists.add(new TwoItemLists(nu,dummy));
                    }
                }
                requestQueue.add(aDivReq);
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

        requestQueue.add(purposeReq);
    }

    private void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < purposeLists.size(); i++) {
                    type.add(purposeLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                purposeOfReq.setAdapter(arrayAdapter);

                ArrayList<String> type2 = new ArrayList<>();
                for(int i = 0; i < divLists.size(); i++) {
                    type2.add(divLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, R.id.drop_down_item, type2);

                fromLocDivSpinner.setAdapter(arrayAdapter2);
                toLocDivSpinner.setAdapter(arrayAdapter2);

                ArrayList<String> type3 = new ArrayList<>();
                for(int i = 0; i < reqFleetTypeList.size(); i++) {
                    type3.add(reqFleetTypeList.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter3 = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, R.id.drop_down_item, type3);

                reqFleetType.setAdapter(arrayAdapter3);

            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(NewRequisition.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getData();
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
            AlertDialog dialog = new AlertDialog.Builder(NewRequisition.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    public void getDistrict(int loc) {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        String div_id = "";
        if (loc == 1) {
            fldistLists = new ArrayList<>();
            div_id = selected_from_loc_div_id;
        }
        else if (loc == 2) {
            tldistLists = new ArrayList<>();
            div_id = selected_to_loc_div_id;
        }

        String aDistUrl = api_url_front+"fleet_requisition/getAreaDistricts?div_id="+div_id;

        RequestQueue requestQueue = Volley.newRequestQueue(NewRequisition.this);

        StringRequest aDistReq = new StringRequest(Request.Method.GET, aDistUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject depInfo = array.getJSONObject(i);
                        String dist_id = depInfo.getString("dist_id");
                        String dist_name = depInfo.getString("dist_name");

                        if (loc == 1) {
                            fldistLists.add(new TwoItemLists(dist_id,dist_name));
                        }
                        else if (loc == 2) {
                            tldistLists.add(new TwoItemLists(dist_id,dist_name));
                        }
                    }
                }
                connected = true;
                updateInterface(loc);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateInterface(loc);
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateInterface(loc);
        });

        requestQueue.add(aDistReq);
    }

    private void updateInterface(int loc) {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                if (loc == 1) {
                    ArrayList<String> type = new ArrayList<>();
                    for(int i = 0; i < fldistLists.size(); i++) {
                        type.add(fldistLists.get(i).getName());
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                    fromLocDistSpinner.setAdapter(arrayAdapter);
                    fLDistSpinnLay.setEnabled(true);
                }
                else if (loc == 2) {
                    ArrayList<String> type = new ArrayList<>();
                    for(int i = 0; i < tldistLists.size(); i++) {
                        type.add(tldistLists.get(i).getName());
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                    toLocDistSpinner.setAdapter(arrayAdapter);
                    tLDistSpinnLay.setEnabled(true);
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(NewRequisition.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getDistrict(loc);
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
            AlertDialog dialog = new AlertDialog.Builder(NewRequisition.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getDistrict(loc);
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    public void getArea(int loc) {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        String dist_id = "";
        if (loc == 1) {
            flThanaLists = new ArrayList<>();
            dist_id = selected_from_loc_dist_id;
        }
        else if (loc == 2) {
            tlThanaLists = new ArrayList<>();
            dist_id = selected_from_loc_dist_id;
        }

        String aDistDtlUrl = api_url_front+"fleet_requisition/getArea?dist_id="+dist_id;

        RequestQueue requestQueue = Volley.newRequestQueue(NewRequisition.this);

        StringRequest aDistDtlReq = new StringRequest(Request.Method.GET, aDistDtlUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject depInfo = array.getJSONObject(i);
                        String dd_id = depInfo.getString("dd_id");
                        String dd_thana_name = depInfo.getString("dd_thana_name");

                        if (loc == 1) {
                            flThanaLists.add(new TwoItemLists(dd_id,dd_thana_name));
                        }
                        else if (loc == 2) {
                            tlThanaLists.add(new TwoItemLists(dd_id,dd_thana_name));
                        }
                    }
                }
                connected = true;
                updateDtlInterface(loc);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateDtlInterface(loc);
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateDtlInterface(loc);
        });

        requestQueue.add(aDistDtlReq);
    }

    private void updateDtlInterface(int loc) {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                if (loc == 1) {
                    ArrayList<String> type = new ArrayList<>();
                    for(int i = 0; i < flThanaLists.size(); i++) {
                        type.add(flThanaLists.get(i).getName());
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                    fromArea.setAdapter(arrayAdapter);
                    fromAreaLay.setEnabled(true);
                }
                else if (loc == 2) {
                    ArrayList<String> type = new ArrayList<>();
                    for(int i = 0; i < tlThanaLists.size(); i++) {
                        type.add(tlThanaLists.get(i).getName());
                    }

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                    toArea.setAdapter(arrayAdapter);
                    toAreaLay.setEnabled(true);
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(NewRequisition.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getArea(loc);
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
            AlertDialog dialog = new AlertDialog.Builder(NewRequisition.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getArea(loc);
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    public void sendRequisition() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        String reqUrl = api_url_front+"fleet_requisition/sendRequisition";

        RequestQueue requestQueue = Volley.newRequestQueue(NewRequisition.this);

        StringRequest requisitionReq = new StringRequest(Request.Method.POST, reqUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    connected = true;
                }
                else {
                    System.out.println(string_out);
                    connected = false;
                }
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
        }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_DIV_ID",fl_div_id);
                headers.put("P_DEPT_ID",fl_dep_id);
                headers.put("P_DESIGNATION_ID",fl_desig_id);
                headers.put("P_JSD_ID",fl_jsd_id);
                headers.put("P_REQUESTER_MOBILE_NO",req_emp_mob_no);
                headers.put("P_MODE_OF_USE",selected_purpose_id);
                headers.put("P_REQUEST_DATE",request_date);
                headers.put("P_LEAVE_TIME",selected_leave_date_time);
                headers.put("P_FROM_DIV_ID",selected_from_loc_div_id);
                headers.put("P_FROM_DIST_ID",selected_from_loc_dist_id);
                headers.put("P_FROM_AREA",from_loc_area);
                headers.put("P_FROM_ADDRESS",pickup_point);
                headers.put("P_TO_DIV_ID",selected_to_loc_div_id);
                headers.put("P_TO_DIST_ID",selected_to_loc_dist_id);
                headers.put("P_TO_AREA",to_loc_area);
                headers.put("P_TO_ADDRESS",drop_point);
                headers.put("P_RETURN_DATE_TIME",selected_return_date_time);
                headers.put("P_VH_QTY",vh_qty);
                headers.put("P_TRAVELERS_QTY",travelers_qty);
                headers.put("P_USER",user_name);
                headers.put("P_REQUISITION_FLEET_TYPE",req_fleet_type_id);
                return  headers;
            }
        };

        requestQueue.add(requisitionReq);
    }

    private  void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;
                AlertDialog.Builder builder = new AlertDialog.Builder(NewRequisition.this);
                builder
                        .setMessage("Requisition sent successfully")
                        .setPositiveButton("OK", (dialog, which) -> {
                            dialog.dismiss();
                            finish();
                        });

                AlertDialog alert = builder.create();
                alert.setCancelable(false);
                alert.setCanceledOnTouchOutside(false);
                alert.show();
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(NewRequisition.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    sendRequisition();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(NewRequisition.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                sendRequisition();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}