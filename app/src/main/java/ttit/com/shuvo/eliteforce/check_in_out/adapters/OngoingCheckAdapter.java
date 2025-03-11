package ttit.com.shuvo.eliteforce.check_in_out.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.check_in_out.addCheck.AddCheckIn;
import ttit.com.shuvo.eliteforce.check_in_out.arraylists.OnGoingCheckList;
import ttit.com.shuvo.eliteforce.check_in_out.dialogs.CheckOutDialog;
import ttit.com.shuvo.eliteforce.check_in_out.dialogs.PhotoDialogueExtra;

public class OngoingCheckAdapter extends RecyclerView.Adapter<OngoingCheckAdapter.OCAHolder> {
    private ArrayList<OnGoingCheckList> onGoingCheckLists;
    private Context context;

    public OngoingCheckAdapter(ArrayList<OnGoingCheckList> onGoingCheckLists, Context context) {
        this.onGoingCheckLists = onGoingCheckLists;
        this.context = context;
    }

    @NonNull
    @Override
    public OCAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ongoing_check_in_list_view_details_layout, parent, false);
        return new OCAHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OCAHolder holder, int position) {
        OnGoingCheckList goingCheckList = onGoingCheckLists.get(position);
        if (onGoingCheckLists.size() == 1) {
            holder.topBar.setVisibility(View.INVISIBLE);
            holder.bottomBar.setVisibility(View.INVISIBLE);
        }
        else {
            if (position == 0) {
                holder.topBar.setVisibility(View.INVISIBLE);
                holder.bottomBar.setVisibility(View.VISIBLE);
            } else if (position == onGoingCheckLists.size() - 1) {
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
    }

    @Override
    public int getItemCount() {
        return onGoingCheckLists != null ? onGoingCheckLists.size() : 0;
    }

    public class OCAHolder extends RecyclerView.ViewHolder {

        LinearLayout topBar;
        TextView checkDate;
        LinearLayout bottomBar;
        ImageView imageView;
        TextView regNo;
        TextView companyName;
        TextView comAddress;
        TextView checkInTime;
        TextView inRemarks;
        MaterialButton checkOut;

        public OCAHolder(@NonNull View itemView) {
            super(itemView);
            topBar = itemView.findViewById(R.id.top_bar_of_check_date);
            checkDate = itemView.findViewById(R.id.check_in_date_in_check_in_list);
            bottomBar = itemView.findViewById(R.id.bottom_bar_of_check_date);
            imageView = itemView.findViewById(R.id.check_in_image_in_list);
            regNo = itemView.findViewById(R.id.check_in_register_no);
            companyName = itemView.findViewById(R.id.company_name_of_check_in_list);
            comAddress = itemView.findViewById(R.id.company_address_of_check_in_list);
            checkInTime = itemView.findViewById(R.id.check_in_time_of_check_in_list);
            inRemarks = itemView.findViewById(R.id.in_remarks_of_check_in_list);
            checkOut = itemView.findViewById(R.id.check_out_button_in_check_in_list);

            imageView.setOnClickListener(view -> {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                PhotoDialogueExtra photoDialoguePE = new PhotoDialogueExtra(context,onGoingCheckLists.get(getAdapterPosition()).getCior_blob());
                photoDialoguePE.show(activity.getSupportFragmentManager(),"PICTURE_4");
            });

            checkOut.setOnClickListener(view -> {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                CheckOutDialog checkOutDialog = new CheckOutDialog(context, onGoingCheckLists.get(getAdapterPosition()).getCior_id());
                checkOutDialog.show(activity.getSupportFragmentManager(),"CH_OUT");
            });
        }
    }

}
