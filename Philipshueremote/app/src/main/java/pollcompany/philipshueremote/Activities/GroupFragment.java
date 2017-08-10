package pollcompany.philipshueremote.Activities;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
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

import pollcompany.philipshueremote.Adapters.GroupCardViewAdapter;
import pollcompany.philipshueremote.AsyncTasks.GetGroupTask;
import pollcompany.philipshueremote.AsyncTasks.GetLightsTask;
import pollcompany.philipshueremote.AsyncTasks.GetListener;
import pollcompany.philipshueremote.Group;
import pollcompany.philipshueremote.R;

public class GroupFragment extends Fragment {
    private List<Group> groups;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewGroups;
    private RecyclerView.Adapter groupAdapter;
    private RecyclerView.LayoutManager groupLayoutManager;

    private GetGroupTask getGroupTask;

    @RequiresApi(api = Build.VERSION_CODES.M)
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_group_fragment, container, false);
        groups = new ArrayList<>();
        buildGetTask();

        List<String> fileList = Arrays.asList(getContext().fileList());

        if(savedInstanceState == null && fileList.contains("GroupSaveFile.lmp")) {
            try {
                FileInputStream fileInputStream = getContext().openFileInput("GroupSaveFile.lmp");
                ObjectInputStream inputReader = new ObjectInputStream(fileInputStream);
                groups.addAll((List<Group>) inputReader.readObject());
                inputReader.close();
                fileInputStream.close();
            }
            catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        if(savedInstanceState != null) {
            if (savedInstanceState.getSerializable("GROUP_ARRAY") != null) {
                groups.addAll((Collection<? extends Group>) savedInstanceState.getSerializable("GROUP_ARRAY"));
            }
        }

        swipeRefreshLayout = view.findViewById(R.id.refreshLayoutGroup);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(getGroupTask.getStatus().equals(AsyncTask.Status.PENDING)) {
                    getGroupTask.execute();
                }
                else if(getGroupTask.getStatus().equals(AsyncTask.Status.FINISHED)) {
                    buildGetTask();
                    getGroupTask.execute();
                }
            }
        });

        Resources r = getResources();
        swipeRefreshLayout.setColorSchemeColors(r.getColor(android.R.color.holo_blue_bright,null),
                r.getColor(android.R.color.holo_green_light, null),
                r.getColor(android.R.color.holo_orange_light, null),
                r.getColor(android.R.color.holo_red_light, null));

        groupAdapter = new GroupCardViewAdapter(groups);
        groupLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewGroups = view.findViewById(R.id.recyclerViewGroup);

        recyclerViewGroups.setHasFixedSize(false);
        recyclerViewGroups.setLayoutManager(groupLayoutManager);
        recyclerViewGroups.setAdapter(groupAdapter);

        getGroupTask.execute();
        return view;
    }

    private void buildGetTask() {
        getGroupTask = new GetGroupTask(new GetListener() {
            @Override
            public void updateContent(List items) {
                groups.clear();
                groups.addAll(items);
                groupAdapter.notifyDataSetChanged();
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
        outState.putSerializable("GROUP_ARRAY", (Serializable) groups);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(!groups.isEmpty()) {
            try {
                FileOutputStream fileOutputStream = getContext().openFileOutput("GroupSaveFile.lmp", Context.MODE_PRIVATE);
                ObjectOutputStream outputWriter = new ObjectOutputStream(fileOutputStream);
                outputWriter.writeObject(groups);
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
        if(getGroupTask.getStatus() == AsyncTask.Status.RUNNING) {
            getGroupTask.cancel(true);
        }
    }
}