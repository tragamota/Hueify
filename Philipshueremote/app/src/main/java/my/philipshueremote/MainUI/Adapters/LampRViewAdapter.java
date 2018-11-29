package my.philipshueremote.MainUI.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import my.philipshueremote.Database.Entities.Lamp;
import my.philipshueremote.R;

public class LampRViewAdapter extends RecyclerView.Adapter<LampRViewAdapter.LampViewHolder>{
    private List<Lamp> lamps;

    public static class LampViewHolder extends ViewHolder{
        public CardView cardView;
        public TextView title;
        public ImageView image;
        public SeekBar seekBar;
        public Switch cardviewSwitch;

        public LampViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.cardview_cardview);
            this.title = itemView.findViewById(R.id.cardview_title);
            this.image = itemView.findViewById(R.id.cardview_image);
            this.seekBar = itemView.findViewById(R.id.cardview_seekbar);
            this.cardviewSwitch = itemView.findViewById(R.id.cardview_switch);
        }
    }

    public LampRViewAdapter(List lamps){
        this.lamps = lamps;
    }

    public LampRViewAdapter.LampViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_item, parent , false);
        LampViewHolder view = new LampViewHolder(v);
        return view;
    }


    @Override

    public void onBindViewHolder(LampViewHolder holder, int position){
        holder.title.setText(lamps.get(position).getLampName());
    }

    @Override
    public int getItemCount() {
        return lamps.size();
    }

    public void update(List<Lamp> lamps){
        this.lamps = lamps;
    }
}
