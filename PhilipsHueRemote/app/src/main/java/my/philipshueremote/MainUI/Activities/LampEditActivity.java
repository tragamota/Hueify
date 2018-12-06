package my.philipshueremote.MainUI.Activities;

import android.arch.lifecycle.LiveData;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SaturationBar;

import my.philipshueremote.DataCommunication.Requests.PremadeHueRequest;
import my.philipshueremote.DataCommunication.VolleyJsonSocket;
import my.philipshueremote.Database.Entities.Lamp;
import my.philipshueremote.Database.HueDatabase;
import my.philipshueremote.Init.Models.BridgeInfo;
import my.philipshueremote.R;

public class LampEditActivity extends AppCompatActivity {
    private VolleyJsonSocket socket;
    private HueDatabase appDatabase;

    private ColorPicker colorPicker;
    private SaturationBar saturationBar;

    private BridgeInfo selectedBridge;
    private LiveData<Lamp> lamp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp_edit);
        socket = VolleyJsonSocket.getInstance(getApplicationContext());
        appDatabase = HueDatabase.getInstance(getApplicationContext());

        selectedBridge = getIntent().getExtras().getParcelable("BRIDGE");
        short lampId = getIntent().getExtras().getShort("LAMP_ID");
        lamp = appDatabase.lampDAO().getLiveLamp(selectedBridge.getBridgeID(), lampId);

        colorPicker = findViewById(R.id.Main_edit_colorpicker);
        saturationBar = findViewById(R.id.Main_edit_saturation);

        colorPicker.setTouchAnywhereOnColorWheelEnabled(true);
        colorPicker.setShowOldCenterColor(false);
        colorPicker.addSaturationBar(saturationBar);

        lamp.observe(this, lamp1 -> {
            colorPicker.setColor(lamp1.getState().getRGB());
        });

        colorPicker.setOnColorSelectedListener(color -> {
            float[] hsv = new float[3];
            Lamp colorLamp = lamp.getValue();
            if(colorLamp != null) {
                Color.colorToHSV(color, hsv);
                colorLamp.getState().setHue((int) (hsv[0] * (65535/360)));
                colorLamp.getState().setSaturation((short) (hsv[1] * 254));
                socket.addRequestToQueue(PremadeHueRequest.lampPutColorRequest(selectedBridge, colorLamp));
            }
        });
    }
}
