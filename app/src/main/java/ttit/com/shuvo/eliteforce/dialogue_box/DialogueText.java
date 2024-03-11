package ttit.com.shuvo.eliteforce.dialogue_box;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.att_update.new_request.AttUpNewRequest;
import ttit.com.shuvo.eliteforce.attendance.att_update.req_approve.AttUpReqApprove;
import ttit.com.shuvo.eliteforce.leave.leave_application.approve_application.LeaveApprove;
import ttit.com.shuvo.eliteforce.leave.leave_application.new_application.NewLeaveApplication;

public class DialogueText extends AppCompatDialogFragment {

    private TextInputEditText editText;
    TextInputLayout textLay;
    AppCompatActivity activity;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.textdialogue, null);

        editText = view.findViewById(R.id.dialogue_text_edit);
        textLay = view.findViewById(R.id.dialogue_text_edit_lay);
        activity = (AppCompatActivity) view.getContext();

        builder.setView(view);
        AlertDialog dialog = builder.create();

        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        if (AttUpNewRequest.number == 1) {
            String h = "Write Reason Description:";
            textLay.setHint(h);
            editText.setText(AttUpNewRequest.text);
        }
        else if (AttUpNewRequest.number == 2) {
            String h = "Write Address During Outside of Station:";
            textLay.setHint(h);
            editText.setText(AttUpNewRequest.text);
        }
        else if (NewLeaveApplication.leaveNumber == 1) {
            String h = "Write Your Other Reason:";
            textLay.setHint(h);
            editText.setText(NewLeaveApplication.text);
        }
        else if (NewLeaveApplication.leaveNumber == 2) {
            String h = "Write Address During Leave:";
            textLay.setHint(h);
            editText.setText(NewLeaveApplication.text);
        }
        else if (AttUpReqApprove.number == 1) {
            textLay.setHint(AttUpReqApprove.hint);
            editText.setText(AttUpReqApprove.text);
        }
        else if (LeaveApprove.number == 1) {
            textLay.setHint(LeaveApprove.hintLA);
            editText.setText(LeaveApprove.textLA);
        }


        dialog.setButton(Dialog.BUTTON_POSITIVE, "OK", (dialog1, which) -> {

            String text = Objects.requireNonNull(editText.getText()).toString();
            if (AttUpNewRequest.number == 1) {
                AttUpNewRequest.reasonDesc.setText(text);
                if (text.isEmpty()) {
                    AttUpNewRequest.reasonLay.setHint("Write Reason Description:");
                }
                else {
                    AttUpNewRequest.reasonLay.setHint("Reason Description:");
                }

                AttUpNewRequest.errorReasonDesc.setVisibility(View.GONE);
            }
            else if (AttUpNewRequest.number == 2) {
                if (text.isEmpty()) {
                    AttUpNewRequest.addStaLay.setHint("Write Address During Outside of Station:");
                }
                else {
                    AttUpNewRequest.addStaLay.setHint("Address During Outside of Station:");
                }

                AttUpNewRequest.addressStation.setText(text);
            }
            else if (NewLeaveApplication.leaveNumber == 1) {
                NewLeaveApplication.otherReason.setText(text);
                if (text.isEmpty()) {
                    NewLeaveApplication.otherReasonInputLay.setHint("Write Your Other Reason:");
                }
                else {
                    NewLeaveApplication.otherReasonInputLay.setHint("Other Reason:");
                }

                NewLeaveApplication.errorReason.setVisibility(View.GONE);
            }
            else if (NewLeaveApplication.leaveNumber == 2) {
                if (text.isEmpty()) {
                    NewLeaveApplication.leaveAddressLay.setHint("Write Address During Leave:");
                }
                else {
                    NewLeaveApplication.leaveAddressLay.setHint("Address During Leave:");
                }
                NewLeaveApplication.leaveAddress.setText(text);
            }
            else if (AttUpReqApprove.number == 1) {
                AttUpReqApprove.comments.setText(text);
                if (text.isEmpty()) {
                    AttUpReqApprove.commentsLay.setHint("Write Approve/Reject Comments:");
                }
                else {
                    AttUpReqApprove.commentsLay.setHint("Comments:");
                }
            }
            else if (LeaveApprove.number == 1) {
                LeaveApprove.comments.setText(text);
                if (text.isEmpty()) {
                    LeaveApprove.commentsLay.setHint("Write Approve/Reject Comments:");
                }
                else {
                    LeaveApprove.commentsLay.setHint("Comments:");
                }
            }
            AttUpNewRequest.number = 0;
            NewLeaveApplication.leaveNumber = 0;
            AttUpReqApprove.number = 0;
            LeaveApprove.number = 0;

            dialog1.dismiss();
        });

        dialog.setButton(Dialog.BUTTON_NEGATIVE, "CANCEL", (dialog12, which) -> {
            AttUpNewRequest.number = 0;
            NewLeaveApplication.leaveNumber = 0;
            AttUpReqApprove.number = 0;
            LeaveApprove.number = 0;
            dialog12.dismiss();
        });

        return dialog;
    }
}
