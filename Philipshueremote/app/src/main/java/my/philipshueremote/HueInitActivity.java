package my.philipshueremote;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class HueInitActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_init);

        FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
        fragTransaction.add(R.id.HueInitFragmentHolder, new HueInitSearchFragment(), "Search");
        fragTransaction.addToBackStack("Search");
        fragTransaction.commit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
