package ttit.com.shuvo.eliteforce.movement_reg.movements.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_report.map_view.TimeLineActivity;
import ttit.com.shuvo.eliteforce.movement_reg.movements.arraylists.MovementList;

public class MovementStatusAdapter extends RecyclerView.Adapter<MovementStatusAdapter.MSAHolder> {
    public ArrayList<MovementList> movementLists;
    public Context myContext;
    public static Blob movementBlob;

    public MovementStatusAdapter(ArrayList<MovementList> movementLists, Context myContext) {
        this.movementLists = movementLists;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public MSAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.movement_status_details_layout, parent, false);
        return new MSAHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MSAHolder holder, int position) {
        MovementList movementList = movementLists.get(position);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
        String dd = simpleDateFormat.format(calendar.getTime());
        dd = dd.toUpperCase(Locale.ENGLISH);
        if (dd.equals(movementList.getM_date())) {
            dd = "TODAY";
        }
        else {
            dd = movementList.getM_date();
        }
        holder.moveDateSum.setText(dd);
        holder.moveCode.setText(movementList.getFmr_code());
        holder.moveDate.setText(movementList.getM_date());
        String mt;
        switch (movementList.getFmr_movement_type()) {
            case "1":
                mt = "CIT Service";
                break;
            case "2":
                mt = "Guard Service";
                break;
            case "3":
                mt = "Visitor Receive";
                break;
            case "4":
                mt = "Others";
                break;
            default:
                mt = "";
                break;
        }

        holder.moveType.setText(mt);
        holder.movePurpose.setText(movementList.getFmr_movement_details());

        if (movementList.getVehicle_name().isEmpty()) {
            holder.vhlLay.setVisibility(View.GONE);
        }
        else {
            holder.vhlLay.setVisibility(View.VISIBLE);
        }
        holder.moveVhl.setText(movementList.getVehicle_name());

        if (movementList.getDi_full_name().isEmpty()) {
            holder.drLay.setVisibility(View.GONE);
        }
        else {
            holder.drLay.setVisibility(View.VISIBLE);
        }
        holder.moveDriver.setText(movementList.getDi_full_name());

        if (movementList.getAd_name().isEmpty()) {
            holder.clientLay.setVisibility(View.GONE);
        }
        else {
            holder.clientLay.setVisibility(View.VISIBLE);
        }
        holder.moveClient.setText(movementList.getAd_name());

        if (movementList.getFr_code().isEmpty()) {
            holder.reqLay.setVisibility(View.GONE);
        }
        else {
            holder.reqLay.setVisibility(View.VISIBLE);
        }
        holder.forReq.setText(movementList.getFr_code());

        if (movementList.getMap_value().equals("0")) {
            holder.maps.setVisibility(View.GONE);
        }
        else {
            holder.maps.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return movementLists != null ? movementLists.size() : 0;
    }

    public class MSAHolder extends RecyclerView.ViewHolder {

        TextView moveDateSum;
        TextView moveCode;
        TextView moveDate;
        TextView moveType;
        TextView movePurpose;
        LinearLayout vhlLay;
        TextView moveVhl;
        LinearLayout drLay;
        TextView moveDriver;
        LinearLayout clientLay;
        TextView moveClient;
        LinearLayout reqLay;
        TextView forReq;
        CardView maps;

        public MSAHolder(@NonNull View itemView) {
            super(itemView);

            moveDateSum = itemView.findViewById(R.id.movement_date_summary);
            moveCode = itemView.findViewById(R.id.movement_code_in_movement_status);
            moveDate = itemView.findViewById(R.id.movement_date_in_movement_status);
            moveType = itemView.findViewById(R.id.movement_type_in_movement_status);
            movePurpose = itemView.findViewById(R.id.movement_purpose_in_movement_status);
            vhlLay = itemView.findViewById(R.id.movement_vehicle_m_s_lay);
            moveVhl = itemView.findViewById(R.id.movement_vehicle_in_movement_status);
            drLay = itemView.findViewById(R.id.movement_driver_m_s_lay);
            moveDriver = itemView.findViewById(R.id.movement_driver_in_movement_status);
            clientLay = itemView.findViewById(R.id.movement_client_m_s_lay);
            moveClient = itemView.findViewById(R.id.movement_client_in_movement_status);
            reqLay = itemView.findViewById(R.id.movement_for_req_lay);
            forReq = itemView.findViewById(R.id.movement_for_req_in_movement_status);
            maps = itemView.findViewById(R.id.map_of_movement_in_ms);

            maps.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                Intent intent = new Intent(myContext, TimeLineActivity.class);
                String elr_id = movementLists.get(getAdapterPosition()).getFmrd_id();
                intent.putExtra("ELR", elr_id);
                intent.putExtra("MOVE_FLAG",1);
                movementBlob = movementLists.get(getAdapterPosition()).getMove_file();
                activity.startActivity(intent);
            });
        }
    }
}
