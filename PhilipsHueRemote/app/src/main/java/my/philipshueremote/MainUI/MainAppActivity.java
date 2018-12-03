package my.philipshueremote.MainUI;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import my.philipshueremote.Init.Models.SearchingStates;
import my.philipshueremote.MainUI.Adapters.ScreenSlidePageAdapter;
import my.philipshueremote.MainUI.ViewModels.MainAppViewModel;
import my.philipshueremote.R;
import my.philipshueremote.SplashActivity;

public class MainAppActivity extends AppCompatActivity {
    private MainAppViewModel viewModel;

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigation;
    private FloatingActionButton fragmentActionButton;
    private MenuItem settingMenuItem;

    private CardView connectionStatusCard;
    private TextView connectionStatusText;
    private ProgressBar connectionStatusProBar;
    private ImageView connectionStatusCancelImage;

    private List<View.OnClickListener> actionButtonClickActions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        viewModel = ViewModelProviders.of(this).get(MainAppViewModel.class);

        viewPager = findViewById(R.id.Main_view_pager);
        bottomNavigation = findViewById(R.id.Main_bottom_navigation);
        fragmentActionButton = findViewById(R.id.Main_action_button);
        connectionStatusCard = findViewById(R.id.include);
        connectionStatusText = findViewById(R.id.Main_connection_Text);
        connectionStatusProBar = findViewById(R.id.Main_connection_waiting_bar);
        connectionStatusCancelImage = findViewById(R.id.Main_connection_close);

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

        viewModel.getDiscoveryState().observe(this, searchingStates ->  {
            int color;
            if(viewModel.isConnectionStateHidden() &&
                    searchingStates == SearchingStates.WAITING) {
                connectionStatusCard.setVisibility(View.GONE);
                return;
            }
            else {
                connectionStatusCard.setVisibility(View.VISIBLE);
            }
            switch(searchingStates) {
                case SEARCHING:
                    color = connectionStatusCard.getCardBackgroundColor().getDefaultColor();
                    connectionStatusCard.setCardBackgroundColor(color);
                    connectionStatusText.setText("Verbinden...");
                    connectionStatusProBar.setVisibility(View.VISIBLE);
                    connectionStatusCancelImage.setVisibility(View.INVISIBLE);
                    break;
                case FOUND:
                    color = getResources().getColor(R.color.colorDarkPassTextLight);
                    connectionStatusCard.setCardBackgroundColor(color);
                    connectionStatusText.setText("Verbonden...");
                    connectionStatusProBar.setVisibility(View.INVISIBLE);
                    connectionStatusCancelImage.setVisibility(View.VISIBLE);
                    break;
                case WAITING:
                    color = getResources().getColor(R.color.colorDarkFailTextLight);
                    connectionStatusCard.setCardBackgroundColor(color);
                    connectionStatusText.setText("Kon niet verbinden...");
                    connectionStatusProBar.setVisibility(View.INVISIBLE);
                    connectionStatusCancelImage.setVisibility(View.VISIBLE);
                    break;
            }
        });

        connectionStatusCard.setOnClickListener(view -> {
            connectionStatusCard.setVisibility(View.GONE);
            viewModel.hideConnectionState();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(viewPager.getCurrentItem() == 0) {
            fragmentActionButton.setImageResource(R.drawable.ic_sync_check);
            fragmentActionButton.setOnClickListener(actionButtonClickActions.get(0));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        settingMenuItem = menu.findItem(R.id.main_menu_settings);
        getMenuInflater().inflate(R.menu.main_app_toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.main_menu_settings:
                Intent intent = new Intent(this, MainSettingActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(viewPager.getCurrentItem() != viewModel.FIRST_VIEWPAGER_FRAGMENT) {
            viewPager.setCurrentItem(viewModel.FIRST_VIEWPAGER_FRAGMENT);
            bottomNavigation.setSelectedItemId(R.id.main_menu_lamps_item);
        }
        else {
            super.onBackPressed();
        }
    }
}
