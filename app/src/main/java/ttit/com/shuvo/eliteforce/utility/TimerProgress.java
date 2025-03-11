package ttit.com.shuvo.eliteforce.utility;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.Objects;

import ticker.views.com.ticker.widgets.circular.timer.callbacks.CircularViewCallback;
import ticker.views.com.ticker.widgets.circular.timer.view.CircularView;
import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.movement_reg.interfaces.TimerFinishedListener;

public class TimerProgress extends AppCompatDialogFragment {

    CircularView circularView;
    public AlertDialog dialog;

    private TimerFinishedListener timerFinishedListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        if (getActivity() instanceof TimerFinishedListener)
            timerFinishedListener = (TimerFinishedListener) getActivity();

        View view = inflater.inflate(R.layout.timer_view, null);
        circularView = view.findViewById(R.id.circular_view);

        CircularView.OptionsBuilder optionsBuilder = new CircularView.OptionsBuilder()
                .shouldDisplayText(true)
                .setCounterInSeconds(5)
                        .setCircularViewCallback(new CircularViewCallback() {
                            @Override
                            public void onTimerFinish() {
                                if (timerFinishedListener!= null)
                                    timerFinishedListener.onTimeFinished();
                                dialog.dismiss();
                            }

                            @Override
                            public void onTimerCancelled() {

                            }
                        });
        circularView.setOptions(optionsBuilder);


        builder.setView(view);
        circularView.startTimer();

        dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        setCancelable(false);
        return dialog;
    }
}
