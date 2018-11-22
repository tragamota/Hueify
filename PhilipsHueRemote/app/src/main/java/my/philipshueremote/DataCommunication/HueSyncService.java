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
    private static volatile HueSyncService Instance;

    private VolleyJsonSocket socket;
    private HueDatabase appDatabase;
    private Timer freqTimer;

    private BridgeInfo selectedBridge;
    private Response.Listener<JSONObject> onLampSuccess, onGroupSuccess, onSceneSuccess;

    private HueSyncService(Context appContext) {
        socket = VolleyJsonSocket.getInstance(appContext);
        appDatabase = HueDatabase.getInstance(appContext);
        selectedBridge = new BridgeInfo("145.48.205.33", 80, "bla", "1.2.8", "asdasd", "asdasdad");
        selectedBridge.setBridgeAccessKey("iYrmsQq1wu5FxF9CPqpJCnm1GpPVylKBWDUsNDhB");

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
    }

    public synchronized static HueSyncService getInstance(Context appContext) {
        if(Instance == null) {
            Instance = new HueSyncService(appContext);
        }
        return Instance;
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
                    socket.addRequestToQueue(PremadeHueRequest.lampGetRequest(selectedBridge, onLampSuccess, null));
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
