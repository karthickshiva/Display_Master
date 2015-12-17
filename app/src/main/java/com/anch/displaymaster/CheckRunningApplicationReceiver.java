package com.anch.displaymaster;

import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.provider.Settings;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by Karthick on 12/14/2015.
 */
public class CheckRunningApplicationReceiver extends BroadcastReceiver {
    public final String TAG = "CRAR";
    public static final String PREFS_NAME = "APP_PREFS";

    @SuppressWarnings("ResourceType")
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String topApp = null;
        if(Build.VERSION.SDK_INT > 20) {
            UsageStatsManager usm = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*1000, time);

            if(stats != null) {
                SortedMap<Long, UsageStats> apps = new TreeMap<>();
                for(UsageStats usageStats : stats) {
                    apps.put(usageStats.getLastTimeUsed(), usageStats);
                }
                topApp = apps.get(apps.lastKey()).getPackageName();
            }

        }
        else
            topApp = am.getRunningTasks(1).get(0).topActivity.getPackageName();

        String current = " ";
            Gson gson = new Gson();
            if(!current.equals(topApp)) {

                Map m = prefs.getAll();
                current = topApp;
                AppPrefs appPrefs = gson.fromJson((String) m.get(current), AppPrefs.class);
                if(appPrefs.isEnabled()) {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, appPrefs.getBrightness());

                }
                else {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, -1);
                }

                if(appPrefs.isTEnabled()) {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, appPrefs.getBackTime());
                }
                else {
                    Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_OFF_TIMEOUT, -1);
                }

            }
    }
}
