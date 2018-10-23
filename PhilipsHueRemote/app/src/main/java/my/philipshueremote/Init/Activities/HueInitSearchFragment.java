package my.philipshueremote.Init.Activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.transition.Transition;
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

    private ProgressBar searchIndicatorBar;
    private TextView searchStatusText;
    private Button searchStartButton, searchManualButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(HueInitSearchViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.hue_init_search_fragment, container, false);
        searchIndicatorBar = view.findViewById(R.id.Init_Search_FindingProgressCircle);
        searchStatusText = view.findViewById(R.id.Init_Search_LoadingStatusText);
        searchStartButton = view.findViewById(R.id.Init_Search_FindButton);
        searchManualButton = view.findViewById(R.id.Init_Search_ManualButton);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getSearchState().observe(this, searchingStates -> {
            if (searchingStates == SearchingStates.FOUND) {
               //make loading bar go as a checked sign and show how many bridges are found...

               searchStartButton.setText("Proceed");
            }
            else if(searchingStates == SearchingStates.SEARCHING) {
                //show animation

                changeStatusVisibility(View.VISIBLE, false);
            }
            else {

                //hide animation...
                changeStatusVisibility(View.GONE, true);
            }
        });

        searchStartButton.setOnClickListener(view -> {
            if(viewModel.getSearchState().getValue() == SearchingStates.FOUND) {
                Fragment transitionFragment;
                String fragmentTag;
                Bundle transitionBundle = new Bundle();
                if(viewModel.getBridges().size() > 1) {
                    transitionFragment = new HueInitBridgeSelectionFragment();
                    fragmentTag = "INIT_BRIDGE_LIST";
                    transitionBundle.putParcelableArrayList("BRIDGES", (ArrayList<? extends Parcelable>) viewModel.getBridges());
                }
                else {
                    transitionFragment = new HueInitBridgeSelectionFragment();
                    fragmentTag = "INIT_TOKEN";
                    transitionBundle.putParcelable("BRIDGE", viewModel.getBridges().get(0));
                }
                transitionFragment.setArguments(transitionBundle);
                proceedToNextFragment(transitionFragment, null, fragmentTag);

                viewModel.setInitialSearch(false);
                searchStartButton.setText("Search");
            }
            else {
                viewModel.startSearching();
            }
        });

        searchManualButton.setOnClickListener(view -> {
            viewModel.stopSearching();
            viewModel.setInitialSearch(false);
            proceedToNextFragment(new HueInitBridgeSelectionFragment(), null, "INIT_BRIDGE_MANUAL");
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

    private void changeStatusVisibility(int isVisible, boolean isClickable) {
        searchIndicatorBar.setVisibility(isVisible);
        searchStatusText.setVisibility(isVisible);
        searchStartButton.setClickable(isClickable);
    }

    private void proceedToNextFragment(Fragment fragment, Transition transition, String tag) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.Init_Activity_Holder, fragment, tag);
        transaction.addToBackStack(tag);
        transaction.commit();
    }
}
