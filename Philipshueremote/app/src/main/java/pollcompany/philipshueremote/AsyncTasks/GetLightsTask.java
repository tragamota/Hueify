package pollcompany.philipshueremote.AsyncTasks;


import android.os.AsyncTask;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pollcompany.philipshueremote.Group;
import pollcompany.philipshueremote.Lamp;

/**
 * Created by Ian on 22-7-2017.
 */

public class GetLightsTask extends AsyncTask<String, Void, String> {
    private GetListener listener;

    public GetLightsTask(GetListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        String response = "";

        String urlAdress = "http://192.168.0.39:8000" + "/api/" + "newdeveloper" + "/lights";
        try {
            URL url = new URL(urlAdress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(2500);
            connection.setReadTimeout(2500);

            inputStream = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

            String readline;
            while ((readline = reader.readLine()) != null) {
                response += readline;
            }

            if(inputStream != null) {
                inputStream.close();
                reader.close();
            }

            connection.disconnect();
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        JSONObject object;
        List<Lamp> lampsToAdd = new ArrayList<>();
        if (response != null) {
            try {
                object = new JSONObject(response);

                for (int i = 0; i < object.length(); i++) {
                    boolean onOff;
                    String name;
                    String id;
                    String type;
                    int hue;
                    int brightness;
                    int sat;

                    JSONObject lampObject = object.getJSONObject(("" + (i + 1)));
                    id = String.valueOf((i + 1));
                    name = lampObject.getString("name");
                    type = lampObject.getString("type");

                    if(type.equals("Extended color light")) {
                        JSONObject lampState = lampObject.getJSONObject("state");
                        onOff = lampState.getBoolean("on");
                        hue = lampState.getInt("hue");
                        brightness = lampState.getInt("bri");
                        sat = lampState.getInt("sat");

                        lampsToAdd.add(new Lamp(onOff, id, name, type, brightness, hue, sat));
                    }
                }
                listener.updateContent(lampsToAdd);
            } catch (JSONException e) {
                e.printStackTrace();
                listener.notReachable();
            }
        } else {
            listener.notReachable();
        }
    }
}
