package my.philipshueremote.Init.Activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.philipshueremote.MainUI.MainAppActivity;
import my.philipshueremote.SplashActivity;
import my.philipshueremote.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HueInitDoneFragment extends Fragment {
    private FloatingActionButton doneButton;

    public HueInitDoneFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hue_init_done_fragment, container, false);

        doneButton = view.findViewById(R.id.Init_done_button);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        doneButton.setOnClickListener(view -> {
            if(getActivity().isTaskRoot()) {
                startMainActivity();
            }
            getActivity().finish();
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().setTitle("Done");
    }

    private void startMainActivity() {
        Intent intent = new Intent(getActivity(), MainAppActivity.class);
        startActivity(intent);
    }
}
