package com.anch.displaymaster;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by Karthick on 12/14/2015.
 */
public class StartUpReceiver extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1;
    static final String TAG = "SR";
    final int startupID = 1111111;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intt = new Intent(context, MainActivity.class);
        intent.setAction("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        PendingIntent contentIndent = PendingIntent.getActivity(context, 0, intt, 0);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                .setContentTitle("Display Master")
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText("Running")
                .setContentIntent(contentIndent)
                .setOngoing(true);
        Notification notification = mBuilder.build();
        notificationManager.notify(NOTIFICATION_ID, notification);
        final AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        try {
            Intent i = new Intent(context, CheckRunningApplicationReceiver.class);
            PendingIntent ServiceManagementIntent = PendingIntent.getBroadcast(context, startupID, i, 0);
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), 1000, ServiceManagementIntent);
        } catch (Exception e) {
            Log.i(TAG, "Exception: " + e);
        }

    }
}
