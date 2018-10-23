package my.philipshueremote;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import my.philipshueremote.Init.Activities.HueInitActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //start timer for 5 sec after
        Intent intent = new Intent(this, HueInitActivity.class);
        startActivity(intent);
        finish();
    }
}
