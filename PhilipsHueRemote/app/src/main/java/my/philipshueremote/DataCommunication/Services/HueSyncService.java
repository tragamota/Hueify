package my.philipshueremote.DataCommunication.Services;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
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
    private Timer freqTimer;

    private MutableLiveData<BridgeInfo> selectedBridge;
    private JsonRequest lampJsonRequest, groupJsonRequest, sceneJsonRequest;
    private Response.Listener<JSONObject> onLampSuccess, onGroupSuccess, onSceneSuccess;

    private HueSyncService(Context appContext) {
        socket = VolleyJsonSocket.getInstance(appContext);
        appDatabase = HueDatabase.getInstance(appContext);
        selectedBridge = new MutableLiveData<>();
        BridgeInfo tempSelectedBridge = new BridgeInfo("145.48.205.33", 80, "bla", "1.2.8", "asdasd", "asdasdad");
        tempSelectedBridge.setBridgeAccessKey("iYrmsQq1wu5FxF9CPqpJCnm1GpPVylKBWDUsNDhB");
        selectedBridge.setValue(tempSelectedBridge);

        onLampSuccess = response -> {
            System.out.print(response.toString());
            Iterator<String> lampKeys = response.keys();
            while (lampKeys.hasNext()) {
                String lampKey = lampKeys.next();

                Lamp lampEntity;
                try {
                    lampEntity = Lamp.parseFromJson(selectedBridge.getValue().getBridgeID(),
                            Short.decode(lampKey),
                            response.getJSONObject(lampKey));
                } catch (JSONException e) {
                    e.printStackTrace();
                    break;
                }

                appDatabase.performBackgroundQuery(() -> {
                    if (appDatabase.lampDAO().containsLamp(lampEntity.getBridgeID(), lampEntity.getLampApiID()) > 0) {
                        appDatabase.lampDAO().updateLamp(lampEntity);
                    } else {
                        appDatabase.lampDAO().insertLamp(lampEntity);
                    }
                });
            }
            lampJsonRequest = null;
        };

        onGroupSuccess = response -> {
            //System.out.println(response.toString());
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
            //System.out.println(response.toString());
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
        if (freqTimer != null) {
            stopService();
            selectedBridge.postValue(bridge);
            startService();
        } else {
            selectedBridge.postValue(bridge);
        }
    }

    public void startService() {
        if (freqTimer == null && selectedBridge != null) {
            freqTimer = new Timer();
            freqTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if(lampJsonRequest == null) {
                        socket.addRequestToQueue(
                                (lampJsonRequest = PremadeHueRequest.lampGetRequest(selectedBridge.getValue(),
                                        onLampSuccess,
                                        null)));
                    }
                    if(groupJsonRequest == null) {
                        //socket.addRequestToQueue((groupJsonRequest = PremadeHueRequest.groupGetRequest(selectedBridge.getValue(), onGroupSuccess, null)));
                    }
                    if(sceneJsonRequest == null) {
                        //socket.addRequestToQueue((sceneJsonRequest = PremadeHueRequest.sceneGetRequest(selectedBridge.getValue(), onSceneSuccess, null)));
                    }
                }
            }, 0, 10000);
        }
    }

    public void stopService() {
        if (freqTimer != null) {
            freqTimer.cancel();
            freqTimer = null;
        }
    }
}
