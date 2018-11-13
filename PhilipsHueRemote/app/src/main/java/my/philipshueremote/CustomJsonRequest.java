package my.philipshueremote;


import android.support.annotation.Nullable;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public abstract class CustomJsonRequest<T> extends Request<T> {
    private Response.Listener<T> listener;
    private String requestBody;

    public CustomJsonRequest(int method, String url, @Nullable Object requestBody, Response.Listener<T> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        this.listener = listener;
        if(requestBody instanceof JSONObject || requestBody instanceof JSONArray) {
            this.requestBody = requestBody.toString();
        }
        else {
            throw new UnsupportedClassVersionError("Not using a JsonObject or JsonArray....");
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        byte[] result = null;
        try {
            result = requestBody.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String getBodyContentType() {
        return String.format("application/json; charset=%s", "utf-8");
    }

    @Override
    protected abstract Response parseNetworkResponse(NetworkResponse response);

    @Override
    protected void deliverResponse(T response) {
        if(listener != null) {
            listener.onResponse(response);
        }
    }
}
