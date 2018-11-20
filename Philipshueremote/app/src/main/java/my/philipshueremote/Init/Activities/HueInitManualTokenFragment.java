package my.philipshueremote.Init.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import my.philipshueremote.Init.Viewmodels.HueInitManualTokenViewModel;
import my.philipshueremote.R;

public class HueInitManualTokenFragment extends Fragment {
    private HueInitManualTokenViewModel viewModel;

    private TextInputLayout textInputLayout;
    private EditText editText;
    private ProgressBar waitingProgressBar;
    private TextView statusTextView;
    private FloatingActionButton proceedButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HueInitManualTokenViewModel.class);

        Bundle bundle = getArguments();
        if(bundle != null) {
            if(bundle.containsKey("BRIDGE")) {
                viewModel.setBridgeInfo(bundle.getParcelable("BRIDGE"));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hue_init_manual_token_fragment, container, false);

        textInputLayout = view.findViewById(R.id.Init_Input_edit_layout);
        editText = view.findViewById(R.id.Init_Input_edit_text);
        waitingProgressBar = view.findViewById(R.id.Init_waiting_ProgressBar);
        statusTextView = view.findViewById(R.id.Init_manual_token_status_text);
        proceedButton = view.findViewById(R.id.Init_manual_token_proceed_button);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel.getCurrentState().observe(this, integer -> {
            if(integer == viewModel.NEVER_SEARCHED) {
                statusTextView.setVisibility(View.INVISIBLE);
                waitingProgressBar.setIndeterminate(false);
                textInputLayout.setEnabled(true);

                proceedButton.setVisibility(View.VISIBLE);
                proceedButton.setClickable(true);
            }
            else if(integer == viewModel.SEARCHING) {
                statusTextView.setVisibility(View.INVISIBLE);
                waitingProgressBar.setIndeterminate(true);
                textInputLayout.setEnabled(false);

                proceedButton.setVisibility(View.INVISIBLE);
                proceedButton.setClickable(false);
            }
            else if(integer == viewModel.KEY_NOT_EXIST) {
                statusTextView.setTextColor(getResources().getColor(R.color.colorDarkFailText));
                statusTextView.setText("Access key is not valid");
                statusTextView.setVisibility(View.VISIBLE);

                textInputLayout.setEnabled(true);
                waitingProgressBar.setIndeterminate(false);

                proceedButton.setVisibility(View.VISIBLE);
                proceedButton.setClickable(true);
            }
            else if(integer == viewModel.KEY_EXIST) {
                statusTextView.setTextColor(getResources().getColor(R.color.colorDarkPassText));
                statusTextView.setText("Access key is valid");
                statusTextView.setVisibility(View.VISIBLE);

                textInputLayout.setEnabled(false);
                waitingProgressBar.setIndeterminate(false);

                proceedButton.setImageResource(R.drawable.ic_forward_arrow);
                proceedButton.setVisibility(View.VISIBLE);
                proceedButton.setClickable(true);
            }
        });

        proceedButton.setOnClickListener(view -> {
            int currentState = viewModel.getCurrentState().getValue();
            if(currentState == viewModel.KEY_NOT_EXIST || currentState == viewModel.NEVER_SEARCHED) {
                if(!editText.getText().toString().isEmpty()) {
                    viewModel.checkIfAccessKeyExist(editText.getText().toString());
                }
                else {
                    statusTextView.setVisibility(View.VISIBLE);
                    statusTextView.setText("Empty input is not allowed");
                }
            }
            else if(currentState == viewModel.KEY_EXIST) {

            }
        });

        textInputLayout.setHint("Access Token");
    }

    private void proceedToNextFragment(Fragment fragment) {
        getFragmentManager().popBackStack("INIT_SEARCH", 0);
        getFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.Init_Activity_Holder, fragment, "INIT_DONE")
                .addToBackStack("INIT_DONE")
                .commit();
    }
}
