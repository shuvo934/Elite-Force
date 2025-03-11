package ttit.com.shuvo.eliteforce.check_in_out.dialogs;

import static ttit.com.shuvo.eliteforce.check_in_out.addCheck.AddCheckIn.checkInImage;
import static ttit.com.shuvo.eliteforce.check_in_out.addCheck.AddCheckIn.ci_address;
import static ttit.com.shuvo.eliteforce.check_in_out.addCheck.AddCheckIn.ci_lat;
import static ttit.com.shuvo.eliteforce.check_in_out.addCheck.AddCheckIn.ci_lon;
import static ttit.com.shuvo.eliteforce.check_in_out.addCheck.AddCheckIn.currentPhotoPath_ci;
import static ttit.com.shuvo.eliteforce.check_in_out.addCheck.AddCheckIn.targetLocation_ci;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.rosemaryapp.amazingspinner.AmazingSpinner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.attendance.give_attendance.AttendanceGive;
import ttit.com.shuvo.eliteforce.attendance.give_attendance.dialogs.AttendanceRequest;
import ttit.com.shuvo.eliteforce.check_in_out.interfaces.ImageSaveListener;

public class ImageSaveDialog extends AppCompatDialogFragment {

    ImageView imageView;
    Button cancel;
    Button save;

    NestedScrollView fullLayout;
    CircularProgressIndicator circularProgressIndicator;
    String address = "";

    AlertDialog dialog;
    Context mContext;
    Activity activity;

    public ImageSaveDialog(Context mContext, Activity activity) {
        this.mContext = mContext;
        this.activity = activity;
    }
    private ImageSaveListener imageSaveListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        if (getActivity() instanceof ImageSaveListener)
            imageSaveListener = (ImageSaveListener) getActivity();

        View view = inflater.inflate(R.layout.captured_image_view_layout, null);

        imageView = view.findViewById(R.id.image_captured);
        save = view.findViewById(R.id.save_image);
        cancel = view.findViewById(R.id.cancel_save_image);
        fullLayout = view.findViewById(R.id.image_load_full_layout);
        circularProgressIndicator = view.findViewById(R.id.progress_indicator_image_load);
        circularProgressIndicator.setVisibility(View.GONE);

        builder.setView(view);

        dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        cancel.setOnClickListener(v -> {
            File deletefile = new File(currentPhotoPath_ci);
            boolean deleted = deletefile.delete();
            if (deleted) {
                System.out.println("deleted");
            }
            dialog.dismiss();
        });

        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                // deleting file
                File deletefile = new File(currentPhotoPath_ci);
                boolean deleted = deletefile.delete();
                if (deleted) {
                    System.out.println("deleted");
                }

                if(imageSaveListener != null)
                    imageSaveListener.onImageSave();

                dialog.dismiss();
            }
        });

        getAddressOfImage();
        return dialog;
    }

    public void getAddressOfImage() {
        circularProgressIndicator.setVisibility(View.VISIBLE);
        fullLayout.setVisibility(View.GONE);
        new Thread(() -> {
            double lat = targetLocation_ci.getLatitude();
            double lng = targetLocation_ci.getLongitude();

            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            try {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                if (Geocoder.isPresent()) {
                    if (addresses != null) {
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

                        Log.v("IGA", "Address: " + add);
                        Log.v("NEW ADD", "Address: " + adds);
                        address = adds;
                    }
                    else {
                        address = "Address Not Found";
                    }
                }
                else {
                    address = "Address Not Found";
                }
            }
            catch (IOException e) {
                address = "Address Not Found";
            }

            activity.runOnUiThread(() -> {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yy, hh:mm:ss a", Locale.getDefault());
                Date c = Calendar.getInstance().getTime();
                String dd = simpleDateFormat.format(c);
                ci_address = address;
                System.out.println(dd);
                String timeLatLng = "Time: " + dd + "\n" + "Latitude: " + targetLocation_ci.getLatitude() + "\n" + "Longitude: " + targetLocation_ci.getLongitude();
                address = timeLatLng + "\n"+ "Address: " + address;

                ci_lat = String.valueOf(targetLocation_ci.getLatitude());
                ci_lon = String.valueOf(targetLocation_ci.getLongitude());

                Resources resources = getResources();
                float scale = resources.getDisplayMetrics().density;

                Canvas canvas = new Canvas(checkInImage);

                // new antialiased Paint
                TextPaint paint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
                // text color - #3D3D3D
                paint.setColor(Color.WHITE);
                // text size in pixels
                paint.setTextSize((int) (36 * scale));
                // text shadow
                paint.setShadowLayer(4f, 0f, 2f, Color.BLACK);
                paint.setFakeBoldText(true);

                // set text width to canvas width minus 16dp padding
                int textWidth = canvas.getWidth() - (int) (16 * scale);

                // init StaticLayout for text

                StaticLayout textLayout = new StaticLayout(
                        address, paint, textWidth, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);

                // get height of multiline text
                int textHeight = textLayout.getHeight();

                // get position of text's top left corner
//                            float x = (firstBitmap_pc.getWidth() - textWidth)/2;
//                            float y = (firstBitmap_pc.getHeight() - textHeight)/2;


                // draw text to the Canvas center
                int yyyy = checkInImage.getHeight() - textHeight - 16;
                canvas.save();
                canvas.translate(5, yyyy);
                textLayout.draw(canvas);
                canvas.restore();

                if (checkInImage != null) {
                    Glide.with(mContext)
                            .load(checkInImage)
                            .error(R.drawable.loading_error)
                            .placeholder(R.drawable.loading)
                            .into(imageView);
                }

                circularProgressIndicator.setVisibility(View.GONE);
                fullLayout.setVisibility(View.VISIBLE);
            });
        }).start();
    }
}
