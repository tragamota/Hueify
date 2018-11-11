package my.philipshueremote.Init.Activities;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import my.philipshueremote.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HueInitDoneFragment extends Fragment {
    public HueInitDoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.hue_init_done_fragment, container, false);
    }

}
