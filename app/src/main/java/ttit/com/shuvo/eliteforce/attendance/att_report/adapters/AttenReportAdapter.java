package ttit.com.shuvo.eliteforce.attendance.att_report.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Blob;
import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_report.map_view.MapsActivity;
import ttit.com.shuvo.eliteforce.attendance.att_report.map_view.TimeLineActivity;
import ttit.com.shuvo.eliteforce.attendance.att_report.model.AttenReportList;

public class AttenReportAdapter extends RecyclerView.Adapter<AttenReportAdapter.AttenHolder>{

    public ArrayList<AttenReportList> attenReportLists;
    public Context myContext;
    public static Blob blobFromAdapter;

    public AttenReportAdapter(ArrayList<AttenReportList> attenReportLists, Context myContext) {
        this.attenReportLists = attenReportLists;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public AttenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.att_rep_list_view, parent, false);
        return new AttenHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AttenHolder holder, int position) {
        AttenReportList aRtl = attenReportLists.get(position);

        holder.date.setText(aRtl.getDate());
        holder.shift.setText(aRtl.getShift());
        holder.punch.setText(aRtl.getPunchLoc());
        holder.inTime.setText(aRtl.getInTime());
        System.out.println(aRtl.getInStatus());

        String ininini = aRtl.getInStatus();
        String outtttt = aRtl.getOutStatus();

        if (ininini.isEmpty() && outtttt.isEmpty()) {
            holder.status.setText(aRtl.getStatus());
        } else if (ininini.isEmpty() && !outtttt.isEmpty()) {
            String sss = aRtl.getStatus() + "\n(" + outtttt + ")";
            holder.status.setText(sss);
        } else if (!ininini.isEmpty() && outtttt.isEmpty()) {
            String sss = aRtl.getStatus() + "\n(" + aRtl.getInStatus() + ")";
            holder.status.setText(sss);
        } else if (!ininini.isEmpty() && !outtttt.isEmpty()) {
            String sss = aRtl.getStatus() + "\n(" + aRtl.getInStatus() + ")" + "\n(" + aRtl.getOutStatus() + ")";
            holder.status.setText(sss);
        }

        holder.out.setText(aRtl.getOutTime());

        String attStatus = aRtl.getAttStatusColor();

        switch (attStatus) {
            case "Multi Station":
                holder.linearLayout.setBackgroundColor(Color.parseColor("#C0C000"));
                break;
            case "Out Miss":
                holder.linearLayout.setBackgroundColor(Color.parseColor("#00C080"));
                break;
            case "In Leave":
                holder.linearLayout.setBackgroundColor(Color.parseColor("#8F8F8F"));
                break;
            case "Off Day":
                holder.linearLayout.setBackgroundColor(Color.parseColor("#7FDFFE"));
                break;
            case "Present on Leave Day":
                holder.linearLayout.setBackgroundColor(Color.parseColor("#4080C0"));
                break;
            case "Present on Off Day":
                holder.linearLayout.setBackgroundColor(Color.parseColor("#E0C080"));
                break;
            case "Absent":
                holder.linearLayout.setBackgroundColor(Color.parseColor("#C000FF"));
                break;
            case "White":
                holder.linearLayout.setBackgroundColor(Color.WHITE);
                break;
            default:
                holder.linearLayout.setBackgroundColor(Color.WHITE);
                break;
        }

        if (aRtl.getDate().equals("Date")) {
            holder.date.setBackgroundColor(Color.parseColor("#ced6e0"));
        }
        else {
            holder.date.setBackgroundColor(Color.WHITE);
        }
        if (aRtl.getShift().equals("Shift")) {
            holder.shift.setBackgroundColor(Color.parseColor("#ced6e0"));
        }
        else {
            holder.shift.setBackgroundColor(Color.WHITE);
        }
        if (aRtl.getPunchLoc().equals("Punch Location")) {
            holder.punch.setBackgroundColor(Color.parseColor("#ced6e0"));
        }
        else {
            holder.punch.setBackgroundColor(Color.WHITE);
        }
        if (aRtl.getInTime().equals("In Time")) {
            holder.inTime.setBackgroundColor(Color.parseColor("#ced6e0"));
        }
        else {
            holder.inTime.setBackgroundColor(Color.WHITE);
        }
        if (aRtl.getOutTime().equals("Out Time")) {
            holder.out.setBackgroundColor(Color.parseColor("#ced6e0"));
        }
        else {
            holder.out.setBackgroundColor(Color.WHITE);
        }

        String in_lat = aRtl.getInLat();
        String in_lon = aRtl.getInLon();
        String out_lat = aRtl.getOutLat();
        String out_lon = aRtl.getOutLon();

        if (in_lat.isEmpty() || in_lon.isEmpty()) {
            holder.inLoc.setVisibility(View.GONE);
        } else {
            holder.inLoc.setVisibility(View.VISIBLE);
        }

        if (out_lat.isEmpty() || out_lon.isEmpty()) {
            holder.outLoc.setVisibility(View.GONE);
        } else {
            holder.outLoc.setVisibility(View.VISIBLE);
        }

        Blob blob = aRtl.getBlob();

        if (blob == null) {
            holder.timeline.setVisibility(View.GONE);
        } else {
            holder.timeline.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return attenReportLists.size();
    }

    public class AttenHolder extends RecyclerView.ViewHolder {

        TextView date;
        TextView status;
        TextView shift;
        TextView punch;
        TextView inTime;
        TextView out;
        Button inLoc;
        Button outLoc;
        Button timeline;

        LinearLayout linearLayout;

        public AttenHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date_0f_att);
            shift = itemView.findViewById(R.id.shift_of_att);
            status = itemView.findViewById(R.id.status_of_att);
            punch = itemView.findViewById(R.id.punch_loc_of_att);
            inTime = itemView.findViewById(R.id.in_time_of_att);
            out = itemView.findViewById(R.id.out_time_of_att);
            linearLayout = itemView.findViewById(R.id.status_of_att_lay);
            inLoc = itemView.findViewById(R.id.in_location);
            outLoc = itemView.findViewById(R.id.out_location);
            timeline = itemView.findViewById(R.id.timeline_activity_map);

            inLoc.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                String in_lat = attenReportLists.get(getAdapterPosition()).getInLat();
                String in_lon = attenReportLists.get(getAdapterPosition()).getInLon();
                String in_time = inTime.getText().toString();
                String dd = date.getText().toString();

                Intent intent = new Intent(myContext, MapsActivity.class);
                intent.putExtra("lat", in_lat);
                intent.putExtra("lon", in_lon);
                intent.putExtra("time", in_time);
                intent.putExtra("date", dd);
                intent.putExtra("status","Attendance In Time");
                activity.startActivity(intent);
            });

            outLoc.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                String out_lat = attenReportLists.get(getAdapterPosition()).getOutLat();
                String out_lon = attenReportLists.get(getAdapterPosition()).getOutLon();
                String out_time = out.getText().toString();
                String dd = date.getText().toString();

                Intent intent = new Intent(myContext, MapsActivity.class);
                intent.putExtra("lat", out_lat);
                intent.putExtra("lon", out_lon);
                intent.putExtra("time", out_time);
                intent.putExtra("date", dd);
                intent.putExtra("status","Attendance Out Time");
                activity.startActivity(intent);
            });

            timeline.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(myContext, TimeLineActivity.class);
                String elr_id = attenReportLists.get(getAdapterPosition()).getElr_id();
                intent.putExtra("ELR", elr_id);
                blobFromAdapter = attenReportLists.get(getAdapterPosition()).getBlob();
                activity.startActivity(intent);
            });
        }
    }
}
