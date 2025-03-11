package ttit.com.shuvo.eliteforce.fleet.assignment.dialogs;


import static ttit.com.shuvo.eliteforce.fleet.assignment.RequisitionAssignment.request_date;
import static ttit.com.shuvo.eliteforce.fleet.assignment.RequisitionAssignment.requester_name;
import static ttit.com.shuvo.eliteforce.fleet.assignment.RequisitionAssignment.requisition_id;
import static ttit.com.shuvo.eliteforce.fleet.assignment.RequisitionAssignment.token_no;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.fleet.assignment.adapters.RequisitionApproveAdapter;
import ttit.com.shuvo.eliteforce.fleet.assignment.arraylists.RequisitionToApproveList;
import ttit.com.shuvo.eliteforce.fleet.assignment.interfaces.ReqSelectListener;

public class SelectRequisitionDialog extends AppCompatDialogFragment implements RequisitionApproveAdapter.ClickedItem {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RequisitionApproveAdapter requisitionApproveAdapter;

    TextInputEditText search;

    AlertDialog dialog;

    Boolean isfiltered = false;
    ArrayList<RequisitionToApproveList> filteredList;
    ArrayList<RequisitionToApproveList> selectedReqList;

    AppCompatActivity activity;
    View view;

    Context mContext;

    private ReqSelectListener reqSelectListener;

    public SelectRequisitionDialog(ArrayList<RequisitionToApproveList> selectedReqList, Context mContext) {
        this.selectedReqList = selectedReqList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        if (getActivity() instanceof ReqSelectListener)
            reqSelectListener = (ReqSelectListener) getActivity();

        view = inflater.inflate(R.layout.req_list_for_selected_dialog_view, null);
        activity = (AppCompatActivity) view.getContext();
        recyclerView = view.findViewById(R.id.requisition_list_for_assignment);

        search = view.findViewById(R.id.search_by_token);

        filteredList = new ArrayList<>();

        search.setImeOptions(EditorInfo.IME_ACTION_DONE);

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        requisitionApproveAdapter = new RequisitionApproveAdapter(this, mContext,selectedReqList);
        recyclerView.setAdapter(requisitionApproveAdapter);

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", (dialog, which) -> {
            dialog.dismiss();
        });

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

        search.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    search.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        return dialog;
    }

    private void closeKeyBoard() {
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void filter(String text) {
        filteredList = new ArrayList<>();
        for (RequisitionToApproveList item : selectedReqList) {
            if (item.getFr_code().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        requisitionApproveAdapter.filterList(filteredList);
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        String name;
        String id;
        String req_name;
        String req_date;
        if (isfiltered) {
            name = filteredList.get(CategoryPosition).getFr_code();
            id = filteredList.get(CategoryPosition).getFr_pk();
            req_name = filteredList.get(CategoryPosition).getEmp_name();
            req_date = filteredList.get(CategoryPosition).getReq_date();
        }
        else {
            name = selectedReqList.get(CategoryPosition).getFr_code();
            id = selectedReqList.get(CategoryPosition).getFr_pk();
            req_name = selectedReqList.get(CategoryPosition).getEmp_name();
            req_date = selectedReqList.get(CategoryPosition).getReq_date();
        }

        token_no = name;
        requisition_id = id;
        requester_name = req_name;
        request_date = req_date;

        if(reqSelectListener != null)
            reqSelectListener.onSelected();

        dialog.dismiss();
    }
}
