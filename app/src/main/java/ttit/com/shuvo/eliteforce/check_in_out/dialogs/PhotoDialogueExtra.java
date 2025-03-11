package ttit.com.shuvo.eliteforce.check_in_out.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import ttit.com.shuvo.eliteforce.R;

public class PhotoDialogueExtra extends AppCompatDialogFragment {

    AppCompatActivity activity;
    AlertDialog alertDialog;

    PhotoView photoView;
    ImageView clear;
    Context mContext;
    Bitmap imageBitmap;

    public PhotoDialogueExtra(Context mContext, Bitmap imageBitmap) {
        this.mContext = mContext;
        this.imageBitmap = imageBitmap;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.pic_dial_view, null);
        activity = (AppCompatActivity) view.getContext();
        photoView=  view.findViewById(R.id.photo_view);
        clear = view.findViewById(R.id.clear_image);

        Glide.with(mContext)
                .load(imageBitmap)
                .error(R.drawable.loading_error)
                .placeholder(R.drawable.loading)
                .into(photoView);

        builder.setView(view);

        alertDialog = builder.create();

        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        clear.setOnClickListener(view1 -> alertDialog.dismiss());

        return alertDialog;
    }
}
