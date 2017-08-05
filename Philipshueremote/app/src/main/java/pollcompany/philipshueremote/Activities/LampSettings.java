package pollcompany.philipshueremote.Activities;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;
import com.larswerkman.holocolorpicker.ValueBar;

import pollcompany.philipshueremote.AsyncTasks.ColorTask;
import pollcompany.philipshueremote.Lamp;
import pollcompany.philipshueremote.R;

public class LampSettings extends AppCompatActivity {
    private Lamp lamp;
    private ColorPicker colorPicker;
    private ValueBar brightnessBar;
    private SaturationBar saturationBar;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_settings);

        lamp = (Lamp) getIntent().getExtras().getSerializable("LAMP_OBJECT");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Lamp Settings");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        EditText nameText = (EditText) findViewById(R.id.LampNameSetting);
        nameText.setText(lamp.getName());

        colorPicker = (ColorPicker) findViewById(R.id.ColorPicker);
        brightnessBar = (ValueBar) findViewById(R.id.valueBar);
        saturationBar = (SaturationBar) findViewById(R.id.saturationBar);

        colorPicker.setTouchAnywhereOnColorWheelEnabled(true);
        colorPicker.setShowOldCenterColor(false);
        colorPicker.addSaturationBar(saturationBar);
        colorPicker.addValueBar(brightnessBar);

        colorPicker.setColor(lamp.calculateHSV(1.0f));
        colorPicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChanged(final int color) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new ColorTask(color).execute(lamp.getId());
                    }
                }, 200);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}