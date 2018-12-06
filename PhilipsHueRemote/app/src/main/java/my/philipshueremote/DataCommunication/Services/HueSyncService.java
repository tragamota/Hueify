package my.philipshueremote.DataCommunication.Services;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import my.philipshueremote.DataCommunication.Requests.PremadeHueRequest;
import my.philipshueremote.DataCommunication.VolleyJsonSocket;
import my.philipshueremote.Database.Entities.Lamp;
import my.philipshueremote.Database.HueDatabase;
import my.philipshueremote.Init.Models.BridgeInfo;

public class HueSyncService {
    private static volatile HueSyncService Instance;

    private VolleyJsonSocket socket;
    private HueDatabase appDatabase;
    private Timer lampFreqTimer, groupFreqTimer;

    private MutableLiveData<BridgeInfo> selectedBridge;
    private JsonRequest lampJsonRequest, groupJsonRequest, sceneJsonRequest;
    private Response.Listener<JSONObject> onLampSuccess, onGroupSuccess, onSceneSuccess;
    private Response.ErrorListener onLampError, onGroupError, onSceneError;

    private HueSyncService(Context appContext) {
        socket = VolleyJsonSocket.getInstance(appContext);
        appDatabase = HueDatabase.getInstance(appContext);
        selectedBridge = new MutableLiveData<>();
        BridgeInfo tempSelectedBridge = new BridgeInfo("192.168.0.33", 80, "bla", "1.2.8", "asdasd", "asdasdad");
        tempSelectedBridge.setBridgeAccessKey("newdeveloper");
        selectedBridge.setValue(tempSelectedBridge);

        onLampSuccess = response -> {
            appDatabase.performBackgroundQuery(() -> {
                List<Lamp> lampsToUpdate = new ArrayList<>();
                Iterator<String> lampKeys = response.keys();
                while (lampKeys.hasNext()) {
                    String lampKey = lampKeys.next();
                    try {
                        lampsToUpdate.add(Lamp.parseFromJson(selectedBridge.getValue().getBridgeID(),
                                Short.decode(lampKey),
                                response.getJSONObject(lampKey)));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                if(appDatabase.lampDAO().containsLamp(selectedBridge.getValue().getBridgeID(), lampsToUpdate.get(0).getLampApiID()) < 0) {
                    appDatabase.lampDAO().insertLamps(lampsToUpdate);
                }
                else {
                    appDatabase.lampDAO().updateLamps(lampsToUpdate);
                }

                lampJsonRequest = null;
            });
        };

        onGroupSuccess = response -> {
            Iterator<String> groupKeys = response.keys();
            while (groupKeys.hasNext()) {
                try {
                    //parse that with a static method!
                    response.get(groupKeys.next());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            groupJsonRequest = null;
        };

        onSceneSuccess = response -> {
            Iterator<String> sceneKeys = response.keys();
            while (sceneKeys.hasNext()) {
                try {

                    response.get(sceneKeys.next());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            sceneJsonRequest = null;
        };

        onLampError = error -> {
            lampJsonRequest = null;
        };
        onGroupError = error -> {
            groupJsonRequest = null;
        };
        onSceneError = error -> {
            sceneJsonRequest = null;
        };
    }

    public synchronized static HueSyncService getInstance(Context appContext) {
        if (Instance == null) {
            Instance = new HueSyncService(appContext);
        }
        return Instance;
    }

    public LiveData<BridgeInfo> getSelectedBridge() {
        return selectedBridge;
    }

    public void setSelectedBridge(BridgeInfo bridge) {
        if (lampFreqTimer != null && groupFreqTimer != null) {
            stopService();
            selectedBridge.setValue(bridge);
            startService();
        } else {
            selectedBridge.postValue(bridge);
        }
    }

    public void startService() {
        if (lampFreqTimer == null && groupFreqTimer == null && selectedBridge.getValue() != null) {
            socket.addRequestToQueue(PremadeHueRequest.sceneGetRequest(selectedBridge.getValue(), onSceneSuccess, onSceneError));
            lampFreqTimer = new Timer();
            lampFreqTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (lampJsonRequest == null) {
                        socket.addRequestToQueue(
                                (lampJsonRequest = PremadeHueRequest.lampGetRequest(selectedBridge.getValue(),
                                        onLampSuccess,
                                        onLampError)));
                    }
                }
            }, 0, 10000);
            groupFreqTimer = new Timer();
            groupFreqTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (groupJsonRequest == null) {
                        socket.addRequestToQueue(
                                (groupJsonRequest = PremadeHueRequest.groupGetRequest(selectedBridge.getValue(),
                                        onGroupSuccess,
                                        onGroupError)));
                    }
                }
            }, 0, 1000 * 60 * 2);
        }
    }

    public void stopService() {
        if (lampFreqTimer != null) {
            lampFreqTimer.cancel();
            lampFreqTimer = null;
        }
        if (groupFreqTimer != null) {
            groupFreqTimer.cancel();
            groupFreqTimer = null;
        }
    }
}
