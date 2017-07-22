package pollcompany.philipshueremote.AsyncTasks;

import android.os.AsyncTask;

import java.util.List;

import pollcompany.philipshueremote.Lamp;

/**
 * Created by Ian on 22-7-2017.
 */

public class AllLightsTask extends AsyncTask<String, Void, String> {
    private List<Lamp> lampList;
    private GetListener listener;

    public AllLightsTask(List<Lamp> lampList) {
        this.lampList = lampList;
    }

    @Override
    protected String doInBackground(String... strings) {


        return null;
    }
}
