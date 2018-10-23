package my.philipshueremote.Init.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.philipshueremote.Init.Viewmodels.HueInitBridgeSelectionViewModel;
import my.philipshueremote.R;

public class HueInitBridgeSelectionFragment extends Fragment {
    private HueInitBridgeSelectionViewModel slaveViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        slaveViewModel = ViewModelProviders.of(this).get(HueInitBridgeSelectionViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.hue_init_bridgeselection_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

}
