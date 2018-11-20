package my.philipshueremote.MainUI;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import my.philipshueremote.MainUI.Adapters.ScreenSlidePageAdapter;
import my.philipshueremote.MainUI.ViewModels.MainAppViewModel;
import my.philipshueremote.R;
import my.philipshueremote.SplashActivity;

public class MainAppActivity extends AppCompatActivity {
    private MainAppViewModel viewModel;

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fragmentActionButton;

    private List<View.OnClickListener> actionButtonClickActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        viewModel = ViewModelProviders.of(this).get(MainAppViewModel.class);

        viewPager = findViewById(R.id.Main_view_pager);
        bottomNavigation = findViewById(R.id.Main_bottom_navigation);
        fragmentActionButton = findViewById(R.id.Main_action_button);

        viewPager.setAdapter(new ScreenSlidePageAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int index) {
                switch (index) {
                    case 0:
                        bottomNavigation.setSelectedItemId(R.id.main_menu_lamps_item);
                        fragmentActionButton.setImageResource(R.drawable.ic_sync_check);
                        fragmentActionButton.setOnClickListener(actionButtonClickActions.get(0));
                        break;
                    case 1:
                        bottomNavigation.setSelectedItemId(R.id.main_menu_groups_item);
                        fragmentActionButton.setImageResource(R.drawable.ic_plus_add);
                        fragmentActionButton.setOnClickListener(actionButtonClickActions.get(1));
                        break;
                    case 2:
                        bottomNavigation.setSelectedItemId(R.id.main_menu_scenes_item);
                        fragmentActionButton.setImageResource(R.drawable.ic_plus_add);
                        fragmentActionButton.setOnClickListener(actionButtonClickActions.get(2));
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        bottomNavigation.setOnNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.main_menu_lamps_item:
                    viewPager.setCurrentItem(0);
                    fragmentActionButton.setImageResource(R.drawable.ic_sync_check);
                    fragmentActionButton.setOnClickListener(actionButtonClickActions.get(0));
                    break;
                case R.id.main_menu_groups_item:
                    viewPager.setCurrentItem(1);
                    fragmentActionButton.setImageResource(R.drawable.ic_plus_add);
                    fragmentActionButton.setOnClickListener(actionButtonClickActions.get(1));
                    break;
                case R.id.main_menu_scenes_item:
                    viewPager.setCurrentItem(2);
                    fragmentActionButton.setImageResource(R.drawable.ic_plus_add);
                    fragmentActionButton.setOnClickListener(actionButtonClickActions.get(2));
                    break;
            }
            return true;
        });

        actionButtonClickActions = new ArrayList<>(3);
        actionButtonClickActions.add(view -> {
            System.out.println("lampen toevoegen!");
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        });
        actionButtonClickActions.add(view -> {
            System.out.println("groepen toevoegen!");
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        });
        actionButtonClickActions.add(view -> {
            System.out.println("Scene toevoegen!");
            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        viewModel.stopHueBackgroundService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(viewPager.getCurrentItem() == 0 ) {
            fragmentActionButton.setImageResource(R.drawable.ic_sync_check);
            fragmentActionButton.setOnClickListener(actionButtonClickActions.get(0));
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.stopHueBackgroundService();
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() != 0) {
            viewPager.setCurrentItem(0);
            bottomNavigation.setSelectedItemId(R.id.main_menu_lamps_item);
        }
        else {
            super.onBackPressed();
        }
    }
}