package pollcompany.philipshueremote.Activities;

import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;


import pollcompany.philipshueremote.Adapters.FragmentViewPageAdapter;
import pollcompany.philipshueremote.Group;
import pollcompany.philipshueremote.R;

public class MainHolderActivity extends AppCompatActivity {
    private ViewPager mainViewPage;
    private FragmentViewPageAdapter viewPageAdapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_holder);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        if(!preferences.contains("userCode")) {

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Lamps"));
        tabLayout.addTab(tabLayout.newTab().setText("Groups"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPageAdapter = new FragmentViewPageAdapter(getSupportFragmentManager());
        mainViewPage = (ViewPager) findViewById(R.id.main_activity_view_pager);
        mainViewPage.setAdapter(viewPageAdapter);
        mainViewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mainViewPage.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(android.R.color.white, null));
    }
}