package ttit.com.shuvo.eliteforce.employeeInfo.personal;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.employeeInfo.personal.model.EMPInformation;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class PersonalData extends AppCompatActivity {

    EditText bloodGroup;
    EditText groupDisplay;
    EditText religion;
    EditText sex;
    EditText marital_status;
    EditText mailing_address;

    public ArrayList<String> bg;
    public ArrayList<String> gd;
    public ArrayList<String> rel;
    public ArrayList<String> se;
    public ArrayList<String> ms;
    public ArrayList<String> ma;

    EditText nameP;
    EditText nameBP;
    EditText revision;
    EditText effectedDate;
    EditText dateOfBirth;
    EditText nationality;
    EditText familyName;
    EditText callingName;
    EditText email;
    EditText personalAdd;
    EditText permanentAdd;
    EditText personalAddBangla;
    EditText permanentAddBangla;

    public static ArrayList<EMPInformation> empInformations;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";
    String empName = "";
    String banglaName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        emp_id = userInfoLists.get(0).getEmp_id();

        bloodGroup = findViewById(R.id.spinner_blood);
        groupDisplay = findViewById(R.id.spinner_gr_display);
        religion = findViewById(R.id.spineer_religion);
        sex = findViewById(R.id.spinner_sex);
        marital_status = findViewById(R.id.spinner_marital_status);
        mailing_address = findViewById(R.id.spinner_mailing_address);

        nameP = findViewById(R.id.emp_name_p);
        nameBP = findViewById(R.id.bangla_name_p);
        revision = findViewById(R.id.revision_no);
        effectedDate = findViewById(R.id.effected_da);
        dateOfBirth = findViewById(R.id.birth);
        nationality = findViewById(R.id.nationality);
        familyName = findViewById(R.id.family_name);
        callingName = findViewById(R.id.calling_name);
        email = findViewById(R.id.personal_email);
        personalAdd = findViewById(R.id.present_address);
        permanentAdd = findViewById(R.id.permanent_address);
        personalAddBangla = findViewById(R.id.present_address_bangla);
        permanentAddBangla = findViewById(R.id.permanent_address_bangla);

        bg = new ArrayList<>();
        gd = new ArrayList<>();
        se = new ArrayList<>();
        rel = new ArrayList<>();
        ms = new ArrayList<>();
        ma = new ArrayList<>();

        empInformations = new ArrayList<>();

        bg.add("Not Found");
        bg.add("A+");
        bg.add("A-");
        bg.add("B+");
        bg.add("B-");
        bg.add("AB+");
        bg.add("AB-");
        bg.add("O+");
        bg.add("O-");

        gd.add("Publish");
        gd.add("Not Publish");

        se.add("Not Found");
        se.add("Male");
        se.add("Female");
        se.add("Transgender");
        se.add("Prefer not to say");

        rel.add("Not Found");
        rel.add("Islam");
        rel.add("Christian");
        rel.add("Buddhist");
        rel.add("Hindu");
        rel.add("Others");
        rel.add("Sikhism");
        rel.add("Jains");
        rel.add("Religion not stated");

        ms.add("Not Found");
        ms.add("Married");
        ms.add("Single");
        ms.add("Divorced");
        ms.add("Widowed");
        ms.add("Widower");

        ma.add("Present Address");
        ma.add("Permanent Address");

        String firstname = userInfoLists.get(0).getUser_fname();
        String lastName = userInfoLists.get(0).getUser_lname();
        if (firstname == null) {
            firstname = "";
        }
        if (lastName == null) {
            lastName = "";
        }
        empName = firstname+" "+lastName;

        nameP.setText(empName);

        getEmpInformation();
    }

    public void getEmpInformation() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        banglaName = "";
        empInformations = new ArrayList<>();

        String jobDescUrl = api_url_front+"emp_information/getPersonalInfo/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(PersonalData.this);

        StringRequest emoInfoReq = new StringRequest(Request.Method.GET, jobDescUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empInfo = array.getJSONObject(i);

                        String pi_rivision_code = empInfo.getString("pi_rivision_code");
                        if(pi_rivision_code.equals("null") || pi_rivision_code.equals("NULL")) {
                            pi_rivision_code = "";
                        }
                        pi_rivision_code = transformText(pi_rivision_code);

                        String pi_eff_date = empInfo.getString("pi_eff_date");
                        if(pi_eff_date.equals("null") || pi_eff_date.equals("NULL")) {
                            pi_eff_date = "";
                        }
                        pi_eff_date = transformText(pi_eff_date);

                        String pi_blood_gp = empInfo.getString("pi_blood_gp");
                        if(pi_blood_gp.equals("null") || pi_blood_gp.equals("NULL")) {
                            pi_blood_gp = "0";
                        }
                        pi_blood_gp = transformText(pi_blood_gp);

                        String pi_blood_gp_publish_flag = empInfo.getString("pi_blood_gp_publish_flag");
                        if(pi_blood_gp_publish_flag.equals("null") || pi_blood_gp_publish_flag.equals("NULL")) {
                            pi_blood_gp_publish_flag = "0";
                        }
                        pi_blood_gp_publish_flag = transformText(pi_blood_gp_publish_flag);

                        String pi_dob = empInfo.getString("pi_dob");
                        if(pi_dob.equals("null") || pi_dob.equals("NULL")) {
                            pi_dob = "";
                        }
                        pi_dob = transformText(pi_dob);

                        String pi_religion = empInfo.getString("pi_religion");
                        if(pi_religion.equals("null") || pi_religion.equals("NULL")) {
                            pi_religion = "0";
                        }
                        pi_religion = transformText(pi_religion);

                        String pi_nationality = empInfo.getString("pi_nationality");
                        if(pi_nationality.equals("null") || pi_nationality.equals("NULL")) {
                            pi_nationality = "";
                        }
                        pi_nationality = transformText(pi_nationality);

                        String pi_sex = empInfo.getString("pi_sex");
                        if(pi_sex.equals("null") || pi_sex.equals("NULL")) {
                            pi_sex = "0";
                        }
                        pi_sex = transformText(pi_sex);

                        String pi_family_name = empInfo.getString("pi_family_name");
                        if(pi_family_name.equals("null") || pi_family_name.equals("NULL")) {
                            pi_family_name = "";
                        }
                        pi_family_name = transformText(pi_family_name);

                        String pi_calling_name = empInfo.getString("pi_calling_name");
                        if(pi_calling_name.equals("null") || pi_calling_name.equals("NULL")) {
                            pi_calling_name = "";
                        }
                        pi_calling_name = transformText(pi_calling_name);

                        String pi_email = empInfo.getString("pi_email");
                        if(pi_email.equals("null") || pi_email.equals("NULL")) {
                            pi_email = "";
                        }
                        pi_email = transformText(pi_email);

                        String pi_pre_address = empInfo.getString("pi_pre_address");
                        if(pi_pre_address.equals("null") || pi_pre_address.equals("NULL")) {
                            pi_pre_address = "";
                        }
                        pi_pre_address = transformText(pi_pre_address);

                        String pi_par_address = empInfo.getString("pi_par_address");
                        if(pi_par_address.equals("null") || pi_par_address.equals("NULL")) {
                            pi_par_address = "";
                        }
                        pi_par_address = transformText(pi_par_address);

                        String pi_marital_status = empInfo.getString("pi_marital_status");
                        if(pi_marital_status.equals("null") || pi_marital_status.equals("NULL")) {
                            pi_marital_status = "0";
                        }
                        pi_marital_status = transformText(pi_marital_status);

                        String pi_pre_address_ban = empInfo.getString("pi_pre_address_ban");
                        if(pi_pre_address_ban.equals("null") || pi_pre_address_ban.equals("NULL")) {
                            pi_pre_address_ban = "";
                        }
                        pi_pre_address_ban = transformText(pi_pre_address_ban);

                        String pi_par_address_ban = empInfo.getString("pi_par_address_ban");
                        if(pi_par_address_ban.equals("null") || pi_par_address_ban.equals("NULL")) {
                            pi_par_address_ban = "";
                        }
                        pi_par_address_ban = transformText(pi_par_address_ban);

                        String pi_address_flag = empInfo.getString("pi_address_flag");
                        if(pi_address_flag.equals("null") || pi_address_flag.equals("NULL")) {
                            pi_address_flag = "0";
                        }
                        pi_address_flag = transformText(pi_address_flag);

                        String emp_name_bn = empInfo.getString("emp_name_bn");
                        if(emp_name_bn.equals("null") || emp_name_bn.equals("NULL")) {
                            emp_name_bn = "";
                        }
                        emp_name_bn = transformText(emp_name_bn);

                        empInformations.add(new EMPInformation(pi_rivision_code,pi_eff_date,pi_blood_gp,
                                pi_blood_gp_publish_flag, pi_dob,pi_religion,pi_nationality,
                                pi_sex,pi_family_name,pi_calling_name,pi_email,
                                pi_pre_address,pi_par_address,pi_marital_status,pi_pre_address_ban,
                                pi_par_address_ban,pi_address_flag));
                        banglaName = emp_name_bn;
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
        },error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });

        requestQueue.add(emoInfoReq);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (empInformations.size() != 0) {
                    for (int i = 0 ; i < empInformations.size(); i++) {

                        String dateC = empInformations.get(i).getEffectedDAte();
                        effectedDate.setText(dateC);

                        String datt = empInformations.get(i).getBirth_date();
                        dateOfBirth.setText(datt);

                        revision.setText(empInformations.get(i).getRevision());

                        if (empInformations.get(i).getBlood_grp() != null) {
                            bloodGroup.setText(bg.get(Integer.parseInt(empInformations.get(i).getBlood_grp())));
                        }
                        if (empInformations.get(i).getGrop_dis() != null) {
                            groupDisplay.setText(gd.get(Integer.parseInt(empInformations.get(i).getGrop_dis())));
                        }
                        if (empInformations.get(i).getReligion() != null) {
                            religion.setText(rel.get(Integer.parseInt(empInformations.get(i).getReligion())));
                        }

                        nationality.setText(empInformations.get(i).getNational());

                        if (empInformations.get(i).getSex() != null) {
                            sex.setText(se.get(Integer.parseInt(empInformations.get(i).getSex())));
                        }

                        familyName.setText(empInformations.get(i).getFamilyName());
                        callingName.setText(empInformations.get(i).getCallingName());
                        email.setText(empInformations.get(i).getEmail());
                        personalAdd.setText(empInformations.get(i).getPrsentAdd());
                        permanentAdd.setText(empInformations.get(i).getPerManentAdd());

                        if (empInformations.get(i).getMarital_status() != null) {
                            marital_status.setText(ms.get(Integer.parseInt(empInformations.get(i).getMarital_status())));
                        }

                        personalAddBangla.setText(empInformations.get(i).getPresent_bangla());
                        permanentAddBangla.setText(empInformations.get(i).getPermanent_bangla());

                        if (empInformations.get(i).getMailingAdd() != null) {
                            mailing_address.setText(ma.get(Integer.parseInt(empInformations.get(i).getMailingAdd())));
                        }

                        if (banglaName == null) {
                            banglaName = "";
                        }
                        nameBP.setText(banglaName);
                    }
                }
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(PersonalData.this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getEmpInformation();
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
            AlertDialog dialog = new AlertDialog.Builder(PersonalData.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getEmpInformation();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {

                dialog.dismiss();
                finish();
            });
        }
    }

    //    --------------------------Transforming Bangla Text-----------------------------
    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }
}