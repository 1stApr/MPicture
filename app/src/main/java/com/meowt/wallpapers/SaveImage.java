package com.meowt.wallpapers;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;


public class SaveImage extends AppCompatActivity implements Target {
    private Context context;
    private WeakReference<AlertDialog> alertDialogWeakReference;
    private WeakReference<ContentResolver> contentResolverWeakReference;
    private String name;
    private  String desc;
    private  final String CHANNEL_ID = " display_notification";
    private   final int NOTIFICATION_ID = 001;

    public SaveImage(Context context, AlertDialog alertDialog, ContentResolver contentResolver, String name, String desc) {
        this.context = context;
        this.alertDialogWeakReference = new WeakReference<AlertDialog>(alertDialog);
        this.contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolver);
        this.name = name;
        this.desc = desc;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        ContentResolver contentResolver = contentResolverWeakReference.get();
        AlertDialog alert = alertDialogWeakReference.get();
        if(contentResolver != null){
            MediaStore.Images.Media.insertImage(contentResolver,bitmap,name,desc);
        }
        alert.dismiss();
        Toast.makeText(context,"Downloaded\n", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBitmapFailed(Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {


    }
   
}
