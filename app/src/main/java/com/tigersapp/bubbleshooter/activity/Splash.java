package com.tigersapp.bubbleshooter.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.chartboost.sdk.ChartboostDelegate;
import com.chartboost.sdk.Libraries.CBLogging;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.tigersapp.bubbleshooter.DiggAPI;
import com.tigersapp.bubbleshooter.R;
import com.tigersapp.bubbleshooter.arcade.ScoreManager;

import java.util.Locale;

/**
 * Created by Ripon on 1/5/17.
 */

public class Splash extends FragmentActivity implements View.OnClickListener {
    private static final String LOG_TAG = "DfpInterstitialSample";
    private static final String TAG = "Splash";
    private Button appPointButton;
    private Chartboost cb;
    private ChartboostDelegate delegate = new ChartboostDelegate() {
        public boolean shouldRequestInterstitial(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("SHOULD REQUEST INTERSTITIAL '");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
            return true;
        }

        public boolean shouldDisplayInterstitial(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("SHOULD DISPLAY INTERSTITIAL '");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
            return true;
        }

        public void didCacheInterstitial(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID CACHE INTERSTITIAL '");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
        }

        public void didFailToLoadInterstitial(String location, CBImpressionError error) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID FAIL TO LOAD INTERSTITIAL '");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).append(" Error: ").append(error.name()).toString());
        }

        public void didDismissInterstitial(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID DISMISS INTERSTITIAL: ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
        }

        public void didCloseInterstitial(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID CLOSE INTERSTITIAL: ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
        }

        public void didClickInterstitial(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID CLICK INTERSTITIAL: ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
        }

        public void didDisplayInterstitial(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID DISPLAY INTERSTITIAL: ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
        }

        public boolean shouldRequestMoreApps(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("SHOULD REQUEST MORE APPS: ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
            return true;
        }

        public boolean shouldDisplayMoreApps(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("SHOULD DISPLAY MORE APPS: ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
            return true;
        }

        public void didFailToLoadMoreApps(String location, CBImpressionError error) {
            if (Splash.this.isBack) {
                Log.i(Splash.TAG, "DID FAIL TO LOAD MOREAPPS " + (location != null ? location : "null") + " Error: " + "isBack");
                Splash.this.finish();
            }
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID FAIL TO LOAD MOREAPPS ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).append(" Error: ").append(error.name()).toString());
        }

        public void didCacheMoreApps(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID CACHE MORE APPS: ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
        }

        public void didDismissMoreApps(String location) {
            if (Splash.this.isBack) {
                Splash.this.isBack = false;
                Splash.this.finish();
            }
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID DISMISS MORE APPS ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
        }

        public void didCloseMoreApps(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID CLOSE MORE APPS: ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
        }

        public void didClickMoreApps(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID CLICK MORE APPS: ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
        }

