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

/**
 * Created by Ian on 8-8-2017.
 */

public class GetGroupTask extends AsyncTask<String, Void, String> {
    private GetListener listener;

    public GetGroupTask(GetListener listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream inputStream;
        BufferedReader reader;
        String response = "";

        String urlAdress = "http://192.168.1.110" + "/api/" + "PDnGdnei5sjeQ91Ndo1n1FA5-WJV9qtYKf7dkM6g" + "/groups";
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

            if (inputStream != null) {
                inputStream.close();
                reader.close();
            }

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        JSONObject object;
        List<Group> groupsToAdd = new ArrayList<>();
        if (response != null) {
            try {
                object = new JSONObject(response);

                for (int i = 0; i < object.length(); i++) {
                    String name;
                    String id;

                    JSONObject lampObject = object.getJSONObject(("" + (i + 1)));
                    id = String.valueOf((i + 1));
                    name = lampObject.getString("name");

                    groupsToAdd.add(new Group(id, name));
                }
                listener.updateContent(groupsToAdd);
            } catch (JSONException e) {
                e.printStackTrace();
                listener.notReachable();
            }
        } else {
            listener.notReachable();
        }
    }
}