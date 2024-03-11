package ttit.com.shuvo.eliteforce.directory.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.directory.Directory;
import ttit.com.shuvo.eliteforce.directory.model.DirectoryList;
import ttit.com.shuvo.eliteforce.directory.model.PhoneList;

public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryHolder>{

    public ArrayList<DirectoryList> directoryLists;
    public Context myContext;
    public static PhoneAdapter phoneAdapter;

    public DirectoryAdapter(Context context, ArrayList<DirectoryList> directoryLists) {
        this.myContext = context;
        this.directoryLists = directoryLists;
    }

    @NonNull
    @Override
    public DirectoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(myContext).inflate(R.layout.directory_item, parent, false);
        return new DirectoryHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectoryHolder holder, int position) {
        DirectoryList itemList = directoryLists.get(position);

        holder.empName.setText(itemList.getEmp_name());
        holder.div.setText(itemList.getDiv_name());
        holder.dep.setText(itemList.getDep_name());
        holder.des.setText(itemList.getDes_name());
        String mail = itemList.getEmail_name();
        if (mail == null) {
            String text = "No Email Found";
            holder.mailName.setText(text);
            holder.mailClick.setVisibility(View.GONE);
        } else {
            holder.mailName.setText(itemList.getEmail_name());
            holder.mailClick.setVisibility(View.VISIBLE);
        }

        String id = itemList.getEmp_id();

        ArrayList<PhoneList> newArray = new ArrayList<>();

        String no = itemList.getNo();

        if (no.equals("2")) {
            if(Directory.allPhoneLists.size() != 0) {
                for (int i = 0; i < Directory.allPhoneLists.size(); i++) {
                    if (id.equals(Directory.allPhoneLists.get(i).getP_emp_id())) {
                        newArray.add(new PhoneList(Directory.allPhoneLists.get(i).getP_emp_id(), Directory.allPhoneLists.get(i).getPhone()));
                    }
                }
            }
        }

        if (newArray.size() == 0) {
            if (itemList.getContact_no() != null) {
                if (!itemList.getContact_no().isEmpty()) {
                    newArray.add(new PhoneList(id, itemList.getContact_no()));
                }
            }
        }

        if (newArray.size() == 0) {
            System.out.println("FAKA ARRAY");
            holder.numberNot.setVisibility(View.VISIBLE);
            holder.phoneView.setVisibility(View.GONE);
        }
        else {
            System.out.println("KISU PAISE");
            holder.numberNot.setVisibility(View.GONE);
            holder.phoneView.setVisibility(View.VISIBLE);
            holder.phoneView.setHasFixedSize(true);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(myContext);
            holder.phoneView.setLayoutManager(layoutManager);
            phoneAdapter = new PhoneAdapter(myContext, newArray);
            holder.phoneView.setAdapter(phoneAdapter);
        }
    }

    @Override
    public int getItemCount() {
        return directoryLists.size();
    }

    public static class DirectoryHolder extends RecyclerView.ViewHolder {

        TextView empName;
        TextView div;
        TextView dep;
        TextView des;
        TextView mailName;
        TextView numberNot;
        RecyclerView phoneView;
        ImageView mailClick;

        public DirectoryHolder(@NonNull View itemView) {
            super(itemView);

            empName = itemView.findViewById(R.id.directory_emp_name);
            div = itemView.findViewById(R.id.directory_division);
            dep = itemView.findViewById(R.id.directory_department);
            des = itemView.findViewById(R.id.directory_designation);
            mailName = itemView.findViewById(R.id.directory_mail);
            phoneView = itemView.findViewById(R.id.phone_number_list_view);
            mailClick = itemView.findViewById(R.id.mail_click);
            numberNot = itemView.findViewById(R.id.phone_number_not_found);

            mailClick.setOnClickListener(v -> {
                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                String mmm = mailName.getText().toString();

                AlertDialog dialog = new AlertDialog.Builder(activity)
                        .setMessage("Do you want to send an email to "+mmm+" ?")
                        .setPositiveButton("Yes", null)
                        .setNegativeButton("No",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v1 -> {

                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri data = Uri.parse("mailto:"+mmm);
                    intent.setData(data);
                    try {
                        activity.startActivity(intent);
                    }
                    catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(activity, "There is no email app found.", Toast.LENGTH_SHORT).show();
                    }
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v12 -> dialog.dismiss());
            });
        }
    }

    public void filterList(ArrayList<DirectoryList> filteredList) {
        directoryLists = filteredList;
        notifyDataSetChanged();
    }
}
