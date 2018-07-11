package com.example.leebet_pc.bidconnect;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public final class Utilities {

    static ProgressDialog dialogProgress;
    private DatabaseReference aucDB;

    private FirebaseDatabase mainDB = FirebaseDatabase.getInstance();
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static void networkAlert(Context context){
        if(!isNetworkAvailable(context)){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("No Internet Connection");
            builder.setMessage("Please reconnect to see updated information.");
            builder.setCancelable(false);
            builder.setPositiveButton("Okay, I will", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        }
    }

    public static void makeToast(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void pleaseReconnectFirst(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("No Internet Connection");
        builder.setMessage("Please reconnect before uploading.");
        builder.setCancelable(false);
        builder.setPositiveButton("Okay, I will", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    public static void showLoadingDialog(Context context){
        if(isNetworkAvailable(context)){
            dialogProgress = new ProgressDialog(context);
            dialogProgress.setCancelable(false);
            dialogProgress.setMessage("Loading...");
            dialogProgress.setIndeterminate(true);
            dialogProgress.show();
        }
    }

    public static void setDialogMessage(String message){
        if(dialogProgress!=null && dialogProgress.isShowing()){
            dialogProgress.setMessage(message);
        }
    }

    public static void showUploadingDialog(Context context){
        dialogProgress = new ProgressDialog(context);
        dialogProgress.setCancelable(false);
        dialogProgress.setMessage("Uploading...");
        dialogProgress.setIndeterminate(true);
        dialogProgress.show();
    }

    public static void dismissDialog(){
        if(dialogProgress!=null && dialogProgress.isShowing()){
            dialogProgress.dismiss();
        }
    }

    public static void taskFailedAlert(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Task Failed");
        builder.setMessage("Cannot complete task. Please connect to the internet and try again.");
        builder.setCancelable(true);
        builder.show();
    }

    public static void basicAlert(Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();
    }

    public static void loadImage(final Context context, final String uri, final ImageView imageView){
        try{
            Picasso.with(context)
                    .load(uri).fit().centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            // Try again online if cache failed
                            Picasso.with(context)
                                    .load(uri).fit().centerCrop()
                                    .placeholder(R.drawable.progress_animation)
                                    .error(R.drawable.ic_baseline_error_outline_24px)
                                    .into(imageView);
                        }
                    });
        }catch (Exception e){
            imageView.setImageResource(R.drawable.ic_baseline_error_outline_24px);
        }
    }
}
