package my.philipshueremote.MainUI.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
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

import java.util.ArrayList;
import java.util.List;

import my.philipshueremote.DataCommunication.Requests.PremadeHueRequest;
import my.philipshueremote.DataCommunication.VolleyJsonSocket;
import my.philipshueremote.Database.Entities.Lamp;
import my.philipshueremote.Init.Models.BridgeInfo;
import my.philipshueremote.MainUI.Activities.LampEditActivity;
import my.philipshueremote.R;

public class LampRecyclerAdapter extends RecyclerView.Adapter<LampRecyclerAdapter.LampViewHolder> {
    private VolleyJsonSocket socket;
    private BridgeInfo selectedBridge;
    private List<Lamp> lamps;

    public LampRecyclerAdapter(Context context) {
        socket = VolleyJsonSocket.getInstance(context);
        lamps = new ArrayList<>();
        selectedBridge = null;
    }

    public void updateBridge(BridgeInfo bridge) {
        selectedBridge = bridge;
    }

    public void updateLampList(List<Lamp> lamps) {
        this.lamps = lamps;
    }

    public static class LampViewHolder extends ViewHolder {
        public CardView cardView;
        public ConstraintLayout backgroundDrawable;
        public TextView lampTitle;
        public ImageView lampImage;
        public Switch lampSwitch;
        public SeekBar lampBrightness;

        public LampViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.main_lamp_cardview);
            this.backgroundDrawable = itemView.findViewById(R.id.main_lamp_constraint);
            this.lampTitle = itemView.findViewById(R.id.main_lamp_cardview_title);
            this.lampImage = itemView.findViewById(R.id.main_lamp_cardview_image);
            this.lampBrightness = itemView.findViewById(R.id.main_lamp_cardview_seekbar);
            this.lampSwitch = itemView.findViewById(R.id.main_lamp_cardview_switch);
        }
    }

    @Override
    public LampRecyclerAdapter.LampViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_lamp_cardview, parent, false);
        return new LampViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LampViewHolder holder, int position) {
        Lamp positionLamp = lamps.get(position);
        holder.backgroundDrawable.setBackgroundColor(positionLamp.getState().getRGB());

        holder.lampTitle.setText(positionLamp.getLampName());
        holder.lampBrightness.setProgress(positionLamp.getState().getBrightness());
        holder.lampSwitch.setChecked(positionLamp.getState().isOn());

        holder.lampSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            positionLamp.getState().setOn(b);
            socket.addRequestToQueue(PremadeHueRequest.lampPutOnOffRequest(selectedBridge, positionLamp, null, null));
        });

        holder.lampBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            short brightness = positionLamp.getState().getBrightness();
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b) {
                    brightness = (short) i;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                positionLamp.getState().setBrightness(brightness);
                socket.addRequestToQueue(PremadeHueRequest.lampPutBrightnessRequest(selectedBridge, positionLamp));
            }
        });

        holder.cardView.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putShort("LAMP_ID", positionLamp.getLampApiID());
            bundle.putParcelable("BRIDGE", selectedBridge);

            Intent intent = new Intent(holder.cardView.getContext(), LampEditActivity.class);
            intent.putExtras(bundle);

            holder.cardView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return lamps.size();
    }
}