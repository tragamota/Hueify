package my.philipshueremote.Init.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;

import my.philipshueremote.Init.Models.SearchingStates;
import my.philipshueremote.Init.Viewmodels.HueInitSearchViewModel;
import my.philipshueremote.R;

public class HueInitSearchFragment extends Fragment {
    private HueInitSearchViewModel viewModel;

    private ProgressBar statusIndicatorBar;
    private TextView statusTextView;
    private Button searchManualButton;
    private FloatingActionButton searchNavButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HueInitSearchViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hue_init_search_fragment, container, false);
        searchManualButton = view.findViewById(R.id.Init_Search_ManualButton);
        statusIndicatorBar = view.findViewById(R.id.Init_waiting_ProgressBar);
        statusTextView = view.findViewById(R.id.Init_waiting_statusText);
        searchNavButton = view.findViewById(R.id.Init_Search_proceedButton);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity().isTaskRoot()) {
            getActivity().setTitle("Welkom");
        }
        else {
            getActivity().setTitle("Bridge setup");
        }

        viewModel.getSearchState().observe(this, searchingStates -> {
            if (searchingStates == SearchingStates.FOUND) {
                if(viewModel.getBridges().size() > 1) {
                    statusTextView.setText("Found multiple bridges");
                }
                else {
                    statusTextView.setText("Found a single bridge");
                }
                changeProgressBarStatus(false);
                changeClickableButon(searchNavButton, true);
                searchNavButton.setVisibility(View.VISIBLE);
            }
            else if(searchingStates == SearchingStates.SEARCHING) {
                statusTextView.setText("Searching for bridges..");
                changeProgressBarStatus(true);
                changeClickableButon(searchNavButton, false);
                searchNavButton.setVisibility(View.INVISIBLE);
            }
            else {
                statusTextView.setText("No brigde found");
                changeProgressBarStatus(false);
                changeClickableButon(searchNavButton, true);
                searchNavButton.setVisibility(View.VISIBLE);
            }
        });

        searchNavButton.setOnClickListener(view -> {
            if(viewModel.getSearchState().getValue() == SearchingStates.FOUND) {
                Fragment transitionFragment;
                String fragmentTag;
                Bundle transitionBundle = new Bundle();
                if(viewModel.getBridges().size() > 1) {
                    transitionFragment = new HueInitBridgeSelectionFragment();
                    fragmentTag = "INIT_BRIDGE_LIST";
                    transitionBundle.putParcelableArrayList("BRIDGES",
                            (ArrayList<? extends Parcelable>) viewModel.getBridges());
                }
                else {
                    transitionFragment = new HueInitTokenFragment();
                    fragmentTag = "INIT_TOKEN";
                    transitionBundle.putParcelable("BRIDGE",
                            viewModel.getBridges().get(0));
                }
                transitionFragment.setArguments(transitionBundle);
                proceedToNextFragment(transitionFragment, fragmentTag);
            }
            else {
                viewModel.startSearching();
            }
        });

        searchManualButton.setOnClickListener(view -> {
            viewModel.stopSearching();
            viewModel.setInitialSearch(false);
            proceedToNextFragment(new HueInitManualSearchFragment(), "INIT_BRIDGE_MANUAL");
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!viewModel.getInitialSearch()) {
            viewModel.startSearching();
            viewModel.setInitialSearch(true);
        }
    }

    private void changeProgressBarStatus(boolean active) {
        if(active) {
            statusIndicatorBar.setIndeterminate(active);
        }
        else {
            statusIndicatorBar.setIndeterminate(active);
            statusIndicatorBar.setProgress(0);
        }
    }

    private void changeClickableButon(View view, boolean value) {
        view.setClickable(value);
    }

    private void proceedToNextFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction
                .setReorderingAllowed(true)
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right,
                        android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .replace(R.id.Init_Activity_Holder, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }
}
