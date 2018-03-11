package com.whitewalkers.smartnotificationsmanager;

import android.Manifest;
import android.app.Fragment;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class SettingFragment extends Fragment {

    private static final int REQUEST_WRITE_STORAGE = 112;
    private View mView;

    public SettingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_setting, container, false);
        Button exportDBButton = (Button) mView.findViewById(R.id.exportDBButton);
        exportDBButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler.getInstance(getActivity()).exportNotifications();
            }
        });

        Button clearNotificationsButton = (Button) mView.findViewById(R.id.clearNotificationsButton);
        clearNotificationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler.getInstance(getActivity()).clearNotificationsTable();
                Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
            }
        });

        Button clearAppSettingsButton = (Button) mView.findViewById(R.id.clearAppSettingsButton);
        clearAppSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHandler.getInstance(getActivity()).clearAppSettingsTable();
                Toast.makeText(getActivity(), "Done", Toast.LENGTH_SHORT).show();
            }
        });

        // get write to external storage permission for api >= marshmellow
        boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }

        return mView;
    }
}
