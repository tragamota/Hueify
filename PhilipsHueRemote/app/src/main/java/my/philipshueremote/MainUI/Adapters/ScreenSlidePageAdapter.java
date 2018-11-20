package my.philipshueremote.MainUI.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import my.philipshueremote.MainUI.Fragments.GroupFragment;
import my.philipshueremote.MainUI.Fragments.LampFragment;
import my.philipshueremote.MainUI.Fragments.SceneFragment;

public class ScreenSlidePageAdapter extends FragmentPagerAdapter {
    private final int TOTAL_ITEMS_IN_PAGER = 3;

    public ScreenSlidePageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        Fragment fragment = null;
        switch (index) {
            case 0:
                fragment = new LampFragment();
                break;
            case 1:
                fragment = new GroupFragment();
                break;
            case 2:
                fragment = new SceneFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return TOTAL_ITEMS_IN_PAGER;
    }
}
