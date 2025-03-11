package ttit.com.shuvo.eliteforce.check_in_out.addCheck;

import static ttit.com.shuvo.eliteforce.login.Login.userInfoLists;
import static ttit.com.shuvo.eliteforce.utility.Constants.api_url_front;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.basic_model.TwoItemLists;
import ttit.com.shuvo.eliteforce.check_in_out.dialogs.ImageSaveDialog;
import ttit.com.shuvo.eliteforce.check_in_out.dialogs.PhotoDialogueExtra;
import ttit.com.shuvo.eliteforce.check_in_out.interfaces.ImageSaveListener;
import ttit.com.shuvo.eliteforce.utility.WaitProgress;

public class AddCheckIn extends AppCompatActivity implements OnMapReadyCallback, ImageSaveListener {

    private GoogleMap mMap;

    TextInputLayout clientSpinnerLay;
    AutoCompleteTextView clientSpinner;
    String selected_ad_id = "";
    String selected_client_name = "";
    ArrayList<TwoItemLists> clientLists;

    TextInputLayout remarksLay;
    TextInputEditText remarks;
    String remarks_text = "";

    MaterialButton cameraClick;
    MaterialCardView imageCard;
    ImageView capturedImage;
    ImageView recapture;

    Button submit;

    FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager locationManager;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    String emp_id = "";

    ActivityResultLauncher<Intent> someActivityResultLauncher;

    public static String ci_lat = "";
    public static String ci_lon = "";
    public static String ci_address = "";
    public static Bitmap checkInImage;
    Bitmap finalImage;
    public static String currentPhotoPath_ci;
    public static String imageFileName_ci = "";

    public static Location targetLocation_ci = null;
    LatLng lastLatLongitude_ci = null;

    WaitProgress waitProgress = new WaitProgress();
    private Boolean conn = false;
    private Boolean connected = false;
    boolean selectedFromItems = false;

    Logger logger = Logger.getLogger(AddCheckIn.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_check_in);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.check_in_map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        clientSpinnerLay = findViewById(R.id.spinner_layout_check_in_client);
        clientSpinner = findViewById(R.id.check_in_client_spinner);

        remarksLay = findViewById(R.id.remarks_for_check_in_lay);
        remarks = findViewById(R.id.remarks_for_check_in);

        cameraClick = findViewById(R.id.camera_click_button_in_check_in);
        imageCard = findViewById(R.id.captured_image_view_in_check_in_card);
        imageCard.setVisibility(View.GONE);
        capturedImage = findViewById(R.id.captured_image_view_in_check_in);
        recapture = findViewById(R.id.re_capture_button_in_check_in);
        recapture.setVisibility(View.GONE);

        submit = findViewById(R.id.check_in_submit_button);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 3000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();

        emp_id = userInfoLists.get(0).getEmp_id();

        clientLists = new ArrayList<>();

        clientSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    clientSpinnerLay.setHelperText("");
                }
            }
        });

        clientSpinner.setOnItemClickListener((parent, view, position, id) -> {
            String name = parent.getItemAtPosition(position).toString();
            System.out.println(position+": "+name);
            selected_ad_id = "";
            selected_client_name = "";
            for (int i = 0; i <clientLists.size(); i++) {
                if (name.equals(clientLists.get(i).getName())) {
                    selected_ad_id = clientLists.get(i).getId();
                    selected_client_name = clientLists.get(i).getName();
                }
            }

            selectedFromItems = true;
            clientSpinnerLay.setHelperText("");
            closeKeyBoard();
        });

        clientSpinner.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                String ss = clientSpinner.getText().toString();
                if (!selectedFromItems) {
                    selected_ad_id = "";
                    for (int i = 0; i < clientLists.size(); i++) {
                        if (ss.equals(clientLists.get(i).getName())) {
                            selected_ad_id = clientLists.get(i).getId();
                            selected_client_name = clientLists.get(i).getName();
                        }
                    }
                    if (selected_ad_id.isEmpty()) {
                        if (ss.isEmpty()) {
                            clientSpinnerLay.setHelperText("Please Provide Client Name");
                        }
                        else {
                            clientSpinnerLay.setHelperText("Invalid Client");
                        }
                    }
                    else {
                        clientSpinnerLay.setHelperText("");
                    }
                }
                else {
                    selectedFromItems = false;
                }
            }
        });

        clientSpinner.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    clientSpinner.clearFocus();
                    closeKeyBoard();

                    return false; // consume.
                }
            }
            return false;
        });

        remarks.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                    actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT || event != null && event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                    event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT) {
                if (event == null || !event.isShiftPressed()) {
                    // the user is done typing.
                    Log.i("Let see", "Come here");
                    closeKeyBoard();
                    return false; // consume.
                }
            }
            return false;
        });

        someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        // Getting ImageFile Name
                        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",Locale.getDefault()).format(new Date());
                        System.out.println(timeStamp);
                        imageFileName_ci = "IMG_" + timeStamp;

                        File imgFile = new  File(currentPhotoPath_ci);
                        if(imgFile.exists()) {

                            checkInImage = BitmapFactory.decodeFile(currentPhotoPath_ci);
                            try {
                                checkInImage = modifyOrientation(checkInImage, currentPhotoPath_ci);
                            } catch (IOException e) {
                                logger.log(Level.WARNING,e.getMessage(),e);
                            }

                            android.graphics.Bitmap.Config bitmapConfig = checkInImage.getConfig();
                            // set default bitmap config if none
                            if(bitmapConfig == null) {
                                bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
                            }
                            // resource bitmaps are imutable,
                            // so we need to convert it to mutable one
                            checkInImage = checkInImage.copy(bitmapConfig, true);

                            ImageSaveDialog imageDialogue = new ImageSaveDialog(AddCheckIn.this,AddCheckIn.this);
                            imageDialogue.show(getSupportFragmentManager(),"ImageCI");
                        }
                    }
                });

        capturedImage.setOnClickListener(view -> {
            PhotoDialogueExtra photoDialoguePE = new PhotoDialogueExtra(AddCheckIn.this,finalImage);
            photoDialoguePE.show(getSupportFragmentManager(),"PICTURE_3");
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                finish();
            }
        });

        submit.setOnClickListener(view -> {
            remarks_text = Objects.requireNonNull(remarks.getText()).toString();
            if (!selected_ad_id.isEmpty()) {
                if (finalImage != null) {

                    MaterialAlertDialogBuilder alertDialogBuilder = new MaterialAlertDialogBuilder(this);
                    alertDialogBuilder.setMessage("Do you want to submit now ?")
                                    .setPositiveButton("Yes",(dialogInterface, i) -> {
                                        addCheckInProcess();
                                        dialogInterface.dismiss();
                                    })
                            .setNegativeButton("No", (dialogInterface, i) -> {
                                dialogInterface.dismiss();
                            });

                    AlertDialog alert = alertDialogBuilder.create();
                    alert.show();

                }
                else {
                    Toast.makeText(this, "Please Provide Image", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                clientSpinnerLay.setHelperText("Please Provide Client Name");
            }
        });

    }

    private void closeKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            view.clearFocus();
            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    lastLatLongitude_ci = new LatLng(location.getLatitude(), location.getLongitude());
                    targetLocation_ci = location;
                }
            }
        };

        cameraClick.setOnClickListener(view -> {
            if (lastLatLongitude_ci != null) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        System.out.println("PhotoFile: " + ex.getLocalizedMessage());
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                "com.ttit.android.shuvoCameraProviderEF",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        try {
//                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_PC);
                            someActivityResultLauncher.launch(takePictureIntent);
                            Log.i("Activity:", "Shuru hoise");

                        } catch (ActivityNotFoundException e) {
                            // display error state to the user
                            System.out.println("Activity: "+e.getLocalizedMessage());
                        }
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Please wait for getting location",Toast.LENGTH_SHORT).show();
            }
        });

        recapture.setOnClickListener(view -> {
            if (lastLatLongitude_ci != null) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go
                    File photoFile = null;
                    try {
                        photoFile = createImageFile();
                    } catch (IOException ex) {
                        System.out.println("PhotoFile: " + ex.getLocalizedMessage());
                        // Error occurred while creating the File
                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                        Uri photoURI = FileProvider.getUriForFile(getApplicationContext(),
                                "com.ttit.android.shuvoCameraProviderEF",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        try {
//                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE_PC);
                            someActivityResultLauncher.launch(takePictureIntent);
                            Log.i("Activity:", "Shuru hoise");

                        } catch (ActivityNotFoundException e) {
                            // display error state to the user
                            System.out.println("Activity: "+e.getLocalizedMessage());
                        }
                    }
                }
            }
            else {
                Toast.makeText(getApplicationContext(), "Please wait for getting location",Toast.LENGTH_SHORT).show();
            }
        });


        getData();
    }

    public void getData() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        clientLists = new ArrayList<>();

        String clientUrl = api_url_front+"movement/getClient";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest clientReq = new StringRequest(Request.Method.GET, clientUrl, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String items = jsonObject.getString("items");
                String count = jsonObject.getString("count");
                if (!count.equals("0")) {
                    JSONArray array = new JSONArray(items);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject depInfo = array.getJSONObject(i);
                        String ad_id = depInfo.getString("ad_id")
                                .equals("null") ? "" : depInfo.getString("ad_id");
                        String ad_name = depInfo.getString("ad_name")
                                .equals("null") ? "" : depInfo.getString("ad_name");

                        clientLists.add(new TwoItemLists(ad_id,ad_name));
                    }
                }
                connected = true;
                updateLay();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                connected = false;
                updateLay();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            conn = false;
            connected = false;
            updateLay();
        });

        requestQueue.add(clientReq);
    }

    private void updateLay() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {
                conn = false;
                connected = false;

                ArrayList<String> type = new ArrayList<>();
                for(int i = 0; i < clientLists.size(); i++) {
                    type.add(clientLists.get(i).getName());
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(),R.layout.dropdown_menu_popup_item,R.id.drop_down_item,type);

                clientSpinner.setAdapter(arrayAdapter);

                newEnableGps();

            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage("There is a network issue in the server. Please Try later")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel", null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {

                    getData();
                    dialog.dismiss();
                });

                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> {
                    dialog.dismiss();
                    finish();
                });
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel", null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {

                getData();
                dialog.dismiss();
            });

            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> {
                dialog.dismiss();
                finish();
            });
        }
    }

    private void newEnableGps() {
        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(1000)
                .setMaxUpdateDelayMillis(2000)
                .build();
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> zoomToUserLocation());

        task.addOnFailureListener(this, e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(AddCheckIn.this,
                            1000);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
//                assert data != null;
//                String result = data.getStringExtra("result");
                zoomToUserLocation();
                Log.i("Hoise ", "1");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.i("Hoise ", "2");
                finish();
            }
        }
    }

    public void zoomToUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (!mMap.isMyLocationEnabled()) {
            mMap.setMyLocationEnabled(true);
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(location -> {
//                Log.i("lattt", location.toString());
            LatLng latLng;

            if (location != null) {
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
            }
            else {
                latLng = new LatLng(23.6850, 90.3563);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7));
            }

        });
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        System.out.println(timeStamp);
        String imageFileName = "IMGLC_" + timeStamp + "_";
        System.out.println(imageFileName);
        //File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath_ci = image.getAbsolutePath();
        return image;
    }

    public static Bitmap modifyOrientation(Bitmap bitmap, String image_absolute_path) throws IOException {
        ExifInterface ei = new ExifInterface(image_absolute_path);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotate(bitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotate(bitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotate(bitmap, 270);

            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                return flip(bitmap, true, false);

            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                return flip(bitmap, false, true);

            default:
                return bitmap;
        }
    }

    public static Bitmap rotate(Bitmap bitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap flip(Bitmap bitmap, boolean horizontal, boolean vertical) {
        Matrix matrix = new Matrix();
        matrix.preScale(horizontal ? -1 : 1, vertical ? -1 : 1);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    @Override
    public void onImageSave() {
        if (checkInImage != null) {
            finalImage = checkInImage;
            Glide.with(getApplicationContext())
                    .load(finalImage)
                    .error(R.drawable.loading_error)
                    .placeholder(R.drawable.loading)
                    .into(capturedImage);
            imageCard.setVisibility(View.VISIBLE);
            cameraClick.setVisibility(View.GONE);
            recapture.setVisibility(View.VISIBLE);
        }
    }

    public void addCheckInProcess() {
        waitProgress.show(getSupportFragmentManager(),"WaitBar");
        waitProgress.setCancelable(false);
        conn = false;
        connected = false;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        int quality = 30;
        finalImage.compress(Bitmap.CompressFormat.JPEG, quality, bos);
        byte[] bArray = bos.toByteArray();
//        long lengthbmp1 = bArray.length;
//        lengthbmp1 = (lengthbmp1/1024);
//
//        if (lengthbmp1 > 100) {
//            quality = quality - 10;
//            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
//            finalImage.compress(Bitmap.CompressFormat.JPEG, quality, bos1);
//            bArray = bos1.toByteArray();
//            lengthbmp1 = bArray.length;
//            lengthbmp1 = (lengthbmp1/1024);
//            System.out.println(lengthbmp1);
//            if (lengthbmp1 > 100) {
//                quality = quality - 10;
//                ByteArrayOutputStream bos2 = new ByteArrayOutputStream();
//                finalImage.compress(Bitmap.CompressFormat.JPEG, quality, bos2);
//                bArray = bos2.toByteArray();
//                lengthbmp1 = bArray.length;
//                lengthbmp1 = (lengthbmp1/1024);
//                System.out.println(lengthbmp1);
//                if (lengthbmp1 > 100) {
//                    quality = quality - 5;
//                    ByteArrayOutputStream bos3 = new ByteArrayOutputStream();
//                    finalImage.compress(Bitmap.CompressFormat.JPEG, quality, bos3);
//                    bArray = bos3.toByteArray();
//                    lengthbmp1 = bArray.length;
//                    lengthbmp1 = (lengthbmp1/1024);
//                    System.out.println(lengthbmp1);
//                    if (lengthbmp1 > 100) {
//                        quality = quality - 3;
//                        ByteArrayOutputStream bos4 = new ByteArrayOutputStream();
//                        finalImage.compress(Bitmap.CompressFormat.JPEG, quality, bos4);
//                        bArray = bos4.toByteArray();
//                        lengthbmp1 = bArray.length;
//                        lengthbmp1 = (lengthbmp1/1024);
//                        System.out.println(lengthbmp1);
//                    }
//                }
//            }
//        }

        String url = api_url_front+"checkInOut/insertCheckIn";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest attReq = new StringRequest(Request.Method.POST, url, response -> {
            conn = true;
            try {
                JSONObject jsonObject = new JSONObject(response);
                String string_out = jsonObject.getString("string_out");
                if (string_out.equals("Successfully Created")) {
                    connected = true;
                }
                else {
                    System.out.println(string_out);
                    connected = false;
                }
                updateLayout();
            }
            catch (JSONException e) {
                logger.log(Level.WARNING,e.getMessage(),e);
                connected = false;
                updateLayout();
            }
        }, error -> {
            logger.log(Level.WARNING,error.getMessage(),error);
            conn = false;
            connected = false;
            updateLayout();
        }) {
            @Override
            public byte[] getBody() {
                return bArray;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("P_EMP_ID",emp_id);
                headers.put("P_AD_NAME",selected_client_name);
                headers.put("P_AD_ID",selected_ad_id);
                headers.put("P_ADDRESS",ci_address);
                headers.put("P_LAT",ci_lat);
                headers.put("P_LNG",ci_lon);
                headers.put("P_REMARKS",remarks_text);
                return  headers;
            }

            @Override
            public String getBodyContentType() {
                return "application/binary";
            }
        };

        attReq.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 10,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));
        requestQueue.add(attReq);
    }

    private  void updateLayout() {
        waitProgress.dismiss();
        if (conn) {
            if (connected) {

                Toast.makeText(this, "Check In Submitted Successfully", Toast.LENGTH_SHORT).show();
                finish();

                conn = false;
                connected = false;
            }
            else {
                AlertDialog dialog = new AlertDialog.Builder(AddCheckIn.this)
                        .setMessage("There is a network issue in the server. Please Try later.")
                        .setPositiveButton("Retry", null)
                        .setNegativeButton("Cancel",null)
                        .show();

                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                positive.setOnClickListener(v -> {
                    addCheckInProcess();
                    dialog.dismiss();
                });
                Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                negative.setOnClickListener(v -> dialog.dismiss());
            }
        }
        else {
            AlertDialog dialog = new AlertDialog.Builder(AddCheckIn.this)
                    .setMessage("Please Check Your Internet Connection")
                    .setPositiveButton("Retry", null)
                    .setNegativeButton("Cancel",null)
                    .show();

            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            Button positive = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            positive.setOnClickListener(v -> {
                addCheckInProcess();
                dialog.dismiss();
            });
            Button negative = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            negative.setOnClickListener(v -> dialog.dismiss());
        }
    }
}