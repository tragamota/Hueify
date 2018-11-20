package my.philipshueremote.DataCommunication;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

public class VolleyJsonSocket {
    private static VolleyJsonSocket thisInstance;
    private RequestQueue requestQueue;

    private VolleyJsonSocket(Context appContext) {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(appContext);
        }
    }

    public static synchronized VolleyJsonSocket getInstance(Context appContext) {
        if(thisInstance == null) {
            thisInstance = new VolleyJsonSocket(appContext);
        }
        return thisInstance;
    }

    public void start() {
        requestQueue.start();
    }

    public void stop() {
        requestQueue.stop();
    }

    public <T> boolean addRequestToQueue(JsonRequest<T> request) {
        boolean success = false;
        if(requestQueue != null) {
            requestQueue.add(request);
            success = true;
        }
        return success;
    }

    public <T> void cancel(JsonRequest<T> requestToCancel) {
        requestToCancel.cancel();
    }

    public void cancelAllRequest(Context appContext) {
        requestQueue.cancelAll(appContext);
    }
}
