package com.whitewalkers.snm;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ArticleFragment extends Fragment {

    public static final String MESSAGE  = "message";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_article, container, false);
        String message = this.getArguments().getString(MESSAGE);
        TextView messageLabel = (TextView) v.findViewById(R.id.MessageLabel);
        messageLabel.setText(message);
        return v;
    }
}