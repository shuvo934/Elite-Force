package ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.dialogue_box;

import static ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.LeaveApprove.forwardHistoryLists;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.adapters.ForwardHistoryAdapter;

public class ForwardHistoryDial extends AppCompatDialogFragment {

    RecyclerView apptRecyclerView;
    ForwardHistoryAdapter forwardHistoryAdapter;
    RecyclerView.LayoutManager apptLayout;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.forward_history_list_view, null);

        apptRecyclerView = view.findViewById(R.id.forward_list_for_leave);
        apptRecyclerView.setHasFixedSize(true);
        apptLayout = new LinearLayoutManager(getContext());
        apptRecyclerView.setLayoutManager(apptLayout);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(apptRecyclerView.getContext(),DividerItemDecoration.VERTICAL);
        apptRecyclerView.addItemDecoration(dividerItemDecoration);
        forwardHistoryAdapter = new ForwardHistoryAdapter(forwardHistoryLists,getContext());
        apptRecyclerView.setAdapter(forwardHistoryAdapter);

        builder.setView(view);

        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "OK", (dialog1, which) -> dialog1.dismiss());

        return dialog;
    }
}
