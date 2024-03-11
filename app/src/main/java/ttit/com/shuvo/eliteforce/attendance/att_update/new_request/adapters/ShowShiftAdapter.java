package ttit.com.shuvo.eliteforce.attendance.att_update.new_request.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_update.new_request.model.ShowShiftList;

public class ShowShiftAdapter extends RecyclerView.Adapter<ShowShiftAdapter.ShowShiftHolder>{

    public ArrayList<ShowShiftList> showShiftLists;

    public Context myContext;


    public ShowShiftAdapter(ArrayList<ShowShiftList> showShiftLists, Context myContext) {
        this.showShiftLists = showShiftLists;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public ShowShiftHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.shift_in_details_view, parent, false);
        return new ShowShiftHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowShiftHolder holder, int position) {

        ShowShiftList showShiftList = showShiftLists.get(position);

        holder.shift.setText(showShiftList.getShift());
        holder.inTime.setText(showShiftList.getInTime());
        holder.lateArr.setText(showShiftList.getLateART());
        holder.early.setText(showShiftList.getEarlyBT());
        holder.ouutt.setText(showShiftList.getOutTime());
        holder.extend.setText(showShiftList.getExtendedOT());
    }

    @Override
    public int getItemCount() {
        return showShiftLists.size();
    }

    public static class ShowShiftHolder extends RecyclerView.ViewHolder {

        TextView shift;
        TextView inTime;
        TextView lateArr;
        TextView early;
        TextView ouutt;
        TextView extend;


        public ShowShiftHolder(@NonNull View itemView) {
            super(itemView);

            shift = itemView.findViewById(R.id.shift_name_from_list);
            inTime = itemView.findViewById(R.id.in_time_from_list);
            lateArr = itemView.findViewById(R.id.late_arr_time_from_list);
            early = itemView.findViewById(R.id.early_bef_time_from_list);
            ouutt = itemView.findViewById(R.id.out_time_from_list);
            extend = itemView.findViewById(R.id.extended_out_time_from_list);

        }
    }
}
