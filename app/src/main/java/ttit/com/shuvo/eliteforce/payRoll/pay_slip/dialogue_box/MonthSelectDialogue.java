package ttit.com.shuvo.eliteforce.payRoll.pay_slip.dialogue_box;

import static ttit.com.shuvo.eliteforce.payRoll.pay_slip.PaySlip.errorMsgMonth;
import static ttit.com.shuvo.eliteforce.payRoll.pay_slip.PaySlip.selectMonth;
import static ttit.com.shuvo.eliteforce.payRoll.pay_slip.PaySlip.selectMonthLay;
import static ttit.com.shuvo.eliteforce.payRoll.pay_slip.PaySlip.select_month_id;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.payRoll.pay_slip.adapters.MonthSelectAdapter;
import ttit.com.shuvo.eliteforce.payRoll.pay_slip.model.MonthSelectList;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class MonthSelectDialogue extends AppCompatDialogFragment implements MonthSelectAdapter.ClickedItem{
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    MonthSelectAdapter monthSelectAdapter;

    TextInputEditText search;
    AlertDialog dialog;
    TextView noMonthMsg;

    Boolean isfiltered = false;
    ArrayList<MonthSelectList> filteredList = new ArrayList<>();
    private ArrayList<MonthSelectList> lists = new ArrayList<>();


    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    AppCompatActivity activity;

    Context mContext;

    public MonthSelectDialogue(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.month_select_dialogue, null);

        activity = (AppCompatActivity) view.getContext();
        recyclerView = view.findViewById(R.id.month_list_of_item);
        noMonthMsg = view.findViewById(R.id.no_month_found_msg);
        noMonthMsg.setVisibility(View.GONE);

        search = view.findViewById(R.id.month_search);
        lists = new ArrayList<>();

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        getMonths();

        builder.setView(view);
        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);


        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> dialog.dismiss());

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        return dialog;
    }

    private void filter(String text) {
        filteredList = new ArrayList<>();
        for (MonthSelectList item : lists) {
            if (item.getMonthName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        monthSelectAdapter.filterList(filteredList);
    }


    @Override
    public void onCategoryClicked(int CategoryPosition) {

        String name;
        String id;
        if (isfiltered) {
            name = filteredList.get(CategoryPosition).getMonthName();
            id = filteredList.get(CategoryPosition).getMonthId();
        }
        else {
            name = lists.get(CategoryPosition).getMonthName();
            id = lists.get(CategoryPosition).getMonthId();
        }

        System.out.println(name);
        System.out.println(id);

        select_month_id = id;
        errorMsgMonth.setVisibility(View.GONE);
        selectMonthLay.setHint("Month:");
        selectMonth.setText(name);
        dialog.dismiss();
    }

    public void getMonths() {
        waitProgress.show(activity.getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;
        lists = new ArrayList<>();

        String url = api_url_front+"paySlip/getPayMonths";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject monthInfo = array.getJSONObject(i);

                        String month_name = monthInfo.getString("month_name")
                                .equals("null") ? "" : monthInfo.getString("month_name");
                        String month_id = monthInfo.getString("month_id")
                                .equals("null") ? "" : monthInfo.getString("month_id");
                        String month_start = monthInfo.getString("month_start")
                                .equals("null") ? "" : monthInfo.getString("month_start");
                        String month_end = monthInfo.getString("month_end")
                                .equals("null") ? "" : monthInfo.getString("month_end");

                        lists.add(new MonthSelectList(month_name,month_id,month_start,month_end));
                    }
                }
                connected = true;
                updateDial();
            }
            catch (JSONException e) {
                e.printStackTrace();
                connected = false;
                updateDial();
            }
        }, error -> {
            error.printStackTrace();
            conn = false;
            connected = false;
            updateDial();
        });

        requestQueue.add(stringRequest);
    }

    private void updateDial() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                if (lists.size() == 0) {
                    noMonthMsg.setVisibility(View.VISIBLE);
                }
                else {
                    noMonthMsg.setVisibility(View.GONE);
                }
                monthSelectAdapter = new MonthSelectAdapter(lists,getContext(), MonthSelectDialogue.this);
                recyclerView.setAdapter(monthSelectAdapter);
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(mContext)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getMonths();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    ((Activity)mContext).finish();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(mContext)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getMonths();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                ((Activity)mContext).finish();
            });
        }
    }
}
