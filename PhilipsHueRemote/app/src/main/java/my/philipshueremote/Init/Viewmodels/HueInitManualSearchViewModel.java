package my.philipshueremote.Init.Viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextWatcher;

import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import my.philipshueremote.DataCommunication.VolleyJsonSocket;
import my.philipshueremote.Init.Models.BridgeInfo;
import my.philipshueremote.Init.Models.IPAddressTextWatcher;

public class HueInitManualSearchViewModel extends AndroidViewModel {
    public int NO_SEARCH = 0;
    public int BRIDGE_SEARCH = 1;
    public int NO_BRIDGE_FOUND = 2;
    public int BRIDGE_FOUND = 3;

    private VolleyJsonSocket jsonSocket;
    private BridgeInfo foundBridge;
    private IPAddressTextWatcher ipWatcher;

    private JsonRequest onGoingRequest;
    private MutableLiveData<Integer> bridgeState;
    private MutableLiveData<Boolean> validIp;

    public HueInitManualSearchViewModel(@NonNull Application application) {
        super(application);
        jsonSocket = VolleyJsonSocket.getInstance(application);
    }

    public LiveData<Integer> getBridgeState() {
        if (bridgeState == null) {
            bridgeState = new MutableLiveData<>();
            bridgeState.setValue(NO_SEARCH);
        }
        return bridgeState;
    }

    public LiveData<Boolean> getValidIpIdenticator() {
        if(validIp == null) {
            validIp = new MutableLiveData<>();
            validIp.postValue(false);
        }
        return validIp;
    }

    public TextWatcher getIPTextWatcher() {
        if(ipWatcher == null) {
            ipWatcher = new IPAddressTextWatcher(validIp);
        }
        return ipWatcher;
    }

    public void resetBridgeState() {
        bridgeState.postValue(NO_SEARCH);
        foundBridge = null;
    }

    public BridgeInfo getFoundBridge() {
        return foundBridge;
    }

    public void startSearching(String ip) {
        String bridgeUrl = "http://" + ip + "/api/noUser/config";
        onGoingRequest = new JsonObjectRequest(bridgeUrl, null, response -> {
            try {
                foundBridge = BridgeInfo.BridgeInfo(ip, 80, response);
                bridgeState.postValue(BRIDGE_FOUND);
            } catch (JSONException e) {
                e.printStackTrace();
                bridgeState.postValue(NO_BRIDGE_FOUND);
            }
        }, error -> bridgeState.postValue(NO_BRIDGE_FOUND));

        jsonSocket.addRequestToQueue(onGoingRequest);
        bridgeState.postValue(BRIDGE_SEARCH);
    }

    public void stopSearching() {
        if(onGoingRequest != null) {
            jsonSocket.cancel(onGoingRequest);
            bridgeState.postValue(NO_BRIDGE_FOUND);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        stopSearching();
    }
}
