package ttit.com.shuvo.eliteforce.fleet.req_stat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.fleet.req_stat.arraylists.RequisitionList;

public class RequisitionAdapter extends RecyclerView.Adapter<RequisitionAdapter.RAHolder> {

    public ArrayList<RequisitionList> requisitionLists;
    public Context myContext;

    public RequisitionAdapter(ArrayList<RequisitionList> requisitionLists, Context myContext) {
        this.requisitionLists = requisitionLists;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public RAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.req_status_list_view_details, parent, false);
        return new RAHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RAHolder holder, int position) {
        RequisitionList statusList = requisitionLists.get(position);

        holder.tokenNo.setText(statusList.getFr_code());
        String stat = statusList.getReq_stat();

        switch (stat) {
            case "0":
                stat = "REJECTED";
                holder.reqCardView.setCardBackgroundColor(Color.parseColor("#c0392b"));
                break;
            case "1":
                stat = "APPROVED";
                holder.reqCardView.setCardBackgroundColor(Color.parseColor("#1abc9c"));
                break;
            case "2":
                stat = "ON HOLD";
                holder.reqCardView.setCardBackgroundColor(Color.parseColor("#fdcb6e"));
                break;
            case "3":
                stat = "PENDING";
                holder.reqCardView.setCardBackgroundColor(Color.parseColor("#636e72"));
                break;
        }

        holder.reqStatus.setText(stat);
        holder.reqDate.setText(statusList.getReq_date());
        holder.fromLocation.setText(statusList.getFrom_location());
        holder.fromDate.setText(statusList.getFrom_date());
        holder.toLocation.setText(statusList.getTo_location());
        holder.toDate.setText(statusList.getTo_date());
        holder.vhType.setText(statusList.getVh_type());
        holder.vhQty.setText(statusList.getFr_qty());
        holder.trvQty.setText(statusList.getFr_travelers_qty());
    }

    @Override
    public int getItemCount() {
        return requisitionLists != null ? requisitionLists.size() : 0;
    }

    public static class RAHolder extends RecyclerView.ViewHolder {

        TextView tokenNo;
        CardView reqCardView;
        TextView reqStatus;
        TextView reqDate;
        TextView fromLocation;
        TextView fromDate;
        TextView toLocation;
        TextView toDate;
        TextView vhType;
        TextView vhQty;
        TextView trvQty;

        public RAHolder(@NonNull View itemView) {
            super(itemView);

            tokenNo = itemView.findViewById(R.id.requisition_token_in_req_stat_list);
            reqCardView = itemView.findViewById(R.id.requisition_status_card);
            reqStatus = itemView.findViewById(R.id.requisition_status_in_req_stat_list);
            reqDate = itemView.findViewById(R.id.request_date_in_req_stat_list);
            fromLocation = itemView.findViewById(R.id.from_location_all_area_in_req_stat_list);
            fromDate = itemView.findViewById(R.id.from_location_date_in_req_stat_list);
            toLocation = itemView.findViewById(R.id.to_location_all_area_in_req_stat_list);
            toDate = itemView.findViewById(R.id.to_location_date_in_req_stat_list);
            vhType = itemView.findViewById(R.id.requisition_fleet_type_in_req_stat_list);
            vhQty = itemView.findViewById(R.id.vehicle_qty_in_req_stat_list);
            trvQty = itemView.findViewById(R.id.travelers_qty_in_req_stat_list);
        }
    }
}
