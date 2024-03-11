package ttit.com.shuvo.eliteforce.employeeInfo.transcript;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.Menu;

import android.widget.ScrollView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.employeeInfo.transcript.fragments.FirstFragment;
import ttit.com.shuvo.eliteforce.employeeInfo.transcript.fragments.SecondFragment;

public class EMPTranscript extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emptranscript);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        scrollView = findViewById(R.id.emp_trans_scroll_view);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FirstFragment(EMPTranscript.this)).commit();

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFrag = null;

            if (item.getItemId() == R.id.first_fragment) {
                selectedFrag = new FirstFragment(EMPTranscript.this);
                item.setIcon(R.drawable.star);

                Menu menu = bottomNavigationView.getMenu();
                menu.findItem(R.id.second_fragment).setIcon(R.drawable.star_border);
            }
            else if (item.getItemId() == R.id.second_fragment) {
                selectedFrag = new SecondFragment(EMPTranscript.this);
                item.setIcon(R.drawable.star);

                Menu menu1 = bottomNavigationView.getMenu();
                menu1.findItem(R.id.first_fragment).setIcon(R.drawable.star_border);
            }

            if (selectedFrag != null) {
                scrollView.smoothScrollTo(0,0);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFrag).commit();
            }

            return true;
        });
    }
}