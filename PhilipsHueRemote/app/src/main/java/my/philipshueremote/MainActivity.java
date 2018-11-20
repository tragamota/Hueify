package my.philipshueremote;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import my.philipshueremote.Fragments.GroupsFragment;
import my.philipshueremote.Fragments.LampFragment;
import my.philipshueremote.Fragments.ScenesFragment;

import my.philipshueremote.Init.Activities.HueInitActivity;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_PAGES = 3;

    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.activity_main_bottomNavigation);

        bottomNavigation.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_lamps:
                                mPager.setCurrentItem(0);
                                break;

                            case R.id.menu_groups:
                                mPager.setCurrentItem(1);
                                break;

                            case R.id.menu_scenes:
                                mPager.setCurrentItem(3);
                                break;

                        }
                        return true;
                    }
                });


        mPager = findViewById(R.id.activity_main_view_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);

        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch(i){
                    case 0:
                        bottomNavigation.setSelectedItemId(R.id.menu_lamps);
                        break;
                    case 1:
                        bottomNavigation.setSelectedItemId(R.id.menu_groups);
                        break;
                    case 2:
                        bottomNavigation.setSelectedItemId(R.id.menu_scenes);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //start timer for 5 sec after
        Intent intent = new Intent(this, HueInitActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }



    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment f = null;

            switch (position) {
                case 0:
                    f = new LampFragment();
                    break;
                case 1:
                    f = new GroupsFragment();
                    break;
                case 2:
                    f = new ScenesFragment();
                    break;
            }
            return f;
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}
