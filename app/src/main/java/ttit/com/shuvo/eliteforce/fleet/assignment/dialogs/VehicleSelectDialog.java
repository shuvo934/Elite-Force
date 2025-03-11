package ttit.com.shuvo.eliteforce.fleet.assignment.dialogs;

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
import ttit.com.shuvo.eliteforce.fleet.assignment.adapters.VehicleSelectAdapter;
import ttit.com.shuvo.eliteforce.fleet.assignment.arraylists.VehicleList;
import ttit.com.shuvo.eliteforce.fleet.assignment.interfaces.VhSelectListener;

public class VehicleSelectDialog extends AppCompatDialogFragment implements VehicleSelectAdapter.ClickedItem {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    VehicleSelectAdapter vehicleSelectAdapter;
    TextInputEditText search;

    AlertDialog dialog;

    Boolean isfiltered = false;
    ArrayList<VehicleList> filteredList = new ArrayList<>();
    ArrayList<VehicleList> selectedReqList;

    AppCompatActivity activity;
    View view;
    Context mContext;

    private VhSelectListener vhSelectListener;

    public VehicleSelectDialog(ArrayList<VehicleList> selectedReqList, Context mContext) {
        this.selectedReqList = selectedReqList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        if (getActivity() instanceof VhSelectListener)
            vhSelectListener = (VhSelectListener) getActivity();

        view = inflater.inflate(R.layout.vehicle_selection_dialog_view, null);
        activity = (AppCompatActivity) view.getContext();
        recyclerView = view.findViewById(R.id.vehicle_list_for_selection);

        search = view.findViewById(R.id.search_by_reg_no);

        filteredList = new ArrayList<>();

        search.setImeOptions(EditorInfo.IME_ACTION_DONE);

        builder.setView(view);

        dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        vehicleSelectAdapter = new VehicleSelectAdapter(this, mContext,selectedReqList);
        recyclerView.setAdapter(vehicleSelectAdapter);

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
        for (VehicleList item : selectedReqList) {
            if (item.getReg_no().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add((item));
                isfiltered = true;
            }
        }
        vehicleSelectAdapter.filterList(filteredList);
    }

    @Override
    public void onCategoryClicked(int CategoryPosition) {
        String name;
        String id;
        String rg_no;
        String di_id_new;
        String di_name;
        String di_emp_id;
        if (isfiltered) {
            name = filteredList.get(CategoryPosition).getName();
            id = filteredList.get(CategoryPosition).getVi_id();
            rg_no = filteredList.get(CategoryPosition).getReg_no();
            di_id_new = filteredList.get(CategoryPosition).getDi_id();
            di_name = filteredList.get(CategoryPosition).getDi_name();
            di_emp_id = filteredList.get(CategoryPosition).getDi_emp_id();
        }
        else {
            name = selectedReqList.get(CategoryPosition).getName();
            id = selectedReqList.get(CategoryPosition).getVi_id();
            rg_no = selectedReqList.get(CategoryPosition).getReg_no();
            di_id_new = selectedReqList.get(CategoryPosition).getDi_id();
            di_name = selectedReqList.get(CategoryPosition).getDi_name();
            di_emp_id = selectedReqList.get(CategoryPosition).getDi_emp_id();
        }

        if(vhSelectListener != null)
            vhSelectListener.onVehicleSelect(name, id, rg_no, di_id_new, di_name, di_emp_id);

        dialog.dismiss();
    }
}