        public void didDisplayMoreApps(String location) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID DISPLAY MORE APPS: ");
            if (location == null) {
                location = "null";
            }
            Log.i(str, stringBuilder.append(location).toString());
        }

        public void didFailToRecordClick(String uri, CBClickError error) {
            String str = Splash.TAG;
            StringBuilder stringBuilder = new StringBuilder("DID FAILED TO RECORD CLICK ");
            if (uri == null) {
                uri = "null";
            }
            Log.i(str, stringBuilder.append(uri).append(", error: ").append(error.name()).toString());
        }

        public boolean shouldDisplayRewardedVideo(String location) {
            String str = Splash.TAG;
            String str2 = "SHOULD DISPLAY REWARDED VIDEO: '%s'";
            Object[] objArr = new Object[1];
            if (location == null) {
                location = "null";
            }
            objArr[0] = location;
            Log.i(str, String.format(str2, objArr));
            return true;
        }

        public void didCacheRewardedVideo(String location) {
            String str = Splash.TAG;
            String str2 = "DID CACHE REWARDED VIDEO: '%s'";
            Object[] objArr = new Object[1];
            if (location == null) {
                location = "null";
            }
            objArr[0] = location;
            Log.i(str, String.format(str2, objArr));
        }

        public void didFailToLoadRewardedVideo(String location, CBImpressionError error) {
            String str = Splash.TAG;
            String str2 = "DID FAIL TO LOAD REWARDED VIDEO: '%s', Error:  %s";
            Object[] objArr = new Object[2];
            if (location == null) {
                location = "null";
            }
            objArr[0] = location;
            objArr[1] = error.name();
            Log.i(str, String.format(str2, objArr));
        }

        public void didDismissRewardedVideo(String location) {
            String str = Splash.TAG;
            String str2 = "DID DISMISS REWARDED VIDEO '%s'";
            Object[] objArr = new Object[1];
            if (location == null) {
                location = "null";
            }
            objArr[0] = location;
            Log.i(str, String.format(str2, objArr));
        }

        public void didCloseRewardedVideo(String location) {
            String str = Splash.TAG;
            String str2 = "DID CLOSE REWARDED VIDEO '%s'";
            Object[] objArr = new Object[1];
            if (location == null) {
                location = "null";
            }
            objArr[0] = location;
            Log.i(str, String.format(str2, objArr));
        }

        public void didClickRewardedVideo(String location) {
            String str = Splash.TAG;
            String str2 = "DID CLICK REWARDED VIDEO '%s'";
            Object[] objArr = new Object[1];
            if (location == null) {
                location = "null";
            }
            objArr[0] = location;
            Log.i(str, String.format(str2, objArr));
        }

        public void didCompleteRewardedVideo(String location, int reward) {
            String str = Splash.TAG;
            String str2 = "DID COMPLETE REWARDED VIDEO '%s' FOR REWARD %d";
            Object[] objArr = new Object[2];
            if (location == null) {
                location = "null";
            }
            objArr[0] = location;
            objArr[1] = Integer.valueOf(reward);
            Log.i(str, String.format(str2, objArr));
        }

        public void didDisplayRewardedVideo(String location) {
            Log.i(Splash.TAG, String.format("DID DISPLAY REWARDED VIDEO '%s' FOR REWARD", new Object[]{location}));
        }

        public void willDisplayVideo(String location) {
            Log.i(Splash.TAG, String.format("WILL DISPLAY VIDEO '%s", new Object[]{location}));
        }
    };
    private Button helpButton;
    private InterstitialAd interstitialAd;
    private boolean isBack;
    public boolean isDownloadZipFile = false;
    private Button moreAppButton;
    private Button newButton;
    private Button resumeButton;
    private Button selLevelButton;
    private SharedPreferences sp;
    private int unLockLevel;

    private class InterstitialAdListener extends AdListener {
        private InterstitialAdListener() {
        }

        public void onAdLoaded() {
            Log.d(Splash.LOG_TAG, "onAdLoaded");
        }

        public void onAdFailedToLoad(int errorCode) {
            Log.d(Splash.LOG_TAG, String.format("onAdFailedToLoad (%s)", new Object[]{getErrorReason(errorCode)}));
        }

        public void onAdOpened() {
            Log.d(Splash.LOG_TAG, "onAdOpened");
        }

        public void onAdClosed() {
            Log.d(Splash.LOG_TAG, "onAdClosed");
        }

        public void onAdLeftApplication() {
            Log.d(Splash.LOG_TAG, "onAdLeftApplication");
        }

        private String getErrorReason(int errorCode) {
            switch (errorCode) {
                case 0:
                    return "Internal error";
                case 1:
                    return "Invalid request";
                case 2:
                    return "Network Error";
                case 3:
                    return "No fill";
                default:
                    return "Unknown error";
            }
        }
    }

    @Override
    public void onCreate(Bundle icicle) {
        int i;
        super.onCreate(icicle);
        requestWindowFeature(1);
        getWindow().setFlags(128, 128);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.splash);
        Chartboost.startWithAppId(this, getResources().getString(R.string.appId), getResources().getString(R.string.appSignature));
        Chartboost.setLoggingLevel(CBLogging.Level.ALL);
        Chartboost.setDelegate(this.delegate);
        Chartboost.onCreate(this);
        EasyTracker.getInstance().activityStart(this);
        AppEventsLogger.activateApp(this, getResources().getString(R.string.app_id));
        this.resumeButton = (Button) findViewById(R.id.resumeGameButton);
        this.resumeButton.setOnClickListener(this);
        this.newButton = (Button) findViewById(R.id.newGameButton);
        this.newButton.setOnClickListener(this);
        this.selLevelButton = (Button) findViewById(R.id.selectLevelButton);
        this.selLevelButton.setOnClickListener(this);
        this.moreAppButton = (Button) findViewById(R.id.moreAppButton);
        this.moreAppButton.setOnClickListener(this);
        this.helpButton = (Button) findViewById(R.id.helpButton);
        this.helpButton.setOnClickListener(this);
        this.appPointButton = (Button) findViewById(R.id.appPointButton);
        this.appPointButton.setOnClickListener(this);
        Button button = this.appPointButton;
        if ("CN".equals(Locale.getDefault().getCountry())) {
            i = 0;
        } else {
            i = 8;
        }
        button.setVisibility(i);
        this.appPointButton.setVisibility(8);
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/CHEESEBU.TTF");
        this.resumeButton.setTypeface(face);
        this.newButton.setTypeface(face);
        this.selLevelButton.setTypeface(face);
        this.moreAppButton.setTypeface(face);
        ((Button) findViewById(R.id.btnArcade)).setTypeface(face);
        findViewById(R.id.btnArcade).setOnClickListener(this);
        this.sp = getSharedPreferences(BubbleShooterActivity.PREFS_NAME, 0);
        this.unLockLevel = this.sp.getInt("Unlock_level", 0);
        int level = this.sp.getInt("level", 0);
        if (level > this.unLockLevel) {
            this.unLockLevel = level;
            this.sp.edit().putInt("Unlock_level", this.unLockLevel).commit();
        }
        GameConfig.getInstance().init(this);
        ScoreManager.getInstance().init(this);
        DiggAPI.start(this, getApplicationContext());
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

    public void onStart() {
        super.onStart();
        Chartboost.onStart(this);
    }

    public void onBackPressed() {
        if (!Chartboost.onBackPressed()) {
            super.onBackPressed();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        Chartboost.onDestroy(this);
    }

    protected void onPause() {
        super.onPause();
        Chartboost.onPause(this);
    }

    protected void onResume() {
        super.onResume();
        Chartboost.onResume(this);
    }

    protected void onStop() {
        EasyTracker.getInstance().activityStart(this);
        super.onStop();
        Chartboost.onStop(this);
    }

    public void onLoadButtonClick(View view) {
        String toastStr = "Loading Interstitial";
        if (Chartboost.hasInterstitial(CBLocation.LOCATION_LEADERBOARD)) {
            toastStr = "Loading Interstitial From Cache";
        }
        Log.i(TAG, toastStr);
        Chartboost.showInterstitial(CBLocation.LOCATION_LEADERBOARD);
    }

    public void onMoreButtonClick(View view) {
        String toastStr = "Loading More Apps";
        if (Chartboost.hasMoreApps(CBLocation.LOCATION_GAME_SCREEN)) {
            toastStr = "Loading More Apps From Cache";
        }
        Log.i(TAG, toastStr);
        Chartboost.showMoreApps(CBLocation.LOCATION_GAME_SCREEN);
    }

    public void onPreloadClick(View v) {
        Log.i(TAG, "Preloading Interstitial Ad");
        Chartboost.cacheInterstitial(CBLocation.LOCATION_LEADERBOARD);
    }

    public void onPreloadMoreAppsClick(View v) {
        Log.i(TAG, "Preloading More apps Ad");
        Chartboost.cacheMoreApps(CBLocation.LOCATION_GAME_SCREEN);
    }

    public void onLoadVideoClick(View view) {
        String toastStr = "Loading Rewarded Interstitial";
        if (Chartboost.hasRewardedVideo(CBLocation.LOCATION_ACHIEVEMENTS)) {
            toastStr = "Loading Rewarded Interstitial From Cache";
        }
        Log.i(TAG, toastStr);
        Chartboost.showRewardedVideo(CBLocation.LOCATION_ACHIEVEMENTS);
    }

    public void onPreloadVideoClick(View v) {
        Log.i(TAG, "Preloading Rewarded Interstitial Ad");
        Chartboost.cacheRewardedVideo(CBLocation.LOCATION_ACHIEVEMENTS);
    }

    public void onClick(View v) {
        Intent i = null;
        switch (v.getId()) {
            case R.id.resumeGameButton:
                i = new Intent(this, BubbleShooterActivity.class);
                break;
            case R.id.newGameButton:
                i = new Intent(this, BubbleShooterActivity.class);
                this.sp.edit().putInt("level", 0).commit();
                break;
            case R.id.selectLevelButton:
                i = new Intent(this, LevelSelectorActivity.class);
                break;
            case R.id.btnArcade:
                i = new Intent(this, BubbleArcadeActivity.class);
                break;
            case R.id.moreAppButton:
                String toastStr = "Loading More Apps";
                if (Chartboost.hasMoreApps(CBLocation.LOCATION_GAME_SCREEN)) {
                    toastStr = "Loading More Apps From Cache";
                }
                Log.i(TAG, toastStr);
                Chartboost.showMoreApps(CBLocation.LOCATION_GAME_SCREEN);
                break;
        }
        if (i != null) {
            startActivity(i);
        }
    }

    public void finish() {
        super.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }
}