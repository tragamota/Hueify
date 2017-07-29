package pollcompany.philipshueremote.Adapters;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import pollcompany.philipshueremote.Lamp;
import pollcompany.philipshueremote.R;

/**
 * Created by Ian on 21-7-2017.
 */

public class LampCardViewAdapter extends RecyclerView.Adapter<LampCardViewAdapter.ViewHolder> {
    private List<Lamp> lamps;

    public LampCardViewAdapter(List<Lamp> lamps) {
        this.lamps = lamps;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardViewLamp;
        private TextView lampName;
        private Switch onOffSwitch;
        private ProgressBar progressBar;


        public ViewHolder(View itemView) {
            super(itemView);
            cardViewLamp = itemView.findViewById(R.id.LampCardView);
            lampName = itemView.findViewById(R.id.lampCardViewLampName);
            onOffSwitch = itemView.findViewById(R.id.lampcardOnOffSwitch);
            progressBar = itemView.findViewById(R.id.lampOn);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lamp_cardlayout, parent, false);
        ViewHolder view = new ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.lampName.setText(lamps.get(position).getName());
        holder.progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return lamps.size();
    }
}