package com.example.elitevetcare.Helper;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.elitevetcare.R;

import java.util.Date;

public class NotificationHelper {
    private static final String CHANNEL_ID = "ELITE_VET_CARE_CHANEL";
    private static NotificationManagerCompat notificationManager = null;
    public static void showNotification(Context context, String title, String message,int notificationId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.image_logo)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        // Add actions (optional)
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.example.com"));
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
//        builder.addAction(R.drawable.ic_open_link, "Open Link", pendingIntent);

        // Set notification sound (optional)
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);

        // Set large icon (optional)
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.image_logo));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("TAgggggg", "khong du dieu kien");
            return;
        }
        if(notificationManager == null){
            createNotificationChannel(context);
        }
        notificationManager.notify(notificationId, builder.build());
    }
    private static void createNotificationChannel(Context context) {
        notificationManager = NotificationManagerCompat.from(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Elite Vet Care";
            String description = "Vì Trải Nghiệm Của bạn";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this.
            notificationManager.createNotificationChannel(channel);
        }
    }
}
