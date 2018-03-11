package com.whitewalkers.smartnotificationsmanager;

import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HistoryFragment extends Fragment implements TabLayout.OnTabSelectedListener{

    private View mView;
    private TabLayout tabLayout;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);
        this.createTabLayout();
        this.replaceFragment(new TabNotBlocked());
        return mView;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tab.select();
        int index = tab.getPosition();
        switch (index)
        {
            case 0:
                this.replaceFragment(new TabNotBlocked());
                break;
            case 1:
                this.replaceFragment(new TabBlocked());
                break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private void createTabLayout() {
        tabLayout = (TabLayout) mView.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("INBOX"));
        tabLayout.addTab(tabLayout.newTab().setText("BLOCKED"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.addOnTabSelectedListener(this);
    }

    private void replaceFragment(Fragment fragment) {
        if (mView.findViewById(R.id.tabLayout) != null) {
            getChildFragmentManager().beginTransaction().replace(R.id.tabFrameLayout, fragment).commit();
        }
    }
}
