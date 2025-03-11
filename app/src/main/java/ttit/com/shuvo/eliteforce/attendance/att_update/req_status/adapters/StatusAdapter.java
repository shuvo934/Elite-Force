package ttit.com.shuvo.eliteforce.attendance.att_update.req_status.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_update.req_status.statusDetails.AttUpdateStatusDetails;
import ttit.com.shuvo.eliteforce.basic_model.StatusList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusView>{

    public ArrayList<StatusList> statusLists;

    public Context myContext;

    String reqNo = "";
    String att_Status = "";
    String app_Date = "";
    String update_Date = "";
    String arrTime = "";
    String depTime = "";
    String approverrrrr = "";

    public StatusAdapter(ArrayList<StatusList> statusLists, Context myContext) {
        this.statusLists = statusLists;
        this.myContext = myContext;
    }

    @NonNull
    @Override
    public StatusView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.status_item_list, parent, false);
        return new StatusView(v);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusView holder, int position) {

        StatusList statusList = statusLists.get(position);

        holder.req_no.setText(statusList.getApp_code());
        String approve = statusList.getApprover();
        String stat = statusList.getApproved();

        switch (stat) {
            case "0":
                stat = "PENDING";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#636e72"));
                holder.appLay.setVisibility(View.GONE);
                holder.approver.setText("");
                break;
            case "1":
                stat = "APPROVED";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#1abc9c"));
                holder.approver.setText(approve);
                holder.appLay.setVisibility(View.VISIBLE);
                String aText = "Approved By:";
                holder.appRej.setText(aText);
                break;
            case "2":
                stat = "REJECTED";
                holder.cardView.setCardBackgroundColor(Color.parseColor("#c0392b"));
                holder.approver.setText(approve);
                holder.appLay.setVisibility(View.VISIBLE);
                String rText = "Rejected By:";
                holder.appRej.setText(rText);
                break;
        }

        holder.status.setText(stat);
        holder.reqDate.setText(statusList.getReq_date());
        holder.reqType.setText(statusList.getReq_type());
        holder.upDate.setText(statusList.getUp_date());
        holder.arr.setText(statusList.getArr_time());
        holder.dept.setText(statusList.getDep_time());

        if (statusList.getArr_time().isEmpty()) {
            holder.inLay.setVisibility(View.GONE);
        }
        else {
            holder.inLay.setVisibility(View.VISIBLE);
        }

        if (statusList.getDep_time().isEmpty()) {
            holder.outLay.setVisibility(View.GONE);
        }
        else {
            holder.outLay.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return statusLists.size();
    }

    public class StatusView extends RecyclerView.ViewHolder {

        TextView req_no;
        TextView status;
        TextView reqDate;
        TextView reqType;
        TextView upDate;
        TextView arr;
        TextView dept;
        TextView approver;
        TextView appRej;
        LinearLayout appLay;
        CardView cardView;
        Button details;
        LinearLayout inLay;
        LinearLayout outLay;

        public StatusView(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.status_card);
            req_no = itemView.findViewById(R.id.request_code_status);
            status = itemView.findViewById(R.id.status_status);
            reqDate = itemView.findViewById(R.id.request_date_status);
            reqType = itemView.findViewById(R.id.request_type_status);
            upDate = itemView.findViewById(R.id.request_update_date_status);
            arr = itemView.findViewById(R.id.arrival_time_status);
            dept = itemView.findViewById(R.id.departure_time_status);
            approver = itemView.findViewById(R.id.approver_name_by);
            appRej = itemView.findViewById(R.id.text_app_rej);
            inLay = itemView.findViewById(R.id.req_status_in_time_lay);
            outLay = itemView.findViewById(R.id.req_status_out_time_lay);

            appLay = itemView.findViewById(R.id.approver_layout);
            details = itemView.findViewById(R.id.button_status_all);

            details.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                reqNo = req_no.getText().toString();
                att_Status = status.getText().toString();
                app_Date = reqDate.getText().toString();
                update_Date = upDate.getText().toString();
                arrTime = arr.getText().toString();
                depTime = dept.getText().toString();

                approverrrrr = approver.getText().toString();

                Intent intent = new Intent(myContext, AttUpdateStatusDetails.class);
                intent.putExtra("Request", reqNo);
                intent.putExtra("Status", att_Status);
                intent.putExtra("APP_DATE", app_Date);
                intent.putExtra("UPDATE_DATE", update_Date);
                intent.putExtra("ARRIVAL", arrTime);
                intent.putExtra("DEPARTURE", depTime);
                intent.putExtra("APPROVER",approverrrrr);
                activity.startActivity(intent);
            });
        }
    }
}
