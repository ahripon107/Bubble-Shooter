package com.tigersapp.bubbleshooter.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ViewUtils;
import android.telephony.TelephonyManager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.InterstitialAd;
import com.tigersapp.bubbleshooter.R;

/**
 * Created by Ripon on 1/5/17.
 */

public class LevelSelectorActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {
    private static final int NUM_COLUMNS = 3;
    private InterstitialAd interstitialAd;
    private GridView localGridView;
    private Vibrator localVibrator;
    private AppCompatActivity mContext;
    private LevelAdapter mLevelAdapter;
    private SharedPreferences sp;

    class LevelAdapter extends ArrayAdapter<LevelInfo> {
        private AppCompatActivity mContext;
        private final SparseArray<LevelInfo> mLevelInfos = new SparseArray();

        public LevelAdapter(AppCompatActivity pContext, int resource, int textViewResourceId) {
            super(pContext, resource, textViewResourceId);
            this.mContext = pContext;
            if (LevelSelectorActivity.this.sp == null) {
                LevelSelectorActivity.this.sp = LevelSelectorActivity.this.getSharedPreferences(BubbleShooterActivity.PREFS_NAME, 0);
            }
            int maxLevel = LevelSelectorActivity.this.sp.getInt("Unlock_level", 0);
            String imei = ((TelephonyManager) LevelSelectorActivity.this.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            for (int i = 1; i <= 640; i++) {
                boolean z;
                SparseArray sparseArray = this.mLevelInfos;
                if (i <= maxLevel + 1) {
                    z = true;
                } else {
                    z = false;
                }
                sparseArray.put(i, new LevelInfo(i, z));
            }
        }

        private View populateView(View paramView, int paramInt) {
            LevelInfo localLevelInfo = getItem(paramInt);
            TextView localTextView = (TextView) paramView.findViewById(R.id.tv_levelselector_level);
            localTextView.setBackgroundResource(R.drawable.levelselector_level);
            localTextView.setText(String.valueOf(localLevelInfo.getLevel()));
            if (localLevelInfo.isUnlocked()) {
                localTextView.setEnabled(true);
                localTextView.setBackgroundResource(R.drawable.unlocked);
            } else {
                localTextView.setEnabled(false);
                localTextView.setBackgroundResource(R.drawable.locked);
                localTextView.setText("");
            }
            return paramView;
        }

        public int getCount() {
            return this.mLevelInfos.size();
        }

        public LevelInfo getItem(int paramInt) {
            return (LevelInfo) this.mLevelInfos.get(paramInt + 1);
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            return populateView(super.getView(position, convertView, parent), position);
        }

        public void notifyDataSetChanged() {
            this.mLevelInfos.clear();
            super.notifyDataSetChanged();
        }
    }

    public class LevelInfo {
        private int mLevel;
        private boolean mUnlocked;

        public LevelInfo(int level, boolean bool) {
            this.mLevel = level;
            this.mUnlocked = bool;
        }

        public int getLevel() {
            return this.mLevel;
        }

        public boolean isUnlocked() {
            return this.mUnlocked;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        boolean bool = requestWindowFeature(1);
        setContentView(R.layout.levelselector);
        this.localVibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        this.localGridView = (GridView) findViewById(R.id.grid_levelselector);
        this.localGridView.setOnItemClickListener(this);
        this.localGridView.setOnItemSelectedListener(this);
        this.mLevelAdapter = new LevelAdapter(this, R.layout.levelselector_level, R.id.tv_levelselector_level);
        this.localGridView.setAdapter(this.mLevelAdapter);
        this.sp = getSharedPreferences(BubbleShooterActivity.PREFS_NAME, 0);
    }

    private void showLevelNotYetUnlockedToast() {
        View localView = ViewUtils.inflate(this, R.layout.levelselector_toast_level_locked);
        Toast localToast = new Toast(this);
        localToast.setGravity(16, 0, 0);
        localToast.setDuration(0);
        localToast.setView(localView);
        localToast.show();
    }

    public void onItemClick(AdapterView<?> adapterView, View paramView, int paramInt, long paramLong) {
        paramView.setSelected(true);
        LevelInfo localLevelInfo = this.mLevelAdapter.getItem(paramInt);
        if (localLevelInfo.isUnlocked()) {
            Animation animation = AnimationUtils.loadAnimation(this.mContext, R.anim.levelselector_level_sel);
            paramView.startAnimation(animation);
            Intent i = new Intent(this, BubbleShooterActivity.class);
            this.sp.edit().putInt("level", localLevelInfo.mLevel - 1).commit();
            startActivity(i);
            finish();
            paramView.startAnimation(animation);
            return;
        }
        this.localVibrator.vibrate(100);
        paramView.startAnimation(AnimationUtils.loadAnimation(this.mContext, R.anim.levelselector_level_locked));
        showLevelNotYetUnlockedToast();
    }

    public void onItemSelected(AdapterView<?> adapterView, View arg1, int arg2, long arg3) {
    }

    public void onNothingSelected(AdapterView<?> adapterView) {
    }
}