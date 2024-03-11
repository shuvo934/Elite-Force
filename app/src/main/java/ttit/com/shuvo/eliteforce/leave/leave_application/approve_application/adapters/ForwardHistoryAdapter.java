package ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.model.ForwardHistoryList;

public class ForwardHistoryAdapter extends RecyclerView.Adapter<ForwardHistoryAdapter.FHVHolder>{
    public ArrayList<ForwardHistoryList> forwardHistoryLists;

    public Context myContext;


    public ForwardHistoryAdapter(ArrayList<ForwardHistoryList> forwardHistoryLists, Context myContext) {
        this.forwardHistoryLists = forwardHistoryLists;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public FHVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.forward_history_list_item, parent, false);
        return new FHVHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FHVHolder holder, int position) {
        ForwardHistoryList forwardHistoryList = forwardHistoryLists.get(position);

        holder.foby.setText(forwardHistoryList.getForby());
        holder.foc.setText(forwardHistoryList.getForComm());
        holder.fot.setText(forwardHistoryList.getForTo());
    }

    @Override
    public int getItemCount() {
        return forwardHistoryLists.size();
    }

    public static class FHVHolder extends RecyclerView.ViewHolder {

        TextView foby;
        TextView foc;
        TextView fot;

        public FHVHolder(@NonNull View itemView) {
            super(itemView);

            foby = itemView.findViewById(R.id.forwarded_by_leave_approve);
            foc = itemView.findViewById(R.id.forward_comment_leave_approve);
            fot = itemView.findViewById(R.id.forward_to_leave_approve);
        }
    }
}
