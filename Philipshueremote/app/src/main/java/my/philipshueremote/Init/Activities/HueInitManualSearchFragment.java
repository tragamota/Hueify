package my.philipshueremote.Init.Activities;

import android.arch.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import my.philipshueremote.Init.Viewmodels.HueInitManualSearchViewModel;
import my.philipshueremote.R;

public class HueInitManualSearchFragment extends Fragment {
    private HueInitManualSearchViewModel viewModel;

    private TextView statusText;
    private TextInputLayout editTextLayout;
    private TextInputEditText editText;
    private ProgressBar searchingBar;
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
        searchingBar = view.findViewById(R.id.Init_waiting_ProgressBar);
        proceedButton = view.findViewById(R.id.Init_manual_processButton);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getValidIpIdenticator().observe(this, aBoolean -> {
            if(!aBoolean) {
                statusText.setText("Not a valid IP address");
                statusText.setVisibility(View.VISIBLE);
                statusText.setTextColor(getResources().getColor(R.color.colorDarkFailText));
                proceedButton.setClickable(false);
                proceedButton.setVisibility(View.INVISIBLE);
            }
            else {
                statusText.setVisibility(View.INVISIBLE);
                proceedButton.setClickable(true);
                proceedButton.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getBridgeState().observe(this, integer -> {
            if(integer == viewModel.NO_SEARCH) {
                statusText.setVisibility(View.INVISIBLE);
                searchingBar.setIndeterminate(false);
                proceedButton.setVisibility(View.VISIBLE);
                editTextLayout.setEnabled(true);
                proceedButton.setClickable(true);
                proceedButton.setImageResource(R.drawable.ic_search_glass);
            }
            else if(integer == viewModel.BRIDGE_SEARCH) {
                statusText.setVisibility(View.VISIBLE);
                statusText.setTextColor(getResources().getColor(R.color.colorDarkPrimaryText));
                statusText.setText("Searching for bridge");
                searchingBar.setIndeterminate(true);
                editTextLayout.setEnabled(false);
                proceedButton.setVisibility(View.INVISIBLE);
                proceedButton.setClickable(false);
                proceedButton.setImageResource(R.drawable.ic_search_glass);
            }
            else if(integer == viewModel.BRIDGE_FOUND) {
                statusText.setVisibility(View.VISIBLE);
                statusText.setTextColor(getResources().getColor(R.color.colorDarkPassText));
                statusText.setText("Found your bridge");
                searchingBar.setIndeterminate(false);
                editTextLayout.setEnabled(false);
                proceedButton.setVisibility(View.VISIBLE);
                proceedButton.setClickable(true);
                proceedButton.setImageResource(R.drawable.ic_forward_arrow);
            }
            else {
                statusText.setVisibility(View.VISIBLE);
                statusText.setTextColor(getResources().getColor(R.color.colorDarkFailText));
                statusText.setText("No bridge found");
                searchingBar.setIndeterminate(false);
                editTextLayout.setEnabled(true);
                proceedButton.setVisibility(View.VISIBLE);
                proceedButton.setClickable(true);
                proceedButton.setImageResource(R.drawable.ic_search_glass);
            }
        });

        proceedButton.setOnClickListener(view -> {
            int currentState = viewModel.getBridgeState().getValue();
            proceedToNextFragment();
            if(currentState == viewModel.NO_SEARCH || currentState == viewModel.NO_BRIDGE_FOUND) {
                viewModel.startSearching(editText.getText().toString());
            }
            else {
                proceedToNextFragment();
                viewModel.resetBridgeState();
            }
        });

        editTextLayout.setHint("IP address");
        editText.setInputType(InputType.TYPE_CLASS_PHONE);
        editText.addTextChangedListener(viewModel.getIPTextWatcher());

        startPostponedEnterTransition();
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().setTitle("Manual search");
    }

    private void proceedToNextFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("BRIDGE", viewModel.getFoundBridge());

        Fragment fragment = new HueInitTokenFragment();
        fragment.setArguments(bundle);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.Init_Activity_Holder, fragment, "INIT_TOKEN")
                .addToBackStack("INIT_TOKEN")
                .commit();
    }
}
