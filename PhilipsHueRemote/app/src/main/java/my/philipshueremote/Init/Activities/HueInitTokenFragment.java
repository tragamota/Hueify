package my.philipshueremote.Init.Activities;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import my.philipshueremote.Init.Viewmodels.HueInitTokenViewModel;
import my.philipshueremote.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HueInitTokenFragment extends Fragment {
    private HueInitTokenViewModel viewModel;

    private TextView statusTextView;
    private ProgressBar statusWaitingBar;
    private Button manualButton;
    private FloatingActionButton proceedButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HueInitTokenViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        postponeEnterTransition();
        View view = inflater.inflate(R.layout.hue_init_token_fragment, container, false);
        statusTextView = view.findViewById(R.id.Init_Search_Status);
        statusWaitingBar = view.findViewById(R.id.Init_waiting_ProgressBar);
        manualButton = view.findViewById(R.id.Init_access_manual_button);
        proceedButton = view.findViewById(R.id.Init_access_action_button);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        manualButton.setOnClickListener(view -> {
            proceedToNextFragment(new HueInitManualTokenFragment(), "INIT_TOKEN_MANUAL");
        });

        startPostponedEnterTransition();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Grant access");
    }

    private void proceedToNextFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.Init_Activity_Holder, fragment)
                .addToBackStack(tag)
                .commit();
    }
}
