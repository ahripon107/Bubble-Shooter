package com.tigersapp.bubbleshooter.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Ripon on 1/4/17.
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textView = new TextView(getApplicationContext());
        StringBuilder buf = new StringBuilder("original frozen bubble:\n");
        buf.append("guillaume cottenceau\n");
        buf.append("alexis younes\n");
        buf.append("amaury amblard-ladurantie\n");
        buf.append("matthias le bidan\n");
        buf.append("\n");
        buf.append("java version:");
        buf.append("glenn sanson\n");
        buf.append("\n");
        buf.append("android port:");
        buf.append("aleksander fedorynski\n");
        buf.append("\n");
        buf.append("android port source code is available at:http://code.google.com/p/frozenbubbleandroid\n");
        buf.append("\n");
        buf.append("bubble shooter pro source code is available at:http://code.google.com/p/bubble-shoot\n");
        textView.setText(buf.toString());
        textView.setGravity(3);
        textView.setPadding(10, 20, 10, 20);
        textView.setTextSize(16.0f);
        addContentView(textView, new ActionBar.LayoutParams(-1, -2));
    }
}
