package pollcompany.philipshueremote.AsyncTasks;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ian on 3-8-2017.
 */

public class ColorTask extends AsyncTask<String, Void, String> {
    private float[] hsv = new float[3];
    private byte totalColorChangeTasks;

    public ColorTask(int color, byte totalColorChanges) {
        Color.colorToHSV(color, hsv);
        totalColorChangeTasks = totalColorChanges;
    }

    @Override
    protected String doInBackground(String... strings) {
        OutputStream outputStream = null;
        BufferedWriter writer = null;
        totalColorChangeTasks++;

        String urlAdress = "http://192.168.1.110" + "/api/" + "PDnGdnei5sjeQ91Ndo1n1FA5-WJV9qtYKf7dkM6g" + "/lights/" + strings[0] + "/state";
        try {
            URL url = new URL(urlAdress);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("USER-AGENT", "HueRemoteApp.4.0");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setReadTimeout(150);
            connection.setConnectTimeout(150);
            connection.setDoOutput(true);

            int brightness = (int) (hsv[2] * 255);
            if(brightness == 255) {
                brightness = 254;
            }

            String output = "{\n";
            output += ("\"hue\": " + (int) (hsv[0] * (65535/360)) + "," + "\n");
            output += ("\"sat\": " + (int) (hsv[1] * 255) + "," + "\n");
            output += ("\"bri\": " + brightness + "\n");
            output += "}";

            outputStream = connection.getOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(outputStream));
            writer.write(output);
            writer.flush();
            writer.close();
            outputStream.close();

            int responseCode = connection.getResponseCode();
            connection.disconnect();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        totalColorChangeTasks--;
        return null;
    }
}
