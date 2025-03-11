package ttit.com.shuvo.eliteforce.fleet.assignment.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.fleet.assignment.arraylists.RequisitionToApproveList;

public class RequisitionApproveAdapter extends RecyclerView.Adapter<RequisitionApproveAdapter.RAAHolder> {
    private ArrayList<RequisitionToApproveList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public RequisitionApproveAdapter(ClickedItem myClickedItem, Context myContext, ArrayList<RequisitionToApproveList> mCategoryItem) {
        this.myClickedItem = myClickedItem;
        this.myContext = myContext;
        this.mCategoryItem = mCategoryItem;
    }

    @NonNull
    @Override
    public RAAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.req_list_for_assign_details_view, parent, false);
        return new RAAHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull RAAHolder holder, int position) {
        RequisitionToApproveList approveList = mCategoryItem.get(position);

        holder.token.setText(approveList.getFr_code());
        holder.reqDate.setText(approveList.getReq_date());
        holder.requester.setText(approveList.getEmp_name());
        String div_dep = approveList.getDivm_name() + "\n" + approveList.getDept_name();
        holder.rDivDep.setText(div_dep);
        holder.frmLoc.setText(approveList.getFr_from_address());
        holder.frmLocDate.setText(approveList.getFrom_date());
        holder.toLoc.setText(approveList.getFr_to_address());
        holder.toLocDate.setText(approveList.getTo_date());
        holder.reqFl.setText(approveList.getVh_type());
        holder.vhQty.setText(approveList.getFr_qty());
        holder.trvQty.setText(approveList.getFr_travelers_qty());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem != null ? mCategoryItem.size() : 0;
    }

    public static class RAAHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView token;
        TextView reqDate;
        TextView requester;
        TextView rDivDep;
        TextView frmLoc;
        TextView frmLocDate;
        TextView toLoc;
        TextView toLocDate;
        TextView reqFl;
        TextView vhQty;
        TextView trvQty;

        ClickedItem mClickedItem;

        public RAAHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            token = itemView.findViewById(R.id.requisition_token_in_req_selected_list);
            reqDate = itemView.findViewById(R.id.request_date_in_req_selected_list);
            requester = itemView.findViewById(R.id.requester_in_req_selected_list);
            rDivDep = itemView.findViewById(R.id.requester_div_dept_in_req_selected_list);
            frmLoc = itemView.findViewById(R.id.from_location_all_area_in_req_selected_list);
            frmLocDate = itemView.findViewById(R.id.from_location_date_in_req_selected_list);
            toLoc = itemView.findViewById(R.id.to_location_all_area_in_req_selected_list);
            toLocDate = itemView.findViewById(R.id.to_location_date_in_req_selected_list);
            reqFl = itemView.findViewById(R.id.requisition_fleet_type_in_req_selected_list);
            vhQty = itemView.findViewById(R.id.vehicle_qty_in_req_selected_list);
            trvQty = itemView.findViewById(R.id.travelers_qty_in_req_selected_list);

            this.mClickedItem = ci;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    public void filterList(ArrayList<RequisitionToApproveList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}
