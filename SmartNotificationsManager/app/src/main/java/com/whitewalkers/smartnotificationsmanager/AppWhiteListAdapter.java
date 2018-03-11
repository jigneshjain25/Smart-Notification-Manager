package com.whitewalkers.smartnotificationsmanager;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ronaka on 24-07-2017.
 */

public class AppWhiteListAdapter extends BaseAdapter {
    private static LayoutInflater inflater=null;
    private List<ApplicationInfo> appList;
    private Activity activity;
    private Resources resources;
    private PackageManager packageManager;

    public AppWhiteListAdapter(Activity activity, Resources resources, List<ApplicationInfo> appList){
        this.activity = activity;
        this.appList = appList;
        this.resources = this.resources;

        inflater = (LayoutInflater) this.activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        packageManager = this.activity.getPackageManager();
    }


    @Override
    public int getCount() {
        if(appList.size() <= 0){
            return 1;
        }
        return appList.size();
    }

    @Override
    public Object getItem(int position) {

        return appList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    public static class ViewHolder{
        public TextView appName;
        public ImageView appIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder viewHolder;

        if(convertView == null) {
            row = inflater.inflate(R.layout.app_white_list_row, null);

            viewHolder = new ViewHolder();
            viewHolder.appName = (TextView)row.findViewById(R.id.appName);
            viewHolder.appIcon = (ImageView)row.findViewById(R.id.appIcon);
            row.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) row.getTag();
        }

        if(appList.size() <= 0) {
            viewHolder.appName.setText("No Data");
        }else{
            ApplicationInfo appInfo = appList.get(position);
            viewHolder.appName.setText(appInfo.loadLabel(this.packageManager));
            Drawable icon = Helper.getIconForNotification(this.activity, appInfo.packageName);
            if (icon != null) viewHolder.appIcon.setImageDrawable(icon);
            row.setOnClickListener(new OnItemClickListener( position ));
        }
        return row;
    }

    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements View.OnClickListener {
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            AppSettingFragment settingFragment = new AppSettingFragment();
            Bundle args = new Bundle();
            args.putString(AppSettingFragment.PACKAGE_NAME, appList.get(this.mPosition).packageName);
            settingFragment.setArguments(args);
            replaceFragment(settingFragment);
        }
    }

    private void replaceFragment(Fragment fragment) {
        if (activity.findViewById(R.id.FrameLayout) != null) {
            activity.getFragmentManager().beginTransaction().replace(R.id.FrameLayout, fragment).commit();
        }
    }
}
