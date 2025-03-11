package ttit.com.shuvo.eliteforce;

import static ttit.com.shuvo.eliteforce.utility.Constants.LOGIN_ACTIVITY_FILE;
import static ttit.com.shuvo.eliteforce.utility.Constants.LOGIN_TF;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ttit.com.shuvo.eliteforce.login.Login;
import ttit.com.shuvo.eliteforce.mainPage.MainMenu;

@RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
public class MainActivity extends AppCompatActivity {

    TextView permissionMsg;
    private final Handler mHandler = new Handler();

    SharedPreferences sharedPreferences;
    boolean loginFile = false;

    boolean locPerm = false;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }
    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(LOGIN_ACTIVITY_FILE, MODE_PRIVATE);
        loginFile = sharedPreferences.getBoolean(LOGIN_TF,false);
        permissionMsg = findViewById(R.id.permission_not_granted_msg);

        locPerm = false;
        enableUserLocation();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (locPerm) {
            locPerm = false;
            System.out.println("LETS SEE");
            enableUserLocation();
        }
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private void goToActivityMap() {
        mHandler.postDelayed(() -> {
            Intent intent;
            if (loginFile) {
                intent = new Intent(MainActivity.this, MainMenu.class);
            } else {
                intent = new Intent(MainActivity.this, Login.class);
            }
            startActivity(intent);
            showSystemUI();
            finish();
        }, 1500);
    }

    public void showDialog(String title, String message, String positiveButtonTitle, DialogInterface.OnClickListener positiveListener) {
        MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
        alertDialogBuilder.setIcon(R.drawable.elite_force_logo)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonTitle, positiveListener);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void enableUserLocation() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            Log.i("Ekhane", "3");
            enableStorageAccess();
