package com.anch.displaymaster;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private List <PackageItem> apps;
    ExpandableListView appList;
    AppListAdapter appListAdapter;
    public static final String PREFS_NAME = "APP_PREFS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apps = getInstalledApplications();
        initialise();
        Resources resources = getResources();
        appList = (ExpandableListView) findViewById(R.id.listApps);
        appListAdapter = new AppListAdapter(this, apps, resources);
        appList.setAdapter(appListAdapter);
        appList.setDrawingCacheEnabled(false);
        appList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;
            @Override
            public void onGroupExpand(int i) {
                if(i != previousGroup)
                    appList.collapseGroup(previousGroup);
                previousGroup = i;
            }
        });
        sendBroadcast(new Intent("StartupReceiver_Manual_Start"));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_toggle) {
            SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            if(settings.getBoolean("Enable", true)) {
                settings.edit().putBoolean("Enable", false).apply();

            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public List<PackageItem> getInstalledApplications() {
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> listInfo = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        Collections.sort(listInfo, new ApplicationInfo.DisplayNameComparator(pm));

        List<PackageItem> data = new ArrayList<>();

        for(int i=0; i<listInfo.size(); i++) {
            try {
                ApplicationInfo app = listInfo.get(i);
                if(app.flags != ApplicationInfo.FLAG_SYSTEM && app.enabled) {
                    if(app.icon != 0) {
                        PackageItem item = new PackageItem();
                        item.setName(pm.getApplicationLabel(app).toString());
                        item.setPackageName(app.packageName);
                        item.setIcon(pm.getDrawable(app.packageName, app.icon, app));
                        data.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    public void initialise() {
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();

        for(PackageItem app : apps) {
            if(!settings.contains(app.getPackageName())) {
                Gson gson = new Gson();
                AppPrefs prefs = new AppPrefs();
                String app_gson = gson.toJson(prefs);
                editor.putString(app.getPackageName(), app_gson);
                editor.apply();
            }
        }

        if(!settings.contains("Enable")) {
            editor.putBoolean("Enable", true);
            editor.apply();
        }
    }
}
