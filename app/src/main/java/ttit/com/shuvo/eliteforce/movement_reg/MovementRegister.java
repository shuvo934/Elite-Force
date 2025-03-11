package ttit.com.shuvo.eliteforce.movement_reg;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.movement_reg.movements.MovementStatus;
import ttit.com.shuvo.eliteforce.movement_reg.new_reg.NewRegister;

public class MovementRegister extends AppCompatActivity {

    MaterialCardView newMovement;
    MaterialCardView moveStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_register);

        newMovement = findViewById(R.id.new_movement_register);
        moveStatus = findViewById(R.id.movement_register_status);

        newMovement.setOnClickListener(v -> {
            Intent intent = new Intent(MovementRegister.this, NewRegister.class);
            startActivity(intent);
        });

        moveStatus.setOnClickListener(v -> {
            Intent intent = new Intent(MovementRegister.this, MovementStatus.class);
            startActivity(intent);
        });
    }
}