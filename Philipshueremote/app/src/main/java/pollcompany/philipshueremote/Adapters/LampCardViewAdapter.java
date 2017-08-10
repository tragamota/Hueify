package pollcompany.philipshueremote.Adapters;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import pollcompany.philipshueremote.Activities.LampSettings;
import pollcompany.philipshueremote.AsyncTasks.GetListener;
import pollcompany.philipshueremote.AsyncTasks.OnOffTask;
import pollcompany.philipshueremote.Lamp;
import pollcompany.philipshueremote.R;

/**
 * Created by Ian on 21-7-2017.
 */

public class LampCardViewAdapter extends RecyclerView.Adapter<LampCardViewAdapter.ViewHolder> {
    private List<Lamp> lamps;

    public LampCardViewAdapter(List<Lamp> items) {
        lamps = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardViewLamp;
        private TextView lampName;
        private SwitchCompat onOffSwitch;
        private ProgressBar progressBar;
        private ImageView cardImage;

        public ViewHolder(View itemView) {
            super(itemView);
            cardViewLamp = itemView.findViewById(R.id.LampCardView);
            lampName = itemView.findViewById(R.id.lampCardViewLampName);
            onOffSwitch = itemView.findViewById(R.id.lampcardOnOffSwitch);
            progressBar = itemView.findViewById(R.id.lampOn);
            cardImage = itemView.findViewById(R.id.cardviewLampIcon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lamp_cardlayout, parent, false);
        ViewHolder view = new ViewHolder(v);
        return view;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
            holder.lampName.setText(lamps.get(position).getName());
            holder.progressBar.setVisibility(View.INVISIBLE);
            holder.cardViewLamp.setCardBackgroundColor(lamps.get(position).getHsvColor());
            if (lamps.get(position).isOnOff()) {
                holder.onOffSwitch.setChecked(true);
            } else {
                holder.onOffSwitch.setChecked(false);
            }
            holder.onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    new OnOffTask(b, new GetListener() {
                        @Override
                        public void updateContent(List items) {
                            holder.progressBar.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void notReachable() {

                        }
                    }).execute(lamps.get(position).getId());
                    holder.progressBar.setVisibility(View.VISIBLE);
                }
            });
            holder.cardViewLamp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), LampSettings.class);
                    intent.putExtra("LAMP_OBJECT", lamps.get(position));
                    view.getContext().startActivity(intent);
                }
            });
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