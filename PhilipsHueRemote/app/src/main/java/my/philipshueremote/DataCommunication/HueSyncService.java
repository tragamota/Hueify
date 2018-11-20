package my.philipshueremote.DataCommunication;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

import my.philipshueremote.Database.HueDatabase;
import my.philipshueremote.Init.Models.BridgeInfo;

public class HueSyncService {
    private VolleyJsonSocket socket;
    private HueDatabase appDatabase;
    private Timer freqTimer;

    private BridgeInfo selectedBridge;
    private Response.Listener<JSONObject> onLampSuccess, onGroupSuccess, onSceneSuccess;
    private Response.ErrorListener onError;

    public HueSyncService(Context appContext) {
        socket = VolleyJsonSocket.getInstance(appContext);
        appDatabase = HueDatabase.getInstance(appContext);
        selectedBridge = new BridgeInfo("192.168.0.33", 80, "bla", "1.2.8", "asdasd", "asdasdad");
        selectedBridge.setBridgeAccessKey("newdeveloper");

        onLampSuccess = response -> {
            System.out.println(response.toString());
            Iterator<String> lampKeys = response.keys();
            while(lampKeys.hasNext()) {
                try {
                    //parse that with a static method!
                    response.get(lampKeys.next());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        onGroupSuccess = response -> {
            System.out.println(response.toString());
            Iterator<String> groupKeys = response.keys();
            while(groupKeys.hasNext()) {
                try {
                    //parse that with a static method!
                    response.get(groupKeys.next());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        onSceneSuccess = response -> {
            System.out.println(response.toString());
            Iterator<String> sceneKeys = response.keys();
            while(sceneKeys.hasNext()) {
                try {
                    //parse that with a static method!
                    response.get(sceneKeys.next());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        onError = error -> {
            stopService();
            Toast.makeText(appContext, "Bridge is niet bereikbaar", Toast.LENGTH_SHORT).show();
        };
    }

    public void setSelectedBridge(BridgeInfo bridge) {
        if(freqTimer != null) {
            stopService();
            selectedBridge = bridge;
            startService();
        }
        else {
            selectedBridge = bridge;
        }
    }

    public void startService() {
        if(freqTimer == null && selectedBridge != null) {
            freqTimer = new Timer();
            freqTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    socket.addRequestToQueue(PremadeHueRequest.lampGetRequest(selectedBridge, onLampSuccess, onError));
                    socket.addRequestToQueue(PremadeHueRequest.groupGetRequest(selectedBridge, onGroupSuccess, null));
                    socket.addRequestToQueue(PremadeHueRequest.sceneGetRequest(selectedBridge, onSceneSuccess, null));
                }
            },0,7500);
        }
    }

    public void stopService() {
        if(freqTimer != null) {
            freqTimer.cancel();
            freqTimer = null;
        }
    }
}
