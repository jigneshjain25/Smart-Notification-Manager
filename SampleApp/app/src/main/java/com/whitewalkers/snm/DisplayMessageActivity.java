package com.whitewalkers.snm;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Intent intent = getIntent();
        if (intent != null) {
            String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
            if (findViewById(R.id.FrameLayout) != null) {
                if (savedInstanceState != null) {
                    return;
                }
                ArticleFragment article = new ArticleFragment();
                Bundle args = new Bundle();
                args.putString(ArticleFragment.MESSAGE, message);
                article.setArguments(args);
                getFragmentManager().beginTransaction().add(R.id.FrameLayout, article).commit();
            }
        }
    }

}
