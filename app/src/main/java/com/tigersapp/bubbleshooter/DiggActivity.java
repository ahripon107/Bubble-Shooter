package com.tigersapp.bubbleshooter;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;

/**
 * Created by Ripon on 1/5/17.
 */

public class DiggActivity extends AppCompatActivity {

    Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            DiggActivity.this.close();
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreenMode();
        setContentView(R.layout.digg_main);
        initView();
    }

    public void setFullscreenMode() {
        requestWindowFeature(2);
        Window window = getWindow();
        window.setFlags(1024, 1024);
        window.setFlags(512, 512);
    }

    private void initView() {
        findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DiggActivity.this.close();
            }
        });
        findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DiggActivity.this.gotoMarket();
            }
        });
        if (getIntent().getBooleanExtra(DiggConstant.IS_AUTO_CLOSE_KEY, true)) {
            this.mHandler.sendEmptyMessageDelayed(0, 8000);
        }
    }

    private void close() {
        finish();
    }

    private void gotoMarket() {
        try {
            Uri uri = Uri.parse("market://details?id=com.spaceV.bubbleshooter");
            Intent it = new Intent();
            it.setData(uri);
            startActivity(it);
            close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
