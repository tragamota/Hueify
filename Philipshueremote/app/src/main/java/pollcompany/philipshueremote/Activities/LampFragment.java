package pollcompany.philipshueremote.Activities;


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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pollcompany.philipshueremote.Adapters.LampCardViewAdapter;
import pollcompany.philipshueremote.AsyncTasks.AllLightsTask;
import pollcompany.philipshueremote.AsyncTasks.GetListener;
import pollcompany.philipshueremote.Lamp;
import pollcompany.philipshueremote.R;

public class LampFragment extends Fragment {
    private List<Lamp> lamps;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewLamps;
    private RecyclerView.Adapter lampAdapter;
    private RecyclerView.LayoutManager lampLayoutManager;

    private AllLightsTask getLampsTask;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lamp, container, false);
        lamps = new ArrayList<>();
        buildGetTask();

        if(savedInstanceState != null) {
            assert (Collection<? extends Lamp>) savedInstanceState.getSerializable("LAMP_ARRAY") != null;
            lamps.addAll((Collection<? extends Lamp>) savedInstanceState.getSerializable("LAMP_ARRAY"));
        }

        swipeRefreshLayout = view.findViewById(R.id.refreshLayoutLamps);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLampsTask.execute("1", " 1");
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

        getLampsTask.execute("1", "2");
        return view;
    }

    private void buildGetTask() {
        getLampsTask = new AllLightsTask(lamps, new GetListener() {
            @Override
            public void updateContent() {
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
    public void onDestroy() {
        super.onDestroy();
        if(getLampsTask.getStatus() == AsyncTask.Status.RUNNING) {
            getLampsTask.cancel(true);
        }
    }
}