package ttit.com.shuvo.eliteforce.check_in_out.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.check_in_out.arraylists.CompletedCheckList;
import ttit.com.shuvo.eliteforce.check_in_out.dialogs.PhotoDialogueExtra;

public class CompletedCheckAdapter extends RecyclerView.Adapter<CompletedCheckAdapter.CCAHolder> {
    private final ArrayList<CompletedCheckList> completedCheckLists;
    private final Context context;

    public CompletedCheckAdapter(ArrayList<CompletedCheckList> completedCheckLists, Context context) {
        this.completedCheckLists = completedCheckLists;
        this.context = context;
    }

    @NonNull
    @Override
    public CCAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.completed_check_list_view_details_layout, parent, false);
        return new CCAHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CCAHolder holder, int position) {
        CompletedCheckList goingCheckList = completedCheckLists.get(position);
        if (completedCheckLists.size() == 1) {
            holder.topBar.setVisibility(View.INVISIBLE);
            holder.bottomBar.setVisibility(View.INVISIBLE);
        }
        else {
            if (position == 0) {
                holder.topBar.setVisibility(View.INVISIBLE);
                holder.bottomBar.setVisibility(View.VISIBLE);
            } else if (position == completedCheckLists.size() - 1) {
                holder.topBar.setVisibility(View.VISIBLE);
                holder.bottomBar.setVisibility(View.INVISIBLE);
            } else {
                holder.topBar.setVisibility(View.VISIBLE);
                holder.bottomBar.setVisibility(View.VISIBLE);
            }
        }

        holder.checkDate.setText(goingCheckList.getCheck_date());

        Glide.with(context)
                .load(goingCheckList.getCior_blob())
                .error(R.drawable.loading_error)
                .placeholder(R.drawable.loading)
                .into(holder.imageView);

        holder.regNo.setText(goingCheckList.getCior_register_no());
        holder.companyName.setText(goingCheckList.getCior_company_info());
        holder.comAddress.setText(goingCheckList.getCior_company_loc_info());
        holder.checkInTime.setText(goingCheckList.getIn_time());
        holder.inRemarks.setText(goingCheckList.getCior_in_remarks());
        holder.checkOutTime.setText(goingCheckList.getOut_time());
        holder.outRemarks.setText(goingCheckList.getCior_out_remarks());
    }

    @Override
    public int getItemCount() {
        return completedCheckLists != null ? completedCheckLists.size() : 0;
    }

    public class CCAHolder extends RecyclerView.ViewHolder {

        LinearLayout topBar;
        TextView checkDate;
        LinearLayout bottomBar;
        ImageView imageView;
        TextView regNo;
        TextView companyName;
        TextView comAddress;
        TextView checkInTime;
        TextView inRemarks;
        TextView checkOutTime;
        TextView outRemarks;

        public CCAHolder(@NonNull View itemView) {
            super(itemView);
            topBar = itemView.findViewById(R.id.top_bar_of_check_date_com_cio);
            checkDate = itemView.findViewById(R.id.check_in_date_in_check_in_list_com_cio);
            bottomBar = itemView.findViewById(R.id.bottom_bar_of_check_date_com_cio);
            imageView = itemView.findViewById(R.id.check_in_image_in_list_com_cio);
            regNo = itemView.findViewById(R.id.check_in_register_no_com_cio);
            companyName = itemView.findViewById(R.id.company_name_of_check_in_list_com_cio);
            comAddress = itemView.findViewById(R.id.company_address_of_check_in_list_com_cio);
            checkInTime = itemView.findViewById(R.id.check_in_time_of_check_in_list_com_cio);
            inRemarks = itemView.findViewById(R.id.in_remarks_of_check_in_list_com_cio);
            checkOutTime = itemView.findViewById(R.id.check_out_time_of_check_in_list_com_cio);
            outRemarks = itemView.findViewById(R.id.out_remarks_of_check_in_list_com_cio);

            imageView.setOnClickListener(view -> {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                PhotoDialogueExtra photoDialoguePE = new PhotoDialogueExtra(context,completedCheckLists.get(getAdapterPosition()).getCior_blob());
                photoDialoguePE.show(activity.getSupportFragmentManager(),"PICTURE_4");
            });
        }
    }
}
