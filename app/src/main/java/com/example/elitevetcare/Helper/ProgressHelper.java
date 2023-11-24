package com.example.elitevetcare.Helper;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.ContextCompat;

import com.example.elitevetcare.Activity.Login;
import com.example.elitevetcare.R;

import java.io.IOException;

import okhttp3.Response;

public class ProgressHelper {
    private static AlertDialog dialog = null;

    public static void showDialog(Context context, String message) {
        if(dialog == null){
            int llPadding = 30;
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setPadding(llPadding, llPadding, llPadding, llPadding);
            ll.setBackgroundResource(R.color.transparent);
            ll.setGravity(Gravity.CENTER);
            LinearLayout.LayoutParams llParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            llParam.gravity = Gravity.CENTER;
            ll.setLayoutParams(llParam);

            ProgressBar progressBar = new ProgressBar(context);
            progressBar.setIndeterminate(true);
            progressBar.setPadding(0, 0, llPadding, 0);
            Drawable progressDrawable = progressBar.getIndeterminateDrawable();
            progressDrawable.setColorFilter(Color.BLUE, PorterDuff.Mode.SRC_IN);
            progressBar.setIndeterminateDrawable(progressDrawable);
            progressBar.setLayoutParams(llParam);

            llParam = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            llParam.gravity = Gravity.CENTER;
            TextView tvText = new TextView(context);
            tvText.setText(message);
            tvText.setTextColor(Color.parseColor("#000000"));
            tvText.setTextSize(20);
            tvText.setLayoutParams(llParam);

            ll.addView(progressBar);
            ll.addView(tvText);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(ll);

            dialog = builder.create();

            dialog.getWindow().setBackgroundDrawableResource(R.drawable.background_transparent);
            dialog.show();
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
                layoutParams.copyFrom(dialog.getWindow().getAttributes());
                layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT;
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(layoutParams);
            }
        }
    }
    public static void showMessageDialog(Context context, String ContentMessage, CallbackMesageDialog callback ) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle("Cảnh Báo !!");
        SpannableString message = new SpannableString(ContentMessage);
        message.setSpan(new ForegroundColorSpan(Color.BLACK), 0, message.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton("Đồng Ý", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        callback.OnAccecpted();
                    }
                })
                .setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.custom_button_time);
        alertDialog.show();
        setBlueButton(context, alertDialog);
    }

    private static void setBlueButton(Context context, AlertDialog alertDialog) {
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        positiveButton.setTextColor(context.getResources().getColor(R.color.blue));

        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        negativeButton.setTextColor(context.getResources().getColor(R.color.blue));
    }

    public static  boolean isDialogVisible(){
        if(dialog != null){
            return dialog.isShowing();
        }else {
            return false;
        }
    }

    public static  void dismissDialog(){
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    public interface CallbackMesageDialog {
        void OnAccecpted();
    }
}
