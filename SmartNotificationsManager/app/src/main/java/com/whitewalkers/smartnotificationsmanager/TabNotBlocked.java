package com.whitewalkers.smartnotificationsmanager;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

public class TabNotBlocked extends Fragment {

    private NotBlockedNotificationReceiver notBlockedNotificationReceiver;
    private View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.notBlockedNotificationReceiver = new NotBlockedNotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.whitewalkers.snm.NOTIFICATION_LISTENER");
        getActivity().registerReceiver(this.notBlockedNotificationReceiver, filter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.tab_notblocked, container, false);
        this.addAllNotifications(DatabaseHandler.getInstance(getActivity()).getAllNotifications());
        return mView;
    }

    private void addAllNotifications(List<SNMNotification> notifications)
    {
        LinearLayout container = (LinearLayout) mView.findViewById(R.id.tabNotBlockedLinearLayout);
        FragmentTransaction txn = getChildFragmentManager().beginTransaction();
        for (SNMNotification notification : notifications)
        {
            if (notification.getBlocked() == 0)
            {
                LinearLayout childLayout = new LinearLayout(mView.getContext());
                childLayout.setId(mView.generateViewId());
                container.addView(childLayout);
                NotificationFragment notificationFragment = NotificationFragment.newInstance(notification);
                txn.add(childLayout.getId(), notificationFragment);
            }
        }

        txn.commit();
    }

    class NotBlockedNotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try{
                SNMNotification notification = new SNMNotification(intent.getIntExtra("notification_id", 0),
                        intent.getStringExtra("notification_tag"),
                        intent.getLongExtra("notification_post_time", 0),
                        intent.getStringExtra("notification_package_name"),
                        intent.getStringExtra("notification_app_name"),
                        intent.getStringExtra("notification_title"),
                        intent.getStringExtra("notification_description"),
                        intent.getIntExtra("notification_icon_id", 0),
                        intent.getIntExtra("notification_blocked", 0));

                if (notification.getBlocked() == 0) {
                    NotificationFragment notificationFragment = NotificationFragment.newInstance(notification);

                    LinearLayout container = (LinearLayout) mView.findViewById(R.id.tabNotBlockedLinearLayout);
                    LinearLayout childLayout = new LinearLayout(mView.getContext());
                    childLayout.setId(mView.generateViewId());
                    container.addView(childLayout, 0);
                    FragmentTransaction txn = getChildFragmentManager().beginTransaction();
                    txn.add(childLayout.getId(), notificationFragment);
                    txn.commit();
                }
            } catch (Exception ex){
                Log.e("jj", "TabNotBlocked.java onReceive: \n" + ex);
            }
        }
    }
}