//            goToActivityMap();

        }
        else {
            Log.i("Ekhane", "4");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Log.i("Ekhane", "5");
                showDialog("Location Permission!", "This app needs the precise location permission for functioning.", "OK", (dialogInterface, i) -> {
                    multipleResultLauncher.launch(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION});
                });
            }
            else {
                Log.i("Ekhane", "6");
                multipleResultLauncher.launch(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION});
            }
        }
    }

    private final ActivityResultLauncher<String[]> multipleResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        System.out.println("OnActivityResult: " +result);
        boolean allGranted = true;
        for (String key: result.keySet()) {
            allGranted = allGranted && result.get(key);
        }
        if (allGranted) {
            System.out.println("HOLA2");
            enableStorageAccess();
        }
        else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                showDialog("Location Permission!", "This app needs the precise location permission to function. Please Allow that permission from settings.", "Go to Settings", (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
                    startActivity(intent);
                    locPerm = true;
                });
            }
            else {
                System.out.println("HOLA");
                enableUserLocation();
            }
        }
    });

    private void enableStorageAccess() {
        if (Build.VERSION.SDK_INT < 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                Log.i("Ekhane", "7");
                enableCallPhonePermission();
            }
            else {
                Log.i("Ekhane", "8");
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                    Log.i("Ekhane", "9");
                    showDialog("Storage Permission!", "This app needs the storage permission for functioning.", "OK", (dialogInterface, i) -> {
                        storageResultLauncher.launch(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE});
                    });
                }
                else {
                    Log.i("Ekhane", "10");
                    storageResultLauncher.launch(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,
                            android.Manifest.permission.WRITE_EXTERNAL_STORAGE});
                }
            }
        }
        else if (Build.VERSION.SDK_INT == 33) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED) {

                Log.i("Ekhane", "11");
                enableCallPhonePermission();
            }
            else {
                Log.i("Ekhane", "12");
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_MEDIA_IMAGES)) {

                    Log.i("Ekhane", "13");
                    showDialog("Photos and Videos Permission!", "This app needs the photos and videos permission for functioning.", "OK", (dialogInterface, i) -> {
                        storageResultLauncher.launch(new String[]{android.Manifest.permission.READ_MEDIA_IMAGES});
                    });
                }
                else {
                    Log.i("Ekhane", "14");
                    storageResultLauncher.launch(new String[]{android.Manifest.permission.READ_MEDIA_IMAGES});
                }
            }
        }
        else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)
                            == PackageManager.PERMISSION_GRANTED) {

                Log.i("Ekhane", "15");
                enableCallPhonePermission();
            }
            else {
                Log.i("Ekhane", "16");
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_MEDIA_IMAGES)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED)) {

                    Log.i("Ekhane", "17");
                    showDialog("Photos and Videos Permission!", "This app needs the photos and videos permission for functioning. Don't Select Limited Access. Please select Allow all.", "OK", (dialogInterface, i) -> {
                        storageResultLauncher.launch(new String[]{android.Manifest.permission.READ_MEDIA_IMAGES,
                                android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED});
                    });
                }
                else {
                    Log.i("Ekhane", "18");
                    storageResultLauncher.launch(new String[]{android.Manifest.permission.READ_MEDIA_IMAGES,
                            android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED});
                }
            }
        }

    }

    private final ActivityResultLauncher<String[]> storageResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        System.out.println("OnActivityResult: " +result);
        boolean allGranted = true;
        for (String key: result.keySet()) {
            allGranted = allGranted && result.get(key);
        }
        if (allGranted) {
            System.out.println("HOLA3");
            enableCallPhonePermission();
        }
        else {
            if (Build.VERSION.SDK_INT < 33) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        || !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showDialog("Storage Permission!", "This app needs the storage permission to function. Please Allow that permission from settings.", "Go to Settings", (dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
                        startActivity(intent);
                        locPerm = true;
                    });
                }
                else {
                    System.out.println("HOLA4");
                    enableStorageAccess();
                }
            }
            else if (Build.VERSION.SDK_INT == 33) {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    showDialog("Photos and Videos Permission!", "This app needs the photos and videos permission to function. Please Allow that permission from settings.", "Go to Settings", (dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
                        startActivity(intent);
                        locPerm = true;
                    });
                }
                else {
                    System.out.println("HOLA5");
                    enableStorageAccess();
                }
            }
            else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    showDialog("Photos and Videos Permission!", "This app needs the photos and videos permission to function. Don't Select Limited Access. Please Allow all from permission settings.", "Go to Settings", (dialogInterface, i) -> {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
                        startActivity(intent);
                        locPerm = true;
                    });
                }
                else {
                    System.out.println("HOLA6");
                    enableStorageAccess();
                }
            }
        }
    });

    private void enableCallPhonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED) {

            Log.i("Ekhane", "19");
            if (Build.VERSION.SDK_INT < 33) {
                goToActivityMap();
            }
            else {
                enableNotificationPermission();
            }
        }
        else {
            Log.i("Ekhane", "20");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)) {
                Log.i("Ekhane", "21");
                showDialog("Call Permission!", "This app needs the Call permission for functioning.", "OK", (dialogInterface, i) -> {
                    callPermResultLauncher.launch(new String[]{android.Manifest.permission.CALL_PHONE});
                });
            }
            else {
                Log.i("Ekhane", "22");
                callPermResultLauncher.launch(new String[]{android.Manifest.permission.CALL_PHONE});
            }
        }
    }

    private final ActivityResultLauncher<String[]> callPermResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        System.out.println("OnActivityResult: " +result);
        boolean allGranted = true;
        for (String key: result.keySet()) {
            allGranted = allGranted && result.get(key);
        }
        if (allGranted) {
            System.out.println("HOLA7");
            if (Build.VERSION.SDK_INT < 33) {
                goToActivityMap();
            }
            else {
                enableNotificationPermission();
            }
        }
        else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CALL_PHONE)) {
                showDialog("Call Permission!", "This app needs the Call permission to function. Please Allow that permission from settings.", "Go to Settings", (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
                    startActivity(intent);
                    locPerm = true;
                });
            }
            else {
                System.out.println("HOLA8");
                enableCallPhonePermission();
            }
        }
    });

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    public void enableNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {

            Log.i("Ekhane", "23");
            goToActivityMap();
        }
        else {
            Log.i("Ekhane", "24");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.POST_NOTIFICATIONS)) {
                Log.i("Ekhane", "25");
                showDialog("Notification Permission!", "This app needs the Notification permission for functioning.", "OK", (dialogInterface, i) -> {
                    notifyPermResultLauncher.launch(new String[]{android.Manifest.permission.POST_NOTIFICATIONS});
                });
            }
            else {
                Log.i("Ekhane", "26");
                notifyPermResultLauncher.launch(new String[]{android.Manifest.permission.POST_NOTIFICATIONS});
            }
        }
    }

    private final ActivityResultLauncher<String[]> notifyPermResultLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        System.out.println("OnActivityResult: " +result);
        boolean allGranted = true;
        for (String key: result.keySet()) {
            allGranted = allGranted && result.get(key);
        }
        if (allGranted) {
            System.out.println("HOLA9");
            goToActivityMap();
        }
        else {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.POST_NOTIFICATIONS)) {
                showDialog("Notification Permission!", "This app needs the Notification permission to function. Please Allow that permission from settings.", "Go to Settings", (dialogInterface, i) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:"+ getPackageName()));
                    startActivity(intent);
                    locPerm = true;
                });
            }
            else {
                System.out.println("HOLA10");
                enableNotificationPermission();
            }
        }
    });
}