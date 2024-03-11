package ttit.com.shuvo.eliteforce.attendance.trackService;

import static ttit.com.shuvo.eliteforce.attendance.trackService.model.DistanceCalculator.CalculationByDistance;
import static ttit.com.shuvo.eliteforce.attendance.trackService.notification.Notification.CHANNEL_ID;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Environment;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import ttit.com.shuvo.eliteforce.MainActivity;
import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.trackService.model.GPXFileWriter;
import ttit.com.shuvo.eliteforce.attendance.trackService.model.LatLngTimeList;

public class Service extends android.app.Service {
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 3000;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    public static ArrayList<LatLngTimeList> locationLists;
    public static ArrayList<String> trk;

    private static final int ONE_MINUTES = 1000 * 60;
    public Location previousBestLocation = null;
    public Location firstLocation = null;

    final int[] local = {0};
    final int[] localLive = {0};
    LatLng lastlatLng = null;
    String lastTime = "";
    double lastDistance = 0.0;
    final LatLng[] preLatlng = {new LatLng(0, 0)};
    final Double[] w = {0.0};
    public static String length_multi = "";

    SharedPreferences sharedPreferences;
    public static final String LOGIN_ACTIVITY_FILE = "LOGIN_ACTIVITY_FILE_ELITE_FORCE_HR";
    public static final String EMP_ID_LOGIN = "EMP_ID";
    public static final String LIVE_FLAG = "LIVE_FLAG";
    String emp_id = "";
    int live_flag = 0;

