package ttit.com.shuvo.eliteforce.payRoll.pay_slip.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.payRoll.pay_slip.model.MonthSelectList;

public class MonthSelectAdapter extends RecyclerView.Adapter<MonthSelectAdapter.MonthHolder>{

    private ArrayList<MonthSelectList> monthSelectLists;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public MonthSelectAdapter(ArrayList<MonthSelectList> monthSelectLists, Context context, ClickedItem cli) {
        this.monthSelectLists = monthSelectLists;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    @NonNull
    @Override
    public MonthHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.month_select_list, parent, false);
        return new MonthHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthHolder holder, int position) {
        MonthSelectList monthSelectList = monthSelectLists.get(position);

        holder.monthName.setText(monthSelectList.getMonthName());
        holder.monStart.setText(monthSelectList.getMonthstart());
        holder.monEnd.setText(monthSelectList.getMonthEnd());
    }

    @Override
    public int getItemCount() {
        return monthSelectLists.size();
    }

    public static class MonthHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView monthName;
        TextView monStart;
        TextView monEnd;

        ClickedItem mClickedItem;

        public MonthHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            monthName = itemView.findViewById(R.id.month_name_mmm);
            monStart = itemView.findViewById(R.id.month_start);
            monEnd = itemView.findViewById(R.id.month_end);

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

    public void filterList(ArrayList<MonthSelectList> filteredList) {
        monthSelectLists = filteredList;
        notifyDataSetChanged();
    }
}
