package com.anch.displaymaster;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.app.NotificationCompat;

import java.util.List;

/**
 * Created by Karthick on 12/13/2015.
 */
public class DMService extends Service {
    private static final int NOTIFICATION_ID = 1;
    ActivityManager am;
    Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        PendingIntent contentIndent = PendingIntent.getActivity(this, 0, intent, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Display Master")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText("Running")
                .setContentIntent(contentIndent)
                .setOngoing(true);
        Notification notification = mBuilder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);
        context = getApplicationContext();
        am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return flags;
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
