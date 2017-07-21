package pollcompany.philipshueremote;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Ian on 21-7-2017.
 */

//https://code.tutsplus.com/tutorials/getting-started-with-recyclerview-and-cardview-on-android--cms-23465

public class LampCardviewAdapter extends RecyclerView.Adapter<LampCardviewAdapter.ViewHolder> {
    private List<Lamp> lamps;

    public LampCardviewAdapter(List<Lamp> lamps) {
        this.lamps = lamps;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardViewLamp;
        private TextView lampName;


        public ViewHolder(View itemView) {
            super(itemView);
            cardViewLamp = itemView.findViewById(R.id.LampCardView);
            lampName = itemView.findViewById(R.id.lampCardViewLampName);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_cardlamps, parent, false);
        ViewHolder view = new ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return lamps.size();
    }

}
