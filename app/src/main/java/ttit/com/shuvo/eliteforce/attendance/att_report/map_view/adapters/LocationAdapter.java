package ttit.com.shuvo.eliteforce.attendance.att_report.map_view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_report.map_view.model.LocationNameArray;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocHolder>{

    public ArrayList<LocationNameArray> locationNameArrays;
    public Context myContext;
    private final ClickedItem myClickedItem;

    public LocationAdapter(ArrayList<LocationNameArray> locationNameArrays, Context myContext, ClickedItem cli) {
        this.locationNameArrays = locationNameArrays;
        this.myContext = myContext;
        this.myClickedItem = cli;
    }

    @NonNull
    @Override
    public LocHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.location_details, parent, false);
        return new LocHolder(v, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull LocHolder holder, int position) {

        LocationNameArray locNA = locationNameArrays.get(position);

        boolean isWay = locNA.getWay();

        if (isWay) {
            holder.lastLay.setVisibility(View.GONE);
            holder.midDes.setVisibility(View.GONE);
            String tt = "You were in:";
            holder.wayTrack.setText(tt);
            holder.place.setText(locNA.getFirstLocation());
            holder.endTime.setVisibility(View.GONE);
            holder.distancelay.setVisibility(View.GONE);
            holder.timeClay.setVisibility(View.GONE);
            if (locNA.getFirstTime().isEmpty()) {
                holder.startLay.setVisibility(View.GONE);
                holder.startTime.setVisibility(View.GONE);
            } else {
                holder.startLay.setVisibility(View.GONE);
                holder.startTime.setVisibility(View.VISIBLE);
                holder.startTime.setText(locNA.getFirstTime());
            }
        } else {
            holder.lastLay.setVisibility(View.VISIBLE);
            holder.midDes.setVisibility(View.VISIBLE);
            String tt = "You went from ";
            holder.wayTrack.setText(tt);
            holder.place.setText(locNA.getFirstLocation());
            holder.lastPlace.setText(locNA.getLastLocation());
            if (locNA.getFirstTime().isEmpty()) {
                holder.startLay.setVisibility(View.GONE);
                holder.startTime.setVisibility(View.GONE);
            } else {
                holder.startLay.setVisibility(View.VISIBLE);
                holder.startTime.setVisibility(View.VISIBLE);
                holder.startTime.setText(locNA.getFirstTime());
            }

            if (locNA.getLastTime().isEmpty()) {
                holder.endTime.setVisibility(View.GONE);
            } else {
                holder.endTime.setVisibility(View.VISIBLE);
                holder.endTime.setText(locNA.getLastTime());
            }

            if (locNA.getDistance().isEmpty()) {
                holder.distancelay.setVisibility(View.GONE);
            }else {
                holder.distancelay.setVisibility(View.VISIBLE);
                holder.distance.setText(locNA.getDistance());
            }

            if (locNA.getCalcTime().isEmpty()) {
                holder.timeClay.setVisibility(View.GONE);
            } else {
                holder.timeClay.setVisibility(View.VISIBLE);
                holder.calculateTime.setText(locNA.getCalcTime());
            }
        }
    }

    @Override
    public int getItemCount() {
        return locationNameArrays.size();
    }

    public static class LocHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView place;
        TextView wayTrack;
        LinearLayout midDes;
        LinearLayout lastLay;
        TextView lastPlace;
        LinearLayout startLay;
        TextView startTime;
        TextView endTime;
        LinearLayout distancelay;
        TextView distance;
        LinearLayout timeClay;
        TextView calculateTime;

        ClickedItem clickedItem;


        public LocHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);

            place = itemView.findViewById(R.id.place_text);
            wayTrack = itemView.findViewById(R.id.way_or_track);
            midDes = itemView.findViewById(R.id.mid_to_des);
            lastLay = itemView.findViewById(R.id.last_place_lay);
            lastPlace = itemView.findViewById(R.id.last_place_text);
            startLay = itemView.findViewById(R.id.start_time_layout);
            startTime = itemView.findViewById(R.id.start_time);
            endTime = itemView.findViewById(R.id.end_time);
            distancelay =itemView.findViewById(R.id.distance_layout);
            distance = itemView.findViewById(R.id.calculate_distance);
            timeClay = itemView.findViewById(R.id.time_calculate_layout);
            calculateTime = itemView.findViewById(R.id.calculate_time);

            this.clickedItem = ci;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            clickedItem.onCategoryClicked(getAdapterPosition());
        }
    }
    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }
}
