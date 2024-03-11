package ttit.com.shuvo.eliteforce.leave.leave_balance.adpaters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.leave.leave_balance.model.LeaveBalanceList;

public class LeaveBalanceAdapter extends RecyclerView.Adapter<LeaveBalanceAdapter.LeaveHolder>{

    public ArrayList<LeaveBalanceList> leaveBalanceLists;
    public Context myContext;

    public LeaveBalanceAdapter(ArrayList<LeaveBalanceList> leaveBalanceLists, Context myContext) {
        this.leaveBalanceLists = leaveBalanceLists;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public LeaveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.leave_balance_list_view, parent, false);
        return new LeaveHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveHolder holder, int position) {
        LeaveBalanceList leaveBalanceList = leaveBalanceLists.get(position);

        holder.category.setText(leaveBalanceList.getCategory());
        holder.code.setText(leaveBalanceList.getCode());
        holder.open.setText(leaveBalanceList.getOpening_qty());
        holder.earn.setText(leaveBalanceList.getEarn());
        holder.consumed.setText(leaveBalanceList.getConsumed());
        holder.transfer.setText(leaveBalanceList.getTransfered());
        holder.cash.setText(leaveBalanceList.getCash_taken());
        holder.balance.setText(leaveBalanceList.getBalance_qty());
    }

    @Override
    public int getItemCount() {
        return leaveBalanceLists.size();
    }

    public static class LeaveHolder extends RecyclerView.ViewHolder {

        TextView category;
        TextView code;
        TextView open;
        TextView earn;
        TextView consumed;
        TextView transfer;
        TextView cash;
        TextView balance;

        public LeaveHolder(@NonNull View itemView) {
            super(itemView);

            category = itemView.findViewById(R.id.leave_category);
            code = itemView.findViewById(R.id.short_code);
            open = itemView.findViewById(R.id.opening_qty);
            earn = itemView.findViewById(R.id.earn);
            consumed = itemView.findViewById(R.id.consumed);
            transfer = itemView.findViewById(R.id.transfered);
            cash = itemView.findViewById(R.id.cash_taken);
            balance = itemView.findViewById(R.id.balance_qty);
        }
    }
}
