package com.whitewalkers.smartnotificationsmanager;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

@TargetApi(19)
public class NLService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;
    public static boolean isNotificationAccessEnabled = false;

    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        Log.e("jj", "Inside nlservice create");
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.whitewalkers.snm.NOTIFICATION_LISTENER_SERVICE");

        try {
            Helper.initializeClassifier(new BayesianClassifier(getApplicationContext()));
        }
        catch (Exception ex) {
            Log.e("jj", "NLService helper initialize with classifer: " + ex);
            Helper.initializeClassifier(new NoobSpamClassifier());
        }

        registerReceiver(nlservicereciver,filter);
    }

    @Override
    public IBinder onBind(Intent intent) {
        IBinder mIBinder = super.onBind(intent);
        isNotificationAccessEnabled = true;
        return mIBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        boolean mOnUnbind = super.onUnbind(intent);
        isNotificationAccessEnabled = false;
        return mOnUnbind;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG,"**********  onNotificationPosted");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Intent i = new  Intent("com.whitewalkers.snm.NOTIFICATION_LISTENER");
        String title = sbn.getNotification().extras.getString("android.title");
        String description = sbn.getNotification().extras.getString("android.text");
        String packageName = sbn.getPackageName();
        String appName = Helper.getAppNameFromPackageName(packageName, getApplicationContext());
        String tag = sbn.getTag();
        int notificationId = sbn.getId();
        long postTime = sbn.getPostTime();
        int iconId = sbn.getNotification().extras.getInt(Notification.EXTRA_SMALL_ICON);
        //int iconId = sbn.getId();

        Log.e("jj", "" + appName + " " + iconId);

        i.putExtra("notification_title", title);
        i.putExtra("notification_description", description);
        i.putExtra("notification_package_name", packageName);
        i.putExtra("notification_app_name", appName);
        i.putExtra("notification_tag", tag);
        i.putExtra("notification_id", notificationId);
        i.putExtra("notification_post_time", postTime);
        i.putExtra("notification_icon_id", iconId);

        SNMNotification notification = new SNMNotification(notificationId,
                                            tag,
                                            postTime,
                                            packageName,
                                            appName,
                                            title,
                                            description,
                                            iconId,
                                            0);
        try {
            boolean isSpamNotification = Helper.isSpamNotification(notification);
            if (isSpamNotification) {
                //this.cancelNotification(sbn);
                notification.setBlocked(1);
                i.putExtra("notification_blocked", 1);
            }
        }
        catch (Exception ex) {
            Log.e("jj", "Exception classifying a notification as spam\n" + ex);
        }
        sendBroadcast(i);
        DatabaseHandler.getInstance(this).addNotification(notification);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText +"\t" + sbn.getPackageName());
        //Intent i = new  Intent("com.whitewalkers.snm.NOTIFICATION_LISTENER");
        //i.putExtra("notification_event","onNotificationRemoved :" + sbn.getPackageName() + "\n");
        //sendBroadcast(i);
    }

    private void cancelNotification(StatusBarNotification statusBarNotification) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            this.cancelNotification(statusBarNotification.getKey());
        } else{
            this.cancelNotification(statusBarNotification.getPackageName(), statusBarNotification.getTag(), statusBarNotification.getId());
        }
    }

    private void sendNotification (Notification notification) {
        NotificationManager nManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nManager.notify((int)System.currentTimeMillis(),notification);
    }

    class NLServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                Intent i1 = new  Intent("com.whitewalkers.snm.NOTIFICATION_LISTENER");
                i1.putExtra("notification_event","=====================");
                sendBroadcast(i1);
                int i=1;
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Intent i2 = new  Intent("com.whitewalkers.snm.NOTIFICATION_LISTENER");
                    i2.putExtra("notification_event",i +" " + sbn.getPackageName() + "\n");
                    sendBroadcast(i2);
                    i++;
                }
                Intent i3 = new  Intent("com.whitewalkers.snm.NOTIFICATION_LISTENER");
                i3.putExtra("notification_event","===== Notification List ====");
                sendBroadcast(i3);
            }
        }
    }
}
