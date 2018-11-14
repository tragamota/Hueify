package my.philipshueremote.Init.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import my.philipshueremote.Init.Viewmodels.HueInitManualSearchViewModel;
import my.philipshueremote.R;

public class HueInitManualSearchFragment extends Fragment {
    private HueInitManualSearchViewModel viewModel;

    private TextView statusText;
    private TextInputLayout editTextLayout;
    private TextInputEditText editText;
    private FloatingActionButton proceedButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HueInitManualSearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        postponeEnterTransition();
        View view = inflater.inflate(R.layout.hue_init_manual_search_fragment, container, false);

        statusText = view.findViewById(R.id.Init_manual_bridgeInputErrorText);
        editTextLayout = view.findViewById(R.id.Init_Input_edit_layout);
        editText = view.findViewById(R.id.Init_Input_edit_text);
        proceedButton = view.findViewById(R.id.Init_manual_processButton);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        editTextLayout.setHint("IP address");

        viewModel.getBridgeState().observe(this, integer -> {
            switch (integer) {
                case viewModel.NO_SEARCH:
                    break;
                case viewModel.BRIDGE_SEARCH:

            }
        });
        statusText.setVisibility(View.INVISIBLE);

        //editText.addTextChangedListener();
        //proceedButton.s

        startPostponedEnterTransition();
    }
}
