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
import java.util.List;

import pollcompany.philipshueremote.Lamp;

/**
 * Created by Ian on 22-7-2017.
 */

public class AllLightsTask extends AsyncTask<String, Void, String> {
    private List<Lamp> lampList;
    private GetListener listener;

    public AllLightsTask(List<Lamp> lampList, GetListener listener) {
        this.lampList = lampList;
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream inputStream = null;
        BufferedReader reader = null;
        String response = "";

        String urlAdress = "http://" + strings[0] + "/api/" + strings[1] + "/lights";
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
        }
        catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        JSONObject responseObject;

        if(response != null) {
            try {
                lampList.clear();
                //responseObject = new JSONObject(response);
                JSONObject object = new JSONObject("{\n" +
                        "\n" +
                        "    \"1\": {\n" +
                        "        \"state\": {\n" +
                        "            \"on\": true,\n" +
                        "            \"bri\": 144,\n" +
                        "            \"hue\": 13088,\n" +
                        "            \"sat\": 212,\n" +
                        "            \"xy\": [0.5128,0.4147],\n" +
                        "            \"ct\": 467,\n" +
                        "            \"alert\": \"none\",\n" +
                        "            \"effect\": \"none\",\n" +
                        "            \"colormode\": \"xy\",\n" +
                        "            \"reachable\": true\n" +
                        "        },\n" +
                        "        \"type\": \"Extended color light\",\n" +
                        "        \"name\": \"Hue Lamp 1\",\n" +
                        "        \"modelid\": \"LCT001\",\n" +
                        "        \"swversion\": \"66009461\",\n" +
                        "        \"pointsymbol\": {\n" +
                        "            \"1\": \"none\",\n" +
                        "            \"2\": \"none\",\n" +
                        "            \"3\": \"none\",\n" +
                        "            \"4\": \"none\",\n" +
                        "            \"5\": \"none\",\n" +
                        "            \"6\": \"none\",\n" +
                        "            \"7\": \"none\",\n" +
                        "            \"8\": \"none\"\n" +
                        "        }\n" +
                        "    },\n" +
                        "    \"2\": {\n" +
                        "        \"state\": {\n" +
                        "            \"on\": false,\n" +
                        "            \"bri\": 0,\n" +
                        "            \"hue\": 0,\n" +
                        "            \"sat\": 0,\n" +
                        "            \"xy\": [0,0],\n" +
                        "            \"ct\": 0,\n" +
                        "            \"alert\": \"none\",\n" +
                        "            \"effect\": \"none\",\n" +
                        "            \"colormode\": \"hs\",\n" +
                        "            \"reachable\": true\n" +
                        "        },\n" +
                        "        \"type\": \"Extended color light\",\n" +
                        "        \"name\": \"Hue Lamp 2\",\n" +
                        "        \"modelid\": \"LCT001\",\n" +
                        "        \"swversion\": \"66009461\",\n" +
                        "        \"pointsymbol\": {\n" +
                        "            \"1\": \"none\",\n" +
                        "            \"2\": \"none\",\n" +
                        "            \"3\": \"none\",\n" +
                        "            \"4\": \"none\",\n" +
                        "            \"5\": \"none\",\n" +
                        "            \"6\": \"none\",\n" +
                        "            \"7\": \"none\",\n" +
                        "            \"8\": \"none\"\n" +
                        "        }\n" +
                        "    }\n" +
                        "}");
                for(int i = 0; i < object.length(); i++) {
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

                    JSONObject lampState = lampObject.getJSONObject("state");
                    onOff = lampState.getBoolean("on");
                    hue = lampState.getInt("hue");
                    brightness = lampState.getInt("bri");
                    sat = lampState.getInt("sat");

                    lampList.add(new Lamp(onOff, id, name, type, brightness, hue, sat));
                }

                System.out.println(lampList);
                listener.updateContent();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            listener.notReachable();
        }
    }
}
