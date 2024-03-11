package ttit.com.shuvo.eliteforce.leave.leave_application.new_application.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.leave.leave_application.new_application.model.LeaveTypeList;

public class LeaveTypeAdapter extends RecyclerView.Adapter<LeaveTypeAdapter.LeaveTypeHolder>{

    private ArrayList<LeaveTypeList> leaveTypeLists;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public LeaveTypeAdapter(ArrayList<LeaveTypeList> leaveTypeLists, Context context, ClickedItem cli) {
        this.leaveTypeLists = leaveTypeLists;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    @NonNull
    @Override
    public LeaveTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.leave_type_list_view, parent, false);
        return new LeaveTypeHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveTypeHolder holder, int position) {
        LeaveTypeList leaveTypeList = leaveTypeLists.get(position);

        holder.ftext.setText(leaveTypeList.getTypeName());
    }

    @Override
    public int getItemCount() {
        return leaveTypeLists.size();
    }

    public static class LeaveTypeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ftext;
        ClickedItem mClickedItem;

        public LeaveTypeHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            ftext = itemView.findViewById(R.id.leave_type_name);

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

    public void filterList(ArrayList<LeaveTypeList> filteredList) {
        leaveTypeLists = filteredList;
        notifyDataSetChanged();
    }
}
