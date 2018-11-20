package my.philipshueremote.Init.Viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.room.Room;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import my.philipshueremote.DataCommunication.CustomJsonArrayRequest;
import my.philipshueremote.DataCommunication.VolleyJsonSocket;
import my.philipshueremote.Database.HueDatabase;
import my.philipshueremote.Init.Models.BridgeInfo;

public class HueInitTokenViewModel extends AndroidViewModel {
    private VolleyJsonSocket volleySocket;

    private MutableLiveData<String> accessToken;
    private MutableLiveData<Integer> accessWaitingProcess;
    private CountDownTimer buttonPressCounter;
    private JsonRequest onGoingUserRequest;

    private boolean initialRequestDone;
    private BridgeInfo bridgeInfo;

    public HueInitTokenViewModel(@NonNull Application application) {
        super(application);
        volleySocket = VolleyJsonSocket.getInstance(application);
        initialRequestDone = false;
    }

    public boolean getInitialRequestDone() {
        return initialRequestDone;
    }

    public void setInitialRequestDone(boolean value) {
        initialRequestDone = value;
    }

    public void setBridgeInfo(BridgeInfo info) {
        if(bridgeInfo == null) {
            bridgeInfo = info;
        }
    }

    public LiveData<String> getAccessToken() {
        if(accessToken == null) {
            accessToken = new MutableLiveData<>();
            accessToken.postValue(null);
        }
        return accessToken;
    }

    public LiveData<Integer> getTimeToGo() {
        if(accessWaitingProcess == null) {
            accessWaitingProcess = new MutableLiveData<>();
            accessWaitingProcess.postValue(0);
        }
        return accessWaitingProcess;
    }

    public void startAccessToken() {
        retrieveAccessToken();
        buttonPressCounter = new CountDownTimer(30000, 750) {
            @Override
            public void onTick(long l) {
                int percentage = (int) ((l/30000.0f)*100.0f);
                accessWaitingProcess.postValue(percentage);
            }

            @Override
            public void onFinish() {
                accessWaitingProcess.postValue(0);
                volleySocket.cancel(onGoingUserRequest);
            }
        }.start();
    }

    public void stopAccessToken() {
        if(buttonPressCounter != null) {
            buttonPressCounter.cancel();
        }
        if(onGoingUserRequest != null) {
            volleySocket.cancel(onGoingUserRequest);
        }
    }

    private synchronized void retrieveAccessToken() {
        String instanceID = UUID.randomUUID().toString().substring(0,20);
        String bridgeUrl = "http://" + bridgeInfo.getIpAddress() + "/api";

        Map<String, Object> accessInfoMap = new HashMap<>();
        accessInfoMap.put("devicetype", ("hueify#"+ instanceID));

        onGoingUserRequest = new CustomJsonArrayRequest(Request.Method.POST, bridgeUrl,
                new JSONObject(accessInfoMap),
                response -> {
                try {
                    JSONObject accessObject = (JSONObject) response.get(0);
                    if(accessObject.has("error")) {
                        retrieveAccessToken();
                    }
                    else {
                        JSONObject successObject = accessObject.getJSONObject("success");
                        String accessKey = successObject.getString("username");
                        bridgeInfo.setBridgeAccessKey(accessKey);
                        saveBridgeInfoInDB(bridgeInfo);
                        accessToken.postValue(accessKey);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    retrieveAccessToken();
                }},
                error -> retrieveAccessToken()
        );
        volleySocket.addRequestToQueue(onGoingUserRequest);
    }

    private void saveBridgeInfoInDB(BridgeInfo bridge) {
        if(bridge.getBridgeAccessKey() != null) {
            HueDatabase database = HueDatabase.getInstance(getApplication());
            database.bridgeDAO().insertBridgeInformation(bridgeInfo);

            database.closeDatabase();
            database = HueDatabase.getInstance(getApplication());
            for(BridgeInfo info :  database.bridgeDAO().getAllBridgeInformation()) {
                System.out.println(info.getBridgeID() + "\t" + info.getBridgeAccessKey());
            }
        }
    }
}
