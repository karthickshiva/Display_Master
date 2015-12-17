package com.anch.displaymaster;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Karthick on 12/12/2015.
 */


public class AppListAdapter extends BaseExpandableListAdapter implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, AdapterView.OnItemSelectedListener {

    private Activity activity;
    private List<PackageItem> data;
    private static LayoutInflater inflater = null;
    public static final String PREFS_NAME = "APP_PREFS";
    public Resources res;
    PackageItem app = null;
    private HashMap<String, AppPrefs> mapBright;
    private HashMap<String, Integer> mapTimeout;
    private ArrayAdapter<String> dataAdapter;
    private List<Integer> indList;

    public AppListAdapter(Activity a, List d, Resources resLocal) {
        activity = a;
        data = d;
        res = resLocal;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mapBright = new HashMap<>();
        Gson gson = new Gson();
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        for (PackageItem app : data) {
            AppPrefs prefs = gson.fromJson(settings.getString(app.getPackageName(), ""), AppPrefs.class);
            mapBright.put(app.getPackageName(), prefs);
        }

        List<String> categories = new ArrayList<>();
        categories.add("15 seconds");
        categories.add("30 seconds");
        categories.add("1 minute");
        categories.add("2 minutes");
        categories.add("10 minutes");
        categories.add("30 minutes");

        dataAdapter = new ArrayAdapter<>(a, android.R.layout.simple_spinner_item, categories);
        mapTimeout = new HashMap<>();
        mapTimeout.put("15 seconds", 15000);
        mapTimeout.put("30 seconds", 30000);
        mapTimeout.put("1 minute", 60000);
        mapTimeout.put("2 minutes", 120000);
        mapTimeout.put("10 minutes", 600000);
        mapTimeout.put("30 minutes", 1800000);
        indList = new ArrayList<>();
        indList.add(15000);
        indList.add(30000);
        indList.add(60000);
        indList.add(120000);
        indList.add(600000);
        indList.add(1800000);
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return null;
    }

    @Override
    public Object getChild(int i, int i2) {
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i2) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        View v = view;
        ViewHolder holder;

        if(view == null) {
            v = inflater.inflate(R.layout.list_apps, null);
            holder = new ViewHolder();
            holder.addView(v.findViewById(R.id.appName));
            holder.addView(v.findViewById(R.id.appIcon));
            v.setTag(holder);
        }
        else
            holder = (ViewHolder) v.getTag();

       /* if(data.size() <= 0)
            holder.appName.setText("No applications found"); */

        app = data.get(i);

        TextView appName = (TextView) holder.getView(R.id.appName);
        ImageView appIcon = (ImageView) holder.getView(R.id.appIcon);

        appName.setText(app.getName());
        appIcon.setImageDrawable(app.getIcon());
        return v;
    }

    @Override
    public View getChildView(int i, int i2, boolean b, View view, ViewGroup viewGroup) {
        View v;
        ViewHolder holder;
        app = data.get(i);
        AppPrefs prefs = mapBright.get(app.getPackageName());
        v = inflater.inflate(R.layout.list_prefs, null);
        holder = new ViewHolder();
        holder.addView(v.findViewById(R.id.cbEnable));
        holder.addView(v.findViewById(R.id.barBrightness));
        holder.addView(v.findViewById(R.id.cbTEnable));
        holder.addView(v.findViewById(R.id.spinTimeout));
        CheckBox cbEnable = (CheckBox) holder.getView(R.id.cbEnable);
        SeekBar barBrightness = (SeekBar) holder.getView(R.id.barBrightness);
        CheckBox cbTEnable = (CheckBox) holder.getView(R.id.cbTEnable);
        Spinner spinTimeout = (Spinner) holder.getView(R.id.spinTimeout);
        spinTimeout.setAdapter(dataAdapter);

       if(prefs.getBrightness() >= 0)
            barBrightness.setProgress(prefs.getBrightness());

       if(prefs.getBackTime() >= 0)
           spinTimeout.setSelection(indList.indexOf(prefs.getBackTime()));

        barBrightness.setEnabled(prefs.isEnabled());
        spinTimeout.setEnabled(prefs.isTEnabled());
        cbEnable.setChecked(prefs.isEnabled());
        cbTEnable.setChecked(prefs.isTEnabled());

        cbEnable.setTag(barBrightness);
        cbTEnable.setTag(spinTimeout);
        barBrightness.setTag(app.getPackageName());
        spinTimeout.setTag(app.getPackageName());
        barBrightness.setOnSeekBarChangeListener(this);
        spinTimeout.setOnItemSelectedListener(this);
        cbEnable.setOnClickListener(this);
        cbTEnable.setOnClickListener(this);
        return v;
    }

    @Override
    public boolean isChildSelectable(int i, int i2) {
        return false;
    }

    @Override
    public void onProgressChanged(SeekBar sb, int i, boolean b) {
        Gson gson = new Gson();
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        AppPrefs prefs = gson.fromJson(settings.getString((String) sb.getTag(), ""), AppPrefs.class);
        prefs.setBrightness(i);
        mapBright.put((String) sb.getTag(), prefs);
        editor.putString((String) sb.getTag(), gson.toJson(prefs));
        editor.apply();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String t = (String) adapterView.getItemAtPosition(i);
        int time = mapTimeout.get(t);
        Gson gson = new Gson();
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        AppPrefs prefs = gson.fromJson(settings.getString((String) adapterView.getTag(), ""), AppPrefs.class);
        prefs.setBackTime(time);
        mapBright.put((String) adapterView.getTag(), prefs);
        editor.putString((String) adapterView.getTag(), gson.toJson(prefs));
        editor.apply();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public class ViewHolder {
       private HashMap<Integer, View> storedViews = new HashMap<>();

        public ViewHolder() {}

        public ViewHolder addView(View view) {
            int id = view.getId();
            storedViews.put(id, view);
            return this;
        }

        public View getView(int id) {
            return storedViews.get(id);
        }

    }


    @Override
    public void onClick(View view) {
        CheckBox cb = (CheckBox) view;
        View sb = (View) view.getTag();
        Gson gson = new Gson();
        SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        AppPrefs prefs = gson.fromJson(settings.getString((String) sb.getTag(), ""), AppPrefs.class);
        if(view.getId() == R.id.cbEnable)
            prefs.setEnabled(cb.isChecked());
        else
            prefs.setTEnabled(cb.isChecked());

        mapBright.put((String) sb.getTag(), prefs);
        sb.setEnabled(cb.isChecked());

        String app_gson = gson.toJson(prefs);
        editor.putString((String) sb.getTag(), app_gson);
        editor.apply();
    }
}