    SharedPreferences sharedPreferencesDA;
    public static String FILE_OF_DAILY_ACTIVITY = "";
    public static  String DISTANCE = "DISTANCE";
    public static  String TOTAL_TIME = "TOTAL_TIME";
    public static  String STOPPED_TIME = "STOPPED_TIME";

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {

            for (Location location : locationResult.getLocations()) {
                Log.d("Locations", location.getLatitude() + "," + location.getLongitude());

                Date stopdate = Calendar.getInstance().getTime();
                SimpleDateFormat stopsimpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                String stoptime = stopsimpleDateFormat.format(stopdate);

                System.out.println(stoptime);
                if (stoptime.equals("11:59 PM")) {
                    stopSelf();
                }

                System.out.println(locationLists.size());

                if (local[0] == 0) {
                    local[0]++;
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    preLatlng[0] = ll;
                    length_multi = (String.format(Locale.ENGLISH,"%.3f", w[0]) + " KM");

                    firstLocation = location;
                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    String time = simpleDateFormat.format(date);
                    //firstLoc.setText(add);
                    locationLists.add(new LatLngTimeList(ll, time));

                } else {
                    local[0]++;
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    Double distance = CalculationByDistance(preLatlng[0], ll);

                    Date date = Calendar.getInstance().getTime();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
                    String time = simpleDateFormat.format(date);
                    Log.i("Distance", distance.toString());

                    long timeData = location.getTime() - firstLocation.getTime();
                    double hour = (double) timeData / (ONE_MINUTES * 60);
                    int speed = (int) (distance / hour);
                    System.out.println("SPEED FOR GPX: " + speed + " KM/H");
                    firstLocation = location;

                    lastlatLng = ll;
                    lastTime = time;
                    lastDistance = distance;

                    if (distance >= 0.03 && speed < 500) {

                        w[0] = w[0] + distance;

                        preLatlng[0] = ll;
                        length_multi = (String.format(Locale.ENGLISH,"%.3f", w[0]) + " KM");

                        locationLists.add(new LatLngTimeList(ll, time));
                    }
                }

                System.out.println("LOCATION ACCURACY: " + location.getAccuracy());
                System.out.println("LOCATION TIME: " + location.getTime());

                if (live_flag == 1) {
                    if (localLive[0] == 0) {

                        localLive[0]++;

                        System.out.println("LOCATION FIRST: " + location);
                        previousBestLocation = location;
                        LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());

                        String lat = String.valueOf(location.getLatitude());
                        String lon = String.valueOf(location.getLongitude());
                        String spd = "0 KM/H";
                        String add = getAddress(ll.latitude, ll.longitude);
                        String acc = String.valueOf(location.getAccuracy());
                        String bear = String.valueOf(location.getBearing());
                        UpdateLocation(lat, lon, spd, add, acc, bear);

                    } else {
                        if (previousBestLocation != null) {

                            localLive[0]++;
                            long timeData = location.getTime() - previousBestLocation.getTime();
                            if (timeData >= ONE_MINUTES) {

                                System.out.println("LOCATION OTHER: " + location);
                                LatLng prell = new LatLng(previousBestLocation.getLatitude(), previousBestLocation.getLongitude());
                                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                                double distance = CalculationByDistance(prell, ll);
                                System.out.println("DISTANCE: " + distance);
                                System.out.println("TIME DATA: " + timeData);
                                double hour = (double) timeData / (ONE_MINUTES * 60);
                                System.out.println("HOUR: " + hour);
                                int speed = (int) (distance / hour);
                                System.out.println("SPEED: " + speed + " KM/H");
                                System.out.println("BEARING: " + String.valueOf(location.getBearing()));
                                previousBestLocation = location;

                                String lat = String.valueOf(location.getLatitude());
                                String lon = String.valueOf(location.getLongitude());
                                String spd = String.valueOf(speed) + " KM/H";
                                String add = getAddress(ll.latitude, ll.longitude);
                                String acc = String.valueOf(location.getAccuracy());
                                String bear = String.valueOf(location.getBearing());

                                UpdateLocation(lat, lon, spd, add, acc, bear);

                            }
                        }
                    }
                }
            }
        }
    };

    public void UpdateLocation(String lat, String lon, String speed, String adds, String acc, String bear) {

        String url = api_url_front+"tracker/update_loc";

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.POST, url, response -> {

            System.out.println("RESPONSE ADDED");
            try {
                JSONObject jsonObject = new JSONObject(response);

            } catch (JSONException e) {
                //System.out.println(e.getLocalizedMessage());
            }
        }, error -> System.out.println("Failed to Upload Data")) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", emp_id);
                params.put("lat", lat);
                params.put("long", lon);
                params.put("speed", speed);
                params.put("address",adds);
                params.put("accuracy", acc);
                params.put("bearing",bear);
                return params;
            }
        };

        queue.add(request);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initData();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        initData();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        }
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Elite Force")
                .setContentText("Tracking your Location")
                .setSmallIcon(R.drawable.elite_force_logo)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(101, notification);
        startLocationUpdates();

        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        mFusedLocationClient.removeLocationUpdates(locationCallback);

        Calendar cal = GregorianCalendar.getInstance();
        Date calTime = cal.getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy",Locale.ENGLISH);

        String ddd = simpleDateFormat.format(calTime);

        ddd = ddd.toUpperCase();

        FILE_OF_DAILY_ACTIVITY = emp_id+"_"+ddd+"_track";

        sharedPreferencesDA = getSharedPreferences(FILE_OF_DAILY_ACTIVITY, MODE_PRIVATE);
        String distance = sharedPreferencesDA.getString(DISTANCE,null);
        String totalTime = sharedPreferencesDA.getString(TOTAL_TIME,null);
        String stopppedTime = sharedPreferencesDA.getString(STOPPED_TIME, null);


        if (locationLists.size() > 1) {

            if (lastlatLng != null) {
                locationLists.add(new LatLngTimeList(lastlatLng,lastTime));
                w[0] = w[0] + lastDistance;
                length_multi = (String.format(Locale.ENGLISH,"%.3f", w[0]) + " KM");
            }

            if (distance != null) {
                double ddddiss = Double.parseDouble(distance);
                ddddiss = ddddiss + w[0];
                distance = String.format(Locale.ENGLISH,"%.3f", ddddiss);
            }
            else {
                distance = String.format(Locale.ENGLISH,"%.3f", w[0]);
            }

            if (totalTime != null) {
                long tt = Long.parseLong(totalTime);

                String fTime = locationLists.get(0).getTime();
                String lTime = locationLists.get(locationLists.size()-1).getTime();

                SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);

                Date first = null;
                Date last = null;

                try {
                    first = sdfTime.parse(fTime);
                    last = sdfTime.parse(lTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (first != null && last != null) {
                    long millis =  last.getTime() - first.getTime();
                    tt = tt + millis;
                    totalTime = String.valueOf(tt);
                }
            }
            else {
                String fTime = locationLists.get(0).getTime();
                String lTime = locationLists.get(locationLists.size()-1).getTime();

                SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);

                Date first = null;
                Date last = null;

                try {
                    first = sdfTime.parse(fTime);
                    last = sdfTime.parse(lTime);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (first != null && last != null) {
                    long millis =  last.getTime() - first.getTime();
                    totalTime = String.valueOf(millis);
                }
            }

            if (stopppedTime != null) {
                long st = Long.parseLong(stopppedTime);

                long millissss = 0;
                for (int l = 0; l <locationLists.size(); l++) {
                    if (l != locationLists.size() - 1) {
                        String oneTime = locationLists.get(l).getTime();
                        String twoTime = locationLists.get(l+1).getTime();

                        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);

                        Date first = null;
                        Date last = null;

                        try {
                            first = sdfTime.parse(oneTime);
                            last = sdfTime.parse(twoTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (first != null && last != null) {
                            long millis =  last.getTime() - first.getTime();
                            int hours = (int) (millis / (1000 * 60 * 60));
                            int mins = (int) ((millis / (1000 * 60)) % 60);

                            if (hours != 0 || mins >= 5) {
                                System.out.println("5 min er beshi");
                                millissss = millissss + millis;
                            }
                        }
                    }
                }
                st = st + millissss;
                stopppedTime = String.valueOf(st);
            }
            else {
                long millissss = 0;
                for (int l = 0; l <locationLists.size(); l++) {
                    if (l != locationLists.size() - 1) {
                        String oneTime = locationLists.get(l).getTime();
                        String twoTime = locationLists.get(l+1).getTime();

                        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);

                        Date first = null;
                        Date last = null;

                        try {
                            first = sdfTime.parse(oneTime);
                            last = sdfTime.parse(twoTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (first != null && last != null) {
                            long millis =  last.getTime() - first.getTime();
                            int hours = (int) (millis / (1000 * 60 * 60));
                            int mins = (int) ((millis / (1000 * 60)) % 60);

                            if (hours != 0 || mins >= 5) {
                                System.out.println("5 min er beshi");
                                millissss = millissss + millis;

                            }
                        }
                    }
                }

                stopppedTime = String.valueOf(millissss);
            }


            String start = "\t<trk>\n" +
                    "\t\t<name>TTIT</name>\n";
            String desc = "\t\t<desc>Length: " + length_multi + "</desc>\n";
            String trkseg = "\t\t<trkseg>\n";
            String trkpt = "";
            for (int b = 0; b < locationLists.size(); b++) {
                Log.i("Latlng :", locationLists.get(b).toString());
                trkpt += "\t\t\t<trkpt lat=\"" + locationLists.get(b).getLatLng().latitude + "\" lon=\"" + locationLists.get(b).getLatLng().longitude + "\">\n"+
                        "\t\t\t\t<time>"+locationLists.get(b).getTime()+"</time>\n"+
                        "\t\t\t</trkpt>\n";
            }
            String trksegFinish = "\t\t</trkseg>\n";
            String finish = "\t</trk>\n";

            trk.add(start + desc + trkseg + trkpt + trksegFinish + finish);

            System.out.println("Many Location add to trk: "+ trk.size());
        }
        else if (locationLists.size() == 1) {

            for (int i = 0; i < locationLists.size(); i++) {

                String wpt = "\t<wpt lat=\""+ locationLists.get(i).getLatLng().latitude +"\" lon=\""+ locationLists.get(i).getLatLng().longitude+"\">\n" +
                        "\t\t<name>TTIT</name>\n" +
                        "\t\t<time>"+locationLists.get(i).getTime()+"</time>\n"+
                        "\t</wpt>";
                trk.add(wpt);
            }
            System.out.println("1 Location add to trk: "+ trk.size());
        }

        System.out.println("Final Location Size: "+locationLists.size());
        System.out.println("Track Size: "+ trk.size());

        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yy", Locale.ENGLISH);

        String fileName = sdf.format(c);
        fileName = fileName.toUpperCase();
        fileName = emp_id+"_"+fileName+"_track";
        //fileName = "16-Oct-21"+"_Track";


        File myExternalFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),fileName+".gpx");

        if (myExternalFile.exists()) {
            try {
                System.out.println("EXISTING FILE");
                String gpxFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath() + File.separator +  fileName +".gpx";
                BufferedReader bufferedReader = new BufferedReader(new FileReader(gpxFile));
                String line;
                String input = "";

                while ((line = bufferedReader.readLine()) != null) {
                    input += line + '\n';
                }

                bufferedReader.close();

                if (input.contains("</gpx>")){
                    System.out.println("Got It");
                    String newInput = input.replace("</gpx>","");
                    GPXFileWriter.upDateGpxFile("TTITGenerator",trk,myExternalFile,newInput);
                    Toast.makeText(getApplicationContext(), "Your Track Record Has Been Saved", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could Not Save", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            try {
                GPXFileWriter.writeGpxFile("TTITGenerator", trk, myExternalFile);
                Toast.makeText(getApplicationContext(), "Your Track Record Has Been Saved", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Could Not Save", Toast.LENGTH_SHORT).show();
            }
        }

        SharedPreferences.Editor editor = sharedPreferencesDA.edit();
        editor.remove(TOTAL_TIME);
        editor.remove(DISTANCE);
        editor.remove(STOPPED_TIME);
        editor.putString(TOTAL_TIME,totalTime);
        editor.putString(STOPPED_TIME,stopppedTime);
        editor.putString(DISTANCE,distance);
        editor.apply();
        editor.commit();

        //Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();

        System.out.println("LocationList cleared");
        System.out.println("Track Record Cleared");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(this.locationRequest,
                this.locationCallback, Looper.getMainLooper());
    }

    private void initData() {
        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        emp_id = sharedPreferences.getString(EMP_ID_LOGIN,null);
        live_flag = sharedPreferences.getInt(LIVE_FLAG,0);
        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, UPDATE_INTERVAL_IN_MILLISECONDS)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();

        locationLists = new ArrayList<>();
        trk = new ArrayList<>();
        local[0] = 0;
        localLive[0] = 0;
        preLatlng[0] = new LatLng(0, 0);
        w[0] = 0.0;
        length_multi = "";
        lastDistance = 0.0;
        lastlatLng = null;
        lastTime = "";

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

    }

    public String getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(Service.this, Locale.ENGLISH);
        String address = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            if (Geocoder.isPresent()) {
                assert addresses != null;
                Address obj = addresses.get(0);
                String adds = obj.getAddressLine(0);
                String add = "Address from GeoCODE: ";
                add = add + "\n" + obj.getCountryName();
                add = add + "\n" + obj.getCountryCode();
                add = add + "\n" + obj.getAdminArea();
                add = add + "\n" + obj.getPostalCode();
                add = add + "\n" + obj.getSubAdminArea();
                add = add + "\n" + obj.getLocality();
                add = add + "\n" + obj.getSubThoroughfare();
                add = add + "\n" + obj.getFeatureName();
                add = add + "\n" + obj.getPhone();
                add = add + "\n" + obj.getPremises();
                add = add + "\n" + obj.getSubLocality();
                add = add + "\n" + obj.getThoroughfare();
                add = add + "\n" + obj.getUrl();

                Log.v("IGA", "Address: " + add);
                Log.v("NEW ADD", "Address: " + adds);
                address = adds;
            }

            return address;

        } catch (IOException e) {
            e.printStackTrace();
            return address;
        }
    }
}
