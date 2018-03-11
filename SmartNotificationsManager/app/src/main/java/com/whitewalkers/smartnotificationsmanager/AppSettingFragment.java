package com.whitewalkers.smartnotificationsmanager;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AppSettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AppSettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AppSettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    static final String PACKAGE_NAME = "packageName";

    // TODO: Rename and change types of parameters
    private String mPackageName;

    private OnFragmentInteractionListener mListener;

    public Switch alwaysBlockToggle;
    public Switch alwaysShowToggle;

    private View mView;

    public AppSettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AppSettingFragment newInstance(String param1, String param2) {
        AppSettingFragment fragment = new AppSettingFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPackageName = getArguments().getString(PACKAGE_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.app_setting, container, false);
        this.setAppSettings();
        return mView;
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

    private void setAppSettings()
    {
        this.alwaysBlockToggle = (Switch) mView.findViewById(R.id.switch2);
        this.alwaysShowToggle = (Switch) mView.findViewById(R.id.switch1);

        AppSetting appSetting = DatabaseHandler.getInstance(getActivity()).getAppSetting(this.mPackageName);
        this.alwaysBlockToggle.setChecked(appSetting.isAlwaysBlock());
        this.alwaysShowToggle.setChecked(appSetting.isAlwaysShow());

        this.alwaysBlockToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alwaysShowToggle.setChecked(false);
                }
                DatabaseHandler.getInstance(getActivity()).setAppSetting(mPackageName, alwaysBlockToggle.isChecked(), alwaysShowToggle.isChecked());
            }
        });
        this.alwaysShowToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    alwaysBlockToggle.setChecked(false);
                }
                DatabaseHandler.getInstance(getActivity()).setAppSetting(mPackageName, alwaysBlockToggle.isChecked(), alwaysShowToggle.isChecked());
            }
        });
    }
}
