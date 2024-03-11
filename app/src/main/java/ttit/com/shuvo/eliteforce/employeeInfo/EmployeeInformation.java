package ttit.com.shuvo.eliteforce.employeeInfo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_information);

        personal = findViewById(R.id.personal_data);
        jobDesc = findViewById(R.id.job_description);
        performance = findViewById(R.id.performance_app);
        empTrans = findViewById(R.id.emp_trans);

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