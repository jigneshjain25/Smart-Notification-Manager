package com.whitewalkers.smartnotificationsmanager;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_TITLE = "Title";
    private static final String ARG_DESCRIPTION = "Description";
    private static final String ARG_POSTTIME = "PostTime";
    private static final String ARG_ID = "Id";
    private static final String ARG_PACKAGENAME = "PackageName";
    private static final String ARG_APPNAME = "AppName";
    private static final String ARG_TAG = "Tag";
    private static final String ARG_ICONID = "IconId";
    private static final String ARG_BLOCKED = "Blocked";

    private String titleParam;
    private String descriptionParam;
    private String packageNameParam;
    private String appNameParam;
    private String tagParam;
    private int idParam;
    private long postTimeParam;
    private int iconIdParam;
    private int blockedParam;

    private OnFragmentInteractionListener mListener;

    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(SNMNotification notification) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, notification.getTitle());
        args.putString(ARG_DESCRIPTION, notification.getDescription());
        args.putLong(ARG_POSTTIME, notification.getPostTime());
        args.putInt(ARG_ID, notification.getId());
        args.putString(ARG_TAG, notification.getTag());
        args.putString(ARG_PACKAGENAME, notification.getPackageName());
        args.putString(ARG_APPNAME, notification.getAppName());
        args.putInt(ARG_ICONID, notification.getIconid());
        args.putInt(ARG_BLOCKED, notification.getBlocked());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            titleParam = getArguments().getString(ARG_TITLE);
            descriptionParam = getArguments().getString(ARG_DESCRIPTION);
            tagParam = getArguments().getString(ARG_TAG);
            packageNameParam = getArguments().getString(ARG_PACKAGENAME);
            appNameParam = getArguments().getString(ARG_APPNAME);
            idParam = getArguments().getInt(ARG_ID);
            postTimeParam = getArguments().getLong(ARG_POSTTIME);
            iconIdParam = getArguments().getInt(ARG_ICONID);
            blockedParam = getArguments().getInt(ARG_BLOCKED);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        TextView titleTextView = (TextView) view.findViewById(R.id.titleTextView);
        titleTextView.setText(appNameParam);
        //titleTextView.setTextColor((blockedParam == 1) ? Color.RED : Color.BLUE);

        TextView descriptionTextView = (TextView) view.findViewById(R.id.descriptionTextView);
        descriptionTextView.setText(titleParam + "\n" + descriptionParam);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);

        // to set small notification icons
        //Bitmap bmp = Helper.getIconForNotification(getActivity(), packageNameParam, iconIdParam);
        //if (bmp != null) imageView.setImageBitmap(bmp);

        Drawable icon = Helper.getIconForNotification(getActivity(), packageNameParam);
        if (icon != null) imageView.setImageDrawable(icon);

        view.setOnClickListener(this);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    */

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        Context ctx=getActivity();
        try {
            Intent i = ctx.getPackageManager().getLaunchIntentForPackage(packageNameParam);
            ctx.startActivity(i);
        } catch (Exception ex) {}
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
