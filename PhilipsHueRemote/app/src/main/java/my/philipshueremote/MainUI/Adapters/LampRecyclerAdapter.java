package my.philipshueremote.MainUI.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import my.philipshueremote.Database.Entities.Lamp;
import my.philipshueremote.R;

public class LampRecyclerAdapter extends RecyclerView.Adapter<LampRecyclerAdapter.LampViewHolder>{
    private List<Lamp> lamps;

    public LampRecyclerAdapter(){
        lamps = new ArrayList<>();
    }

    public void updateLampList(List<Lamp> lamps){
        this.lamps = lamps;
    }

    public static class LampViewHolder extends ViewHolder{
        public CardView cardView;
        public TextView lampTitle;
        public ImageView lampImage;
        public SeekBar lampBrightness;
        public Switch lampSwitch;

        public LampViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.main_lamp_cardview);
            this.lampTitle = itemView.findViewById(R.id.main_lamp_cardview_title);
            this.lampImage = itemView.findViewById(R.id.main_lamp_cardview_image);
            this.lampBrightness = itemView.findViewById(R.id.main_lamp_cardview_seekbar);
            this.lampSwitch = itemView.findViewById(R.id.main_lamp_cardview_switch);
        }
    }

    @Override
    public LampRecyclerAdapter.LampViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_lamp_cardview, parent,false);
        return new LampViewHolder(v);
    }

    @Override
    public void onBindViewHolder(LampViewHolder holder, int position){
        Lamp positionLamp = lamps.get(position);
        holder.cardView.setOnClickListener(view -> Toast.makeText(holder.cardView.getContext(), "Op card gedrukt!", Toast.LENGTH_SHORT).show());
        holder.lampTitle.setText(positionLamp.getLampName());
        holder.lampSwitch.setChecked(positionLamp.getState().isOn());
    }

    @Override
    public int getItemCount() {
        return lamps.size();
    }
}