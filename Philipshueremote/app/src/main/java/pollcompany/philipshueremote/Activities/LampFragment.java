package pollcompany.philipshueremote.Activities;


import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import pollcompany.philipshueremote.Adapters.LampCardViewAdapter;
import pollcompany.philipshueremote.AsyncTasks.GetLightsTask;
import pollcompany.philipshueremote.AsyncTasks.GetListener;
import pollcompany.philipshueremote.Lamp;
import pollcompany.philipshueremote.R;

public class LampFragment extends Fragment {
    private List<Lamp> lamps;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewLamps;
    private RecyclerView.Adapter lampAdapter;
    private RecyclerView.LayoutManager lampLayoutManager;

    private GetLightsTask getLampsTask;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lamp, container, false);
        lamps = new ArrayList<>();
        buildGetTask();

        List<String> fileList = Arrays.asList(getContext().fileList());

        if(savedInstanceState == null && fileList.contains("LampSaveFile.lmp")) {
            try {
                FileInputStream fileInputStream = getContext().openFileInput("LampSaveFile.lmp");
                ObjectInputStream inputReader = new ObjectInputStream(fileInputStream);
                lamps.addAll((List<Lamp>) inputReader.readObject());
                inputReader.close();
                fileInputStream.close();
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(savedInstanceState != null) {
            assert (Collection<? extends Lamp>) savedInstanceState.getSerializable("LAMP_ARRAY") != null;
            lamps.addAll((Collection<? extends Lamp>) savedInstanceState.getSerializable("LAMP_ARRAY"));
        }

        swipeRefreshLayout = view.findViewById(R.id.refreshLayoutLamps);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getLampsTask.getStatus().equals(AsyncTask.Status.PENDING)) {
                    getLampsTask.execute();
                }
                else if(getLampsTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                    buildGetTask();
                    getLampsTask.execute();
                }
            }
        });

        Resources r = getResources();
        swipeRefreshLayout.setColorSchemeColors(r.getColor(android.R.color.holo_blue_bright,null),
                r.getColor(android.R.color.holo_green_light, null),
                r.getColor(android.R.color.holo_orange_light, null),
                r.getColor(android.R.color.holo_red_light, null));

        lampAdapter = new LampCardViewAdapter(lamps);
        lampLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewLamps = view.findViewById(R.id.recyclerViewLamps);

        recyclerViewLamps.setHasFixedSize(false);
        recyclerViewLamps.setLayoutManager(lampLayoutManager);
        recyclerViewLamps.setAdapter(lampAdapter);

        getLampsTask.execute();
        return view;
    }

    private void buildGetTask() {
        getLampsTask = new GetLightsTask(new GetListener() {
            @Override
            public void updateContent(List items) {
                lamps.clear();
                lamps.addAll(items);
                lampAdapter.notifyDataSetChanged();
                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                buildGetTask();
            }
            @Override
            public void notReachable() {
                Toast.makeText(getActivity().getApplicationContext(), "The philips hue bridge is not reachable", Toast.LENGTH_SHORT).show();
                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                buildGetTask();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("LAMP_ARRAY", (Serializable) lamps);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!lamps.isEmpty()) {
            try {
                FileOutputStream fileOutputStream = getContext().openFileOutput("LampSaveFile.lmp", Context.MODE_PRIVATE);
                ObjectOutputStream outputWriter = new ObjectOutputStream(fileOutputStream);
                outputWriter.writeObject(lamps);
                outputWriter.close();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(getLampsTask.getStatus() == AsyncTask.Status.RUNNING) {
            getLampsTask.cancel(true);
        }
    }
}