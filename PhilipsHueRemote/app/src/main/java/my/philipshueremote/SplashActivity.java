package my.philipshueremote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import my.philipshueremote.Init.Activities.HueInitActivity;
import my.philipshueremote.MainUI.MainAppActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //start timer for 5 sec after
        Intent intent = new Intent(this, MainAppActivity.class);
        startActivity(intent);
        finish();
    }
}
