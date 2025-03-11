package ttit.com.shuvo.eliteforce.fleet;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.fleet.assignment.RequisitionAssignment;
import ttit.com.shuvo.eliteforce.fleet.fleet_requisition.NewRequisition;
import ttit.com.shuvo.eliteforce.fleet.req_stat.RequisitionStatus;

public class FleetRequisition extends AppCompatActivity {

    MaterialCardView newRequisition;
    MaterialCardView reqStatus;
    MaterialCardView reqApprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_requisition);

        newRequisition = findViewById(R.id.new_fleet_requisition);
        reqStatus = findViewById(R.id.requisition_status);
        reqApprove = findViewById(R.id.fleet_requisition_approval);

        newRequisition.setOnClickListener(v -> {
            Intent intent = new Intent(FleetRequisition.this, NewRequisition.class);
            startActivity(intent);
        });

        reqStatus.setOnClickListener(v -> {
            Intent intent = new Intent(FleetRequisition.this, RequisitionStatus.class);
            startActivity(intent);
        });

        reqApprove.setOnClickListener(v -> {
            Intent intent = new Intent(FleetRequisition.this, RequisitionAssignment.class);
            startActivity(intent);
        });
    }
}