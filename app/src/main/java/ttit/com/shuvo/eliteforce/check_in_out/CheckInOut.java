package ttit.com.shuvo.eliteforce.check_in_out;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayout;


import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.check_in_out.fragments.CheckOngoingFragment;
import ttit.com.shuvo.eliteforce.check_in_out.fragments.CompletedCheckFragment;
import ttit.com.shuvo.eliteforce.check_in_out.interfaces.CheckOutSaveListener;

public class CheckInOut extends AppCompatActivity implements CheckOutSaveListener {

    TabLayout checkTab;

    boolean tabUpdateNeeded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in_out);

        checkTab = findViewById(R.id.check_in_out_tab_layout);

        checkTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.check_in_out_fragment_container,
                                        new CheckOngoingFragment(CheckInOut.this)).commit();
                        break;
                    case 1:
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.check_in_out_fragment_container,
                                        new CompletedCheckFragment(CheckInOut.this)).commit();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (!tabUpdateNeeded) {
                    tabUpdateNeeded = true;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        TabLayout.Tab tabAt = checkTab.getTabAt(0);
        tabUpdateNeeded = false;
        checkTab.selectTab(tabAt);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.check_in_out_fragment_container,
                        new CheckOngoingFragment(CheckInOut.this)).commit();
    }

    @Override
    public void onCheckOut() {
        TabLayout.Tab tabAt = checkTab.getTabAt(0);
        tabUpdateNeeded = false;
        checkTab.selectTab(tabAt);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.check_in_out_fragment_container,
                        new CheckOngoingFragment(CheckInOut.this)).commit();
    }
}