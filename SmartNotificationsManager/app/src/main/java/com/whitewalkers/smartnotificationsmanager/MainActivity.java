package com.whitewalkers.smartnotificationsmanager;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        // check if we have notification access permissions
        if (!NLService.isNotificationAccessEnabled) {
            Intent intent=new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivityForResult(intent, 1);
        }
        else {
            Log.e("jj", "Inside else of main activity");
            this.replaceFragment(new HistoryFragment());
        }
    }

    public void buttonClicked (View view) {
        switch (view.getId()) {
            case R.id.HistoryButton:
                this.replaceFragment(new HistoryFragment());
                break;
            case R.id.WhiteListButton:
                this.replaceFragment(new WhiteListFragment());
                break;
            case R.id.SettingsButton:
                this.replaceFragment(new SettingFragment());
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.e("jj", "inside onActivityResult of mainActivity");
        super.onActivityResult(requestCode, resultCode, data);
        this.replaceFragment(new HistoryFragment());
    }

    private void replaceFragment(Fragment fragment) {
        if (findViewById(R.id.FrameLayout) != null) {
            getFragmentManager().beginTransaction().replace(R.id.FrameLayout, fragment).commit();
        }
    }
}
