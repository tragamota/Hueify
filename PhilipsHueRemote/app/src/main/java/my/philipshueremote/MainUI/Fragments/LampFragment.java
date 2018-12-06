package my.philipshueremote.MainUI.Fragments;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import my.philipshueremote.DataCommunication.Services.HueSyncService;
import my.philipshueremote.MainUI.Adapters.LampRecyclerAdapter;
import my.philipshueremote.MainUI.ViewModels.LampFragmentViewModel;
import my.philipshueremote.R;

public class LampFragment extends Fragment {
    private LampFragmentViewModel viewModel;

    private TextView statusText;
    private RecyclerView lampRecyclerView;
    private LampRecyclerAdapter lampAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(LampFragmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_lamps_fragment, container, false);

        statusText = view.findViewById(R.id.Main_lamps_statusText);
        lampRecyclerView = view.findViewById(R.id.Main_lamps_recyclerview);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        lampRecyclerView.setHasFixedSize(true);
        lampRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lampRecyclerView.setAdapter(lampAdapter = new LampRecyclerAdapter(getContext().getApplicationContext()));

        HueSyncService.getInstance(getContext()).getSelectedBridge().observe(this, bridgeInfo -> {
            lampAdapter.updateBridge(bridgeInfo);
        });

        viewModel.getLiveLamps().observe(this, lamps -> {
            if(lamps != null) {
                lampAdapter.updateLampList(lamps);
                statusText.setVisibility(View.GONE);
                lampAdapter.notifyDataSetChanged();
            }
            else {
                statusText.setVisibility(View.VISIBLE);
            }
        });
    }
}
