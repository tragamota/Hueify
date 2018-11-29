package my.philipshueremote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import my.philipshueremote.Database.HueDatabase;
import my.philipshueremote.Init.Activities.HueInitActivity;
import my.philipshueremote.MainUI.MainAppActivity;
import my.philipshueremote.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        HueDatabase.getInstance(this.getApplicationContext())
                .bridgeDAO().sizeOfBridges().observe(this, integer -> {
            Intent intent;
            if(integer < 1) {
                intent = new Intent(this, MainAppActivity.class);
            }
            else {
                intent = new Intent(this, HueInitActivity.class);
            }
            startActivity(intent);
            finish();
        });
    }
}
