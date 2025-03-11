package ttit.com.shuvo.eliteforce.check_in_out.fragments;

import static java.nio.charset.StandardCharsets.ISO_8859_1;
import static java.nio.charset.StandardCharsets.UTF_8;
import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.check_in_out.adapters.OngoingCheckAdapter;
import ttit.com.shuvo.eliteforce.check_in_out.addCheck.AddCheckIn;
import ttit.com.shuvo.eliteforce.check_in_out.arraylists.OnGoingCheckList;
import ttit.com.shuvo.eliteforce.employeeInfo.transcript.fragments.model.FirstPageAnotherData;
import ttit.com.shuvo.eliteforce.employeeInfo.transcript.fragments.model.FirstPageData;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class CheckOngoingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;

    RecyclerView ongoingListView;
    RecyclerView.LayoutManager layoutManager;
    OngoingCheckAdapter ongoingCheckAdapter;

    ArrayList<OnGoingCheckList> onGoingCheckLists;

    FloatingActionButton addCheck;

    String emp_id;

    public CheckOngoingFragment() {
        // Required empty public constructor
    }

    Context mContext;

    public CheckOngoingFragment(Context context) {
        this.mContext = context;
    }

    public static CheckOngoingFragment newInstance(String param1, String param2) {
        CheckOngoingFragment fragment = new CheckOngoingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_ongoing, container, false);

        ongoingListView = view.findViewById(R.id.ongoing_check_in_list);
        addCheck = view.findViewById(R.id.add_check_in);

        onGoingCheckLists = new ArrayList<>();

        ongoingListView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        ongoingListView.setLayoutManager(layoutManager);

        emp_id = userInfoLists.get(0).getEmp_id();

        addCheck.setOnClickListener(view1 -> {
            Intent intent = new Intent(mContext, AddCheckIn.class);
            startActivity(intent);
        });

        getListData();
        return view;
    }

    public void getListData() {
        waitProgress.show(requireActivity().getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);

        conn = false;
        connected = false;
        onGoingCheckLists = new ArrayList<>();

        String url = api_url_front+"checkInOut/getCheckInList?p_emp_id="+emp_id;

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest request = new StringRequest(Request.Method.GET, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empTranscriptFirst_1 = array.getJSONObject(i);

                        String cior_id = empTranscriptFirst_1.getString("cior_id")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_id");

                        String cior_register_no = empTranscriptFirst_1.getString("cior_register_no")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_register_no");

                        String cior_company_info = empTranscriptFirst_1.getString("cior_company_info")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_company_info");

                        String check_date = empTranscriptFirst_1.getString("check_date")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("check_date");

                        String cior_company_loc_info = empTranscriptFirst_1.getString("cior_company_loc_info")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_company_loc_info");
                        cior_company_loc_info = transformText(cior_company_loc_info);

                        String cior_lat_val = empTranscriptFirst_1.getString("cior_lat_val")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_lat_val");

                        String cior_long_val = empTranscriptFirst_1.getString("cior_long_val")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_long_val");

                        String in_time = empTranscriptFirst_1.getString("in_time")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("in_time");

                        String cior_in_remarks = empTranscriptFirst_1.getString("cior_in_remarks")
                                .equals("null") ? "" : empTranscriptFirst_1.getString("cior_in_remarks");

                        String cior_blob = empTranscriptFirst_1.optString("cior_blob");

                        Bitmap bitmap = null;
                        if (!cior_blob.equals("null") && !cior_blob.isEmpty()) {
                            byte[] decodedString = Base64.decode(cior_blob,Base64.DEFAULT);
                            bitmap = BitmapFactory.decodeByteArray(decodedString,0,decodedString.length);
                        }

                        onGoingCheckLists.add(new OnGoingCheckList(cior_id,cior_register_no,cior_company_info,
                                check_date,cior_company_loc_info,cior_lat_val,cior_long_val,in_time,cior_in_remarks,bitmap));
                    }
                }
                connected = true;
                updateLayout();
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                updateLayout();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            updateLayout();
        });


        requestQueue.add(request);
    }

    private void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {

                ongoingCheckAdapter = new OngoingCheckAdapter(onGoingCheckLists, mContext);
                ongoingListView.setAdapter(ongoingCheckAdapter);

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getListData();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    ((Activity)mContext).finish();
                    dialog.dismiss();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(getContext())
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getListData();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                ((Activity)mContext).finish();
                dialog.dismiss();
            });
        }
    }

    //    --------------------------Transforming Bangla Text-----------------------------
    private String transformText(String text) {
        byte[] bytes = text.getBytes(ISO_8859_1);
        return new String(bytes, UTF_8);
    }
}