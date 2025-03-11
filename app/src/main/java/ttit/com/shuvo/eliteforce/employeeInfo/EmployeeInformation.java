package ttit.com.shuvo.eliteforce.employeeInfo;

import static ttit.com.shuvo.eliteforce.dashboard.Dashboard.selectedImage;
import static ttit.com.shuvo.eliteforce.login.Login.userDesignations;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.employeeInfo.jobDesc.JobDescription;
import ttit.com.shuvo.eliteforce.employeeInfo.performance.PerformanceApp;
import ttit.com.shuvo.eliteforce.employeeInfo.personal.PersonalData;
import ttit.com.shuvo.eliteforce.employeeInfo.transcript.EMPTranscript;

public class EmployeeInformation extends AppCompatActivity {

    MaterialCardView personal;
    MaterialCardView jobDesc;
    MaterialCardView performance;
    MaterialCardView empTrans;

    TextView userName;
    TextView designation;
    TextView department;
    ImageView userImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_information);

        personal = findViewById(R.id.personal_data);
        jobDesc = findViewById(R.id.job_description);
        performance = findViewById(R.id.performance_app);
        empTrans = findViewById(R.id.emp_trans);

        userName = findViewById(R.id.user_name_in_emp_info);
        department = findViewById(R.id.user_depart_in_emp_info);
        designation = findViewById(R.id.user_desg_in_emp_info);
        userImage = findViewById(R.id.user_pic_in_emp_information);

        if (selectedImage != null) {
            Glide.with(getApplicationContext())
                    .load(selectedImage)
                    .fitCenter()
                    .into(userImage);
        }
        else {
            userImage.setImageResource(R.drawable.profile);
        }

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
            userName.setText(empFullName);
        }

        if (!userDesignations.isEmpty()) {
            String jsmName = userDesignations.get(0).getJsm_name();
            if (jsmName == null) {
                jsmName = "";
            }
            designation.setText(jsmName);

            String deptName = userDesignations.get(0).getDiv_name();
            if (deptName == null) {
                deptName = "";
            }
            department.setText(deptName);
        }

        personal.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeInformation.this, PersonalData.class);
            startActivity(intent);
        });

        jobDesc.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeInformation.this, JobDescription.class);
            startActivity(intent);
        });

        performance.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeInformation.this, PerformanceApp.class);
            startActivity(intent);
        });

        empTrans.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeInformation.this, EMPTranscript.class);
            startActivity(intent);
        });
    }
}