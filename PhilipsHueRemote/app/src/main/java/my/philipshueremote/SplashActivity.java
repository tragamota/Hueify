package my.philipshueremote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.Future;

import my.philipshueremote.Database.HueDatabase;
import my.philipshueremote.Init.Activities.HueInitActivity;
import my.philipshueremote.MainUI.MainAppActivity;

public class SplashActivity extends AppCompatActivity {
    private int totalBridgeSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Dark);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Future bridgeSize = HueDatabase.getInstance(this).performBackgroundQuery(() -> {
            totalBridgeSize = HueDatabase.getInstance(this).bridgeDAO().sizeOfBridges();
        });

        while(!bridgeSize.isDone()) {
            Thread.yield();
        }

        Intent intent;
        if(totalBridgeSize >= 1) {
            intent = new Intent(this, MainAppActivity.class);
        }
        else {
            intent = new Intent(this, HueInitActivity.class);
        }
        startActivity(intent);
        finish();
    }
}
