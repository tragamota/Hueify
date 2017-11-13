package pollcompany.philipshueremote.Adapters;

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

import pollcompany.philipshueremote.AsyncTasks.GetListener;
import pollcompany.philipshueremote.AsyncTasks.OnOffTaskGroup;
import pollcompany.philipshueremote.Group;
import pollcompany.philipshueremote.R;

/**
 * Created by Ian on 7-8-2017.
 */

public class GroupCardViewAdapter extends RecyclerView.Adapter<GroupCardViewAdapter.ViewHolderGroup> {
    private List<Group> groups;

    public GroupCardViewAdapter(List<Group> items) {
        groups = items;
    }

    public static class ViewHolderGroup extends RecyclerView.ViewHolder {
        private CardView cardViewLamp;
        private TextView lampName;
        private SwitchCompat onOffSwitch;
        private ProgressBar progressBar;
        private ImageView cardImage;

        public ViewHolderGroup(View itemView) {
            super(itemView);
            cardViewLamp = itemView.findViewById(R.id.LampCardView);
            lampName = itemView.findViewById(R.id.lampCardViewLampName);
            onOffSwitch = itemView.findViewById(R.id.lampcardOnOffSwitch);
            progressBar = itemView.findViewById(R.id.lampOn);
            cardImage = itemView.findViewById(R.id.cardviewLampIcon);
        }
    }

    @Override
    public ViewHolderGroup onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lamp_cardlayout, parent, false);
        ViewHolderGroup view = new ViewHolderGroup(v);
        return view;
    }

    @Override
    public void onBindViewHolder(final ViewHolderGroup holder, final int position) {
        holder.lampName.setText(groups.get(position).getGroupName());
        holder.progressBar.setVisibility(View.INVISIBLE);
        if (groups.get(position).isOnOff()) {
            holder.onOffSwitch.setChecked(true);
        } else {
            holder.onOffSwitch.setChecked(false);
        }
        holder.onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                holder.progressBar.setVisibility(View.VISIBLE);
                new OnOffTaskGroup(b, new GetListener() {
                    @Override
                    public void updateContent(List items) {
                        holder.progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void notReachable() {

                    }
                }).execute(groups.get(position).getGroupID());
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
