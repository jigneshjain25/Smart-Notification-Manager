package com.whitewalkers.smartnotificationsmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class Helper {

    private static ISpamClassifier spamClassifier;
    private static ArrayList<String> _alwaysBlockList;
    private static ArrayList<String> _alwaysShowList;

    public static void initializeClassifier(ISpamClassifier _spamClassifier) {
        spamClassifier = _spamClassifier;
    }

    public static void setBlockAndShowList(ArrayList<String> alwaysShowList, ArrayList<String> alwaysBlockList) {
        _alwaysBlockList = alwaysBlockList;
        _alwaysShowList = alwaysShowList;
    }

    public static void updateBlockAndShowList(Context context) {
        ArrayList<AppSetting> settings = DatabaseHandler.getInstance(context).getAllAppSettings();
        _alwaysBlockList = new ArrayList<String>();
        _alwaysShowList = new ArrayList<String>();
        for (AppSetting setting: settings) {
            if (setting.isAlwaysBlock()) {
                _alwaysBlockList.add(setting.getPackageName());
            }
            else if (setting.isAlwaysShow()) {
                _alwaysShowList.add(setting.getPackageName());
            }
        }
    }

    public static boolean isSpamNotification(SNMNotification notification) throws Exception {
        if (spamClassifier == null) {
            throw new Exception("Spam classifier is not initalized");
        }
        if (_alwaysShowList != null && _alwaysShowList.contains(notification.getPackageName())) {
            return false;
        }
        else if (_alwaysBlockList != null && _alwaysBlockList.contains(notification.getPackageName())) {
            return true;
        }
        return spamClassifier.isSpamNotification(notification);
    }

    // Gets app name for a package name. If not able to find, will just return packagename
    public static String getAppNameFromPackageName(String packageName, Context context) {
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = packageManager.getApplicationInfo( packageName, 0);
        } catch (final PackageManager.NameNotFoundException e) {
            applicationInfo = null;
        }

        if (applicationInfo == null) {
            return packageName;
        }

        return (String)packageManager.getApplicationLabel(applicationInfo);
    }

    public static Bitmap getIconForNotification(Context context, String packageName, int iconId) {
        Context remotePackageContext = null;
        Bitmap bmp = null;
        try {
            remotePackageContext = context.createPackageContext(packageName, 0);
            Drawable icon = remotePackageContext.getResources().getDrawable(iconId);
            if(icon != null) {
                bmp = ((BitmapDrawable) icon).getBitmap();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (bmp == null)
            Log.e("jj", packageName + " " + iconId + " bmp is null");
        else
            Log.e("jj", packageName + " " + iconId + " bmp is not null");

        return bmp;
    }

    public static Drawable getIconForNotification(Context context, String packageName) {
        Drawable icon = null;
        try {
            icon = context.getPackageManager().getApplicationIcon(packageName);
        } catch (Exception ex){}

        return icon;
    }
}
