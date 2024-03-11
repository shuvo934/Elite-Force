package ttit.com.shuvo.eliteforce.scheduler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class Uploader extends BroadcastReceiver {
    public static String channelId = "File Uploader Notification";
    String emp_id = "";
    SharedPreferences sharedPreferences;
    public static final String SCHEDULING_FILE = "SCHEDULING FILE_EF_HR";
    public static final String SCHEDULING_EMP_ID = "SCHEDULING EMP ID";
    @Override
    public void onReceive(Context context, Intent intent) {
        sharedPreferences = context.getSharedPreferences(SCHEDULING_FILE, Context.MODE_PRIVATE);
        emp_id = sharedPreferences.getString(SCHEDULING_EMP_ID,null);

        Data data = new Data.Builder()
                .putString(MyWorker.TASK_DESC_EMP_ID, emp_id)
                .build();

        //This is the subclass of our WorkRequest
        final OneTimeWorkRequest workRequest = new OneTimeWorkRequest.Builder(MyWorker.class)
                .setInputData(data)
                .build();

        WorkManager.getInstance().enqueue(workRequest);
    }
}
