package my.philipshueremote.Init.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import my.philipshueremote.R;

public class HueInitActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hue_init);
        hideActionBar();

        if(getSupportFragmentManager().getFragments().isEmpty()) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Init_Activity_Holder, new HueInitSearchFragment(),"INIT_SEARCH")
                    .addToBackStack("INIT_SEARCH")
                    .commit();
        }
    }

    private void hideActionBar() {
        try {
            getSupportActionBar().hide();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
