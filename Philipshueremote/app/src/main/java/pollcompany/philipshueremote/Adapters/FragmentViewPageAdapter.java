package pollcompany.philipshueremote.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import pollcompany.philipshueremote.Activities.GroupFragment;
import pollcompany.philipshueremote.Activities.LampFragment;

/**
 * Created by Ian on 29-7-2017.
 */
public class FragmentViewPageAdapter extends FragmentPagerAdapter {

    public FragmentViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new LampFragment();
                break;
            case 1:
                fragment = new GroupFragment();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
