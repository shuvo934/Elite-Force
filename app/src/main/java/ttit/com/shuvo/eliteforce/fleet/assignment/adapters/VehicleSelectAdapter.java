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
import ttit.com.shuvo.eliteforce.fleet.assignment.arraylists.VehicleList;

public class VehicleSelectAdapter extends RecyclerView.Adapter<VehicleSelectAdapter.VSAHolder> {
    private ArrayList<VehicleList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public VehicleSelectAdapter(ClickedItem myClickedItem, Context myContext, ArrayList<VehicleList> mCategoryItem) {
        this.myClickedItem = myClickedItem;
        this.myContext = myContext;
        this.mCategoryItem = mCategoryItem;
    }

    @NonNull
    @Override
    public VSAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.vehicle_list_details_view, parent, false);
        return new VSAHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull VSAHolder holder, int position) {
        VehicleList vehicleList = mCategoryItem.get(position);

        holder.vhName.setText(vehicleList.getName());
        holder.vhModel.setText(vehicleList.getModel());
        holder.vhYear.setText(vehicleList.getYear());
        holder.vhReg.setText(vehicleList.getReg_no());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem != null ? mCategoryItem.size() : 0;
    }


    public static class VSAHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView vhName;
        TextView vhYear;
        TextView vhModel;
        TextView vhReg;
        ClickedItem mClickedItem;

        public VSAHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            vhName = itemView.findViewById(R.id.vehicle_name_from_list);
            vhYear = itemView.findViewById(R.id.vehicle_year_from_list);
            vhModel = itemView.findViewById(R.id.vehicle_model_from_list);
            vhReg = itemView.findViewById(R.id.vehicle_reg_no_from_list);

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

    public void filterList(ArrayList<VehicleList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}
