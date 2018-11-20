package my.philipshueremote.DataCommunication;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import my.philipshueremote.Init.Models.BridgeInfo;

public class PremadeHueRequest {
    public static JsonRequest lampGetRequest(BridgeInfo bridgeInfo, Response.Listener onSuccess,
                                             Response.ErrorListener onError) {
        String hueLightUrl = "http://" + bridgeInfo.getIpAddress() +
                "/api/" + bridgeInfo.getBridgeAccessKey() + "/lights";
        return new JsonObjectRequest(Request.Method.GET,
                hueLightUrl, null,
                onSuccess, onError);
    }

    public static JsonRequest groupGetRequest(BridgeInfo bridgeInfo, Response.Listener onSuccess,
                                              Response.ErrorListener onError) {
        String hueGroupUrl = "http://" + bridgeInfo.getIpAddress() +
                "/api/" + bridgeInfo.getBridgeAccessKey() + "/groups";
        return new JsonObjectRequest(Request.Method.GET,
                hueGroupUrl, null,
                onSuccess, onError);
    }

    public static JsonRequest sceneGetRequest(BridgeInfo bridgeInfo, Response.Listener onSuccess,
                                              Response.ErrorListener onError) {
        String hueSceneUrl = "http://" + bridgeInfo.getIpAddress() +
                "/api/" + bridgeInfo.getBridgeAccessKey() + "/scenes";
        return new JsonObjectRequest(Request.Method.GET,
                hueSceneUrl, null,
                onSuccess, onError);
    }
}
