package ttit.com.shuvo.eliteforce.dialogue_box.adapters;

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
import ttit.com.shuvo.eliteforce.basic_model.SelectApproveReqList;

public class SelectApproveReqAdapter extends RecyclerView.Adapter<SelectApproveReqAdapter.ApproveHolder>{

    private ArrayList<SelectApproveReqList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;

    public SelectApproveReqAdapter(ArrayList<SelectApproveReqList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    @NonNull
    @Override
    public ApproveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.approval_request_view, parent, false);
        return new ApproveHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ApproveHolder holder, int position) {
        SelectApproveReqList approveReqList = mCategoryItem.get(position);

        holder.ftext.setText(approveReqList.getReqCode());
        holder.stext.setText(approveReqList.getName());
        holder.ttext.setText(approveReqList.getId());
        holder.fotext.setText(approveReqList.getAppdate());
        holder.fitext.setText(approveReqList.getUpDate());
    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public static class ApproveHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView ftext;
        TextView stext;
        TextView ttext;
        TextView fotext;
        TextView fitext;

        ClickedItem mClickedItem;

        public ApproveHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            ftext = itemView.findViewById(R.id.app_code_approve);
            stext = itemView.findViewById(R.id.emp_name_approve);
            ttext = itemView.findViewById(R.id.emp_id_approve);
            fotext = itemView.findViewById(R.id.app_date_approve);
            fitext = itemView.findViewById(R.id.update_date_approve);

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

    public void filterList(ArrayList<SelectApproveReqList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }
}
