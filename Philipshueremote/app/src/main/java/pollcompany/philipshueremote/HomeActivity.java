package pollcompany.philipshueremote;


import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pollcompany.philipshueremote.AsyncTasks.AllLightsTask;
import pollcompany.philipshueremote.AsyncTasks.GetListener;

public class HomeActivity extends AppCompatActivity {
    private List<Lamp> lamps;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewLamps;
    private RecyclerView.Adapter lampAdapter;
    private RecyclerView.LayoutManager lampLayoutManager;

    private AllLightsTask getLampsTask;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        lamps = new ArrayList<>();
        buildGetTask();

        if(savedInstanceState != null) {
            assert (Collection<? extends Lamp>) savedInstanceState.getSerializable("LAMP_ARRAY") != null;
            lamps.addAll((Collection<? extends Lamp>) savedInstanceState.getSerializable("LAMP_ARRAY"));
        }

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayoutLamps);
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

        lampAdapter = new LampCardviewAdapter(lamps);
        lampLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewLamps = (RecyclerView) findViewById(R.id.recyclerViewLamps);

        recyclerViewLamps.setHasFixedSize(false);
        recyclerViewLamps.setLayoutManager(lampLayoutManager);
        recyclerViewLamps.setAdapter(lampAdapter);

        getLampsTask.execute("1", "2");
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
                Toast.makeText(getApplicationContext(), "The philips hue bridge is not reachable", Toast.LENGTH_SHORT).show();
                if(swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                buildGetTask();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("LAMP_ARRAY", (Serializable) lamps);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(getLampsTask.getStatus() == AsyncTask.Status.RUNNING) {
            getLampsTask.cancel(true);
        }
    }
}