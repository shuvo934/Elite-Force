package ttit.com.shuvo.eliteforce.scheduler;

import static android.content.Context.MODE_PRIVATE;

import static ttit.com.shuvo.eliteforce.scheduler.Uploader.channelId;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ttit.com.shuvo.eliteforce.R;

public class MyWorker extends Worker {
    private Boolean conn = false;
    private Boolean connected = false;

    String emp_id = "";
    String emp_code = "";
    int tracking_flag = 0;
    int dateCount = 0;
    int retryNo = 0;
    boolean updated = false;

    ArrayList<String> lastTenDays;
    ArrayList<String> lastTenDaysFromSQL;
    ArrayList<String> updatedFiles;
    ArrayList<String> updatedXml;
    Context mContext;

    SharedPreferences sharedPreferencesDA;
    public static String FILE_OF_DAILY_ACTIVITY = "";
    public static  String DISTANCE = "DISTANCE";
    public static  String TOTAL_TIME = "TOTAL_TIME";
    public static  String STOPPED_TIME = "STOPPED_TIME";


    public static final String TASK_DESC_EMP_ID = "task_desc";

    public MyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        mContext = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        emp_id = getInputData().getString(TASK_DESC_EMP_ID);
        UploadingFile();
        return Result.success();
    }

    private void UploadingFile() {

        lastTenDays = new ArrayList<>();
        lastTenDaysFromSQL = new ArrayList<>();
        updatedFiles = new ArrayList<>();
        updatedXml = new ArrayList<>();
        updated = false;

        Calendar cal = GregorianCalendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -11);

        for (int i = 0 ; i < 10 ;i ++) {
            cal.add(Calendar.DAY_OF_YEAR, +1);
            Date calTime = cal.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);
            String ddd = simpleDateFormat.format(calTime);

            ddd = ddd.toUpperCase();
            System.out.println(ddd);
            lastTenDays.add(ddd);
        }

        getEmpData();
    }

    public void getEmpData() {
        final int[] job_id = {0};
        final String[] coa_id = {""};
        final String[] divm_id = {""};
        final String[] dept_id = {""};
        conn = false;
        connected = false;

        String empDataUrl = "http://103.56.208.123:8001/apex/elite_force/attendance/getEmpData/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest empDataReq = new StringRequest(Request.Method.GET, empDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject empDataInfo = array.getJSONObject(i);

                        tracking_flag = Integer.parseInt(empDataInfo.getString("emp_timeline_tracker_flag")
                                .equals("null") ? "0" : empDataInfo.getString("emp_timeline_tracker_flag"));
                        job_id[0] = Integer.parseInt(empDataInfo.getString("emp_job_id")
                                .equals("null") ? "0" : empDataInfo.getString("emp_job_id"));
                        coa_id[0] = empDataInfo.getString("job_pri_coa_id")
                                .equals("null") ? "" : empDataInfo.getString("job_pri_coa_id");
                        divm_id[0] = empDataInfo.getString("jsm_divm_id")
                                .equals("null") ? "" : empDataInfo.getString("jsm_divm_id");
                        dept_id[0] = empDataInfo.getString("jsm_dept_id")
                                .equals("null") ? "" : empDataInfo.getString("jsm_dept_id");
                        emp_code = empDataInfo.getString("emp_code")
                                .equals("null") ? "" : empDataInfo.getString("emp_code");
                    }
                    if (tracking_flag == 1) {
                        getTrackerUploadDate(job_id[0],coa_id[0],divm_id[0],dept_id[0]);
                    }
                    else {
                        connected = true;
                        notifyUser();
                    }
                }
                else {
                    connected = false;
                    notifyUser();
                }
            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                notifyUser();
            }
        }, error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            notifyUser();
        });

        requestQueue.add(empDataReq);
    }

    public void getTrackerUploadDate(int job_id, String coa_id, String divm_id, String dept_id) {
        lastTenDaysFromSQL = new ArrayList<>();
        conn = false;
        connected = false;

        String trackerDataUrl = "http://103.56.208.123:8001/apex/elite_force/attendance/getTrackUploadedDate/"+emp_id+"";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest trackerDataReq = new StringRequest(Request.Method.GET, trackerDataUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject trackerDataInfo = array.getJSONObject(i);

                        String elr_date = trackerDataInfo.getString("elr_date")
                                .equals("null") ? "" : trackerDataInfo.getString("elr_date");
                        lastTenDaysFromSQL.add(elr_date);
                    }
                }
                dateCount = 0;
                getTrackerDate(job_id,coa_id,divm_id,dept_id);

            }
            catch (JSONException e) {
                connected = false;
                e.printStackTrace();
                notifyUser();
            }
        },error -> {
            conn = false;
            connected = false;
            error.printStackTrace();
            notifyUser();
        });

        requestQueue.add(trackerDataReq);
    }

    public void getTrackerDate(int job_id, String coa_id, String divm_id, String dept_id) {
        boolean noDatetoUp = true;
        for (int i = 0; i < lastTenDays.size(); i++) {
            boolean dateFound = false;
            String date = lastTenDays.get(i);

            for (int j = 0; j < lastTenDaysFromSQL.size(); j++) {
                if (date.equals(lastTenDaysFromSQL.get(j))) {
                    dateFound = true;
                    System.out.println(date);
                }
            }
            if (!dateFound) {
                noDatetoUp = false;
                String fileName = emp_id+"_"+date+"_track";

                FILE_OF_DAILY_ACTIVITY = fileName;

                sharedPreferencesDA = mContext.getSharedPreferences(FILE_OF_DAILY_ACTIVITY, MODE_PRIVATE);

                String dist = sharedPreferencesDA.getString(DISTANCE,null);
                String totalTime = sharedPreferencesDA.getString(TOTAL_TIME,null);
                String stoppedTime = sharedPreferencesDA.getString(STOPPED_TIME,null);

                String stringFIle = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +  fileName +".gpx";

                File blobFile = new File(stringFIle);

                updatedXml.add(fileName);
                byte[] bytes = null;
                if (blobFile.exists()) {
                    dateCount++;
                    try {
                        bytes = method(blobFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updatedFiles.add(stringFIle);
                }
                uploadEmpTrackerFile(job_id,coa_id,divm_id,dept_id,date,bytes, blobFile,fileName,dist,totalTime,stoppedTime);
                break;
            }
            else {
                System.out.println("Ei "+date +" database e ase");
            }
        }
        if (noDatetoUp) {
            connected = true;
            notifyUser();
        }
    }

    public void uploadEmpTrackerFile(int job_id, String coa_id, String divm_id, String dept_id, String date, byte[] bytes, File blobFile, String fileName, String dist, String totalTime, String stoppedTime) {

        String uploadFileUrl = "http://103.56.208.123:8001/apex/elite_force/attendance/uploadTrackerFile";

        RequestQueue requestQueue = Volley.newRequestQueue(mContext);

        StringRequest uploadReq = new StringRequest(Request.Method.POST, uploadFileUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                System.out.println(string_out);
                if (string_out.equals("Successfully Created")) {
                    lastTenDaysFromSQL.add(date);
                    getTrackerDate(job_id,coa_id,divm_id,dept_id);
                }
                else {
                    System.out.println("EKHANE ASHE 3");
                    connected = false;
                    notifyUser();
                }
            }
            catch (JSONException e) {
                e.printStackTrace();
                System.out.println("EKHANE ASHE 0");
                connected = false;
                notifyUser();
            }
        },error -> {
            error.printStackTrace();
            System.out.println("EKHANE ASHE -1");
            conn = false;
            connected = false;
            notifyUser();
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                return bytes;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                if (blobFile.exists()) {
                    headers.put("P_ELR_ACTIVE","1");
                    headers.put("P_ELR_FILE_NAME",fileName);
                    headers.put("P_ELR_MIMETYPE","application/gpx+xml");
                    headers.put("P_ELR_FILETYPE",".gpx");
                }
                else {
                    headers.put("P_ELR_ACTIVE","0");
                    headers.put("P_ELR_FILE_NAME",null);
                    headers.put("P_ELR_MIMETYPE",null);
                    headers.put("P_ELR_FILETYPE",null);
                }
                headers.put("P_ELR_EMP_ID",emp_id);
                headers.put("P_ELR_JOB_ID",String.valueOf(job_id));
                headers.put("P_ELR_COA_ID",coa_id);
                headers.put("P_ELR_DIVM_ID",divm_id);
                headers.put("P_ELR_DEPT_ID",dept_id);
                headers.put("P_ELR_DATE",date);
                headers.put("P_ELR_USER",emp_code);
                headers.put("P_TOTAL_DISTANCE_KM",dist);
                headers.put("P_TOTAL_TIME",totalTime);
                headers.put("P_TOTAL_STOPPED_TIME",stoppedTime);
                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/binary";
            }
        };

        requestQueue.add(uploadReq);
    }

    public static byte[] method(File file) throws IOException {
        FileInputStream fl = new FileInputStream(file);
        byte[] arr = new byte[(int) file.length()];
        fl.read(arr);
        fl.close();
        return arr;
    }

    public void notifyUser() {
        if (conn) {
            if(connected) {
                if (dateCount > 0) {
                    if (updatedFiles.size() != 0) {
                        for (int i = 0; i < updatedFiles.size(); i++) {
                            String stringFile = updatedFiles.get(i);
                            File blobFile = new File(stringFile);
                            if (blobFile.exists()) {
                                boolean deleted = blobFile.delete();
                                if (deleted) {
                                    System.out.println("Deleted");
                                }
                            }
                        }
                    }
                    if (updatedXml.size() != 0) {
                        for (int i = 0 ; i < updatedXml.size(); i++) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                File dir = new File(mContext.getApplicationInfo().dataDir, "shared_prefs/" + updatedXml.get(i)+ ".xml");
                                if(dir.exists()) {
                                    mContext.getSharedPreferences(updatedXml.get(i), MODE_PRIVATE).edit().clear().apply();
                                    boolean ddd = dir.delete();
                                    System.out.println(ddd);
                                } else {
                                    System.out.println(false);
                                }
                            }
                        }
                    }

                    if (dateCount == 1) {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                                .setSmallIcon(R.drawable.notification_icon)
                                .setContentTitle("Tracking Service")
                                .setContentText(dateCount + " File Uploaded")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
                        notificationManagerCompat.notify(200, builder.build());
                    } else {
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, channelId)
                                .setSmallIcon(R.drawable.notification_icon)
                                .setContentTitle("Tracking Service")
                                .setContentText(dateCount + " Files Uploaded")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(mContext);
                        notificationManagerCompat.notify(200, builder.build());
                    }

                }
                conn = false;
                connected = false;
            }
            else {
                retryNo++;
                if (retryNo <= 3) {
                    getEmpData();
                }
            }
        }
        else {
            retryNo++;
            if (retryNo <= 3) {
                getEmpData();
            }
        }
    }
}
