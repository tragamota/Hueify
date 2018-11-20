package my.philipshueremote.Discovery;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.net.nsd.NsdManager;
import android.net.nsd.NsdServiceInfo;

import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import my.philipshueremote.DataCommunication.VolleyJsonSocket;
import my.philipshueremote.Init.Models.BridgeInfo;
import my.philipshueremote.Init.Models.SearchingStates;

public class MultiCastDiscovery implements Discoverable {
    private NsdManager bridgeDiscoveryManager;
    private NsdManager.DiscoveryListener bridgeDiscoveryListener;
    private VolleyJsonSocket volleySocket;

    private MutableLiveData<SearchingStates> state;
    private List<BridgeInfo> bridges;
    private List<JsonRequest> requests;
    private Timer detectionTimer;

    public MultiCastDiscovery(Application appContext, MutableLiveData<SearchingStates> state) {
        bridgeDiscoveryManager = (NsdManager) appContext.getSystemService(Context.NSD_SERVICE);
        volleySocket = VolleyJsonSocket.getInstance(appContext);

        this.state = state;
        bridges = new ArrayList<>();
        requests = new ArrayList<>();
        detectionTimer = new Timer();

        createListener();
    }

    public List<BridgeInfo> getBridges() {
        return bridges;
    }

    @Override
    public void onStart() {
        if(state.getValue() != SearchingStates.SEARCHING) {
            state.postValue(SearchingStates.SEARCHING);
            bridges.clear();
            bridgeDiscoveryManager.discoverServices("_hue._tcp", NsdManager.PROTOCOL_DNS_SD, bridgeDiscoveryListener);
        }
    }

    @Override
    public void onStop() {
        if(state.getValue() == SearchingStates.SEARCHING) {
            state.postValue(bridges.isEmpty() ? SearchingStates.WAITING : SearchingStates.FOUND);
            bridgeDiscoveryManager.stopServiceDiscovery(bridgeDiscoveryListener);

            for(JsonRequest request : requests) {
                volleySocket.cancel(request);
                requests.remove(request);
            }
            detectionTimer.cancel();
        }
    }

    private void createListener() {
        bridgeDiscoveryListener = new NsdManager.DiscoveryListener() {
            @Override
            public void onStartDiscoveryFailed(String s, int i) {

            }

            @Override
            public void onStopDiscoveryFailed(String s, int i) {

            }

            @Override
            public void onDiscoveryStarted(String s) {
                detectionTimer = new Timer();
                detectionTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        System.out.println("Timer is done!");
                        onStop();
                    }
                }, 5000);
            }

            @Override
            public void onDiscoveryStopped(String s) {

            }

            @Override
            public void onServiceFound(NsdServiceInfo nsdServiceInfo) {
                bridgeDiscoveryManager.resolveService(nsdServiceInfo, new NsdManager.ResolveListener() {
                    @Override
                    public void onResolveFailed(NsdServiceInfo nsdServiceInfo, int i) {

                    }

                    @Override
                    public void onServiceResolved(NsdServiceInfo nsdServiceInfo) {
                        getBridgeInfo(nsdServiceInfo.getHost().getHostAddress(), nsdServiceInfo.getPort());
                    }
                });
            }

            @Override
            public void onServiceLost(NsdServiceInfo nsdServiceInfo) {

            }
        };
    }

    private void getBridgeInfo(String ipAdress, int portNumber) {
        String bridgeUrl = "http://" + ipAdress + ":" + portNumber + "/api/NoUser/config";

        JsonObjectRequest request = new JsonObjectRequest(bridgeUrl,null , response -> {
            try {
                bridges.add(BridgeInfo.BridgeInfo(ipAdress, portNumber, response));
            } catch (JSONException e) {
                e.printStackTrace();
                requests.remove(this);
            } finally {
                requests.remove(this);
            }
        }, error -> requests.remove(this));

        requests.add(request);
        volleySocket.addRequestToQueue(request);
    }
}
