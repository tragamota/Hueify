package my.philipshueremote.Init.Viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import my.philipshueremote.DataCommunication.VolleyJsonSocket;
import my.philipshueremote.Init.Models.BridgeInfo;

public class HueInitManualTokenViewModel extends AndroidViewModel {
    public final int NEVER_SEARCHED = 0, SEARCHING = 1, KEY_NOT_EXIST = 2, KEY_EXIST = 3;

    private VolleyJsonSocket volleySocket;
    private JsonRequest accessKeyExistRequest;

    private MutableLiveData<Integer> currentState;
    private BridgeInfo bridgeInfo;

    public HueInitManualTokenViewModel(@NonNull Application application) {
        super(application);
        volleySocket = VolleyJsonSocket.getInstance(application);
    }

    public LiveData<Integer> getCurrentState() {
        if(currentState == null) {
            currentState = new MutableLiveData<>();
            currentState.postValue(NEVER_SEARCHED);
        }
        return currentState;
    }

    public void setBridgeInfo(BridgeInfo bridge) {
        if(bridgeInfo == null) {
            bridgeInfo = bridge;
        }
    }

    public synchronized void checkIfAccessKeyExist(String manualAccessKey) {
        String bridgeUrl = "http://" + bridgeInfo.getIpAddress() + "/api/" + manualAccessKey;
        currentState.postValue(SEARCHING);
        accessKeyExistRequest = new JsonObjectRequest(Request.Method.GET, bridgeUrl, null,
                response -> {
                    try {
                        JSONObject userKeyObject = response.getJSONObject("config").getJSONObject("whitelist");
                        if(userKeyObject.has(manualAccessKey)) {
                            currentState.postValue(KEY_EXIST);
                        }
                    } catch (JSONException e) {
                        currentState.postValue(KEY_NOT_EXIST);
                        e.printStackTrace();
                    }
                },
                error -> currentState.postValue(KEY_NOT_EXIST)
        );
        volleySocket.addRequestToQueue(accessKeyExistRequest);
    }

    private synchronized void stopExistingAccessRequest() {
        if(accessKeyExistRequest != null) {
            volleySocket.cancel(accessKeyExistRequest);
            accessKeyExistRequest = null;
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopExistingAccessRequest();
    }
}
