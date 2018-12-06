package my.philipshueremote.DataCommunication.Requests;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import my.philipshueremote.Database.Entities.Lamp;
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

    public static JsonRequest lampPutOnOffRequest(BridgeInfo bridge, Lamp lamp, Response.Listener onSuccess,
                                                  Response.ErrorListener onError) {
        String hueLightUrl = "http://" + bridge.getIpAddress() +
                "/api/" + bridge.getBridgeAccessKey() + "/lights" + "/" + lamp.getLampApiID() + "/state";
        Map<String, Object> onJsonObject = new HashMap<>();
        onJsonObject.put("on", lamp.getState().isOn());

        return new JsonObjectRequest(Request.Method.PUT, hueLightUrl, new JSONObject(onJsonObject), onSuccess, onError);
    }

    public static JsonRequest lampPutColorRequest(BridgeInfo bridge, Lamp lamp) {
        String hueLightUrl = "http://" + bridge.getIpAddress() +
                "/api/" + bridge.getBridgeAccessKey() + "/lights" + "/" + lamp.getLampApiID() + "/state";
        Map<String, Object> onJsonObject = new HashMap<>();
        onJsonObject.put("hue", lamp.getState().getHue());
        onJsonObject.put("sat", lamp.getState().getSaturation());

        return new JsonObjectRequest(Request.Method.PUT, hueLightUrl, new JSONObject(onJsonObject), null, null);
    }

    public static JsonRequest lampPutBrightnessRequest(BridgeInfo bridge, Lamp lamp) {
        String hueLightUrl = "http://" + bridge.getIpAddress() +
                "/api/" + bridge.getBridgeAccessKey() + "/lights" + "/" + lamp.getLampApiID() + "/state";
        Map<String, Object> onJsonObject = new HashMap<>();
        onJsonObject.put("bri", lamp.getState().getBrightness());

        return new JsonObjectRequest(Request.Method.PUT, hueLightUrl, new JSONObject(onJsonObject), null, null);
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
