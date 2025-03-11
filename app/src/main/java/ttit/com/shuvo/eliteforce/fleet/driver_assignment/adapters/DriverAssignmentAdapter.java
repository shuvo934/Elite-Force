package ttit.com.shuvo.eliteforce.fleet.driver_assignment.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.fleet.driver_assignment.arraylists.DriverAssignmentList;
import ttit.com.shuvo.eliteforce.movement_reg.new_reg.NewRegister;

public class DriverAssignmentAdapter extends RecyclerView.Adapter<DriverAssignmentAdapter.DAAHolder> {

    public ArrayList<DriverAssignmentList> driverAssignmentLists;
    public Context myContext;

    public DriverAssignmentAdapter(ArrayList<DriverAssignmentList> driverAssignmentLists, Context myContext) {
        this.driverAssignmentLists = driverAssignmentLists;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public DAAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.driver_assignment_details_view, parent, false);
        return new DAAHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DAAHolder holder, int position) {
        DriverAssignmentList list = driverAssignmentLists.get(position);
        holder.purposeVisit.setText(list.getVisit_purpose());
        holder.fromLocation.setText(list.getFrom_location());
        holder.fromDate.setText(list.getFrom_date());
        holder.toLocation.setText(list.getTo_location());
        holder.toDate.setText(list.getTo_date());
        String vh = list.getVh_name() + " (" +list.getVh_reg_no() + ")";
        holder.vehicle.setText(vh);
        holder.requesterName.setText(list.getRequester_name());
        holder.requesterMobile.setText(list.getFr_requester_mobile_no());

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        String appt_date = list.getFrom_date();
        appt_date = appt_date.substring(0, appt_date.indexOf(","));
        appt_date = appt_date.toUpperCase(Locale.ENGLISH);

        Date todayDate = Calendar.getInstance().getTime();

        String to_date = simpleDateFormat.format(todayDate);
        to_date = to_date.toUpperCase(Locale.ENGLISH);

        if (appt_date.equals(to_date)) {
            holder.goMovement.setVisibility(View.VISIBLE);
        }
        else {
            holder.goMovement.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return driverAssignmentLists != null ? driverAssignmentLists.size() : 0;
    }

    public class DAAHolder extends RecyclerView.ViewHolder {

        TextView purposeVisit;
        CardView goMovement;
        TextView fromLocation;
        TextView fromDate;
        TextView toLocation;
        TextView toDate;
        TextView vehicle;
        TextView requesterName;
        TextView requesterMobile;

        public DAAHolder(@NonNull View itemView) {
            super(itemView);

            purposeVisit = itemView.findViewById(R.id.purpose_of_visit_driver_assign);
            goMovement = itemView.findViewById(R.id.go_to_movement_reg_in_driver_assign);
            fromLocation = itemView.findViewById(R.id.from_location_all_area_in_driver_assign);
            fromDate = itemView.findViewById(R.id.from_location_date_in_driver_assign);
            toLocation = itemView.findViewById(R.id.to_location_all_area_in_driver_assign);
            toDate = itemView.findViewById(R.id.to_location_date_in_driver_assign);
            vehicle = itemView.findViewById(R.id.vehicle_identity_in_driver_assign);
            requesterName = itemView.findViewById(R.id.requester_name_in_driver_assign);
            requesterMobile = itemView.findViewById(R.id.requester_mobile_in_driver_assign);

            goMovement.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) myContext;
                Intent intent = new Intent(myContext, NewRegister.class);
                String vh = driverAssignmentLists.get(getAdapterPosition()).getVh_name() + " (" +driverAssignmentLists.get(getAdapterPosition()).getVh_reg_no() + ")";
                intent.putExtra("FROM_REQ", 1);
                intent.putExtra("REQ_ID",driverAssignmentLists.get(getAdapterPosition()).getFra_requision_id());
                intent.putExtra("REQ_TOKEN",driverAssignmentLists.get(getAdapterPosition()).getFr_code());
                intent.putExtra("VI_ID",driverAssignmentLists.get(getAdapterPosition()).getFra_fleet_vi_id());
                intent.putExtra("VI_NAME",vh);
                intent.putExtra("FRA_PK",driverAssignmentLists.get(getAdapterPosition()).getFra_pk());
                activity.startActivity(intent);
                activity.finish();
            });
        }
    }
}
