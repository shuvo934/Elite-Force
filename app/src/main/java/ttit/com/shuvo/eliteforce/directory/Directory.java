package ttit.com.shuvo.eliteforce.directory;

import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.basic_model.TwoItemLists;
import ttit.com.shuvo.eliteforce.directory.adapters.DirectoryAdapter;
import ttit.com.shuvo.eliteforce.directory.model.DirectoryList;
import ttit.com.shuvo.eliteforce.directory.model.PhoneList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class Directory extends AppCompatActivity {

    RecyclerView directoryView;
    DirectoryAdapter directoryAdapter;
    RecyclerView.LayoutManager layoutManager;

    public static ArrayList<PhoneList> allPhoneLists;
    public static ArrayList<DirectoryList> allDirectoryLists;

    String emp_id = "";
    ArrayList<DirectoryList> filteredList;
    String searchingDep = "";
    String searchingName = "";
    String searchingDesg = "";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    AmazingSpinner divSpinner;
    AmazingSpinner depSpinner;
    AmazingSpinner desSpinner;

    TextInputEditText search;
    TextInputLayout searchLay;

    String divName = "";
    String depName = "";
    String desigName = "";
    String div_id = "";
    String dep_id = "";
    String desig_id = "";

    ArrayList<TwoItemLists> divLists;
    ArrayList<TwoItemLists> depLists;
    ArrayList<TwoItemLists> desLists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_directory);

        depSpinner = findViewById(R.id.department_type_spinner);
        divSpinner = findViewById(R.id.division_type_spinner);
        desSpinner = findViewById(R.id.designation_type_spinner);

        directoryView = findViewById(R.id.directory_division_list_view);

        search = findViewById(R.id.search_item_name_diretory_division);
        searchLay = findViewById(R.id.search_item_name_lay_directory_division);

        allPhoneLists = new ArrayList<>();
        allDirectoryLists = new ArrayList<>();
        filteredList = new ArrayList<>();
        divLists = new ArrayList<>();
        depLists = new ArrayList<>();
        desLists = new ArrayList<>();

        emp_id = userInfoLists.get(0).getEmp_id();
        div_id = userDesignations.get(0).getDiv_id();

        directoryView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        directoryView.setLayoutManager(layoutManager);

        // Selecting Division
        divSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);

            depSpinner.setText("");
            desSpinner.setText("");
            search.setText("");
            depName = "";
            desigName = "";
            searchingName = "";
            searchingDep = "";
            searchingDesg = "";
            for (int i = 0; i <divLists.size(); i++) {
                if (name.equals(divLists.get(i).getName())) {
                    div_id = divLists.get(i).getId();
                    if (div_id.isEmpty()) {
                        divName = "";
                    } 
                    else {
                        divName = divLists.get(i).getName();
                    }
                }
            }
            dep_id = "";
            desig_id = "";

            getDivisionWiseDepData();
        });

        // Selecting Department
        depSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            desSpinner.setText("");
            search.setText("");
            desigName = "";
            searchingName = "";
            searchingDesg = "";
            for (int i = 0; i <depLists.size(); i++) {
                if (name.equals(depLists.get(i).getName())) {
                    dep_id = depLists.get(i).getId();
                    if (dep_id.isEmpty()) {
                        depName = "";
                        searchingDep = "";
                    } 
                    else {
                        depName = depLists.get(i).getName();
                        searchingDep = depLists.get(i).getName();
                    }
                }
            }

            desig_id = "";
            getDesignations();
        });

        // Selecting Employee
        desSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);

            search.setText("");
            searchingName = "";

            for (int i = 0; i <desLists.size(); i++) {
                if (name.equals(desLists.get(i).getName())) {
                    desig_id = desLists.get(i).getId();
                    if (desig_id.isEmpty()) {
                        desigName = "";
                        searchingDesg = "";
                    } 
                    else {
                        searchingDesg = desLists.get(i).getName();
                        desigName = desLists.get(i).getName();
                    }
                }
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchingName = s.toString();
                filter(s.toString());
                if (s.toString().isEmpty()) {
                    searchLay.setHint("Search By Name");
                } 
                else {
                    searchLay.setHint("Name");
                }
            }
        });

        depSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchingDep = s.toString();
                filterDep(s.toString());

            }
        });

        desSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchingDesg = s.toString();
                filterDesg(s.toString());

            }
        });

        search.setOnEditorActionListener((v, actionId, event) -> {
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

        getFirstData();
    }

    private void filter(String text) {
        filteredList = new ArrayList<>();
        for (DirectoryList item : allDirectoryLists) {
            if (searchingDep.isEmpty()) {
                if (searchingDesg.isEmpty()) {
                    if (item.getEmp_name().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                    }
                } 
                else {
                    if (item.getDes_name().toLowerCase().contains(searchingDesg.toLowerCase())) {
                        if (item.getEmp_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                        }
                    }
                }
            } 
            else {
                if (searchingDesg.isEmpty()) {
                    if (item.getDep_name().toLowerCase().contains(searchingDep.toLowerCase())) {
                        if (item.getEmp_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));

                        }
                    }
                }
                else {
                    if (item.getDes_name().toLowerCase().contains(searchingDesg.toLowerCase())) {
                        if (item.getDep_name().toLowerCase().contains(searchingDep.toLowerCase())) {
                            if (item.getEmp_name().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add((item));

                            }
                        }
                    }
                }
            }
        }
        directoryAdapter.filterList(filteredList);
    }

    private void filterDep(String text) {
        filteredList = new ArrayList<>();
        for (DirectoryList item : allDirectoryLists) {
            if (searchingDesg.isEmpty()){
                if (searchingName.isEmpty()) {
                    if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add((item));
                    }
                } 
                else {
                    if (item.getEmp_name().toLowerCase().contains(searchingName.toLowerCase())) {
                        if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                        }
                    }
                }
            } 
            else {
                if (searchingName.isEmpty()) {
                    if (item.getDes_name().toLowerCase().contains(searchingDesg.toLowerCase())) {
                        if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
                            filteredList.add((item));
                        }
                    }
                } 
                else {
                    if (item.getDes_name().toLowerCase().contains(searchingDesg.toLowerCase())) {
                        if (item.getEmp_name().toLowerCase().contains(searchingName.toLowerCase())) {
                            if (item.getDep_name().toLowerCase().contains(text.toLowerCase())) {
                                filteredList.add((item));
                            }
                        }
                    }
                }
            }
        }
        directoryAdapter.filterList(filteredList);
    }

    private void filterDesg(String text) {
        filteredList = new ArrayList<>();
        for (DirectoryList item : allDirectoryLists) {
            if (searchingDep.isEmpty()) {
                if (searchingName.isEmpty()) {
                    if (item.getDes_name().toLowerCase().contains(text.toLowerCase())){
                        filteredList.add((item));
                    }
                } 
                else {
                    if (item.getEmp_name().toLowerCase().contains(searchingName.toLowerCase())){
                        if (item.getDes_name().toLowerCase().contains(text.toLowerCase())){
                            filteredList.add((item));
                        }
                    }
                }
            } 
            else {
                if (searchingName.isEmpty()) {
                    if (item.getDep_name().toLowerCase().contains(searchingDep.toLowerCase())){
                        if (item.getDes_name().toLowerCase().contains(text.toLowerCase())){
                            filteredList.add((item));
                        }
                    }
                } 
                else {
                    if (item.getDep_name().toLowerCase().contains(searchingDep.toLowerCase())){
                        if (item.getEmp_name().toLowerCase().contains(searchingName.toLowerCase())){
                            if (item.getDes_name().toLowerCase().contains(text.toLowerCase())){
                                filteredList.add((item));
                            }
                        }
                    }
                }
            }
        }
        directoryAdapter.filterList(filteredList);
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

    public void getFirstData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        divLists = new ArrayList<>();
        depLists = new ArrayList<>();
        allPhoneLists = new ArrayList<>();
        allDirectoryLists = new ArrayList<>();
        ArrayList<String> idofALl = new ArrayList<>();

        String divUrl = api_url_front+"directory/getDivisions";
        String depUrl = api_url_front+"directory/getDepartments/"+div_id+"";
        String divDirUrl = api_url_front+"directory/getDivDirectories/"+div_id+"/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Directory.this);

        StringRequest divDirReq = new StringRequest(Request.Method.GET, divDirUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject dirInfo = array.getJSONObject(i);

                        String emp_id_new = dirInfo.getString("emp_id")
                                .equals("null") ? "" : dirInfo.getString("emp_id");
                        String emp_name_new = dirInfo.getString("emp_name")
                                .equals("null") ? "" : dirInfo.getString("emp_name");
                        String dept_name_new = dirInfo.getString("dept_name")
                                .equals("null") ? "" : dirInfo.getString("dept_name");
                        String divm_name_new = dirInfo.getString("divm_name")
                                .equals("null") ? "" : dirInfo.getString("divm_name");
                        String desig_name_new = dirInfo.getString("desig_name")
                                .equals("null") ? "" : dirInfo.getString("desig_name");
                        String usr_contact_new = dirInfo.getString("usr_contact")
                                .equals("null") ? null : dirInfo.getString("usr_contact");
                        String usr_email_new = dirInfo.getString("usr_email")
                                .equals("null") ? null : dirInfo.getString("usr_email");
                        String job_email = dirInfo.getString("job_email")
                                .equals("null") ? null: dirInfo.getString("job_email");

                        String mail;
                        if (job_email == null) {
                            mail = usr_email_new;
                        }
                        else {
                            mail = job_email;
                        }

                        allDirectoryLists.add(new DirectoryList(emp_id_new,emp_name_new,divm_name_new,
                                dept_name_new,desig_name_new,mail,usr_contact_new,"2"));

                        idofALl.add(emp_id_new);
                    }
                }
                getPhoneLists(idofALl);
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

        StringRequest depReq = new StringRequest(Request.Method.GET, depUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject depInfo = array.getJSONObject(i);
                        String dept_id_new = depInfo.getString("dept_id");
                        String dept_name_new = depInfo.getString("dept_name");

                        depLists.add(new TwoItemLists(dept_id_new,dept_name_new));
                    }
                }
                requestQueue.add(divDirReq);
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

        StringRequest divReq = new StringRequest(Request.Method.GET, divUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject divInfo = array.getJSONObject(i);
                        
                        String divm_id_new = divInfo.getString("divm_id");
                        String divm_name_new = divInfo.getString("divm_name");

                        divLists.add(new TwoItemLists(divm_id_new,divm_name_new));
                    }
                }

                requestQueue.add(depReq);
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

        requestQueue.add(divReq);
    }

    public void getPhoneLists(ArrayList<String> allId) {
        if (allId.size() != 0) {
            String id = allId.get(0);
            getPhones(id,allId);
        }
        else {
            connected = true;
            updateLay();
        }
    }

    public void getPhones(String id,ArrayList<String> allId) {
        String url = api_url_front+"directory/getAllPhones/"+id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Directory.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject phnInfo = array.getJSONObject(i);

                        String job_emp_id = phnInfo.getString("job_emp_id")
                                .equals("null") ? "" : phnInfo.getString("job_emp_id");
                        String esh_mbl_sim = phnInfo.getString("esh_mbl_sim")
                                .equals("null") ? "" : phnInfo.getString("esh_mbl_sim");

                        allPhoneLists.add(new PhoneList(job_emp_id,esh_mbl_sim));
                    }
                }
                allId.remove(0);
                getPhoneLists(allId);
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

        requestQueue.add(stringRequest);
    }

    private void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                for(int i = 0; i < divLists.size(); i++) {
                    if(div_id.equals(divLists.get(i).getId())) {
                        divSpinner.setText(divLists.get(i).getName());
                    }
                }
                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < divLists.size(); i++) {
                    type.add(divLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                divSpinner.setAdapter(arrayAdapter);


                ArrayList<String> type1 = new ArrayList<>();
                for(int i = 0; i < depLists.size(); i++) {
                    type1.add(depLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_menu_popup_item, R.id.drop_down_item, type1);

                depSpinner.setAdapter(arrayAdapter1);

                directoryAdapter = new DirectoryAdapter(Directory.this, allDirectoryLists);
                directoryView.setAdapter(directoryAdapter);

            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Directory.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getFirstData();
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
            AlertDialog dialog = new AlertDialog.Builder(Directory.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getFirstData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    public void getDivisionWiseDepData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        depLists = new ArrayList<>();
        allPhoneLists = new ArrayList<>();
        allDirectoryLists = new ArrayList<>();
        ArrayList<String> idofALl = new ArrayList<>();

        String depUrl = api_url_front+"directory/getDepartments/"+div_id+"";
        String divDirUrl = api_url_front+"directory/getDivDirectories/"+div_id+"/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Directory.this);

        StringRequest divDirReq = new StringRequest(Request.Method.GET, divDirUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject dirInfo = array.getJSONObject(i);

                        String emp_id_new = dirInfo.getString("emp_id")
                                .equals("null") ? "" : dirInfo.getString("emp_id");
                        String emp_name_new = dirInfo.getString("emp_name")
                                .equals("null") ? "" : dirInfo.getString("emp_name");
                        String dept_name_new = dirInfo.getString("dept_name")
                                .equals("null") ? "" : dirInfo.getString("dept_name");
                        String divm_name_new = dirInfo.getString("divm_name")
                                .equals("null") ? "" : dirInfo.getString("divm_name");
                        String desig_name_new = dirInfo.getString("desig_name")
                                .equals("null") ? "" : dirInfo.getString("desig_name");
                        String usr_contact_new = dirInfo.getString("usr_contact")
                                .equals("null") ? null : dirInfo.getString("usr_contact");
                        String usr_email_new = dirInfo.getString("usr_email")
                                .equals("null") ? null : dirInfo.getString("usr_email");

                        allDirectoryLists.add(new DirectoryList(emp_id_new,emp_name_new,divm_name_new,
                                dept_name_new,desig_name_new,usr_email_new,usr_contact_new,"2"));

                        idofALl.add(emp_id_new);
                    }
                }
                getDivPhoneLists(idofALl);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateAfterSelectingDiv();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateAfterSelectingDiv();
        });

        StringRequest depReq = new StringRequest(Request.Method.GET, depUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject depInfo = array.getJSONObject(i);
                        String dept_id_new = depInfo.getString("dept_id");
                        String dept_name_new = depInfo.getString("dept_name");

                        depLists.add(new TwoItemLists(dept_id_new,dept_name_new));
                    }
                }
                requestQueue.add(divDirReq);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateAfterSelectingDiv();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateAfterSelectingDiv();
        });

        requestQueue.add(depReq);
    }

    public void getDivPhoneLists(ArrayList<String> allId) {
        if (allId.size() != 0) {
            String id = allId.get(0);
            getDivPhones(id,allId);
        }
        else {
            connected = true;
            updateAfterSelectingDiv();
        }
    }

    public void getDivPhones(String id,ArrayList<String> allId) {
        String url = api_url_front+"directory/getAllPhones/"+id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Directory.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject phnInfo = array.getJSONObject(i);
                        
                        String job_emp_id = phnInfo.getString("job_emp_id")
                                .equals("null") ? "" : phnInfo.getString("job_emp_id");
                        String esh_mbl_sim = phnInfo.getString("esh_mbl_sim")
                                .equals("null") ? "" : phnInfo.getString("esh_mbl_sim");

                        allPhoneLists.add(new PhoneList(job_emp_id,esh_mbl_sim));
                    }
                }
                allId.remove(0);
                getDivPhoneLists(allId);
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateAfterSelectingDiv();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateAfterSelectingDiv();
        });

        requestQueue.add(stringRequest);
    }

    private void updateAfterSelectingDiv() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < depLists.size(); i++) {
                    type.add(depLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                depSpinner.setAdapter(arrayAdapter);


                desLists = new ArrayList<>();
                ArrayList<String> type1 = new ArrayList<>();
                for(int i = 0; i < desLists.size(); i++) {
                    type1.add(desLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type1);

                desSpinner.setAdapter(arrayAdapter1);

                directoryAdapter = new DirectoryAdapter(Directory.this, allDirectoryLists);
                directoryView.setAdapter(directoryAdapter);

            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Directory.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getDivisionWiseDepData();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(Directory.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getDivisionWiseDepData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }

    public void getDesignations() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        desLists = new ArrayList<>();

        String url = api_url_front+"directory/getDesignations/"+div_id+"/"+dep_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(Directory.this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject desInfo = array.getJSONObject(i);

                        String desig_id_new = desInfo.getString("desig_id");
                        String desig_name_new = desInfo.getString("desig_name");

                        desLists.add(new TwoItemLists(desig_id_new,desig_name_new));
                    }
                }
                connected = true;
                updateAfterSelectingDEP();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateAfterSelectingDEP();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateAfterSelectingDEP();
        });

        requestQueue.add(stringRequest);
    }

    private void updateAfterSelectingDEP() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < desLists.size(); i++) {
                    type.add(desLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                desSpinner.setAdapter(arrayAdapter);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(Directory.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getDesignations();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(Directory.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getDesignations();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}