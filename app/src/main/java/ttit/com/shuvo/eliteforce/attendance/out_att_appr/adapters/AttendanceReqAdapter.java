package ttit.com.shuvo.eliteforce.attendance.out_att_appr.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.out_att_appr.approve_att.ApproveAttendance;
import ttit.com.shuvo.eliteforce.attendance.out_att_appr.arraylists.AttendanceReqList;

public class AttendanceReqAdapter extends RecyclerView.Adapter<AttendanceReqAdapter.ARAHolder> {
    private final ArrayList<AttendanceReqList> mCategoryItem;
    private final Context myContext;

    public AttendanceReqAdapter(ArrayList<AttendanceReqList> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public ARAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.attendance_req_list_view, parent, false);
        return new ARAHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ARAHolder holder, int position) {
        AttendanceReqList attendanceReqList = mCategoryItem.get(position);

        holder.empName.setText(attendanceReqList.getEmp_name());
        holder.empDesignation.setText(attendanceReqList.getJob_calling_title());
        String dep_div = attendanceReqList.getDept_name()+", " +attendanceReqList.getDivm_name();
        holder.depDivName.setText(dep_div);
        holder.shiftName.setText(attendanceReqList.getOsm_name());
        holder.attDate.setText(attendanceReqList.getAtt_date());
        holder.attTime.setText(attendanceReqList.getAtt_time());
        String ts = "("+attendanceReqList.getTime_type()+")";
        holder.timeStatus.setText(ts);

        switch (attendanceReqList.getAtt_approve()) {
            case "2":
                holder.attApprove.setTextColor(myContext.getColor(R.color.elite_red));
                holder.attApprove.setText("Rejected");
                holder.attReqCard.setEnabled(false);
                holder.cardEnabled.setVisibility(View.VISIBLE);
                holder.detailsLay.setVisibility(View.INVISIBLE);
                break;
            case "1":
                holder.attApprove.setTextColor(myContext.getColor(R.color.present_color));
                holder.attApprove.setText("Approved");
                holder.attReqCard.setEnabled(false);
                holder.cardEnabled.setVisibility(View.VISIBLE);
                holder.detailsLay.setVisibility(View.INVISIBLE);
                break;
            default:
                holder.attApprove.setTextColor(myContext.getColor(R.color.progress_back_color));
                holder.attApprove.setText("Pending");
                holder.attReqCard.setEnabled(true);
                holder.cardEnabled.setVisibility(View.GONE);
                holder.detailsLay.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mCategoryItem != null ? mCategoryItem.size() : 0;
    }

    public class ARAHolder extends RecyclerView.ViewHolder {
        TextView empName;
        TextView empDesignation;
        TextView depDivName;
        TextView shiftName;
        TextView attDate;
        TextView attTime;
        TextView timeStatus;
        TextView attApprove;
        LinearLayout cardEnabled;
        LinearLayout detailsLay;
        MaterialCardView attReqCard;

        public ARAHolder(@NonNull View itemView) {
            super(itemView);
            empName = itemView.findViewById(R.id.emp_name_att_req);
            empDesignation = itemView.findViewById(R.id.emp_designation_att_req);
            depDivName = itemView.findViewById(R.id.emp_dept_div_att_req);
            shiftName = itemView.findViewById(R.id.shift_name_att_req);
            attDate = itemView.findViewById(R.id.attendance_date_att_req);
            attTime = itemView.findViewById(R.id.attendance_time_att_req);
            timeStatus = itemView.findViewById(R.id.in_out_status_att_req);
            attApprove = itemView.findViewById(R.id.attendance_req_approve_status);
            attReqCard = itemView.findViewById(R.id.emp_att_req_card_view);
            cardEnabled = itemView.findViewById(R.id.card_enabled_layout);
            detailsLay = itemView.findViewById(R.id.att_req_details_layout);

            attReqCard.setOnClickListener(view -> {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Intent intent = new Intent(myContext, ApproveAttendance.class);
                intent.putExtra("LAT",mCategoryItem.get(getAdapterPosition()).getDa_attd_latitude());
                intent.putExtra("LNG",mCategoryItem.get(getAdapterPosition()).getDa_attd_longitude());
                intent.putExtra("EMP_NAME",mCategoryItem.get(getAdapterPosition()).getEmp_name());
                intent.putExtra("DIVM_NAME",mCategoryItem.get(getAdapterPosition()).getDivm_name());
                intent.putExtra("DESIG_NAME",mCategoryItem.get(getAdapterPosition()).getJob_calling_title());
                intent.putExtra("DEPT_NAME",mCategoryItem.get(getAdapterPosition()).getDept_name());
                intent.putExtra("ATT_DATE",mCategoryItem.get(getAdapterPosition()).getAtt_date());
                intent.putExtra("ATT_TIME",mCategoryItem.get(getAdapterPosition()).getAtt_time());
                intent.putExtra("ATT_ADDS",mCategoryItem.get(getAdapterPosition()).getArm_add_during_cause());
                intent.putExtra("ATT_REASON",mCategoryItem.get(getAdapterPosition()).getArm_reason());
                intent.putExtra("ARM_ID",mCategoryItem.get(getAdapterPosition()).getArm_id());
                intent.putExtra("DA_ID",mCategoryItem.get(getAdapterPosition()).getDa_id());
                intent.putExtra("EMP_ID",mCategoryItem.get(getAdapterPosition()).getEmp_id());
                activity.startActivity(intent);
            });
        }
    }
}
