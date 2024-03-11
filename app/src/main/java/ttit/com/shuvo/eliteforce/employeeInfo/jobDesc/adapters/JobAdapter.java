package ttit.com.shuvo.eliteforce.employeeInfo.jobDesc.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.employeeInfo.jobDesc.model.JobDescDetails;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.JobHolder> {
    public ArrayList<JobDescDetails> jobDescDetails;
    public Context myContext;

    public JobAdapter(ArrayList<JobDescDetails> jobDescDetails, Context myContext) {
        this.jobDescDetails = jobDescDetails;
        this.myContext = myContext;
    }


    @NonNull
    @Override
    public JobHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.job_details_view, parent, false);
        return new JobHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull JobHolder holder, int position) {

        JobDescDetails jobdddd = jobDescDetails.get(position);

        holder.jobDetails.setText(jobdddd.getJob_desc());
        holder.item.setText(jobdddd.getItem_no());

    }

    @Override
    public int getItemCount() {
        return jobDescDetails.size();
    }

    public static class JobHolder extends RecyclerView.ViewHolder {

        TextView item;
        TextView jobDetails;

        public JobHolder(@NonNull View itemView) {
            super(itemView);

            item = itemView.findViewById(R.id.item_noo);
            jobDetails = itemView.findViewById(R.id.job_details_desc);

        }
    }
}
