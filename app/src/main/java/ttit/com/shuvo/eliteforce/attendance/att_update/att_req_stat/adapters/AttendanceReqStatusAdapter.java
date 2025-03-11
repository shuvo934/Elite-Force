package ttit.com.shuvo.eliteforce.attendance.att_update.att_req_stat.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_update.att_req_stat.arraylists.AttendanceReqStatusList;

public class AttendanceReqStatusAdapter extends RecyclerView.Adapter<AttendanceReqStatusAdapter.ARSAHolder> {

    private final ArrayList<AttendanceReqStatusList> statusLists;
    private final Context myContext;

    public AttendanceReqStatusAdapter(ArrayList<AttendanceReqStatusList> statusLists, Context myContext) {
        this.statusLists = statusLists;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public ARSAHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.attendance_request_status_details_layout, parent, false);
        return new ARSAHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ARSAHolder holder, int position) {
        AttendanceReqStatusList statusList = statusLists.get(position);

        holder.req_no.setText(statusList.getArm_id());
        holder.attDate.setText(statusList.getAtt_date());
        String at = statusList.getAtt_time() + " ("+statusList.getTime_type()+")";
        holder.attTime.setText(at);
        holder.attAddress.setText(statusList.getArm_add_during_cause());
        holder.attReason.setText(statusList.getArm_reason());

        String approve = statusList.getApprover_name();
        String stat = statusList.getArm_approved();

        switch (stat) {
            case "0":
                stat = "PENDING";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#636e72"));
                holder.appLay.setVisibility(View.GONE);
                holder.approver.setText("");
                holder.apprDesig.setText("");
                holder.commLay.setVisibility(View.GONE);
                holder.comments.setText("");
                break;
            case "1":
                stat = "APPROVED";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#1abc9c"));
                holder.approver.setText(approve);
                holder.apprDesig.setText(statusList.getDesignation());
                holder.appLay.setVisibility(View.VISIBLE);
                String aText = "Approved By:";
                holder.appRej.setText(aText);
                if (statusList.getArm_comments().isEmpty()) {
                    holder.commLay.setVisibility(View.GONE);
                    holder.comments.setText("");
                }
                else {
                    holder.commLay.setVisibility(View.VISIBLE);
                    holder.comments.setText(statusList.getArm_comments());
                }
                break;
            case "2":
                stat = "REJECTED";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#c0392b"));
                holder.approver.setText(approve);
                holder.apprDesig.setText(statusList.getDesignation());
                holder.appLay.setVisibility(View.VISIBLE);
                String rText = "Rejected By:";
                holder.appRej.setText(rText);
                if (statusList.getArm_comments().isEmpty()) {
                    holder.commLay.setVisibility(View.GONE);
                    holder.comments.setText("");
                }
                else {
                    holder.commLay.setVisibility(View.VISIBLE);
                    holder.comments.setText(statusList.getArm_comments());
                }
                break;
        }

        holder.status.setText(stat);
    }

    @Override
    public int getItemCount() {
        return statusLists != null ? statusLists.size() : 0;
    }

    public static class ARSAHolder extends RecyclerView.ViewHolder {

        TextView req_no;
        CardView cardView;
        TextView status;
        TextView attDate;
        TextView attTime;
        TextView attAddress;
        TextView attReason;
        LinearLayout appLay;
        TextView appRej;
        TextView approver;
        TextView apprDesig;
        LinearLayout commLay;
        TextView comments;

        public ARSAHolder(@NonNull View itemView) {
            super(itemView);

            req_no = itemView.findViewById(R.id.att_request_no_status);
            cardView = itemView.findViewById(R.id.att_req_status_card);
            status = itemView.findViewById(R.id.att_req_status_status);
            attDate = itemView.findViewById(R.id.att_request_date_status);
            attTime = itemView.findViewById(R.id.attendance_req_time_with_type_status);
            attAddress = itemView.findViewById(R.id.att_req_location_status);
            attReason = itemView.findViewById(R.id.att_req_reason_status);

            appLay = itemView.findViewById(R.id.att_req_approver_layout);
            appRej = itemView.findViewById(R.id.att_req_text_app_rej);
            approver = itemView.findViewById(R.id.att_req_approver_name_by);
            apprDesig = itemView.findViewById(R.id.att_req_approver_designation);

            commLay = itemView.findViewById(R.id.att_req_comment_status_lay);
            comments = itemView.findViewById(R.id.att_req_comments_status);
        }
    }
}
